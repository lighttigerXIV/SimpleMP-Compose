package com.lighttigerxiv.simple.mp.compose.app_viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsVM(application: Application) : AndroidViewModel(application){


    //************************************************
    // Variables
    //************************************************


    val context = application

    private val preferences = context.getSharedPreferences(application.packageName, Context.MODE_PRIVATE)

    private val _themeModeSetting = MutableStateFlow(preferences.getString("ThemeMode", "System")!!)
    val themeModeSetting = _themeModeSetting.asStateFlow()

    private val _darkModeSetting = MutableStateFlow(preferences.getString("DarkMode", "Color")!!)
    val darkModeSetting = _darkModeSetting.asStateFlow()

    private val _filterAudioSetting = MutableStateFlow(preferences.getString("FilterAudio", "60")!!)
    val filterAudioSetting = _filterAudioSetting.asStateFlow()

    private val _themeAccentSetting = MutableStateFlow(preferences.getString("ThemeAccent", "Default")!!)
    val themeAccentSetting = _themeAccentSetting.asStateFlow()

    private val _downloadArtistCoverSetting = MutableStateFlow(preferences.getBoolean("DownloadArtistCoverSetting", true))
    val downloadArtistCoverSetting = _downloadArtistCoverSetting.asStateFlow()

    private val _downloadOverDataSetting = MutableStateFlow(preferences.getBoolean("DownloadOverDataSetting", false))
    val downloadOverDataSetting = _downloadOverDataSetting.asStateFlow()
}