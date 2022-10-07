package com.lighttigerxiv.simple.mp.compose.navigation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.gson.Gson
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.UsefulFunctions
import com.lighttigerxiv.simple.mp.compose.composables.BasicToolbar
import com.lighttigerxiv.simple.mp.compose.composables.SongItem
import com.lighttigerxiv.simple.mp.compose.viewmodels.ActivityMainViewModel
import com.lighttigerxiv.simple.mp.compose.viewmodels.PlaylistScreenViewModel
import moe.tlaster.nestedscrollview.VerticalNestedScrollView
import moe.tlaster.nestedscrollview.rememberNestedScrollViewState

@Composable
fun PlaylistScreen(

    activityMainViewModel: ActivityMainViewModel,
    onBackClicked : () -> Unit
){

    val playlistID = activityMainViewModel.clickedPlaylistID.value
    val playlist = remember{activityMainViewModel.playlists.value!!.find { it.id == playlistID }!!}
    val playlistName = playlist.name
    val playlistSongs = playlist.songs
    val showMenu = remember{ mutableStateOf(false)}
    val showDeletePlaylistDialog = remember{ mutableStateOf(false) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(activityMainViewModel.surfaceColor.value!!)
            .padding(10.dp)
    ) {

        VerticalNestedScrollView(
            state = rememberNestedScrollViewState(),
            header = {
                val context = LocalContext.current

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)) {

                        Button(
                            onClick = {onBackClicked()},
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            ),
                            contentPadding = PaddingValues(0.dp)
                        ) {

                            Image(
                                bitmap = remember { UsefulFunctions.getBitmapFromVectorDrawable(context, R.drawable.icon_back_solid).asImageBitmap() },
                                contentDescription = "",
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                                modifier = Modifier
                                    .height(25.dp)
                                    .width(25.dp)
                            )
                            Text(
                                text = "Playlists",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }

                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .weight(fill = true, weight = 1f)
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxHeight()
                        ){

                            DropdownMenu(
                                expanded = showMenu.value,
                                onDismissRequest = { showMenu.value = false }
                            ) {

                                DropdownMenuItem(
                                    text = { Text(text = "Edit Playlist") },
                                    onClick = {}
                                )

                                DropdownMenuItem(
                                    text = { Text(text = "Delete Playlist") },
                                    onClick = {

                                        showDeletePlaylistDialog.value = true
                                    }
                                )


                                if(showDeletePlaylistDialog.value){

                                    Dialog(
                                        onDismissRequest = { showDeletePlaylistDialog.value = false }
                                    ){
                                        Surface(
                                            shape = RoundedCornerShape(14.dp),
                                            color = MaterialTheme.colorScheme.surface
                                        ) {

                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(10.dp)
                                            ) {

                                                Text(
                                                    text = "Delete Playlist",
                                                    fontSize = 18.sp,
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                                Spacer(modifier = Modifier.height(10.dp))

                                                Text(text = "Are you sure you want to delete the playlist?")
                                                
                                                Spacer(modifier = Modifier.height(20.dp))

                                                Row(
                                                    modifier = Modifier.fillMaxWidth()
                                                ){

                                                    Spacer(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .weight(fill = true, weight = 1f)
                                                    )

                                                    Button(
                                                        onClick = { showDeletePlaylistDialog.value = false },
                                                        border = BorderStroke(
                                                            1.dp,
                                                            MaterialTheme.colorScheme.primary
                                                        ),
                                                        colors = ButtonDefaults.buttonColors(
                                                            containerColor = MaterialTheme.colorScheme.surface
                                                        )
                                                    ) {
                                                        Text(
                                                            text = "Cancel",
                                                            maxLines = 1,
                                                            overflow = TextOverflow.Ellipsis,
                                                            color = MaterialTheme.colorScheme.onSurface
                                                        )
                                                    }
                                                    
                                                    Spacer(modifier = Modifier.width(10.dp))

                                                    Button(
                                                        onClick = {

                                                            val temp = activityMainViewModel.playlists.value
                                                            temp!!.removeIf { it.id == playlistID }

                                                            val playlistsJson = Gson().toJson(temp)
                                                            activityMainViewModel.preferences.edit().putString("playlists", playlistsJson).apply()
                                                            activityMainViewModel.playlists.value = temp

                                                            showMenu.value = false
                                                            showDeletePlaylistDialog.value = false

                                                            onBackClicked()
                                                        },
                                                        colors = ButtonDefaults.buttonColors(
                                                            containerColor = MaterialTheme.colorScheme.primary
                                                        )
                                                    ){

                                                        Text( text = "Delete" )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            


                            Icon(
                                painter = painterResource(id = R.drawable.icon_more_regular),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .height(25.dp)
                                    .aspectRatio(1f)
                                    .clickable { showMenu.value = true }
                            )
                        }
                    }

                    Image(
                        painter = painterResource(id = R.drawable.icon_playlists),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    )
                    Text(
                        text = playlistName,
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
                        onClick = { activityMainViewModel.selectSong(playlistSongs, 0) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = activityMainViewModel.surfaceColor.value!!
                        ),
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Icon(
                            bitmap = activityMainViewModel.miniPlayerPlayIcon,
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

                            itemsIndexed(
                                playlistSongs,
                                key={ _, song-> song.id}
                            ){ index, song->

                                SongItem(
                                    song = song,
                                    position = index,
                                    lastPosition = index == playlistSongs.size - 1,
                                    songAlbumArt = activityMainViewModel.songsImagesList.find { song.albumID == it.albumID }!!.albumArt.asImageBitmap(),
                                    highlight = activityMainViewModel.selectedSongPath.observeAsState().value == song.path,
                                    onSongClick = {

                                            activityMainViewModel.selectSong(playlistSongs, index)
                                    }
                                )
                            }
                        }
                    )
                }
            }
        )
    }
}