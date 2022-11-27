package com.lighttigerxiv.simple.mp.compose.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.UsefulFunctions

@Composable
fun BasicToolbar(
    backText: String,
    onBackClick : ()-> Unit
) {

    val context = LocalContext.current

    Row(modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)) {

        androidx.compose.material3.Button(
            onClick = {onBackClick()},
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            contentPadding = PaddingValues(0.dp)
        ) {

            Image(
                bitmap = remember { UsefulFunctions.getBitmapFromVectorDrawable(context, R.drawable.icon_back_solid).asImageBitmap() },
                contentDescription = "",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .height(25.dp)
                    .width(25.dp)
            )
            Text(
                text = backText,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}