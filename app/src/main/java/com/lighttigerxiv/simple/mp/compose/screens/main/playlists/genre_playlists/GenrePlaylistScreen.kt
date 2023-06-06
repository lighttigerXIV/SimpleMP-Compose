package com.lighttigerxiv.simple.mp.compose.screens.main.playlists.genre_playlists

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.ui.composables.CustomToolbar
import com.lighttigerxiv.simple.mp.compose.ui.composables.PlayAndShuffleRow
import com.lighttigerxiv.simple.mp.compose.ui.composables.SongItem
import com.lighttigerxiv.simple.mp.compose.activities.main.MainVM
import com.lighttigerxiv.simple.mp.compose.data.variables.SCREEN_PADDING
import com.lighttigerxiv.simple.mp.compose.data.variables.SMALL_SPACING
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.MediumHeightSpacer
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.SmallHeightSpacer
import moe.tlaster.nestedscrollview.VerticalNestedScrollView
import moe.tlaster.nestedscrollview.rememberNestedScrollViewState

@Composable
fun GenrePlaylistScreen(
    mainVM: MainVM,
    genre: String,
    onBackClicked : () -> Unit,
){

    val surfaceColor = mainVM.surfaceColor.collectAsState().value

    val selectedSong = mainVM.currentSong.collectAsState().value

    val songs = mainVM.songs.collectAsState().value

    val songsImages = mainVM.songsCovers.collectAsState().value

    val playlist = songs?.filter { it.genre == genre }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(surfaceColor)
            .padding(SCREEN_PADDING)
    ) {

        if(playlist != null){
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

                        MediumHeightSpacer()

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.playlist),
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

                        SmallHeightSpacer()

                        Text(
                            text = genre,
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.fillMaxWidth()
                        )

                        MediumHeightSpacer()
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

                        MediumHeightSpacer()

                        LazyColumn(
                            content = {

                                itemsIndexed(
                                    items = playlist,
                                    key = { _, song -> song.id}
                                ){ index, song ->
                                    SongItem(
                                        song = song,
                                        songAlbumArt = songsImages?.find { song.albumID == it.albumID }?.albumArt,
                                        highlight = song.path == selectedSong?.path,
                                        onSongClick = {mainVM.selectSong(playlist, position = index)}
                                    )
                                }
                            }
                        )
                    }
                }
            )
        }
    }
}