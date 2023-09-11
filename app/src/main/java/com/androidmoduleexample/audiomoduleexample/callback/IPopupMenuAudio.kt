package com.androidmoduleexample.audiomoduleexample.callback

import com.google.android.material.audio.model.AudiosItem

interface IPopupMenuAudio {
    fun onAddToFavorite(audiosItem: AudiosItem)
    fun onAddToPlaylist(audiosItem: AudiosItem)
    fun onShareAudio(audiosItem: AudiosItem)
}