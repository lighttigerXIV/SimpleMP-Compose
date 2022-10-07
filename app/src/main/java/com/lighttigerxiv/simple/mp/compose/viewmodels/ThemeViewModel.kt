package com.lighttigerxiv.simple.mp.compose.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class ThemeViewModel(application: Application) : AndroidViewModel(application) {

    private val preferences = application.getSharedPreferences(application.packageName, Context.MODE_PRIVATE)
    var currentThemeSetting = MutableLiveData("")
    var darkModeSetting = MutableLiveData("")
    var themeAccentSetting = MutableLiveData("")


    init {

        currentThemeSetting.value = preferences.getString("ThemeMode", "System")
        darkModeSetting.value = preferences.getString("DarkMode", "Color")
        themeAccentSetting.value = preferences.getString("ThemeAccent", "Default")
    }
}