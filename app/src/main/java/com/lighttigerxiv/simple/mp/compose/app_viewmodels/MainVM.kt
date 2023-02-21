package com.lighttigerxiv.simple.mp.compose.app_viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.appwidget.AppWidgetManager
import android.content.*
import android.content.Context.MODE_PRIVATE
import android.graphics.Bitmap
import android.os.IBinder
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.google.gson.Gson
import com.lighttigerxiv.simple.mp.compose.*
import com.lighttigerxiv.simple.mp.compose.data.AppDatabase
import com.lighttigerxiv.simple.mp.compose.data.Playlist
import com.lighttigerxiv.simple.mp.compose.services.SimpleMPService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class MainVM(application: Application) : AndroidViewModel(application) {


    //************************************************
    // Variables
    //************************************************


    private val context = application

    private val _songs = MutableStateFlow<List<Song>?>(null)
    val songs = _songs.asStateFlow()


    /*
    private val _recentArtistSongs = MutableStateFlow<List<Song>?>(null)
    val recentArtistSongs = _recentArtistSongs.asStateFlow()

    private val _oldestArtistSongs = MutableStateFlow<List<Song>?>(null)
    val oldestArtistSongs = _oldestArtistSongs.asStateFlow()

    private val _ascendentArtistSongs = MutableStateFlow<List<Song>?>(null)
    val ascendentArtistSongs = _ascendentArtistSongs.asStateFlow()

    private val _descendentArtistSongs = MutableStateFlow<List<Song>?>(null)
    val descendentArtistSongs = _recentArtistSongs.asStateFlow()

    private val _recentAlbumsSongs = MutableStateFlow<List<Song>?>(null)
    val recentAlbumsSongs = _recentAlbumsSongs.asStateFlow()

    private val _oldestAlbumsSongs = MutableStateFlow<List<Song>?>(null)
    val oldestAlbumsSongs = _oldestAlbumsSongs.asStateFlow()

    private val _ascendentAlbumsSongs = MutableStateFlow<List<Song>?>(null)
    val ascendentAlbumsSongs = _ascendentAlbumsSongs.asStateFlow()

    private val _descendentAlbumsSongs = MutableStateFlow<List<Song>?>(null)
    val descendentAlbumsSongs = _recentAlbumsSongs.asStateFlow()


     */






    private val playlistDao = AppDatabase.getInstance(application).playlistDao
    private val preferences = application.getSharedPreferences(application.packageName, MODE_PRIVATE)




    private val _surfaceColor = MutableStateFlow(Color(0xFF000000))
    val surfaceColor = _surfaceColor.asStateFlow()
    fun updateSurfaceColor(newValue:Color) {
        _surfaceColor.update { newValue }
    }

    val queueList = MutableLiveData(ArrayList<Song>())
    val upNextQueueList = MutableLiveData(ArrayList<Song>())
    var songsImagesList = GetSongs.getAllAlbumsImages(application)
    var compressedImagesList = GetSongs.getAllAlbumsImages(application, compressed = true)
    lateinit var navController: NavController

    private val _showNavigationBar = MutableStateFlow(true)
    val showNavigationBar = _showNavigationBar.asStateFlow()
    fun updateShowNavigationBar(value: Boolean){

        try{

            if(smpService.isMusicPlayingOrPaused() && value){
                _miniPlayerHeight.update { 60.dp }
            }
            else{
                _miniPlayerHeight.update { 0.dp }
            }
        }
        catch(_: Exception){}

        _showNavigationBar.update { value }
    }


    private val _isMusicPlaying = MutableStateFlow(false)
    val isMusicPlaying = _isMusicPlaying.asStateFlow()

    private val _isMusicShuffled = MutableStateFlow(false)
    val isMusicShuffled = _isMusicShuffled.asStateFlow()

    private val _isMusicOnRepeat = MutableStateFlow(false)
    val isMusicOnRepeat = _isMusicOnRepeat.asStateFlow()

    private val _currentMediaPlayerPosition = MutableStateFlow(0)
    val currentMediaPlayerPosition = _currentMediaPlayerPosition.asStateFlow()


    //Genres Songs
    var genresList = ArrayList<String>()


    val currentArtistsList = MutableLiveData<List<Song>>(ArrayList())
    val currentAlbumsList = MutableLiveData<List<Song>>(ArrayList())


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

        /*
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

         */

        return playlistDao.getAllPlaylists()
    }

    //Callbacks
    var onSongSelected: (Song) -> Unit = {}
    var onSongSecondPassed: (position: Int) -> Unit = {}
    var onMediaPlayerStopped: () -> Unit = {}


    //Song Related
    private val _selectedSong = MutableStateFlow<Song?>(null)
    val selectedSong = _selectedSong.asStateFlow()

    var selectedSongTitle = MutableLiveData("")
    var selectedSongArtistName = MutableLiveData("")
    var selectedSongPath = MutableLiveData("")
    var selectedSongDuration = MutableLiveData(0)
    var selectedSongMinutesAndSeconds = MutableLiveData("")
    var selectedSongCurrentMinutesAndSeconds = MutableLiveData("")
    var selectedSongAlbumArt = MutableLiveData<Bitmap?>(null)

    private val _miniPlayerHeight = MutableStateFlow(0.dp)
    val miniPlayerHeight = _miniPlayerHeight.asStateFlow()
    fun updateMiniPlayerHeight(newValue: Dp) {
        _miniPlayerHeight.update { newValue }
    }


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

                _selectedSong.update { smpService.currentSong }
                selectedSongTitle.value = selectedSong.value!!.title
                selectedSongArtistName.value = selectedSong.value!!.artist
                selectedSongPath.value = selectedSong.value!!.path
                selectedSongDuration.value = selectedSong.value!!.duration
                _currentMediaPlayerPosition.value = smpService.getCurrentMediaPlayerPosition()
                selectedSongMinutesAndSeconds.value = getMinutesAndSecondsFromPosition(selectedSongDuration.value!! / 1000)
                selectedSongCurrentMinutesAndSeconds.value = "0:00"
                updateCurrentSongAlbumArt()
                _miniPlayerHeight.update { 60.dp }


                queueList.value = smpService.getCurrentQueueList()
            }

            _isMusicPlaying.value = smpService.isMusicPlaying()
            _isMusicShuffled.value = smpService.isMusicShuffled
            _isMusicOnRepeat.value = smpService.isMusicOnRepeat

            isServiceBound = true


            smpService.onSongSelected = { song ->

                _selectedSong.update { smpService.currentSong }
                selectedSongTitle.value = selectedSong.value!!.title
                selectedSongArtistName.value = selectedSong.value!!.artist
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

                _miniPlayerHeight.update { 60.dp }

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


    fun selectSong(newQueueList: List<Song>, position: Int) {

        if (newQueueList.isNotEmpty()) {

            smpService.selectSong(getApplication(), newQueueList, position)
            onSongSelected(smpService.currentSong!!)
            _selectedSong.update { smpService.currentSong }
            selectedSongTitle.value = selectedSong.value!!.title
            selectedSongArtistName.value = selectedSong.value!!.artist
            selectedSongPath.value = selectedSong.value!!.path
            selectedSongDuration.value = selectedSong.value!!.duration
            selectedSongMinutesAndSeconds.value = getMinutesAndSecondsFromPosition(selectedSongDuration.value!! / 1000)
            selectedSongCurrentMinutesAndSeconds.value = "0:00"
            updateCurrentSongAlbumArt()
        }
    }

    fun shuffleAndPlay(newQueueList: List<Song>){

        if(isServiceBound){
            smpService.shuffleAndPlay(newQueueList, context)
        }
    }

    fun unshuffleAndPlay(newQueueList: List<Song>, position: Int){

        if(isServiceBound){
            if(smpService.isMusicShuffled) smpService.toggleShuffle()

            smpService.selectSong(context, newQueueList, position)
        }
    }


    fun shuffle(newQueueList: List<Song>) {

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

        _songs.update { getSongs(context, "Recent") }
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





    fun filterArtistsList(sortType: String) {

        /*
        when (sortType) {

            "Recent" -> currentArtistsList.value = recentArtistsList.filterNot { !it.artistName.lowercase().trim().contains(artistsSearchText.value!!.lowercase().trim()) } as ArrayList<Song>
            "Oldest" -> currentArtistsList.value = oldestArtistsList.filterNot { !it.artistName.lowercase().trim().contains(artistsSearchText.value!!.lowercase().trim()) } as ArrayList<Song>
            "Ascendent" -> currentArtistsList.value = ascendentArtistsList.filterNot { !it.artistName.lowercase().trim().contains(artistsSearchText.value!!.lowercase().trim()) } as ArrayList<Song>
            "Descendent" -> currentArtistsList.value = descendentArtistsList.filterNot { !it.artistName.lowercase().trim().contains(artistsSearchText.value!!.lowercase().trim()) } as ArrayList<Song>
        }

         */
    }

    fun filterAlbumsList(sortType: String) {

        /*
        when (sortType) {

            "Recent" -> currentAlbumsList.value = recentAlbumsList.filterNot { !it.albumName.lowercase().trim().contains(albumsSearchText.value!!.lowercase().trim()) } as ArrayList<Song>
            "Oldest" -> currentAlbumsList.value = oldestAlbumsList.filterNot { !it.albumName.lowercase().trim().contains(albumsSearchText.value!!.lowercase().trim()) } as ArrayList<Song>
            "Ascendent" -> currentAlbumsList.value = ascendentAlbumsList.filterNot { !it.albumName.lowercase().trim().contains(albumsSearchText.value!!.lowercase().trim()) } as ArrayList<Song>
            "Descendent" -> currentAlbumsList.value = descendentAlbumsList.filterNot { !it.albumName.lowercase().trim().contains(albumsSearchText.value!!.lowercase().trim()) } as ArrayList<Song>
        }

         */
    }


    private val _themeModeSetting = MutableStateFlow(preferences.getString("ThemeMode", "System")!!)
    val themeModeSetting = _themeModeSetting.asStateFlow()

    private val _darkModeSetting = MutableStateFlow(preferences.getString("DarkMode", "Color")!!)
    val darkModeSetting = _darkModeSetting.asStateFlow()

    private val _filterAudioSetting = MutableStateFlow(preferences.getString("FilterAudio", "60")!!)
    val filterAudioSetting = _filterAudioSetting.asStateFlow()

    private val _themeAccentSetting = MutableStateFlow(preferences.getString("ThemeAccent", "Default")!!)
    val themeAccentSetting = _themeAccentSetting.asStateFlow()

    private val _downloadArtistCoverSetting = MutableStateFlow(preferences.getBoolean("DownloadArtistCoverSetting", true))
    val downloadArtistCoverSetting = _downloadArtistCoverSetting.asStateFlow()

    private val _downloadOverDataSetting = MutableStateFlow(preferences.getBoolean("DownloadOverDataSetting", false))
    val downloadOverDataSetting = _downloadOverDataSetting.asStateFlow()


    val selectedThemeModeDialog = MutableLiveData(themeModeSetting.value)
    val selectedDarkModeDialog = MutableLiveData(darkModeSetting.value)
    val etFilterAudioDialog = MutableLiveData(filterAudioSetting.value)
    val selectedThemeAccentDialog = MutableLiveData(themeAccentSetting.value)

    fun setThemeMode(){

        preferences.edit().putString("ThemeMode", selectedThemeModeDialog.value).apply()
        _themeModeSetting.value = selectedThemeModeDialog.value!!
    }

    fun setDarkMode(){

        preferences.edit().putString("DarkMode", selectedDarkModeDialog.value).apply()
        _darkModeSetting.value = selectedDarkModeDialog.value!!
    }


    fun setFilterAudio(){

        preferences.edit().putString("FilterAudio", etFilterAudioDialog.value).apply()
        _filterAudioSetting.value = etFilterAudioDialog.value!!

        Toast.makeText(context, "Setting will take effect on next app restart", Toast.LENGTH_LONG).show()
    }

    fun setThemeAccent(theme: String = "Default"){

        preferences.edit().putString("ThemeAccent", theme).apply()
        _themeAccentSetting.value = theme
    }

    fun toggleDownloadArtistCoverSetting(){
        preferences.edit().putBoolean("DownloadArtistCoverSetting", !downloadArtistCoverSetting.value).apply()
        _downloadArtistCoverSetting.value = !_downloadArtistCoverSetting.value
    }

    fun toggleDownloadOverDataSetting(){
        preferences.edit().putBoolean("DownloadOverDataSetting", !downloadOverDataSetting.value).apply()
        _downloadOverDataSetting.value = !_downloadOverDataSetting.value
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

    //------------------------- Select Artist Cover Screen (SACS)----------------------------------//

    private val _onlineCoversSACS: MutableStateFlow<DiscogsResponse?> = MutableStateFlow(null)
    val onlineCoversSACS = _onlineCoversSACS.asStateFlow()
    private var loadingOnlineCovers = false

    var onArtistImageSelected: (bitmapString: String?) -> Unit = {}

    fun loadOnlineCovers(artistName: String){

        if(!loadingOnlineCovers){
            loadingOnlineCovers = true

            if(CheckInternet.isNetworkAvailable(context)){
                getDiscogsRetrofit()
                    .getArtistCover(
                        token = "Discogs token=addIURHUBwvyDlSqWcNqPWkHXUbMgUzNgbpZGZnd",
                        artist = artistName
                    )
                    .enqueue(object : Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {

                            loadingOnlineCovers = false

                            if(response.code() == 200){

                                val data = Gson().fromJson(response.body(), DiscogsResponse::class.java)
                                val filteredCovers = data.copy(results = data.results.filter { !it.cover_image.endsWith(".gif") })

                                _onlineCoversSACS.value = filteredCovers
                            }
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                            loadingOnlineCovers = false
                            println("Error while getting artist cover")
                        }
                    })
            }
        }
    }

    fun resetSACS(){
        _onlineCoversSACS.value = null
        loadingOnlineCovers = false
    }
}