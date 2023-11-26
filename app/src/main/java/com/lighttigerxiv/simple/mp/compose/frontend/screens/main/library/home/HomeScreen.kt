package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.frontend.composables.MenuItem
import com.lighttigerxiv.simple.mp.compose.frontend.composables.SongCard
import com.lighttigerxiv.simple.mp.compose.frontend.composables.TextField
import com.lighttigerxiv.simple.mp.compose.frontend.composables.VSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goToAbout
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goToSettings
import com.lighttigerxiv.simple.mp.compose.frontend.utils.Sizes

@Composable
fun HomeScreen(
    rootController: NavHostController,
    vm: HomeScreenVM = viewModel(factory = HomeScreenVM.Factory)
) {

    val uiState = vm.uiSate.collectAsState().value

    Scaffold(
        floatingActionButton = {
            if (uiState.songs.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable { vm.shuffleAndPlay() }
                        .padding(Sizes.LARGE),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        modifier = Modifier.size(30.dp),
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

            LazyColumn {
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
                            onClick = { vm.playSong(song) }
                        )

                        VSpacer(size = Sizes.SMALL)
                    }
                }
            }
        }
    }
}

@Composable
fun Menu(
    vm: HomeScreenVM,
    uiState: HomeUiState,
    rootController: NavHostController
) {

    DropdownMenu(
        expanded = uiState.showMenu,
        onDismissRequest = { vm.updateShowMenu(false) }
    ) {
        MenuItem(
            iconId = R.drawable.sort,
            text = stringResource(id = R.string.sort_by_default),
            onClick = { /*TODO*/ }
        )

        MenuItem(
            iconId = R.drawable.date_sort,
            text = stringResource(id = R.string.sort_by_modification_date),
            onClick = { /*TODO*/ }
        )

        MenuItem(
            iconId = R.drawable.alphabetic_sort,
            text = stringResource(id = R.string.sort_alphabetically),
            onClick = { /*TODO*/ }
        )

        Divider(Modifier.height(1.dp), color = MaterialTheme.colorScheme.surfaceVariant)

        MenuItem(
            iconId = R.drawable.reload,
            text = stringResource(id = R.string.reindex_library),
            onClick = { vm.indexLibrary() },
            disabled = uiState.indexingLibrary
        )

        Divider(Modifier.height(1.dp), color = MaterialTheme.colorScheme.surfaceVariant)

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