package com.lighttigerxiv.simple.mp.compose.ui.composables.spacers

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lighttigerxiv.simple.mp.compose.data.variables.MEDIUM_SPACING

@Composable
fun MediumHeightSpacer(){
    Spacer(modifier = Modifier.height(MEDIUM_SPACING))
}