package com.vinayakgupta3112.movieapp.movieList.presentation

import com.vinayakgupta3112.movieapp.movieList.domain.model.Movie

data class SearchScreenState(

    val searchPage: Int = 1,

    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,

    val searchQuery: String = "",

    val searchList: List<Movie> = emptyList(),


    )