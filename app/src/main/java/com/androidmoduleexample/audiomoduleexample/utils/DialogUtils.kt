package com.androidmoduleexample.audiomoduleexample.utils

import android.app.Activity
import com.google.android.material.audio.model.Playlist
import com.androidmoduleexample.audiomoduleexample.callback.IDialogAddAudioToPlaylist
import com.androidmoduleexample.audiomoduleexample.callback.IDialogConfirmationDelete
import com.androidmoduleexample.audiomoduleexample.callback.IDialogCreatePlaylist
import com.androidmoduleexample.audiomoduleexample.databinding.FormAddPlaylistBinding

enum class TypeAction {
    CREATE,
    UPDATE,
}

fun Activity.showDialogFormPlaylist(
    typeAction: TypeAction,
    defaultValue: Playlist?,
    iDialogPlaylist: IDialogCreatePlaylist?
) {
    val builder = androidx.appcompat.app.AlertDialog.Builder(this)
    val formCreatePlaylistBinding = FormAddPlaylistBinding.inflate(layoutInflater)
    builder.setCancelable(true)
    val title = if (typeAction == TypeAction.CREATE) "Create Playlist" else "Update Playlist"
    builder.setTitle(title)
    builder.setView(formCreatePlaylistBinding.root)
    formCreatePlaylistBinding.apply {
        edtPlaylistName.setText(defaultValue?.playlistName)
    }
    builder.setInverseBackgroundForced(true)
    val txtBtnPositive = if (typeAction == TypeAction.CREATE) "Create" else "Update"
    builder.setPositiveButton(
        txtBtnPositive
    ) { _, _ -> iDialogPlaylist?.onSubmit(formCreatePlaylistBinding.edtPlaylistName.text.toString()) }
    builder.setNegativeButton(
        "Cancel"
    ) { dialog, _ -> dialog.dismiss() }
    val alert = builder.create()
    alert.show()
}

fun Activity.showDialogDeleteConfirmation(
    iDialogConfirmationDelete: IDialogConfirmationDelete?
) {
    val builder = androidx.appcompat.app.AlertDialog.Builder(this)
    builder.setCancelable(true)
    builder.setTitle("Are you Sure?")
    builder.setMessage(
        "Do you really want to delete these data?\n" +
                "This process cannot be undone."
    )
    builder.setPositiveButton(
        "Ok"
    ) { _, _ -> iDialogConfirmationDelete?.onDeleteConfirmation() }
    builder.setNegativeButton(
        "Cancel"
    ) { dialog, _ -> dialog.dismiss() }
    val alert = builder.create()
    alert.show()
}

fun Activity.showDialogAddAudioToPlaylist(
    listPlaylist: List<Playlist>,
    iDialogAddAudioToPlaylist: IDialogAddAudioToPlaylist?
) {
    var playlistSelected = Playlist(null, "", "")
    val builder = androidx.appcompat.app.AlertDialog.Builder(this)
    builder.setCancelable(true)
    builder.setTitle("Select Playlist")
    val strPlaylist = listPlaylist.map { it.playlistName }.toTypedArray()
    builder.setSingleChoiceItems(
        strPlaylist, -1
    ) { _, index ->
        playlistSelected = listPlaylist[index]
    }
    builder.setPositiveButton(
        "OK"
    ) { _, _ ->
        iDialogAddAudioToPlaylist?.onAddToPlaylist(playlistSelected)
    }
    builder.setNeutralButton(
        "Create Playlist"
    ) { _, _ -> iDialogAddAudioToPlaylist?.onCreatePlaylist() }
    builder.setNegativeButton(
        "Cancel"
    ) { dialog, _ -> dialog.dismiss() }
    val alert = builder.create()
    alert.show()
}

