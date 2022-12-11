package com.lighttigerxiv.simple.mp.compose.viewmodels

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ActivityFirstSetupViewModel(application: Application) : AndroidViewModel(application){

    val context = application
    var isStoragePermissionGranted = MutableLiveData(false)
    var isNotificationPermissionGranted = MutableLiveData(false)
    var selectedTheme = MutableLiveData("")

    private val preferences = application.getSharedPreferences(application.packageName, Context.MODE_PRIVATE)
    val themeModeSetting = preferences.getString("ThemeMode", "System")

    private val _themeAccentSetting = MutableStateFlow(preferences.getString("ThemeAccent", "Default"))
    val themeAccentSetting = _themeAccentSetting.asStateFlow()

    val darkModeSetting = preferences.getString("DarkMode", "Color")


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

    fun updateThemeAccent(accent: String){

        selectedTheme.value = accent
        preferences.edit().putString("ThemeAccent", accent).apply()
        _themeAccentSetting.value = accent
    }
}