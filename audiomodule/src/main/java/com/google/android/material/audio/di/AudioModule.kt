package com.google.android.material.audio.di

import androidx.room.Room
import com.google.android.material.audio.data.local.LocalDataSource
import com.google.android.material.audio.data.local.room.AudioModuleDatabase
import com.google.android.material.audio.data.remote.ApiService
import com.google.android.material.audio.data.remote.RemoteDataSource
import com.google.android.material.audio.utils.AudioModuleUtils
import com.google.android.material.audio.viewmodel.AudioViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val audioModule = module(override = true) {
    factory { get<AudioModuleDatabase>().audioDao() }
    single {
        Room.databaseBuilder(
            androidContext(),
            AudioModuleDatabase::class.java, "AudioModule.db"
        ).fallbackToDestructiveMigration()
            .build()
    }
    single {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.NONE)

        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .build()
    }

    single {
        val retrofit = AudioModuleUtils.style?.let { it1 ->
            Retrofit.Builder()
                .baseUrl(it1)
                .addConverterFactory(GsonConverterFactory.create())
                .client(get())
                .build()
        }
        retrofit?.create(ApiService::class.java)
    }

    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    single { com.google.android.material.audio.data.Repository(get(), get()) }
    viewModel { AudioViewModel(get()) }
}
