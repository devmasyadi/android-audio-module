package com.androidmoduleexample.audiomoduleexample

import android.app.Application
import com.google.android.material.audio.di.audioModule
import com.google.android.material.audio.utils.AudioModuleUtils
//import com.androidmoduleexample.audiomoduleserviceexoplayer.AudioUtils
//import com.androidmoduleexample.audiomoduleserviceexoplayer.audioPlayerServiceExoPlayerModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AudioModuleUtils.maxRecentPlayedAudio = 3
        AudioModuleUtils.style = ""
//        AudioUtils.detailAudioActivity = DetailAudioActivity::class.java
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    audioModule,
//                    audioPlayerServiceExoPlayerModule,
                )
            )
        }
    }
}