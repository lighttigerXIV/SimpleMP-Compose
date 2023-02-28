package com.lighttigerxiv.simple.mp.compose.screens.main.settings

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.lighttigerxiv.simple.mp.compose.app_viewmodels.SettingsVM
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsScreenVM(application: Application) : AndroidViewModel(application) {

    //************************************************
    // Variables
    //************************************************

    private val _screenLoaded = MutableStateFlow(false)
    val screenLoaded = _screenLoaded.asStateFlow()

    private val _showThemeModeDialog = MutableStateFlow(false)
    val showThemeModeDialog = _showThemeModeDialog.asStateFlow()
    fun updateShowThemeModeDialog(newValue:Boolean) {
        _showThemeModeDialog.update { newValue }
    }

    private val _showDarkModeDialog = MutableStateFlow(false)
    val showDarkModeDialog = _showDarkModeDialog.asStateFlow()
    fun updateShowDarkModeDialog(newValue:Boolean) {
        _showDarkModeDialog.update { newValue }
    }

    private val _showFilterAudioDialog = MutableStateFlow(false)
    val showFilterAudioDialog = _showFilterAudioDialog.asStateFlow()
    fun updateShowFilterAudioDialog(newValue:Boolean) {
        _showFilterAudioDialog.update { newValue }
    }



    private val _selectedThemeMode = MutableStateFlow("")
    val selectedThemeMode = _selectedThemeMode.asStateFlow()
    fun updateSelectedThemeMode(newValue:String) {
        _selectedThemeMode.update { newValue }
    }

    private val _selectedDarkMode = MutableStateFlow("")
    val selectedDarkMode = _selectedDarkMode.asStateFlow()
    fun updateSelectedDarkMode(newValue:String) {
        _selectedDarkMode.update { newValue }
    }

    private val _filterAudioText = MutableStateFlow("")
    val filterAudioText = _filterAudioText.asStateFlow()
    fun updateFilterAudioText(newValue:String) {
        _filterAudioText.update { newValue }
    }


    //************************************************
    // Functions
    //************************************************

    fun loadScreen(settingsVM: SettingsVM){


        _selectedThemeMode.update { settingsVM.themeModeSetting.value }

        _selectedDarkMode.update { settingsVM.darkModeSetting.value }

        _filterAudioText.update { settingsVM.filterAudioSetting.value }

        _screenLoaded.update { true }
    }
}