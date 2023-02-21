package com.lighttigerxiv.simple.mp.compose.composables

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.app_viewmodels.MainVM

@Composable
fun MiniPlayer(
    mainVM: MainVM
) {

    val context = LocalContext.current
    val songTitle = mainVM.selectedSong.value!!.title
    val songArtistName = mainVM.selectedSong.value!!.artist
    val songAlbumArt = mainVM.selectedSongAlbumArt.value
    val isMusicPlaying = mainVM.isMusicPlaying.collectAsState().value
    val currentPlayerIcon = remember{ mutableStateOf(if (isMusicPlaying)
        R.drawable.icon_pause_solid
    else
        R.drawable.icon_play_solid) }

    LaunchedEffect(isMusicPlaying){

        currentPlayerIcon.value =
            if (isMusicPlaying)
                R.drawable.icon_pause_solid
            else
                R.drawable.icon_play_solid
    }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(4.dp)
    ) {

        Image(
            bitmap = (songAlbumArt ?: BitmapFactory.decodeResource(context.resources, R.drawable.icon_music_record)).asImageBitmap(),
            colorFilter = if(songAlbumArt == null) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null,
            contentDescription = "",
            modifier = Modifier
                .fillMaxHeight()
                .clip(RoundedCornerShape(14.dp))
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f, fill = true),
            verticalArrangement = Arrangement.Top
        ) {

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
        Column(
            modifier = Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {

            Icon(
                modifier = Modifier
                    .height(20.dp)
                    .aspectRatio(1f)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        mainVM.pauseResumeMusic()
                    },
                painter = painterResource(id = currentPlayerIcon.value),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.width(20.dp))
    }
}