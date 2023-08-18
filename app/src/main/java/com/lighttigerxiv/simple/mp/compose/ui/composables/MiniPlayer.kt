package com.lighttigerxiv.simple.mp.compose.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.data.variables.SMALL_SPACING
import com.lighttigerxiv.simple.mp.compose.activities.main.MainVM
import com.lighttigerxiv.simple.mp.compose.data.variables.ImageSizes
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.SmallHorizontalSpacer
import com.lighttigerxiv.simple.mp.compose.functions.getImage
import com.lighttigerxiv.simple.mp.compose.screens.main.playlists.playlist.modifyIf

@Composable
fun MiniPlayer(
    mainVM: MainVM,
    onClick: () -> Unit
) {

    val context = LocalContext.current
    val song = mainVM.currentSong.collectAsState().value
    val art = mainVM.currentSongAlbumArt.collectAsState().value
    val musicIsPlaying = mainVM.musicPlayling.collectAsState().value
    val surfaceColor = mainVM.surfaceColor.collectAsState().value
    val playPauseIcon = if (musicIsPlaying) {
        remember { getImage(context, R.drawable.icon_pause_solid, ImageSizes.SMALL) }
    } else {
        remember { getImage(context, R.drawable.icon_play_solid, ImageSizes.SMALL) }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(SMALL_SPACING)
            .clickable { onClick() }
    ) {

        if (song != null) {

            val artistName = mainVM.getSongArtist(song).name

            Image(
                bitmap = (art ?: getImage(context, R.drawable.cd, ImageSizes.SMALL)).asImageBitmap(),
                colorFilter = if (art == null) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null,
                contentDescription = "Song Album Art",
                modifier = Modifier
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(7.dp))
                    .modifyIf(art == null) {
                        background(surfaceColor)
                    }
                    .modifyIf(art == null) {
                        padding(5.dp)
                    }
            )

            SmallHorizontalSpacer()

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f, fill = true),
                verticalArrangement = Arrangement.Top
            ) {

                CustomText(
                    text = song.title,
                    weight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                CustomText(
                    text = artistName
                )
            }

            SmallHorizontalSpacer()

            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {

                Image(
                    modifier = Modifier
                        .height(20.dp)
                        .width(20.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .clickable {
                            mainVM.pauseResumeMusic()
                        },
                    bitmap = playPauseIcon.asImageBitmap(),
                    contentDescription = "Play/Pause button",
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                )
            }

            SmallHorizontalSpacer()
        }
    }
}