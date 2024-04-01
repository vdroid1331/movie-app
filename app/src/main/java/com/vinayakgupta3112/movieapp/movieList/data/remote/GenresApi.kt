package com.vinayakgupta3112.movieapp.movieList.data.remote

import com.vinayakgupta3112.movieapp.movieList.data.remote.respond.GenresListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GenresApi {

    @GET("genre/{type}/list")
    suspend fun getGenresList(
        @Path("type") genre: String,
        @Query("api_key") apiKey: String
    ): GenresListDto

}