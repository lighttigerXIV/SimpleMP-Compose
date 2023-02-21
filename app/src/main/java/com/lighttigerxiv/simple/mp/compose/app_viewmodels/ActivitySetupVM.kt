package com.lighttigerxiv.simple.mp.compose.app_viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ActivitySetupVM(application: Application) : AndroidViewModel(application){

    val context = application
    var selectedTheme = MutableLiveData("")

    private val preferences = application.getSharedPreferences(application.packageName, Context.MODE_PRIVATE)
    val themeModeSetting = preferences.getString("ThemeMode", "System")

    private val _themeAccentSetting = MutableStateFlow(preferences.getString("ThemeAccent", "Default")!!)
    val themeAccentSetting = _themeAccentSetting.asStateFlow()

    val darkModeSetting = preferences.getString("DarkMode", "Color")


    fun updateThemeAccent(accent: String){

        preferences.edit().putString("ThemeAccent", accent).apply()
        _themeAccentSetting.update { accent }
    }
}