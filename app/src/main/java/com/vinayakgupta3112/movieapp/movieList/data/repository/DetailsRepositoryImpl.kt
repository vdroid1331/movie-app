package com.vinayakgupta3112.movieapp.movieList.data.repository

import com.vinayakgupta3112.movieapp.movieList.data.local.movie.MovieDatabase
import com.vinayakgupta3112.movieapp.movieList.data.mappers.toMovie
import com.vinayakgupta3112.movieapp.movieList.data.remote.ExtraDetailsApi
import com.vinayakgupta3112.movieapp.movieList.data.remote.respond.DetailsDto
import com.vinayakgupta3112.movieapp.movieList.domain.model.Movie
import com.vinayakgupta3112.movieapp.movieList.domain.repository.DetailsRepository
import com.vinayakgupta3112.movieapp.movieList.util.Constants
import com.vinayakgupta3112.movieapp.movieList.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailsRepositoryImpl @Inject constructor(
    private val extraDetailsApi: ExtraDetailsApi,
    movieDb: MovieDatabase
) : DetailsRepository {

    private val movieDao = movieDb.movieDao

    override suspend fun getDetails(
        type: String,
        isRefresh: Boolean,
        id: Int,
        apiKey: String
    ): Flow<Resource<Movie>> {

        return flow {

            emit(Resource.Loading(true))

            val mediaEntity = movieDao.getMediaById(id = id)

            val doDetailsExist = !(mediaEntity.runtime == null ||
                    mediaEntity.status == null || mediaEntity.tagline == null)

            if (!isRefresh && doDetailsExist) {
                emit(
                    Resource.Success(
                        data = mediaEntity.toMovie(
                            type = mediaEntity.media_type ?: Constants.MOVIE,
                            category = mediaEntity.category ?: Constants.POPULAR
                        )
                    )
                )

                emit(Resource.Loading(false))
                return@flow
            }

            val remoteDetails = fetchRemoteForDetails(
                type = mediaEntity.media_type ?: Constants.MOVIE,
                id = id,
                apiKey = apiKey
            )

            if (remoteDetails == null) {emit(
                Resource.Success(
                    data = mediaEntity.toMovie(
                        type = mediaEntity.media_type ?: Constants.MOVIE,
                        category = mediaEntity.category ?: Constants.POPULAR
                    )
                )
            )
                emit(Resource.Loading(false))
                return@flow
            }

            remoteDetails.let { details ->

                mediaEntity.runtime = details.runtime
                mediaEntity.status = details.status
                mediaEntity.tagline = details.tagline

                movieDao.updateMediaItem(mediaEntity)

                emit(
                    Resource.Success(
                        data = mediaEntity.toMovie(
                            type = mediaEntity.media_type ?: Constants.MOVIE,
                            category = mediaEntity.category ?: Constants.POPULAR
                        )
                    )
                )

                emit(Resource.Loading(false))

            }


        }

    }

    private suspend fun fetchRemoteForDetails(
        type: String,
        id: Int,
        apiKey: String
    ): DetailsDto? {
        val remoteDetails = try {
            extraDetailsApi.getDetails(
                type = type,
                id = id,
                apiKey = apiKey
            )
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } catch (e: HttpException) {
            e.printStackTrace()
            null
        }

        return remoteDetails

    }

}