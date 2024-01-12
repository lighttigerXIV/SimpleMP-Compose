package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.playlists.playlist

import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.lighttigerxiv.layout_scaffold.inLandscape
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.frontend.composables.CollapsableHeader
import com.lighttigerxiv.simple.mp.compose.frontend.composables.HSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.composables.IconDialog
import com.lighttigerxiv.simple.mp.compose.frontend.composables.MenuItem
import com.lighttigerxiv.simple.mp.compose.frontend.composables.MiniPlayerSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.composables.PlayShuffleRow
import com.lighttigerxiv.simple.mp.compose.frontend.composables.PrimaryButton
import com.lighttigerxiv.simple.mp.compose.frontend.composables.RemoveFromPlaylistSongCard
import com.lighttigerxiv.simple.mp.compose.frontend.composables.SongCard
import com.lighttigerxiv.simple.mp.compose.frontend.composables.TextField
import com.lighttigerxiv.simple.mp.compose.frontend.composables.Toolbar
import com.lighttigerxiv.simple.mp.compose.frontend.composables.VSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goBack
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goToAddSongsToPlaylist
import com.lighttigerxiv.simple.mp.compose.frontend.utils.FontSizes
import com.lighttigerxiv.simple.mp.compose.frontend.utils.Sizes
import com.lighttigerxiv.simple.mp.compose.frontend.utils.modifyIf
import org.mongodb.kbson.ObjectId

@Composable
fun PlaylistScreen(
    playlistId: ObjectId,
    navController: NavHostController,
    rootController: NavHostController,
    vm: PlaylistScreenVM = viewModel(factory = PlaylistScreenVM.Factory),
    showMiniPlayer: Boolean
) {

    val uiState = vm.uiState.collectAsState().value



    LaunchedEffect(uiState.requestedLoading) {
        if (!uiState.requestedLoading) {
            vm.load(playlistId)
        }
    }

    Column {
        PlaylistToolbar(
            playlistId = playlistId,
            uiState = uiState,
            vm = vm,
            navController = navController,
            rootController = rootController
        )

        if (!uiState.loading) {

            if (inLandscape()) {

                VSpacer(size = Sizes.LARGE)

                Row(Modifier.fillMaxWidth()) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .weight(0.2f, fill = true)
                    ) {

                        PlaylistArtAndName(uiState = uiState, vm = vm)
                    }

                    HSpacer(size = Sizes.LARGE)

                    Column(
                        Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = true)
                    ) {

                        Songs(vm = vm, uiState = uiState, showMiniPlayer = showMiniPlayer)
                    }
                }

            } else {

                CollapsableHeader(
                    header = {
                        PlaylistArtAndName(uiState = uiState, vm = vm)
                    }
                ) {

                    VSpacer(size = Sizes.LARGE)

                    Songs(vm = vm, uiState = uiState, showMiniPlayer = showMiniPlayer)
                }
            }
        }

        DeletePlaylistDialog(playlistId = playlistId, uiState = uiState, vm = vm, navController = navController)
        EditArtDialog(uiState = uiState, vm = vm)
        EditNameDialog(uiState = uiState, vm = vm)
    }
}

@Composable
fun PlaylistToolbar(
    playlistId: ObjectId,
    uiState: PlaylistScreenVM.UiState,
    vm: PlaylistScreenVM,
    navController: NavHostController,
    rootController: NavHostController
) {

    if (uiState.inEditMode) {
        Row {

            PrimaryButton(
                text = stringResource(id = R.string.cancel),
                onClick = { vm.cancelEditMode() }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = true)
            )

            PrimaryButton(
                text = stringResource(id = R.string.save),
                onClick = { vm.save() }
            )
        }
    } else {
        Toolbar(navController = navController) {
            Icon(
                modifier = Modifier
                    .size(30.dp)
                    .clickable { vm.updateShowMenu(true) },
                painter = painterResource(id = R.drawable.menu),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Column {
                Menu(playlistId = playlistId, uiState = uiState, vm = vm, rootController = rootController)
            }
        }
    }
}

@Composable
fun Menu(
    playlistId: ObjectId,
    uiState: PlaylistScreenVM.UiState,
    vm: PlaylistScreenVM,
    rootController: NavHostController
) {

    DropdownMenu(
        modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant),
        expanded = uiState.showMenu,
        onDismissRequest = { vm.updateShowMenu(false) }
    ) {

        MenuItem(
            iconId = R.drawable.plus,
            text = stringResource(id = R.string.add_songs),
            onClick = {
                vm.updateShowMenu(false)
                rootController.goToAddSongsToPlaylist(playlistId)
            }
        )

        MenuItem(
            iconId = R.drawable.edit,
            text = stringResource(id = R.string.edit),
            onClick = { vm.updateEditMode(true) }
        )

        MenuItem(
            iconId = R.drawable.trash,
            text = stringResource(id = R.string.delete),
            onClick = {
                vm.updateShowMenu(false)
                vm.updateShowDeleteDialog(true)
            }
        )
    }
}

