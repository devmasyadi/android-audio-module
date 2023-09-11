package com.google.android.material.audio.data.local.room

import androidx.room.*
import com.google.android.material.audio.model.Playlist
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