package com.lighttigerxiv.simple.mp.compose.navigation.screens.main

import android.content.res.Configuration
import android.graphics.BitmapFactory
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.SCREEN_PADDING
import com.lighttigerxiv.simple.mp.compose.XSMALL_SPACING
import com.lighttigerxiv.simple.mp.compose.app_viewmodels.MainVM
import com.lighttigerxiv.simple.mp.compose.composables.CustomTextField
import com.lighttigerxiv.simple.mp.compose.composables.ImageCard
import com.lighttigerxiv.simple.mp.compose.composables.spacers.MediumHeightSpacer
import com.lighttigerxiv.simple.mp.compose.getAppString
import com.lighttigerxiv.simple.mp.compose.screens.main.artists.ArtistsScreenVM
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArtistsScreen(
    mainVM: MainVM,
    artistsVM: ArtistsScreenVM,
    onArtistClicked: (artistID: Long) -> Unit
) {

    //States
    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    val configuration = LocalConfiguration.current

    val gridState = rememberLazyGridState()

    //Variables
    val screenLoaded = artistsVM.screenLoaded.collectAsState().value

    val searchText = artistsVM.searchText.collectAsState().value

    val menuExpanded = artistsVM.menuExpanded.collectAsState().value

    val artists = artistsVM.currentArtists.collectAsState().value

    val recentArtists = artistsVM.recentArtists.collectAsState().value

    val oldestArtists = artistsVM.oldestArtists.collectAsState().value

    val ascendentArtists = artistsVM.ascendentArtists.collectAsState().value

    val descendentArtists = artistsVM.descendentArtists.collectAsState().value

    val surfaceColor = mainVM.surfaceColor.collectAsState().value


    val gridCellsCount = when (configuration.orientation) {

        Configuration.ORIENTATION_PORTRAIT -> 2
        else -> 4
    }


    if (!screenLoaded) {
        artistsVM.loadScreen(mainVM)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(surfaceColor)
            .padding(SCREEN_PADDING)
    ) {


        if (screenLoaded) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {

                    CustomTextField(
                        text = searchText,
                        placeholder = remember { getAppString(context, R.string.SearchArtists) },
                        textType = "text",
                        onTextChange = {

                            artistsVM.updateSearchText(it)

                            artistsVM.filterArtists()

                            scope.launch { gridState.scrollToItem(0) }
                        },
                        sideIcon = R.drawable.icon_sort_solid,
                        onSideIconClick = {
                            artistsVM.updateMenuExpanded(true)
                        },
                    )
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = {
                            artistsVM.updateMenuExpanded(false)
                        }
                    ) {

                        DropdownMenuItem(
                            text = { Text(text = remember { getAppString(context, R.string.SortByRecentlyAdded) }) },
                            onClick = {

                                artistsVM.updateSortType("Recent")

                                artistsVM.updateCurrentArtists(recentArtists)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = remember { getAppString(context, R.string.SortByOldestAdded) }) },
                            onClick = {

                                artistsVM.updateSortType("Oldest")

                                artistsVM.updateCurrentArtists(oldestArtists)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = remember { getAppString(context, R.string.SortByAscendent) }) },
                            onClick = {

                                artistsVM.updateSortType("Ascendent")

                                artistsVM.updateCurrentArtists(ascendentArtists)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = remember { getAppString(context, R.string.SortByDescendent) }) },
                            onClick = {

                                artistsVM.updateSortType("Descendent")

                                artistsVM.updateCurrentArtists(descendentArtists)
                            }
                        )
                    }
                }

                MediumHeightSpacer()

                LazyVerticalGrid(
                    columns = GridCells.Fixed(gridCellsCount),
                    verticalArrangement = Arrangement.spacedBy(XSMALL_SPACING),
                    horizontalArrangement = Arrangement.spacedBy(XSMALL_SPACING),
                    state = gridState,
                    modifier = Modifier
                        .fillMaxSize(),
                ) {

                    items(
                        items = artists!!,
                        key = { artist -> artist.artistID },
                    ) { artist ->

                        val albumArt = mainVM.songsImagesList.first { it.albumID == artist.albumID }.albumArt

                        ImageCard(
                            modifier = Modifier.animateItemPlacement(),
                            cardImage = remember { albumArt ?: BitmapFactory.decodeResource(context.resources, R.drawable.icon_music_record) },
                            imageTint = if (albumArt == null) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null,
                            cardText = remember { artist.artist },
                            onCardClicked = {

                                onArtistClicked(artist.artistID)
                            }
                        )
                    }
                }
            }
        }
    }
}