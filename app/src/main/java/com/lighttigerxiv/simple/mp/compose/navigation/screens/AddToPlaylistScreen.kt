package com.lighttigerxiv.simple.mp.compose.navigation.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lighttigerxiv.simple.mp.compose.Playlist
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.Song
import com.lighttigerxiv.simple.mp.compose.composables.BasicToolbar
import com.lighttigerxiv.simple.mp.compose.composables.CustomTextField
import com.lighttigerxiv.simple.mp.compose.viewmodels.ActivityMainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddToPlaylistScreen(
    activityMainViewModel: ActivityMainViewModel,
    previousPage: String,
    selectedSongID: Long,
    onBackClick: () -> Unit
) {

    val createPlaylistSheetState = rememberBottomSheetScaffoldState()
    val playlists = activityMainViewModel.playlists.observeAsState().value!!
    val selectedSong = activityMainViewModel.songsList.find { it.id == selectedSongID }!!
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            BasicToolbar(
                backButtonText = remember { previousPage },
                onBackClicked = { onBackClick() }
            )

            BottomSheetScaffold(
                scaffoldState = createPlaylistSheetState,
                sheetShape = RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp),
                sheetElevation = 10.dp,
                sheetPeekHeight = 0.dp,
                sheetContent = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(10.dp)

                    ) {

                        val playlistNameValue = activityMainViewModel.tfNewPlaylistNameValue.observeAsState().value!!

                        Spacer(Modifier.height(2.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Row(
                                modifier = Modifier
                                    .width(40.dp)
                                    .height(5.dp)
                                    .clip(RoundedCornerShape(percent = 100))
                                    .background(MaterialTheme.colorScheme.primary)
                            ) {}
                        }

                        Spacer(Modifier.height(10.dp))

                        Text(
                            text = "Playlist Name",
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        CustomTextField(
                            text = playlistNameValue,
                            placeholder = "Insert playlist name",
                            onValueChanged = { activityMainViewModel.tfNewPlaylistNameValue.value = it },
                            textType = "text"
                        )

                        Spacer(Modifier.height(10.dp))

                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Button(
                                onClick = {

                                    activityMainViewModel.createPlaylist(playlistNameValue)
                                    scope.launch { createPlaylistSheetState.bottomSheetState.collapse() }
                                }
                            ) {

                                Text(
                                    text = "Create"
                                )
                            }
                        }
                    }
                }
            ) { sheetPadding ->

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(activityMainViewModel.surfaceColor.value!!)
                        .padding(sheetPadding)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Button(
                            onClick = { scope.launch { createPlaylistSheetState.bottomSheetState.expand() } },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            ),
                            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                            modifier = Modifier
                                .wrapContentWidth()
                                .height(50.dp)
                                .clip(RoundedCornerShape(percent = 100))
                        ) {

                            Icon(
                                painter = painterResource(id = R.drawable.icon_plus_regular),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .height(14.dp)
                                    .width(14.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Create Playlist",
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        content = {

                            items(playlists, key = { playlist -> playlist.id }) { playlist ->

                                val playlistID = remember { playlist.id }
                                val playlistName = remember { playlist.name }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {

                                            var canContinue = true


                                            val playlistSongs = if (playlist.songs != null)
                                                Gson().fromJson(playlist.songs, object : TypeToken<ArrayList<Song>>() {}.type) as ArrayList<Song>
                                            else
                                                ArrayList()


                                            playlistSongs.forEach { song ->

                                                if (song.id == selectedSongID) {
                                                    canContinue = false
                                                    Toast
                                                        .makeText(context, "Song already in playlist", Toast.LENGTH_SHORT)
                                                        .show()
                                                }
                                            }


                                            if (canContinue) {

                                                playlistSongs.add(selectedSong)
                                                val newPlaylistSongsJson = Gson().toJson(playlistSongs)

                                                activityMainViewModel.updatePlaylistSongs(songsJson = newPlaylistSongsJson, playlistID = playlist.id)

                                                onBackClick()
                                            }
                                        }
                                ) {

                                    Image(
                                        painter = painterResource(id = R.drawable.icon_playlists),
                                        contentDescription = "",
                                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                                        modifier = Modifier
                                            .width(80.dp)
                                            .aspectRatio(1f)
                                    )

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Text(
                                        text = playlistName,
                                        fontSize = 18.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}