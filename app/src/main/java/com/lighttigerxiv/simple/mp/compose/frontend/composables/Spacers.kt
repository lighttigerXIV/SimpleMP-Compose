package com.lighttigerxiv.simple.mp.compose.frontend.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun VSpacer(size: Dp){
    Box(modifier = Modifier.height(size))
}

@Composable
fun HSpacer(size: Dp){
    Box(modifier = Modifier.width(size))
}

@Composable
fun MiniPlayerSpacer(isShown: Boolean){
    Box(modifier = Modifier.height(if(isShown) 75.dp else 0.dp))
}