package com.lighttigerxiv.simple.mp.compose.services

import android.app.*
import android.appwidget.AppWidgetManager
import android.content.*
import android.graphics.Bitmap
import android.media.*
import android.media.AudioManager.OnAudioFocusChangeListener
import android.media.session.MediaSession
import android.media.session.PlaybackState
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.KeyEvent
import androidx.core.app.NotificationCompat
import com.lighttigerxiv.simple.mp.compose.GetSongs
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.SimpleMPWidget
import com.lighttigerxiv.simple.mp.compose.Song
import com.lighttigerxiv.simple.mp.compose.activities.MainActivity


class SimpleMPService: Service() {

    private val mBinder = LocalBinder()
    private lateinit var notification: Notification
    private lateinit var notificationManager: NotificationManager
    var queueList = ArrayList<Song>()
    var shuffledQueueList = ArrayList<Song>()
    var currentSongPosition: Int = 0
    var isMusicOnRepeat = false
    private lateinit var audioManager: AudioManager
    val mediaPlayer = MediaPlayer()


    //Listeners
    var onSongSelected : (song: Song) -> Unit = {}
    var onSongSelectedForPlayer : (song: Song) -> Unit = {}
    var onSongSelectedForWidget : (song: Song) -> Unit = {}
    var onSongPaused : () -> Unit = {}
    var onSongResumed : () -> Unit = {}
    var onSongSecondPassed : ( currentPosition: Int ) -> Unit = {}
    var onMediaPlayerStopped : () -> Unit = {}


    var currentSong: Song ?= null

    //Player States
    private var serviceStarted = false
    var isMusicShuffled = false
    private var musicStarted = false


    //Others
    private lateinit var mediaButtonReceiver: ComponentName
    private lateinit var mediaSession: MediaSessionCompat


    inner class LocalBinder : Binder() {
        fun getService(): SimpleMPService = this@SimpleMPService
    }



    override fun onBind(intent: Intent?): IBinder {

        val context = this

        mediaButtonReceiver = ComponentName(context, ReceiverPlayPause::class.java)
        mediaSession = MediaSessionCompat(context, "SimpleMPSession")
        mediaSession.setCallback(object : MediaSessionCompat.Callback(){

            override fun onMediaButtonEvent(mediaButtonIntent: Intent): Boolean {

                val ke = mediaButtonIntent.getParcelableExtra<KeyEvent>(Intent.EXTRA_KEY_EVENT)

                if( ke?.action == KeyEvent.ACTION_DOWN ){

                    if( ke.keyCode == KeyEvent.KEYCODE_MEDIA_PREVIOUS )
                        selectPreviousSong( context )

                    if( ke.keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE )
                        pauseResumeMusic( context )

                    if( ke.keyCode == KeyEvent.KEYCODE_MEDIA_PLAY )
                        pauseResumeMusic( context )

                    if( ke.keyCode == KeyEvent.KEYCODE_MEDIA_NEXT )
                        selectNextSong( context )
                }

                return super.onMediaButtonEvent(mediaButtonIntent)
            }

            override fun onPlay() {
                super.onPlay()

                pauseResumeMusic(context)
            }

            override fun onStop() {
                super.onStop()

                stopMediaPlayer()
            }

            override fun onPause() {
                super.onPause()

                pauseResumeMusic(context)
            }

            override fun onSkipToNext() {
                super.onSkipToNext()

                selectNextSong(context)
            }

            override fun onSkipToPrevious() {
                super.onSkipToPrevious()

                selectPreviousSong(context)
            }
        })

        /*
        mediaSession.setFlags(
            MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
        )

         */


        return mBinder
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return START_STICKY
    }


    override fun onCreate() {
        super.onCreate()

        notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }


    fun getCurrentQueueList(): ArrayList<Song>{

        return if(!isMusicShuffled) queueList else shuffledQueueList
    }

    fun getUpNextQueueList(): ArrayList<Song>{

        val currentQueueList = getCurrentQueueList()
        val upNextQueueList = ArrayList<Song>()

        currentQueueList.forEachIndexed{ index, song->
            if(index > currentSongPosition)
                upNextQueueList.add(song)
        }

        return upNextQueueList
    }


