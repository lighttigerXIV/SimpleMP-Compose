package com.lighttigerxiv.simple.mp.compose.screens.main.playlists.playlist

import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import com.lighttigerxiv.simple.mp.compose.*
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.activities.main.MainVM
import com.lighttigerxiv.simple.mp.compose.data.data_classes.Song
import com.lighttigerxiv.simple.mp.compose.data.mongodb.items.Playlist
import com.lighttigerxiv.simple.mp.compose.data.variables.ImageSizes
import com.lighttigerxiv.simple.mp.compose.data.variables.SCREEN_PADDING
import com.lighttigerxiv.simple.mp.compose.data.variables.SMALL_SPACING
import com.lighttigerxiv.simple.mp.compose.ui.composables.text.TitleMedium
import com.lighttigerxiv.simple.mp.compose.functions.getAppString
import com.lighttigerxiv.simple.mp.compose.functions.getImage
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
    vm: PlaylistScreenVM,
    activityContext: ViewModelStoreOwner,
    navController: NavHostController,
    rootNavController: NavHostController,
    playlistID: String,
    onGetImage: () -> Unit
) {

    val selectImageScaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()
    val inPortrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
    val surfaceColor = mainVM.surfaceColor.collectAsState().value
    val surfaceVariantColor = MaterialTheme.colorScheme.surfaceVariant
    val screenLoaded = vm.screenLoaded.collectAsState().value
    val playlist = vm.playlist.collectAsState().value
    val playlistImage = vm.playlistImage.collectAsState().value
    val tintImage = vm.tintImage.collectAsState().value
    val songs = vm.currentSongs.collectAsState().value
    val showMenu = vm.showMenu.collectAsState().value
    val showDeleteDialog = vm.showDeleteDialog.collectAsState().value
    val saveButtonEnabled = vm.saveButtonEnabled.collectAsState().value
    val onEditMode = vm.onEditMode.collectAsState().value
    val playlistNameText = vm.playlistNameText.collectAsState().value
    val currentSong = mainVM.currentSong.collectAsState().value

    if (!screenLoaded) {
        vm.loadScreen(playlistID, mainVM, playlistsVM)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(surfaceColor)
    ) {

        BottomSheetScaffold(
            scaffoldState = selectImageScaffoldState,
            sheetPeekHeight = 0.dp,
            backgroundColor = surfaceColor,
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

                    MediumVerticalSpacer()

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

                    SmallVerticalSpacer()

                    Row(
                        modifier = Modifier
                            .clickable {

                                scope.launch {

                                    vm.deleteImage()
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

            if (inPortrait) {
                VerticalNestedScrollView(
                    modifier = Modifier
                        .background(surfaceColor)
                        .padding(bottomSheetScaffoldPadding)
                        .padding(SCREEN_PADDING),
                    state = rememberNestedScrollViewState(),
                    header = {

                        if (screenLoaded) {

                            Column {

                                PlaylistToolbar(
                                    vm,
                                    playlistsVM,
                                    activityContext,
                                    navController,
                                    rootNavController,
                                    playlistID,
                                    onEditMode,
                                    saveButtonEnabled,
                                    showMenu,
                                    showDeleteDialog
                                )

                                MediumVerticalSpacer()

                                PlaylistCoverAndName(
                                    vm,
                                    playlistImage,
                                    selectImageScaffoldState,
                                    true,
                                    tintImage,
                                    onEditMode,
                                    surfaceVariantColor,
                                    playlistNameText,
                                    playlist
                                )
                            }
                        }
                    }
                ) {
                    PlaylistSongs(
                        vm,
                        mainVM,
                        songs,
                        onEditMode,
                        surfaceColor,
                        surfaceVariantColor,
                        currentSong,
                        //songsCovers
                    )
                }
            } else {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(SCREEN_PADDING)
                ) {
                    PlaylistToolbar(
                        vm,
                        playlistsVM,
                        activityContext,
                        navController,
                        rootNavController,
                        playlistID,
                        onEditMode,
                        saveButtonEnabled,
                        showMenu,
                        showDeleteDialog
                    )

                    MediumVerticalSpacer()

                    if (screenLoaded) {

                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {

                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth()
                                    .weight(0.4f, fill = true),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {

                                PlaylistCoverAndName(
                                    vm,
                                    playlistImage,
                                    selectImageScaffoldState,
                                    false,
                                    tintImage,
                                    onEditMode,
                                    surfaceVariantColor,
                                    playlistNameText,
                                    playlist
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth()
                                    .weight(0.6f, fill = true)
                            ) {

                                PlaylistSongs(
                                    vm,
                                    mainVM,
                                    songs,
                                    onEditMode,
                                    surfaceColor,
                                    surfaceVariantColor,
                                    currentSong
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun PlaylistToolbar(
    vm: PlaylistScreenVM,
    playlistsVM: PlaylistsScreenVM,
    activityContext: ViewModelStoreOwner,
    navController: NavHostController,
    rootNavController: NavHostController,
    playlistID: String,
    onEditMode: Boolean,
    saveButtonEnabled: Boolean,
    showMenu: Boolean,
    showDeleteDialog: Boolean
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

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

                                vm.cancelEdit()
                                vm.updateOnEditMode(false)
                            }
                        }

                        false -> {
                            navController.navigateUp()
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

                                    vm.savePlaylistChanges(playlistsVM)

                                    vm.updateOnEditMode(false)
                                }
                            }

                            false -> {
                                vm.updateShowMenu(true)
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
                    vm.updateShowMenu(false)
                }
            ) {

                DropdownMenuItem(
                    text = {
                        CustomText(
                            text = stringResource(id = R.string.AddSongs)
                        )
                    },
                    onClick = {

                        vm.updateShowMenu(false)
                        vm.openAddSongsScreen(activityContext, rootNavController, playlistID)
                    }
                )

                DropdownMenuItem(
                    text = {
                        CustomText(
                            text = stringResource(id = R.string.EditPlaylist)
                        )
                    },
                    onClick = {

                        vm.updateShowMenu(false)

                        vm.updateOnEditMode(true)
                    }
                )

                DropdownMenuItem(
                    text = {
                        CustomText(
                            text = stringResource(id = R.string.DeletePlaylist)
                        )
                    },
                    onClick = {

                        vm.updateShowMenu(false)

                        vm.updateShowDeleteDialog(true)
                    }
                )
            }

            if (showDeleteDialog) {

                Dialog(
                    onDismissRequest = {
                        vm.updateShowDeleteDialog(false)
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

                            SmallVerticalSpacer()

                            Text(
                                text = stringResource(id = R.string.ConfirmDeletePlaylist)
                            )

                            MediumVerticalSpacer()

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
                                        vm.updateShowDeleteDialog(false)
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
                                            navController.navigateUp()
                                            vm.deletePlaylist(playlistID, playlistsVM)
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
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlaylistCoverAndName(
    vm: PlaylistScreenVM,
    playlistImage: ImageBitmap?,
    selectImageScaffoldState: BottomSheetScaffoldState,
    inPortrait: Boolean,
    tintImage: Boolean,
    onEditMode: Boolean,
    surfaceVariantColor: Color,
    playlistNameText: String,
    playlist: Playlist?,

    ) {

    val scope = rememberCoroutineScope()


    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {

            if (inPortrait) {
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
            } else {
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
                        .fillMaxHeight(0.7f)
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
        }

        SmallVerticalSpacer()

        when (onEditMode) {

            true -> {

                CustomTextField(
                    text = playlistNameText,
                    placeholder = stringResource(id = R.string.InsertPlaylistName),
                    onTextChange = {

                        vm.updatePlaylistNameText(it)

                        vm.updateSaveButtonEnabled(it.isNotEmpty())
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

        MediumVerticalSpacer()
    }

}


@Composable
fun PlaylistSongs(
    vm: PlaylistScreenVM,
    mainVM: MainVM,
    songs: List<Song>?,
    onEditMode: Boolean,
    surfaceColor: Color,
    surfaceVariantColor: Color,
    currentSong: Song?,
    //songsCovers: List<SongCover>?
) {

    val context = LocalContext.current

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

            MediumVerticalSpacer()

            LazyColumn(
                content = {

                    itemsIndexed(
                        songs,
                        key = { _, song -> song.id }
                    ) { index, song ->

                        val songTitle = remember { song.title }
                        val songArtist = remember { mainVM.getSongArtist(song).name }
                        val highlight = song.path == currentSong?.path
                        val songAlbumArt = remember { mainVM.getSongArt(song) }


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
                                    bitmap = remember { songAlbumArt ?: getImage(context, R.drawable.cd, ImageSizes.LARGE) }.asImageBitmap(),
                                    colorFilter = if (songAlbumArt == null) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null,
                                    contentDescription = songTitle,
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(20))
                                        .modifyIf(songAlbumArt == null) {
                                            background(surfaceVariantColor)
                                        }
                                        .modifyIf(songAlbumArt == null) {
                                            padding(5.dp)
                                        },
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

                                                    vm.removeSong(song)
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


fun Modifier.modifyIf(condition: Boolean, modify: Modifier.() -> Modifier) =
    if (condition) modify() else this
