package com.lighttigerxiv.simple.mp.compose.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class ActivitySettingsViewModel(application: Application) : AndroidViewModel(application){

    private val preferences = application.getSharedPreferences(application.packageName, Context.MODE_PRIVATE)
    val themeModeSetting = MutableLiveData(preferences.getString("ThemeMode", "System"))
    val darkModeSetting = MutableLiveData(preferences.getString("DarkMode", "Color"))
    val filterAudioSetting = MutableLiveData(preferences.getString("FilterAudio", "60"))
    val themeAccentSetting = MutableLiveData(preferences.getString("ThemeAccent", "Default"))


    val selectedThemeModelDialog = MutableLiveData(themeModeSetting.value)
    val selectedDarkModeDialog = MutableLiveData(darkModeSetting.value)
    val etFilterAudioDialog = MutableLiveData(filterAudioSetting.value)
    val selectedThemeAccentDialog = MutableLiveData(themeAccentSetting.value)


    val showRestartSnack = MutableLiveData(false)


    fun setThemeMode(){

        preferences.edit().putString("ThemeMode", selectedThemeModelDialog.value).apply()
        themeModeSetting.value = selectedThemeModelDialog.value

        showRestartSnack.value = true
    }

    fun setDarkMode(){

        preferences.edit().putString("DarkMode", selectedDarkModeDialog.value).apply()
        darkModeSetting.value = selectedDarkModeDialog.value

        showRestartSnack.value = true
    }


    fun setFilterAudio(){

        preferences.edit().putString("FilterAudio", etFilterAudioDialog.value).apply()
        filterAudioSetting.value = etFilterAudioDialog.value

        showRestartSnack.value = true
    }

    fun setThemeAccent(){

        preferences.edit().putString("ThemeAccent", selectedThemeAccentDialog.value).apply()
        themeAccentSetting.value = selectedThemeAccentDialog.value

        showRestartSnack.value = true
    }
}