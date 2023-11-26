package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.albums

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.lighttigerxiv.simple.mp.compose.SimpleMPApplication
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Album
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Artist
import com.lighttigerxiv.simple.mp.compose.backend.repositories.LibraryRepository
import com.lighttigerxiv.simple.mp.compose.backend.repositories.SettingsRepository
import com.lighttigerxiv.simple.mp.compose.backend.settings.SettingsOptions
import com.lighttigerxiv.simple.mp.compose.backend.utils.matchesSearch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AlbumsScreenVM(
    private val libraryRepository: LibraryRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    companion object Factory {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {

                val application = (this[APPLICATION_KEY] as SimpleMPApplication)
                val libraryRepository = application.container.libraryRepository
                val settingsRepository = application.container.settingsRepository

                AlbumsScreenVM(libraryRepository, settingsRepository)
            }
        }
    }

    data class UiState(
        val searchText: String = "",
        val albums: List<Album> = ArrayList(),
        val showMenu: Boolean = false,
        val sortType: String = ""
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private var albums: List<Album> = ArrayList()

    init {
        viewModelScope.launch(Dispatchers.Main) {
            settingsRepository.settingsFlow.collect { newSettings ->

                _uiState.update { uiState.value.copy(sortType = newSettings.albumsSort) }

                viewModelScope.launch(Dispatchers.Main) {
                    libraryRepository.albums.collect { newAlbums ->

                        val sortedAlbums = newAlbums.sorted(newSettings.albumsSort)

                        albums = sortedAlbums
                        _uiState.update { uiState.value.copy(albums = sortedAlbums) }
                    }
                }
            }
        }
    }

    fun getAlbumArt(albumId: Long): Bitmap? {
        return libraryRepository.getLargeAlbumArt(albumId)
    }

    private fun filter() {
        _uiState.update {
            uiState.value.copy(
                albums = albums.filter { album -> matchesSearch(album.name, uiState.value.searchText) }
            )
        }
    }

    fun updateSearchText(text: String) {
        _uiState.update { uiState.value.copy(searchText = text) }
        filter()
    }

    fun updateShowMenu(v: Boolean) {
        _uiState.update { uiState.value.copy(showMenu = v) }
    }

    fun updateSort(sortType: String) {
        viewModelScope.launch(Dispatchers.Main) {
            settingsRepository.updateAlbumsSort(sortType)

            albums = libraryRepository.albums.value.sorted(sortType)
        }
    }

    private fun List<Album>.sorted(sortType: String): List<Album> {
        return when (sortType) {
            SettingsOptions.Sort.DEFAULT_REVERSED -> this.reversed()
            SettingsOptions.Sort.ALPHABETICALLY_ASCENDENT -> this.sortedBy { it.name }
            SettingsOptions.Sort.ALPHABETICALLY_DESCENDENT -> this.sortedByDescending { it.name }
            else -> this
        }
    }
}