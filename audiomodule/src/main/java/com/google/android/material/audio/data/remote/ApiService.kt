package com.google.android.material.audio.data.remote

import com.google.android.material.audio.model.AudioModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("apps")
    suspend fun getData(
        @Query("packageName") packageName: String?
    ): AudioModel
}