package com.lighttigerxiv.simple.mp.compose

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.lighttigerxiv.simple.mp.compose.app_viewmodels.ThemeViewModel

class ActivityAddToPlaylist : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val themeViewModel = ViewModelProvider(this)[ThemeViewModel::class.java]

        setContent {
        }
    }
}