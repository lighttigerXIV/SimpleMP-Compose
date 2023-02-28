package com.lighttigerxiv.simple.mp.compose.screens.main.albums

import android.content.res.Configuration
import android.graphics.BitmapFactory
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
import com.lighttigerxiv.simple.mp.compose.SCREEN_PADDING
import com.lighttigerxiv.simple.mp.compose.composables.CustomTextField
import com.lighttigerxiv.simple.mp.compose.composables.ImageCard
import com.lighttigerxiv.simple.mp.compose.getAppString
import com.lighttigerxiv.simple.mp.compose.app_viewmodels.MainVM
import com.lighttigerxiv.simple.mp.compose.composables.spacers.MediumHeightSpacer
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlbumsScreen(
    mainVM: MainVM,
    albumsVM: AlbumsScreenVM,
    onAlbumClicked: (albumID: Long) -> Unit
) {

    //States
    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    val listState = rememberLazyGridState()

    //Variables

    val surfaceColor = mainVM.surfaceColor.collectAsState().value

    val screenLoaded = albumsVM.screenLoaded.collectAsState().value

    val searchText = albumsVM.searchText.collectAsState().value

    val menuExpanded = albumsVM.menuExpanded.collectAsState().value

    val albums = albumsVM.currentAlbums.collectAsState().value

    val recentAlbums = albumsVM.recentAlbums.collectAsState().value

    val oldestAlbums = albumsVM.oldestAlbums.collectAsState().value

    val ascendentAlbums = albumsVM.ascendentAlbums.collectAsState().value

    val descendentAlbums = albumsVM.descendentAlbums.collectAsState().value

    val gridCellsCount = when (LocalConfiguration.current.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> 2
        else -> 4
    }


    if (!screenLoaded) {
        albumsVM.loadScreen(mainVM)
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

                        albumsVM.updateSearchText(it)

                        albumsVM.filterAlbums()

                        scope.launch { listState.scrollToItem(0) }
                    },
                    sideIcon = R.drawable.sort,
                    onSideIconClick = { albumsVM.updateMenuExpanded(true) }
                )
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { albumsVM.updateMenuExpanded(false) }
                ) {

                    DropdownMenuItem(
                        text = { Text(text = remember { getAppString(context, R.string.SortByRecentlyAdded) }) },
                        onClick = {

                            albumsVM.updateSortType("Recent")

                            albumsVM.updateCurrentAlbums(recentAlbums)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = remember { getAppString(context, R.string.SortByOldestAdded) }) },
                        onClick = {

                            albumsVM.updateSortType("Oldest")

                            albumsVM.updateCurrentAlbums(oldestAlbums)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = remember { getAppString(context, R.string.SortByAscendent) }) },
                        onClick = {

                            albumsVM.updateSortType("Ascendent")

                            albumsVM.updateCurrentAlbums(ascendentAlbums)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = remember { getAppString(context, R.string.SortByDescendent) }) },
                        onClick = {

                            albumsVM.updateSortType("Descendent")

                            albumsVM.updateCurrentAlbums(descendentAlbums)
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

                    val albumArt = mainVM.songsImages.collectAsState().value?.first { it.albumID == album.albumID }?.albumArt

                    ImageCard(
                        modifier = Modifier.animateItemPlacement(),
                        cardImage = remember { albumArt ?: BitmapFactory.decodeResource(context.resources, R.drawable.icon_music_record) },
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