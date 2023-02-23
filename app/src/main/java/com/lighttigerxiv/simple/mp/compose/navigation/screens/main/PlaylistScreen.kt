package com.lighttigerxiv.simple.mp.compose.navigation.screens.main

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
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
import com.lighttigerxiv.simple.mp.compose.SCREEN_PADDING
import com.lighttigerxiv.simple.mp.compose.composables.BottomSheetHandle
import com.lighttigerxiv.simple.mp.compose.composables.CustomText
import com.lighttigerxiv.simple.mp.compose.composables.CustomTextField
import com.lighttigerxiv.simple.mp.compose.composables.PlayAndShuffleRow
import com.lighttigerxiv.simple.mp.compose.getAppString
import com.lighttigerxiv.simple.mp.compose.getBitmapFromVectorDrawable
import com.lighttigerxiv.simple.mp.compose.app_viewmodels.MainVM
import kotlinx.coroutines.launch
import moe.tlaster.nestedscrollview.VerticalNestedScrollView
import moe.tlaster.nestedscrollview.rememberNestedScrollViewState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlaylistScreen(
    mainVM: MainVM,
    arguments: Bundle,
    onBackClick: () -> Unit,
    onGetImage: () -> Unit
) {

    val playlistID = arguments.getString("playlistID")!!.toInt()
    val playlist = mainVM.playlists.collectAsState().value.find { it.id == playlistID }

    if(playlist != null){

        val playlistName = playlist.name
        val playlistSongs = mainVM.currentPlaylistSongs.observeAsState().value!!
        val playlistImageString = mainVM.currentPlaylistImageString.collectAsState().value
        val showMenu = remember { mutableStateOf(false) }
        val showDeletePlaylistDialog = remember { mutableStateOf(false) }
        val isOnEditMode = mainVM.isOnEditModePlaylistScreen.observeAsState().value!!
        val tfPlaylistNameValue = mainVM.tfPlaylistNamePlaylistScreen.observeAsState().value!!
        val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
        val scope = rememberCoroutineScope()
        val context = LocalContext.current


        val playlistImage = if (playlistImageString.isEmpty()) {
            getBitmapFromVectorDrawable(context, R.drawable.icon_playlists)
        } else {
            val imageBytes = Base64.decode(playlistImageString, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        }.asImageBitmap()



        mainVM.onPlaylistImageSelected = { bitmapString ->

            mainVM.setCurrentPlaylistImageString(bitmapString)
        }

        BottomSheetScaffold(
            scaffoldState = bottomSheetScaffoldState,
            sheetContent = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(
                                topStart = 14.dp,
                                topEnd = 14.dp
                            )
                        )
                        .border(
                            1.dp, MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(
                                topStart = 14.dp,
                                topEnd = 14.dp
                            )
                        )
                        .background(mainVM.surfaceColor.collectAsState().value)
                        .padding(10.dp)
                ) {

                    Spacer(Modifier.height(2.dp))

                    BottomSheetHandle()

                    Spacer(modifier = Modifier.height(10.dp))

                    CustomText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onGetImage()
                                scope.launch { bottomSheetScaffoldState.bottomSheetState.collapse() }
                            },
                        text = "Select Image"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    CustomText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                scope.launch { bottomSheetScaffoldState.bottomSheetState.collapse() }
                                mainVM.setCurrentPlaylistImageString("")
                            },
                        text = "Remove Image"
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }
            },

            sheetElevation = 10.dp,
            sheetShape = RoundedCornerShape(
                topStart = 14.dp,
                topEnd = 14.dp,
            ),
            sheetPeekHeight = 0.dp
        ) { bottomSheetScaffoldPaddding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(mainVM.surfaceColor.collectAsState().value)
                    .padding(bottomSheetScaffoldPaddding)
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


                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                when (isOnEditMode) {

                                    true -> {

                                        Text(
                                            text = remember { getAppString(context, R.string.Cancel) },
                                            color = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(14.dp))
                                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                                .clickable {
                                                    mainVM.tfPlaylistNamePlaylistScreen.value = playlistName
                                                    mainVM.currentPlaylistSongs.value = mainVM.playlistSongs.value
                                                    mainVM.setCurrentPlaylistImageString(playlist.image ?: "")
                                                    mainVM.isOnEditModePlaylistScreen.value = false
                                                }
                                                .padding(10.dp)
                                        )

                                        Spacer(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .weight(fill = true, weight = 1f)
                                        )

                                        Button(
                                            onClick = {
                                                mainVM.updatePlaylistName(
                                                    playlistID = playlist.id,
                                                    playlistName = mainVM.tfPlaylistNamePlaylistScreen.value!!
                                                )

                                                val newPlaylistSongsJson = Gson().toJson(playlistSongs)

                                                mainVM.updatePlaylistSongs(
                                                    songsJson = newPlaylistSongsJson,
                                                    playlistID = playlist.id
                                                )

                                                mainVM.updatePlaylistImage(playlistImageString, playlistID)

                                                mainVM.isOnEditModePlaylistScreen.value = false
                                            },
                                            enabled = tfPlaylistNameValue.isNotEmpty(),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                                            ),
                                            shape = RoundedCornerShape(14.dp)
                                        ) {

                                            Icon(
                                                modifier = Modifier
                                                    .height(20.dp)
                                                    .width(20.dp),
                                                painter = painterResource(id = R.drawable.icon_save_outline),
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }

                                    false -> {

                                        Row(
                                            modifier = Modifier
                                                .wrapContentWidth()
                                                .clip(RoundedCornerShape(percent = 30))
                                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                                .clickable { onBackClick() }
                                                .padding(10.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {

                                            Image(
                                                bitmap = remember { getBitmapFromVectorDrawable(context, R.drawable.icon_back_solid).asImageBitmap() },
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                                                modifier = Modifier
                                                    .height(14.dp)
                                                    .width(14.dp)
                                            )

                                            Text(
                                                text = "Playlists",
                                                fontSize = 14.sp,
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
                                                    text = { Text(text = remember { getAppString(context, R.string.EditPlaylist) }) },
                                                    onClick = {

                                                        showMenu.value = false
                                                        mainVM.tfPlaylistNamePlaylistScreen.value = playlistName
                                                        mainVM.isOnEditModePlaylistScreen.value = true
                                                    }
                                                )

                                                DropdownMenuItem(
                                                    text = { Text(text = remember { getAppString(context, R.string.DeletePlaylist) }) },
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
                                                                    text = remember { getAppString(context, R.string.DeletePlaylist) },
                                                                    fontSize = 18.sp,
                                                                    color = MaterialTheme.colorScheme.primary
                                                                )
                                                                Spacer(modifier = Modifier.height(10.dp))

                                                                Text(text = remember { getAppString(context, R.string.ConfirmDeletePlaylist) })

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
                                                                        onClick = {
                                                                            showDeletePlaylistDialog.value = false
                                                                        },
                                                                        border = BorderStroke(
                                                                            1.dp,
                                                                            MaterialTheme.colorScheme.primary
                                                                        ),
                                                                        colors = ButtonDefaults.buttonColors(
                                                                            containerColor = MaterialTheme.colorScheme.surface
                                                                        )
                                                                    ) {
                                                                        Text(
                                                                            text = remember { getAppString(context, R.string.Cancel) },
                                                                            maxLines = 1,
                                                                            overflow = TextOverflow.Ellipsis,
                                                                            color = MaterialTheme.colorScheme.onSurface
                                                                        )
                                                                    }

                                                                    Spacer(modifier = Modifier.width(10.dp))

                                                                    Button(
                                                                        onClick = {

                                                                            onBackClick()
                                                                            mainVM.deletePlaylist(playlistID = playlist.id)
                                                                        },
                                                                        colors = ButtonDefaults.buttonColors(
                                                                            containerColor = MaterialTheme.colorScheme.primary
                                                                        )
                                                                    ) {

                                                                        Text(text = remember { getAppString(context, R.string.Delete) })
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

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {


                                val surfaceVariantColor = MaterialTheme.colorScheme.surfaceVariant


                                Image(
                                    bitmap = playlistImage,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    colorFilter = if (playlistImageString.isEmpty()) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null,
                                    modifier = Modifier
                                        .fillMaxWidth(0.6f)
                                        .aspectRatio(1f)
                                        .padding(5.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .modifyIf(playlistImageString.isEmpty()) {
                                            background(surfaceVariantColor)
                                        }
                                        .modifyIf(isOnEditMode) {
                                            clickable {
                                                scope.launch {
                                                    bottomSheetScaffoldState.bottomSheetState.expand()
                                                }
                                            }
                                        }
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            when (isOnEditMode) {

                                true -> {

                                    CustomTextField(
                                        text = tfPlaylistNameValue,
                                        placeholder = remember { getAppString(context, R.string.InsertPlaylistName) },
                                        onTextChange = { mainVM.tfPlaylistNamePlaylistScreen.value = it }
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

                                if (playlistSongs.isNotEmpty()) {
                                    PlayAndShuffleRow(
                                        surfaceColor = mainVM.surfaceColor.collectAsState().value,
                                        onPlayClick = { mainVM.unshuffleAndPlay(playlistSongs, 0) },
                                        onSuffleClick = { mainVM.shuffleAndPlay(playlistSongs) }
                                    )
                                } else {

                                    Spacer(modifier = Modifier.height(10.dp))

                                    CustomText(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        text = remember { getAppString(context, R.string.NoSongsAdded) },
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(5.dp),
                                content = {

                                    itemsIndexed(
                                        playlistSongs,
                                        key = { _, song -> song.id }
                                    ) { index, song ->

                                        val songTitle = remember { song.title }
                                        val songArtist = remember { song.artist }
                                        val highlight = song.path == mainVM.selectedSongPath.observeAsState().value!!
                                        val songAlbumArt = mainVM.songsImages.collectAsState().value?.find { it.albumID == song.albumID }?.albumArt


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
                                                    mainVM.selectSong(playlistSongs, index)
                                                }
                                            }
                                        ) {


                                            Image(
                                                bitmap = remember { songAlbumArt ?: BitmapFactory.decodeResource(context.resources, R.drawable.icon_music_record) }.asImageBitmap(),
                                                colorFilter = if(songAlbumArt == null) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null,
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

                                                if (isOnEditMode) {

                                                    Icon(
                                                        painter = painterResource(id = R.drawable.icon_remove_regular),
                                                        contentDescription = "",
                                                        tint = MaterialTheme.colorScheme.onSurface,
                                                        modifier = Modifier
                                                            .fillMaxHeight()
                                                            .width(25.dp)
                                                            .clickable {

                                                                val temp = ArrayList(mainVM.currentPlaylistSongs.value!!)
                                                                temp.removeIf { it.id == song.id }

                                                                mainVM.currentPlaylistSongs.value = temp
                                                            }
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            )
                        }
                    }
                )
            }
        }
    }
}

fun Modifier.modifyIf(condition: Boolean, modify: Modifier.() -> Modifier) =
    if (condition) modify() else this
