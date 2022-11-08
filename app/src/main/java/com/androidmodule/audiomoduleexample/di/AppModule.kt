package com.androidmodule.audiomoduleexample.di

import com.androidmodule.audiomoduleexample.viewmodel.AudioPlayerViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        AudioPlayerViewModel()
    }
    viewModel { get() }
}