package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.artists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.lighttigerxiv.simple.mp.compose.SimpleMPApplication
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Artist
import com.lighttigerxiv.simple.mp.compose.backend.repositories.LibraryRepository
import com.lighttigerxiv.simple.mp.compose.backend.utils.matchesSearch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ArtistsScreenVM(
    libraryRepository: LibraryRepository
) : ViewModel() {
    companion object Factory {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {

                val application = (this[APPLICATION_KEY] as SimpleMPApplication)
                val libraryRepository = application.container.libraryRepository

                ArtistsScreenVM(libraryRepository)
            }
        }
    }

    data class UiState(
        val searchText: String,
        val artists: List<Artist>
    )

    private val _uiState = MutableStateFlow(
        UiState(
            searchText = "",
            artists = ArrayList()
        )
    )
    val uiState = _uiState.asStateFlow()

    private val _artists = MutableStateFlow<List<Artist>>(ArrayList())
    val artists = _artists.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.Main) {
            libraryRepository.artists.collect { newArtists ->
                _artists.update { newArtists }
                _uiState.update { uiState.value.copy(artists = newArtists) }
            }
        }
    }

    private fun filter() {
        _uiState.update {
            uiState.value.copy(
                artists = artists.value.filter { artist -> matchesSearch(artist.name, uiState.value.searchText) }
            )
        }
    }

    fun updateSearchText(text: String) {
        _uiState.update { uiState.value.copy(searchText = text) }
        filter()
    }
}