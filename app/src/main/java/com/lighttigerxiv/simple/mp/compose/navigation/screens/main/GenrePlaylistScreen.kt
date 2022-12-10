package com.lighttigerxiv.simple.mp.compose.navigation.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.SCREEN_PADDING
import com.lighttigerxiv.simple.mp.compose.Song
import com.lighttigerxiv.simple.mp.compose.composables.CustomToolbar
import com.lighttigerxiv.simple.mp.compose.composables.PlayAndShuffleRow
import com.lighttigerxiv.simple.mp.compose.composables.SongItem
import com.lighttigerxiv.simple.mp.compose.viewmodels.ActivityMainVM
import moe.tlaster.nestedscrollview.VerticalNestedScrollView
import moe.tlaster.nestedscrollview.rememberNestedScrollViewState

@Composable
fun GenrePlaylistScreen(
    activityMainVM: ActivityMainVM,
    genreID: String,
    onBackClicked : () -> Unit,
){

    val genrePlaylist = remember{activityMainVM.songsList.filter { it.genreID == genreID.toLong() } as ArrayList<Song>}
    val genreName = genrePlaylist[0].genre

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(activityMainVM.surfaceColor.collectAsState().value)
            .padding(SCREEN_PADDING)
    ) {

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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.icon_playlists),
                            contentDescription = "",
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(5.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = genreName,
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
            },
            content = {

                Column(
                    modifier = Modifier.fillMaxSize()
                ) {

                    PlayAndShuffleRow(
                        surfaceColor = activityMainVM.surfaceColor.collectAsState().value,
                        onPlayClick = {activityMainVM.unshuffleAndPlay(genrePlaylist, 0)},
                        onSuffleClick = {activityMainVM.shuffleAndPlay(genrePlaylist)}
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        content = {

                            items(
                                genrePlaylist,
                                key={ song-> song.id}
                            ){ song->

                                SongItem(
                                    song = song,
                                    songAlbumArt = activityMainVM.songsImagesList.find { song.albumID == it.albumID }!!.albumArt,
                                    highlight = activityMainVM.selectedSongPath.observeAsState().value == song.path,
                                    onSongClick = {activityMainVM.selectSong(genrePlaylist, position = genrePlaylist.indexOf(song))}
                                )
                            }
                        }
                    )
                }
            }
        )
    }
}