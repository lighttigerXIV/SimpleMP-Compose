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
import com.lighttigerxiv.simple.mp.compose.data.variables.ImageSizes
import com.lighttigerxiv.simple.mp.compose.functions.getImage
import com.lighttigerxiv.simple.mp.compose.ui.composables.CustomText
import com.lighttigerxiv.simple.mp.compose.ui.composables.CustomToolbar
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.MediumVerticalSpacer
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.SmallHorizontalSpacer
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.XSmallHeightSpacer
import com.lighttigerxiv.simple.mp.compose.screens.main.playlists.playlist.PlaylistScreenVM
import com.lighttigerxiv.simple.mp.compose.screens.main.playlists.playlist.modifyIf
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
    val surfaceVariantColor = MaterialTheme.colorScheme.surfaceVariant
    val screenLoaded = addSongsVM.screenLoaded.collectAsState().value
    val songs = addSongsVM.songs.collectAsState().value
    val songsData = mainVM.songsData.collectAsState().value

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

        MediumVerticalSpacer()

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

                    val art = remember { songsData?.albums?.first { it.id == song.albumID }?.art }
                    val artistName = remember { songsData?.artists?.first { it.id == song.artistID }?.name ?: "" }

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
                                model = remember { art ?: getImage(context, R.drawable.cd, ImageSizes.SMALL) },
                                contentDescription = null,
                                colorFilter = if (art == null) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .height(70.dp)
                                    .width(70.dp)
                                    .modifyIf(art == null) {
                                        background(surfaceVariantColor)
                                    }
                                    .modifyIf(art == null) {
                                        padding(5.dp)
                                    }
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
                                    text = artistName
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

            MediumVerticalSpacer()

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