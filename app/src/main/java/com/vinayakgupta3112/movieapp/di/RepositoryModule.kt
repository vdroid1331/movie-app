package com.vinayakgupta3112.movieapp.di

import com.vinayakgupta3112.movieapp.movieList.data.repository.DetailsRepositoryImpl
import com.vinayakgupta3112.movieapp.movieList.data.repository.ExtraDetailsRepositoryImpl
import com.vinayakgupta3112.movieapp.movieList.data.repository.GenreRepositoryImpl
import com.vinayakgupta3112.movieapp.movieList.data.repository.MovieListRepositoryImpl
import com.vinayakgupta3112.movieapp.movieList.data.repository.SearchRepositoryImpl
import com.vinayakgupta3112.movieapp.movieList.domain.repository.DetailsRepository
import com.vinayakgupta3112.movieapp.movieList.domain.repository.ExtraDetailsRepository
import com.vinayakgupta3112.movieapp.movieList.domain.repository.GenreRepository
import com.vinayakgupta3112.movieapp.movieList.domain.repository.MovieListRepository
import com.vinayakgupta3112.movieapp.movieList.domain.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMovieListRepository(
        movieListRepositoryImpl: MovieListRepositoryImpl
    ): MovieListRepository

    @Binds
    @Singleton
    abstract fun bindSearchRepository(
        searchRepositoryImpl: SearchRepositoryImpl
    ): SearchRepository

    @Binds
    @Singleton
    abstract fun bindGenreRepository(
        genreRepositoryImpl: GenreRepositoryImpl
    ): GenreRepository

    @Binds
    @Singleton
    abstract fun bindDetailsRepository(
        detailsRepositoryImpl: DetailsRepositoryImpl
    ): DetailsRepository

    @Binds
    @Singleton
    abstract fun bindExtraDetailsRepository(
        extraDetailsRepositoryImpl: ExtraDetailsRepositoryImpl
    ): ExtraDetailsRepository
}