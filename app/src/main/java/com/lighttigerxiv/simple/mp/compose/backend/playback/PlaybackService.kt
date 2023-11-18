package com.lighttigerxiv.simple.mp.compose.backend.playback

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaMetadata
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import androidx.annotation.OptIn
import androidx.core.app.NotificationCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaStyleNotificationHelper
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.SimpleMPApplication
import com.lighttigerxiv.simple.mp.compose.backend.repositories.PlaybackRepository
import com.lighttigerxiv.simple.mp.compose.frontend.activities.main.MainActivity

class PlaybackService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val app = this.application as SimpleMPApplication
        val playbackRepository = app.container.playbackRepository

        when (intent?.action) {
            Actions.NOTIFY -> {
                start(playbackRepository)
            }

            Actions.PREVIOUS -> {
                playbackRepository.previous()
            }

            Actions.PAUSE_RESUME -> {
                playbackRepository.pauseOrResume()
            }

            Actions.SKIP -> {
                playbackRepository.skip()
            }

            Actions.STOP -> {
                stopForeground(STOP_FOREGROUND_REMOVE)
                playbackRepository.player.stop()
                playbackRepository.player.release()
                playbackRepository.resetPlayback()
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    object Actions {
        const val NOTIFY = "start"
        const val PREVIOUS = "previous"
        const val PAUSE_RESUME = "pause_resume"
        const val SKIP = "skip"
        const val STOP = "stop"
    }

    @OptIn(UnstableApi::class)
    private fun start(playbackRepository: PlaybackRepository) {

        createNotificationChannel()

        if (playbackRepository.currentSong.value != null && playbackRepository.currentSongArt.value != null) {

            val openAppIntent = Intent(this, MainActivity::class.java)
            val pendingOpenAppIntent = TaskStackBuilder.create(this).run {
                addNextIntentWithParentStack(openAppIntent)
                getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
            }

            val stopIntent = Intent(this, StopPlaybackReceiver::class.java)
            val pendingStopIntent = PendingIntent.getBroadcast(this, 1, stopIntent, PendingIntent.FLAG_IMMUTABLE)

            val previousIntent = Intent(this, PreviousPlaybackReceiver::class.java)
            val pendingPreviousIntent = PendingIntent.getBroadcast(application, 1, previousIntent, PendingIntent.FLAG_IMMUTABLE)

            val pauseResumeIntent = Intent(this, PauseResumePlaybackReceiver::class.java)
            val pendingPauseResumeIntent = PendingIntent.getBroadcast(this, 1, pauseResumeIntent, PendingIntent.FLAG_IMMUTABLE)

            val skipIntent = Intent(this, SkipPlaybackReceiver::class.java)
            val pendingSkipIntent = PendingIntent.getBroadcast(this, 1, skipIntent, PendingIntent.FLAG_IMMUTABLE)


            val notification = NotificationCompat.Builder(application, "Playback")
                .setContentIntent(pendingOpenAppIntent)
                .setStyle(
                    androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(playbackRepository.session.sessionToken)
                        .setShowCancelButton(true)
                        .setCancelButtonIntent(pendingStopIntent)
                )
                .setSmallIcon(R.drawable.play_empty)
                .setLargeIcon(playbackRepository.currentSongArt.value)
                .setContentTitle(playbackRepository.currentSong.value?.name ?: "n/a")
                .setContentText(playbackRepository.currentSongArtistName.value)
                .addAction(R.drawable.close, "Stop Player", pendingStopIntent)
                .addAction(R.drawable.previous_notification, "Previous Song", pendingPreviousIntent)
                .addAction(if (playbackRepository.player.isPlaying) R.drawable.pause_notification else R.drawable.play_notification, "Pause/Resume song", pendingPauseResumeIntent)
                .addAction(R.drawable.next_notification, "Skip Song", pendingSkipIntent)
                .build()

            playbackRepository.session.setMetadata(
                MediaMetadataCompat.Builder()
                    .putString(MediaMetadata.METADATA_KEY_TITLE, playbackRepository.currentSong.value?.name)
                    .putString(MediaMetadata.METADATA_KEY_ARTIST, playbackRepository.currentSongArtistName.value)
                    .putString(MediaMetadata.METADATA_KEY_ALBUM_ARTIST, playbackRepository.currentSongArtistName.value)
                    .putString(MediaMetadata.METADATA_KEY_ALBUM, playbackRepository.currentSongAlbumName.value)
                    .putBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART, playbackRepository.currentSongArt.value ?: BitmapFactory.decodeResource(application.resources, R.drawable.album))
                    .putBitmap(MediaMetadata.METADATA_KEY_ART, playbackRepository.currentSongArt.value ?: BitmapFactory.decodeResource(application.resources, R.drawable.album))
                    .putLong(MediaMetadata.METADATA_KEY_DURATION, playbackRepository.currentSong.value?.duration?.toLong() ?: 0L)
                    .build()
            )

            val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            startForeground(2, notification)
            notificationManager.notify(2, notification)
        }
    }

    private fun createNotificationChannel() {
        val playbackChannel = NotificationChannel(
            "Playback",
            "Playback",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Simple MP Playback"
        }

        val syncingSongsChannel = NotificationChannel(
            "Sync",
            "Sync",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Syncing Notifications"
        }

        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(playbackChannel)
        notificationManager.createNotificationChannel(syncingSongsChannel)
    }
}