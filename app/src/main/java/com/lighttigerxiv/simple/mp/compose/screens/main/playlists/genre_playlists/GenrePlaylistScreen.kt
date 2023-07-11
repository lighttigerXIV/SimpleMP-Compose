package com.lighttigerxiv.simple.mp.compose.screens.main.playlists.genre_playlists

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.ui.composables.CustomToolbar
import com.lighttigerxiv.simple.mp.compose.ui.composables.PlayAndShuffleRow
import com.lighttigerxiv.simple.mp.compose.activities.main.MainVM
import com.lighttigerxiv.simple.mp.compose.data.variables.SCREEN_PADDING
import com.lighttigerxiv.simple.mp.compose.data.variables.SMALL_SPACING
import com.lighttigerxiv.simple.mp.compose.ui.composables.NewSongItem
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.MediumVerticalSpacer
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.SmallVerticalSpacer
import moe.tlaster.nestedscrollview.VerticalNestedScrollView
import moe.tlaster.nestedscrollview.rememberNestedScrollViewState

@Composable
fun GenrePlaylistScreen(
    mainVM: MainVM,
    genre: String,
    onBackClicked : () -> Unit,
){

    val configuration = LocalConfiguration.current
    val inPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val surfaceColor = mainVM.surfaceColor.collectAsState().value
    val songs = mainVM.songsData.collectAsState().value?.songs
    val playlist = songs?.filter { it.genre == genre }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(surfaceColor)
            .padding(SCREEN_PADDING)
    ) {

        if(playlist != null){

            if(inPortrait){

                VerticalNestedScrollView(
                    state = rememberNestedScrollViewState(),
                    header = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        ) {
                            CustomToolbar(
                                backText = "Playlists",
                                onBackClick = {onBackClicked()}
                            )

                            MediumVerticalSpacer()

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.playlist_filled),
                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                                    modifier = Modifier
                                        .fillMaxWidth(0.6f)
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .padding(SMALL_SPACING)
                                )
                            }

                            SmallVerticalSpacer()

                            Text(
                                text = genre,
                                textAlign = TextAlign.Center,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.fillMaxWidth()
                            )

                            MediumVerticalSpacer()
                        }
                    },
                    content = {

                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {

                            PlayAndShuffleRow(
                                surfaceColor = mainVM.surfaceColor.collectAsState().value,
                                onPlayClick = {mainVM.unshuffleAndPlay(playlist, 0)},
                                onSuffleClick = {mainVM.shuffleAndPlay(playlist)}
                            )

                            MediumVerticalSpacer()

                            LazyColumn(
                                content = {

                                    itemsIndexed(
                                        items = playlist,
                                        key = { _, song -> song.id}
                                    ){ index, song ->
                                        NewSongItem(
                                            mainVM = mainVM,
                                            song = song,
                                            onSongClick = {mainVM.selectSong(playlist, position = index)}
                                        )
                                    }
                                }
                            )
                        }
                    }
                )
            }else{

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(surfaceColor)
                        .padding(SCREEN_PADDING)
                ) {

                    CustomToolbar(backText = stringResource(id = R.string.Albums), onBackClick = { onBackClicked() })

                    MediumVerticalSpacer()

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                                .weight(0.4f, fill = true),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {


                                Image(
                                    painter = painterResource(id = R.drawable.playlist_filled),
                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                                    modifier = Modifier
                                        .fillMaxHeight(0.7f)
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .padding(SMALL_SPACING)
                                )


                            SmallVerticalSpacer()

                            Text(
                                text = genre,
                                textAlign = TextAlign.Center,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.fillMaxWidth()
                            )

                            MediumVerticalSpacer()
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                                .weight(0.6f, fill = true)
                        ) {

                            PlayAndShuffleRow(
                                surfaceColor = mainVM.surfaceColor.collectAsState().value,
                                onPlayClick = {mainVM.unshuffleAndPlay(playlist, 0)},
                                onSuffleClick = {mainVM.shuffleAndPlay(playlist)}
                            )

                            MediumVerticalSpacer()

                            LazyColumn(
                                content = {

                                    itemsIndexed(
                                        items = playlist,
                                        key = { _, song -> song.id}
                                    ){ index, song ->
                                        NewSongItem(
                                            mainVM = mainVM,
                                            song = song,
                                            onSongClick = {mainVM.selectSong(playlist, position = index)}
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}