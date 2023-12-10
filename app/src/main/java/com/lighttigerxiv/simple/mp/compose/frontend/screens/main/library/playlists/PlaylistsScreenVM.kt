package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.playlists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.lighttigerxiv.simple.mp.compose.SimpleMPApplication
import com.lighttigerxiv.simple.mp.compose.backend.realm.Queries
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Playlist
import com.lighttigerxiv.simple.mp.compose.backend.realm.getRealm
import com.lighttigerxiv.simple.mp.compose.backend.repositories.LibraryRepository
import com.lighttigerxiv.simple.mp.compose.backend.repositories.PlaylistsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaylistsScreenVM(
    private val playlistsRepository: PlaylistsRepository,
    private val libraryRepository: LibraryRepository
) : ViewModel() {
    companion object Factory {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {

                val app = (this[APPLICATION_KEY] as SimpleMPApplication)
                val playlistsRepository = app.container.playlistsRepository
                val libraryRepository = app.container.libraryRepository

                PlaylistsScreenVM(playlistsRepository, libraryRepository)
            }
        }
    }

    data class UiState(
        val genrePlaylists: List<Playlist> = ArrayList(),
        val userPlaylists: List<Playlist> = ArrayList(),
        val searchText: String = "",
        val showAddPlaylistDialog: Boolean = false,
        val addPlaylistDialogText: String = ""
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.Main) {
            playlistsRepository.genrePlaylists.collect { newGenrePlaylists ->
                _uiState.update { uiState.value.copy(genrePlaylists = newGenrePlaylists) }
            }
        }

        viewModelScope.launch(Dispatchers.Main) {
            playlistsRepository.userPlaylists.collect { newUserPlaylists ->
                _uiState.update { uiState.value.copy(userPlaylists = newUserPlaylists) }
            }
        }

        viewModelScope.launch(Dispatchers.Main) {
            libraryRepository.songs.collect{newSongs ->
                playlistsRepository.loadPlaylists(newSongs)
            }
        }
    }

    fun updateUserPlaylistsSearchText(v: String) {
        _uiState.update { uiState.value.copy(searchText = v) }
    }

    fun updateShowAddPlaylistDialog(v: Boolean) {
        _uiState.update { uiState.value.copy(showAddPlaylistDialog = v) }
    }

    fun updateAddPlaylistDialogText(v: String) {
        _uiState.update { uiState.value.copy(addPlaylistDialogText = v) }
    }

    fun addPlaylist() {
        viewModelScope.launch(Dispatchers.Main) {

            _uiState.update { uiState.value.copy(showAddPlaylistDialog = false) }

            Queries(getRealm()).addPlaylist(uiState.value.addPlaylistDialogText)
            playlistsRepository.loadPlaylists(libraryRepository.songs.value)

            _uiState.update { uiState.value.copy(addPlaylistDialogText = "") }
        }
    }
}