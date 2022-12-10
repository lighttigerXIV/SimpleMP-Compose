package com.lighttigerxiv.simple.mp.compose.navigation.screens.main

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import coil.compose.AsyncImage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.SCREEN_PADDING
import com.lighttigerxiv.simple.mp.compose.Song
import com.lighttigerxiv.simple.mp.compose.composables.BottomSheetHandle
import com.lighttigerxiv.simple.mp.compose.composables.CustomText
import com.lighttigerxiv.simple.mp.compose.composables.CustomToolbar
import com.lighttigerxiv.simple.mp.compose.composables.CustomTextField
import com.lighttigerxiv.simple.mp.compose.getBitmapFromVectorDrawable
import com.lighttigerxiv.simple.mp.compose.viewmodels.ActivityMainVM
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddToPlaylistScreen(
    activityMainVM: ActivityMainVM,
    backStackEntry: NavBackStackEntry,
    previousPage: String,
    onBackClick: () -> Unit
) {

    val songID = backStackEntry.arguments?.getLong("songID")
    val createPlaylistSheetState = rememberBottomSheetScaffoldState()
    val playlists = activityMainVM.playlists.collectAsState().value
    val selectedSong = activityMainVM.songsList.find { it.id == songID }!!
    val context = LocalContext.current
    val scope = rememberCoroutineScope()



    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .padding(
                    top = SCREEN_PADDING,
                    start = SCREEN_PADDING,
                    end = SCREEN_PADDING
                )
        ) {
            CustomToolbar(
                backText = remember { previousPage },
                onBackClick = { onBackClick() },
                secondaryContent = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = true),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Row(
                            modifier = Modifier
                                .wrapContentWidth()
                                .clip(RoundedCornerShape(100))
                                .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(100))
                                .clickable {
                                    scope.launch { createPlaylistSheetState.bottomSheetState.expand() }
                                }
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {

                            Icon(
                                modifier = Modifier
                                    .width(15.dp)
                                    .height(15.dp),
                                painter = painterResource(id = R.drawable.icon_plus_solid),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            CustomText(
                                text = "Create Playlist",
                                color = MaterialTheme.colorScheme.primary,
                                size = 14.sp,
                                maxLines = 1
                            )
                        }
                    }
                }
            )
        }


        BottomSheetScaffold(
            scaffoldState = createPlaylistSheetState,
            sheetShape = RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp),
            sheetPeekHeight = 0.dp,
            sheetContent = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .border(2.dp, MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .padding(10.dp)

                ) {

                    val playlistNameValue = activityMainVM.tfNewPlaylistNameValue.observeAsState().value!!

                    BottomSheetHandle()

                    Spacer(Modifier.height(10.dp))

                    Text(
                        text = "Playlist Name",
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    CustomTextField(
                        text = playlistNameValue,
                        placeholder = "Insert playlist name",
                        onTextChange = { activityMainVM.tfNewPlaylistNameValue.value = it },
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

                                activityMainVM.createPlaylist(playlistNameValue)
                                scope.launch { createPlaylistSheetState.bottomSheetState.collapse() }
                            },
                            enabled = playlistNameValue.isNotEmpty()
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
                    .background(activityMainVM.surfaceColor.collectAsState().value)
                    .padding(sheetPadding)
                    .padding(
                        start = SCREEN_PADDING,
                        end = SCREEN_PADDING,
                        bottom = SCREEN_PADDING
                    )
            ) {

                Spacer(
                    modifier = Modifier.height(20.dp)
                )


                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(2.5.dp),
                    content = {

                        items(playlists, key = { playlist -> playlist.id }) { playlist ->

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

                                            if (song.id == songID) {
                                                canContinue = false
                                                Toast
                                                    .makeText(context, "Song already in playlist", Toast.LENGTH_SHORT)
                                                    .show()
                                            }
                                        }


                                        if (canContinue) {

                                            playlistSongs.add(selectedSong)
                                            val newPlaylistSongsJson = Gson().toJson(playlistSongs)

                                            activityMainVM.updatePlaylistSongs(songsJson = newPlaylistSongsJson, playlistID = playlist.id)

                                            onBackClick()
                                        }
                                    }
                            ) {

                                val playlistImage = remember {
                                    if (playlist.image.isNullOrEmpty()) {
                                        getBitmapFromVectorDrawable(context, R.drawable.icon_playlists)
                                    } else {
                                        val imageBytes = Base64.decode(playlist.image, Base64.DEFAULT)
                                        BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size).apply {
                                            compress(Bitmap.CompressFormat.PNG, 40, ByteArrayOutputStream())
                                        }
                                    }
                                }

                                AsyncImage(
                                    model = playlistImage,
                                    contentDescription = "",
                                    colorFilter = if (playlist.image.isNullOrEmpty()) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .width(80.dp)
                                        .aspectRatio(1f)
                                        .padding(5.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
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