package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.home

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.backend.settings.SettingsOptions
import com.lighttigerxiv.simple.mp.compose.frontend.composables.MenuItem
import com.lighttigerxiv.simple.mp.compose.frontend.composables.MiniPlayerSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.composables.SongCard
import com.lighttigerxiv.simple.mp.compose.frontend.composables.SongMenuItem
import com.lighttigerxiv.simple.mp.compose.frontend.composables.TextField
import com.lighttigerxiv.simple.mp.compose.frontend.composables.VSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goToAbout
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goToAddSongToPlaylist
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goToPreviewAlbum
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goToPreviewArtist
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goToSettings
import com.lighttigerxiv.simple.mp.compose.frontend.utils.Sizes
import com.lighttigerxiv.simple.mp.compose.frontend.utils.modifyIf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun HomeScreen(
    rootController: NavHostController,
    vm: HomeScreenVM = viewModel(factory = HomeScreenVM.Factory)
) {

    val uiState = vm.uiSate.collectAsState().value
    val lazyColumnState = rememberLazyListState()
    val context = LocalContext.current

    LaunchedEffect(uiState.sortType) {
        withContext(Dispatchers.Main) {
            if (uiState.songs.isNotEmpty()) {
                lazyColumnState.scrollToItem(0)
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            if (uiState.songs.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .modifyIf(uiState.withMiniPlayer) {
                            offset(y = (-65).dp)
                        }
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable { vm.shuffleAndPlay() }
                        .padding(Sizes.LARGE),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        modifier = Modifier
                            .size(30.dp),
                        painter = painterResource(id = R.drawable.shuffle),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(paddingValues)
        ) {

            Column {

                TextField(
                    text = uiState.searchText,
                    onTextChange = { vm.updateSearchText(it) },
                    placeholder = stringResource(id = R.string.search_songs),
                    startIcon = R.drawable.menu,
                    onStartIconClick = {
                        vm.updateShowMenu(true)
                    }
                )
                Menu(vm = vm, uiState = uiState, rootController)
            }

            VSpacer(size = Sizes.LARGE)

            LazyColumn(state = lazyColumnState) {
                items(
                    items = uiState.songs,
                    key = { "song-${it.id}" }
                ) { song ->

                    Column {

                        SongCard(
                            song = song,
                            art = vm.getSongArt(song.albumId),
                            artistName = vm.getArtistName(song.artistId),
                            highlight = song.id == uiState.currentSong?.id,
                            onClick = { vm.playSong(song) },
                            menuItems = getSongMenuItems(context),
                            onMenuItemClick = { menuItemId ->
                                when (menuItemId) {
                                    "preview_artist" -> rootController.goToPreviewArtist(song.artistId)
                                    "preview_album" -> rootController.goToPreviewAlbum(song.albumId)
                                    "add_to_playlist" -> rootController.goToAddSongToPlaylist(song.id)
                                }
                            }
                        )

                        VSpacer(size = Sizes.SMALL)
                    }
                }

                item {
                    MiniPlayerSpacer(isShown = uiState.withMiniPlayer)
                }
            }
        }
    }
}

fun getSongMenuItems(context: Context): List<SongMenuItem> {
    return listOf(
        SongMenuItem("preview_artist", context.getString(R.string.go_to_artist), R.drawable.artist),
        SongMenuItem("preview_album", context.getString(R.string.go_to_album), R.drawable.album),
        SongMenuItem("add_to_playlist", context.getString(R.string.add_to_playlist), R.drawable.playlist)
    )
}

@Composable
fun Menu(
    vm: HomeScreenVM,
    uiState: HomeUiState,
    rootController: NavHostController
) {

    DropdownMenu(
        modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant),
        expanded = uiState.showMenu,
        onDismissRequest = { vm.updateShowMenu(false) }
    ) {
        MenuItem(
            iconId = R.drawable.sort,
            text = stringResource(id = R.string.sort_by_default),
            onClick = {

                vm.updateShowMenu(false)

                vm.updateSort(
                    if (uiState.sortType == SettingsOptions.Sort.DEFAULT_REVERSED)
                        SettingsOptions.Sort.DEFAULT
                    else
                        SettingsOptions.Sort.DEFAULT_REVERSED
                )
            }
        )

        MenuItem(
            iconId = R.drawable.date_sort,
            text = stringResource(id = R.string.sort_by_modification_date),
            onClick = {

                vm.updateShowMenu(false)

                vm.updateSort(
                    if (uiState.sortType == SettingsOptions.Sort.MODIFICATION_DATE_RECENT)
                        SettingsOptions.Sort.MODIFICATION_DATE_OLD
                    else
                        SettingsOptions.Sort.MODIFICATION_DATE_RECENT
                )
            }
        )

        MenuItem(
            iconId = R.drawable.date_sort,
            text = stringResource(id = R.string.sort_by_year),
            onClick = {

                vm.updateShowMenu(false)

                vm.updateSort(
                    if (uiState.sortType == SettingsOptions.Sort.YEAR_RECENT)
                        SettingsOptions.Sort.YEAR_OLD
                    else
                        SettingsOptions.Sort.YEAR_RECENT
                )
            }
        )

        MenuItem(
            iconId = R.drawable.alphabetic_sort,
            text = stringResource(id = R.string.sort_alphabetically),
            onClick = {

                vm.updateShowMenu(false)

                vm.updateSort(
                    if (uiState.sortType == SettingsOptions.Sort.ALPHABETICALLY_ASCENDENT)
                        SettingsOptions.Sort.ALPHABETICALLY_DESCENDENT
                    else
                        SettingsOptions.Sort.ALPHABETICALLY_ASCENDENT
                )
            }
        )

        Divider(Modifier.height(1.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)

        MenuItem(
            iconId = R.drawable.reload,
            text = stringResource(id = R.string.reindex_library),
            onClick = { vm.indexLibrary() },
            disabled = uiState.indexingLibrary
        )

        Divider(Modifier.height(1.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)

        MenuItem(
            iconId = R.drawable.settings,
            text = stringResource(id = R.string.settings),
            onClick = {
                vm.updateShowMenu(false)
                rootController.goToSettings()
            }
        )

        MenuItem(
            iconId = R.drawable.info,
            text = stringResource(id = R.string.about),
            onClick = {
                vm.updateShowMenu(false)
                rootController.goToAbout()
            }
        )
    }
}