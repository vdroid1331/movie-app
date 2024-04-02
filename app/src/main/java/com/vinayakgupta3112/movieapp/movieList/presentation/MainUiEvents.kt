package com.vinayakgupta3112.movieapp.movieList.presentation

sealed class MainUiEvents {
    data class Refresh(val type: String) : MainUiEvents()
    data class OnPaginate(val type: String) : MainUiEvents()
}