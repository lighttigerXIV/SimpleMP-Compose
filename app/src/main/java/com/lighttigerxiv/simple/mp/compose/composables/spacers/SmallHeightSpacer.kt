package com.lighttigerxiv.simple.mp.compose.composables.spacers

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lighttigerxiv.simple.mp.compose.SMALL_SPACING

@Composable
fun SmallHeightSpacer(){
    Spacer(modifier = Modifier.height(SMALL_SPACING))
}