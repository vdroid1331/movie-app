package com.vinayakgupta3112.movieapp.movieList.data.remote.respond

import com.vinayakgupta3112.movieapp.movieList.domain.model.Genre

data class GenresListDto(
    val genres: List<Genre>
)
