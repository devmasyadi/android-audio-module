package com.androidmodule.audiomodule.di

import androidx.room.Room
import com.androidmodule.audiomodule.BuildConfig
import com.androidmodule.audiomodule.data.Repository
import com.androidmodule.audiomodule.data.local.LocalDataSource
import com.androidmodule.audiomodule.data.local.room.AudioModuleDatabase
import com.androidmodule.audiomodule.data.remote.ApiService
import com.androidmodule.audiomodule.data.remote.RemoteDataSource
import com.androidmodule.audiomodule.utils.AudioModuleUtils
import com.androidmodule.audiomodule.viewmodel.AudioViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val audioModule = module {
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
        val retrofit = Retrofit.Builder()
            .baseUrl(AudioModuleUtils.baseUrl ?: BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }

    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    single { Repository(get(), get()) }
    viewModel { AudioViewModel(get()) }
}
