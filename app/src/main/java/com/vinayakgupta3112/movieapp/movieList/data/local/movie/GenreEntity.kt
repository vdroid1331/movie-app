package com.vinayakgupta3112.movieapp.movieList.data.local.movie

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GenreEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val type: String
)