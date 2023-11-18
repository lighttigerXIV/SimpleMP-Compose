package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.lighttigerxiv.simple.mp.compose.frontend.composables.SongCard
import com.lighttigerxiv.simple.mp.compose.frontend.composables.TextField
import com.lighttigerxiv.simple.mp.compose.frontend.composables.VSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.utils.Sizes

@Composable
fun HomeScreen(
    navController: NavHostController,
    vm: HomeScreenVM = viewModel(factory = HomeScreenVM.Factory)
) {

    val uiState = vm.uiSate.collectAsState().value

    Scaffold(
        floatingActionButton = {
            if(uiState.songs.isNotEmpty()){
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
                .padding(paddingValues)
        ) {

            TextField(
                text = uiState.searchText,
                onTextChange = { vm.updateSearchText(it) },
                placeholder = stringResource(id = R.string.search_songs),
                startIcon = R.drawable.menu,
                onStartIconClick = { vm.updateShowMenu(true) }
            )

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