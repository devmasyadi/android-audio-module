package com.google.android.material.audio.data.local.room

import androidx.room.*
import com.google.android.material.audio.model.AudioModel
import com.google.android.material.audio.model.AudiosItem
import com.google.android.material.audio.model.DownloadedAudio
import com.google.android.material.audio.model.FavoriteAudio
import com.google.android.material.audio.model.Playlist
import com.google.android.material.audio.model.PlaylistAudioCrossRef
import com.google.android.material.audio.model.PlaylistWithAudios
import com.google.android.material.audio.model.RecentPlayedAudio
import kotlinx.coroutines.flow.Flow

@Dao
interface AudioDao {
    //audio
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAudioModel(audioModel: AudioModel)

    @Query("SELECT * from audioModels")
    fun getAudioModel(): Flow<List<AudioModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAudioItem(audiosItem: AudiosItem)

    //favorite
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAudioToFavorite(favoriteAudio: FavoriteAudio)

    @Query("DELETE FROM favoriteAudios WHERE audioId = :audioId")
    fun deleteAudioFromFavorite(audioId: String)

    @Query("SELECT * FROM favoriteAudios ORDER BY createdAt DESC")
    fun listAudioFavorite(): Flow<List<AudiosItem>>

    @Query("SELECT * FROM favoriteAudios WHERE audioId = :audioId")
    fun isAudioFavorite(audioId: String): Flow<List<FavoriteAudio>>

    //playlist
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createPlaylist(playlist: Playlist)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updatePlaylist(playlist: Playlist)

    @Query("UPDATE playlists SET image=:newImage WHERE playlistId=:playlistId")
    fun updatePlaylist(playlistId: Int, newImage: String?)

    @Delete
    fun deletePlaylist(playlist: Playlist)

    @Query("SELECT * FROM playlists")
    fun getAllPlaylist(): Flow<List<Playlist>>

    @Transaction
    @Query("SELECT * FROM playlists ")
    fun getAllPlaylistWithAudios(): Flow<List<PlaylistWithAudios>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAudioToPlaylist(playlistAudioCrossRef: PlaylistAudioCrossRef)

    @Delete
    fun deleteAudioFromPlaylist(playlistAudioCrossRef: PlaylistAudioCrossRef)

    @Transaction
    @Query("SELECT * FROM playlists Where playlistId= :playlistId")
    fun getAudiosByPlaylistId(playlistId: Int): Flow<PlaylistWithAudios>

    //download
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAudioToDownload(downloadedAudio: DownloadedAudio)

    @Query("DELETE FROM downloadedAudios WHERE audioId = :audioId")
    fun deleteAudioFromDownload(audioId: String)

    @Query("Update downloadedAudios set isDoneDownload=:isDoneDownload WHERE reqDownload=:reqDownload")
    fun updateDownload(reqDownload: Long, isDoneDownload: Boolean)

    @Query("SELECT * FROM downloadedAudios ORDER BY createdAt DESC")
    fun listAudioDownload(): Flow<List<AudiosItem>>

    @Query("SELECT * FROM downloadedAudios WHERE audioId= :audioId")
    fun isAudioDownload(audioId: String): Flow<List<AudiosItem>>

    //recentPlayed
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAudioToRecentPlayed(recentPlayedAudio: RecentPlayedAudio)

    @Query("DELETE FROM recentPlayedAudios WHERE audioId = :audioId")
    fun deleteAudioFromRecentPlayed(audioId: String)

    @Delete
    fun deleteManyRecentPlayed(list: List<RecentPlayedAudio>)

    @Query("SELECT * FROM recentPlayedAudios ORDER BY createdAt DESC")
    fun listRecentPlayed(): Flow<List<RecentPlayedAudio>>

    @Query("SELECT * FROM recentPlayedAudios ORDER BY createdAt DESC")
    fun listRecentPlayedNonStream(): List<RecentPlayedAudio>

    @Query("SELECT * FROM recentPlayedAudios ORDER BY createdAt DESC")
    fun listAudioRecentPlayed(): Flow<List<AudiosItem>>

    @Query("SELECT * FROM recentPlayedAudios WHERE audioId=:audioId")
    fun isAudioRecentPlayed(audioId: String): Flow<List<AudiosItem>>
}