package com.lighttigerxiv.simple.mp.compose.navigation.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import com.lighttigerxiv.simple.mp.compose.Song
import com.lighttigerxiv.simple.mp.compose.composables.BasicToolbar
import com.lighttigerxiv.simple.mp.compose.composables.SongItem
import com.lighttigerxiv.simple.mp.compose.viewmodels.ActivityMainVM
import moe.tlaster.nestedscrollview.VerticalNestedScrollView
import moe.tlaster.nestedscrollview.rememberNestedScrollViewState


@Composable
fun AlbumScreen(
    activityMainVM: ActivityMainVM,
    backStackEntry: NavBackStackEntry,
    onBackClicked: () -> Unit
){

    val albumID = remember { backStackEntry.arguments?.getLong("albumID") }
    val album = remember { activityMainVM.currentAlbumsList.value!!.find{ it.albumID == albumID }!! }
    val albumArt = remember { activityMainVM.songsImagesList.find { it.albumID == albumID }!!.albumArt.asImageBitmap() }
    val albumName = remember { album.albumName }
    val albumArtist = remember { album.artistName }
    val albumSongsList = remember { activityMainVM.recentHomeSongsList.filter { it.albumID == albumID } as ArrayList<Song> }
    val nestedScrollViewState = rememberNestedScrollViewState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(activityMainVM.surfaceColor.value!!)
            .padding(14.dp)
    ){

        VerticalNestedScrollView(
            state = nestedScrollViewState,
            header = {

                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                ){
                    BasicToolbar(backText = "Albums", onBackClick = {onBackClicked()})
                    Spacer(modifier = Modifier.height(10.dp))
                    Spacer(modifier = Modifier.height(10.dp) )
                    Image(
                        bitmap = albumArt,
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .clip(RoundedCornerShape(14.dp))
                            .aspectRatio(1f)
                            .align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }

            },
            content = {

                Column(modifier = Modifier.fillMaxSize()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                    ) {
                        Column(modifier = Modifier
                            .fillMaxWidth(0.7f)
                        ) {
                            Text(
                                text = albumName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = albumArtist,
                                fontSize = 16.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            onClick = { activityMainVM.selectSong(albumSongsList, 0) },
                            modifier = Modifier
                                .height(60.dp)
                                .width(60.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(
                                bitmap = activityMainVM.miniPlayerPlayIcon,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = MaterialTheme.colorScheme.surfaceVariant)
                    LazyColumn(
                        content = {

                            item {

                                Spacer(modifier = Modifier.height(20.dp))
                            }


                            items(
                                items = albumSongsList,
                                key = { song-> song.id }
                            ){song->

                                SongItem(
                                    song = song,
                                    songAlbumArt = remember { activityMainVM.songsImagesList.first { it.albumID == song.albumID }.albumArt },
                                    highlight = song.path == activityMainVM.selectedSongPath.observeAsState().value,
                                    onSongClick = { activityMainVM.selectSong(albumSongsList, albumSongsList.indexOf(song)) }
                                )
                            }
                        },
                        modifier = Modifier.defaultMinSize(200.dp)
                    )
                }


            }
        )
    }


}