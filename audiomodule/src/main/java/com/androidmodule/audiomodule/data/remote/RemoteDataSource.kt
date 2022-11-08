package com.androidmodule.audiomodule.data.remote

class RemoteDataSource(
    private val apiService: ApiService
) {
    suspend fun getData(packageName: String?) = apiService.getData(packageName)
}