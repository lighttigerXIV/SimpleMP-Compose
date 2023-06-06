package com.lighttigerxiv.simple.mp.compose.activities.main

import android.annotation.SuppressLint
import android.app.Application
import android.appwidget.AppWidgetManager
import android.content.*
import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.IBinder
import android.util.Log
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lighttigerxiv.simple.mp.compose.*
import com.lighttigerxiv.simple.mp.compose.data.data_classes.Song
import com.lighttigerxiv.simple.mp.compose.data.data_classes.SongCover
import com.lighttigerxiv.simple.mp.compose.data.variables.SORTS
import com.lighttigerxiv.simple.mp.compose.functions.getAllAlbumsImages
import com.lighttigerxiv.simple.mp.compose.functions.getSongs
import com.lighttigerxiv.simple.mp.compose.services.SimpleMPService
import com.lighttigerxiv.simple.mp.compose.widgets.SimpleMPWidget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.burnoutcrew.reorderable.ItemPosition
import java.util.*

class MainVM(application: Application) : AndroidViewModel(application) {


    //************************************************
    // Variables
    //************************************************

    private val context = application

    private val _loadingSongs = MutableStateFlow(true)
    val loadingSongs = _loadingSongs.asStateFlow()


    @SuppressLint("StaticFieldLeak")
    private var smpService: SimpleMPService? = null

    private val _surfaceColor = MutableStateFlow(Color(0xFF000000))
    val surfaceColor = _surfaceColor.asStateFlow()
    fun updateSurfaceColor(newValue: Color) {
        _surfaceColor.update { newValue }
    }


    private val _songs = MutableStateFlow<List<Song>?>(null)
    val songs = _songs.asStateFlow()

    private val _songsCovers = MutableStateFlow<List<SongCover>?>(null)
    val songsCovers = _songsCovers.asStateFlow()

    private val _compressedSongsCovers = MutableStateFlow<List<SongCover>?>(null)
    val compressedSongsCovers = _compressedSongsCovers.asStateFlow()

    private val _queue = MutableStateFlow<List<Song>?>(null)
    val queue = _queue.asStateFlow()

    private val _upNextQueue = MutableStateFlow<List<Song>?>(null)
    val upNextQueue = _upNextQueue.asStateFlow()

    private val _selectedSong = MutableStateFlow<Song?>(null)
    val currentSong = _selectedSong.asStateFlow()

    private val _songAlbumArt = MutableStateFlow<Bitmap?>(null)
    val currentSongAlbumArt = _songAlbumArt.asStateFlow()

    private val _songMinutesAndSecondsText = MutableStateFlow("")
    val songMinutesAndSecondsText = _songMinutesAndSecondsText.asStateFlow()

    private val _currentSongMinutesAndSecondsText = MutableStateFlow("")
    val currentSongMinutesAndSecondsText = _currentSongMinutesAndSecondsText.asStateFlow()
    fun updateCurrentSongMinutesAndSecondsText(newValue: String) {
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

    private val _miniPlayerPeekHeight = MutableStateFlow(0.dp)
    val miniPlayerPeekHeight = _miniPlayerPeekHeight.asStateFlow()

    private val _hideNavProgress = MutableStateFlow(0f)
    val hideNavProgress = _hideNavProgress.asStateFlow()

    private val _showMainPlayer = MutableStateFlow(false)
    val showMainPlayer = _showMainPlayer.asStateFlow()


    //************************************************
    // Callbacks
    //************************************************


    var onSongSelected: () -> Unit = {}


    //************************************************
    // Functions
    //************************************************

    private val simpleMPConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

            viewModelScope.launch {
                withContext(Dispatchers.IO){

                    delay(1)

                    val binder = service as SimpleMPService.LocalBinder
                    smpService = binder.getService()

                    smpService?.let { smp ->

                        _musicPlaying.update { smp.musicPlaying() }

                        _queueShuffled.update { smp.queueShuffled }

                        _songOnRepeat.update { smp.songOnRepeat }

                        _songs.update { getSongs(context, SORTS.RECENT) }

                        _songsCovers.update { getAllAlbumsImages(songs.value, context) }

                        _compressedSongsCovers.update { getAllAlbumsImages(songs.value, context, compressed = true) }


                        if (smp.isMusicPlayingOrPaused()) {

                            _selectedSong.update { smp.currentSong }

                            _songAlbumArt.update { getAlbumArt() }

                            _songSeconds.update { (smp.mediaPlayer.currentPosition / 1000).toFloat() }

                            _songMinutesAndSecondsText.update { getMinutesAndSeconds(currentSong.value!!.duration / 1000) }

                            _currentSongMinutesAndSecondsText.update { getMinutesAndSeconds(smp.mediaPlayer.currentPosition / 1000) }

                            updateMiniPlayerPeekHeight()

                            _queue.update { smp.getQueue() }

                            _upNextQueue.update { smp.getUpNextQueue() }

                            _songPosition.update { smp.currentSongPosition }

                            onSongSelected()
                        }


                        smp.onSongSelected = { song ->

                            _selectedSong.update { song }

                            _songSeconds.update { (smp.mediaPlayer.currentPosition / 1000).toFloat() }

                            _songMinutesAndSecondsText.update { getMinutesAndSeconds(currentSong.value!!.duration / 1000) }

                            _songAlbumArt.update { getAlbumArt() }

                            _songOnRepeat.update { smp.songOnRepeat }

                            updateMiniPlayerPeekHeight()

                            _queue.update { smp.getQueue() }

                            _upNextQueue.update { smp.getUpNextQueue() }

                            _songPosition.update { smp.currentSongPosition }

                            onSongSelected()

                            updateWidget()
                        }


                        smp.onSecondPassed = {

                            _songSeconds.update { (smp.mediaPlayer.currentPosition / 1000).toFloat() }
                        }

                        smp.onQueueShuffle = {

                            _queueShuffled.update { smp.queueShuffled }

                            _queue.update { smp.getQueue() }

                            _upNextQueue.update { smp.getUpNextQueue() }

                            _songPosition.update { smp.currentSongPosition }
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

                            updateMiniPlayerPeekHeight()
                        }

                        _loadingSongs.update { false }
                    }
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

        val minutes = ((receivedSeconds * 1000) / (1000 * 60)) % 60

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
                compressedSongsCovers.value?.first { it.albumID == currentSong.value?.albumID }?.albumArt
            }

            false -> {
                songsCovers.value?.first { it.albumID == currentSong.value?.albumID }?.albumArt
            }
        }
    }


