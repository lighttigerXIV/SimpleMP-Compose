package com.lighttigerxiv.simple.mp.compose.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.appwidget.AppWidgetManager
import android.content.*
import android.content.Context.MODE_PRIVATE
import android.graphics.Bitmap
import android.os.IBinder
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lighttigerxiv.simple.mp.compose.*
import com.lighttigerxiv.simple.mp.compose.data.AppDatabase
import com.lighttigerxiv.simple.mp.compose.data.Playlist
import com.lighttigerxiv.simple.mp.compose.services.SimpleMPService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*
import kotlin.collections.ArrayList

class ActivityMainVM(application: Application) : AndroidViewModel(application) {

    private val playlistDao = AppDatabase.getInstance(application).playlistDao
    private val preferences = application.getSharedPreferences(application.packageName, MODE_PRIVATE)

    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext

    val songsList = GetSongs.getSongsList(application, "Recent")
    val queueList = MutableLiveData(ArrayList<Song>())
    val upNextQueueList = MutableLiveData(ArrayList<Song>())
    var songsImagesList = GetSongs.getAllAlbumsImages(application)
    var compressedImagesList = GetSongs.getAllAlbumsImages(application, compressed = true)
    lateinit var navController: NavController

    private val _showNavigationBar = MutableStateFlow(true)
    val showNavigationBar = _showNavigationBar.asStateFlow()
    fun setShowNavigationBar(value: Boolean){

        try{

            if(smpService.isMusicPlayingOrPaused() && value){
                miniPlayerHeight.value = 60.dp
            }
            else{
                miniPlayerHeight.value = 0.dp
            }
        }
        catch(_: Exception){}

        _showNavigationBar.value = value
    }

    //--------------------------- Playback States ---------------------------
    private val _isMusicPlaying = MutableStateFlow(false)
    val isMusicPlaying = _isMusicPlaying.asStateFlow()

    private val _isMusicShuffled = MutableStateFlow(false)
    val isMusicShuffled = _isMusicShuffled.asStateFlow()

    private val _isMusicOnRepeat = MutableStateFlow(false)
    val isMusicOnRepeat = _isMusicOnRepeat.asStateFlow()

    private val _currentMediaPlayerPosition = MutableStateFlow(0)
    val currentMediaPlayerPosition = _currentMediaPlayerPosition.asStateFlow()





    //--------------------------- Home Screen (HMS) --------------------------------------



    //-------------------------- Player Screen (PLS) --------------------------------------




    //Home Songs
    var recentHomeSongsList = ArrayList(songsList)
    var oldestHomeSongsList = ArrayList(songsList)
    var ascendentHomeSongsList = ArrayList(songsList)
    var descendentHomeSongsList = ArrayList(songsList)

    //Artists Songs
    var recentArtistsList = ArrayList(songsList)
    var oldestArtistsList = ArrayList(songsList)
    var ascendentArtistsList = ArrayList(songsList)
    var descendentArtistsList = ArrayList(songsList)

    //Albums Songs
    var recentAlbumsList = ArrayList(songsList)
    var oldestAlbumsList = ArrayList(songsList)
    var ascendentAlbumsList = ArrayList(songsList)
    var descendentAlbumsList = ArrayList(songsList)

    //Genres Songs
    var genresList = ArrayList(recentHomeSongsList)

    private val _currentHomeSongsList = MutableStateFlow(recentHomeSongsList)
    val currentHomeSongsList = _currentHomeSongsList.asStateFlow()
    fun setCurrentHomeSongsList(v: ArrayList<Song>){
        _currentHomeSongsList.value = v
    }
    val currentArtistsList = MutableLiveData(recentHomeSongsList)
    val currentAlbumsList = MutableLiveData(recentHomeSongsList)


    private val _playlists: MutableStateFlow<List<Playlist>> = MutableStateFlow(getAllPlaylists())
    val playlists = _playlists.asStateFlow()
    val playlistSongs = MutableLiveData(ArrayList<Song>())
    val currentPlaylistSongs = MutableLiveData(ArrayList<Song>())


    var homeSearchText = MutableLiveData("")
    var artistsSearchText = MutableLiveData("")
    var albumsSearchText = MutableLiveData("")

    var tfNewPlaylistNameValue = MutableLiveData("")

    @SuppressLint("StaticFieldLeak")
    private lateinit var smpService: SimpleMPService
    private var isServiceBound = false


    //Home Screen
    var showHomePopupMenu = MutableLiveData(false)

