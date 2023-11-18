package com.lighttigerxiv.simple.mp.compose.backend.playback

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class OpenAppReceiver: BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {

    }
}

class SkipPlaybackReceiver: BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        doServiceAction(PlaybackService.Actions.SKIP, context)
    }
}

class PreviousPlaybackReceiver: BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        doServiceAction(PlaybackService.Actions.PREVIOUS, context)
    }
}

class PauseResumePlaybackReceiver: BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        doServiceAction(PlaybackService.Actions.PAUSE_RESUME, context)
    }
}

class StopPlaybackReceiver: BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        doServiceAction(PlaybackService.Actions.STOP, context)
    }
}

fun doServiceAction(action: String, context: Context?){
    val serviceIntent = Intent(context, PlaybackService::class.java)
    serviceIntent.action = action
    context?.startService(serviceIntent)
}

