package com.lighttigerxiv.simple.mp.compose.backend.utils

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.lighttigerxiv.simple.mp.compose.SimpleMPApplication

class BackgroundSyncWorker(private val appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {

        val app = (appContext.applicationContext as SimpleMPApplication)
        val libraryRepository = app.container.libraryRepository;

        if(!libraryRepository.indexingLibrary.value){
            libraryRepository.initLibrary();
            libraryRepository.indexLibrary(app);
        }

        return Result.success()
    }
}