package com.google.android.material.audio.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.google.android.material.audio.model.AudioModel
import com.google.android.material.audio.model.AudioWithCategoryItem
import com.google.android.material.audio.model.AudiosItem
import com.google.android.material.audio.model.DownloadedAudio
import com.google.android.material.audio.model.FavoriteAudio
import com.google.android.material.audio.model.Playlist
import com.google.android.material.audio.model.PlaylistAudioCrossRef
import com.google.android.material.audio.model.RecentPlayedAudio
import com.google.android.material.audio.utils.RoomDataConverter

@Database(
    entities = [
        AudioModel::class,
        AudioWithCategoryItem::class,
        AudiosItem::class,
        FavoriteAudio::class,
        DownloadedAudio::class,
        RecentPlayedAudio::class,
        Playlist::class,
        PlaylistAudioCrossRef::class
    ], version = 2, exportSchema = false
)

@TypeConverters(RoomDataConverter::class)
abstract class AudioModuleDatabase : RoomDatabase() {
    abstract fun audioDao(): AudioDao
}