package com.lighttigerxiv.simple.mp.compose.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.lighttigerxiv.simple.mp.compose.viewmodels.ActivityMainViewModel

@Composable
fun MiniPlayer(
    activityMainViewModel: ActivityMainViewModel
){

    val songTitle = activityMainViewModel.selectedSong.value!!.title
    val songArtistName = activityMainViewModel.selectedSong.value!!.artistName
    val songAlbumArt = activityMainViewModel.selectedSongAlbumArt.value!!
    val currentMiniPlayerIcon = activityMainViewModel.currentMiniPlayerIcon.observeAsState()


    Row(modifier = Modifier
        .fillMaxWidth()
        .height(60.dp)
        .background(MaterialTheme.colorScheme.surfaceVariant)
        .padding(4.dp)
    ) {

        Image(
            bitmap = songAlbumArt.asImageBitmap(),
            contentDescription = "",
            modifier = Modifier
                .fillMaxHeight()
                .clip(RoundedCornerShape(14.dp))
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier
            .fillMaxHeight()
            .weight(1f)) {

            Text(
                text = songTitle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.wrapContentHeight()
            )
            Text(
                text = songArtistName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxHeight()
            )
        }
        Image(
            bitmap = currentMiniPlayerIcon.value!!,
            contentDescription = "",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxHeight()
                .width(30.dp)
                .padding(2.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    activityMainViewModel.pauseResumeMusic()
                }
        )
        Spacer(modifier = Modifier.width(10.dp))
    }
}