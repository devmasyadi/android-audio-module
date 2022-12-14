package com.androidmodule.audiomodule.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.androidmodule.audiomodule.data.Repository
import com.androidmodule.audiomodule.model.AudiosItem
import com.androidmodule.audiomodule.model.Playlist

class AudioViewModel(private val repository: Repository): ViewModel() {
    fun getData(packageName: String) = repository.getData(packageName).asLiveData()
    //favorite
    fun addAudioToFavorite(audiosItem: AudiosItem) = repository.addAudioToFavorite(audiosItem)
    fun deleteAudioFromFavorite(audioId: String) = repository.deleteAudioFromFavorite(audioId)
    fun listAudioFavorite() = repository.listAudioFavorite().asLiveData()
    fun isAudioFavorite(audioId: String) = repository.isAudioFavorite(audioId)
    //playlist
    fun createPlaylist(playlist: Playlist) = repository.createPlaylist(playlist)
    fun updatePlaylist(playlist: Playlist) = repository.updatePlaylist(playlist)
    fun deletePlaylist(playlist: Playlist) = repository.deletePlaylist(playlist)
    fun getAllPlaylist() = repository.getAllPlaylist().asLiveData()
    fun getAllPlaylistWithAudios() = repository.getAllPlaylistWithAudios().asLiveData()
    fun addAudioToPlaylist(playlistId: Int, audiosItem: AudiosItem) = repository.addAudioToPlaylist(playlistId, audiosItem)
    fun deleteAudioFromPlaylist(playlistId: Int, audioId: String) = repository.deleteAudioFromPlaylist(playlistId, audioId)
    fun getAudiosByPlaylistId(playlistId: Int) = repository.getAudiosByPlaylistId(playlistId)
    //download
    fun addAudioToDownload(audiosItem: AudiosItem) = repository.addAudioToDownload(audiosItem)
    fun deleteAudioFromDownload(audioId: String) = repository.deleteAudioFromDownload(audioId)
    fun listAudioDownload() = repository.listAudioDownload().asLiveData()
    fun isDoneDownload(reqDownload: Long) = repository.isDoneDownload(reqDownload)
    fun isAudioDownload(audioId: String) = repository.isAudioDownload(audioId)
    fun updateDownload(reqDownload: Long, isDoneDownload: Boolean) = repository.updateDownload(reqDownload, isDoneDownload)
    //recentPlayed
    fun addAudioToRecentPlayed(audiosItem: AudiosItem) = repository.addAudioToRecentPlayed(audiosItem)
    fun deleteAudioFromRecentPlayed(audioId: String) = repository.deleteAudioFromRecentPlayed(audioId)
    fun listAudioRecentPlayed() = repository.listAudioRecentPlayed().asLiveData()
    fun isAudioRecentPlayed(audioId: String) = repository.isAudioRecentPlayed(audioId)
}