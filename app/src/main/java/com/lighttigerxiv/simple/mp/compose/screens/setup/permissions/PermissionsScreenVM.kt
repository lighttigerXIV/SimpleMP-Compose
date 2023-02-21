package com.lighttigerxiv.simple.mp.compose.screens.setup.permissions

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PermissionsScreenVM(application: Application) : AndroidViewModel(application) {


    //************************************************
    // Variables
    //************************************************


    val context = application

    private val _screenLoaded = MutableStateFlow(false)
    val screenLoaded = _screenLoaded.asStateFlow()

    private val _storagePermissionGranted = MutableStateFlow(false)
    val storagePermissionGranted = _storagePermissionGranted.asStateFlow()
    fun updateStoragePermissionGranted(newValue:Boolean) {
        _storagePermissionGranted.update { newValue }
    }

    private val _notificationsPermissionGranted = MutableStateFlow(false)
    val notificationsPermissionGranted = _notificationsPermissionGranted.asStateFlow()
    fun updateNotificationsPermissionGranted(newValue:Boolean) {
        _notificationsPermissionGranted.update { newValue }
    }


    //************************************************
    // Functions
    //************************************************


    fun loadScreen(){

        if(Build.VERSION.SDK_INT >= 33){
            _storagePermissionGranted.update { ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED }
            _notificationsPermissionGranted.update { ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED }
        }
        else{
            _storagePermissionGranted.update { ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED }
        }

        _screenLoaded.update { true }
    }
}