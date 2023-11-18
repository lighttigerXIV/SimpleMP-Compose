package com.lighttigerxiv.simple.mp.compose.frontend.activities.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.lighttigerxiv.simple.mp.compose.SimpleMPApplication
import com.lighttigerxiv.simple.mp.compose.backend.repositories.SettingsRepository
import com.lighttigerxiv.simple.mp.compose.backend.settings.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ActivityMainVM(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {
    companion object Factory {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SimpleMPApplication)
                val settingsRepository = application.container.settingsRepository
                ActivityMainVM(settingsRepository)
            }
        }
    }

    private val _settings = MutableStateFlow<Settings?>(null)
    val settings = _settings.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.Default) {
            settingsRepository.settingsFlow.collect { newSettings ->
                _settings.update { newSettings }
            }
        }
    }
}