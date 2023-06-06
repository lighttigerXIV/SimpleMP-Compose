package com.lighttigerxiv.simple.mp.compose.screens.main.playlists.playlist.add_songs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.data.variables.SCREEN_PADDING
import com.lighttigerxiv.simple.mp.compose.activities.main.MainVM
import com.lighttigerxiv.simple.mp.compose.functions.getBitmapFromVector
import com.lighttigerxiv.simple.mp.compose.ui.composables.CustomText
import com.lighttigerxiv.simple.mp.compose.ui.composables.CustomToolbar
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.MediumHeightSpacer
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.SmallHorizontalSpacer
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.XSmallHeightSpacer
import com.lighttigerxiv.simple.mp.compose.screens.main.playlists.playlist.PlaylistScreenVM
import com.lighttigerxiv.simple.mp.compose.ui.composables.SmallIcon
import kotlinx.coroutines.launch

@Composable
fun AddSongsScreen(
    mainVM: MainVM,
    addSongsVM: AddSongsScreenVM,
    playlistVM: PlaylistScreenVM,
    playlistID: String,
    onGoBack: () -> Unit
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val surfaceColor = mainVM.surfaceColor.collectAsState().value
    val screenLoaded = addSongsVM.screenLoaded.collectAsState().value
    val songs = addSongsVM.songs.collectAsState().value
    val songsCovers = mainVM.compressedSongsCovers.collectAsState().value

    if (!screenLoaded) {
        addSongsVM.loadScreen(playlistID, mainVM)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(surfaceColor)
            .padding(SCREEN_PADDING)
    ) {

        CustomToolbar(
            backText = stringResource(id = R.string.Playlist),
            onBackClick = {
                onGoBack()
            }
        )

        MediumHeightSpacer()

        if (screenLoaded) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f, fill = true)
            ) {

                items(
                    items = songs!!,
                    key = { it.id }
                ) { song ->

                    val songAlbumArt = songsCovers?.find { it.albumID == song.albumID }?.albumArt

                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(70.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .clickable {
                                    addSongsVM.toggleSong(song.id)
                                },
                            verticalAlignment = Alignment.CenterVertically

                        ) {

                            AsyncImage(
                                model = remember { songAlbumArt ?: getBitmapFromVector(context, R.drawable.record) },
                                contentDescription = null,
                                colorFilter = if (songAlbumArt == null) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .height(70.dp)
                                    .width(70.dp)


                            )

                            SmallHorizontalSpacer()

                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1f),
                                verticalArrangement = Arrangement.Top
                            ) {

                                CustomText(
                                    text = song.title
                                )

                                CustomText(
                                    text = song.artist
                                )
                            }

                            SmallIcon(
                                id = when (song.selected) {

                                    true -> R.drawable.check

                                    false -> R.drawable.plus
                                },
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        XSmallHeightSpacer()
                    }
                }
            }

            MediumHeightSpacer()

            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {

                    scope.launch {

                        addSongsVM.addSongs(
                            playlistID = playlistID,
                            onSuccess = {
                                onGoBack()
                            },
                            playlistVM = playlistVM,
                            mainVM = mainVM
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                CustomText(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.Select),
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}