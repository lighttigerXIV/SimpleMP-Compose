package com.lighttigerxiv.simple.mp.compose.backend.repositories

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.IOException

class InternalStorageRepository(private val application: Application) {

    suspend fun saveImageToInternalStorage(filename: String, bitmap: Bitmap): Boolean {
        return withContext(Dispatchers.IO) {
            return@withContext try {



                val isExistingFile = application.filesDir.listFiles()?.firstOrNull {
                    it.canRead() && it.isFile && it.nameWithoutExtension == filename
                }?.exists() ?: false

                if (isExistingFile) {
                    deleteImageFromInternalStorage(filename)
                }

                application.openFileOutput(filename, Context.MODE_PRIVATE).use { stream ->
                    val success = bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    if (!success) {
                        throw IOException("Couldn't save Bitmap!")
                    }
                }
                true
            } catch (e: IOException) {
                Log.e("Local Storage Repository", e.toString())
                false
            }
        }
    }

    suspend fun loadImageFromInternalStorage(filename: String) = flow {
        emit(
            application.filesDir.listFiles()?.filter {
                it.canRead() && it.isFile && it.nameWithoutExtension == filename
            }?.map {
                val bytes = it.readBytes()
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            }?.firstOrNull()
        )
    }.flowOn(Dispatchers.IO)

    suspend fun deleteImageFromInternalStorage(filename: String): Boolean {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                application.deleteFile(filename)
            } catch (e: Exception) {
                Log.e("Local Storage Repository", "error deleting image", e)
                false
            }
        }
    }
}