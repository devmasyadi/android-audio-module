package com.androidmodule.audiomodule.data

import com.androidmodule.audiomodule.model.AudioModel
import com.androidmodule.audiomodule.model.AudiosItem
import com.androidmodule.audiomodule.model.Playlist
import com.androidmodule.audiomodule.model.PlaylistWithAudios
import kotlinx.coroutines.flow.Flow

interface IRepository {
    //audio
    fun getData(packageName: String): Flow<Resource<AudioModel>>
    //favorite
    fun addAudioToFavorite(audiosItem: AudiosItem)
    fun deleteAudioFromFavorite(audioId: String)
    fun listAudioFavorite(): Flow<Resource<List<AudiosItem>>>
    fun isAudioFavorite(audioId: String): Flow<Boolean>
    //playlist
    fun createPlaylist(playlist: Playlist)
    fun updatePlaylist(playlist: Playlist)
    fun deletePlaylist(playlist: Playlist)
    fun getAllPlaylist(): Flow<Resource<List<Playlist>>>
    fun getAllPlaylistWithAudios(): Flow<Resource<List<PlaylistWithAudios>>>
    fun addAudioToPlaylist(playlistId: Int, audiosItem: AudiosItem)
    fun deleteAudioFromPlaylist(playlistId: Int, audioId: String)
    fun getAudiosByPlaylistId(playlistId: Int): Flow<Resource<PlaylistWithAudios>>
    //download
    fun addAudioToDownload(audiosItem: AudiosItem)
    fun updateDownload(reqDownload: Long, isDoneDownload: Boolean)
    fun deleteAudioFromDownload(audioId: String)
    fun listAudioDownload(): Flow<Resource<List<AudiosItem>>>
    fun isAudioDownload(audioId: String): Flow<Boolean>
    //recentPlayed
    fun addAudioToRecentPlayed(audiosItem: AudiosItem)
    fun deleteAudioFromRecentPlayed(audioId: String)
    fun listAudioRecentPlayed(): Flow<Resource<List<AudiosItem>>>
    fun isAudioRecentPlayed(audioId: String): Flow<Boolean>
}