package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.preview_artist

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.lighttigerxiv.simple.mp.compose.SimpleMPApplication
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Song
import com.lighttigerxiv.simple.mp.compose.backend.repositories.InternalStorageRepository
import com.lighttigerxiv.simple.mp.compose.backend.repositories.LibraryRepository
import com.lighttigerxiv.simple.mp.compose.backend.repositories.PlaybackRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.internal.toHexString

class PreviewArtistScreenVM(
    private val libraryRepository: LibraryRepository,
    private val playbackRepository: PlaybackRepository,
    private val internalStorageRepository: InternalStorageRepository
) : ViewModel(){
    companion object Factory{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as SimpleMPApplication)

                PreviewArtistScreenVM(
                    app.container.libraryRepository,
                    app.container.playbackRepository,
                    app.container.internalStorageRepository
                )
            }
        }
    }

    data class UiState(
        val requestedLoading: Boolean = false,
        val isLoading: Boolean = true,
        val artistName: String = "",
        val artistImage: Bitmap? = null,
        val songs: List<Song> = ArrayList(),
        val currentSong: Song? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    var listPosition = 0

    fun load(artistId: Long){
        viewModelScope.launch(Dispatchers.Main) {

            _uiState.update { uiState.value.copy(requestedLoading = true) }

            _uiState.update {
                uiState.value.copy(
                    isLoading = false,
                    artistName = libraryRepository.getArtistName(artistId),
                    songs = libraryRepository.getArtistSongs(artistId),
                )
            }
        }

        viewModelScope.launch(Dispatchers.Main) {
            playbackRepository.currentSongState.collect{ newSongState ->
                _uiState.update {
                    uiState.value.copy(currentSong = newSongState?.currentSong)
                }
            }
        }

        val request = libraryRepository.getArtistImageRequest(artistId)

        if (request == null) {
            _uiState.update { uiState.value.copy(artistImage = null) }
        } else {

            if (request.useDefault) {
                _uiState.update { uiState.value.copy(artistImage = null) }
            } else {
                viewModelScope.launch(Dispatchers.Main) {
                    internalStorageRepository.loadImageFromInternalStorage(artistId.toHexString()).collect { image ->
                        _uiState.update { uiState.value.copy(artistImage = image) }
                    }
                }
            }
        }
    }

    fun shuffle(){
        playbackRepository.shuffleAndPlay(uiState.value.songs)
    }

    fun getAlbumArt(albumId: Long): Bitmap? {
        return libraryRepository.getLargeAlbumArt(albumId = albumId)
    }

    fun playSong(song: Song) {
        playbackRepository.playSelectedSong(song, uiState.value.songs)
    }
}