package com.androidmoduleexample.audiomoduleexample.callback

import com.google.android.material.audio.model.Playlist


interface IDialogAddAudioToPlaylist {
    fun onAddToPlaylist(playlist: Playlist)
    fun onCreatePlaylist()
}