package com.androidmodule.audiomodule.utils

import androidx.room.TypeConverter
import com.androidmodule.audiomodule.model.AudioWithCategoryItem
import com.androidmodule.audiomodule.model.AudiosItem
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