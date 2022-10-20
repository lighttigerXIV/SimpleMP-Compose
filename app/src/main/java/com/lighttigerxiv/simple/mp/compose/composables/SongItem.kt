package com.lighttigerxiv.simple.mp.compose.composables

import android.graphics.Bitmap
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.Song

@Composable
fun SongItem(
    song: Song,
    songAlbumArt: Bitmap,
    highlight: Boolean = false,
    popupMenuEntries: ArrayList<String> = ArrayList(),
    onSongClick: () -> Unit = {},
    onMenuClicked: (option: String) -> Unit = {}
) {

    val songTitle = remember { song.title }
    val songArtist = remember { song.artistName }
    val isPopupMenuExpanded = remember { mutableStateOf(false) }

    val titleColor = when (highlight) {
        true -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.onSurface
    }


    val titleWeight = when (highlight){
        true -> FontWeight.Medium
        else -> FontWeight.Normal
    }


    Row(modifier = Modifier
        .fillMaxWidth()
        .height(70.dp)
        .clickable(
            interactionSource = remember{ MutableInteractionSource() },
            indication = null
        ) { onSongClick() }
    ) {

        AsyncImage(
            model = remember{songAlbumArt},
            contentDescription = "",
            modifier = Modifier
                .clip(RoundedCornerShape(20))
                .height(70.dp)
                .width(70.dp)
        )

        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .width(10.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = songTitle,
                    fontSize = 16.sp,
                    color = titleColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = titleWeight
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
            ) {
                Text(
                    text = songArtist,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }


        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxHeight()
                .width(20.dp)
        ) {

            DropdownMenu(
                expanded = isPopupMenuExpanded.value,
                onDismissRequest = { isPopupMenuExpanded.value = false }
            ) {

                if (popupMenuEntries.contains("artist")) {

                    DropdownMenuItem(
                        text = { Text(text = "Go to Artist") },
                        onClick = { onMenuClicked("artist") }
                    )
                }

                if (popupMenuEntries.contains("album")) {

                    DropdownMenuItem(
                        text = { Text(text = "Go to Album") },
                        onClick = { onMenuClicked("album") }
                    )
                }

                if (popupMenuEntries.contains("playlist")) {

                    DropdownMenuItem(
                        text = { Text(text = "Add to Playlist") },
                        onClick = { onMenuClicked("playlist") }
                    )
                }
            }

            if(popupMenuEntries.size > 0){

                Icon(
                    painter = painterResource(id = R.drawable.icon_three_dots_regular),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(20.dp)
                        .clickable { isPopupMenuExpanded.value = true }
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(10.dp))
}

