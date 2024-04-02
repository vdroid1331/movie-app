package com.vinayakgupta3112.movieapp.movieList.data.repository

import com.vinayakgupta3112.movieapp.movieList.data.mappers.toMovie
import com.vinayakgupta3112.movieapp.movieList.data.remote.MovieApi
import com.vinayakgupta3112.movieapp.movieList.domain.model.Movie
import com.vinayakgupta3112.movieapp.movieList.domain.repository.SearchRepository
import com.vinayakgupta3112.movieapp.movieList.util.Constants
import com.vinayakgupta3112.movieapp.movieList.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
) : SearchRepository {

    override suspend fun getSearchList(
        fetchFromRemote: Boolean,
        query: String,
        page: Int,
        apiKey: String
    ): Flow<Resource<List<Movie>>> {

        return flow {
            emit(Resource.Loading(true))

            val remoteMovieList = try {
                movieApi.getSearchList(query, page, apiKey).results.map { movie ->
                    movie.toMovie(
                        type = movie.media_type ?: Constants.unavailable,
                        category = movie.category ?: Constants.unavailable
                    )
                }
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                emit(Resource.Loading(false))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                emit(Resource.Loading(false))
                return@flow
            }

            emit(Resource.Success(remoteMovieList))

            emit(Resource.Loading(false))
        }
    }
}