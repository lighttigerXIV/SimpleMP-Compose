package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.artists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.backend.settings.SettingsOptions
import com.lighttigerxiv.simple.mp.compose.frontend.composables.Card
import com.lighttigerxiv.simple.mp.compose.frontend.composables.MenuItem
import com.lighttigerxiv.simple.mp.compose.frontend.composables.TextField
import com.lighttigerxiv.simple.mp.compose.frontend.composables.VSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goToAbout
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goToArtist
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goToSettings
import com.lighttigerxiv.simple.mp.compose.frontend.utils.Sizes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ArtistsScreen(
    vm: ArtistsScreenVM = viewModel(factory = ArtistsScreenVM.Factory),
    navController: NavHostController
) {

    val uiState = vm.uiState.collectAsState().value
    val lazyGridState = rememberLazyGridState()

    LaunchedEffect(uiState.sortType){
        withContext(Dispatchers.Main){
            if(uiState.artists.isNotEmpty()){
                lazyGridState.scrollToItem(0)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        TextField(
            text = uiState.searchText,
            onTextChange = { vm.updateSearchText(it) },
            placeholder = stringResource(id = R.string.search_artists),
            startIcon = R.drawable.sort,
            onStartIconClick = {vm.updateShowMenu(true)}
        )

        Menu(vm = vm, uiState = uiState)

        VSpacer(size = Sizes.LARGE)

        LazyVerticalGrid(
            state = lazyGridState,
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(Sizes.SMALL),
            horizontalArrangement = Arrangement.spacedBy(Sizes.SMALL)
        ) {
            items(
                items = uiState.artists,
                key = { it.id }
            ) { artist ->

                Card(
                    defaultIconId = R.drawable.artist,
                    text = artist.name,
                    onClick = { navController.goToArtist(artist.id) }
                )
            }
        }
    }
}

@Composable
fun Menu(
    vm: ArtistsScreenVM,
    uiState: ArtistsScreenVM.UiState
) {
    Column {
        DropdownMenu(
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
        }
    }
}