package com.google.android.material.audio.data

import com.google.android.material.audio.model.AudioModel
import com.google.android.material.audio.model.AudiosItem
import com.google.android.material.audio.model.Playlist
import com.google.android.material.audio.model.PlaylistWithAudios
import kotlinx.coroutines.flow.Flow

interface IRepository {
    //audio
    fun getData(packageName: String): Flow<com.google.android.material.audio.data.Resource<AudioModel>>
    //favorite
    fun addAudioToFavorite(audiosItem: AudiosItem)
    fun deleteAudioFromFavorite(audioId: String)
    fun listAudioFavorite(): Flow<com.google.android.material.audio.data.Resource<List<AudiosItem>>>
    fun isAudioFavorite(audioId: String): Flow<Boolean>
    //playlist
    fun createPlaylist(playlist: Playlist)
    fun updatePlaylist(playlist: Playlist)
    fun deletePlaylist(playlist: Playlist)
    fun getAllPlaylist(): Flow<com.google.android.material.audio.data.Resource<List<Playlist>>>
    fun getAllPlaylistWithAudios(): Flow<com.google.android.material.audio.data.Resource<List<PlaylistWithAudios>>>
    fun addAudioToPlaylist(playlistId: Int, audiosItem: AudiosItem)
    fun deleteAudioFromPlaylist(playlistId: Int, audioId: String)
    fun getAudiosByPlaylistId(playlistId: Int): Flow<com.google.android.material.audio.data.Resource<PlaylistWithAudios>>
    //download
    fun addAudioToDownload(audiosItem: AudiosItem)
    fun updateDownload(reqDownload: Long, isDoneDownload: Boolean)
    fun deleteAudioFromDownload(audioId: String)
    fun listAudioDownload(): Flow<com.google.android.material.audio.data.Resource<List<AudiosItem>>>
    fun isDoneDownload(reqDownload: Long): Flow<Long>
    fun isAudioDownload(audioId: String): Flow<Boolean>
    //recentPlayed
    fun addAudioToRecentPlayed(audiosItem: AudiosItem)
    fun deleteAudioFromRecentPlayed(audioId: String)
    fun listAudioRecentPlayed(): Flow<com.google.android.material.audio.data.Resource<List<AudiosItem>>>
    fun isAudioRecentPlayed(audioId: String): Flow<Boolean>
}