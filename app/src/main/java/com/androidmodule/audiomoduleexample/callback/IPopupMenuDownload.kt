package com.androidmodule.audiomoduleexample.callback

import com.androidmodule.audiomodule.model.AudiosItem

interface IPopupMenuDownload {
    fun onRemoveFromDownload(audioItem: AudiosItem)
}