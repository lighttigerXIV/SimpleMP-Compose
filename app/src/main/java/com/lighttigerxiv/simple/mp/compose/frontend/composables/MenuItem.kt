package com.lighttigerxiv.simple.mp.compose.frontend.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goToSelectArtistCover
import com.lighttigerxiv.simple.mp.compose.frontend.utils.modifyIf

@Composable
fun MenuItem(
    iconId: Int,
    text: String,
    onClick: () -> Unit,
    disabled: Boolean = false
) {

    DropdownMenuItem(
        modifier = Modifier.modifyIf(disabled) { background(MaterialTheme.colorScheme.surface) },
        enabled = !disabled,
        leadingIcon = {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = iconId),
                contentDescription = null,
                tint = if(disabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        text = {
            Text(
                text = text,
                fontWeight = FontWeight.Medium,
                color = if(disabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        onClick = { onClick() }
    )
}