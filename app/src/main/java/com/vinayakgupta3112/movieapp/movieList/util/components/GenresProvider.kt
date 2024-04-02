package com.vinayakgupta3112.movieapp.movieList.util.components

import androidx.compose.runtime.Composable
import com.vinayakgupta3112.movieapp.movieList.domain.model.Genre

@Composable
fun genresProvider(
    genre_ids: List<Int>,
    allGenres: List<Genre>
): String {


    var genres = ""

    for (i in allGenres.indices) {
        for (j in genre_ids.indices) {
            if (allGenres[i].id == genre_ids[j]) {
                genres += "${allGenres[i].name} - "
            }
        }
    }

    return genres.dropLastWhile { it == ' ' || it == '-' }
}