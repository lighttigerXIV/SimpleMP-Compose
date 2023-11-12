package com.lighttigerxiv.simple.mp.compose.frontend.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun VDivider(
    height: Dp = 1.dp,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant
){

    Box(
        modifier = Modifier
            .height(height)
            .fillMaxWidth()
            .background(color)
    )
}