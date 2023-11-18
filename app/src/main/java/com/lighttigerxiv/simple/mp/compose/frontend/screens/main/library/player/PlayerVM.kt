package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.player

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.lighttigerxiv.simple.mp.compose.SimpleMPApplication
import com.lighttigerxiv.simple.mp.compose.backend.playback.RepeatSate
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Song
import com.lighttigerxiv.simple.mp.compose.backend.repositories.LibraryRepository
import com.lighttigerxiv.simple.mp.compose.backend.repositories.PlaybackRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.burnoutcrew.reorderable.ItemPosition

class PlayerVM(
    private val playbackRepository: PlaybackRepository,
    private val libraryRepository: LibraryRepository
) : ViewModel() {
    companion object Factory {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {

                val application = (this[APPLICATION_KEY] as SimpleMPApplication)
                val playbackRepository = application.container.playbackRepository
                val libraryRepository = application.container.libraryRepository

                PlayerVM(playbackRepository, libraryRepository)
            }
        }
    }

    data class UiState(
        val playingPlaylist: List<Song>,
        val upNextPlaylist: List<Song>,
        val currentSong: Song?,
        val currentSongPosition: Int,
        val currentSongArtistName: String,
        val currentSongArt: Bitmap?,
        val pagerAlbumsArts: List<Bitmap?>,
        val isPlaying: Boolean,
        val shuffle: Boolean,
        val repeatState: RepeatSate,
        val currentProgress: Int,
        val currentProgressAsTime: String,
        val songDurationAsTime: String,
    )


    private val _uiState = MutableStateFlow(
        UiState(
            playingPlaylist = ArrayList(),
            upNextPlaylist = ArrayList(),
            currentSong = null,
            currentSongPosition = 0,
            currentSongArtistName = "",
            currentSongArt = null,
            pagerAlbumsArts = ArrayList(),
            isPlaying = false,
            shuffle = false,
            repeatState = RepeatSate.Off,
            currentProgress = 0,
            currentProgressAsTime = "",
            songDurationAsTime = ""
        )
    )
    val uiState = _uiState.asStateFlow()

    init {

        viewModelScope.launch(Dispatchers.Main) {
            playbackRepository.currentSong.collect { newSong ->

                _uiState.update {
                    uiState.value.copy(
                        currentSong = newSong,
                        currentSongPosition = playbackRepository.currentSongPosition.value,
                        currentSongArt = playbackRepository.currentSongArt.value,
                        currentSongArtistName = playbackRepository.currentSongArtistName.value,
                        songDurationAsTime = (newSong?.duration ?: 0).asTime()
                    )
                }
            }
        }

        viewModelScope.launch(Dispatchers.Main) {
            playbackRepository.playingPlaylist.collect { newPlayingPlaylist ->
                _uiState.update {
                    uiState.value.copy(
                        playingPlaylist = newPlayingPlaylist
                    )
                }
            }
        }

        viewModelScope.launch(Dispatchers.Main) {
            playbackRepository.upNextPlaylist.collect { newUpNextPlaylist ->
                _uiState.update {
                    uiState.value.copy(
                        upNextPlaylist = newUpNextPlaylist
                    )
                }
            }
        }

        viewModelScope.launch(Dispatchers.Main) {
            playbackRepository.currentSongPosition.collect {
                _uiState.update {
                    uiState.value.copy(
                        currentSongPosition = playbackRepository.currentSongPosition.value,
                        pagerAlbumsArts = playbackRepository.getCurrentPlaylistAlbumArts()
                    )
                }
            }
        }

        viewModelScope.launch(Dispatchers.Main) {
            playbackRepository.isPlaying.collect { newIsPlaying ->
                _uiState.update { uiState.value.copy(isPlaying = newIsPlaying) }
            }
        }

        viewModelScope.launch(Dispatchers.Main) {
            playbackRepository.shuffle.collect { newShuffle ->
                _uiState.update { uiState.value.copy(shuffle = newShuffle) }
            }
        }

        viewModelScope.launch(Dispatchers.Main) {
            playbackRepository.repeatState.collect { newRepeatState ->
                _uiState.update { uiState.value.copy(repeatState = newRepeatState) }
            }
        }

        viewModelScope.launch(Dispatchers.Main) {
            playbackRepository.currentProgress.collect { newProgress ->

                _uiState.update {
                    uiState.value.copy(
                        currentProgress = newProgress
                    )
                }
            }
        }
    }

    fun getSongArt(albumId: Long): Bitmap? {
        return libraryRepository.getLargeAlbumArt(albumId)
    }

    fun pauseOrResume() {
        playbackRepository.pauseOrResume()
    }

    fun previous(testFiveSeconds: Boolean = true) {
        playbackRepository.previous(testFiveSeconds)
    }

    fun skip() {
        playbackRepository.skip()
    }


    fun seekTo(seconds: Int) {
        playbackRepository.seekTo(seconds)
    }

    fun toggleShuffle() {
        playbackRepository.toggleShuffle()
    }

    fun toggleRepeatState() {
        playbackRepository.toggleRepeatState()
    }

    fun updateCurrentProgressAsTime(newProgress: Int) {
        _uiState.update { uiState.value.copy(currentProgressAsTime = newProgress.asTime()) }
    }

    private fun Int.asTime(): String {

        val minutes = (this / 1000) / 60
        val seconds = (this / 1000) % 60

        return "${if (minutes <= 9) "0" else ""}$minutes:${if (seconds <= 9) "0" else ""}$seconds"
    }

    fun getArtistName(artistId: Long): String {
        return libraryRepository.getArtistName(artistId)
    }

    fun getSmallSongArt(albumId: Long): Bitmap? {
        return libraryRepository.getSmallAlbumArt(albumId)
    }

    fun reorderPlayingPlaylist(fromId: Any?, toId: Any?) {
        if (fromId is Long && toId is Long) {
            playbackRepository.reorderPlayingPlaylist(fromId, toId)
        }
    }
}