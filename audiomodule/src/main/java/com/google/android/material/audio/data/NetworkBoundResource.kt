package com.google.android.material.audio.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

abstract class NetworkBoundResource<ResultType, RequestType> {

    private var result: Flow<com.google.android.material.audio.data.Resource<ResultType>> = flow {
            try {
                emit(com.google.android.material.audio.data.Resource.Loading(true))
                val dbSource = loadFromDB().firstOrNull()
                if (shouldFetch(dbSource)) {
                    val apiResponse = createCall()
                    saveCallResult(apiResponse)
                }
            }catch (e: Exception) {
                emit(com.google.android.material.audio.data.Resource.Error(e.toString()))
            }finally {
                emit(com.google.android.material.audio.data.Resource.Loading(false))
                loadFromDB().collect {
                    emit(com.google.android.material.audio.data.Resource.Success(it))
                }
            }
    }

    protected abstract fun loadFromDB(): Flow<ResultType>

    protected abstract fun shouldFetch(data: ResultType?): Boolean

    protected abstract suspend fun createCall(): RequestType

    protected abstract suspend fun saveCallResult(data: RequestType)

    fun asFlow(): Flow<com.google.android.material.audio.data.Resource<ResultType>> = result
}
