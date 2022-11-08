package com.androidmodule.audiomodule.data.remote

import com.androidmodule.audiomodule.model.AudioModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("mp3/apps")
    suspend fun getData(
        @Query("packageName") packageName: String?
    ): AudioModel
}