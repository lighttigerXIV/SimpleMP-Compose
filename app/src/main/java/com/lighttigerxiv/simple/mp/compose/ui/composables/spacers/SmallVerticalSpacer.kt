package com.lighttigerxiv.simple.mp.compose.ui.composables.spacers

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lighttigerxiv.simple.mp.compose.data.variables.SMALL_SPACING

@Composable
fun SmallVerticalSpacer(){
    Spacer(modifier = Modifier.height(SMALL_SPACING))
}