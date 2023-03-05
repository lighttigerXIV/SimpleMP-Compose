package com.lighttigerxiv.simple.mp.compose.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun SmallIcon(
    id: Int,
    description: String? = null,
    color: Color = MaterialTheme.colorScheme.primary
) {

    Image(
        modifier = Modifier
            .height(20.dp)
            .width(20.dp),
        contentScale = ContentScale.Fit,
        painter = painterResource(id = id),
        contentDescription = description,
        colorFilter = ColorFilter.tint(color)
    )
}

@Composable
fun ClickableSmallIcon(
    id: Int,
    description: String? = null,
    color: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit
) {

    Image(
        modifier = Modifier
            .height(20.dp)
            .width(20.dp)
            .clickable {
                onClick()
            },
        contentScale = ContentScale.Fit,
        painter = painterResource(id = id),
        contentDescription = description,
        colorFilter = ColorFilter.tint(color)
    )
}

@Composable
fun MediumIcon(
    id: Int,
    description: String? = null,
    color: Color = MaterialTheme.colorScheme.primary
) {

    Image(
        modifier = Modifier
            .height(30.dp)
            .width(30.dp),
        contentScale = ContentScale.Fit,
        painter = painterResource(id = id),
        contentDescription = description,
        colorFilter = ColorFilter.tint(color)
    )
}

@Composable
fun ClickableMediumIcon(
    id: Int,
    description: String? = null,
    color: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit
) {

    Image(
        modifier = Modifier
            .height(30.dp)
            .width(30.dp)
            .clickable {
                onClick()
            },
        contentScale = ContentScale.Fit,
        painter = painterResource(id = id),
        contentDescription = description,
        colorFilter = ColorFilter.tint(color)
    )
}