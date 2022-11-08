package com.androidmodule.audiomodule.model

import androidx.room.Embedded
import androidx.room.Entity
import java.util.*

@Entity(tableName = "favoriteAudios", primaryKeys = ["audioId"])
data class FavoriteAudio(
    @Embedded
    val audiosItem: AudiosItem,
    val createdAt: Long? = Date().time
)
