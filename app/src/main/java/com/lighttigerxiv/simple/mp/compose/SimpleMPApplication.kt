package com.lighttigerxiv.simple.mp.compose

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.lighttigerxiv.simple.mp.compose.backend.AppContainer
import com.lighttigerxiv.simple.mp.compose.backend.DefaultAppContainer
import com.lighttigerxiv.simple.mp.compose.backend.utils.BackgroundSyncWorker

class SimpleMPApplication : Application() {

    private val Context.dataStore by preferencesDataStore(name = "settings")

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this, this.dataStore)

        val request: OneTimeWorkRequest = OneTimeWorkRequest.from(BackgroundSyncWorker::class.java)
        WorkManager.getInstance(this).enqueueUniqueWork("indexing", ExistingWorkPolicy.KEEP, request)
    }
}