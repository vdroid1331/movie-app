package com.vinayakgupta3112.movieapp.movieList.data.mappers

import com.vinayakgupta3112.movieapp.movieList.data.local.movie.MovieEntity
import com.vinayakgupta3112.movieapp.movieList.data.remote.respond.MovieDto
import com.vinayakgupta3112.movieapp.movieList.domain.model.Movie

fun MovieDto.toMovieEntity(
    type: String = "",
    category: String
): MovieEntity {
    return MovieEntity(
        adult = adult ?: false,
        backdrop_path = backdrop_path ?: "",
        original_language = original_language ?: "",
        overview = overview ?: "",
        poster_path = poster_path ?: "",
        release_date = release_date ?: "-1,-2",
        title = title ?: "",
        vote_average = vote_average ?: 0.0,
        popularity = popularity ?: 0.0,
        vote_count = vote_count ?: 0,
        id = id ?: 1,
        original_title = original_title ?: "",
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
        original_name = original_name ?: "",
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
        backdrop_path = backdrop_path,
        original_language = original_language,
        overview = overview,
        poster_path = poster_path,
        release_date = release_date,
        title = title,
        vote_average = vote_average,
        popularity = popularity,
        vote_count = vote_count,
        video = video,
        id = id,
        adult = adult,
        original_title = original_title,

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