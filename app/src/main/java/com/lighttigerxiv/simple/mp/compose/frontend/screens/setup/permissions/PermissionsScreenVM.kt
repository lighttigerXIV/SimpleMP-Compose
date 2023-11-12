package com.lighttigerxiv.simple.mp.compose.frontend.screens.setup.permissions

import android.app.Application
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.lighttigerxiv.simple.mp.compose.backend.utils.hasNotificationsPermission
import com.lighttigerxiv.simple.mp.compose.backend.utils.hasStoragePermission
import com.lighttigerxiv.simple.mp.compose.backend.utils.isAtLeastAndroid13
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PermissionsScreenVM(application: Application) : AndroidViewModel(application) {

    private val _storagePermissionGranted = MutableStateFlow(hasStoragePermission(getApplication()))
    val storagePermissionGranted = _storagePermissionGranted.asStateFlow()
    fun updateStoragePermissionGranted(v: Boolean){
        _storagePermissionGranted.update { v }
    }

    private val _notificationsPermissionGranted = MutableStateFlow(hasNotificationsPermission(getApplication()))
    val notificationsPermissionGranted = _notificationsPermissionGranted.asStateFlow()
    fun updateNotificationsPermissionGranted(v: Boolean){
        _notificationsPermissionGranted.update { v }
    }


    fun hasAllPermissions(): Boolean {
        return if (isAtLeastAndroid13())
            storagePermissionGranted.value && notificationsPermissionGranted.value
        else
            storagePermissionGranted.value
    }
}