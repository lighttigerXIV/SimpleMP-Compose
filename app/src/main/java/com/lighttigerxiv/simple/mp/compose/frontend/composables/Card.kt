package com.lighttigerxiv.simple.mp.compose.frontend.composables

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.lighttigerxiv.simple.mp.compose.frontend.utils.Sizes
import com.lighttigerxiv.simple.mp.compose.frontend.utils.modifyIf

@Composable
fun Card(
    image: Bitmap? = null,
    defaultIconId: Int,
    text: String,
    onClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Sizes.XLARGE))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onClick() }
            .padding(Sizes.SMALL),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(Sizes.MEDIUM))
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {

            if (image != null) {
                Image(
                    modifier = Modifier
                        .fillMaxSize(),
                    bitmap = image.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Sizes.LARGE),
                    painter = painterResource(id = defaultIconId),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        VSpacer(size = Sizes.SMALL)

        Text(
            text = text,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun PermissionCard(
    checked: Boolean,
    iconId: Int,
    description: String,
    onCheckedChange: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .modifyIf(!checked) {
                clickable { onCheckedChange() }
            }
            .padding(Sizes.LARGE),
        verticalAlignment = Alignment.CenterVertically
    ) {

        androidx.compose.material.Icon(
            modifier = Modifier.size(30.dp),
            painter = painterResource(id = iconId),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        HSpacer(size = Sizes.LARGE)

        androidx.compose.material.Text(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = true),
            text = description,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )

        HSpacer(size = Sizes.SMALL)

        Switch(checked = checked, onCheckedChange = { if (!checked) onCheckedChange() })
    }
}
