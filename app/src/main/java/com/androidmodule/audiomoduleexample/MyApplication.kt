package com.androidmodule.audiomoduleexample

import android.app.Application
import com.androidmodule.audiomodule.di.audioModule
import com.androidmodule.audiomodule.utils.AudioModuleUtils
import com.androidmodule.audiomoduleexample.ui.detailAudio.DetailAudioActivity
import com.androidmodule.audiomoduleserviceexoplayer.AudioUtils
import com.androidmodule.audiomoduleserviceexoplayer.audioPlayerServiceExoPlayerModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AudioModuleUtils.maxRecentPlayedAudio = 3
        AudioModuleUtils.baseUrl = "http://192.168.1.4:3002"
        AudioUtils.detailAudioActivity = DetailAudioActivity::class.java
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    audioModule,
                    audioPlayerServiceExoPlayerModule,
                )
            )
        }
    }
}