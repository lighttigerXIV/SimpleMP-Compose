package com.lighttigerxiv.simple.mp.compose.screens.main.home

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.data.variables.SCREEN_PADDING
import com.lighttigerxiv.simple.mp.compose.ui.composables.CustomTextField
import com.lighttigerxiv.simple.mp.compose.activities.main.MainVM
import com.lighttigerxiv.simple.mp.compose.ui.composables.SongItem
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.MediumHeightSpacer
import kotlinx.coroutines.launch

@SuppressLint("FrequentlyChangedStateReadInComposition")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    mainVM: MainVM,
    homeScreenVM: HomeScreenVM,
    openPage: (page: String) -> Unit
) {

    //States
    val scope = rememberCoroutineScope()

    //Variables
    val surfaceColor = mainVM.surfaceColor.collectAsState().value

    val screenLoaded = homeScreenVM.screenLoaded.collectAsState().value

    val searchText = homeScreenVM.searchText.collectAsState().value

    val menuExpanded = homeScreenVM.menuExpanded.collectAsState().value

    val songs = mainVM.songs.collectAsState().value

    val currentSongs = homeScreenVM.currentSongs.collectAsState().value

    val recentSongs = homeScreenVM.recentSongs.collectAsState().value

    val oldestSongs = homeScreenVM.oldestSongs.collectAsState().value

    val ascendentSongs = homeScreenVM.ascendentSongs.collectAsState().value

    val descendentSongs = homeScreenVM.descendentSongs.collectAsState().value

    val selectedSong = mainVM.selectedSong.collectAsState().value


    if (!screenLoaded) {
        homeScreenVM.loadScreen(mainVM)
    }


    val listState = rememberLazyListState()

    val menuEntries = remember {
        ArrayList<String>().apply {
            add("Artist")
            add("Album")
            add("Playlist")
        }
    }


    Column(
        modifier = Modifier
            //.fillMaxSize()
            //.background(surfaceColor)
            .padding(SCREEN_PADDING)

    ) {

        currentSongs?.let {

            Scaffold(
                floatingActionButton = {


                    if (currentSongs.isNotEmpty()) {

                        ExtendedFloatingActionButton(
                            onClick = {
                                mainVM.shuffle(songs!!)
                            },
                            containerColor = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .height(60.dp)
                                .width(60.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.icon_shuffle_solid),
                                contentDescription = "",
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                                modifier = Modifier
                                    .height(30.dp)
                                    .width(30.dp)
                            )
                        }
                    }
                }
            ) { scaffoldPadding ->

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(surfaceColor)
                        .padding(scaffoldPadding)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {

                        CustomTextField(
                            text = searchText,
                            placeholder = stringResource(id = R.string.SearchSongs),
                            onTextChange = {

                                homeScreenVM.updateSearchText(it)

                                homeScreenVM.filterSongs()

                                scope.launch {
                                    listState.scrollToItem(0)
                                }
                            },
                            sideIcon = R.drawable.menu,
                            onSideIconClick = {
                                homeScreenVM.updateMenuExpanded(true)
                            }
                        )
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = {
                                homeScreenVM.updateMenuExpanded(false)
                            }
                        ) {

                            DropdownMenuItem(
                                text = {
                                    Text(text = stringResource(id = R.string.SortByRecentlyAdded))
                                },
                                onClick = {

                                    homeScreenVM.updateMenuExpanded(false)

                                    homeScreenVM.updateSortType("Recent")

                                    homeScreenVM.updateCurrentSongs(recentSongs)
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(text = stringResource(id = R.string.SortByOldestAdded))
                                },
                                onClick = {

                                    homeScreenVM.updateMenuExpanded(false)

                                    homeScreenVM.updateSortType("Oldest")

                                    homeScreenVM.updateCurrentSongs(oldestSongs)
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(text = stringResource(id = R.string.SortByAscendent))
                                },
                                onClick = {

                                    homeScreenVM.updateMenuExpanded(false)

                                    homeScreenVM.updateSortType("Ascendent")

                                    homeScreenVM.updateCurrentSongs(ascendentSongs)
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(text = stringResource(id = R.string.SortByDescendent))
                                },
                                onClick = {

                                    homeScreenVM.updateMenuExpanded(false)

                                    homeScreenVM.updateSortType("Descendent")

                                    homeScreenVM.updateCurrentSongs(descendentSongs)
                                }
                            )

                            DropdownMenuItem(
                                text = {
                                    Text(text = stringResource(id = R.string.Settings))
                                },
                                onClick = {

                                    homeScreenVM.updateMenuExpanded(false)

                                    openPage("Settings")
                                }
                            )

                            DropdownMenuItem(
                                text = {
                                    Text(text = stringResource(id = R.string.About))
                                },
                                onClick = {

                                    homeScreenVM.updateMenuExpanded(false)

                                    openPage("About")
                                }
                            )
                        }
                    }

                    MediumHeightSpacer()

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        state = listState,
                        content = {

                            items(
                                items = currentSongs,
                                key = { song -> song.id }
                            ) { song ->

                                SongItem(
                                    modifier = Modifier.animateItemPlacement(),
                                    song = song,
                                    songAlbumArt = mainVM.compressedSongsImages.collectAsState().value?.find { it.albumID == song.albumID }?.albumArt,
                                    highlight = song.path == selectedSong?.path,
                                    menuEntries = menuEntries,
                                    onMenuClicked = { option ->

                                        when (option) {

                                            "Artist" -> {

                                                val artistID = song.artistID

                                                openPage("FloatingArtist/$artistID")
                                            }

                                            "Album" -> {

                                                val albumID = song.albumID

                                                openPage("FloatingAlbum/$albumID")
                                            }

                                            "Playlist" -> {

                                                val songID = song.id.toString()

                                                openPage("AddToPlaylist/$songID")
                                            }
                                        }
                                    },
                                    onSongClick = {

                                        homeScreenVM.selectSong(song, mainVM)
                                    }
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}


