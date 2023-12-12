package com.lighttigerxiv.simple.mp.compose.backend.repositories

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.lighttigerxiv.simple.mp.compose.backend.playback.BluetoothReceiver
import com.lighttigerxiv.simple.mp.compose.backend.playback.PlaybackService
import com.lighttigerxiv.simple.mp.compose.backend.playback.RepeatSate
import com.lighttigerxiv.simple.mp.compose.backend.playback.StopPlaybackReceiver
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Song
import com.lighttigerxiv.simple.mp.compose.backend.utils.isAtLeastAndroid13
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaybackRepository(
    private val libraryRepository: LibraryRepository,
    private val application: Application
) {

    companion object States {
        data class CurrentSongState(
            val currentSong: Song,
            val artistName: String,
            val albumName: String,
            val albumArt: Bitmap?
        )

        data class PlaylistsState(
            val original: List<Song>,
            val shuffled: List<Song>,
            val current: List<Song>,
            val upNext: List<Song>,
            val songPosition: Int
        )

        data class PlaybackState(
            val isPlaying: Boolean = false,
            val shuffle: Boolean = false,
            val repeatSate: RepeatSate = RepeatSate.Off,
            val progress: Long = 0
        )
    }

    val player = ExoPlayer.Builder(application).build().also { player ->
        player.addListener(object : Player.Listener {

            override fun onPlaybackStateChanged(state: Int) {

                if (state == Player.STATE_ENDED) {

                    if (playbackState.value!!.repeatSate == RepeatSate.One) {
                        _playbackState.update { playbackState.value!!.copy(repeatSate = RepeatSate.Off) }
                        player.seekTo(0)
                        return
                    }

                    if (playbackState.value!!.repeatSate == RepeatSate.Endless) {
                        player.seekTo(0)
                        return
                    }

                    if (playlistsState.value!!.songPosition + 1 < playlistsState.value!!.current.size) {
                        skipToNext()
                    }
                }

                super.onPlaybackStateChanged(state)
            }

            override fun onPlayerError(error: PlaybackException) {

                val corruptedSong = playlistsState.value!!.current[playlistsState.value!!.songPosition]
                val newOriginalPlaylist = playlistsState.value!!.original.toMutableList().apply {
                    remove(corruptedSong)
                }

                val newShuffledPlaylist = playlistsState.value!!.shuffled.toMutableList().apply {
                    remove(corruptedSong)
                }

                val newCurrentPlaylist = playlistsState.value!!.current.toMutableList().apply {
                    remove(corruptedSong)
                }

                _playlistsState.update {
                    playlistsState.value?.copy(
                        original = newOriginalPlaylist,
                        shuffled = newShuffledPlaylist,
                        current = newCurrentPlaylist,
                        songPosition = playlistsState.value!!.songPosition - 1
                    )
                }

                skipToNext()

                CoroutineScope(Dispatchers.Main).launch {
                    libraryRepository.indexLibrary(application, onFinish = {
                        withContext(Dispatchers.Main){
                            libraryRepository.initLibrary()
                        }
                    })
                }


                super.onPlayerError(error)
            }
        })
    }

    val session = MediaSessionCompat(application, "SimpleMPMediaSession").apply {
        setCallback(object : MediaSessionCompat.Callback() {
            override fun onMediaButtonEvent(mediaButtonEvent: Intent?): Boolean {

                val keyEvent = mediaButtonEvent?.getParcelableExtra<KeyEvent>(Intent.EXTRA_KEY_EVENT)

                if (keyEvent?.action == KeyEvent.ACTION_DOWN) {

                    when (keyEvent.keyCode) {
                        KeyEvent.KEYCODE_MEDIA_PREVIOUS -> {
                            skipToPrevious()
                        }

                        KeyEvent.KEYCODE_MEDIA_NEXT -> {
                            skipToNext()
                        }

                        KeyEvent.KEYCODE_MEDIA_PLAY -> {
                            pauseResume()
                        }

                        KeyEvent.KEYCODE_MEDIA_PAUSE -> {
                            pauseResume()
                        }
                    }
                }

                return super.onMediaButtonEvent(mediaButtonEvent)
            }

            override fun onPlay() {
                pauseResume()
                super.onPlay()
            }

            override fun onPause() {
                pauseResume()
                super.onPause()
            }

            override fun onSkipToNext() {
                skipToNext()
                super.onSkipToNext()
            }

            override fun onSkipToPrevious() {
                skipToPrevious()
                super.onSkipToPrevious()
            }

            override fun onStop() {
                stop()
                super.onStop()
            }

            override fun onSeekTo(pos: Long) {
                seekTo((pos / 1000).toInt())
                super.onSeekTo(pos)
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

    val bluetoothReceiver = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)

    private fun requestFocusAndPlay() {

        val focusLock = Any()
        val resource = audioManager.requestAudioFocus(focusRequest)

        synchronized(focusLock) {
            when (resource) {
                AudioManager.AUDIOFOCUS_REQUEST_GRANTED -> {
                    player.play()
                    session.isActive = true

                    setPlaybackState(PlaybackStateCompat.STATE_PLAYING)
                    _playbackState.update { playbackState.value!!.copy(isPlaying = true) }

                    updateNotification()
                }
            }
        }
    }

    init {
        if(isAtLeastAndroid13()){
            application.registerReceiver(BluetoothReceiver(), bluetoothReceiver, Context.RECEIVER_EXPORTED)
        }else{
            application.registerReceiver(BluetoothReceiver(), bluetoothReceiver)
        }
    }

    private fun setPlaybackState(state: Int) {
        try {

            val stateBuilder = PlaybackStateCompat.Builder()
                .setActions(
                    PlaybackStateCompat.ACTION_PLAY_PAUSE
                            or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                            or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                            or PlaybackStateCompat.ACTION_SEEK_TO
                            or PlaybackStateCompat.ACTION_STOP
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

    private val _currentSongState = MutableStateFlow<CurrentSongState?>(null)
    val currentSongState = _currentSongState.asStateFlow()

    private val _playlistsState = MutableStateFlow<PlaylistsState?>(null)
    val playlistsState = _playlistsState.asStateFlow()

    private val _playbackState = MutableStateFlow<PlaybackState?>(null)
    val playbackState = _playbackState.asStateFlow()

    fun seekTo(seconds: Int) {
        player.seekTo((seconds * 1000).toLong())

        if(!player.isPlaying){
            resume()
        }
    }

    fun toggleShuffle() {

        if (playbackState.value!!.shuffle) {

            _playbackState.update { playbackState.value!!.copy(shuffle = false) }



            _playlistsState.update {
                playlistsState.value?.copy(
                    current = playlistsState.value!!.original,
                    upNext = playlistsState.value!!.original.filterIndexed { index, _ -> index > 0 },
                    songPosition = playlistsState.value!!.original.indexOf(currentSongState.value?.currentSong)
                )
            }

        } else {

            _playbackState.update { playbackState.value!!.copy(shuffle = true) }

            val shuffledPlaylist = playlistsState.value!!.original.shuffled().toMutableList().apply {
                remove(currentSongState.value!!.currentSong)
                add(0, currentSongState.value!!.currentSong)
            }

            _playlistsState.update {
                playlistsState.value?.copy(
                    shuffled = shuffledPlaylist,
                    current = shuffledPlaylist,
                    upNext = shuffledPlaylist.filterIndexed { index, _ -> index > 0 },
                    songPosition = 0
                )
            }
        }
    }

    fun skipToPrevious(testFiveSeconds: Boolean = true) {
        if (testFiveSeconds) {

            if (player.currentPosition > 5000) {
                player.seekTo(0)
                return
            }
        }

        if (playlistsState.value!!.songPosition - 1 >= 0) {

            val newPosition = playlistsState.value!!.songPosition - 1

            _playlistsState.update {
                playlistsState.value!!.copy(
                    songPosition = newPosition
                )
            }

            playSong()
        }
    }

    fun pauseResume() {
        if (playbackState.value?.isPlaying == true) {
            pause()
        } else {
            resume()
        }
    }

    fun pause() {
        player.pause()
        session.isActive = false
        _playbackState.update { playbackState.value?.copy(isPlaying = false) }

        setPlaybackState(PlaybackStateCompat.STATE_PAUSED)
    }

    private fun resume() {
        player.play()
        session.isActive = true
        _playbackState.update { playbackState.value?.copy(isPlaying = true) }

        setPlaybackState(PlaybackStateCompat.STATE_PLAYING)
    }

    fun skipToNext() {
        if (playlistsState.value!!.songPosition + 1 < playlistsState.value!!.current.size) {

            val newPosition = playlistsState.value!!.songPosition + 1

            _playlistsState.update {
                playlistsState.value!!.copy(
                    songPosition = newPosition
                )
            }

            playSong()
        }
    }

    fun toggleRepeat() {
        when (playbackState.value!!.repeatSate) {
            RepeatSate.Off -> {
                _playbackState.update { playbackState.value!!.copy(repeatSate = RepeatSate.One) }
            }

            RepeatSate.One -> {
                _playbackState.update { playbackState.value!!.copy(repeatSate = RepeatSate.Endless) }
            }

            RepeatSate.Endless -> {
                _playbackState.update { playbackState.value!!.copy(repeatSate = RepeatSate.Off) }
            }
        }
    }

    fun playSelectedSong(song: Song, playlist: List<Song>) {

        if (playbackState.value == null) {
            _playbackState.update { PlaybackState() }
        }

        if (playbackState.value!!.shuffle) {

            val shuffledPlaylist = playlist.shuffled().toMutableList().apply {
                remove(song)
                add(0, song)
            }

            _playlistsState.update {
                PlaylistsState(
                    original = playlist,
                    shuffled = shuffledPlaylist,
                    current = shuffledPlaylist,
                    upNext = shuffledPlaylist.filterIndexed { index, _ -> index > 0 },
                    songPosition = 0
                )
            }

        } else {

            val songPosition = playlist.indexOf(song)

            _playlistsState.update {
                PlaylistsState(
                    original = playlist,
                    shuffled = ArrayList(),
                    current = playlist,
                    upNext = playlist.filterIndexed { index, _ -> index > songPosition },
                    songPosition = songPosition
                )
            }
        }

        playSong()
    }

    fun shuffleAndPlay(playlist: List<Song>) {

        _playbackState.update { PlaybackState(shuffle = true) }

        val shuffledPlaylist = playlist.shuffled()

        _playlistsState.update {
            PlaylistsState(
                original = playlist,
                shuffled = shuffledPlaylist,
                current = shuffledPlaylist,
                upNext = shuffledPlaylist.filterIndexed { index, _ -> index > 0 },
                songPosition = 0
            )
        }

        playSong()
    }

    private fun playSong() {

        val songPosition = playlistsState.value!!.songPosition
        val song = playlistsState.value!!.current[songPosition]

        _currentSongState.update {
            CurrentSongState(
                currentSong = song,
                artistName = libraryRepository.getArtistName(song.artistId),
                albumName = libraryRepository.getAlbumName(song.albumId),
                albumArt = libraryRepository.getLargeAlbumArt(song.albumId)
            )
        }

        _playlistsState.update {
            playlistsState.value!!.copy(
                upNext = playlistsState.value!!.current.filterIndexed { index, _ -> index > songPosition }
            )
        }

        player.setMediaItem(MediaItem.fromUri(song.path))
        player.prepare()
        requestFocusAndPlay()

        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {

                _playbackState.update { playbackState.value!!.copy(progress = player.currentPosition) }

                handler.postDelayed(this, 1000)
            }
        })
    }

    fun stop(skipStopService: Boolean = false) {

        if (!skipStopService) {
            val intent = Intent(application, PlaybackService::class.java)
            intent.action = PlaybackService.Actions.STOP
            application.startService(intent)
        }

        player.stop()
        player.release()
        session.isActive = false

        _playbackState.update { null }
        _currentSongState.update { null }
        _playlistsState.update { null }
    }

    private fun updateNotification() {
        val intent = Intent(application, PlaybackService::class.java)
        intent.action = PlaybackService.Actions.NOTIFY
        application.startService(intent)
    }

    fun reorderPlayingPlaylist(fromId: Long, toId: Long) {
        val newPlaylist = playlistsState.value!!.current.toMutableList()
        val fromIndex = newPlaylist.indexOfFirst { it.id == fromId }
        val toIndex = newPlaylist.indexOfFirst { it.id == toId }

        val temp = newPlaylist[fromIndex]
        newPlaylist[fromIndex] = newPlaylist[toIndex]
        newPlaylist[toIndex] = temp

        _playlistsState.update {
            playlistsState.value!!.copy(
                current = newPlaylist,
                upNext = newPlaylist.filterIndexed { index, _ -> index > playlistsState.value!!.songPosition }
            )
        }
    }

    fun getPlayingPlaylistAlbumArts(): List<Bitmap?> {
        return playlistsState.value!!.current.map { song -> libraryRepository.getLargeAlbumArt(song.albumId) }
    }
}