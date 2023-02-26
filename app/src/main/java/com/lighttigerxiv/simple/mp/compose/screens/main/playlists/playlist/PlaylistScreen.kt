package com.lighttigerxiv.simple.mp.compose.screens.main.playlists.playlist

import android.graphics.BitmapFactory
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.lighttigerxiv.simple.mp.compose.*
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.app_viewmodels.MainVM
import com.lighttigerxiv.simple.mp.compose.composables.*
import com.lighttigerxiv.simple.mp.compose.composables.spacers.MediumHeightSpacer
import com.lighttigerxiv.simple.mp.compose.composables.spacers.SmallHeightSpacer
import com.lighttigerxiv.simple.mp.compose.composables.spacers.SmallWidthSpacer
import com.lighttigerxiv.simple.mp.compose.composables.spacers.XSmallWidthSpacer
import com.lighttigerxiv.simple.mp.compose.screens.main.playlists.PlaylistsScreenVM
import kotlinx.coroutines.launch
import moe.tlaster.nestedscrollview.VerticalNestedScrollView
import moe.tlaster.nestedscrollview.rememberNestedScrollViewState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlaylistScreen(
    mainVM: MainVM,
    playlistsVM: PlaylistsScreenVM,
    playlistVM: PlaylistScreenVM,
    playlistID: String,
    onGoBack: () -> Unit,
    onGetImage: () -> Unit
) {

    //States

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    //Variables

    val surfaceColor = mainVM.surfaceColor.collectAsState().value

    val surfaceVariantColor = MaterialTheme.colorScheme.surfaceVariant

    val screenLoaded = playlistVM.screenLoaded.collectAsState().value

    val playlist = playlistVM.playlist.collectAsState().value

    val playlistImage = playlistVM.playlistImage.collectAsState().value

    val songs = playlistVM.currentSongs.collectAsState().value

    val showMenu = playlistVM.showMenu.collectAsState().value

    val showSelectImageDialog = playlistVM.showSelectImageDialog.collectAsState().value

    val showDeleteDialog = playlistVM.showDeleteDialog.collectAsState().value

    val saveButtonEnabled = playlistVM.saveButtonEnabled.collectAsState().value

    val onEditMode = playlistVM.onEditMode.collectAsState().value

    val playlistNameText = playlistVM.playlistNameText.collectAsState().value

    val selectedSong = mainVM.selectedSong.collectAsState().value

    val songsImages = mainVM.songsImages.collectAsState().value


    if (!screenLoaded) {
        playlistVM.loadScreen(playlistID, mainVM, playlistsVM)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(surfaceColor)
            .padding(SCREEN_PADDING)
    ) {

        VerticalNestedScrollView(
            state = rememberNestedScrollViewState(),
            header = {

                if (screenLoaded) {

                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {

                            Row(
                                modifier = Modifier
                                    .height(50.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable {

                                        when (onEditMode) {

                                            true -> {

                                                playlistVM.updateOnEditMode(false)
                                            }
                                            false -> {

                                                onGoBack()
                                            }
                                        }
                                    }
                                    .padding(SMALL_SPACING),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {

                                if (!onEditMode) {

                                    SmallIcon(id = R.drawable.back)

                                    XSmallWidthSpacer()
                                }

                                CustomText(
                                    text = when (onEditMode) {
                                        true -> stringResource(id = R.string.Cancel)
                                        false -> stringResource(id = R.string.Playlists)
                                    },
                                    color = when (onEditMode) {
                                        true -> MaterialTheme.colorScheme.onSurface
                                        false -> MaterialTheme.colorScheme.primary
                                    },
                                    textAlign = TextAlign.Center
                                )
                            }

                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f, fill = true)
                            )



                            Column(
                                modifier = Modifier
                                    .height(50.dp)
                                    .width(50.dp)
                                    .clip(RoundedCornerShape(14))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .modifyIf(saveButtonEnabled) {
                                        clickable {
                                            when (onEditMode) {

                                                true -> {

                                                    scope.launch {

                                                        playlistVM.savePlaylistChanges(playlistsVM)

                                                        playlistVM.updateOnEditMode(false)
                                                    }
                                                }

                                                false -> {
                                                    playlistVM.updateShowMenu(true)
                                                }
                                            }
                                        }
                                    },
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                when (onEditMode) {

                                    true -> {
                                        MediumIcon(
                                            id = R.drawable.save,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }

                                    false -> {
                                        MediumIcon(id = R.drawable.menu)
                                    }
                                }
                            }

                            Column {
                                DropdownMenu(
                                    expanded = showMenu,
                                    onDismissRequest = {
                                        playlistVM.updateShowMenu(false)
                                    }
                                ) {

                                    DropdownMenuItem(
                                        text = {
                                            Text(text = stringResource(id = R.string.EditPlaylist))
                                        },
                                        onClick = {

                                            playlistVM.updateShowMenu(false)

                                            playlistVM.updateOnEditMode(true)
                                        }
                                    )

                                    DropdownMenuItem(
                                        text = {
                                            Text(text = stringResource(id = R.string.DeletePlaylist))
                                        },
                                        onClick = {

                                            playlistVM.updateShowMenu(false)

                                            playlistVM.updateShowDeleteDialog(true)
                                        }
                                    )
                                }

                                if (showDeleteDialog) {

                                    Dialog(
                                        onDismissRequest = {
                                            playlistVM.updateShowDeleteDialog(false)
                                        }
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(14.dp),
                                            color = MaterialTheme.colorScheme.surface
                                        ) {

                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(SMALL_SPACING)
                                            ) {

                                                CustomText(
                                                    text = stringResource(id = R.string.DeletePlaylist),
                                                    size = 18.sp,
                                                    color = MaterialTheme.colorScheme.primary
                                                )

                                                SmallHeightSpacer()

                                                Text(
                                                    text = stringResource(id = R.string.ConfirmDeletePlaylist)
                                                )

                                                MediumHeightSpacer()

                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                ) {

                                                    Spacer(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .weight(fill = true, weight = 1f)
                                                    )

                                                    Button(
                                                        onClick = {
                                                            playlistVM.updateShowDeleteDialog(false)
                                                        },
                                                        border = BorderStroke(
                                                            1.dp,
                                                            MaterialTheme.colorScheme.primary
                                                        ),
                                                        colors = ButtonDefaults.buttonColors(
                                                            containerColor = MaterialTheme.colorScheme.surface
                                                        )
                                                    ) {

                                                        CustomText(
                                                            text = stringResource(id = R.string.Cancel)
                                                        )
                                                    }

                                                    SmallWidthSpacer()

                                                    Button(
                                                        onClick = {

                                                            scope.launch {
                                                                onGoBack()

                                                                playlistVM.deletePlaylist(playlistID, playlistsVM)
                                                            }
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
                        }

                        MediumHeightSpacer()

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {

                            Image(
                                bitmap = playlistImage!!,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                colorFilter = if (playlist!!.image == null) {
                                    ColorFilter.tint(MaterialTheme.colorScheme.primary)
                                } else {
                                    null
                                },
                                modifier = Modifier
                                    .fillMaxWidth(0.6f)
                                    .aspectRatio(1f)
                                    .padding(5.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .modifyIf(playlist.image == null) {
                                        background(surfaceVariantColor)
                                    }
                                    .modifyIf(onEditMode) {
                                        clickable {
                                            scope.launch {
                                                bottomSheetScaffoldState.bottomSheetState.expand()
                                            }
                                        }
                                    }
                            )
                        }

                        SmallHeightSpacer()

                        when (onEditMode) {

                            true -> {

                                CustomTextField(
                                    text = playlistNameText,
                                    placeholder = stringResource(id = R.string.InsertPlaylistName),
                                    onTextChange = {

                                        playlistVM.updatePlaylistNameText(it)

                                        playlistVM.updateSaveButtonEnabled(it.isNotEmpty())
                                    }
                                )
                            }
                            false -> {

                                Text(
                                    text = playlist!!.name,
                                    textAlign = TextAlign.Center,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        MediumHeightSpacer()
                    }
                }
            }
        ) {

            if (songs != null) {

                Column{
                    if (!onEditMode) {

                        if (songs.isNotEmpty()) {

                            PlayAndShuffleRow(
                                surfaceColor = surfaceColor,
                                onPlayClick = {

                                    mainVM.unshuffleAndPlay(songs, 0)
                                },
                                onSuffleClick = {

                                    mainVM.shuffleAndPlay(songs)
                                }
                            )
                        } else {

                            CustomText(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                text = remember { getAppString(context, R.string.NoSongsAdded) },
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    MediumHeightSpacer()

                    LazyColumn(
                        content = {

                            itemsIndexed(
                                songs,
                                key = { _, song -> song.id }
                            ) { index, song ->

                                val songTitle = remember { song.title }
                                val songArtist = remember { song.artist }
                                val highlight = song.path == selectedSong?.path
                                val songAlbumArt = songsImages?.find { it.albumID == song.albumID }?.albumArt


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

                                        if (!onEditMode) {
                                            mainVM.selectSong(songs, index)
                                        }
                                    }
                                ) {


                                    Image(
                                        bitmap = remember { songAlbumArt ?: BitmapFactory.decodeResource(context.resources, R.drawable.icon_music_record) }.asImageBitmap(),
                                        colorFilter = if (songAlbumArt == null) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null,
                                        contentDescription = songTitle,
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .clip(RoundedCornerShape(20)),
                                    )

                                    SmallWidthSpacer()

                                    Column(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .weight(1f)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        ) {
                                            CustomText(
                                                text = songTitle,
                                                color = titleColor,
                                                weight = titleWeight
                                            )
                                        }
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .fillMaxHeight(0.5f)
                                        ) {
                                            CustomText(
                                                text = songArtist,
                                            )
                                        }
                                    }


                                    Column(
                                        verticalArrangement = Arrangement.Center,
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .wrapContentWidth()
                                    ) {

                                        if (onEditMode) {

                                            Icon(
                                                painter = painterResource(id = R.drawable.icon_remove_regular),
                                                contentDescription = "",
                                                tint = MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier
                                                    .fillMaxHeight()
                                                    .width(25.dp)
                                                    .clickable {

                                                        playlistVM.removeSong(song)
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
        }
    }

    /*


    mainVM.onPlaylistImageSelected = { bitmapString ->

        mainVM.setCurrentPlaylistImageString(bitmapString)
    }

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



                        }

                        Spacer(modifier = Modifier.height(10.dp))




                        Spacer(modifier = Modifier.height(20.dp))
                    }
                },
                content = {

                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {



                        Spacer(modifier = Modifier.height(20.dp))


                    }
                }
            )
        }
    }

     */
}

fun Modifier.modifyIf(condition: Boolean, modify: Modifier.() -> Modifier) =
    if (condition) modify() else this
