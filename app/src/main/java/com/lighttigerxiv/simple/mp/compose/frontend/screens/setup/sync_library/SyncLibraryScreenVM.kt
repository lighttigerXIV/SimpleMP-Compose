package com.lighttigerxiv.simple.mp.compose.frontend.screens.setup.sync_library

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.lighttigerxiv.simple.mp.compose.SimpleMPApplication
import com.lighttigerxiv.simple.mp.compose.backend.repositories.LibraryRepository
import com.lighttigerxiv.simple.mp.compose.backend.repositories.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SyncLibraryScreenVM(
    private val application: Application,
    private val settingsRepository: SettingsRepository,
    private val libraryRepository: LibraryRepository
) :  ViewModel(){

    companion object Factory{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SimpleMPApplication)
                val settingsRepository = application.container.settingsRepository
                val libraryRepository = application.container.libraryRepository

                SyncLibraryScreenVM(application, settingsRepository, libraryRepository)
            }
        }
    }

    init {

        //Indexes The Library
        viewModelScope.launch(Dispatchers.Main){
            delay(1000)
            libraryRepository.indexLibrary(application){
                withContext(Dispatchers.Main){
                    settingsRepository.updateSetupCompleted(true)
                }
            }
        }
    }
}