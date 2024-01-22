package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.playlists

import android.graphics.Bitmap
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Playlist
import com.lighttigerxiv.simple.mp.compose.frontend.composables.Card
import com.lighttigerxiv.simple.mp.compose.frontend.composables.DoubleTabRow
import com.lighttigerxiv.simple.mp.compose.frontend.composables.IconDialog
import com.lighttigerxiv.simple.mp.compose.frontend.composables.MiniPlayerSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.composables.TextField
import com.lighttigerxiv.simple.mp.compose.frontend.composables.VSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goToGenrePlaylist
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goToUserPlaylist
import com.lighttigerxiv.simple.mp.compose.frontend.utils.Sizes
import com.lighttigerxiv.simple.mp.compose.frontend.utils.customRememberLazyGridState
import com.lighttigerxiv.simple.mp.compose.frontend.utils.customRememberPagerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlaylistsScreen(
    navController: NavHostController,
    vm: PlaylistsScreenVM = viewModel(factory = PlaylistsScreenVM.Factory),
    inLandscape: Boolean,
    showMiniPlayer: Boolean
) {
    val uiState = vm.uiState.collectAsState().value
    val pagerState = customRememberPagerState(index = vm.pagerTab, onKill = {vm.pagerTab = it})
    val genrePlaylistsState = customRememberLazyGridState(index = vm.genrePlaylistsGridPosition, onKill = { vm.genrePlaylistsGridPosition = it })
    val playlistsState = customRememberLazyGridState(index = vm.playlistsPositionGridPosition, onKill = { vm.playlistsPositionGridPosition = it })
    val gridCellsCount by remember { mutableIntStateOf(if (inLandscape) 5 else 2) }

    Column {
        DoubleTabRow(
            pagerState = pagerState,
            firstTabTitle = stringResource(id = R.string.genres),
            secondTabTitle = stringResource(id = R.string.playlists),
        )

        VSpacer(size = Sizes.LARGE)

        HorizontalPager(
            state = pagerState
        ) { tabPosition ->
            when (tabPosition) {
                0 -> {
                    LazyVerticalGrid(
                        modifier = Modifier.fillMaxSize(),
                        state = genrePlaylistsState,
                        columns = GridCells.Fixed(gridCellsCount),
                        verticalArrangement = Arrangement.spacedBy(Sizes.SMALL),
                        horizontalArrangement = Arrangement.spacedBy(Sizes.SMALL)
                    ) {
                        items(
                            items = uiState.genrePlaylists,
                            key = { "genre-${it._id}" }
                        ) { playlist ->

                            Card(
                                defaultIconId = R.drawable.playlist_filled,
                                text = playlist.name,
                                onClick = { navController.goToGenrePlaylist(playlist._id) }
                            )
                        }

                        item(span = { GridItemSpan(gridCellsCount) }) {
                            MiniPlayerSpacer(isShown = showMiniPlayer)
                        }
                    }
                }

                1 -> {

                    Column(modifier = Modifier.fillMaxSize()) {

                        TextField(
                            text = uiState.searchText,
                            placeholder = stringResource(id = R.string.search_playlists),
                            onTextChange = { vm.updateUserPlaylistsSearchText(it) },
                            startIcon = R.drawable.plus,
                            onStartIconClick = { vm.updateShowAddPlaylistDialog(true) }
                        )

                        VSpacer(size = Sizes.LARGE)

                        LazyVerticalGrid(
                            state = playlistsState,
                            columns = GridCells.Fixed(gridCellsCount),
                            verticalArrangement = Arrangement.spacedBy(Sizes.SMALL),
                            horizontalArrangement = Arrangement.spacedBy(Sizes.SMALL),
                            content = {
                                items(
                                    items = uiState.userPlaylists,
                                    key = { "userPlaylist-${it._id}" }
                                ) { playlist ->

                                    PlaylistCard(
                                        playlist = playlist,
                                        vm = vm,
                                        navController = navController
                                    )
                                }

                                item(span = { GridItemSpan(gridCellsCount) }) {
                                    MiniPlayerSpacer(isShown = showMiniPlayer)
                                }
                            }
                        )
                    }

                    IconDialog(
                        show = uiState.showAddPlaylistDialog,
                        onDismiss = {
                            vm.updateAddPlaylistDialogText("")
                            vm.updateShowAddPlaylistDialog(false)
                        },
                        iconId = R.drawable.plus,
                        title = stringResource(id = R.string.add_playlist),
                        primaryButtonText = stringResource(id = R.string.add),
                        disablePrimaryButton = uiState.addPlaylistDialogText.trim().isEmpty(),
                        onPrimaryButtonClick = {
                            vm.updateShowAddPlaylistDialog(false)
                            vm.addPlaylist()
                        }
                    ) {

                        TextField(
                            text = uiState.addPlaylistDialogText,
                            onTextChange = { vm.updateAddPlaylistDialogText(it) },
                            placeholder = stringResource(id = R.string.playlist_name)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun PlaylistCard(
    playlist: Playlist,
    vm: PlaylistsScreenVM,
    navController: NavHostController
) {

    val image = remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(image) {
        withContext(Dispatchers.IO) {
            image.value = vm.getPlaylistArt(playlist._id)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Sizes.XLARGE))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { navController.goToUserPlaylist(playlist._id) }
            .padding(Sizes.SMALL),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(Sizes.MEDIUM))
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {

            if (image.value != null) {
                Image(
                    modifier = Modifier
                        .fillMaxSize(),
                    bitmap = image.value!!.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Sizes.LARGE),
                    painter = painterResource(id = R.drawable.playlist_filled),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        VSpacer(size = Sizes.SMALL)

        Text(
            text = playlist.name,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}