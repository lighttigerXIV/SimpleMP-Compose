package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.add_song_to_playlist

import android.app.Application
import android.graphics.Bitmap
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.lighttigerxiv.simple.mp.compose.R
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
import org.mongodb.kbson.ObjectId

class AddSongToPlaylistScreenVM(
    private val application: Application,
    private val libraryRepository: LibraryRepository,
    private val playlistsRepository: PlaylistsRepository,
    private val internalStorageRepository: InternalStorageRepository
) : ViewModel() {
    companion object Factory {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as SimpleMPApplication)

                AddSongToPlaylistScreenVM(
                    app,
                    app.container.libraryRepository,
                    app.container.playlistsRepository,
                    app.container.internalStorageRepository
                )
            }
        }
    }

    data class UiState(
        val isLoading: Boolean = true,
        val playlists: List<Playlist> = ArrayList(),
        val showCreatePlaylistDialog: Boolean = false,
        val nameTextCreatePlaylistDialog: String = "",
        val playlistsArts: List<PlaylistArt> = ArrayList()
    ) {
        data class PlaylistArt(
            val id: ObjectId,
            val art: Bitmap?
        )
    }

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val queries = Queries(getRealm())

    init {
        viewModelScope.launch(Dispatchers.Main) {

            val playlists = queries.getUserPlaylists()

            _uiState.update {
                uiState.value.copy(
                    isLoading = false,
                    playlists = playlists
                )
            }

            playlists.forEach { playlist ->
                viewModelScope.launch(Dispatchers.Main) {
                    internalStorageRepository.loadImageFromInternalStorage(playlist._id.toHexString()).collect { art ->
                        val newArts = uiState.value.playlistsArts.toMutableList().apply {
                            add(UiState.PlaylistArt(playlist._id, art))
                        }

                        _uiState.update { state->
                            uiState.value.copy(
                                playlistsArts = newArts,
                                playlists = state.playlists
                            )
                        }
                    }
                }
            }
        }
    }


    fun openCreatePlaylistDialog() {
        _uiState.update { uiState.value.copy(showCreatePlaylistDialog = true) }
    }

    fun cancelCreatePlaylistDialog() {
        _uiState.update {
            uiState.value.copy(
                showCreatePlaylistDialog = false,
                nameTextCreatePlaylistDialog = ""
            )
        }
    }

    fun updateNameTextCreatePlaylistDialog(v: String) {
        _uiState.update { uiState.value.copy(nameTextCreatePlaylistDialog = v) }
    }

    fun createPlaylist() {
        viewModelScope.launch(Dispatchers.Main) {
            queries.createPlaylist(uiState.value.nameTextCreatePlaylistDialog)

            playlistsRepository.loadPlaylists(libraryRepository.songs.value)

            _uiState.update {
                uiState.value.copy(
                    playlists = queries.getUserPlaylists(),
                    showCreatePlaylistDialog = false
                )
            }
        }
    }

    fun addSong(playlistId: ObjectId, songId: Long, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.Main) {

            val playlist = queries.getUserPlaylists().find { it._id == playlistId }
            playlist?.let {
                if (playlist.songs.contains(songId)) {
                    Toast.makeText(application, application.getString(R.string.song_already_in_playlist), Toast.LENGTH_SHORT).show()
                } else {
                    queries.addSongToPlaylist(playlistId, songId)
                    playlistsRepository.loadPlaylists(libraryRepository.songs.value)
                    onSuccess()
                }
            }
        }
    }
}