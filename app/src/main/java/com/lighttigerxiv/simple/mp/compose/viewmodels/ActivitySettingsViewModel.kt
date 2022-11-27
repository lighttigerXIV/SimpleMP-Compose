package com.lighttigerxiv.simple.mp.compose.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class ActivitySettingsViewModel(application: Application) : AndroidViewModel(application){

    private val preferences = application.getSharedPreferences(application.packageName, Context.MODE_PRIVATE)

}