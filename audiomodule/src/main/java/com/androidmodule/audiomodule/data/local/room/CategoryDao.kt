package com.androidmodule.audiomodule.data.local.room

import androidx.room.*
import com.androidmodule.audiomodule.model.Playlist
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createCategory(categoryModel: Playlist)

    @Query("SELECT * FROM categories")
    fun getAllCategory(): Flow<List<Playlist>>

    @Delete
    suspend fun deleteCategory(categoryModel: Playlist)
}