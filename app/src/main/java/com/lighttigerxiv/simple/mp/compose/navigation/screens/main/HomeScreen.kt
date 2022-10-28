package com.lighttigerxiv.simple.mp.compose.navigation.screens.main

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.activities.ActivitySettings
import com.lighttigerxiv.simple.mp.compose.composables.CustomTextField
import com.lighttigerxiv.simple.mp.compose.viewmodels.ActivityMainViewModel
import com.lighttigerxiv.simple.mp.compose.composables.SongItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    activityMainViewModel: ActivityMainViewModel
) {
    val context = LocalContext.current
    val popupMenuExpanded = activityMainViewModel.showHomePopupMenu.observeAsState().value!!
    val sortSharedPrefs = context.getSharedPreferences("sorting", MODE_PRIVATE)
    val homeSongsList = activityMainViewModel.currentHomeSongsList.observeAsState().value!!
    val surfaceColor = remember { activityMainViewModel.surfaceColor.value!! }


    val menuEntries = remember{ArrayList<String>()}
    menuEntries.add("artist")
    menuEntries.add("album")
    menuEntries.add("playlist")


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(surfaceColor)
            .padding(14.dp)
    ) {

        Scaffold(
            floatingActionButton = {

                if (activityMainViewModel.songsList.size > 0) {

                    ExtendedFloatingActionButton(
                        onClick = { activityMainViewModel.shuffle(homeSongsList) },
                        containerColor = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .height(60.dp)
                            .width(60.dp)
                    ) {
                        Image(
                            bitmap = activityMainViewModel.shuffleIcon,
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

                    val searchPlaceholder = activityMainViewModel.hintHomeSearchText.value!!
                    val searchText = activityMainViewModel.homeSearchText.observeAsState().value!!

                    CustomTextField(
                        text = searchText,
                        placeholder = searchPlaceholder,
                        onValueChanged = {
                            activityMainViewModel.homeSearchText.value = it
                            activityMainViewModel.filterHomeSongsList(sortSharedPrefs.getString("home", "Recent")!!)
                        },
                        sideIcon = painterResource(id = R.drawable.icon_more_regular),
                        onSideIconClick = { activityMainViewModel.showHomePopupMenu.value = true }
                    )
                    DropdownMenu(
                        expanded = popupMenuExpanded,
                        onDismissRequest = { activityMainViewModel.showHomePopupMenu.value = false }
                    ) {

                        DropdownMenuItem(
                            text = { Text(text = "Sort By Recent") },
                            onClick = {
                                sortSharedPrefs.edit().putString("home", "Recent").apply()
                                activityMainViewModel.currentHomeSongsList.value = activityMainViewModel.recentHomeSongsList
                                activityMainViewModel.showHomePopupMenu.value = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Sort By Oldest") },
                            onClick = {
                                sortSharedPrefs.edit().putString("home", "Oldest").apply()
                                activityMainViewModel.currentHomeSongsList.value = activityMainViewModel.oldestHomeSongsList
                                activityMainViewModel.showHomePopupMenu.value = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Sort By Ascendent") },
                            onClick = {
                                sortSharedPrefs.edit().putString("home", "Ascendent").apply()
                                activityMainViewModel.currentHomeSongsList.value = activityMainViewModel.ascendentHomeSongsList
                                activityMainViewModel.showHomePopupMenu.value = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = "Sort By Descendent") },
                            onClick = {
                                sortSharedPrefs.edit().putString("home", "Descendent").apply()
                                activityMainViewModel.currentHomeSongsList.value = activityMainViewModel.descendentHomeSongsList
                                activityMainViewModel.showHomePopupMenu.value = false
                            }
                        )
                        Divider(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        DropdownMenuItem(
                            text = { Text(text = "Settings") },
                            onClick = {
                                context.startActivity(Intent(context, ActivitySettings::class.java))
                                activityMainViewModel.showHomePopupMenu.value = false
                            }
                        )
                    }
                }


                Spacer(modifier = Modifier.height(30.dp))


                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    content = {

                        items(
                            items = homeSongsList,
                            key = { song -> song.id }
                        ) { song ->

                            SongItem(
                                song = song,
                                songAlbumArt = remember{activityMainViewModel.compressedImagesList.find { it.albumID == song.albumID }!!.albumArt},
                                highlight = song.path == activityMainViewModel.selectedSongPath.observeAsState().value,
                                popupMenuEntries = menuEntries,
                                onMenuClicked = { option->

                                    when(option){

                                        "artist" -> {

                                            val artistID = song.artistID
                                            activityMainViewModel.navController.navigate("floatingArtistScreen?artistID=$artistID")
                                        }
                                        "album" -> {

                                            val albumID = song.albumID
                                            activityMainViewModel.navController.navigate("floatingAlbumScreen?albumID=$albumID")
                                        }
                                        "playlist" -> {

                                            val songID = song.id
                                            activityMainViewModel.navController.navigate("addToPlaylistScreen?songID=$songID")
                                        }
                                    }
                                },
                                onSongClick = {
                                    activityMainViewModel.selectSong(activityMainViewModel.songsList, activityMainViewModel.songsList.indexOf(song))
                                }
                            )
                        }
                    }
                )
            }
        }
    }
}


