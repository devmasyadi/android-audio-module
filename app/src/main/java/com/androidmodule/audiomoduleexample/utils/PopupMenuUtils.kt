package com.androidmodule.audiomoduleexample.utils

import androidx.appcompat.widget.PopupMenu
import com.androidmodule.audiomodule.model.AudiosItem
import com.androidmodule.audiomodule.model.Playlist
import com.androidmodule.audiomoduleexample.R
import com.androidmodule.audiomoduleexample.callback.IPopupMenuAudio
import com.androidmodule.audiomoduleexample.callback.IPopupMenuDownload
import com.androidmodule.audiomoduleexample.callback.IPopupMenuPlaylist

fun PopupMenu.showPopUpMenuAudio(audiosItem: AudiosItem, iPopupMenuAudio: IPopupMenuAudio?) {
    inflate(R.menu.menu_popup_audio)
    setOnMenuItemClickListener { menuItem ->
        when (menuItem.itemId) {
            R.id.actionAddToFavorite -> iPopupMenuAudio?.onAddToFavorite(audiosItem)
            R.id.actionAddToPlaylist -> iPopupMenuAudio?.onAddToPlaylist(audiosItem)
            R.id.actionShareAudio -> iPopupMenuAudio?.onShareAudio(audiosItem)
        }
        true
    }
    show()
}

fun PopupMenu.showPopUpMenuPlaylist(playlist: Playlist, iPopupMenuPlaylist: IPopupMenuPlaylist?) {
    inflate(R.menu.menu_popup_playlist)
    setOnMenuItemClickListener { menuItem ->
        when (menuItem.itemId) {
            R.id.actionRenamePlaylist -> iPopupMenuPlaylist?.onEditPlaylist(playlist)
            R.id.actionDeletePlaylist -> iPopupMenuPlaylist?.onDeletePlaylist(playlist)
        }
        true
    }
    show()
}

fun PopupMenu.showPopUpMenuDownload(
    audiosItem: AudiosItem,
    iPopupMenuDownload: IPopupMenuDownload?
) {
    inflate(R.menu.menu_popup_download)
    setOnMenuItemClickListener { menuItem ->
        when (menuItem.itemId) {
            R.id.actionRemoveFromDownload -> iPopupMenuDownload?.onRemoveFromDownload(audiosItem)
        }
        true
    }
    show()
}