package com.lighttigerxiv.simple.mp.compose.app_viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.appwidget.AppWidgetManager
import android.content.*
import android.graphics.Bitmap
import android.os.IBinder
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import com.lighttigerxiv.simple.mp.compose.*
import com.lighttigerxiv.simple.mp.compose.services.SimpleMPService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.*

class MainVM(application: Application) : AndroidViewModel(application) {


    //************************************************
    // Variables
    //************************************************


    private val context = application

    @SuppressLint("StaticFieldLeak")
    private var smpService: SimpleMPService? = null

    private val _surfaceColor = MutableStateFlow(Color(0xFF000000))
    val surfaceColor = _surfaceColor.asStateFlow()
    fun updateSurfaceColor(newValue: Color) {
        _surfaceColor.update { newValue }
    }

    private val _showNavigationBar = MutableStateFlow(true)
    val showNavigationBar = _showNavigationBar.asStateFlow()
    fun updateShowNavigationBar(newValue: Boolean) {

        smpService?.let { smp ->

            if (smp.isMusicPlayingOrPaused() && newValue) {
                _miniPlayerHeight.update { 60.dp }
            } else {
                _miniPlayerHeight.update { 0.dp }
            }
        }

        _showNavigationBar.update { newValue }
    }


    private val _songs = MutableStateFlow<List<Song>?>(null)
    val songs = _songs.asStateFlow()

    private val _songsImages = MutableStateFlow<List<SongArt>?>(null)
    val songsImages = _songsImages.asStateFlow()

    private val _compressedSongsImages = MutableStateFlow<List<SongArt>?>(null)
    val compressedSongsImages = _compressedSongsImages.asStateFlow()

    private val _queue = MutableStateFlow<List<Song>?>(null)
    val queue = _queue.asStateFlow()

    private val _upNextQueue = MutableStateFlow<List<Song>?>(null)
    val upNextQueue = _upNextQueue.asStateFlow()

    private val _songsPagerQueue = MutableStateFlow<List<Song>?>(null)
    val songsPagerQueue = _songsPagerQueue.asStateFlow()

    private val _selectedSong = MutableStateFlow<Song?>(null)
    val selectedSong = _selectedSong.asStateFlow()

    private val _songAlbumArt = MutableStateFlow<Bitmap?>(null)
    val songAlbumArt = _songAlbumArt.asStateFlow()

    private val _compressedSongAlbumArt = MutableStateFlow<Bitmap?>(null)
    val compressedSongAlbumArt = _compressedSongAlbumArt.asStateFlow()

    private val _songMinutesAndSecondsText = MutableStateFlow("")
    val songMinutesAndSecondsText = _songMinutesAndSecondsText.asStateFlow()

    private val _currentSongMinutesAndSecondsText = MutableStateFlow("")
    val currentSongMinutesAndSecondsText = _currentSongMinutesAndSecondsText.asStateFlow()
    fun updateCurrentSongMinutesAndSecondsText(newValue:String) {
        _currentSongMinutesAndSecondsText.update { newValue }
    }

    private val _musicPlaying = MutableStateFlow(false)
    val musicPlayling = _musicPlaying.asStateFlow()

    private val _queueShuffled = MutableStateFlow(false)
    val queueShuffled = _queueShuffled.asStateFlow()

    private val _songOnRepeat = MutableStateFlow(false)
    val songOnRepeat = _songOnRepeat.asStateFlow()

    private val _songPosition = MutableStateFlow(0)
    val songPosition = _songPosition.asStateFlow()

    private val _songSeconds = MutableStateFlow(0f)
    val songSeconds = _songSeconds.asStateFlow()

    private val _miniPlayerHeight = MutableStateFlow(0.dp)
    val miniPlayerHeight = _miniPlayerHeight.asStateFlow()
    fun updateMiniPlayerHeight(newValue: Dp) {
        _miniPlayerHeight.update { newValue }
    }

