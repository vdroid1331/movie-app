package com.vinayakgupta3112.movieapp.movieList.domain.repository

import com.vinayakgupta3112.movieapp.movieList.domain.model.Genre
import com.vinayakgupta3112.movieapp.movieList.util.Resource
import kotlinx.coroutines.flow.Flow

interface GenreRepository {
    suspend fun getGenres(
        fetchFromRemote: Boolean,
        type: String,
        apiKey: String
    ): Flow<Resource<List<Genre>>>
}