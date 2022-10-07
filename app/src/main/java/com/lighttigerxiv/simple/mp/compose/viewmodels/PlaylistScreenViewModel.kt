package com.lighttigerxiv.simple.mp.compose.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class PlaylistScreenViewModel(application: Application) : AndroidViewModel(application){

    val isOnEditMode = MutableLiveData(false)


}