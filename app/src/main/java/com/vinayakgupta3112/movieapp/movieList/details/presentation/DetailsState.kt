package com.vinayakgupta3112.movieapp.movieList.details.presentation

import com.vinayakgupta3112.movieapp.movieList.domain.model.Movie

data class DetailsState(
    val isLoading: Boolean = false,
    val movie: Movie? = null
)
