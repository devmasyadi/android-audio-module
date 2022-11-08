package com.androidmodule.audiomoduleexample.callback

import android.view.View
import com.androidmodule.audiomodule.model.AudiosItem

interface IListAudioAdapter {
    fun onItemClicked(audiosItem: AudiosItem, indexAudio: Int)
    fun onOptionClicked(audiosItem: AudiosItem, view: View)
}