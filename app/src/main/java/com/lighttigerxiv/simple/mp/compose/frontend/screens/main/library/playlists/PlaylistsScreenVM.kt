package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.playlists

import android.graphics.Bitmap
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
import com.lighttigerxiv.simple.mp.compose.backend.repositories.InternalStorageRepository
import com.lighttigerxiv.simple.mp.compose.backend.repositories.LibraryRepository
import com.lighttigerxiv.simple.mp.compose.backend.repositories.PlaylistsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId

class PlaylistsScreenVM(
    private val playlistsRepository: PlaylistsRepository,
    private val libraryRepository: LibraryRepository,
    private val internalStorageRepository: InternalStorageRepository
) : ViewModel() {
    companion object Factory {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {

                val app = (this[APPLICATION_KEY] as SimpleMPApplication)

                PlaylistsScreenVM(
                    playlistsRepository = app.container.playlistsRepository,
                    libraryRepository = app.container.libraryRepository,
                    internalStorageRepository = app.container.internalStorageRepository
                )
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

    var pagerTab = 0
    var genrePlaylistsGridPosition = 0
    var playlistsPositionGridPosition = 0

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

            Queries(getRealm()).createPlaylist(uiState.value.addPlaylistDialogText)
            playlistsRepository.loadPlaylists(libraryRepository.songs.value)

            _uiState.update { uiState.value.copy(addPlaylistDialogText = "") }
        }
    }

    suspend fun getPlaylistArt(playlistId: ObjectId): Bitmap?{
        var art: Bitmap? = null

        withContext(Dispatchers.IO){
            internalStorageRepository.loadImageFromInternalStorage(playlistId.toHexString()).collect{a->
                art = a
            }
        }

        return art
    }
}