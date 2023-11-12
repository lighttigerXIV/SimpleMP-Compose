package com.lighttigerxiv.simple.mp.compose.backend.settings

import android.app.Application
import android.content.Context
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsVM(application: Application) : AndroidViewModel(application) {

    private val Context.dataStore by preferencesDataStore(name = "settings")

    private val context = application

    private val _settings = MutableStateFlow<Settings?>(null)
    val settings = _settings.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.Main){
            SettingsRepository(application.dataStore).settingsFlow.collect{settings->
                _settings.update { settings }
            }
        }
    }

    fun updateLightTheme(id: String){
        viewModelScope.launch(Dispatchers.Default){
            SettingsRepository(context.dataStore).updateLightTheme(id)
        }
    }

    fun updateDarkTheme(id: String){
        viewModelScope.launch(Dispatchers.Default){
            SettingsRepository(context.dataStore).updateDarkTheme(id)
        }
    }
}