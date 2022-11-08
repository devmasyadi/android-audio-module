package com.androidmodule.audiomodule.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "audioModels")
@Parcelize
data class AudioModel(

    @PrimaryKey
    @ColumnInfo(name = "audioModelId")
    val audioModelId: Int = 1,

    @field:SerializedName("_id")
    val id: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("audios")
    val audios: List<AudiosItem>? = null,

    @field:SerializedName("audioWithCategory")
    val audioWithCategory: List<AudioWithCategoryItem>? = null,

    @field:SerializedName("packageName")
    val packageName: String? = null,

    @field:SerializedName("isWithCategory")
    val isWithCategory: Boolean? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: String? = null
) : Parcelable

@Entity
@Parcelize
data class AudiosItem(
    @PrimaryKey
    @field:SerializedName("_id")
    val audioId: String = "1",

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("artist")
    val artist: String? = null,

    @field:SerializedName("title")
    val title: String = "",

    @field:SerializedName("url")
    var url: String? = null,

) : Parcelable

@Entity
@Parcelize
data class AudioWithCategoryItem(

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("audios")
    val audios: List<AudiosItem>? = null,

    @PrimaryKey
    @ColumnInfo(name = "audioWithCategoryId")
    @field:SerializedName("_id")
    val id: String = "1"
) : Parcelable
