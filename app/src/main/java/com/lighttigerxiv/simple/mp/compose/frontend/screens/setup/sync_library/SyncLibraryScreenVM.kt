package com.lighttigerxiv.simple.mp.compose.frontend.screens.setup.sync_library

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.lighttigerxiv.simple.mp.compose.backend.library.indexLibrary
import com.lighttigerxiv.simple.mp.compose.backend.settings.SettingsVM
import com.lighttigerxiv.simple.mp.compose.backend.viewmodels.AppVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SyncLibraryScreenVM(application: Application) : AndroidViewModel(application){

    private val context = application

    private val _syncingLibrary = MutableStateFlow(false)
    val syncingLibrary = _syncingLibrary.asStateFlow()

    fun syncLibrary(settingsVM: SettingsVM, appVM: AppVM){

        _syncingLibrary.update { true }

        viewModelScope.launch(Dispatchers.Main){
            indexLibrary(context){
                settingsVM.updateSetupCompleted(true)
                appVM.refreshLibrary()
            }
        }
    }
}