@Composable
fun Songs(
    vm: PlaylistScreenVM,
    uiState: PlaylistScreenVM.UiState,
    showMiniPlayer: Boolean
) {
    if (!uiState.inEditMode) {

        if (uiState.songs.isNotEmpty()) {
            PlayShuffleRow(
                onPlayClick = { vm.playSong(uiState.songs[0]) },
                onShuffleClick = { vm.shuffleAndPlay() }
            )

            VSpacer(size = Sizes.LARGE)

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(Sizes.SMALL)
            ) {
                items(
                    items = uiState.songs,
                    key = { it.id }
                ) { song ->
                    SongCard(
                        song = song,
                        artistName = vm.getArtistName(song.artistId),
                        art = vm.getAlbumArt(song.albumId),
                        highlight = uiState.currentSong?.id == song.id,
                        onClick = { vm.playSong(song) }
                    )
                }

                item {
                    MiniPlayerSpacer(isShown = showMiniPlayer)
                }
            }
        } else {
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.no_songs_added),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    } else {

        VSpacer(size = Sizes.LARGE)

        if(uiState.songs.isNotEmpty()){

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(Sizes.SMALL)
            ) {
                items(
                    items = uiState.songs,
                    key = { it.id }
                ) { song ->
                    RemoveFromPlaylistSongCard(
                        song = song,
                        artistName = vm.getArtistName(song.artistId),
                        art = vm.getAlbumArt(song.albumId),
                        onClick = { vm.removeSong(song.id) }
                    )
                }

                item {
                    MiniPlayerSpacer(isShown = showMiniPlayer)
                }
            }
        }else{

            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.no_songs_added),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun DeletePlaylistDialog(
    playlistId: ObjectId,
    uiState: PlaylistScreenVM.UiState,
    vm: PlaylistScreenVM,
    navController: NavHostController
) {

    IconDialog(
        show = uiState.showDeletePlaylistDialog,
        onDismiss = { vm.updateShowDeleteDialog(false) },
        iconId = R.drawable.trash,
        title = stringResource(id = R.string.delete_playlist),
        primaryButtonText = stringResource(id = R.string.delete),
        onPrimaryButtonClick = {
            vm.updateShowDeleteDialog(false)
            vm.deletePlaylist(playlistId)
            navController.goBack()
        }
    ) {
        Text(
            text = stringResource(id = R.string.confirm_delete_playlist),
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun EditNameDialog(
    uiState: PlaylistScreenVM.UiState,
    vm: PlaylistScreenVM,
) {

    IconDialog(
        show = uiState.showEditNameDialog,
        onDismiss = {
            vm.updateShowEditNameDialog(false)
            vm.updateEditNameText(uiState.playlistName)
        },
        iconId = R.drawable.edit,
        title = stringResource(id = R.string.edit_name),
        primaryButtonText = stringResource(id = R.string.save),
        onPrimaryButtonClick = {
            vm.saveEditName()
        },
        disablePrimaryButton = uiState.editNameText.trim().isEmpty()
    ) {
        TextField(
            text = uiState.editNameText,
            onTextChange = { vm.updateEditNameText(it) },
            placeholder = stringResource(id = R.string.playlist_name)
        )
    }
}

@Composable
fun EditArtDialog(
    uiState: PlaylistScreenVM.UiState,
    vm: PlaylistScreenVM
) {

    val context = LocalContext.current
    val askImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {

                val art = if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                } else {
                    ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
                }

                vm.updatePlaylistArt(art)
            }
        })

    IconDialog(
        show = uiState.showEditArtDialog,
        onDismiss = { vm.updateShowEditArtDialog(false) },
        iconId = R.drawable.edit,
        title = stringResource(id = R.string.edit_art)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    askImageLauncher.launch("image/*")
                    vm.updateShowEditArtDialog(false)
                }
                .padding(Sizes.LARGE),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                modifier = Modifier.size(30.dp),
                painter = painterResource(id = R.drawable.image),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )

            HSpacer(size = Sizes.LARGE)

            Text(
                text = stringResource(id = R.string.select_image),
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    vm.deletePlaylistArt()
                }
                .padding(Sizes.LARGE),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                modifier = Modifier.size(30.dp),
                painter = painterResource(id = R.drawable.trash),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )

            HSpacer(size = Sizes.LARGE)

            Text(
                text = stringResource(id = R.string.use_default),
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}

@Composable
fun PlaylistArtAndName(
    uiState: PlaylistScreenVM.UiState,
    vm: PlaylistScreenVM
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        VSpacer(size = Sizes.LARGE)

        if (uiState.playlistArt == null) {

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(Sizes.XLARGE))
                    .modifyIf(uiState.inEditMode) { clickable { vm.updateShowEditArtDialog(true) } }
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(Sizes.MEDIUM)
            ) {
                Icon(
                    modifier = Modifier
                        .modifyIf(inLandscape()) {
                            fillMaxWidth()
                            aspectRatio(1f)
                        }
                        .modifyIf(!inLandscape()) {
                            size(220.dp)
                        },
                    painter = painterResource(id = R.drawable.playlist_filled),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        } else {
            Column(
                Modifier
                    .modifyIf(inLandscape()) {
                        fillMaxWidth()
                        aspectRatio(1f)
                    }
                    .modifyIf(!inLandscape()) {
                        size(220.dp)
                    }
                    .clip(RoundedCornerShape(Sizes.XLARGE))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .modifyIf(uiState.inEditMode) { clickable { vm.updateShowEditArtDialog(true) } },
                    bitmap = uiState.playlistArt.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
        }

        VSpacer(size = Sizes.LARGE)

        Text(
            modifier = Modifier
                .modifyIf(uiState.inEditMode) {
                    clickable {
                        vm.updateShowEditNameDialog(true)
                    }
                },
            text = uiState.playlistName,
            fontSize = FontSizes.HEADER,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}