    private val _playPauseIcon = MutableStateFlow<Bitmap?>(null)
    val playPauseIcon = _playPauseIcon.asStateFlow()


    //************************************************
    // Callbacks
    //************************************************


    var onSongSelected: () -> Unit = {}
    var onSecondPassed: (seconds: Int) -> Unit = {}



    //************************************************
    // Functions
    //************************************************

    private val simpleMPConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

            val binder = service as SimpleMPService.LocalBinder
            smpService = binder.getService()

            smpService?.let { smp ->

                _musicPlaying.update { smp.musicPlaying() }

                _queueShuffled.update { smp.queueShuffled }

                _songOnRepeat.update { smp.songOnRepeat }


                if (smp.isMusicPlayingOrPaused()) {

                    _selectedSong.update { smp.currentSong }

                    _songAlbumArt.update { getAlbumArt() }

                    _songSeconds.update { (smp.mediaPlayer.currentPosition / 1000).toFloat() }

                    _compressedSongAlbumArt.update { getAlbumArt(compressed = true) }

                    _songMinutesAndSecondsText.update { getMinutesAndSeconds(selectedSong.value!!.duration / 1000) }

                    _currentSongMinutesAndSecondsText.update { getMinutesAndSeconds(smp.mediaPlayer.currentPosition / 1000) }

                    _miniPlayerHeight.update { 60.dp }

                    _queue.update { smp.getQueue() }

                    _songsPagerQueue.update { getSongsPagerQueue() }

                    _songPosition.update { smp.currentSongPosition }

                    onSongSelected()
                }


                smp.onSongSelected = { song ->

                    _selectedSong.update { song }

                    _songSeconds.update { (smp.mediaPlayer.currentPosition / 1000).toFloat() }

                    _songMinutesAndSecondsText.update { getMinutesAndSeconds(selectedSong.value!!.duration / 1000) }

                    _songAlbumArt.update { getAlbumArt() }

                    _compressedSongAlbumArt.update { getAlbumArt(compressed = true) }

                    _musicPlaying.update { smp.musicPlaying() }

                    _songOnRepeat.update { smp.songOnRepeat }

                    _miniPlayerHeight.update { 60.dp }

                    _queue.update { smp.getQueue() }

                    _upNextQueue.update { smp.getUpNextQueue() }

                    _songsPagerQueue.update { getSongsPagerQueue() }

                    _songPosition.update { smp.currentSongPosition }

                    onSongSelected()

                    updateWidget()
                }


                smp.onSecondPassed = {

                    _musicPlaying.update { smp.musicPlaying() }

                    _songSeconds.update { (smp.mediaPlayer.currentPosition / 1000).toFloat() }
                }

                smp.onQueueShuffle = {

                    _queueShuffled.update { smp.queueShuffled }

                    _queue.update { smp.getQueue() }

                    _upNextQueue.update { smp.getUpNextQueue() }
                }


                smp.onPause = {

                    _musicPlaying.update { smp.musicPlaying() }

                    updateWidget()
                }


                smp.onResume = {

                    _musicPlaying.update { smp.musicPlaying() }

                    _songPosition.update { smp.currentSongPosition }

                    updateWidget()
                }

                smp.onSongRepeat = {

                    _songOnRepeat.update { smp.songOnRepeat }
                }

                smp.onStop = {

                    _selectedSong.update { null }
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {

            Log.d("Service Disconnection", "Service was disconnected")
        }
    }

    private fun updateWidget() {
        val intent = Intent(context, SimpleMPWidget::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        }

        val ids = AppWidgetManager.getInstance(context)
            .getAppWidgetIds(ComponentName(context, SimpleMPWidget::class.java))

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)

        context.sendBroadcast(intent)
    }

    fun getMinutesAndSeconds(receivedSeconds: Int): String {

        val minutes = ((receivedSeconds * 1000) / (1000 * 60 ) ) % 60

        val seconds = receivedSeconds % 60

        var stringSeconds = "$seconds"

        if (seconds < 10) {

            stringSeconds = "0$seconds"
        }

        return "$minutes:$stringSeconds"
    }

