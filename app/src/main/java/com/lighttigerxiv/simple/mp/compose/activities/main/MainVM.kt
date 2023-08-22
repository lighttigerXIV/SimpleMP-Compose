package com.lighttigerxiv.simple.mp.compose.activities.main

import android.annotation.SuppressLint
import android.app.Application
import android.appwidget.AppWidgetManager
import android.content.*
import android.content.res.Configuration
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lighttigerxiv.simple.mp.compose.*
import com.lighttigerxiv.simple.mp.compose.data.data_classes.Album
import com.lighttigerxiv.simple.mp.compose.data.data_classes.Artist
import com.lighttigerxiv.simple.mp.compose.data.data_classes.Song
import com.lighttigerxiv.simple.mp.compose.data.data_classes.SongsData
import com.lighttigerxiv.simple.mp.compose.data.mongodb.getMongoRealm
import com.lighttigerxiv.simple.mp.compose.data.mongodb.queries.CacheQueries
import com.lighttigerxiv.simple.mp.compose.data.variables.Settings
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
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class MainVM(application: Application) : AndroidViewModel(application) {


    //************************************************
    // Variables
    //************************************************

    private val context = application

    private val mainVM = this

    private val _loadingSongs = MutableStateFlow(true)
    val loadingSongs = _loadingSongs.asStateFlow()

    private val cachedQueries = CacheQueries(getMongoRealm())


    @SuppressLint("StaticFieldLeak")
    private var smpService: SimpleMPService? = null

    private val _surfaceColor = MutableStateFlow(Color(0xFF000000))
    val surfaceColor = _surfaceColor.asStateFlow()
    fun updateSurfaceColor(newValue: Color) {
        _surfaceColor.update { newValue }
    }

    private val _songsData = MutableStateFlow<SongsData?>(null)
    val songsData = _songsData.asStateFlow()

    private val _songCount = MutableStateFlow(0)
    val songCount = _songCount.asStateFlow()

    private val _indexedSongsCount = MutableStateFlow(0)
    val indexedSongsCount = _indexedSongsCount.asStateFlow()

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
    var onFinish: () -> Unit = {}


    //************************************************
    // Functions
    //************************************************

    private val simpleMPConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

            viewModelScope.launch {
                withContext(Dispatchers.IO) {

                    delay(1)

                    val binder = service as SimpleMPService.LocalBinder
                    smpService = binder.getService()

                    smpService?.let { smp ->

                        smp.mainVM = mainVM
                        _musicPlaying.update { smp.musicPlaying() }
                        _queueShuffled.update { smp.queueShuffled }
                        _songOnRepeat.update { smp.songOnRepeat }
                        getSongsData()

                        if (smp.isMusicPlayingOrPaused()) {

                            _selectedSong.update { smp.currentSong }
                            _songAlbumArt.update { getAlbumArt(songsData.value!!.songs, currentSong.value!!.albumID) }
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
                            _songAlbumArt.update { getAlbumArt(songsData.value!!.songs, song.albumID) }
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
                            onFinish()
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

    @SuppressLint("Range")
    suspend fun getSongsData() {

        val songs = ArrayList<Song>()
        val cachedSongs = cachedQueries.getSongs()
        val artists = ArrayList<Artist>()
        val cachedArtists = cachedQueries.getArtists()
        val albums = ArrayList<Album>()
        val cachedAlbums = cachedQueries.getAlbums()


        //Shows Cached Data if it exists
        if (cachedSongs.isNotEmpty() && cachedAlbums.isNotEmpty() && cachedArtists.isNotEmpty()) {

            _songCount.update { cachedSongs.size }

            val durationFilter = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE).getString(Settings.FILTER_AUDIO, "30")!!.toInt() * 1000

            cachedSongs.forEach { cachedSong ->

                if(cachedSong.duration > durationFilter){
                    songs.add(
                        Song(
                            id = cachedSong.id,
                            path = cachedSong.path,
                            title = cachedSong.title,
                            albumID = cachedSong.albumID,
                            duration = cachedSong.duration,
                            artistID = cachedSong.artistID,
                            year = cachedSong.year,
                            genre = cachedSong.genre,
                            modificationDate = cachedSong.modificationDate
                        )
                    )

                    _indexedSongsCount.update { indexedSongsCount.value + 1 }
                }
            }

            cachedArtists.forEach { cachedArtist ->

                if(songs.any{it.artistID == cachedArtist.id}){
                    artists.add(
                        Artist(
                            id = cachedArtist.id,
                            name = cachedArtist.name,
                            cover = null
                        )
                    )
                }
            }

            cachedAlbums.forEach { cachedAlbum ->

                if(songs.any { it.albumID == cachedAlbum.id }){
                    albums.add(
                        Album(
                            id = cachedAlbum.id,
                            title = cachedAlbum.title,
                            art = null,
                            artistID = cachedAlbum.artistID
                        )
                    )
                }
            }

            _songsData.update {
                SongsData(
                    songs,
                    artists,
                    albums
                )
            }

            val newAlbums = ArrayList<Album>()

            albums.forEach { album ->

                val newAlbum = album.copy(art = getAlbumArt(songs, album.id))
                newAlbums.add(newAlbum)
            }

            _songsData.update {
                SongsData(
                    songs,
                    artists,
                    newAlbums
                )
            }

            _indexedSongsCount.update { 0 }
            _songCount.update { 0 }

        } else {

            //Gets all songs from the device
            indexSongs()
        }
    }

    @SuppressLint("Range")
    suspend fun indexSongs(onFinish: () -> Unit = {}) {

        _indexedSongsCount.update { 0 }
        cachedQueries.clear()


        val songs = ArrayList<Song>()
        val artists = ArrayList<Artist>()
        val albums = ArrayList<Album>()
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        var cursor = context.contentResolver.query(uri, null, null, null, null)
        var count = 0

        if (cursor != null) {
            while (cursor.moveToNext()) {
                count += 1
            }
        }

        cursor?.close()

        _songCount.update { count }

        cursor = context.contentResolver.query(uri, null, null, null, null)

        if (cursor != null) {
            while (cursor.moveToNext()) {

                try {

                    val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                    val songPath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))

                    val retriever = MediaMetadataRetriever()
                    retriever.setDataSource(songPath)

                    var title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                    var albumTitle = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
                    val albumID = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
                    var duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                    var artistName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST)
                    val artistID = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID))
                    var genre = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE)
                    val modificationDate = File(songPath).lastModified()


                    if (title == null) {
                        title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                    }

                    if (albumTitle == null) {
                        albumTitle = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM))
                    }

                    if (duration == null) {
                        duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                    }

                    if (artistName == null) {
                        artistName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST))
                    }

                    if (!artists.any { it.id == artistID }) {

                        val artist = Artist(
                            artistID,
                            artistName ?: "",
                            null
                        )

                        artists.add(artist)

                        cachedQueries.addArtist(artist)
                    }

                    if (!albums.any { it.id == albumID }) {

                        val album = Album(
                            albumID,
                            albumTitle ?: "",
                            null,
                            artistID
                        )

                        albums.add(album)

                        cachedQueries.addAlbum(album)
                    }


                    if (genre == null) genre = context.getString(R.string.Undefined)
                    val year = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.YEAR))

                    if (title != null && duration != null) {

                        val song = Song(id, songPath, title, albumID, duration.toInt(), artistID, year, genre, modificationDate)

                        songs.add(song)

                        cachedQueries.addSong(song)

                        _indexedSongsCount.update { indexedSongsCount.value + 1 }
                    }

                } catch (e: Exception) {
                    Log.e("Song Error", "Exception while getting song. Details -> ${e.message}")
                }
            }
        }

        cursor?.close()

        getSongsData()

        onFinish()

        _indexedSongsCount.update { 0 }
        _songCount.update { 0 }
    }

    fun getSongArt(song: Song): Bitmap? {

        return songsData.value?.albums?.first { it.id == song.albumID }?.art
    }

    fun getSongArtist(song: Song): Artist {

        return songsData.value!!.artists.first { it.id == song.artistID }
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

    private fun getAlbumArt(songs: List<Song>, id: Long): Bitmap? {

        return try {

            val songWithAlbumID = songs.first { it.albumID == id }.id

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                val songWithAlbumUri = ContentUris.withAppendedId(uri, songWithAlbumID)

                context.contentResolver.loadThumbnail(songWithAlbumUri, Size(400, 400), null)
            } else {

                val sArtWorkUri = Uri.parse("content://media/external/audio/albumart")
                val albumArtUri = ContentUris.withAppendedId(sArtWorkUri, id)

                MediaStore.Images.Media.getBitmap(context.contentResolver, albumArtUri)
            }

        } catch (e: Exception) {
            null
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

    fun updateMiniPlayerPeekHeight() {

        val isPortrait = context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

        smpService?.let {
            if (isPortrait && !smpService!!.isMusicPlayingOrPaused()) {
                _miniPlayerPeekHeight.update { 55.dp }
            }
            if (isPortrait && smpService!!.isMusicPlayingOrPaused()) {
                _miniPlayerPeekHeight.update { 115.dp }
            }
            if (!isPortrait && !smpService!!.isMusicPlayingOrPaused()) {
                _miniPlayerPeekHeight.update { 0.dp }
            }
            if (!isPortrait && smpService!!.isMusicPlayingOrPaused()) {
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

                _songAlbumArt.update { getAlbumArt(songsData.value!!.songs, currentSong.value!!.albumID) }

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