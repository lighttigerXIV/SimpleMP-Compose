package com.lighttigerxiv.simple.mp.compose.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun SheetDraggingBar(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .width(50.dp)
                .height(5.dp)
                .clip(RoundedCornerShape(percent = 100))
                .background(MaterialTheme.colorScheme.primary)
        ) {}
    }
}