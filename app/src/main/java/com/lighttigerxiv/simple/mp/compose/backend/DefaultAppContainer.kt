package com.lighttigerxiv.simple.mp.compose.backend

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.lighttigerxiv.simple.mp.compose.backend.repositories.LibraryRepository
import com.lighttigerxiv.simple.mp.compose.backend.repositories.InternalStorageRepository
import com.lighttigerxiv.simple.mp.compose.backend.repositories.PlaybackRepository
import com.lighttigerxiv.simple.mp.compose.backend.repositories.PlaylistsRepository
import com.lighttigerxiv.simple.mp.compose.backend.repositories.SettingsRepository

class DefaultAppContainer(
    application: Application,
    dataStore: DataStore<Preferences>
) : AppContainer {
    override val libraryRepository: LibraryRepository by lazy {
        LibraryRepository(application, settingsRepository, playlistsRepository)
    }

    override val playbackRepository: PlaybackRepository by lazy {
        PlaybackRepository(libraryRepository, application)
    }

    override val playlistsRepository: PlaylistsRepository by lazy {
        PlaylistsRepository()
    }

    override val settingsRepository: SettingsRepository by lazy {
        SettingsRepository(dataStore)
    }

    override val internalStorageRepository: InternalStorageRepository by lazy {
        InternalStorageRepository(application)
    }
}