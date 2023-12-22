package com.lighttigerxiv.simple.mp.compose.backend

import com.lighttigerxiv.simple.mp.compose.backend.repositories.LibraryRepository
import com.lighttigerxiv.simple.mp.compose.backend.repositories.InternalStorageRepository
import com.lighttigerxiv.simple.mp.compose.backend.repositories.PlaybackRepository
import com.lighttigerxiv.simple.mp.compose.backend.repositories.PlaylistsRepository
import com.lighttigerxiv.simple.mp.compose.backend.repositories.SettingsRepository

interface AppContainer {
    val libraryRepository: LibraryRepository
    val settingsRepository: SettingsRepository
    val playbackRepository: PlaybackRepository
    val playlistsRepository: PlaylistsRepository
    val internalStorageRepository: InternalStorageRepository
}