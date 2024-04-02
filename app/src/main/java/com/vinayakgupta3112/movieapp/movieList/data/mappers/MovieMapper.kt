package com.vinayakgupta3112.movieapp.movieList.data.mappers

import com.vinayakgupta3112.movieapp.movieList.data.local.movie.MovieEntity
import com.vinayakgupta3112.movieapp.movieList.data.remote.respond.MovieDto
import com.vinayakgupta3112.movieapp.movieList.domain.model.Movie
import com.vinayakgupta3112.movieapp.movieList.util.Constants

fun MovieDto.toMovieEntity(
    type: String = "",
    category: String
): MovieEntity {
    return MovieEntity(
        adult = adult ?: false,
        backdrop_path = backdrop_path ?: Constants.unavailable,
        original_language = original_language ?: Constants.unavailable,
        overview = overview  ?: Constants.unavailable,
        poster_path = poster_path  ?: Constants.unavailable,
        release_date = release_date ?: "-1,-2",
        title = title ?: Constants.unavailable,
        vote_average = vote_average ?: 0.0,
        popularity = popularity ?: 0.0,
        vote_count = vote_count ?: 0,
        id = id ?: 1,
        original_title = original_title ?: original_name ?: Constants.unavailable,
        video = video ?: false,

        category = category,
        runtime =  0,
        status =  "",
        tagline =  "",
        videos = try {
            videos?.joinToString(",") ?: "-1,-2"
        } catch (e: Exception) {
            "-1,-2"
        },
        first_air_date = first_air_date ?: "",
        media_type = media_type ?: type,
        origin_country = try {
            origin_country?.joinToString(",") ?: "-1,-2"
        } catch (e: Exception) {
            "-1,-2"
        },
        original_name = original_name  ?: Constants.unavailable,
        similar_media_list = try {
            similarMediaList?.joinToString(",") ?: "-1,-2"
        } catch (e: Exception) {
            "-1,-2"
        },


        genre_ids = try {
            genre_ids?.joinToString(",") ?: "-1,-2"
        } catch (e: Exception) {
            "-1,-2"
        }
    )
}

fun MovieEntity.toMovie(
    type: String = "",
    category: String
): Movie {
    return Movie(
        backdrop_path = backdrop_path ?: Constants.unavailable,
        original_language = original_language ?: Constants.unavailable,
        overview = overview ?: Constants.unavailable,
        poster_path = poster_path ?: Constants.unavailable,
        release_date = release_date ?: Constants.unavailable,
        title = title ?: Constants.unavailable,
        vote_average = vote_average ?: 0.0,
        popularity = popularity ?: 0.0,
        vote_count = vote_count ?: 0,
        video = video,
        id = id ?: 1,
        adult = adult ?: false,
        original_title = original_title?: original_name ?: Constants.unavailable,

        category = category,
        first_air_date = first_air_date ?: "",
        media_type = media_type ?: type,
        origin_country = try {
            origin_country?.split(",")!!.map { it }
        } catch (e: Exception) {
            listOf("-1", "-2")
        },
        original_name = original_name ?: "",
        runtime = runtime ?: 0,
        similar_media_list = try {
            similar_media_list?.split(",")!!.map { it.toInt() }
        } catch (e: Exception) {
            listOf(-1, -2)
        },
        status = status ?: "",
        tagline = tagline ?: "",
        videos = try {
            videos?.split(",")?.map { it }
        } catch (e: Exception) {
            listOf("-1", "-2")
        },
        genre_ids = try {
            genre_ids.split(",").map { it.toInt() }
        } catch (e: Exception) {
            listOf(-1, -2)
        }
    )
}


fun MovieDto.toMovie(
    type: String,
    category: String,
): Movie {
    return Movie(
        backdrop_path = backdrop_path ?: Constants.unavailable,
        original_language = original_language ?: Constants.unavailable,
        overview = overview ?: Constants.unavailable,
        poster_path = poster_path ?: Constants.unavailable,
        release_date = release_date ?: Constants.unavailable,
        title = title ?: name ?: Constants.unavailable,
        vote_average = vote_average ?: 0.0,
        popularity = popularity ?: 0.0,
        vote_count = vote_count ?: 0,
        genre_ids = genre_ids ?: emptyList(),
        id = id ?: 1,
        adult = adult ?: false,
        media_type = type,
        category = category,
        origin_country = origin_country ?: emptyList(),
        original_title = original_title ?: original_name ?: Constants.unavailable,
        runtime = null,
        status = null,
        tagline = null,
        videos = videos,
        similar_media_list = similarMediaList ?: emptyList(),
        first_air_date = first_air_date ?: "",
        original_name = original_name ?: "",
        video = video ?: false
    )
}

fun Movie.toMovieEntity(): MovieEntity {
    return MovieEntity(
        backdrop_path = backdrop_path,
        original_language = original_language,
        overview = overview,
        poster_path = poster_path,
        release_date = release_date,
        title = title,
        vote_average = vote_average,
        popularity = popularity,
        vote_count = vote_count,
        genre_ids = try {
            genre_ids.joinToString(",")
        } catch (e: Exception) {
            "-1,-2"
        },
        id = id,
        adult = adult,
        media_type = media_type,
        origin_country = try {
            origin_country.joinToString(",")
        } catch (e: Exception) {
            "-1,-2"
        },
        original_title = original_title,
        category = category,
        videos = try {
            videos?.joinToString(",") ?: "-1,-2"
        } catch (e: Exception) {
            "-1,-2"
        },
        similar_media_list = try {
            similar_media_list.joinToString(",")
        } catch (e: Exception) {
            "-1,-2"
        },
        video = false,
        first_air_date = release_date,
        original_name = original_title,

        status = status ?: "",
        runtime = runtime ?: 0,
        tagline = tagline ?: ""
    )
}