    fun isMusicPlayingOrPaused(): Boolean{ return musicStarted }

    fun selectSong( context: Context, newQueueList: ArrayList<Song>, position: Int){

        queueList = newQueueList

        if(isMusicShuffled)
            playAndShuffle(context = context, position = position)

        else{

            currentSongPosition = position
            playSong(context = context)
        }
    }


    fun toggleShuffle(){

        if( !isMusicShuffled ){

            isMusicShuffled = true

            shuffledQueueList = ArrayList()
            val tempShuffledPlaylist = ArrayList<Song>()


            //Adds the current song to first position
            queueList.forEach { song ->

                if (song.path != currentSong!!.path)
                    tempShuffledPlaylist.add(song)

                else
                    shuffledQueueList.add( song )
            }

            //Shuffles the temp playlist and adds it to the one with just the current song
            tempShuffledPlaylist.shuffle()

            for( song in tempShuffledPlaylist )
                shuffledQueueList.add( song )


            currentSongPosition = 0
        }
        else{

            isMusicShuffled = false


            for( i in queueList.indices ){

                if( queueList[i].path == currentSong!!.path ){

                    currentSongPosition = i
                    break
                }
            }
        }
    }

    fun getCurrentMediaPlayerPosition(): Int{ return mediaPlayer.currentPosition }


    fun playAndShuffle(context: Context, position: Int){

        shuffledQueueList = ArrayList()

        val tempShuffledQueueList = ArrayList(queueList)
        val firstSong = queueList[position]

        tempShuffledQueueList.removeAt(position)
        tempShuffledQueueList.shuffle()

        shuffledQueueList.add(firstSong)
        tempShuffledQueueList.forEach { song->
            shuffledQueueList.add(song)
        }

        currentSongPosition = 0
        playSong(context)
    }

    fun shuffleAndPlay( newQueueList: ArrayList<Song>, context: Context){

        queueList = newQueueList

        isMusicShuffled = true

        shuffledQueueList = ArrayList(queueList)
        shuffledQueueList.shuffle()
        currentSongPosition = 0

        playSong(context)
    }


    fun isMusicPlaying(): Boolean{

        return try{mediaPlayer.isPlaying} catch (_: Exception){false}
    }