    //Playlist Screen
    var tfPlaylistNamePlaylistScreen = MutableLiveData("")
    var isOnEditModePlaylistScreen = MutableLiveData(false)
    private val _currentPlaylistImageString = MutableStateFlow("")
    val currentPlaylistImageString = _currentPlaylistImageString.asStateFlow()
    fun setCurrentPlaylistImageString(value: String?){
        _currentPlaylistImageString.value = value ?: ""
    }

    fun loadPlaylistScreen(playlistID: Int) {

        _currentPlaylistImageString.value = playlists.value.find { it.id == playlistID }!!.image ?: ""
    }

    var onPlaylistImageSelected: (bitmapString: String?) -> Unit = {}

    fun updatePlaylistImage(bitmapString: String?, playlistID: Int){

        playlistDao.updatePlaylistImage(bitmapString, playlistID)
        _playlists.value = getAllPlaylists()
        filterPlaylistsPLSS()
    }

    private fun getAllPlaylists(): List<Playlist>{

        playlistDao.getAllPlaylists().forEach { playlist->

            val newSongs = ArrayList<Song>()

            if(playlist.songs != null){
                val type = object : TypeToken<List<Song>>(){}.type
                val songs = Gson().fromJson<List<Song>>(playlist.songs, type )

                songs.forEach { song->

                    if(songsList.contains(song)) newSongs.add(song)
                }

                playlistDao.updatePlaylistSongs( Gson().toJson(newSongs), playlist.id )
            }
        }

        return playlistDao.getAllPlaylists()
    }

    //Callbacks
    var onSongSelected: (Song) -> Unit = {}
    var onSongSecondPassed: (position: Int) -> Unit = {}
    var onMediaPlayerStopped: () -> Unit = {}


    //Player Sates



    //Song Related
    var selectedSong = MutableLiveData<Song?>(null)
    var selectedSongTitle = MutableLiveData("")
    var selectedSongArtistName = MutableLiveData("")
    var selectedSongPath = MutableLiveData("")
    var selectedSongDuration = MutableLiveData(0)
    var selectedSongMinutesAndSeconds = MutableLiveData("")
    var selectedSongCurrentMinutesAndSeconds = MutableLiveData("")
    var selectedSongAlbumArt = MutableLiveData<Bitmap?>(null)

    var miniPlayerHeight = MutableLiveData(0.dp)


    fun getCurrentSongPosition(): Int {

        return if (isServiceBound)
            smpService.currentSongPosition
        else
            0
    }


    private val simpleMPConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

            val binder = service as SimpleMPService.LocalBinder
            smpService = binder.getService()

            fun updateWidget(){
                val intent = Intent(application, SimpleMPWidget::class.java)
                intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE

                val ids = AppWidgetManager.getInstance(application)
                    .getAppWidgetIds(ComponentName(application, SimpleMPWidget::class.java))

                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
                application.sendBroadcast(intent)
            }


            if (smpService.isMusicPlayingOrPaused()) {

                selectedSong.value = smpService.currentSong
                selectedSongTitle.value = selectedSong.value!!.title
                selectedSongArtistName.value = selectedSong.value!!.artistName
                selectedSongPath.value = selectedSong.value!!.path
                selectedSongDuration.value = selectedSong.value!!.duration
                _currentMediaPlayerPosition.value = smpService.getCurrentMediaPlayerPosition()
                selectedSongMinutesAndSeconds.value = getMinutesAndSecondsFromPosition(selectedSongDuration.value!! / 1000)
                selectedSongCurrentMinutesAndSeconds.value = "0:00"
                updateCurrentSongAlbumArt()
                miniPlayerHeight.value = 60.dp


                queueList.value = smpService.getCurrentQueueList()
            }

            _isMusicPlaying.value = smpService.isMusicPlaying()
            _isMusicShuffled.value = smpService.isMusicShuffled
            _isMusicOnRepeat.value = smpService.isMusicOnRepeat

            isServiceBound = true


            smpService.onSongSelected = { song ->

                selectedSong.value = smpService.currentSong
                selectedSongTitle.value = selectedSong.value!!.title
                selectedSongArtistName.value = selectedSong.value!!.artistName
                selectedSongPath.value = selectedSong.value!!.path
                selectedSongDuration.value = selectedSong.value!!.duration
                _currentMediaPlayerPosition.value = smpService.getCurrentMediaPlayerPosition()
                selectedSongMinutesAndSeconds.value = getMinutesAndSecondsFromPosition(selectedSongDuration.value!! / 1000)
                selectedSongCurrentMinutesAndSeconds.value = "0:00"
                updateCurrentSongAlbumArt()

                _isMusicShuffled.value = smpService.isMusicShuffled
                _isMusicOnRepeat.value = smpService.isMusicOnRepeat

                queueList.value = smpService.getCurrentQueueList()
                upNextQueueList.value = smpService.getUpNextQueueList()
                onSongSelected(song)

                _isMusicPlaying.value = smpService.isMusicPlaying()

                updateWidget()
            }


