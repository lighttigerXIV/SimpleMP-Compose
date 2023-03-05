package com.lighttigerxiv.simple.mp.compose.settings

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsVM(application: Application) : AndroidViewModel(application){

    //************************************************
    // Variables
    //************************************************

    private val preferences = application.getSharedPreferences(application.packageName, Context.MODE_PRIVATE)

    private val _themeModeSetting = MutableStateFlow(preferences.getString("ThemeMode", "System")!!)
    val themeModeSetting = _themeModeSetting.asStateFlow()
    fun updateThemeModeSetting(newValue:String) {

        preferences.edit().putString("ThemeMode", newValue).apply()

        _themeModeSetting.update { newValue }
    }

    private val _darkModeSetting = MutableStateFlow(preferences.getString("DarkMode", "Color")!!)
    val darkModeSetting = _darkModeSetting.asStateFlow()
    fun updateDarkModeSetting(newValue:String) {

        preferences.edit().putString("DarkMode", newValue).apply()

        _darkModeSetting.update { newValue }
    }

    private val _filterAudioSetting = MutableStateFlow(preferences.getString("FilterAudio", "60")!!)
    val filterAudioSetting = _filterAudioSetting.asStateFlow()
    fun updateFilterAudioSetting(newValue:String) {

        preferences.edit().putString("FilterAudio", newValue).apply()

        _filterAudioSetting.update { newValue }
    }

    private val _themeAccentSetting = MutableStateFlow(preferences.getString("ThemeAccent", "Default")!!)
    val themeAccentSetting = _themeAccentSetting.asStateFlow()
    fun updateThemeAccentSetting(newValue:String) {

        preferences.edit().putString("ThemeAccent", newValue).apply()

        _themeAccentSetting.update { newValue }
    }

    private val _downloadArtistCoverSetting = MutableStateFlow(preferences.getBoolean("DownloadArtistCover", true))
    val downloadArtistCoverSetting = _downloadArtistCoverSetting.asStateFlow()
    fun updateDownloadArtistCoverSetting(newValue:Boolean) {

        preferences.edit().putBoolean("DownloadArtistCover", newValue).apply()

        _downloadArtistCoverSetting.update { newValue }
    }

    private val _downloadOverDataSetting = MutableStateFlow(preferences.getBoolean("DownloadOverData", false))
    val downloadOverDataSetting = _downloadOverDataSetting.asStateFlow()
    fun updateDownloadOverDataSetting(newValue:Boolean) {

        preferences.edit().putBoolean("DownloadOverData", newValue).apply()

        _downloadOverDataSetting.update { newValue }
    }
}