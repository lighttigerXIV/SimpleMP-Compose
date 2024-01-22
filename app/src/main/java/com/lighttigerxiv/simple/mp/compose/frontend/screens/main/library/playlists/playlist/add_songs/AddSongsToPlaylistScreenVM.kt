package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.playlists.playlist.add_songs

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.lighttigerxiv.simple.mp.compose.SimpleMPApplication
import com.lighttigerxiv.simple.mp.compose.backend.realm.Queries
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Song
import com.lighttigerxiv.simple.mp.compose.backend.realm.getRealm
import com.lighttigerxiv.simple.mp.compose.backend.repositories.LibraryRepository
import com.lighttigerxiv.simple.mp.compose.backend.repositories.PlaylistsRepository
import com.lighttigerxiv.simple.mp.compose.backend.utils.matchesSearch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId

class AddSongsToPlaylistScreenVM(
    private val libraryRepository: LibraryRepository,
    private val playlistsRepository: PlaylistsRepository
) : ViewModel() {
    companion object Factory {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as SimpleMPApplication)
                val libraryRepository = app.container.libraryRepository
                val playlistsRepository = app.container.playlistsRepository

                AddSongsToPlaylistScreenVM(libraryRepository, playlistsRepository)
            }
        }
    }

    data class UiState(
        val requestedLoading: Boolean = false,
        val isLoading: Boolean = true,
        val songs: List<Song> = ArrayList(),
        val selectedSongsIds: List<Long> = ArrayList(),
        val searchText: String = ""
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private lateinit var songs: List<Song>
    private lateinit var playlistId: ObjectId
    var listPosition = 0

    fun load(id: ObjectId) {
        viewModelScope.launch(Dispatchers.Main) {

            playlistId = id

            _uiState.update { uiState.value.copy(requestedLoading = true) }

            val playlist = playlistsRepository.getUserPlaylist(id)

            playlist?.let {
                songs = libraryRepository.songs.value.filter { !playlist.songs.contains(it.id) }.sortedBy { it.name }
            }

            _uiState.update { uiState.value.copy(isLoading = false, songs = songs) }
        }
    }

    fun getArtistName(artistId: Long): String {
        return libraryRepository.getArtistName(artistId)
    }

    fun getAlbumArt(albumId: Long): Bitmap? {
        return libraryRepository.getSmallAlbumArt(albumId)
    }

    fun toggleSong(songId: Long) {

        val newSelectedSongsIds = uiState.value.selectedSongsIds.toMutableList()

        if (uiState.value.selectedSongsIds.any { it == songId }) {
            newSelectedSongsIds.removeIf { it == songId }
        } else {
            newSelectedSongsIds.add(songId)
        }

        _uiState.update { uiState.value.copy(selectedSongsIds = newSelectedSongsIds) }
    }

    fun updateSearchText(text: String) {
        _uiState.update { uiState.value.copy(searchText = text) }
        filterSongs()
    }

    private fun filterSongs() {
        _uiState.update {
            uiState.value.copy(
                songs = songs.filter { matchesSearch(it.name, uiState.value.searchText) }
            )
        }
    }

    fun addSongs() {
        viewModelScope.launch(Dispatchers.Main) {
            val queries = Queries(getRealm())

            uiState.value.selectedSongsIds.forEach { songId ->
                queries.addSongToPlaylist(playlistId, songId)
            }

            playlistsRepository.loadPlaylists(libraryRepository.songs.value)
        }
    }
}