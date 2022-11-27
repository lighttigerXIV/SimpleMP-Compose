package com.lighttigerxiv.simple.mp.compose.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun CustomText(
    text: String,
    color: Color = MaterialTheme.colorScheme.onSurface,
    size: TextUnit = 16.sp,
    weight: FontWeight = FontWeight.Normal
){

    androidx.compose.material3.Text(
        text = text,
        color = color,
        fontSize = size,
        fontWeight = weight
    )
}