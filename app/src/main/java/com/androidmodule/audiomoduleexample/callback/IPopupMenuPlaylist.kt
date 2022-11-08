package com.androidmodule.audiomoduleexample.callback

import com.androidmodule.audiomodule.model.Playlist

interface IPopupMenuPlaylist {
    fun onEditPlaylist(playlist: Playlist)
    fun onDeletePlaylist(playlist: Playlist)
}