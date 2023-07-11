package com.lighttigerxiv.simple.mp.compose.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.activities.main.MainActivity
import com.lighttigerxiv.simple.mp.compose.functions.getSongAlbumArt
import com.lighttigerxiv.simple.mp.compose.services.SimpleMPService


class SimpleMPWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        println("Action => ${intent?.action}")

        when(intent?.action){

            "openActivity"->{
                val newIntent = Intent(context, MainActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP}
                context!!.applicationContext!!.startActivity(newIntent)
            }

            "previousSong"->{
                getSimpleMPService(
                    context = context!!,
                    onConnect = { smpService ->  smpService.selectPreviousSong(context.applicationContext)}
                )
            }

            "playPause"->{
                getSimpleMPService(
                    context = context!!,
                    onConnect = { smpService ->  smpService.pauseResumeMusic(context.applicationContext)}
                )
            }

            "nextSong"->{
                getSimpleMPService(
                    context = context!!,
                    onConnect = { smpService ->  smpService.selectNextSong(context.applicationContext)}
                )
            }
        }
    }
}



internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {


    val openActivityIntent = Intent(context, SimpleMPWidget::class.java).apply { action = "openActivity" }
    val previousSongIntent = Intent(context, SimpleMPWidget::class.java).apply { action = "previousSong" }
    val playPauseIntent = Intent(context, SimpleMPWidget::class.java).apply { action = "playPause" }
    val nextSongIntent = Intent(context, SimpleMPWidget::class.java).apply { action = "nextSong" }

    val openActivityPendingIntent = PendingIntent.getBroadcast(context, 0 , openActivityIntent, PendingIntent.FLAG_IMMUTABLE)
    val previousSongPendingIntent = PendingIntent.getBroadcast(context, 0 , previousSongIntent, PendingIntent.FLAG_IMMUTABLE)
    val playPausePendingIntent = PendingIntent.getBroadcast(context, 0 , playPauseIntent, PendingIntent.FLAG_IMMUTABLE)
    val nextSongPendingIntent = PendingIntent.getBroadcast(context, 0 , nextSongIntent, PendingIntent.FLAG_IMMUTABLE)


    getSimpleMPService(
        context = context,
        onConnect = { smpService ->

            if(smpService.isMusicPlayingOrPaused()){

                val views = RemoteViews(context.packageName, R.layout.simple_m_p_widget)

                val song = smpService.currentSong!!
                views.setTextViewText(R.id.title_Widget, song.title)
                views.setTextViewText(R.id.artist_Widget, "banana")
                views.setImageViewBitmap(R.id.albumArt_Widget, getSongAlbumArt(context, song.id, song.albumID))

                if(smpService.musicPlaying())
                    views.setImageViewBitmap(R.id.playPauseButton_Widget, ResourcesCompat.getDrawable(context.resources, R.drawable.icon_pause_notification, null)?.toBitmap())
                else
                    views.setImageViewBitmap(R.id.playPauseButton_Widget, ResourcesCompat.getDrawable(context.resources, R.drawable.icon_play_notification, null)?.toBitmap())


                views.setOnClickPendingIntent(R.id.albumArt_Widget, openActivityPendingIntent)
                views.setOnClickPendingIntent(R.id.previousSongButton_Widget, previousSongPendingIntent)
                views.setOnClickPendingIntent(R.id.playPauseButton_Widget, playPausePendingIntent)
                views.setOnClickPendingIntent(R.id.nextSongButton_Widget, nextSongPendingIntent)


                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
            else{

                val views = RemoteViews(context.packageName, R.layout.simple_m_p_disabled_widget)
                views.setOnClickPendingIntent(R.id.noMusicPlaying_Widget, openActivityPendingIntent)
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
    )
}


fun getSimpleMPService(
    context: Context,
    onConnect: (smpService: SimpleMPService) -> Unit ={},
    onDisconnect: () -> Unit = {}
){

    val simpleMPConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

            val binder = service as SimpleMPService.LocalBinder
            val smpService = binder.getService()

            onConnect(smpService)
        }


        override fun onServiceDisconnected(name: ComponentName?) {onDisconnect()}
    }


    val serviceIntent = Intent(context, SimpleMPService::class.java)
    context.applicationContext.bindService(serviceIntent, simpleMPConnection, Context.BIND_AUTO_CREATE)
}