    private val audioFocusChangeListener = OnAudioFocusChangeListener { focusChange ->
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN -> {}

            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT->{}

            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {

                if( mediaPlayer.isPlaying )
                    pauseMusic(this )
            }

            AudioManager.AUDIOFOCUS_LOSS -> {

                if( mediaPlayer.isPlaying )
                    pauseMusic(this )
            }
        }
    }


    private val focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN).run {
        setAudioAttributes(AudioAttributes.Builder().run {
            setUsage(AudioAttributes.USAGE_MEDIA)
            setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            build()
        })
        setAcceptsDelayedFocusGain(true)
        setOnAudioFocusChangeListener(audioFocusChangeListener)
        build()
    }


    private fun playSong(context: Context){

        serviceStarted = true
        musicStarted = true

        val songTitle: String
        val songArtist: String
        val songID: Long
        val songAlbumID: Long
        val songAlbumArt: Bitmap
        val songDuration: Int


        if( !isMusicShuffled ) {

            songTitle = queueList[currentSongPosition].title
            songArtist = queueList[currentSongPosition].artistName
            songID = queueList[currentSongPosition].id
            songAlbumID = queueList[currentSongPosition].albumID
            songAlbumArt = GetSongs.getSongAlbumArt(context, songID, songAlbumID)
            songDuration = queueList[currentSongPosition].duration
        }
        else{

            songTitle = shuffledQueueList[currentSongPosition].title
            songArtist = shuffledQueueList[currentSongPosition].artistName
            songID = shuffledQueueList[currentSongPosition].id
            songAlbumID = shuffledQueueList[currentSongPosition].albumID
            songAlbumArt = GetSongs.getSongAlbumArt(context, songID, songAlbumID)
            songDuration = shuffledQueueList[currentSongPosition].duration
        }

        currentSong = getCurrentQueueList()[currentSongPosition]

        mediaPlayer.reset()
        mediaPlayer.setDataSource(currentSong!!.path)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {

            audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager


            requestPlayWithFocus()



            //Open App
            val openAppIntent = Intent( context, MainActivity::class.java )
            val pendingOpenAppIntent = TaskStackBuilder.create( context ).run{

                addNextIntentWithParentStack(openAppIntent)
                getPendingIntent( 0, PendingIntent.FLAG_IMMUTABLE )
            }

            //Stop Service
            val stopIntent = Intent(context, ReceiverStop::class.java )
            val pendingStopIntent = PendingIntent.getBroadcast( context, 1, stopIntent, PendingIntent.FLAG_IMMUTABLE )


            //Previous Music
            val previousSongIntent = Intent(context, ReceiverPreviousSong::class.java )
            val pendingPreviousSongIntent = PendingIntent.getBroadcast( context, 1, previousSongIntent, PendingIntent.FLAG_IMMUTABLE )


            //Pauses/Plays music
            val playPauseIntent = Intent(context, ReceiverPlayPause::class.java )
            val pendingPlayPauseIntent = PendingIntent.getBroadcast( context, 1, playPauseIntent, PendingIntent.FLAG_IMMUTABLE )


            //Skips to next music
            val skipSongIntent = Intent(context, ReceiverSkipSong::class.java )
            val pendingSkipSongIntent = PendingIntent.getBroadcast( context, 1, skipSongIntent, PendingIntent.FLAG_IMMUTABLE )



            notification = NotificationCompat.Builder(context, "Playback")
                .setContentIntent( pendingOpenAppIntent )
                .setStyle( androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession.sessionToken)
                    .setShowActionsInCompactView(1, 2, 3)
                )
                .setSmallIcon(R.drawable.icon)
                .addAction(R.drawable.icon_x, "Stop Player", pendingStopIntent )
                .addAction(R.drawable.icon_previous_notification, "Previous Music", pendingPreviousSongIntent )
                .addAction(R.drawable.icon_pause_notification, "Play Pause Music", pendingPlayPauseIntent )
                .addAction(R.drawable.icon_next_notification, "Next Music", pendingSkipSongIntent )
                .build()


            mediaSession.setMetadata(
                MediaMetadataCompat.Builder()

                    .putString(MediaMetadata.METADATA_KEY_TITLE, songTitle)
                    .putString(MediaMetadata.METADATA_KEY_ARTIST, songArtist)
                    .putBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART, songAlbumArt)
                    .putLong(MediaMetadata.METADATA_KEY_DURATION, songDuration.toLong())
                    .build()
            )


            startForeground( 2, notification )
            notificationManager.notify( 2, notification )
        }


        handleSongFinished( context )


        onSongSelected(currentSong!!)
        onSongSelectedForPlayer(currentSong!!)
        onSongSelectedForWidget(currentSong!!)


        val bluetoothReceiver = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
        context.registerReceiver(bluetoothBroadcastReceiver, bluetoothReceiver )


        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post( object : Runnable{
            override fun run() {

                if(isMusicPlaying()){

                    onSongSecondPassed(mediaPlayer.currentPosition)
                    setPlaybackState(PlaybackStateCompat.STATE_PLAYING)
                }
                mainHandler.postDelayed( this,1000)
            }
        })
    }

    fun setPlaybackState(state: Int){

        val stateBuilder = PlaybackStateCompat.Builder()
            .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE
                    or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                    or PlaybackStateCompat.ACTION_SKIP_TO_NEXT)
            .apply {
                setState(state, mediaPlayer.currentPosition.toLong(), 1.0f)
            }

        mediaSession.setPlaybackState(stateBuilder.build())
    }


    private val bluetoothBroadcastReceiver = object : BroadcastReceiver(){

        override fun onReceive(p0: Context?, p1: Intent?) {

            if(isMusicPlaying()) pauseMusic(p0!!)
        }
    }



    fun seekTo( position: Int){

        val newSongPosition = position * 1000

        mediaPlayer.seekTo(newSongPosition)

        if( !mediaPlayer.isPlaying ) mediaPlayer.start()
    }


    private fun handleSongFinished(context: Context) {

        mediaPlayer.setOnCompletionListener{

            //If loop mode is activated
            if( isMusicOnRepeat ){

                playSong( context )
            }

            //Is it's the last song
            else if( (currentSongPosition + 1) == queueList.size ){

                stopMediaPlayer()
            }
            else{

                currentSongPosition++

                playSong( context )
            }
        }
    }


    fun toggleRepeat(){

        isMusicOnRepeat = !isMusicOnRepeat
    }


    fun stopMediaPlayer(){

        onMediaPlayerStopped()
        mediaPlayer.stop()
        musicStarted = false
        currentSongPosition = -1

        setPlaybackState(PlaybackStateCompat.STATE_STOPPED)

        val intent = Intent(application, SimpleMPWidget::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE

        val ids = AppWidgetManager.getInstance(application)
            .getAppWidgetIds(ComponentName(application, SimpleMPWidget::class.java))

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        application.sendBroadcast(intent)

        stopForeground(true)
    }


    fun selectNextSong(context: Context){

        if( (currentSongPosition + 1) < queueList.size ){

            currentSongPosition ++
            playSong( context )
        }
    }


    fun selectPreviousSong(context: Context){

        if( (currentSongPosition - 1) >= 0 ){

            currentSongPosition--
            playSong( context )
        }

    }


    @Suppress("DEPRECATION")
    fun pauseMusic(context: Context ){


        val playPauseIcon = R.drawable.icon_play_notification
        mediaPlayer.pause()
        mediaSession.isActive = false
        onSongPaused()

        setPlaybackState(PlaybackStateCompat.STATE_PAUSED)

        //Updates the notification
        val playPauseIntent = Intent(context, ReceiverPlayPause::class.java )
        playPauseIntent.putExtra( "action", "playPause" )
        val pendingPlayPauseIntent = PendingIntent.getBroadcast( context, 1, playPauseIntent, PendingIntent.FLAG_IMMUTABLE )


        notification.actions[2] = Notification.Action( playPauseIcon, "Play Music", pendingPlayPauseIntent )


        startForeground( 2, notification )
        notificationManager.notify( 2, notification )
    }


    @Suppress("DEPRECATION")
    fun pauseResumeMusic(context: Context ){

        val playPauseIcon: Int

        if( mediaPlayer.isPlaying ) {

            playPauseIcon = R.drawable.icon_play_notification

            mediaPlayer.pause()
            setPlaybackState(PlaybackStateCompat.STATE_PAUSED)
            onSongPaused()
        }
        else {

            playPauseIcon = R.drawable.icon_pause_notification

            requestPlayWithFocus()
            setPlaybackState(PlaybackStateCompat.STATE_PLAYING)
            onSongResumed()
        }


        //Updates the notification
        val playPauseIntent = Intent(context, ReceiverPlayPause::class.java )
        playPauseIntent.putExtra( "action", "playPause" )
        val pendingPlayPauseIntent = PendingIntent.getBroadcast( context, 1, playPauseIntent, PendingIntent.FLAG_IMMUTABLE )


        notification.actions[2] = Notification.Action( playPauseIcon, "Play Music", pendingPlayPauseIntent )


        startForeground( 2, notification )
        notificationManager.notify( 2, notification )
    }


    private fun requestPlayWithFocus(){

        val focusLock = Any()
        val res = audioManager.requestAudioFocus(focusRequest)


        synchronized(focusLock) {
            when (res) {
                AudioManager.AUDIOFOCUS_REQUEST_GRANTED -> {

                    mediaPlayer.start()
                    onSongResumed()

                    mediaSession.isActive = true

                    true
                }
                else -> false
            }
        }
    }
}