            smpService.onSongSecondPassed = { mediaPlayerPosition ->
                _currentMediaPlayerPosition.value = mediaPlayerPosition
                selectedSongCurrentMinutesAndSeconds.value = getMinutesAndSecondsFromPosition(mediaPlayerPosition / 1000)
                onSongSecondPassed(mediaPlayerPosition / 1000)
                _isMusicPlaying.value = smpService.isMusicPlaying()
            }

            smpService.onSongPaused = {

                _isMusicPlaying.value = smpService.isMusicPlaying()
                updateWidget()
            }

            smpService.onSongResumed = {

                _isMusicPlaying.value = smpService.isMusicPlaying()
                updateWidget()
            }

            smpService.onMediaPlayerStopped = {
                selectedSongPath.value = ""
                onMediaPlayerStopped()
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {}
    }


    fun selectSong(newQueueList: ArrayList<Song>, position: Int) {

        if (newQueueList.size > 0) {

            smpService.selectSong(getApplication(), newQueueList, position)
            onSongSelected(smpService.currentSong!!)
            selectedSong.value = smpService.currentSong
            selectedSongTitle.value = selectedSong.value!!.title
            selectedSongArtistName.value = selectedSong.value!!.artistName
            selectedSongPath.value = selectedSong.value!!.path
            selectedSongDuration.value = selectedSong.value!!.duration
            selectedSongMinutesAndSeconds.value = getMinutesAndSecondsFromPosition(selectedSongDuration.value!! / 1000)
            selectedSongCurrentMinutesAndSeconds.value = "0:00"
            updateCurrentSongAlbumArt()
        }
    }

    fun shuffleAndPlay(newQueueList: ArrayList<Song>){

        if(isServiceBound){
            smpService.shuffleAndPlay(newQueueList, context)
        }
    }

    fun unshuffleAndPlay(newQueueList: ArrayList<Song>, position: Int){

        if(isServiceBound){
            if(smpService.isMusicShuffled) smpService.toggleShuffle()

            smpService.selectSong(context, newQueueList, position)
        }
    }


    fun shuffle(newQueueList: ArrayList<Song>) {

        smpService.shuffleAndPlay(newQueueList, getApplication())
    }


    fun seekSongPosition(position: Int) {
        if (isServiceBound) {
            smpService.seekTo(position)
        }
    }

    fun updateCurrentSongAlbumArt() {

        selectedSongAlbumArt.value = songsImagesList.find { it.albumID == selectedSong.value!!.albumID }!!.albumArt
    }

    fun toggleShuffle() {
        smpService.toggleShuffle()
        _isMusicPlaying.value = smpService.isMusicPlaying()
        _isMusicShuffled.value = smpService.isMusicShuffled
        queueList.value = smpService.getCurrentQueueList()
        upNextQueueList.value = smpService.getUpNextQueueList()
    }

    fun selectPreviousSong() {
        smpService.selectPreviousSong(getApplication())
    }

    fun selectNextSong() {
        smpService.selectNextSong(getApplication())
    }

    fun toggleRepeat() {
        smpService.toggleRepeat()
        _isMusicOnRepeat.value = smpService.isMusicOnRepeat
    }


    fun getMinutesAndSecondsFromPosition(position: Int): String {

        val minutes = position.div(60)
        val seconds = position.rem(60)

        var stringSeconds = "$seconds"


        if (seconds < 10)
            stringSeconds = "0$seconds"


        return "$minutes:$stringSeconds"
    }


    fun pauseResumeMusic() {
        if (isServiceBound) {

            smpService.pauseResumeMusic(getApplication())
        }
    }


    fun getIsMusicPaused(): Boolean {
        return if (isServiceBound) !smpService.isMusicPlaying() else false
    }


