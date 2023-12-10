package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.playlists.playlist

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
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Song
import com.lighttigerxiv.simple.mp.compose.backend.realm.getRealm
import com.lighttigerxiv.simple.mp.compose.backend.repositories.LibraryRepository
import com.lighttigerxiv.simple.mp.compose.backend.repositories.PlaybackRepository
import com.lighttigerxiv.simple.mp.compose.backend.repositories.PlaylistsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId

class PlaylistScreenVM(
    private val playbackRepository: PlaybackRepository,
    private val playlistsRepository: PlaylistsRepository,
    private val libraryRepository: LibraryRepository
) : ViewModel() {
    companion object Factory {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {

                val app = (this[APPLICATION_KEY] as SimpleMPApplication)
                val playbackRepository = app.container.playbackRepository
                val playlistsRepository = app.container.playlistsRepository
                val libraryRepository = app.container.libraryRepository

                PlaylistScreenVM(playbackRepository, playlistsRepository, libraryRepository)
            }
        }
    }

    data class UiState(
        val requestedLoading: Boolean = false,
        val loading: Boolean = true,
        val playlistId: ObjectId? = null,
        val playlistArt: Bitmap? = null,
        val playlistName: String = "",
        val editNameText: String = "",
        val songs: List<Song> = ArrayList(),
        val showMenu: Boolean = false,
        val inEditMode: Boolean = false,
        val showEditNameDialog: Boolean = false,
        val showEditImageDialog: Boolean = false,
        val showDeletePlaylistDialog: Boolean = false,
        val currentSong: Song? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private lateinit var playlist: Playlist
    private lateinit var songs: List<Song>
    private val queries = Queries(getRealm())

    init {
        viewModelScope.launch(Dispatchers.Main) {
            playbackRepository.currentSongState.collect { currentSongState ->
                _uiState.update { uiState.value.copy(currentSong = currentSongState?.currentSong) }
            }
        }

        viewModelScope.launch(Dispatchers.Main) {
            playlistsRepository.userPlaylists.collect {
                uiState.value.playlistId?.let { playlistId ->
                    val playlist = playlistsRepository.getUserPlaylist(playlistId)

                    _uiState.update {
                        uiState.value.copy(songs = playlistsRepository.getPlaylistSongs(playlist.songs))
                    }
                }
            }
        }
    }

    fun load(playlistId: ObjectId) {
        viewModelScope.launch(Dispatchers.Main) {

            _uiState.update { uiState.value.copy(requestedLoading = true) }

            playlist = playlistsRepository.getUserPlaylist(playlistId)
            songs = playlistsRepository.getPlaylistSongs(playlist.songs)

            _uiState.update {
                uiState.value.copy(
                    loading = false,
                    playlistId = playlistId,
                    playlistName = playlist.name,
                    songs = songs
                )
            }
        }
    }

    fun updateShowMenu(v: Boolean) {
        _uiState.update { uiState.value.copy(showMenu = v) }
    }

    fun getArtistName(artistId: Long): String {
        return libraryRepository.getArtistName(artistId)
    }

    fun getAlbumArt(albumId: Long): Bitmap? {
        return libraryRepository.getSmallAlbumArt(albumId)
    }

    fun playSong(song: Song) {
        playbackRepository.playSelectedSong(song, uiState.value.songs)
    }

    fun updateShowDeleteDialog(v: Boolean) {
        _uiState.update { uiState.value.copy(showDeletePlaylistDialog = v) }
    }

    fun updateShowEditNameDialog(v: Boolean) {
        _uiState.update {
            uiState.value.copy(
                showEditNameDialog = v,
                editNameText = if(v) uiState.value.playlistName else uiState.value.editNameText
            )
        }
    }

    fun updateEditNameText(text: String) {
        _uiState.update { uiState.value.copy(editNameText = text) }
    }

    fun updatePlaylistName() {
        _uiState.update {
            uiState.value.copy(
                playlistName = uiState.value.editNameText,
                showEditNameDialog = false
            )
        }
    }

    fun deletePlaylist(playlistId: ObjectId) {
        viewModelScope.launch(Dispatchers.Main) {
            queries.deletePlaylist(playlistId)
            playlistsRepository.loadPlaylists(libraryRepository.songs.value)
        }
    }

    fun shuffleAndPlay() {
        playbackRepository.shuffleAndPlay(uiState.value.songs)
    }

    fun updateEditMode(v: Boolean) {
        _uiState.update { uiState.value.copy(inEditMode = v) }
    }

    fun cancelEditMode() {
        _uiState.update {
            uiState.value.copy(
                playlistName = playlist.name,
                songs = songs,
                inEditMode = false,
                showMenu = false
            )
        }
    }

    fun save() {
        viewModelScope.launch(Dispatchers.Main) {
            queries.updatePlaylistName(playlist._id, uiState.value.playlistName)
            queries.updatePlaylistSongs(playlist._id, uiState.value.songs.map { it.id })

            _uiState.update {
                uiState.value.copy(
                    showMenu = false,
                    inEditMode = false,
                )
            }

            playlistsRepository.loadPlaylists(libraryRepository.songs.value)
        }
    }

    fun removeSong(songId: Long) {
        _uiState.update {
            uiState.value.copy(
                songs = uiState.value.songs.filter { it.id != songId }
            )
        }
    }
}