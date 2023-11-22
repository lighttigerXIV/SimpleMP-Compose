package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.albums

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.frontend.composables.Card
import com.lighttigerxiv.simple.mp.compose.frontend.composables.TextField
import com.lighttigerxiv.simple.mp.compose.frontend.composables.VSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goToAlbum
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goToArtist
import com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.artists.ArtistsScreenVM
import com.lighttigerxiv.simple.mp.compose.frontend.utils.Sizes

@Composable
fun AlbumsScreen(
    vm: AlbumsScreenVM = viewModel(factory = AlbumsScreenVM.Factory),
    navController: NavHostController
){

    val uiState = vm.uiState.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        TextField(
            text = uiState.searchText,
            onTextChange = { vm.updateSearchText(it) },
            placeholder = stringResource(id = R.string.search_artists),
            startIcon = R.drawable.sort,
            onStartIconClick = {}
        )

        VSpacer(size = Sizes.LARGE)

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(Sizes.SMALL),
            horizontalArrangement = Arrangement.spacedBy(Sizes.SMALL)
        ) {
            items(
                items = uiState.albums,
                key = { it.id }
            ) { album ->

                Card(
                    image = vm.getAlbumArt(album.id),
                    defaultIconId = R.drawable.album,
                    text = album.name,
                    onClick = { navController.goToAlbum(album.id) }
                )
            }
        }
    }
}