package com.vinayakgupta3112.movieapp.movieList.data.local.movie

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [MovieEntity::class],
    version = 2
)
abstract class MovieDatabase: RoomDatabase() {
    abstract val movieDao: MovieDao
}

val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Perform the necessary SQL operations to migrate the schema
        // You need to handle the changes in the schema here
        // For example, adding new columns or renaming existing ones
        db.execSQL("ALTER TABLE MovieEntity ADD COLUMN runtime INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE MovieEntity ADD COLUMN videos TEXT NOT NULL DEFAULT ''")
        db.execSQL("ALTER TABLE MovieEntity ADD COLUMN status TEXT NOT NULL DEFAULT ''")
        db.execSQL("ALTER TABLE MovieEntity ADD COLUMN tagline TEXT NOT NULL DEFAULT ''")
        db.execSQL("ALTER TABLE MovieEntity ADD COLUMN first_air_date TEXT NOT NULL DEFAULT ''")
        db.execSQL("ALTER TABLE MovieEntity ADD COLUMN media_type TEXT NOT NULL DEFAULT ''")
        db.execSQL("ALTER TABLE MovieEntity ADD COLUMN origin_country TEXT NOT NULL DEFAULT ''")
        db.execSQL("ALTER TABLE MovieEntity ADD COLUMN original_name TEXT NOT NULL DEFAULT ''")
        db.execSQL("ALTER TABLE MovieEntity ADD COLUMN similar_media_list TEXT NOT NULL DEFAULT ''")

    }
}
