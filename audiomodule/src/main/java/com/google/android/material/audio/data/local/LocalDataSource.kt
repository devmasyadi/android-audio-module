package com.google.android.material.audio.data.local

import com.google.android.material.audio.data.local.room.AudioDao
import com.google.android.material.audio.model.AudioModel
import com.google.android.material.audio.model.AudiosItem
import com.google.android.material.audio.model.DownloadedAudio
import com.google.android.material.audio.model.FavoriteAudio
import com.google.android.material.audio.model.Playlist
import com.google.android.material.audio.model.PlaylistAudioCrossRef
import com.google.android.material.audio.model.RecentPlayedAudio

class LocalDataSource(private val audioDao: AudioDao) {

    //audio
    fun addAudioModel(audioModel: AudioModel) = audioDao.addAudioModel(audioModel)
    fun addAudioItem(audiosItem: AudiosItem) = audioDao.addAudioItem(audiosItem)
    fun getAudioModel() = audioDao.getAudioModel()

    //favorite
    fun addAudioToFavorite(favoriteAudio: FavoriteAudio) = audioDao.addAudioToFavorite(favoriteAudio)
    fun deleteAudioFromFavorite(audioId: String) = audioDao.deleteAudioFromFavorite(audioId)
    fun listAudioFavorite() = audioDao.listAudioFavorite()
    fun isAudioFavorite(audioId: String) = audioDao.isAudioFavorite(audioId)

    //playlist
    fun createPlaylist(playlist: Playlist) = audioDao.createPlaylist(playlist)
    fun updatePlaylist(playlist: Playlist) = audioDao.updatePlaylist(playlist)
    fun updatePlaylist(playlistId: Int, newImage: String?) = audioDao.updatePlaylist(playlistId, newImage)
    fun deletePlaylist(playlist: Playlist) = audioDao.deletePlaylist(playlist)
    fun getAllPlaylist() = audioDao.getAllPlaylist()
    fun getAllPlaylistWithAudios() = audioDao.getAllPlaylistWithAudios()
    fun addAudioToPlaylist(playlistAudioCrossRef: PlaylistAudioCrossRef) = audioDao.addAudioToPlaylist(playlistAudioCrossRef)
    fun deleteAudioFromPlaylist(playlistAudioCrossRef: PlaylistAudioCrossRef) = audioDao.deleteAudioFromPlaylist(playlistAudioCrossRef)
    fun getAudiosByPlaylistId(playlistId: Int) = audioDao.getAudiosByPlaylistId(playlistId)

    //download
    fun addAudioToDownload(downloadedAudio: DownloadedAudio) = audioDao.addAudioToDownload(downloadedAudio)
    fun updateDownload(reqDownload: Long, isDoneDownload: Boolean) = audioDao.updateDownload(reqDownload, isDoneDownload)
    fun deleteAudioFromDownload(audioId: String) = audioDao.deleteAudioFromDownload(audioId)
    fun listAudioDownload() = audioDao.listAudioDownload()
    fun isAudioDownload(audioId: String) = audioDao.isAudioDownload(audioId)

    //recentPlayed
    fun addAudioToRecentPlayed(recentPlayedAudio: RecentPlayedAudio) = audioDao.addAudioToRecentPlayed(recentPlayedAudio)
    fun deleteAudioFromRecentPlayed(audioId: String) = audioDao.deleteAudioFromRecentPlayed(audioId)
    fun deleteManyRecentPlayed(list: List<RecentPlayedAudio>) = audioDao.deleteManyRecentPlayed(list)
    fun listRecentPlayed() = audioDao.listRecentPlayed()
    fun listRecentPlayedNonStream() = audioDao.listRecentPlayedNonStream()
    fun listAudioRecentPlayed() = audioDao.listAudioRecentPlayed()
    fun isAudioRecentPlayed(audioId: String) = audioDao.isAudioRecentPlayed(audioId)
}