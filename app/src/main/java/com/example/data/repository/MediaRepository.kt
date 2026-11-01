package com.example.data.repository

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import androidx.documentfile.provider.DocumentFile
import com.example.data.model.MediaFilter
import com.example.data.model.StatusItem
import com.example.data.model.StatusSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.util.Locale

class MediaRepository(private val context: Context) {

    companion object {
        private const val TAG = "MediaRepository"
        const val DCIM_STATUS_SAVER = "DCIM/Status Saver"

        const val WHATSAPP_PACKAGE = "com.whatsapp"
        const val WHATSAPP_BUSINESS_PACKAGE = "com.whatsapp.w4b"

        const val PATH_WHATSAPP = "Android/media/com.whatsapp/WhatsApp/Media/.Statuses"
        const val PATH_BUSINESS = "Android/media/com.whatsapp.w4b/WhatsApp Business/Media/.Statuses"
    }

    /**
     * Checks if a granted tree URI permission is currently valid and active in ContentResolver.
     */
    fun hasPersistedUriPermission(uri: Uri): Boolean {
        val persisted = context.contentResolver.persistedUriPermissions
        return persisted.any { it.uri == uri && it.isReadPermission }
    }

    /**
     * Loads status media from a SAF Tree URI.
     */
    suspend fun loadStatusesFromTree(
        treeUri: Uri,
        source: StatusSource,
        savedVaultItems: List<StatusItem>
    ): List<StatusItem> = withContext(Dispatchers.IO) {
        val results = mutableListOf<StatusItem>()
        try {
            val rootDoc = DocumentFile.fromTreeUri(context, treeUri) ?: return@withContext emptyList()
            if (!rootDoc.exists() || !rootDoc.isDirectory) return@withContext emptyList()

            val files = rootDoc.listFiles()
            val savedSignatures = savedVaultItems.map { it.name.lowercase(Locale.ROOT) to it.size }.toSet()

            for (file in files) {
                if (file.isDirectory) continue
                val name = file.name ?: continue
                if (name.startsWith(".nomedia", ignoreCase = true)) continue

                val lowerName = name.lowercase(Locale.ROOT)
                val isImage = lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg") || lowerName.endsWith(".png")
                val isVideo = lowerName.endsWith(".mp4")

                if (!isImage && !isVideo) continue

                val mime = if (isVideo) "video/mp4" else if (lowerName.endsWith(".png")) "image/png" else "image/jpeg"
                val size = file.length()
                val dateMod = file.lastModified()

                // Check if already in vault by size or original name contained
                val isSaved = savedSignatures.any { (savedName, savedSize) ->
                    savedSize == size && size > 0 || savedName.contains(name.substringBeforeLast(".").lowercase(Locale.ROOT))
                }

                var durationMs = 0L
                if (isVideo) {
                    try {
                        val retriever = android.media.MediaMetadataRetriever()
                        retriever.setDataSource(context, file.uri)
                        val timeStr = retriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION)
                        durationMs = timeStr?.toLongOrNull() ?: 0L
                        retriever.release()
                    } catch (e: Exception) {
                        // ignore
                    }
                }

                results.add(
                    StatusItem(
                        id = file.uri.toString(),
                        uri = file.uri,
                        name = name,
                        size = size,
                        dateModified = if (dateMod > 0) dateMod else System.currentTimeMillis(),
                        isVideo = isVideo,
                        mimeType = mime,
                        source = source,
                        isSaved = isSaved,
                        durationMs = durationMs
                    )
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load statuses from tree $treeUri", e)
        }

        results.sortedByDescending { it.dateModified }
    }

    /**
     * Scans and loads all media previously saved into DCIM/Status Saver.
     */
    suspend fun loadSavedVaultStatuses(): List<StatusItem> = withContext(Dispatchers.IO) {
        val vaultItems = mutableListOf<StatusItem>()
        val resolver = context.contentResolver

        // 1. Fetch images from MediaStore in DCIM/Status Saver
        try {
            val imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DATE_MODIFIED,
                MediaStore.Images.Media.MIME_TYPE
            )
            val selection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                "${MediaStore.Images.Media.RELATIVE_PATH} LIKE ?"
            } else {
                "${MediaStore.Images.Media.DATA} LIKE ?"
            }
            val selectionArgs = arrayOf("%$DCIM_STATUS_SAVER%")

            resolver.query(
                imageUri,
                projection,
                selection,
                selectionArgs,
                "${MediaStore.Images.Media.DATE_MODIFIED} DESC"
            )?.use { cursor ->
                val idCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val nameCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val sizeCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
                val dateCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED)
                val mimeCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idCol)
                    val contentUri = ContentUris.withAppendedId(imageUri, id)
                    val name = cursor.getString(nameCol) ?: "STATUS_IMG.jpg"
                    val size = cursor.getLong(sizeCol)
                    val dateSec = cursor.getLong(dateCol)
                    val mime = cursor.getString(mimeCol) ?: "image/jpeg"

                    vaultItems.add(
                        StatusItem(
                            id = contentUri.toString(),
                            uri = contentUri,
                            name = name,
                            size = size,
                            dateModified = dateSec * 1000L,
                            isVideo = false,
                            mimeType = mime,
                            source = StatusSource.SAVED_VAULT,
                            isSaved = true
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error querying images from vault", e)
        }

        // 2. Fetch videos from MediaStore in DCIM/Status Saver
        try {
            val videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATE_MODIFIED,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.DURATION
            )
            val selection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                "${MediaStore.Video.Media.RELATIVE_PATH} LIKE ?"
            } else {
                "${MediaStore.Video.Media.DATA} LIKE ?"
            }
            val selectionArgs = arrayOf("%$DCIM_STATUS_SAVER%")

