package com.androidmoduleexample.audiomoduleexample.callback

import com.google.android.material.audio.model.AudiosItem

interface IPopupMenuDownload {
    fun onRemoveFromDownload(audioItem: AudiosItem)
}