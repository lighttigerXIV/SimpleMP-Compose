package com.lighttigerxiv.simple.mp.compose.composables.spacers

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lighttigerxiv.simple.mp.compose.MEDIUM_SPACING
import com.lighttigerxiv.simple.mp.compose.XSMALL_SPACING

@Composable
fun XSmallWidthSpacer(){
    Spacer(modifier = Modifier.width(XSMALL_SPACING))
}