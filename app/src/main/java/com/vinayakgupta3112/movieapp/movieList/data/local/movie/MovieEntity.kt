package com.vinayakgupta3112.movieapp.movieList.data.local.movie

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MovieEntity (
    val adult: Boolean,
    val backdrop_path: String,
    val genre_ids: String,
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
    var runtime: Int,
    var videos: String,
    val first_air_date: String,
    var status: String,
    var media_type: String,
    val origin_country: String,
    var tagline: String,
    var similar_media_list: String,
    val original_name: String,
    @PrimaryKey val id: Int,
    val category: String
)