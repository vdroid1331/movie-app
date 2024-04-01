package com.vinayakgupta3112.movieapp.movieList.data.local.movie

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [GenreEntity::class],
    version = 1
)
abstract class GenresDatabase: RoomDatabase() {
    abstract val genreDao: GenreDao
}