    init {

        val serviceIntent = Intent(application, SimpleMPService::class.java)
        application.bindService(serviceIntent, simpleMPConnection, Context.BIND_AUTO_CREATE)


        val artistsList = songsList.distinctBy { it.artistID }
        recentArtistsList = ArrayList(artistsList)
        oldestArtistsList = ArrayList(artistsList)
        ascendentArtistsList = ArrayList(artistsList)
        descendentArtistsList = ArrayList(artistsList)


        val albumsList = songsList.distinctBy { it.albumID }
        recentAlbumsList = ArrayList(albumsList)
        oldestAlbumsList = ArrayList(albumsList)
        ascendentAlbumsList = ArrayList(albumsList)
        descendentAlbumsList = ArrayList(albumsList)


        genresList = recentHomeSongsList.distinctBy { song -> song.genreID } as ArrayList<Song>


        //Sorts the songs
        val sharedPrefs = application.getSharedPreferences("sorting", MODE_PRIVATE)
        val homeSort = sharedPrefs.getString("home", "Recent")
        val artistsSort = sharedPrefs.getString("artists", "Recent")
        val albumsSort = sharedPrefs.getString("albums", "Recent")


        oldestHomeSongsList.reverse()
        ascendentHomeSongsList.sortBy { it.title }
        descendentHomeSongsList.sortByDescending { it.title }

        oldestArtistsList.reverse()
        ascendentArtistsList.sortBy { it.artistName }
        descendentArtistsList.sortByDescending { it.artistName }

        oldestAlbumsList.reverse()
        ascendentAlbumsList.sortBy { it.albumName }
        descendentAlbumsList.sortByDescending { it.albumName }


        when (homeSort) {

            "Recent" -> {
                _currentHomeSongsList.value = recentHomeSongsList
            }
            "Oldest" -> {
                _currentHomeSongsList.value = oldestHomeSongsList
            }
            "Ascendent" -> {
                _currentHomeSongsList.value = ascendentHomeSongsList
            }
            "Descendent" -> {
                _currentHomeSongsList.value = descendentHomeSongsList
            }
        }

        when (artistsSort) {

            "Recent" -> {
                currentArtistsList.value = recentArtistsList
            }
            "Oldest" -> {
                currentArtistsList.value = oldestArtistsList
            }
            "Ascendent" -> {
                currentArtistsList.value = ascendentArtistsList
            }
            "Descendent" -> {
                currentArtistsList.value = descendentArtistsList
            }
        }

        when (albumsSort) {

            "Recent" -> {
                currentAlbumsList.value = recentAlbumsList
            }
            "Oldest" -> {
                currentAlbumsList.value = oldestAlbumsList
            }
            "Ascendent" -> {
                currentAlbumsList.value = ascendentAlbumsList
            }
            "Descendent" -> {
                currentAlbumsList.value = descendentAlbumsList
            }
        }
    }


    fun createPlaylist(name: String) {

        playlistDao.insertPlaylist(
            Playlist(name = name)
        )

        _playlists.value = getAllPlaylists()
        _currentPlaylistsPLSS.value = _playlists.value
    }


    fun deletePlaylist(playlistID: Int) {

        playlistDao.deletePlaylist(playlistID = playlistID)
        _playlists.value = getAllPlaylists()
        _currentPlaylistsPLSS.value = _playlists.value
    }

    fun updatePlaylistName(playlistID: Int, playlistName: String) {

        playlistDao.updatePlaylistName(
            playlistName = playlistName,
            playlistID = playlistID
        )

        _playlists.value = getAllPlaylists()
    }


    fun updatePlaylistSongs(songsJson: String, playlistID: Int) {

        playlistDao.updatePlaylistSongs(
            songsJson = songsJson,
            playlistID = playlistID
        )

        _playlists.value = getAllPlaylists()
        filterPlaylistsPLSS()
    }


    fun filterHomeSongsList(sortType: String) {

        when (sortType) {

            "Recent" -> _currentHomeSongsList.value = recentHomeSongsList.filterNot { !it.title.lowercase().trim().contains(homeSearchText.value!!.lowercase().trim()) } as ArrayList<Song>
            "Oldest" -> _currentHomeSongsList.value = oldestHomeSongsList.filterNot { !it.title.lowercase().trim().contains(homeSearchText.value!!.lowercase().trim()) } as ArrayList<Song>
            "Ascendent" -> _currentHomeSongsList.value = ascendentHomeSongsList.filterNot { !it.title.lowercase().trim().contains(homeSearchText.value!!.lowercase().trim()) } as ArrayList<Song>
            "Descendent" -> _currentHomeSongsList.value = descendentHomeSongsList.filterNot { !it.title.lowercase().trim().contains(homeSearchText.value!!.lowercase().trim()) } as ArrayList<Song>
        }
    }

