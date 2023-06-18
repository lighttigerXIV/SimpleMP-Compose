package com.lighttigerxiv.simple.mp.compose.settings

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.lighttigerxiv.simple.mp.compose.data.variables.Settings
import com.lighttigerxiv.simple.mp.compose.data.variables.SettingsValues
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsVM(application: Application) : AndroidViewModel(application){

    //************************************************
    // Variables
    //************************************************

    private val preferences = application.getSharedPreferences(application.packageName, Context.MODE_PRIVATE)

    private val _themeModeSetting = MutableStateFlow(preferences.getString(Settings.THEME_MODE, SettingsValues.ThemeMode.SYSTEM)!!)
    val themeModeSetting = _themeModeSetting.asStateFlow()
    fun updateThemeModeSetting(newValue:String) {

        preferences.edit().putString(Settings.THEME_MODE, newValue).apply()
        _themeModeSetting.update { newValue }
    }

    private val _darkModeSetting = MutableStateFlow(preferences.getString(Settings.DARK_MODE, SettingsValues.DarkMode.COLOR)!!)
    val darkModeSetting = _darkModeSetting.asStateFlow()
    fun updateDarkModeSetting(newValue:String) {

        preferences.edit().putString(Settings.DARK_MODE, newValue).apply()
        _darkModeSetting.update { newValue }
    }

    private val _filterAudioSetting = MutableStateFlow(preferences.getString(Settings.FILTER_AUDIO, "60")!!)
    val filterAudioSetting = _filterAudioSetting.asStateFlow()
    fun updateFilterAudioSetting(newValue:String) {

        preferences.edit().putString(Settings.FILTER_AUDIO, newValue).apply()
        _filterAudioSetting.update { newValue }
    }

    private val _themeAccentSetting = MutableStateFlow(preferences.getString(Settings.THEME_ACCENT, SettingsValues.Themes.DEFAULT)!!)
    val themeAccentSetting = _themeAccentSetting.asStateFlow()
    fun updateThemeAccentSetting(newValue:String) {

        preferences.edit().putString(Settings.THEME_ACCENT, newValue).apply()
        _themeAccentSetting.update { newValue }
    }

    private val _downloadArtistCoverSetting = MutableStateFlow(preferences.getBoolean(Settings.DOWNLOAD_COVER, false))
    val downloadArtistCoverSetting = _downloadArtistCoverSetting.asStateFlow()
    fun updateDownloadArtistCoverSetting(newValue:Boolean) {

        preferences.edit().putBoolean(Settings.DOWNLOAD_COVER, newValue).apply()
        _downloadArtistCoverSetting.update { newValue }
    }

    private val _downloadOverDataSetting = MutableStateFlow(preferences.getBoolean(Settings.DOWNLOAD_OVER_DATA, false))
    val downloadOverDataSetting = _downloadOverDataSetting.asStateFlow()
    fun updateDownloadOverDataSetting(newValue:Boolean) {

        preferences.edit().putBoolean(Settings.DOWNLOAD_OVER_DATA, newValue).apply()
        _downloadOverDataSetting.update { newValue }
    }
}