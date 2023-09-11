package com.google.android.material.audio.utils

import androidx.room.TypeConverter
import com.google.android.material.audio.model.AudioWithCategoryItem
import com.google.android.material.audio.model.AudiosItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RoomDataConverter {
    @TypeConverter
    fun fromAudiosItemList(value: List<AudiosItem>): String {
        val gson = Gson()
        val type = object : TypeToken<List<AudiosItem>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toAudiosItemList(value: String): List<AudiosItem> {
        val gson = Gson()
        val type = object : TypeToken<List<AudiosItem>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromAudioWithCategoryItemList(value: List<AudioWithCategoryItem>): String {
        val gson = Gson()
        val type = object : TypeToken<List<AudioWithCategoryItem>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toAudioWithCategoryItemList(value: String): List<AudioWithCategoryItem> {
        val gson = Gson()
        val type = object : TypeToken<List<AudioWithCategoryItem>>() {}.type
        return gson.fromJson(value, type)
    }
}