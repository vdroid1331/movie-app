package com.vinayakgupta3112.movieapp.movieList.data.local.movie

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

import androidx.room.Upsert

@Dao
interface MovieDao {
    @Upsert
    suspend fun upsertMovieList(movieList: List<MovieEntity>)

    @Query("SELECT * FROM MovieEntity WHERE id = :id")
    suspend fun getMovieById(id: Int): MovieEntity

    @Query("SELECT * FROM MovieEntity WHERE category = :category")
    suspend fun getMovieListByCategory(category: String): List<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMediaList(
        mediaEntities: List<MovieEntity>
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMediaItem(
        mediaItem: MovieEntity
    )

    @Update
    suspend fun updateMediaItem(
        mediaItem: MovieEntity
    )

    @Query(
        """
            DELETE FROM MovieEntity 
            WHERE media_type = :media_type AND category = :category
        """
    )
    suspend fun deleteMediaByTypeAndCategory(media_type: String, category: String)

    @Query("SELECT * FROM MovieEntity WHERE id = :id")
    suspend fun getMediaById(id: Int): MovieEntity

    @Query(
        """
            SELECT * 
            FROM MovieEntity 
            WHERE media_type = :media_type AND category = :category
        """
    )
    suspend fun getMediaListByTypeAndCategory(
        media_type: String, category: String
    ): List<MovieEntity>

    @Query(
        """
            DELETE FROM MovieEntity 
            WHERE category = :category
        """
    )
    suspend fun deleteTrendingMediaList(category: String)


    @Query(
        """
            SELECT * 
            FROM MovieEntity 
            WHERE category = :category
        """
    )
    suspend fun getTrendingMediaList(category: String): List<MovieEntity>
}