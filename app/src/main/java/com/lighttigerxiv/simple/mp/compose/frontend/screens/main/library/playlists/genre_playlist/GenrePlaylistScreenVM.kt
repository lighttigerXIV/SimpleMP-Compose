package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.playlists.genre_playlist

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.lighttigerxiv.simple.mp.compose.SimpleMPApplication
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Song
import com.lighttigerxiv.simple.mp.compose.backend.repositories.LibraryRepository
import com.lighttigerxiv.simple.mp.compose.backend.repositories.PlaybackRepository
import com.lighttigerxiv.simple.mp.compose.backend.repositories.PlaylistsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId

class GenrePlaylistScreenVM(
    private val playlistsRepository: PlaylistsRepository,
    private val libraryRepository: LibraryRepository,
    private val playbackRepository: PlaybackRepository
) : ViewModel() {

    companion object Factory {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as SimpleMPApplication)
                val playlistsRepository = app.container.playlistsRepository
                val libraryRepository = app.container.libraryRepository
                val playbackRepository = app.container.playbackRepository

                GenrePlaylistScreenVM(playlistsRepository, libraryRepository, playbackRepository)
            }
        }
    }

    data class UiState(
        val requestedLoading: Boolean = false,
        val loading: Boolean = true,
        val genreName: String = "",
        val songs: List<Song> = ArrayList(),
        val currentSong: Song? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.Main) {
            playbackRepository.currentSongState.collect{ newCurrentSongState ->
                _uiState.update { uiState.value.copy(currentSong = newCurrentSongState?.currentSong) }
            }
        }
    }

    fun load(id: ObjectId) {
        viewModelScope.launch(Dispatchers.Main) {

            _uiState.update { uiState.value.copy(requestedLoading = true) }

            val playlist = playlistsRepository.getGenrePlaylist(id)

            _uiState.update {
                uiState.value.copy(
                    loading = false,
                    genreName = playlist.name,
                    songs = playlistsRepository.getPlaylistSongs(playlist.songs)
                )
            }
        }
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

    fun shuffleAndPlay(){
        playbackRepository.shuffleAndPlay(uiState.value.songs)
    }
}