package com.lighttigerxiv.simple.mp.compose.data

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(
    entities = [
        Playlist::class,
        Artist::class,
        PlaylistSong::class
    ],
    version = 4,
    autoMigrations = [
        AutoMigration(from = 3, to = 4)
    ],
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract val playlistDao: PlaylistDao
    abstract val artistsDao: ArtistsDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        @OptIn(InternalCoroutinesApi::class)
        fun getInstance(context: Context): AppDatabase {

            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).allowMainThreadQueries().build().also {
                    INSTANCE = it
                }
            }
        }
    }
}