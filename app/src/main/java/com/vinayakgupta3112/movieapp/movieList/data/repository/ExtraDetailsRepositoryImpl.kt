package com.vinayakgupta3112.movieapp.movieList.data.repository

import com.vinayakgupta3112.movieapp.movieList.data.local.movie.MovieDatabase
import com.vinayakgupta3112.movieapp.movieList.data.local.movie.MovieEntity
import com.vinayakgupta3112.movieapp.movieList.data.mappers.toMovie
import com.vinayakgupta3112.movieapp.movieList.data.mappers.toMovieEntity
import com.vinayakgupta3112.movieapp.movieList.data.remote.ExtraDetailsApi
import com.vinayakgupta3112.movieapp.movieList.data.remote.respond.MovieDto
import com.vinayakgupta3112.movieapp.movieList.domain.model.Cast
import com.vinayakgupta3112.movieapp.movieList.domain.model.Movie
import com.vinayakgupta3112.movieapp.movieList.domain.repository.ExtraDetailsRepository
import com.vinayakgupta3112.movieapp.movieList.util.Constants
import com.vinayakgupta3112.movieapp.movieList.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExtraDetailsRepositoryImpl @Inject constructor(
    private val extraDetailsApi: ExtraDetailsApi,
    movieDb: MovieDatabase
) : ExtraDetailsRepository {

    private val movieDao = movieDb.movieDao

    override suspend fun getSimilarMediaList(
        isRefresh: Boolean,
        type: String,
        id: Int,
        page: Int,
        apiKey: String
    ): Flow<Resource<List<Movie>>> {

        return flow {

            emit(Resource.Loading(true))

            val mediaEntity = movieDao.getMediaById(id = id)

            val doesSimilarMediaListExist =
                (mediaEntity.similar_media_list != null && mediaEntity.similar_media_list != "-1,-2")

            if (!isRefresh && doesSimilarMediaListExist) {

                try {
                    val similarMediaListIds =
                        mediaEntity.similar_media_list?.split(",")!!.map { it.toInt() }

                    val similarMediaEntityList = ArrayList<MovieEntity>()
                    for (i in similarMediaListIds.indices) {
                        similarMediaEntityList.add(movieDao.getMediaById(similarMediaListIds[i]))
                    }
                    emit(
                        Resource.Success(
                            data = similarMediaEntityList.map {
                                it.toMovie(
                                    type = it.media_type ?: Constants.MOVIE,
                                    category = it.category ?: Constants.POPULAR
                                )
                            }
                        )
                    )
                } catch (e: Exception) {
                    emit(Resource.Error("Something went wrong."))
                }


                emit(Resource.Loading(false))
                return@flow


            }

            val remoteSimilarMediaList = fetchRemoteForSimilarMediaList(
                type = mediaEntity.media_type ?: Constants.MOVIE,
                id = id,
                page = page,
                apiKey = apiKey
            )

            if (remoteSimilarMediaList == null) {
                emit(
                    Resource.Success(
                        data = emptyList()
                    )
                )
                emit(Resource.Loading(false))
                return@flow
            }

            remoteSimilarMediaList.let { similarMediaList ->

                val similarMediaListIntIds = ArrayList<Int>()
                for (i in similarMediaList.indices) {
                    similarMediaListIntIds.add(similarMediaList[i].id ?: -1)
                }

                mediaEntity.similar_media_list = try {
                    similarMediaListIntIds.joinToString(",")
                } catch (e: Exception) {
                    "-1,-2"
                }

                val similarMediaEntityList = remoteSimilarMediaList.map {
                    it.toMovieEntity(
                        type = it.media_type ?: Constants.MOVIE,
                        category = mediaEntity.category ?: Constants.POPULAR
                    )
                }

                movieDao.insertMediaList(similarMediaEntityList)
                movieDao.updateMediaItem(mediaEntity)

                emit(
                    Resource.Success(
                        data = similarMediaEntityList.map {
                            it.toMovie(
                                type = it.media_type ?: Constants.MOVIE,
                                category = it.category ?: Constants.POPULAR
                            )
                        }
                    )
                )

                emit(Resource.Loading(false))

            }


        }

    }

    private suspend fun fetchRemoteForSimilarMediaList(
        type: String,
        id: Int,
        page: Int,
        apiKey: String
    ): List<MovieDto>? {

        val remoteSimilarMediaList = try {
            extraDetailsApi.getSimilarMediaList(
                type = type,
                id = id,
                page = page,
                apiKey = apiKey
            )
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } catch (e: HttpException) {
            e.printStackTrace()
            null
        }

        return remoteSimilarMediaList?.results

    }


    override suspend fun getVideosList(
        isRefresh: Boolean,
        id: Int,
        apiKey: String
    ): Flow<Resource<List<String>>> {

        return flow {

            emit(Resource.Loading(true))

            val mediaEntity = movieDao.getMediaById(id = id)

            val doVideosExist = (mediaEntity.videos != null)

            if (!isRefresh && doVideosExist) {

                try {
                    val videosIds =
                        mediaEntity.videos?.split(",")!!.map { it }

                    emit(
                        Resource.Success(data = videosIds)
                    )
                } catch (e: Exception) {
                    emit(Resource.Error("Something went wrong."))
                }

                emit(Resource.Loading(false))
                return@flow


            }

            val videosIds = fetchRemoteForVideosIds(
                type = mediaEntity.media_type ?: Constants.MOVIE,
                id = id,
                apiKey = apiKey
            )

            if (videosIds == null) {
                emit(
                    Resource.Success(
                        data = emptyList()
                    )
                )
                emit(Resource.Loading(false))
                return@flow
            }

            videosIds.let {
                mediaEntity.videos = try {
                    it.joinToString(",")
                } catch (e: Exception) {
                    "-1,-2"
                }

                movieDao.updateMediaItem(mediaEntity)

                emit(
                    Resource.Success(data = videosIds)
                )

                emit(Resource.Loading(false))

            }


        }
    }


    private suspend fun fetchRemoteForVideosIds(
        type: String,
        id: Int,
        apiKey: String
    ): List<String>? {

        val remoteVideosIds = try {
            extraDetailsApi.getVideosList(
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

        val listToReturn = remoteVideosIds?.results?.filter {
            it.site == "YouTube" && it.type == "Featurette" || it.type == "Teaser"
        }

        return listToReturn?.map {
            it.key
        }

    }



    // later
    override suspend fun getCastList(
        isRefresh: Boolean,
        id: Int,
        apiKey: String
    ): Flow<Resource<Cast>> {

        return flow {

//            emit(Resource.Loading(true))
//
//            val mediaEntity = mediaDao.getMediaById(id = id)
//
//            val doCastExist = !(mediaEntity.runtime == null ||
//                    mediaEntity.status == null || mediaEntity.tagline == null)
//
//            if (!isRefresh && doCastExist) {
//                emit(
//                    Resource.Success(
//                        data = mediaEntity.toMedia(
//                            type = mediaEntity.media_type ?: Constants.MOVIE,
//                            category = mediaEntity.category ?: Constants.POPULAR
//                        )
//                    )
//                )
//
//                emit(Resource.Loading(false))
//                return@flow
//            }
//
//            val remoteDetails = fetchRemoteForDetails(
//                type = mediaEntity.media_type ?: Constants.MOVIE,
//                id = id,
//                apiKey = apiKey
//            )
//
//            if (remoteDetails == null) {emit(
//                Resource.Success(
//                    data = mediaEntity.toMedia(
//                        type = mediaEntity.media_type ?: Constants.MOVIE,
//                        category = mediaEntity.category ?: Constants.POPULAR
//                    )
//                )
//            )
//                emit(Resource.Loading(false))
//                return@flow
//            }
//
//            remoteDetails.let { details ->
//
//                mediaEntity.runtime = details.runtime
//                mediaEntity.status = details.status
//                mediaEntity.tagline = details.tagline
//
//                mediaDao.updateMediaItem(mediaEntity)
//
//                emit(
//                    Resource.Success(
//                        data = mediaEntity.toMedia(
//                            type = mediaEntity.media_type ?: Constants.MOVIE,
//                            category = mediaEntity.category ?: Constants.POPULAR
//                        )
//                    )
//                )
//
//                emit(Resource.Loading(false))
//
//            }


        }

    }


}