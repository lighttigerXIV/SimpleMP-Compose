package com.lighttigerxiv.simple.mp.compose.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.functions.getAppString

@Composable
fun PlayAndShuffleRow(
    surfaceColor: Color,
    onPlayClick: () -> Unit,
    onSuffleClick: () -> Unit
){

    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = true)
                .clip(RoundedCornerShape(100))
                .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(100))
                .clickable{
                    onPlayClick()
                }
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                modifier = Modifier
                    .height(15.dp)
                    .width(15.dp),
                painter = painterResource(id = R.drawable.play),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.width(10.dp))
            CustomText(
                text = remember { getAppString(context, R.string.Play) },
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = true)
                .border(1.dp, MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(100))
                .clip(RoundedCornerShape(100))
                .background(surfaceColor, shape = RoundedCornerShape(100))
                .clickable{
                    onSuffleClick()
                }
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                modifier = Modifier
                    .height(15.dp)
                    .width(15.dp),
                painter = painterResource(id = R.drawable.shuffle),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(10.dp))
            CustomText(
                text = remember { getAppString(context, R.string.Shuffle) },
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}