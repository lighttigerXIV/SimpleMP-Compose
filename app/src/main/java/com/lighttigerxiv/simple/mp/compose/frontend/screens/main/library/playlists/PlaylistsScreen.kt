package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.playlists

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.frontend.composables.Card
import com.lighttigerxiv.simple.mp.compose.frontend.composables.DoubleTabRow
import com.lighttigerxiv.simple.mp.compose.frontend.composables.IconDialog
import com.lighttigerxiv.simple.mp.compose.frontend.composables.TextField
import com.lighttigerxiv.simple.mp.compose.frontend.composables.VSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goToGenrePlaylist
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goToPlaylists
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goToUserPlaylist
import com.lighttigerxiv.simple.mp.compose.frontend.utils.Sizes

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlaylistsScreen(
    navController: NavHostController,
    vm: PlaylistsScreenVM = viewModel(factory = PlaylistsScreenVM.Factory)
) {
    val uiState = vm.uiState.collectAsState().value
    val pagerState = rememberPagerState(pageCount = { 2 })

    Column {
        DoubleTabRow(
            pagerState = pagerState,
            firstTabTitle = stringResource(id = R.string.genres),
            secondTabTitle = stringResource(id = R.string.playlists)
        )

        VSpacer(size = Sizes.LARGE)

        HorizontalPager(
            state = pagerState
        ) { tabPosition ->
            when (tabPosition) {
                0 -> {
                    LazyVerticalGrid(
                        modifier = Modifier.fillMaxSize(),
                        columns = GridCells.Fixed(2),
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
                    }
                }

                1 -> {

                    Column(modifier = Modifier.fillMaxSize()) {

                        TextField(
                            text = uiState.searchText,
                            placeholder = stringResource(id = R.string.search_playlists),
                            onTextChange = { vm.updateUserPlaylistsSearchText(it) },
                            startIcon = R.drawable.plus,
                            onStartIconClick = {vm.updateShowAddPlaylistDialog(true)}
                        )

                        VSpacer(size = Sizes.LARGE)

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            verticalArrangement = Arrangement.spacedBy(Sizes.SMALL),
                            horizontalArrangement = Arrangement.spacedBy(Sizes.SMALL),
                            content = {
                                items(
                                    items = uiState.userPlaylists,
                                    key = { "userPlaylist-${it._id}" }
                                ){ playlist->

                                    Card(
                                        defaultIconId = R.drawable.playlist_filled,
                                        text = playlist.name,
                                        onClick = {navController.goToUserPlaylist(playlist._id)}
                                    )
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
                        onPrimaryButtonClick = {
                            vm.addPlaylist()
                                               },
                        disablePrimaryButton = uiState.addPlaylistDialogText.trim().isEmpty()
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