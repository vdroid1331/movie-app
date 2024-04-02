package com.vinayakgupta3112.movieapp.movieList.details.presentation

import com.vinayakgupta3112.movieapp.movieList.domain.model.Movie

sealed class SearchUiEvents {
    data class Refresh(val type: String) : SearchUiEvents()
    data class OnPaginate(val type: String) : SearchUiEvents()
    data class OnSearchQueryChanged(val query: String) : SearchUiEvents()
    data class OnSearchedItemClick(val media: Movie) : SearchUiEvents()
}