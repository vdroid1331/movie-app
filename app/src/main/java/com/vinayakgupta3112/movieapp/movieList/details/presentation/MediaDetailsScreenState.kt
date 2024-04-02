package com.vinayakgupta3112.movieapp.movieList.details.presentation

import com.vinayakgupta3112.movieapp.movieList.domain.model.Genre
import com.vinayakgupta3112.movieapp.movieList.domain.model.Movie

data class MediaDetailsScreenState(

    val isLoading: Boolean = false,

    val media: Movie? = null,

    val videoId: String = "",
    val readableTime: String = "",

    val similarMediaList: List<Movie> = emptyList(),
    val smallSimilarMediaList: List<Movie> = emptyList(),

    val videosList: List<String> = emptyList(),
    val moviesGenresList: List<Genre> = emptyList(),
    val tvGenresList: List<Genre> = emptyList()

)