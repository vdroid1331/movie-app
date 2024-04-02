package com.vinayakgupta3112.movieapp.movieList.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie (
    val adult: Boolean,
    val backdrop_path: String,
    val genre_ids: List<Int>,
    val id: Int,
    var media_type: String,
    val origin_country: List<String>,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int,
    var runtime: Int?,
    var videos: List<String>?,
    val first_air_date: String,
    var status: String?,
    var tagline: String?,
    var similar_media_list: List<Int>,
    val original_name: String,
    val category: String
): Parcelable