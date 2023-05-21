package com.lighttigerxiv.simple.mp.compose.screens.main.playlists.playlist

import android.graphics.BitmapFactory
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
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
import com.lighttigerxiv.simple.mp.compose.activities.main.MainVM
import com.lighttigerxiv.simple.mp.compose.data.variables.SCREEN_PADDING
import com.lighttigerxiv.simple.mp.compose.data.variables.SMALL_SPACING
import com.lighttigerxiv.simple.mp.compose.ui.composables.text.TitleMedium
import com.lighttigerxiv.simple.mp.compose.functions.getAppString
import com.lighttigerxiv.simple.mp.compose.screens.main.playlists.PlaylistsScreenVM
import com.lighttigerxiv.simple.mp.compose.ui.composables.*
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.*
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
    onGetImage: () -> Unit,
    onAddSongs: () -> Unit
) {

    //States

    val selectImageScaffoldState = rememberBottomSheetScaffoldState()

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    //Variables

    val surfaceColor = mainVM.surfaceColor.collectAsState().value

    val surfaceVariantColor = MaterialTheme.colorScheme.surfaceVariant

    val screenLoaded = playlistVM.screenLoaded.collectAsState().value

    val playlist = playlistVM.playlist.collectAsState().value

    val playlistImage = playlistVM.playlistImage.collectAsState().value

    val tintImage = playlistVM.tintImage.collectAsState().value

    val songs = playlistVM.currentSongs.collectAsState().value

    val showMenu = playlistVM.showMenu.collectAsState().value

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
    ) {

        BottomSheetScaffold(
            scaffoldState = selectImageScaffoldState,
            sheetPeekHeight = 0.dp,
            sheetShape = RoundedCornerShape(
                topStart = 14.dp,
                topEnd = 14.dp
            ),
            sheetContent = {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(surfaceColor)
                        .padding(SCREEN_PADDING)
                ) {

                    TitleMedium(
                        text = stringResource(id = R.string.SelectImage)
                    )

                    MediumHeightSpacer()

                    Row(
                        modifier = Modifier
                            .clickable {
                                onGetImage()

                                scope.launch {

                                    selectImageScaffoldState.bottomSheetState.collapse()
                                }
                            }
                    ) {

                        SmallIcon(
                            id = R.drawable.select,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        SmallHorizontalSpacer()

                        CustomText(
                            text = stringResource(id = R.string.SelectImage)
                        )
                    }

                    SmallHeightSpacer()

                    Row(
                        modifier = Modifier
                            .clickable {

                                scope.launch {

                                    playlistVM.deleteImage()

                                    selectImageScaffoldState.bottomSheetState.collapse()
                                }
                            }
                    ) {

                        SmallIcon(
                            id = R.drawable.trash,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        SmallHorizontalSpacer()

                        CustomText(
                            text = stringResource(id = R.string.DeleteImage)
                        )
                    }
                }
            }
        ) { bottomSheetScaffoldPadding ->

            VerticalNestedScrollView(
                modifier = Modifier
                    .background(surfaceColor)
                    .padding(bottomSheetScaffoldPadding)
                    .padding(SCREEN_PADDING),
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
                                        .height(40.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .clickable {

                                            when (onEditMode) {

                                                true -> {

                                                    scope.launch {

                                                        playlistVM.cancelEdit()

                                                        playlistVM.updateOnEditMode(false)
                                                    }
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
                                        .height(40.dp)
                                        .width(40.dp)
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
                                                CustomText(
                                                    text = stringResource(id = R.string.AddSongs)
                                                )
                                            },
                                            onClick = {

                                                playlistVM.updateShowMenu(false)

                                                onAddSongs()
                                            }
                                        )

                                        DropdownMenuItem(
                                            text = {
                                                CustomText(
                                                    text = stringResource(id = R.string.EditPlaylist)
                                                )
                                            },
                                            onClick = {

                                                playlistVM.updateShowMenu(false)

                                                playlistVM.updateOnEditMode(true)
                                            }
                                        )

                                        DropdownMenuItem(
                                            text = {
                                                CustomText(
                                                    text = stringResource(id = R.string.DeletePlaylist)
                                                )
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
                                                        .padding(SCREEN_PADDING)
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

                                                        SmallHorizontalSpacer()

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
                                    colorFilter = if (tintImage) {
                                        ColorFilter.tint(MaterialTheme.colorScheme.primary)
                                    } else {
                                        null
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth(0.6f)
                                        .aspectRatio(1f)
                                        .padding(5.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .modifyIf(tintImage) {
                                            background(surfaceVariantColor)
                                        }
                                        .modifyIf(onEditMode) {
                                            clickable {
                                                scope.launch {
                                                    selectImageScaffoldState.bottomSheetState.expand()
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

                    Column {
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

                                    Column {
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
                                                bitmap = remember { songAlbumArt ?: BitmapFactory.decodeResource(context.resources, R.drawable.record) }.asImageBitmap(),
                                                colorFilter = if (songAlbumArt == null) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null,
                                                contentDescription = songTitle,
                                                modifier = Modifier
                                                    .fillMaxHeight()
                                                    .clip(RoundedCornerShape(20)),
                                            )

                                            SmallHorizontalSpacer()

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

                                        XSmallHeightSpacer()
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

fun Modifier.modifyIf(condition: Boolean, modify: Modifier.() -> Modifier) =
    if (condition) modify() else this
