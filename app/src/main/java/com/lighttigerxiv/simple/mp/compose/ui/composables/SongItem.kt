package com.lighttigerxiv.simple.mp.compose.ui.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.activities.main.MainVM
import com.lighttigerxiv.simple.mp.compose.data.data_classes.Song
import com.lighttigerxiv.simple.mp.compose.data.variables.ImageSizes
import com.lighttigerxiv.simple.mp.compose.functions.getImage
import com.lighttigerxiv.simple.mp.compose.screens.main.playlists.playlist.modifyIf
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.SmallHorizontalSpacer
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.XSmallHeightSpacer
import org.burnoutcrew.reorderable.ReorderableLazyListState
import org.burnoutcrew.reorderable.detectReorder



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewSongItem(
    modifier: Modifier = Modifier,
    mainVM: MainVM,
    song: Song,
    menuEntries: ArrayList<String> = ArrayList(),
    onSongClick: () -> Unit = {},
    onMenuClicked: (option: String) -> Unit = {}
) {

    val context = LocalContext.current
    val title = remember { song.title }
    val artistName = remember { mainVM.getSongArtist(song).name }
    val art = remember{ mainVM.getSongArt(song) }
    val highlight = song.path == mainVM.currentSong.collectAsState().value?.path
    val isPopupMenuExpanded = remember { mutableStateOf(false) }
    val surfaceVariantColor = MaterialTheme.colorScheme.surfaceVariant

    val titleColor = when (highlight) {
        true -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.onSurface
    }


    val titleWeight = when (highlight) {
        true -> FontWeight.Medium
        else -> FontWeight.Normal
    }

    Column {
        Row(modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .clip(RoundedCornerShape(14.dp))
            .combinedClickable(
                onClick = { onSongClick() },
                onLongClick = { isPopupMenuExpanded.value = true }
            )

        ) {

            AsyncImage(
                model = remember { art ?: getImage(context, R.drawable.cd, ImageSizes.SMALL) },
                contentDescription = "",
                colorFilter = if (art == null) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null,
                modifier = Modifier
                    .clip(RoundedCornerShape(20))
                    .height(70.dp)
                    .width(70.dp)
                    .modifyIf(art == null) {
                        background(surfaceVariantColor)
                    }
                    .modifyIf(art == null) {
                        padding(5.dp)
                    }
            )

            Spacer(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(10.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                verticalArrangement = Arrangement.Top
            ) {

                Text(
                    text = title,
                    fontSize = 16.sp,
                    color = titleColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = titleWeight
                )

                Text(
                    text = artistName,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
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

                    if (menuEntries.contains("Artist")) {

                        DropdownMenuItem(
                            text = { Text(text = "Go to Artist") },
                            onClick = { onMenuClicked("Artist") }
                        )
                    }

                    if (menuEntries.contains("Album")) {

                        DropdownMenuItem(
                            text = { Text(text = "Go to Album") },
                            onClick = { onMenuClicked("Album") }
                        )
                    }

                    if (menuEntries.contains("Playlist")) {

                        DropdownMenuItem(
                            text = { Text(text = "Add to Playlist") },
                            onClick = { onMenuClicked("Playlist") }
                        )
                    }
                }
            }
        }

        XSmallHeightSpacer()
    }
}


@Composable
fun ReorderableSongItem(
    mainVM: MainVM,
    modifier: Modifier = Modifier,
    song: Song,
    state: ReorderableLazyListState,
    isDragging: Boolean
) {

    val context = LocalContext.current
    val title = remember { song.title }
    val artistName = remember { mainVM.getSongArtist(song).name }
    val surfaceColor = mainVM.surfaceColor.collectAsState().value
    val art = remember{ mainVM.getSongArt(song) }

    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(70.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(if (isDragging) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant)
        ) {

            AsyncImage(
                model = remember { art ?: getImage(context, R.drawable.cd, ImageSizes.SMALL) },
                contentDescription = "",
                colorFilter = if (art == null) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null,
                modifier = Modifier
                    .clip(RoundedCornerShape(20))
                    .height(70.dp)
                    .width(70.dp)
                    .modifyIf(art == null) {
                        background(surfaceColor)
                    }
                    .modifyIf(art == null) {
                        padding(5.dp)
                    }
            )

            Spacer(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(10.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                verticalArrangement = Arrangement.Top
            ) {

                Text(
                    text = title,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Normal
                )

                Text(
                    text = artistName,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            SmallHorizontalSpacer()

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {

                Image(
                    modifier = Modifier
                        .height(24.dp)
                        .detectReorder(state),
                    painter = painterResource(id = R.drawable.drag),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                )
            }


        }

        XSmallHeightSpacer()
    }
}
