package com.lighttigerxiv.simple.mp.compose.frontend.utils

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun ChangeStatusBarColor(color: Color){
    val view = LocalView.current
    val useLightBar = color.luminance() > 0.5

    SideEffect {
        val window = (view.context as Activity).window
        window.statusBarColor = color.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = useLightBar
    }
}

@Composable
fun ChangeNavigationBarsColor(color: Color){
    val view = LocalView.current
    val useLightBar = color.luminance() > 0.5

    SideEffect {
        val window = (view.context as Activity).window
        window.navigationBarColor = color.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = useLightBar
    }
}