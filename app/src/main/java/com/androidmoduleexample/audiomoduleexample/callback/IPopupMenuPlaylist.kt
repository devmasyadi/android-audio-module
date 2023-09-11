package com.androidmoduleexample.audiomoduleexample.callback

import com.google.android.material.audio.model.Playlist

interface IPopupMenuPlaylist {
    fun onEditPlaylist(playlist: Playlist)
    fun onDeletePlaylist(playlist: Playlist)
}