package com.vinayakgupta3112.movieapp.movieList.data.repository

import com.vinayakgupta3112.movieapp.movieList.data.local.movie.MovieDatabase
import com.vinayakgupta3112.movieapp.movieList.data.mappers.toMovie
import com.vinayakgupta3112.movieapp.movieList.data.mappers.toMovieEntity
import com.vinayakgupta3112.movieapp.movieList.data.remote.MovieApi
import com.vinayakgupta3112.movieapp.movieList.domain.model.Movie
import com.vinayakgupta3112.movieapp.movieList.domain.repository.MovieListRepository
import com.vinayakgupta3112.movieapp.movieList.util.Constants
import com.vinayakgupta3112.movieapp.movieList.util.Constants.TRENDING
import com.vinayakgupta3112.movieapp.movieList.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class MovieListRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDatabase: MovieDatabase
) : MovieListRepository {

    private val movieDao = movieDatabase.movieDao
    override suspend fun getMovieList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))
            val localMovieList = movieDatabase.movieDao.getMovieListByCategory(category)
            val shouldLoadLocalMovie = localMovieList.isNotEmpty() && !forceFetchFromRemote
            if (shouldLoadLocalMovie) {
                emit(Resource.Success(
                    data = localMovieList.map { movieEntity ->
                        movieEntity.toMovie(category = category)
                    }
                ))
                emit(Resource.Loading(false))
                return@flow
            }

            val movieListFromApi = try {
                movieApi.getMoviesList(category, page)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error Loading Movies"))
                return@flow
            }
            catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error Loading Movies"))
                return@flow
            }
            catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error Loading Movies"))
                return@flow
            }

            val movieEntities = movieListFromApi.results.let {
                it.map { movieDto ->
                    movieDto.toMovieEntity(category = category)
                }
            }
            movieDatabase.movieDao.upsertMovieList(movieEntities)
            emit(Resource.Success(
                movieEntities.map {
                    it.toMovie(category = category)
                }
            ))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun getMovie(id: Int): Flow<Resource<Movie>> {
        return flow {
            emit(Resource.Loading(true))
            val movieEntity = movieDatabase.movieDao.getMovieById(id)
            if (movieEntity != null) {
                emit(
                    Resource.Success(movieEntity.toMovie(category =  movieEntity.category))
                )
                emit(Resource.Loading(false))
                return@flow
            }
            emit(Resource.Error("Error no such movie"))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun updateItem(movie: Movie) {
        val movieEntity = movie.toMovieEntity()
        movieDao.updateMediaItem(
            mediaItem = movieEntity
        )
    }

    override suspend fun insertItem(movie: Movie) {
        val movieEntity = movie.toMovieEntity()
        movieDao.insertMediaItem(
            mediaItem = movieEntity
        )
    }

    override suspend fun getItem(id: Int, type: String, category: String): Movie {
        return movieDao.getMovieById(id = id).toMovie(
            category = category,
            type = type
        )
    }

    override suspend fun getMoviesAndTvSeriesList(
        fetchFromRemote: Boolean,
        isRefresh: Boolean,
        type: String,
        category: String,
        page: Int,
        apiKey: String
    ): Flow<Resource<List<Movie>>> {
        return flow {

            emit(Resource.Loading(true))

            val localMediaList = movieDao.getMediaListByTypeAndCategory(type, category)

            val shouldJustLoadFromCache =
                localMediaList.isNotEmpty() && !fetchFromRemote && !isRefresh
            if (shouldJustLoadFromCache) {

                emit(Resource.Success(
                    data = localMediaList.map {
                        it.toMovie(
                            type = type,
                            category = category
                        )
                    }
                ))

                emit(Resource.Loading(false))
                return@flow
            }

            var searchPage = page
            if (isRefresh) {
                movieDao.deleteMediaByTypeAndCategory(type, category)
                searchPage = 1
            }

            val remoteMediaList = try {
                movieApi.getMoviesAndTvSeriesList(
                    type, category, searchPage, apiKey
                ).results
            } catch (e: java.io.IOException) {
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

            remoteMediaList.let { mediaList ->
                val media = mediaList.map {
                    it.toMovie(
                        type = type,
                        category = category
                    )
                }

                val entities = mediaList.map {
                    it.toMovieEntity(
                        type = type,
                        category = category,
                    )
                }

                movieDao.insertMediaList(entities)

                emit(
                    Resource.Success(data = media)
                )
                emit(Resource.Loading(false))
            }
        }
    }


    override suspend fun getTrendingList(
        fetchFromRemote: Boolean,
        isRefresh: Boolean,
        type: String,
        time: String,
        page: Int,
        apiKey: String
    ): Flow<Resource<List<Movie>>> {
        return flow {

            emit(Resource.Loading(true))

            val localMediaList = movieDao.getTrendingMediaList(TRENDING)

            val shouldJustLoadFromCache = localMediaList.isNotEmpty() && !fetchFromRemote
            if (shouldJustLoadFromCache) {

                emit(Resource.Success(
                    data = localMediaList.map {
                        it.toMovie(
                            type = it.media_type ?: Constants.unavailable,
                            category = TRENDING
                        )
                    }
                ))

                emit(Resource.Loading(false))
                return@flow
            }

            var searchPage = page

            if (isRefresh) {
                movieDao.deleteTrendingMediaList(TRENDING)
                searchPage = 1
            }

            val remoteMediaList = try {
                movieApi.getTrendingList(
                    type, time, searchPage, apiKey
                ).results
            } catch (e: java.io.IOException) {
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

            remoteMediaList.let { mediaList ->

                val media = mediaList.map {
                    it.toMovie(
                        type = it.media_type ?: Constants.unavailable,
                        category = TRENDING
                    )
                }

                val entities = mediaList.map {
                    it.toMovieEntity(
                        type = it.media_type ?: Constants.unavailable,
                        category = TRENDING
                    )
                }

                movieDao.insertMediaList(entities)

                emit(
                    Resource.Success(data = media)
                )
                emit(Resource.Loading(false))
            }
        }
    }
}