package com.lighttigerxiv.simple.mp.compose.navigation.screens.main

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.google.gson.Gson
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.UsefulFunctions
import com.lighttigerxiv.simple.mp.compose.composables.CustomTextField
import com.lighttigerxiv.simple.mp.compose.viewmodels.ActivityMainVM
import moe.tlaster.nestedscrollview.VerticalNestedScrollView
import moe.tlaster.nestedscrollview.rememberNestedScrollViewState

@Composable
fun PlaylistScreen(

    activityMainVM: ActivityMainVM,
    onBackClicked: () -> Unit
) {

    val playlistID = activityMainVM.clickedPlaylistID.value
    val playlist = activityMainVM.playlists.observeAsState().value!!.find { it.id == playlistID }!!
    val playlistName = playlist.name
    val playlistSongs = activityMainVM.currentPlaylistSongs.observeAsState().value!!
    val showMenu = remember { mutableStateOf(false) }
    val showDeletePlaylistDialog = remember { mutableStateOf(false) }
    val isOnEditMode = activityMainVM.isOnEditMode_PlaylistScreen.observeAsState().value!!


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(activityMainVM.surfaceColor.collectAsState().value)
            .padding(14.dp)
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


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {

                        when (isOnEditMode) {

                            true -> {

                                Text(
                                    text = "Cancel",
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.clickable {

                                        activityMainVM.tfPlaylistName_PlaylistScreen.value = playlistName
                                        activityMainVM.currentPlaylistSongs.value = activityMainVM.playlistSongs.value
                                        activityMainVM.isOnEditMode_PlaylistScreen.value = false
                                    }
                                )

                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(fill = true, weight = 1f)
                                )

                                Text(
                                    text = "Save",
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.clickable {

                                        activityMainVM.updatePlaylistName(
                                            playlistID = playlist.id,
                                            playlistName = activityMainVM.tfPlaylistName_PlaylistScreen.value!!
                                        )

                                        val newPlaylistSongsJson = Gson().toJson( playlistSongs )

                                        activityMainVM.updatePlaylistSongs(
                                            songsJson = newPlaylistSongsJson,
                                            playlistID = playlist.id
                                        )

                                        activityMainVM.isOnEditMode_PlaylistScreen.value = false
                                    }
                                )
                            }

                            false -> {

                                Button(
                                    onClick = { onBackClicked() },
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

                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(fill = true, weight = 1f)
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxHeight()
                                ) {

                                    DropdownMenu(
                                        expanded = showMenu.value,
                                        onDismissRequest = { showMenu.value = false }
                                    ) {

                                        DropdownMenuItem(
                                            text = { Text(text = "Edit Playlist") },
                                            onClick = {

                                                showMenu.value = false
                                                activityMainVM.tfPlaylistName_PlaylistScreen.value = playlistName
                                                activityMainVM.isOnEditMode_PlaylistScreen.value = true
                                            }
                                        )

                                        DropdownMenuItem(
                                            text = { Text(text = "Delete Playlist") },
                                            onClick = {

                                                showDeletePlaylistDialog.value = true
                                            }
                                        )


                                        if (showDeletePlaylistDialog.value) {

                                            Dialog(
                                                onDismissRequest = { showDeletePlaylistDialog.value = false }
                                            ) {
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
                                                        ) {

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

                                                                    onBackClicked()
                                                                    activityMainVM.deletePlaylist( playlistID = playlist.id )

                                                                    showMenu.value = false
                                                                    showDeletePlaylistDialog.value = false
                                                                },
                                                                colors = ButtonDefaults.buttonColors(
                                                                    containerColor = MaterialTheme.colorScheme.primary
                                                                )
                                                            ) {

                                                                Text(text = "Delete")
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

                    when (isOnEditMode) {

                        true -> {

                            CustomTextField(
                                text = activityMainVM.tfPlaylistName_PlaylistScreen.observeAsState().value!!,
                                placeholder = "Insert playlist name",
                                onTextChange = { activityMainVM.tfPlaylistName_PlaylistScreen.value = it }
                            )
                        }
                        false -> {

                            Text(
                                text = playlistName,
                                textAlign = TextAlign.Center,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }


                    Spacer(modifier = Modifier.height(20.dp))
                }
            },
            content = {

                Column(
                    modifier = Modifier.fillMaxSize()
                ) {

                    if (!isOnEditMode) {

                        Button(
                            onClick = { activityMainVM.selectSong(playlistSongs, 0) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = activityMainVM.surfaceColor.collectAsState().value
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
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    LazyColumn(
                        content = {

                            itemsIndexed(
                                playlistSongs,
                                key = { _, song -> song.id }
                            ) { index, song ->

                                val songTitle = remember { song.title }
                                val songArtist = remember { song.artistName }
                                val isPopupMenuExpanded = remember { mutableStateOf(false) }
                                val highlight = song.path == activityMainVM.selectedSongPath.observeAsState().value!!
                                val songAlbumArt = remember { activityMainVM.songsImagesList.find { it.albumID == song.albumID }!!.albumArt.asImageBitmap() }

                                val titleColor = when (highlight) {
                                    true -> MaterialTheme.colorScheme.primary
                                    else -> MaterialTheme.colorScheme.onSurface
                                }

                                val titleWeight = when (highlight) {
                                    true -> FontWeight.Medium
                                    else -> FontWeight.Normal
                                }


                                Row(modifier = Modifier
                                    .fillMaxWidth()
                                    .height(70.dp)
                                    .clickable {

                                        if (!isOnEditMode) {
                                            activityMainVM.selectSong(playlistSongs, index)
                                        }
                                    }
                                ) {


                                    Image(
                                        bitmap = remember { songAlbumArt },
                                        contentDescription = songTitle,
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .clip(RoundedCornerShape(20)),
                                    )
                                    Spacer(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .width(10.dp)
                                    )

                                    Column(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .weight(1f)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        ) {
                                            Text(
                                                text = songTitle,
                                                fontSize = 16.sp,
                                                color = titleColor,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                fontWeight = titleWeight
                                            )
                                        }
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .fillMaxHeight(0.5f)
                                        ) {
                                            Text(
                                                text = songArtist,
                                                fontSize = 16.sp,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }


                                    Column(
                                        verticalArrangement = Arrangement.Center,
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .wrapContentWidth()
                                    ) {

                                        when (isOnEditMode) {

                                            true -> {

                                                Icon(
                                                    painter = painterResource(id = R.drawable.icon_remove_regular),
                                                    contentDescription = "",
                                                    tint = MaterialTheme.colorScheme.onSurface,
                                                    modifier = Modifier
                                                        .fillMaxHeight()
                                                        .width(25.dp)
                                                        .clickable {

                                                            val temp = ArrayList(activityMainVM.currentPlaylistSongs.value!!)
                                                            temp.removeIf { it.id == song.id }

                                                            activityMainVM.currentPlaylistSongs.value = temp
                                                        }
                                                )
                                            }
                                            false -> {

                                                DropdownMenu(
                                                    expanded = isPopupMenuExpanded.value,
                                                    onDismissRequest = { isPopupMenuExpanded.value = false }
                                                ) {

                                                    DropdownMenuItem(
                                                        text = { Text(text = "Go to Artist") },
                                                        onClick = {

                                                        }
                                                    )

                                                    DropdownMenuItem(
                                                        text = { Text(text = "Go to Album") },
                                                        onClick = {

                                                        }
                                                    )
                                                }

                                                Icon(
                                                    painter = painterResource(id = R.drawable.icon_three_dots_regular),
                                                    contentDescription = "",
                                                    tint = MaterialTheme.colorScheme.onSurface,
                                                    modifier = Modifier
                                                        .fillMaxHeight()
                                                        .width(20.dp)
                                                        .clickable { isPopupMenuExpanded.value = true }
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(5.dp))
                            }
                        }
                    )
                }
            }
        )
    }
}