    @OptIn(ExperimentalMaterialApi::class)
    fun onSheetChange(progress: Float, sheetState: BottomSheetState) {

        if (progress > 0 && sheetState.currentValue == BottomSheetValue.Collapsed && sheetState.targetValue == BottomSheetValue.Expanded) {
            if (!showMainPlayer.value) {
                _showMainPlayer.update { true }
            }
        }

        if (progress == 1f && sheetState.targetValue == BottomSheetValue.Collapsed) {
            if (showMainPlayer.value) {
                _showMainPlayer.update { false }
            }
        }

        if (progress == 1f && sheetState.targetValue == BottomSheetValue.Expanded) {
            _showMainPlayer.update { true }
        }

        if (progress in 0.1f..1f) {
            if (sheetState.targetValue == BottomSheetValue.Expanded) {
                _hideNavProgress.update { 0 + progress }
            } else {
                _hideNavProgress.update { 1 - progress }
            }
        }
    }

    fun updateMiniPlayerPeekHeight(){

        val isPortrait = context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

        smpService?.let{
            if(isPortrait && !smpService!!.isMusicPlayingOrPaused()){
                _miniPlayerPeekHeight.update { 55.dp }
            }
            if(isPortrait && smpService!!.isMusicPlayingOrPaused()){
                _miniPlayerPeekHeight.update { 115.dp }
            }
            if(!isPortrait && !smpService!!.isMusicPlayingOrPaused()){
                _miniPlayerPeekHeight.update { 0.dp }
            }
            if(!isPortrait && smpService!!.isMusicPlayingOrPaused()){
                _miniPlayerPeekHeight.update { 55.dp }
            }
        }
    }

    //************************************************
    // Playback Functions
    //************************************************

    fun selectSong(newQueueList: List<Song>, position: Int) {

        if (newQueueList.isNotEmpty()) {

            smpService?.let { smp ->

                smp.selectSong(getApplication(), newQueueList, position)

                _selectedSong.update { smp.currentSong }

                _songMinutesAndSecondsText.update { getMinutesAndSeconds(currentSong.value!!.duration / 1000) }

                _songAlbumArt.update { getAlbumArt() }

                _queue.update { smp.getQueue() }

                _upNextQueue.update { smp.getUpNextQueue() }
            }
        }
    }

    fun onUpNextQueueMove(from: ItemPosition, to: ItemPosition) {

        smpService?.let {
            smpService!!.updateQueue(from, to)
            _queue.update { smpService!!.getQueue() }
            _upNextQueue.update { smpService!!.getUpNextQueue() }
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

        smpService?.let { smp ->

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

    init {
        val serviceIntent = Intent(context, SimpleMPService::class.java)
        context.bindService(serviceIntent, simpleMPConnection, Context.BIND_AUTO_CREATE)
    }
}