    private fun getAlbumArt(compressed: Boolean = false): Bitmap? {

        return when (compressed) {

            true -> {
                compressedSongsImages.value?.first { it.albumID == selectedSong.value?.albumID }?.albumArt
            }
            false -> {
                songsImages.value?.first { it.albumID == selectedSong.value?.albumID }?.albumArt
            }
        }
    }

    //************************************************
    // Playback Function
    //************************************************

    fun selectSong(newQueueList: List<Song>, position: Int) {

        if (newQueueList.isNotEmpty()) {

            smpService?.let { smp ->

                smp.selectSong(getApplication(), newQueueList, position)

                _selectedSong.update { smp.currentSong }

                _songMinutesAndSecondsText.update { getMinutesAndSeconds(selectedSong.value!!.duration / 1000) }

                _songAlbumArt.update { getAlbumArt() }

                _compressedSongAlbumArt.update { getAlbumArt(compressed = true) }

                _queue.update { smp.getQueue() }

                _upNextQueue.update { smp.getUpNextQueue() }
            }
        }
    }

    fun shuffleAndPlay(newQueueList: List<Song>) {

        smpService?.shuffleAndPlay(newQueueList, context)
    }

    fun unshuffleAndPlay(newQueueList: List<Song>, position: Int) {

        smpService?.let { smp ->

            if (smp.queueShuffled) {

                smp.toggleShuffle()
            }

            smp.selectSong(context, newQueueList, position)
        }
    }

    fun shuffle(newQueueList: List<Song>) {

        smpService?.shuffleAndPlay(newQueueList, getApplication())
    }


    fun seekSongSeconds(seconds: Int) {

        smpService?.let {smp->

            smp.seekTo(seconds)

            _musicPlaying.update { smp.musicPlaying() }
        }


    }

    fun toggleShuffle() {

        smpService?.let { smp ->

            smp.toggleShuffle()

            _musicPlaying.update { smp.musicPlaying() }

            _queueShuffled.update { smp.queueShuffled }

            _queue.update { smp.getQueue() }

            _upNextQueue.update { smp.getUpNextQueue() }

            _songPosition.update { smp.currentSongPosition }
        }
    }

    fun selectPreviousSong() {

        smpService?.selectPreviousSong(context)
    }

    fun selectNextSong() {

        smpService?.selectNextSong(context)
    }

    fun toggleRepeat() {

        smpService?.let { smp ->

            smp.toggleRepeat()

            _songOnRepeat.update { smp.songOnRepeat }
        }
    }

    fun pauseResumeMusic() {

        smpService?.pauseResumeMusic(context)
    }


    fun getIsMusicPaused(): Boolean {

        return if (smpService != null) smpService!!.musicPlaying() else false
    }

    fun getSongsPagerQueue(): List<Song>{

        val newQueue = ArrayList<Song>()

        /*
        val previousSong = try {
            queue.value!![songPosition.value - 1]
        }catch (_: Exception){
            null
        }

        val currentSong = queue.value!![songPosition.value]

        val nextSong = try {
            queue.value!![songPosition.value + 1]
        }catch (_: Exception){
            null
        }


        if(previousSong != null){
            newQueue.add(previousSong)
        }

        newQueue.add(currentSong)

        if(nextSong != null){
            newQueue.add(nextSong)
        }

         */

        return newQueue
    }

    init {

        val serviceIntent = Intent(application, SimpleMPService::class.java)
        application.bindService(serviceIntent, simpleMPConnection, Context.BIND_AUTO_CREATE)

        _songs.update { getSongs(context, "Recent") }
        _songsImages.update { getAllAlbumsImages(context) }
        _compressedSongsImages.update { getAllAlbumsImages(context, compressed = true) }
    }
}