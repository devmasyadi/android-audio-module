package com.google.android.material.audio.data

import com.google.android.material.audio.data.local.LocalDataSource
import com.google.android.material.audio.data.remote.RemoteDataSource
import com.google.android.material.audio.model.AudioModel
import com.google.android.material.audio.model.AudiosItem
import com.google.android.material.audio.model.DownloadedAudio
import com.google.android.material.audio.model.FavoriteAudio
import com.google.android.material.audio.model.Playlist
import com.google.android.material.audio.model.PlaylistAudioCrossRef
import com.google.android.material.audio.model.PlaylistWithAudios
import com.google.android.material.audio.model.RecentPlayedAudio
import com.google.android.material.audio.utils.AppExecutors
import com.google.android.material.audio.utils.AudioModuleUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.util.*

class Repository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : com.google.android.material.audio.data.IRepository {
    override fun getData(packageName: String): Flow<com.google.android.material.audio.data.Resource<AudioModel>> {
        return object : com.google.android.material.audio.data.NetworkBoundResource<AudioModel, AudioModel>() {
            override fun loadFromDB(): Flow<AudioModel> {
                return flow {
                    localDataSource.getAudioModel().collect {
                        emit(it.firstOrNull() ?: AudioModel())
                    }
                }
            }

            override fun shouldFetch(data: AudioModel?): Boolean {
                return true
            }

            override suspend fun createCall(): AudioModel {
                return remoteDataSource.getData(packageName)
            }

            override suspend fun saveCallResult(data: AudioModel) {
                AppExecutors().diskIO().execute { localDataSource.addAudioModel(data) }
            }
        }.asFlow()
    }

    override fun addAudioToFavorite(audiosItem: AudiosItem) {
        AppExecutors().diskIO().execute {
            localDataSource.addAudioToFavorite(FavoriteAudio(audiosItem, Date().time))
        }
    }

    override fun deleteAudioFromFavorite(audioId: String) {
        AppExecutors().diskIO().execute {
            localDataSource.deleteAudioFromFavorite(audioId)
        }
    }

