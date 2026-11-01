package com.example.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.documentfile.provider.DocumentFile
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.MainActivity
import com.example.R
import com.example.data.datastore.PreferencesManager
import com.example.data.model.StatusItem
import com.example.data.model.StatusSource
import com.example.data.repository.MediaRepository
import kotlinx.coroutines.flow.first
import java.util.Locale

class AutoSaveWorker(
    private val appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        private const val TAG = "AutoSaveWorker"
        const val WORK_NAME = "periodic_status_auto_save"
        const val CHANNEL_ID = "status_auto_save_channel"
        const val NOTIFICATION_ID = 2026
    }

    override suspend fun doWork(): Result {
        Log.d(TAG, "Starting AutoSaveWorker execution...")

        val prefs = PreferencesManager(appContext)
        val isEnabled = prefs.isAutoSaveEnabled.first()
        if (!isEnabled) {
            Log.d(TAG, "Auto-Save is disabled in settings. Skipping execution.")
            return Result.success()
        }

        val repository = MediaRepository(appContext)
        val waUriStr = prefs.whatsappTreeUri.first()
        val bizUriStr = prefs.businessTreeUri.first()

        if (waUriStr.isNullOrEmpty() && bizUriStr.isNullOrEmpty()) {
            Log.w(TAG, "No folder permissions granted yet. Skipping.")
            return Result.success()
        }

        // Fetch items already saved in DCIM/Status Saver
        val existingVault = repository.loadSavedVaultStatuses()
        val existingVaultSignatures = existingVault.map { it.name.lowercase(Locale.ROOT) to it.size }.toSet()
        val processedSignatures = prefs.processedFileSignatures.first()

        var newlySavedCount = 0
        val newlyProcessed = mutableSetOf<String>()

        suspend fun processFolder(uriStr: String?, source: StatusSource) {
            if (uriStr.isNullOrEmpty()) return
            try {
                val treeUri = Uri.parse(uriStr)
                val doc = DocumentFile.fromTreeUri(appContext, treeUri) ?: return
                if (!doc.exists() || !doc.isDirectory) return

                val files = doc.listFiles()
                for (file in files) {
                    if (file.isDirectory) continue
                    val name = file.name ?: continue
                    if (name.startsWith(".nomedia", ignoreCase = true)) continue

                    val lower = name.lowercase(Locale.ROOT)
                    val isImage = lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".png")
                    val isVideo = lower.endsWith(".mp4")
                    if (!isImage && !isVideo) continue

                    val size = file.length()
                    val signature = "${name}_$size"

                    // If already processed or exists in vault, skip
                    if (processedSignatures.contains(signature)) continue
                    val isAlreadySaved = existingVaultSignatures.any { (vName, vSize) ->
                        (vSize == size && size > 0) || vName.contains(name.substringBeforeLast(".").lowercase(Locale.ROOT))
                    }
                    if (isAlreadySaved) {
                        newlyProcessed.add(signature)
                        continue
                    }

                    val itemToSave = StatusItem(
                        id = file.uri.toString(),
                        uri = file.uri,
                        name = name,
                        size = size,
                        dateModified = file.lastModified(),
                        isVideo = isVideo,
                        mimeType = if (isVideo) "video/mp4" else if (lower.endsWith(".png")) "image/png" else "image/jpeg",
                        source = source
                    )

                    val saveResult = repository.saveStatusToGallery(itemToSave)
                    if (saveResult.isSuccess) {
                        newlySavedCount++
                        newlyProcessed.add(signature)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error processing folder for $source", e)
            }
        }

        val isBizEnabled = prefs.isBusinessEnabled.first()
        processFolder(waUriStr, StatusSource.WHATSAPP)
        if (isBizEnabled) {
            processFolder(bizUriStr, StatusSource.BUSINESS)
        }

        if (newlyProcessed.isNotEmpty()) {
            prefs.markFilesProcessed(newlyProcessed)
        }

        if (newlySavedCount > 0) {
            Log.i(TAG, "Auto-saved $newlySavedCount new statuses.")
            sendNotification(newlySavedCount)
        } else {
            Log.d(TAG, "No new statuses to auto-save.")
        }

        return Result.success()
    }

    private fun sendNotification(count: Int) {
        createNotificationChannel()

        val intent = Intent(appContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("OPEN_TAB", "VAULT")
        }
        val pendingIntent = PendingIntent.getActivity(
            appContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val title = "Statuses Auto-Saved"
        val message = "Auto-Saved $count new status${if (count > 1) "es" else ""} to DCIM/Status Saver"

        val notification = NotificationCompat.Builder(appContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        try {
            val manager = NotificationManagerCompat.from(appContext)
            manager.notify(NOTIFICATION_ID, notification)
        } catch (e: SecurityException) {
            Log.w(TAG, "Notification permission not granted, could not show notification", e)
        } catch (e: Exception) {
            Log.e(TAG, "Error displaying auto-save notification", e)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                appContext.getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = appContext.getString(R.string.notification_channel_desc)
                enableLights(true)
                lightColor = 0xFF00A884.toInt()
            }
            val notificationManager =
                appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
