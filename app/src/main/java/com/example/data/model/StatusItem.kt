package com.example.data.model

import android.net.Uri

enum class StatusSource(val label: String) {
    WHATSAPP("WhatsApp"),
    BUSINESS("WA Business"),
    SAVED_VAULT("Saved")
}

enum class MediaFilter(val title: String) {
    ALL("All"),
    IMAGES("Photos"),
    VIDEOS("Videos")
}

data class StatusItem(
    val id: String,
    val uri: Uri,
    val name: String,
    val size: Long = 0L,
    val dateModified: Long = 0L,
    val isVideo: Boolean = false,
    val mimeType: String = "image/jpeg",
    val source: StatusSource = StatusSource.WHATSAPP,
    val isSaved: Boolean = false,
    val durationMs: Long = 0L
)