    override fun listAudioFavorite(): Flow<com.google.android.material.audio.data.Resource<List<AudiosItem>>> {
        return flow {
            try {
                emit(com.google.android.material.audio.data.Resource.Loading(true))
                emitAll(localDataSource.listAudioFavorite().map {
                    com.google.android.material.audio.data.Resource.Success(it)
                }
                )
            } catch (e: Exception) {
                emit(com.google.android.material.audio.data.Resource.Error(e.toString()))
            } finally {
                emit(com.google.android.material.audio.data.Resource.Loading(false))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun isAudioFavorite(audioId: String): Flow<Boolean> {
        return localDataSource.isAudioFavorite(audioId)
            .map { it.isNotEmpty() }
    }

    override fun createPlaylist(playlist: Playlist) {
        AppExecutors().diskIO().execute {
            localDataSource.createPlaylist(playlist)
        }
    }

    override fun updatePlaylist(playlist: Playlist) {
        AppExecutors().diskIO().execute {
            localDataSource.updatePlaylist(playlist)
        }
    }

    override fun deletePlaylist(playlist: Playlist) {
        AppExecutors().diskIO().execute {
            localDataSource.deletePlaylist(playlist)
        }
    }

    override fun getAllPlaylist(): Flow<com.google.android.material.audio.data.Resource<List<Playlist>>> {
        return flow {
            try {
                emit(com.google.android.material.audio.data.Resource.Loading(true))
                emitAll(localDataSource.getAllPlaylist().map {
                    com.google.android.material.audio.data.Resource.Success(it)
                }
                )
            } catch (e: Exception) {
                emit(com.google.android.material.audio.data.Resource.Error(e.toString()))
            } finally {
                emit(com.google.android.material.audio.data.Resource.Loading(false))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getAllPlaylistWithAudios(): Flow<com.google.android.material.audio.data.Resource<List<PlaylistWithAudios>>> {
        return flow {
            try {
                emit(com.google.android.material.audio.data.Resource.Loading(true))
                emitAll(localDataSource.getAllPlaylistWithAudios().map {
                    com.google.android.material.audio.data.Resource.Success(it)
                }
                )
            } catch (e: Exception) {
                emit(com.google.android.material.audio.data.Resource.Error(e.toString()))
            } finally {
                emit(com.google.android.material.audio.data.Resource.Loading(false))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun addAudioToPlaylist(playlistId: Int, audiosItem: AudiosItem) {
        AppExecutors().diskIO().execute {
            localDataSource.addAudioItem(audiosItem)
            localDataSource.updatePlaylist(playlistId, audiosItem.image)
            localDataSource.addAudioToPlaylist(PlaylistAudioCrossRef(playlistId, audiosItem.audioId))
        }
    }

    override fun deleteAudioFromPlaylist(playlistId: Int, audioId: String) {
        AppExecutors().diskIO().execute {
            localDataSource.deleteAudioFromPlaylist(PlaylistAudioCrossRef(playlistId, audioId))
        }
    }

    override fun getAudiosByPlaylistId(playlistId: Int): Flow<com.google.android.material.audio.data.Resource<PlaylistWithAudios>> {
        return flow {
            try {
                emit(com.google.android.material.audio.data.Resource.Loading(true))
                emitAll(localDataSource.getAudiosByPlaylistId(playlistId).map {
                    com.google.android.material.audio.data.Resource.Success(it)
                }
                )
            } catch (e: Exception) {
                emit(com.google.android.material.audio.data.Resource.Error(e.toString()))
            } finally {
                emit(com.google.android.material.audio.data.Resource.Loading(false))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun addAudioToDownload(audiosItem: AudiosItem) {
        AppExecutors().diskIO().execute {
            localDataSource.addAudioToDownload(DownloadedAudio(audiosItem))
        }
    }

    override fun updateDownload(reqDownload: Long, isDoneDownload: Boolean) {
        AppExecutors().diskIO().execute {
            localDataSource.updateDownload(reqDownload, isDoneDownload)
        }
    }

    override fun deleteAudioFromDownload(audioId: String) {
        AppExecutors().diskIO().execute {
            localDataSource.deleteAudioFromDownload(audioId)
        }
    }

    override fun listAudioDownload(): Flow<com.google.android.material.audio.data.Resource<List<AudiosItem>>> {
        return flow {
            try {
                emit(com.google.android.material.audio.data.Resource.Loading(true))
                emitAll(localDataSource.listAudioDownload().map {
                    com.google.android.material.audio.data.Resource.Success(it)
                }
                )
            } catch (e: Exception) {
                emit(com.google.android.material.audio.data.Resource.Error(e.toString()))
            } finally {
                emit(com.google.android.material.audio.data.Resource.Loading(false))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun isDoneDownload(reqDownload: Long): Flow<Long> {
        return flow {
            emit(reqDownload)
        }
    }

    override fun isAudioDownload(audioId: String): Flow<Boolean> {
        return localDataSource.isAudioDownload(audioId)
            .map { it.isNotEmpty() }
    }

    override fun addAudioToRecentPlayed(audiosItem: AudiosItem) {
        AppExecutors().diskIO().execute {
            localDataSource.addAudioToRecentPlayed(RecentPlayedAudio(audiosItem))
            val oldValue = localDataSource.listRecentPlayedNonStream().filterIndexed{ index, _ -> index >= AudioModuleUtils.maxRecentPlayedAudio}
            localDataSource.deleteManyRecentPlayed(oldValue)

        }
    }

    override fun deleteAudioFromRecentPlayed(audioId: String) {
        AppExecutors().diskIO().execute {
            localDataSource.deleteAudioFromRecentPlayed(audioId)
        }
    }

    override fun listAudioRecentPlayed(): Flow<com.google.android.material.audio.data.Resource<List<AudiosItem>>> {
        return flow {
            try {
                emit(com.google.android.material.audio.data.Resource.Loading(true))
                emitAll(localDataSource.listAudioRecentPlayed().map {
                    com.google.android.material.audio.data.Resource.Success(it)
                }
                )
            } catch (e: Exception) {
                emit(com.google.android.material.audio.data.Resource.Error(e.toString()))
            } finally {
                emit(com.google.android.material.audio.data.Resource.Loading(false))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun isAudioRecentPlayed(audioId: String): Flow<Boolean> {
        return localDataSource.isAudioRecentPlayed(audioId)
            .map { it.isNotEmpty() }
    }

}