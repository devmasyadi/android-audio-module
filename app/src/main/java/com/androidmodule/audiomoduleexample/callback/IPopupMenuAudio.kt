package com.androidmodule.audiomoduleexample.callback

import com.androidmodule.audiomodule.model.AudiosItem

interface IPopupMenuAudio {
    fun onAddToFavorite(audiosItem: AudiosItem)
    fun onAddToPlaylist(audiosItem: AudiosItem)
    fun onShareAudio(audiosItem: AudiosItem)
}