package com.lighttigerxiv.simple.mp.compose.navigation

import androidx.compose.ui.graphics.ImageBitmap

data class BottomNavItem(
    val name: String,
    val route: String,
    val activeIcon: ImageBitmap,
    val inactiveIcon: ImageBitmap
)
