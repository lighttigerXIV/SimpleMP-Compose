package com.lighttigerxiv.simple.mp.compose.screens.main.settings

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.activities.main.MainVM
import com.lighttigerxiv.simple.mp.compose.functions.getAppString
import com.lighttigerxiv.simple.mp.compose.settings.SettingsVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsScreenVM(application: Application) : AndroidViewModel(application) {

    //************************************************
    // Variables
    //************************************************

    private val context = application

    private val _screenLoaded = MutableStateFlow(false)
    val screenLoaded = _screenLoaded.asStateFlow()

    private val _showThemeModeDialog = MutableStateFlow(false)
    val showThemeModeDialog = _showThemeModeDialog.asStateFlow()
    fun updateShowThemeModeDialog(newValue: Boolean) {
        _showThemeModeDialog.update { newValue }
    }

    private val _showDarkModeDialog = MutableStateFlow(false)
    val showDarkModeDialog = _showDarkModeDialog.asStateFlow()
    fun updateShowDarkModeDialog(newValue: Boolean) {
        _showDarkModeDialog.update { newValue }
    }

    private val _showFilterAudioDialog = MutableStateFlow(false)
    val showFilterAudioDialog = _showFilterAudioDialog.asStateFlow()
    fun updateShowFilterAudioDialog(newValue: Boolean) {
        _showFilterAudioDialog.update { newValue }
    }


    private val _selectedColorScheme = MutableStateFlow("")
    val selectedColorScheme = _selectedColorScheme.asStateFlow()
    fun updateSelectedThemeMode(newValue: String) {

        _selectedColorScheme.update { newValue }
    }

    private val _selectedDarkMode = MutableStateFlow("")
    val selectedDarkMode = _selectedDarkMode.asStateFlow()
    fun updateSelectedDarkMode(newValue: String) {
        _selectedDarkMode.update { newValue }
    }

    private val _filterAudioText = MutableStateFlow("")
    val filterAudioText = _filterAudioText.asStateFlow()
    fun updateFilterAudioText(newValue: String) {
        _filterAudioText.update { newValue }
    }


    //************************************************
    // Functions
    //************************************************

    fun loadScreen(settingsVM: SettingsVM) {


        _selectedColorScheme.update { settingsVM.colorSchemeSetting.value }

        _selectedDarkMode.update { settingsVM.darkModeSetting.value }

        _filterAudioText.update { settingsVM.filterAudioSetting.value }

        _screenLoaded.update { true }
    }

    fun applyAudioFilterSetting(settingsVM: SettingsVM, mainVM: MainVM) {

        settingsVM.updateFilterAudioSetting(filterAudioText.value)

        Toast.makeText(context, getAppString(context, R.string.IndexingSongs), Toast.LENGTH_LONG).show()

        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                _showFilterAudioDialog.update { false }
                mainVM.indexSongs(showNotification = true)
            }
        }
    }
}