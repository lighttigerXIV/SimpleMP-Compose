package com.lighttigerxiv.simple.mp.compose.navigation.screens.main

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.SCREEN_PADDING
import com.lighttigerxiv.simple.mp.compose.composables.CustomTextField
import com.lighttigerxiv.simple.mp.compose.viewmodels.ActivityMainVM
import com.lighttigerxiv.simple.mp.compose.composables.SongItem
import com.lighttigerxiv.simple.mp.compose.getAppString
import kotlinx.coroutines.launch

@SuppressLint("FrequentlyChangedStateReadInComposition")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    activityMainVM: ActivityMainVM,
    openPage: (page: String) -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val popupMenuExpanded = activityMainVM.showHomePopupMenu.observeAsState().value!!
    val sortSharedPrefs = context.getSharedPreferences("sorting", MODE_PRIVATE)
    val homeSongsList = activityMainVM.currentHomeSongsList.collectAsState().value
    val surfaceColor = activityMainVM.surfaceColor.collectAsState().value
    val listState = rememberLazyListState()

    val menuEntries = remember { ArrayList<String>() }
    menuEntries.add("artist")
    menuEntries.add("album")
    menuEntries.add("playlist")


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(surfaceColor)
            .padding(SCREEN_PADDING)
    ) {

        Scaffold(
            floatingActionButton = {

                if (activityMainVM.songsList.size > 0) {

                    ExtendedFloatingActionButton(
                        onClick = { activityMainVM.shuffle(homeSongsList) },
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

                    val searchText = activityMainVM.homeSearchText.observeAsState().value!!

                    CustomTextField(
                        text = searchText,
                        placeholder = remember { getAppString(context, R.string.SearchSongs) },
                        onTextChange = {
                            activityMainVM.homeSearchText.value = it
                            activityMainVM.filterHomeSongsList(sortSharedPrefs.getString("home", "Recent")!!)
                            scope.launch { listState.scrollToItem(0) }
                        },
                        sideIcon = R.drawable.icon_more_regular,
                        onSideIconClick = { activityMainVM.showHomePopupMenu.value = true }
                    )
                    DropdownMenu(
                        expanded = popupMenuExpanded,
                        onDismissRequest = { activityMainVM.showHomePopupMenu.value = false }
                    ) {

                        DropdownMenuItem(
                            text = { Text(text = remember { getAppString(context, R.string.SortByRecentlyAdded) }) },
                            onClick = {
                                activityMainVM.showHomePopupMenu.value = false
                                sortSharedPrefs.edit().putString("home", "Recent").apply()
                                activityMainVM.setCurrentHomeSongsList(activityMainVM.recentHomeSongsList)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = remember { getAppString(context, R.string.SortByOldestAdded) }) },
                            onClick = {
                                activityMainVM.showHomePopupMenu.value = false
                                sortSharedPrefs.edit().putString("home", "Oldest").apply()
                                activityMainVM.setCurrentHomeSongsList(activityMainVM.oldestHomeSongsList)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = remember { getAppString(context, R.string.SortByAscendent) }) },
                            onClick = {
                                activityMainVM.showHomePopupMenu.value = false
                                sortSharedPrefs.edit().putString("home", "Ascendent").apply()
                                activityMainVM.setCurrentHomeSongsList(activityMainVM.ascendentHomeSongsList)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = remember { getAppString(context, R.string.SortByDescendent) }) },
                            onClick = {
                                activityMainVM.showHomePopupMenu.value = false
                                sortSharedPrefs.edit().putString("home", "Descendent").apply()
                                activityMainVM.setCurrentHomeSongsList(activityMainVM.descendentHomeSongsList)
                            }
                        )

                        DropdownMenuItem(
                            text = { Text(text = remember { getAppString(context, R.string.Settings) }) },
                            onClick = {
                                activityMainVM.showHomePopupMenu.value = false
                                openPage("Settings")
                            }
                        )

                        DropdownMenuItem(
                            text = { Text(text = remember { getAppString(context, R.string.About) }) },
                            onClick = {
                                activityMainVM.showHomePopupMenu.value = false
                                openPage("About")
                            }
                        )
                    }
                }


                Spacer(modifier = Modifier.height(30.dp))


                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    state = listState,
                    content = {

                        items(
                            items = homeSongsList,
                            key = { song -> song.id }
                        ) { song ->

                            SongItem(
                                modifier = Modifier.animateItemPlacement(),
                                song = song,
                                songAlbumArt = remember { activityMainVM.compressedImagesList.find { it.albumID == song.albumID }!!.albumArt },
                                highlight = song.path == activityMainVM.selectedSongPath.observeAsState().value,
                                popupMenuEntries = menuEntries,
                                onMenuClicked = { option ->

                                    when (option) {

                                        "artist" -> {

                                            val artistID = song.artistID
                                            activityMainVM.navController.navigate("floatingArtistScreen?artistID=$artistID")
                                        }
                                        "album" -> {

                                            val albumID = song.albumID
                                            activityMainVM.navController.navigate("floatingAlbumScreen?albumID=$albumID")
                                        }
                                        "playlist" -> {

                                            val songID = song.id
                                            activityMainVM.navController.navigate("addToPlaylistScreen?songID=$songID")
                                        }
                                    }
                                },
                                onSongClick = {
                                    activityMainVM.selectSong(activityMainVM.songsList, activityMainVM.songsList.indexOf(song))
                                }
                            )
                        }
                    }
                )
            }
        }
    }
}


