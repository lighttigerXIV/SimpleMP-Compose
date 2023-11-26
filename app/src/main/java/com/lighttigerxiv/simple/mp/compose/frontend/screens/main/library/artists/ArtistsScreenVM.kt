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
import com.lighttigerxiv.simple.mp.compose.backend.repositories.SettingsRepository
import com.lighttigerxiv.simple.mp.compose.backend.settings.SettingsOptions
import com.lighttigerxiv.simple.mp.compose.backend.utils.matchesSearch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ArtistsScreenVM(
    private val libraryRepository: LibraryRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    companion object Factory {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {

                val application = (this[APPLICATION_KEY] as SimpleMPApplication)
                val libraryRepository = application.container.libraryRepository
                val settingsRepository = application.container.settingsRepository

                ArtistsScreenVM(libraryRepository, settingsRepository)
            }
        }
    }

    data class UiState(
        val searchText: String = "",
        val artists: List<Artist> = ArrayList(),
        val sortType: String = "",
        val showMenu: Boolean = false
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    var artists: List<Artist> = ArrayList()

    init {
        viewModelScope.launch(Dispatchers.Main) {
            settingsRepository.settingsFlow.collect { newSettings ->

                _uiState.update { uiState.value.copy(sortType = newSettings.artistsSort) }

                viewModelScope.launch(Dispatchers.Main) {
                    libraryRepository.artists.collect { newArtists ->

                        val sortedArtists = newArtists.sorted(newSettings.artistsSort)

                        artists = sortedArtists
                        _uiState.update { uiState.value.copy(artists = sortedArtists) }
                    }
                }
            }
        }
    }

    private fun filter() {
        _uiState.update {
            uiState.value.copy(
                artists = artists.filter { artist -> matchesSearch(artist.name, uiState.value.searchText) }
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
            settingsRepository.updateArtistsSort(sortType)

            artists = libraryRepository.artists.value.sorted(sortType)
        }
    }

    private fun List<Artist>.sorted(sortType: String): List<Artist> {
        return when (sortType) {
            SettingsOptions.Sort.DEFAULT_REVERSED -> this.reversed()
            SettingsOptions.Sort.ALPHABETICALLY_ASCENDENT -> this.sortedBy { it.name }
            SettingsOptions.Sort.ALPHABETICALLY_DESCENDENT -> this.sortedByDescending { it.name }
            else -> this
        }
    }
}