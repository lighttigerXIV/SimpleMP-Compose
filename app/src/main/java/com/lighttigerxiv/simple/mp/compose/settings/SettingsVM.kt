package com.lighttigerxiv.simple.mp.compose.settings

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import com.lighttigerxiv.simple.mp.compose.data.variables.Settings
import com.lighttigerxiv.simple.mp.compose.data.variables.Settings.Values
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsVM(application: Application) : AndroidViewModel(application){

    //************************************************
    // Variables
    //************************************************

    val preferences: SharedPreferences = application.getSharedPreferences(application.packageName, Context.MODE_PRIVATE)

    private val _colorScheme = MutableStateFlow(preferences.getString(Settings.COLOR_SCHEME, Values.ColorScheme.SYSTEM)!!)
    val colorSchemeSetting = _colorScheme.asStateFlow()
    fun updateColorSchemeSetting(newValue:String) {

        preferences.edit().putString(Settings.COLOR_SCHEME, newValue).apply()
        _colorScheme.update { newValue }
    }

    private val _darkModeSetting = MutableStateFlow(preferences.getString(Settings.DARK_MODE_TYPE, Values.DarkMode.COLOR)!!)
    val darkModeSetting = _darkModeSetting.asStateFlow()
    fun updateDarkModeSetting(newValue:String) {

        preferences.edit().putString(Settings.DARK_MODE_TYPE, newValue).apply()
        _darkModeSetting.update { newValue }
    }

    private val _filterAudioSetting = MutableStateFlow(preferences.getString(Settings.FILTER_AUDIO, "60")!!)
    val filterAudioSetting = _filterAudioSetting.asStateFlow()
    fun updateFilterAudioSetting(newValue:String) {

        preferences.edit().putString(Settings.FILTER_AUDIO, newValue).apply()
        _filterAudioSetting.update { newValue }
    }

    private val _darkColorSchemeSetting = MutableStateFlow(preferences.getString(Settings.DARK_COLOR_SCHEME, Values.ColorSchemes.BLUE)!!)
    val darkColorSchemeSetting = _darkColorSchemeSetting.asStateFlow()
    fun updateDarkColorSchemeSetting(newValue:String) {

        preferences.edit().putString(Settings.DARK_COLOR_SCHEME, newValue).apply()
        _darkColorSchemeSetting.update { newValue }
    }

    private val _lightColorSchemeSetting = MutableStateFlow(preferences.getString(Settings.LIGHT_COLOR_SCHEME, Values.ColorSchemes.MATERIAL_YOU)!!)
    val lightColorSchemeSetting = _lightColorSchemeSetting.asStateFlow()
    fun updateLightColorSchemeSetting(newValue:String) {

        preferences.edit().putString(Settings.LIGHT_COLOR_SCHEME, newValue).apply()
        _lightColorSchemeSetting.update { newValue }
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

    private val _carPlayerSetting = MutableStateFlow(preferences.getBoolean(Settings.CAR_PLAYER, false))
    val carPlayerSetting = _carPlayerSetting.asStateFlow()
    fun updateCarPlayerSetting(newValue:Boolean) {
        preferences.edit().putBoolean(Settings.CAR_PLAYER, newValue).apply()
        _carPlayerSetting.update { newValue }
    }

    private val _keepScreenOnInCarModeSetting = MutableStateFlow(preferences.getBoolean(Settings.KEEP_SCREEN_ON_IN_CAR_MODE, true))
    val keepScreenOnInCarModeSetting = _keepScreenOnInCarModeSetting.asStateFlow()
    fun updateKeepScreenOnCarModeSetting(newValue:Boolean) {
        preferences.edit().putBoolean(Settings.KEEP_SCREEN_ON_IN_CAR_MODE, newValue).apply()
        _keepScreenOnInCarModeSetting.update { newValue }
    }
}