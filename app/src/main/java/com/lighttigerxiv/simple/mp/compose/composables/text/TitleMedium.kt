package com.lighttigerxiv.simple.mp.compose.composables.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.lighttigerxiv.simple.mp.compose.MEDIUM_TITLE_SIZE

@Composable
fun TitleMedium(
    text: String,
    color: Color = MaterialTheme.colorScheme.onSurface
){

    Text(
        text = text,
        fontSize = MEDIUM_TITLE_SIZE,
        color = color,
        fontWeight = FontWeight(500)
    )
}