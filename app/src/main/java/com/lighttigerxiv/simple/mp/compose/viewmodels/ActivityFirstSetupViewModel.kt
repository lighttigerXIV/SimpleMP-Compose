package com.lighttigerxiv.simple.mp.compose.viewmodels

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class ActivityFirstSetupViewModel(application: Application) : AndroidViewModel(application){

    val context = application
    var isStoragePermissionGranted = MutableLiveData(false)
    var isNotificationPermissionGranted = MutableLiveData(false)
    var isButtonFinishEnabled = MutableLiveData(false)
    var selectedTheme = MutableLiveData("")


    init {

        checkPermissions()
    }


    fun checkPermissions(){

        //Storage Permission
        if( Build.VERSION.SDK_INT >= 33 ){

            isStoragePermissionGranted.value = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED
            isNotificationPermissionGranted.value = ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        }
        else{

            isStoragePermissionGranted.value = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }
    }
}