    fun filterArtistsList(sortType: String) {

        when (sortType) {

            "Recent" -> currentArtistsList.value = recentArtistsList.filterNot { !it.artistName.lowercase().trim().contains(artistsSearchText.value!!.lowercase().trim()) } as ArrayList<Song>
            "Oldest" -> currentArtistsList.value = oldestArtistsList.filterNot { !it.artistName.lowercase().trim().contains(artistsSearchText.value!!.lowercase().trim()) } as ArrayList<Song>
            "Ascendent" -> currentArtistsList.value = ascendentArtistsList.filterNot { !it.artistName.lowercase().trim().contains(artistsSearchText.value!!.lowercase().trim()) } as ArrayList<Song>
            "Descendent" -> currentArtistsList.value = descendentArtistsList.filterNot { !it.artistName.lowercase().trim().contains(artistsSearchText.value!!.lowercase().trim()) } as ArrayList<Song>
        }
    }

    fun filterAlbumsList(sortType: String) {

        when (sortType) {

            "Recent" -> currentAlbumsList.value = recentAlbumsList.filterNot { !it.albumName.lowercase().trim().contains(albumsSearchText.value!!.lowercase().trim()) } as ArrayList<Song>
            "Oldest" -> currentAlbumsList.value = oldestAlbumsList.filterNot { !it.albumName.lowercase().trim().contains(albumsSearchText.value!!.lowercase().trim()) } as ArrayList<Song>
            "Ascendent" -> currentAlbumsList.value = ascendentAlbumsList.filterNot { !it.albumName.lowercase().trim().contains(albumsSearchText.value!!.lowercase().trim()) } as ArrayList<Song>
            "Descendent" -> currentAlbumsList.value = descendentAlbumsList.filterNot { !it.albumName.lowercase().trim().contains(albumsSearchText.value!!.lowercase().trim()) } as ArrayList<Song>
        }
    }

    //-------------------------- Settings Screen ----------------------------
    private val _themeModeSetting = MutableStateFlow(preferences.getString("ThemeMode", "System"))
    val themeModeSetting = _themeModeSetting.asStateFlow()
    private val _darkModeSetting = MutableStateFlow(preferences.getString("DarkMode", "Color"))
    val darkModeSetting = _darkModeSetting.asStateFlow()
    private val _filterAudioSetting = MutableStateFlow(preferences.getString("FilterAudio", "60"))
    val filterAudioSetting = _filterAudioSetting.asStateFlow()
    private val _themeAccentSetting = MutableStateFlow(preferences.getString("ThemeAccent", "Default"))
    val themeAccentSetting = _themeAccentSetting.asStateFlow()

    private val _surfaceColor = MutableStateFlow(Color(0xff000000))
    val surfaceColor = _surfaceColor.asStateFlow()
    fun setSurfaceColor(value: Color){
        _surfaceColor.value = value
    }


    val selectedThemeModeDialog = MutableLiveData(themeModeSetting.value)
    val selectedDarkModeDialog = MutableLiveData(darkModeSetting.value)
    val etFilterAudioDialog = MutableLiveData(filterAudioSetting.value)
    val selectedThemeAccentDialog = MutableLiveData(themeAccentSetting.value)


    fun setThemeMode(){

        preferences.edit().putString("ThemeMode", selectedThemeModeDialog.value).apply()
        _themeModeSetting.value = selectedThemeModeDialog.value
    }

    fun setDarkMode(){

        preferences.edit().putString("DarkMode", selectedDarkModeDialog.value).apply()
        _darkModeSetting.value = selectedDarkModeDialog.value
    }


    fun setFilterAudio(){

        preferences.edit().putString("FilterAudio", etFilterAudioDialog.value).apply()
        _filterAudioSetting.value = etFilterAudioDialog.value

        Toast.makeText(context, "Setting will take effect on next app restart", Toast.LENGTH_LONG).show()
    }

    fun setThemeAccent(theme: String = "Default"){

        preferences.edit().putString("ThemeAccent", theme).apply()
        _themeAccentSetting.value = theme
    }



    //------------------------- Playlists Screen -------------------------

    private val _searchValuePLSS = MutableStateFlow("")
    val searchValuePLSS = _searchValuePLSS.asStateFlow()
    fun setSearchValuePLSS(v: String){
        _searchValuePLSS.value = v
    }

    private val _currentPlaylistsPLSS: MutableStateFlow<List<Playlist>> = MutableStateFlow(playlists.value)
    val currentPlaylistsPLSS = _currentPlaylistsPLSS.asStateFlow()

    fun filterPlaylistsPLSS(){

        val newPlaylists = playlists.value.filter {
            it.name.lowercase().trim().contains(_searchValuePLSS.value.lowercase().trim())
        }


        _currentPlaylistsPLSS.value = newPlaylists
    }
}