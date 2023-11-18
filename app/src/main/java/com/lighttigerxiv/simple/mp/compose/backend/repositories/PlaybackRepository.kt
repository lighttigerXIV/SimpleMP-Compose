package com.lighttigerxiv.simple.mp.compose.backend.repositories

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Handler
import android.os.Looper
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.KeyEvent
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.lighttigerxiv.simple.mp.compose.backend.playback.PlaybackService
import com.lighttigerxiv.simple.mp.compose.backend.playback.RepeatSate
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Song
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.burnoutcrew.reorderable.ItemPosition

class PlaybackRepository(
    private val libraryRepository: LibraryRepository,
    private val application: Application
) {

    val player = ExoPlayer.Builder(application).build()
    val session = MediaSessionCompat(application, "SimpleMPMediaSession").apply {
        setCallback(object : MediaSessionCompat.Callback() {
            override fun onMediaButtonEvent(mediaButtonEvent: Intent?): Boolean {

                val keyEvent = mediaButtonEvent?.getParcelableExtra<KeyEvent>(Intent.EXTRA_KEY_EVENT)

                if (keyEvent?.action == KeyEvent.ACTION_DOWN) {

                    when (keyEvent.keyCode) {
                        KeyEvent.KEYCODE_MEDIA_PREVIOUS -> {
                            previous()
                        }

                        KeyEvent.KEYCODE_MEDIA_NEXT -> {
                            skip()
                        }

                        KeyEvent.KEYCODE_MEDIA_PLAY -> {
                            pauseOrResume()
                        }

                        KeyEvent.KEYCODE_MEDIA_PAUSE -> {
                            pauseOrResume()
                        }
                    }
                }

                return super.onMediaButtonEvent(mediaButtonEvent)
            }

            override fun onPlay() {
                super.onPlay()
                pauseOrResume()
            }

            override fun onPause() {
                super.onPause()
                pauseOrResume()
            }

            override fun onSkipToNext() {
                super.onSkipToNext()
                skip()
            }

            override fun onSkipToPrevious() {
                super.onSkipToPrevious()
                previous()
            }

            override fun onStop() {
                super.onStop()
                onStop()
            }
        })
    }

    private val audioManager = application.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private val focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN).run {
        setAudioAttributes(AudioAttributes.Builder().run {
            setUsage(AudioAttributes.USAGE_MEDIA)
            setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            build()
        })
        setAcceptsDelayedFocusGain(true)
        setOnAudioFocusChangeListener { change ->
            when (change) {
                AudioManager.AUDIOFOCUS_GAIN -> {}
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT -> {}
                AudioManager.AUDIOFOCUS_LOSS -> pause()
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> pause()
            }
        }
        build()


    }

    private fun requestFocusAndPlay() {

        val focusLock = Any()
        val resource = audioManager.requestAudioFocus(focusRequest)

        synchronized(focusLock) {
            when (resource) {
                AudioManager.AUDIOFOCUS_REQUEST_GRANTED -> {
                    player.play()
                    _isPlaying.update { true }
                }
            }
        }
    }

    private fun setPlaybackState(state: Int) {
        try {

            val stateBuilder = PlaybackStateCompat.Builder()
                .setActions(
                    PlaybackStateCompat.ACTION_PLAY_PAUSE
                            or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                            or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                )
                .apply {
                    setState(state, player.currentPosition, 1.0f)
                }

            session.setPlaybackState(stateBuilder.build())

        } catch (e: Exception) {
            Log.e("PlaybackState", e.toString())
        }
    }


    //==========================================//
    //============== PLAYBACK ==================//
    //==========================================//

    private var playlist: List<Song> = ArrayList()
    private var shuffledPlaylist: List<Song> = ArrayList()

    private val _currentSongPosition = MutableStateFlow(-1)
    val currentSongPosition = _currentSongPosition.asStateFlow()

    private val _playingPlaylist = MutableStateFlow<List<Song>>(ArrayList())
    val playingPlaylist = _playingPlaylist.asStateFlow()

    private val _upNextPlaylist = MutableStateFlow<List<Song>>(ArrayList())
    val upNextPlaylist = _upNextPlaylist.asStateFlow()
    private fun updateUpNextPlaylist() {
        _upNextPlaylist.update { playingPlaylist.value.filterIndexed { index, _ -> index > currentSongPosition.value } }
    }

    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong = _currentSong.asStateFlow()

    private val _currentSongArt = MutableStateFlow<Bitmap?>(null)
    val currentSongArt = _currentSongArt.asStateFlow()

    private val _currentSongArtistName = MutableStateFlow("")
    val currentSongArtistName = _currentSongArtistName.asStateFlow()

    private val _currentSongAlbumName = MutableStateFlow("")
    val currentSongAlbumName = _currentSongAlbumName.asStateFlow()

    private val _currentProgress = MutableStateFlow(0)
    val currentProgress = _currentProgress.asStateFlow()

    private val _shuffle = MutableStateFlow(false)
    val shuffle = _shuffle.asStateFlow()

    private val _repeatState = MutableStateFlow(RepeatSate.Off)
    val repeatState = _repeatState.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()


    private fun updateCurrentSongInfo(song: Song) {

        _currentSong.update { song }
        _currentSongArt.update { libraryRepository.getLargeAlbumArt(song.albumId) }
        _currentSongArtistName.update { libraryRepository.getArtistName(song.artistId) }
        _currentSongAlbumName.update { libraryRepository.getAlbumName(song.albumId) }
    }

    fun playSelectedSong(newPlaylist: List<Song>, song: Song) {

        playlist = newPlaylist
        shuffledPlaylist = ArrayList()

        if (shuffle.value) {

            val newShuffledPlaylist = newPlaylist.shuffled().toMutableList().apply {
                remove(song)
                add(0, song)
            }

            _playingPlaylist.update { newShuffledPlaylist }
            _currentSongPosition.update { 0 }
            updateCurrentSongInfo(song)
            updateUpNextPlaylist()
        } else {

            _playingPlaylist.update { playlist }
            _currentSongPosition.update { playlist.indexOf(song) }
            updateCurrentSongInfo(song)
            updateUpNextPlaylist()
        }

        playSong(song)
    }

    fun shuffleAndPlay(newPlaylist: List<Song>) {

        _shuffle.update { true }

        playlist = newPlaylist
        shuffledPlaylist = newPlaylist.shuffled()

        _currentSongPosition.update { 0 }

        _playingPlaylist.update { shuffledPlaylist }
        updateCurrentSongInfo(shuffledPlaylist[0])
        updateUpNextPlaylist()

        playSong(shuffledPlaylist[0])
    }

    private fun playSong(song: Song) {
        player.setMediaItem(MediaItem.fromUri(song.path))
        player.prepare()

        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {

                if (playbackState == Player.STATE_ENDED) {

                    if (repeatState.value == RepeatSate.One) {
                        _repeatState.update { RepeatSate.Off }
                        player.seekTo(0)
                        return
                    }

                    if (repeatState.value == RepeatSate.Endless) {
                        player.seekTo(0)
                        return
                    }

                    if (currentSongPosition.value + 1 < playingPlaylist.value.size) {
                        _currentSongPosition.update { it + 1 }

                        val nextSong = playingPlaylist.value[currentSongPosition.value]

                        updateCurrentSongInfo(nextSong)
                        playSong(nextSong)
                        updateNotification()
                    }

                    updateUpNextPlaylist()
                }

                super.onPlaybackStateChanged(playbackState)
            }
        })

        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                if (player.isPlaying)
                    setPlaybackState(PlaybackStateCompat.STATE_PLAYING)
                else
                    setPlaybackState(PlaybackStateCompat.STATE_PAUSED)

                _currentProgress.update { player.currentPosition.toInt() }

                handler.postDelayed(this, 1000)
            }
        })

        requestFocusAndPlay()
        updateNotification()
    }

    fun getCurrentPlaylistAlbumArts(): List<Bitmap?> {
        return playingPlaylist.value.map { libraryRepository.getLargeAlbumArt(it.albumId) }
    }

    private fun updateNotification() {
        if (currentSong.value != null) {
            val intent = Intent(application, PlaybackService::class.java)
            intent.action = PlaybackService.Actions.NOTIFY
            application.startService(intent)
        }
    }


    private fun pause() {
        if (player.isPlaying) {
            player.pause()
            _isPlaying.update { false }
        }
    }

    private fun resume() {
        if (!player.isPlaying) {
            requestFocusAndPlay()
        }
    }

    fun pauseOrResume() {

        if (player.isPlaying) {
            pause()
        } else {
            resume()
        }
    }

    fun previous(testFiveSeconds: Boolean = true) {

        if (testFiveSeconds && player.currentPosition > 5000) {
            player.seekTo(0)
            return
        }

        if (currentSongPosition.value - 1 >= 0) {
            _currentSongPosition.update { it - 1 }

            val song = playingPlaylist.value[currentSongPosition.value]

            playSong(song)
            updateCurrentSongInfo(song)
            updateUpNextPlaylist()
            updateNotification()
        }
    }

    fun skip() {

        if (currentSongPosition.value + 1 < playingPlaylist.value.size) {
            _currentSongPosition.update { it + 1 }

            val song = playingPlaylist.value[currentSongPosition.value]

            playSong(song)
            updateCurrentSongInfo(song)
            updateUpNextPlaylist()
            updateNotification()

            if (repeatState.value != RepeatSate.Off) {
                _repeatState.update { RepeatSate.Off }
            }
        }
    }

    fun seekTo(seconds: Int) {
        player.seekTo((seconds * 1000).toLong())
    }

    fun toggleShuffle() {

        if (shuffle.value) {
            _shuffle.update { false }
            _currentSongPosition.update { playlist.indexOf(currentSong.value) }
            _playingPlaylist.update { playlist }
            updateCurrentSongInfo(playlist[playlist.indexOf(currentSong.value)])
            updateUpNextPlaylist()

        } else {
            _shuffle.update { true }

            val song = playlist[playlist.indexOf(currentSong.value)]

            val newShuffledPlaylist = playlist.shuffled().toMutableList().apply {
                remove(song)
                add(0, song)
            }

            _playingPlaylist.update { newShuffledPlaylist }
            _currentSongPosition.update { 0 }
            updateCurrentSongInfo(song)
            updateUpNextPlaylist()
        }

        updateUpNextPlaylist()
    }

    fun toggleRepeatState() {

        when (repeatState.value) {
            RepeatSate.Off -> {
                _repeatState.update { RepeatSate.One }
            }

            RepeatSate.One -> {
                _repeatState.update { RepeatSate.Endless }
            }

            RepeatSate.Endless -> {
                _repeatState.update { RepeatSate.Off }
            }
        }
    }


    fun resetPlayback() {
        _currentSong.update { null }
        _currentSongArt.update { null }
        _currentSongAlbumName.update { "" }
        _currentSongArtistName.update { "" }
        _currentProgress.update { 0 }
    }

    fun stop() {
        val intent = Intent(application, PlaybackService::class.java)
        intent.action = PlaybackService.Actions.STOP
        application.startService(intent)
    }

    fun reorderPlayingPlaylist(fromId: Long, toId: Long) {
        val newPlaylist = playingPlaylist.value.toMutableList()
        val fromIndex = newPlaylist.indexOfFirst { it.id == fromId }
        val toIndex = newPlaylist.indexOfFirst { it.id == toId }

        val temp = newPlaylist[fromIndex]
        newPlaylist[fromIndex] = newPlaylist[toIndex]
        newPlaylist[toIndex] = temp

        _playingPlaylist.update { newPlaylist }
        updateUpNextPlaylist()
    }
}