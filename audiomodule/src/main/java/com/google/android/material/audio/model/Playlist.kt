package com.google.android.material.audio.model

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity(tableName = "playlists")
@Parcelize
data class Playlist(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Int?,
    val playlistName: String,
    var image: String?,
): Parcelable

@Entity(tableName = "playlistAudioCrossRefs", primaryKeys = ["playlistId", "audioId"])
data class PlaylistAudioCrossRef(
    val playlistId: Int,
    val audioId: String,
    var createdAt: Long? = Date().time
)

data class PlaylistWithAudios(
    @Embedded val playlist: Playlist,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "audioId",
        associateBy = Junction(PlaylistAudioCrossRef::class)
    )
    val audios: List<AudiosItem>
)

