package com.androidmodule.audiomodule.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.androidmodule.audiomodule.model.*
import com.androidmodule.audiomodule.utils.RoomDataConverter

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
    ], version = 1, exportSchema = false
)

@TypeConverters(RoomDataConverter::class)
abstract class AudioModuleDatabase : RoomDatabase() {
    abstract fun audioDao(): AudioDao
}