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
import com.lighttigerxiv.simple.mp.compose.backend.repositories.LibraryRepository
import com.lighttigerxiv.simple.mp.compose.backend.utils.matchesSearch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AlbumsScreenVM(
    private val libraryRepository: LibraryRepository
) : ViewModel() {
    companion object Factory {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {

                val application = (this[APPLICATION_KEY] as SimpleMPApplication)
                val libraryRepository = application.container.libraryRepository

                AlbumsScreenVM(libraryRepository)
            }
        }
    }

    data class UiState(
        val searchText: String = "",
        val albums: List<Album> = ArrayList()
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private var albums: List<Album> = ArrayList()

    init {
        viewModelScope.launch(Dispatchers.Main) {
            libraryRepository.albums.collect { newAlbums ->
                albums = newAlbums
                _uiState.update { uiState.value.copy(albums = newAlbums) }
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
}