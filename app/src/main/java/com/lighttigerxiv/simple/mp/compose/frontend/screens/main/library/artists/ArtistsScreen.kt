package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.artists

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.backend.settings.SettingsOptions
import com.lighttigerxiv.simple.mp.compose.frontend.composables.Card
import com.lighttigerxiv.simple.mp.compose.frontend.composables.MenuItem
import com.lighttigerxiv.simple.mp.compose.frontend.composables.MiniPlayerSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.composables.TextField
import com.lighttigerxiv.simple.mp.compose.frontend.composables.VSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goToArtist
import com.lighttigerxiv.simple.mp.compose.frontend.utils.Sizes
import com.lighttigerxiv.simple.mp.compose.frontend.utils.customRememberLazyGridState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ArtistsScreen(
    vm: ArtistsScreenVM = viewModel(factory = ArtistsScreenVM.Factory),
    navController: NavHostController,
    inLandscape: Boolean,
    showMiniPlayer: Boolean
) {

    val uiState = vm.uiState.collectAsState().value
    val lazyGridState = customRememberLazyGridState(vm.listPosition) { vm.listPosition = it }
    val gridCellsCount by remember { mutableIntStateOf(if (inLandscape) 5 else 2) }


    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        TextField(
            text = uiState.searchText,
            onTextChange = { vm.updateSearchText(it) },
            placeholder = stringResource(id = R.string.search_artists),
            startIcon = R.drawable.sort,
            onStartIconClick = { vm.updateShowMenu(true) }
        )

        Menu(vm = vm, uiState = uiState, gridState = lazyGridState)

        VSpacer(size = Sizes.LARGE)

        LazyVerticalGrid(
            state = lazyGridState,
            columns = GridCells.Fixed(gridCellsCount),
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

            item(span = { GridItemSpan(gridCellsCount) }) {
                MiniPlayerSpacer(isShown = showMiniPlayer)
            }
        }
    }
}

@Composable
fun Menu(
    vm: ArtistsScreenVM,
    uiState: ArtistsScreenVM.UiState,
    gridState: LazyGridState
) {

    val scope = rememberCoroutineScope()

    fun scrollUp(){
        scope.launch {
            withContext(Dispatchers.Main) {
                delay(200)
                gridState.scrollToItem(0)
            }
        }
    }

    Column {
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

                    scrollUp()
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

                    scrollUp()
                }
            )
        }
    }
}