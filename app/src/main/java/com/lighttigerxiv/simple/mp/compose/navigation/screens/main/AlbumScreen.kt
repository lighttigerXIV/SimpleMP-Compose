package com.lighttigerxiv.simple.mp.compose.navigation.screens.main

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.SCREEN_PADDING
import com.lighttigerxiv.simple.mp.compose.composables.CustomToolbar
import com.lighttigerxiv.simple.mp.compose.composables.CustomText
import com.lighttigerxiv.simple.mp.compose.composables.PlayAndShuffleRow
import com.lighttigerxiv.simple.mp.compose.composables.SongItem
import com.lighttigerxiv.simple.mp.compose.app_viewmodels.MainVM
import moe.tlaster.nestedscrollview.VerticalNestedScrollView
import moe.tlaster.nestedscrollview.rememberNestedScrollViewState


@Composable
fun AlbumScreen(
    mainVM: MainVM,
    backStackEntry: NavBackStackEntry,
    onBackClicked: () -> Unit
){

    val context = LocalContext.current
    val albumID = remember { backStackEntry.arguments?.getLong("albumID") }
    val album = remember { mainVM.currentAlbumsList.value!!.find{ it.albumID == albumID }!! }
    val albumArt = remember { mainVM.songsImagesList.find { it.albumID == albumID }!!.albumArt }
    val albumTitle = remember { album.albumName }
    val albumArtist = remember { album.artist }
    val albumSongsList =  mainVM.songs.collectAsState().value!!.filter { it.albumID == albumID }
    val nestedScrollViewState = rememberNestedScrollViewState()




    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(mainVM.surfaceColor.collectAsState().value)
            .padding(SCREEN_PADDING)
    ){

        VerticalNestedScrollView(
            state = nestedScrollViewState,
            header = {

                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    CustomToolbar(backText = "Albums", onBackClick = {onBackClicked()})
                    Image(
                        bitmap = (albumArt ?: BitmapFactory.decodeResource(context.resources, R.drawable.icon_music_record)).asImageBitmap(),
                        colorFilter = if(albumArt == null) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null,
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .clip(RoundedCornerShape(14.dp))
                            .aspectRatio(1f)
                            .align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    CustomText(
                        text = albumTitle,
                        weight = FontWeight.Bold,
                        size = 18.sp
                    )

                    CustomText(
                        text = albumArtist
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }
            },
            content = {

                Column(modifier = Modifier.fillMaxSize()) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Spacer(modifier = Modifier.height(10.dp))

                            PlayAndShuffleRow(
                                surfaceColor = mainVM.surfaceColor.collectAsState().value,
                                onPlayClick = {mainVM.unshuffleAndPlay(albumSongsList, 0)},
                                onSuffleClick = {mainVM.shuffleAndPlay(albumSongsList)}
                            )
                        }
                    }
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(5.dp),
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
                                    songAlbumArt = remember { mainVM.songsImagesList.first { it.albumID == song.albumID }.albumArt },
                                    highlight = song.path == mainVM.selectedSongPath.observeAsState().value,
                                    onSongClick = { mainVM.selectSong(albumSongsList, albumSongsList.indexOf(song)) }
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