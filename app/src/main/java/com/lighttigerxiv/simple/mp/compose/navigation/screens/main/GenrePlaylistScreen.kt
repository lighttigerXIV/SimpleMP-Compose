package com.lighttigerxiv.simple.mp.compose.navigation.screens.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.Song
import com.lighttigerxiv.simple.mp.compose.composables.BasicToolbar
import com.lighttigerxiv.simple.mp.compose.composables.SongItem
import com.lighttigerxiv.simple.mp.compose.viewmodels.ActivityMainVM
import moe.tlaster.nestedscrollview.VerticalNestedScrollView
import moe.tlaster.nestedscrollview.rememberNestedScrollViewState

@Composable
fun GenrePlaylistScreen(
    activityMainVM: ActivityMainVM,
    onBackClicked : () -> Unit
){

    val genreID = remember{activityMainVM.clickedGenreID}.value!!
    val genrePlaylist = remember{activityMainVM.songsList.filter { it.genreID == genreID } as ArrayList<Song>}
    val genreName = genrePlaylist[0].genre

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(activityMainVM.surfaceColor.value!!)
            .padding(14.dp)
    ) {

        VerticalNestedScrollView(
            state = rememberNestedScrollViewState(),
            header = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    BasicToolbar(
                        backButtonText = "Playlists",
                        onBackClicked = {onBackClicked()}
                    )
                    Image(
                        painter = painterResource(id = R.drawable.icon_playlists),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    )
                    Text(
                        text = genreName,
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
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

                    Button(
                        onClick = { activityMainVM.selectSong(genrePlaylist, 0) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = activityMainVM.surfaceColor.value!!
                        ),
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Icon(
                            bitmap = activityMainVM.miniPlayerPlayIcon,
                            contentDescription = "",
                            modifier = Modifier.height(20.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = "Play All Songs", color = MaterialTheme.colorScheme.primary)
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    LazyColumn(
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