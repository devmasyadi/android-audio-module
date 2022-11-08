package com.androidmodule.audiomodule.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

abstract class NetworkBoundResource<ResultType, RequestType> {

    private var result: Flow<Resource<ResultType>> = flow {
            try {
                emit(Resource.Loading(true))
                val dbSource = loadFromDB().firstOrNull()
                if (shouldFetch(dbSource)) {
                    val apiResponse = createCall()
                    saveCallResult(apiResponse)
                }
            }catch (e: Exception) {
                emit(Resource.Error(e.toString()))
            }finally {
                emit(Resource.Loading(false))
                loadFromDB().collect {
                    emit(Resource.Success(it))
                }
            }
    }

    protected abstract fun loadFromDB(): Flow<ResultType>

    protected abstract fun shouldFetch(data: ResultType?): Boolean

    protected abstract suspend fun createCall(): RequestType

    protected abstract suspend fun saveCallResult(data: RequestType)

    fun asFlow(): Flow<Resource<ResultType>> = result
}
