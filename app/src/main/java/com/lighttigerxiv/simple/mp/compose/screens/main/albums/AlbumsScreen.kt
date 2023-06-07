package com.lighttigerxiv.simple.mp.compose.screens.main.albums

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.data.variables.SCREEN_PADDING
import com.lighttigerxiv.simple.mp.compose.ui.composables.CustomTextField
import com.lighttigerxiv.simple.mp.compose.ui.composables.ImageCard
import com.lighttigerxiv.simple.mp.compose.functions.getAppString
import com.lighttigerxiv.simple.mp.compose.activities.main.MainVM
import com.lighttigerxiv.simple.mp.compose.data.variables.ImageSizes
import com.lighttigerxiv.simple.mp.compose.data.variables.Sorts
import com.lighttigerxiv.simple.mp.compose.functions.getImage
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.MediumHeightSpacer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlbumsScreen(
    mainVM: MainVM,
    vm: AlbumsScreenVM,
    onAlbumClicked: (albumID: Long) -> Unit
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val listState = rememberLazyGridState()
    val surfaceColor = mainVM.surfaceColor.collectAsState().value
    val screenLoaded = vm.screenLoaded.collectAsState().value
    val searchText = vm.searchText.collectAsState().value
    val menuExpanded = vm.menuExpanded.collectAsState().value
    val albums = vm.currentAlbums.collectAsState().value
    val recentAlbums = vm.recentAlbums.collectAsState().value
    val oldestAlbums = vm.oldestAlbums.collectAsState().value
    val ascendentAlbums = vm.ascendentAlbums.collectAsState().value
    val descendentAlbums = vm.descendentAlbums.collectAsState().value
    val gridCellsCount = when (LocalConfiguration.current.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> 2
        else -> 4
    }


    if (!screenLoaded) {
        vm.loadScreen(mainVM)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(surfaceColor)
            .padding(SCREEN_PADDING)
    ) {

        if(screenLoaded){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {

                CustomTextField(
                    text = searchText,
                    placeholder = remember { getAppString(context, R.string.SearchAlbums) },
                    textType = "text",
                    onTextChange = {

                        vm.updateSearchText(it)

                        vm.filterAlbums()

                        scope.launch { listState.scrollToItem(0) }
                    },
                    sideIcon = R.drawable.sort,
                    onSideIconClick = { vm.updateMenuExpanded(true) }
                )
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { vm.updateMenuExpanded(false) }
                ) {

                    DropdownMenuItem(
                        text = { Text(text = remember { getAppString(context, R.string.SortByRecentlyAdded) }) },
                        onClick = {

                            vm.updateSortType(Sorts.RECENT)
                            vm.updateCurrentAlbums(recentAlbums)
                            scope.launch {
                                vm.updateMenuExpanded(false)
                                delay(200)
                                listState.scrollToItem(index = 0)
                            }
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = remember { getAppString(context, R.string.SortByOldestAdded) }) },
                        onClick = {

                            vm.updateSortType(Sorts.OLDEST)
                            vm.updateCurrentAlbums(oldestAlbums)
                            scope.launch {
                                vm.updateMenuExpanded(false)
                                delay(200)
                                listState.scrollToItem(index = 0)
                            }
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = remember { getAppString(context, R.string.SortByAscendent) }) },
                        onClick = {

                            vm.updateSortType(Sorts.ASCENDENT)
                            vm.updateCurrentAlbums(ascendentAlbums)
                            scope.launch {
                                vm.updateMenuExpanded(false)
                                delay(200)
                                listState.scrollToItem(index = 0)
                            }
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = remember { getAppString(context, R.string.SortByDescendent) }) },
                        onClick = {

                            vm.updateSortType(Sorts.DESCENDENT)
                            vm.updateCurrentAlbums(descendentAlbums)
                            scope.launch {
                                vm.updateMenuExpanded(false)
                                delay(200)
                                listState.scrollToItem(index = 0)
                            }
                        }
                    )
                }
            }

            MediumHeightSpacer()

            LazyVerticalGrid(
                columns = GridCells.Fixed(gridCellsCount),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                state = listState,
                modifier = Modifier
                    .fillMaxSize(),
            ) {

                items(
                    items = albums!!,
                    key = { album -> album.albumID },
                ) { album ->

                    val albumArt = mainVM.songsCovers.collectAsState().value?.first { it.albumID == album.albumID }?.albumArt

                    ImageCard(
                        modifier = Modifier.animateItemPlacement(),
                        cardImage = remember { albumArt ?: getImage(context, R.drawable.cd, ImageSizes.MEDIUM) },
                        imageTint = if (albumArt == null) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null,
                        cardText = remember { album.album },
                        onCardClicked = {
                            onAlbumClicked(album.albumID)
                        }
                    )
                }
            }
        }
    }
}