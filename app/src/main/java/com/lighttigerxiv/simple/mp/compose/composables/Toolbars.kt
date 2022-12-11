package com.lighttigerxiv.simple.mp.compose.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.getBitmapFromVectorDrawable

@Composable
fun CustomToolbar(
    backText: String,
    onBackClick: ()-> Unit,
    secondaryContent: @Composable (RowScope.() -> Unit)? = null
) {

    val context = LocalContext.current

    Row(modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)) {

        Row(
            modifier = Modifier
                .wrapContentWidth()
                .clip(RoundedCornerShape(percent = 30))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable { onBackClick() }
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                bitmap = remember { getBitmapFromVectorDrawable(context, R.drawable.icon_back_solid).asImageBitmap() },
                contentDescription = "",
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .height(14.dp)
                    .width(14.dp)
            )

            Text(
                text = backText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        if(secondaryContent != null) secondaryContent()
    }
}