package com.lighttigerxiv.simple.mp.compose.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.lighttigerxiv.simple.mp.compose.GetSongs
import com.lighttigerxiv.simple.mp.compose.Song
import com.lighttigerxiv.simple.mp.compose.services.SimpleMPService

class ActivityQueueListViewModel(application: Application) : AndroidViewModel(application) {

    //Songs
    var songsImagesList = GetSongs.getAllAlbumsImages(application)
    var upNextQueueList = MutableLiveData<ArrayList<Song>>()
    var currentSong = MutableLiveData<Song>()

    //Service
    @SuppressLint("StaticFieldLeak") private lateinit var smpService : SimpleMPService
    var isServiceBound = MutableLiveData(false)

    private val simpleMPConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

            val binder = service as SimpleMPService.LocalBinder
            smpService = binder.getService()
            isServiceBound.value = true

            currentSong.value = smpService.currentSong
            upNextQueueList.value = smpService.getUpNextQueueList()


            smpService.onSongSelectedForQueue = {song->

                currentSong.value = song
                upNextQueueList.value = smpService.getUpNextQueueList()
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {}
    }

    init{

        val serviceIntent = Intent( application, SimpleMPService::class.java )
        application.bindService( serviceIntent, simpleMPConnection, Context.BIND_AUTO_CREATE )
    }
}