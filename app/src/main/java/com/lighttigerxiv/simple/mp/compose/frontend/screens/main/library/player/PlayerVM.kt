package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.player

import android.graphics.Bitmap
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
import com.lighttigerxiv.simple.mp.compose.backend.repositories.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlayerVM(
    private val playbackRepository: PlaybackRepository,
    private val libraryRepository: LibraryRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    companion object Factory {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {

                val application = (this[APPLICATION_KEY] as SimpleMPApplication)

                PlayerVM(
                    application.container.playbackRepository,
                    application.container.libraryRepository,
                    application.container.settingsRepository
                )
            }
        }
    }

    data class UiState(
        val playingPlaylist: List<Song> = ArrayList(),
        val upNextPlaylist: List<Song> = ArrayList(),
        val currentSong: Song? = null,
        val currentSongPosition: Int = 0,
        val currentSongArtistName: String = "",
        val smallAlbumArt: Bitmap? = null,
        val pagerAlbumsArts: List<Bitmap?> = ArrayList(),
        val isPlaying: Boolean = false,
        val shuffle: Boolean = false,
        val repeatState: RepeatSate = RepeatSate.Off,
        val currentProgress: Int = 0,
        val currentProgressAsTime: String = "",
        val songDurationAsTime: String = "",
        val showUpNextPlaylist: Boolean = false,
        val showCarPlayer: Boolean = false,
        val keepScreenOnCarPlayer: Boolean = false
    )


    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.Main) {
            settingsRepository.settingsFlow.collect { settings ->
                _uiState.update { uiState.value.copy(keepScreenOnCarPlayer = settings.keepScreenOnCarPlayer) }
            }
        }
        viewModelScope.launch(Dispatchers.Main) {
            playbackRepository.playlistsState.collect { newPlaylistsState ->
                if (newPlaylistsState != null) {
                    _uiState.update {
                        uiState.value.copy(
                            playingPlaylist = newPlaylistsState.current,
                            upNextPlaylist = newPlaylistsState.upNext,
                            currentSongPosition = newPlaylistsState.songPosition,
                            pagerAlbumsArts = playbackRepository.getPlayingPlaylistAlbumArts()
                        )
                    }
                }
            }
        }

        viewModelScope.launch(Dispatchers.Main) {
            playbackRepository.currentSongState.collect { newCurrentSongState ->
                if (newCurrentSongState != null) {
                    _uiState.update {
                        uiState.value.copy(
                            currentSong = newCurrentSongState.currentSong,
                            currentSongArtistName = newCurrentSongState.artistName,
                            smallAlbumArt = libraryRepository.getSmallAlbumArt(newCurrentSongState.currentSong.albumId),
                            songDurationAsTime = newCurrentSongState.currentSong.duration.asTime()
                        )
                    }
                }
            }
        }

        viewModelScope.launch(Dispatchers.Main) {
            playbackRepository.playbackState.collect { newPlaybackState ->
                if (newPlaybackState != null) {
                    _uiState.update {
                        uiState.value.copy(
                            isPlaying = newPlaybackState.isPlaying,
                            shuffle = newPlaybackState.shuffle,
                            repeatState = newPlaybackState.repeatSate,
                            currentProgress = newPlaybackState.progress.toInt()
                        )
                    }
                }
            }
        }
    }

    fun getSongArt(albumId: Long): Bitmap? {
        return libraryRepository.getLargeAlbumArt(albumId)
    }

    fun pauseOrResume() {
        playbackRepository.pauseResume()
    }

    fun skipToPrevious(testFiveSeconds: Boolean = true) {
        playbackRepository.skipToPrevious(testFiveSeconds)
    }

    fun skipToNext() {
        playbackRepository.skipToNext()
    }


    fun seekTo(seconds: Int) {
        playbackRepository.seekTo(seconds)
    }

    fun toggleShuffle() {
        playbackRepository.toggleShuffle()
    }

    fun toggleRepeatState() {
        playbackRepository.toggleRepeat()
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

    fun updateShowPlaylist(v: Boolean) {
        _uiState.update { uiState.value.copy(showUpNextPlaylist = v) }
    }

    fun getSongPosition(): Int {
        return uiState.value.currentSongPosition
    }

    fun moveSongToTop(songId: Long) {
        playbackRepository.moveToTopOnPlaylingPlaylist(songId)
    }

    fun toggleCarPlayer() {
        _uiState.update { uiState.value.copy(showCarPlayer = !uiState.value.showCarPlayer) }
    }
}