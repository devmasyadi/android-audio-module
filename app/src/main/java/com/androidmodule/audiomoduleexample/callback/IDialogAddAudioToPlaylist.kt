package com.androidmodule.audiomoduleexample.callback

import com.androidmodule.audiomodule.model.Playlist

interface IDialogAddAudioToPlaylist {
    fun onAddToPlaylist(playlist: Playlist)
    fun onCreatePlaylist()
}