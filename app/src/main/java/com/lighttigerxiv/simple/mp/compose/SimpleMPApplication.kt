package com.lighttigerxiv.simple.mp.compose

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.lighttigerxiv.simple.mp.compose.backend.AppContainer
import com.lighttigerxiv.simple.mp.compose.backend.DefaultAppContainer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

class SimpleMPApplication: Application() {

    private val Context.dataStore by preferencesDataStore(name = "settings")

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this, this.dataStore)
    }
}