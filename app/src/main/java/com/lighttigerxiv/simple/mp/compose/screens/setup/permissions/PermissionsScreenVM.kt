package com.lighttigerxiv.simple.mp.compose.screens.setup.permissions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.lighttigerxiv.simple.mp.compose.functions.Permissions
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
    fun updateStoragePermissionGranted(newValue: Boolean) {
        _storagePermissionGranted.update { newValue }
    }

    private val _notificationsPermissionGranted = MutableStateFlow(false)
    val notificationsPermissionGranted = _notificationsPermissionGranted.asStateFlow()
    fun updateNotificationsPermissionGranted(newValue: Boolean) {
        _notificationsPermissionGranted.update { newValue }
    }


    //************************************************
    // Functions
    //************************************************


    fun loadScreen() {

        _storagePermissionGranted.update { Permissions.hasStoragePermission(context) }
        _notificationsPermissionGranted.update { Permissions.hasNotificationsPermission(context) }

        _screenLoaded.update { true }
    }
}