package com.google.android.material.audio.model

import androidx.room.Embedded
import androidx.room.Entity
import java.util.*

@Entity(tableName = "downloadedAudios", primaryKeys = ["audioId"])
data class DownloadedAudio(
    @Embedded
    val audiosItem: AudiosItem,
    val reqDownload: Long? = -1,
    val isDoneDownload: Boolean = false,
    val createdAt: Long? = Date().time
)
