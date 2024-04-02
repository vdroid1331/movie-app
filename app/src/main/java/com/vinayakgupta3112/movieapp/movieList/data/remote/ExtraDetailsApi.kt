package com.vinayakgupta3112.movieapp.movieList.data.remote

import com.vinayakgupta3112.movieapp.movieList.data.remote.respond.DetailsDto
import com.vinayakgupta3112.movieapp.movieList.data.remote.respond.MovieListDto
import com.vinayakgupta3112.movieapp.movieList.data.remote.respond.VideosList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ExtraDetailsApi {

    @GET("{type}/{id}")
    suspend fun getDetails(
        @Path("type") type: String,
        @Path("id") id: Int,
        @Query("api_key") apiKey: String
    ): DetailsDto

    @GET("{type}/{id}/similar")
    suspend fun getSimilarMediaList(
        @Path("type") type: String,
        @Path("id") id: Int,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String
    ): MovieListDto

    @GET("{type}/{id}/videos")
    suspend fun getVideosList(
        @Path("type") type: String,
        @Path("id") id: Int,
        @Query("api_key") apiKey: String
    ): VideosList

}