package com.androidmoduleexample.audiomoduleexample.callback

import android.view.View
import com.google.android.material.audio.model.AudiosItem

interface IListAudioAdapter {
    fun onItemClicked(audiosItem: AudiosItem, indexAudio: Int)
    fun onOptionClicked(audiosItem: AudiosItem, view: View)
}