package com.lighttigerxiv.simple.mp.compose.frontend.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.lighttigerxiv.simple.mp.compose.frontend.utils.FontSizes
import com.lighttigerxiv.simple.mp.compose.frontend.utils.Sizes

@Composable
fun SettingsSwitch(
    checked: Boolean,
    iconId: Int,
    text: String,
    enabled: Boolean = true,
    onCheckedChange: (checked: Boolean) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { if(enabled) onCheckedChange(!checked) }
            .padding(Sizes.LARGE),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(id = iconId),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        HSpacer(size = Sizes.LARGE)

        Text(
            modifier = Modifier.fillMaxWidth().weight(1f, fill = true),
            text = text,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )

        HSpacer(size = Sizes.SMALL)

        Switch(
            checked = checked,
            onCheckedChange = { onCheckedChange(it) },
            enabled = enabled
        )
    }
}