            resolver.query(
                videoUri,
                projection,
                selection,
                selectionArgs,
                "${MediaStore.Video.Media.DATE_MODIFIED} DESC"
            )?.use { cursor ->
                val idCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                val nameCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
                val sizeCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
                val dateCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED)
                val mimeCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)
                val durationCol = cursor.getColumnIndex(MediaStore.Video.Media.DURATION)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idCol)
                    val contentUri = ContentUris.withAppendedId(videoUri, id)
                    val name = cursor.getString(nameCol) ?: "STATUS_VID.mp4"
                    val size = cursor.getLong(sizeCol)
                    val dateSec = cursor.getLong(dateCol)
                    val mime = cursor.getString(mimeCol) ?: "video/mp4"
                    val durationMs = if (durationCol != -1) cursor.getLong(durationCol) else 0L

                    vaultItems.add(
                        StatusItem(
                            id = contentUri.toString(),
                            uri = contentUri,
                            name = name,
                            size = size,
                            dateModified = dateSec * 1000L,
                            isVideo = true,
                            mimeType = mime,
                            source = StatusSource.SAVED_VAULT,
                            isSaved = true,
                            durationMs = durationMs
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error querying videos from vault", e)
        }

        // Fallback for direct storage folder if legacy file exists
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            try {
                val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Status Saver")
                if (dir.exists() && dir.isDirectory) {
                    val diskFiles = dir.listFiles() ?: emptyArray()
                    for (f in diskFiles) {
                        val fileUri = Uri.fromFile(f)
                        if (vaultItems.none { it.name == f.name }) {
                            val isVid = f.name.endsWith(".mp4", ignoreCase = true)
                            vaultItems.add(
                                StatusItem(
                                    id = fileUri.toString(),
                                    uri = fileUri,
                                    name = f.name,
                                    size = f.length(),
                                    dateModified = f.lastModified(),
                                    isVideo = isVid,
                                    mimeType = if (isVid) "video/mp4" else "image/jpeg",
                                    source = StatusSource.SAVED_VAULT,
                                    isSaved = true
                                )
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.w(TAG, "Direct disk inspection fallback notice: ${e.message}")
            }
        }

        vaultItems.sortedByDescending { it.dateModified }
    }

    /**
     * Saves a status from SAF or external URI into MediaStore under "DCIM/Status Saver".
     * Follows the exact required MediaStore attributes with IS_PENDING = 1 then 0.
     */
    suspend fun saveStatusToGallery(item: StatusItem): Result<Uri> = withContext(Dispatchers.IO) {
        val resolver = context.contentResolver
        val timestamp = System.currentTimeMillis()
        val extension = if (item.isVideo) "mp4" else "jpg"
        val displayName = if (item.isVideo) "STATUS_VID_$timestamp.mp4" else "STATUS_IMG_$timestamp.jpg"
        val mimeType = if (item.isVideo) "video/mp4" else "image/jpeg"

        var targetUri: Uri? = null

        try {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
                put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.MediaColumns.RELATIVE_PATH, DCIM_STATUS_SAVER)
                    put(MediaStore.MediaColumns.IS_PENDING, 1)
                }
            }

            val tableUri = if (item.isVideo) {
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }

            targetUri = resolver.insert(tableUri, contentValues)
                ?: return@withContext Result.failure(IllegalStateException("Could not insert MediaStore record"))

            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null

            try {
                inputStream = resolver.openInputStream(item.uri)
                    ?: return@withContext Result.failure(IllegalStateException("Failed to open source stream: ${item.uri}"))
                outputStream = resolver.openOutputStream(targetUri)
                    ?: return@withContext Result.failure(IllegalStateException("Failed to open destination stream: $targetUri"))

                val buffer = ByteArray(8192)
                var bytesRead: Int
                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }
                outputStream.flush()
            } finally {
                inputStream?.close()
                outputStream?.close()
            }

            // Remove IS_PENDING flag when copy succeeds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                resolver.update(targetUri, contentValues, null, null)
            }

            Log.i(TAG, "Successfully saved status to: $targetUri ($displayName)")
            Result.success(targetUri)
        } catch (e: Exception) {
            Log.e(TAG, "Error saving status to gallery", e)
            targetUri?.let {
                try {
                    resolver.delete(it, null, null)
                } catch (delEx: Exception) {
                    Log.w(TAG, "Failed to delete partial MediaStore entry", delEx)
                }
            }
            Result.failure(e)
        }
    }

    /**
     * Deletes an item from the saved vault.
     */
    suspend fun deleteSavedStatus(item: StatusItem): Boolean = withContext(Dispatchers.IO) {
        try {
            val rows = context.contentResolver.delete(item.uri, null, null)
            rows > 0
        } catch (e: Exception) {
            Log.e(TAG, "Failed to delete saved item ${item.uri}", e)
            false
        }
    }

    /**
     * Creates a share intent for a media item.
     */
    fun shareMediaItem(item: StatusItem) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = item.mimeType
                putExtra(Intent.EXTRA_STREAM, item.uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            val chooser = Intent.createChooser(shareIntent, "Share Status via").apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(chooser)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to share media item", e)
        }
    }
}
