package com.androidmodule.audiomodule.data

sealed class Resource<out T> {
    class Success<out T>(val data: T) : Resource<T>()
    class Loading<out T>(val state: Boolean) : Resource<T>()
    class Error<out T>(val message: String) : Resource<T>()
}