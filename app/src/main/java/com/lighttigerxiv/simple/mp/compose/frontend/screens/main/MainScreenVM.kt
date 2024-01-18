package com.lighttigerxiv.simple.mp.compose.frontend.screens.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.lighttigerxiv.simple.mp.compose.backend.utils.hasNotificationsPermission
import com.lighttigerxiv.simple.mp.compose.backend.utils.hasStoragePermission
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainScreenVM(application: Application) : AndroidViewModel(application) {

    val context = application

    data class UiState(
        val hasStoragePermission: Boolean = false,
        val hasNotificationsPermission: Boolean = false
    )

    private val _uiState = MutableStateFlow(
        UiState(
            hasStoragePermission = hasStoragePermission(application),
            hasNotificationsPermission = hasNotificationsPermission(application)
        )
    )
    val uiState = _uiState.asStateFlow()

    fun reloadPermissions() {
        _uiState.update {
            UiState(
                hasStoragePermission = hasStoragePermission(context),
                hasNotificationsPermission = hasNotificationsPermission(context)
            )
        }
    }
}