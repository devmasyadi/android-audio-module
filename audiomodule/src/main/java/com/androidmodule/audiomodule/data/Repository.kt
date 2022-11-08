package com.androidmodule.audiomodule.data

import com.androidmodule.audiomodule.data.local.LocalDataSource
import com.androidmodule.audiomodule.data.remote.RemoteDataSource
import com.androidmodule.audiomodule.model.*
import com.androidmodule.audiomodule.utils.AppExecutors
import com.androidmodule.audiomodule.utils.AudioModuleUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.util.*

class Repository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : IRepository {
    override fun getData(packageName: String): Flow<Resource<AudioModel>> {
        return object : NetworkBoundResource<AudioModel, AudioModel>() {
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

    override fun listAudioFavorite(): Flow<Resource<List<AudiosItem>>> {
        return flow {
            try {
                emit(Resource.Loading(true))
                emitAll(localDataSource.listAudioFavorite().map {
                    Resource.Success(it)}
                )
            } catch (e: Exception) {
                emit(Resource.Error(e.toString()))
            } finally {
                emit(Resource.Loading(false))
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

    override fun getAllPlaylist(): Flow<Resource<List<Playlist>>> {
        return flow {
            try {
                emit(Resource.Loading(true))
                emitAll(localDataSource.getAllPlaylist().map {
                    Resource.Success(it)}
                )
            } catch (e: Exception) {
                emit(Resource.Error(e.toString()))
            } finally {
                emit(Resource.Loading(false))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getAllPlaylistWithAudios(): Flow<Resource<List<PlaylistWithAudios>>> {
        return flow {
            try {
                emit(Resource.Loading(true))
                emitAll(localDataSource.getAllPlaylistWithAudios().map {
                    Resource.Success(it)}
                )
            } catch (e: Exception) {
                emit(Resource.Error(e.toString()))
            } finally {
                emit(Resource.Loading(false))
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

    override fun getAudiosByPlaylistId(playlistId: Int): Flow<Resource<PlaylistWithAudios>> {
        return flow {
            try {
                emit(Resource.Loading(true))
                emitAll(localDataSource.getAudiosByPlaylistId(playlistId).map {
                    Resource.Success(it)}
                )
            } catch (e: Exception) {
                emit(Resource.Error(e.toString()))
            } finally {
                emit(Resource.Loading(false))
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

    override fun listAudioDownload(): Flow<Resource<List<AudiosItem>>> {
        return flow {
            try {
                emit(Resource.Loading(true))
                emitAll(localDataSource.listAudioDownload().map {
                    Resource.Success(it)}
                )
            } catch (e: Exception) {
                emit(Resource.Error(e.toString()))
            } finally {
                emit(Resource.Loading(false))
            }
        }.flowOn(Dispatchers.IO)
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

    override fun listAudioRecentPlayed(): Flow<Resource<List<AudiosItem>>> {
        return flow {
            try {
                emit(Resource.Loading(true))
                emitAll(localDataSource.listAudioRecentPlayed().map {
                    Resource.Success(it)}
                )
            } catch (e: Exception) {
                emit(Resource.Error(e.toString()))
            } finally {
                emit(Resource.Loading(false))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun isAudioRecentPlayed(audioId: String): Flow<Boolean> {
        return localDataSource.isAudioRecentPlayed(audioId)
            .map { it.isNotEmpty() }
    }

}