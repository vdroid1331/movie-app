package com.vinayakgupta3112.movieapp.di

import android.app.Application
import androidx.room.Room
import com.vinayakgupta3112.movieapp.movieList.data.local.movie.GenresDatabase
import com.vinayakgupta3112.movieapp.movieList.data.local.movie.MIGRATION_1_2
import com.vinayakgupta3112.movieapp.movieList.data.local.movie.MovieDatabase
import com.vinayakgupta3112.movieapp.movieList.data.remote.ExtraDetailsApi
import com.vinayakgupta3112.movieapp.movieList.data.remote.GenresApi
import com.vinayakgupta3112.movieapp.movieList.data.remote.MovieApi
import com.vinayakgupta3112.movieapp.movieList.data.remote.MovieApi.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor).build()

    @Provides
    @Singleton
    fun provideGenreDatabase(app: Application): GenresDatabase {
        return Room.databaseBuilder(
            app,
            GenresDatabase::class.java,
            "genresdb.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideMovieApi(): MovieApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(MovieApi.BASE_URL)
            .client(client)
            .build()
            .create(MovieApi::class.java)
    }

    @Provides
    @Singleton
    fun providesMovieDatabase(app: Application): MovieDatabase {
        return Room.databaseBuilder(
            app,
            MovieDatabase::class.java,
            "movie.db"
        ).addMigrations(MIGRATION_1_2).build()
    }

    @Singleton
    @Provides
    fun provideGenresApi() : GenresApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client)
            .build()
            .create(GenresApi::class.java)
    }

    @Singleton
    @Provides
    fun provideExtraDetailsApi() : ExtraDetailsApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client)
            .build()
            .create(ExtraDetailsApi::class.java)
    }


}