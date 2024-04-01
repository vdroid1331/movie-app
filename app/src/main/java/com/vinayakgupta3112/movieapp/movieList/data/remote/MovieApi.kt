package com.vinayakgupta3112.movieapp.movieList.data.remote

import com.vinayakgupta3112.movieapp.BuildConfig
import com.vinayakgupta3112.movieapp.movieList.data.remote.respond.MovieListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("movie/{category}")
    suspend fun getMoviesList(
        @Path("category") category: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = API_KEY
    ) : MovieListDto

    @GET("{type}/{category}")
    suspend fun getMoviesAndTvSeriesList(
        @Path("type") type: String,
        @Path("category") category: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String
    ): MovieListDto

    @GET("trending/{type}/{time}")
    suspend fun getTrendingList(
        @Path("type") type: String,
        @Path("time") time: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String
    ): MovieListDto

    @GET("search/multi")
    suspend fun getSearchList(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String
    ): MovieListDto

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
        const val API_KEY = BuildConfig.TMDB_API_KEY
    }
}