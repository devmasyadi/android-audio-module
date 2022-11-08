package com.androidmodule.audiomodule.model

import androidx.room.Embedded
import androidx.room.Entity
import java.util.*

@Entity(tableName = "recentPlayedAudios", primaryKeys = ["audioId"])
data class RecentPlayedAudio(
    @Embedded
    val audiosItem: AudiosItem,
    val createdAt: Long? = Date().time
)
