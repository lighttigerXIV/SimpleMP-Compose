package com.lighttigerxiv.simple.mp.compose.screens.setup.themes

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ThemesScreenVM(application: Application) : AndroidViewModel(application) {


    //************************************************
    // Variables
    //************************************************


    val context = application

    private val preferences = application.getSharedPreferences(application.packageName, Context.MODE_PRIVATE)

    private val _selectedTheme = MutableStateFlow("")
    val selectedTheme = _selectedTheme.asStateFlow()
    fun updateSelectedTheme(newValue:String) {
        _selectedTheme.update { newValue }
    }


    //************************************************
    // Functions
    //************************************************
}