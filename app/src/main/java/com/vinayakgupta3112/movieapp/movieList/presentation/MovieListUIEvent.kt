package com.vinayakgupta3112.movieapp.movieList.presentation

sealed interface MovieListUIEvent {
    data class Paginate(val category: String): MovieListUIEvent
    object Navigate : MovieListUIEvent
}