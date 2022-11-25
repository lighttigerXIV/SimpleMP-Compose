package com.lighttigerxiv.simple.mp.compose.navigation.screens

import android.content.Context.MODE_PRIVATE
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.composables.CustomTextField
import com.lighttigerxiv.simple.mp.compose.composables.ImageCard
import com.lighttigerxiv.simple.mp.compose.viewmodels.ActivityMainVM

@Composable
fun AlbumsScreen(
    activityMainVM: ActivityMainVM,
    onAlbumClicked : (albumID : Long) -> Unit
){

    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    var popupMenuExpanded by remember { mutableStateOf(false) }
    val sortSharedPrefs = context.getSharedPreferences("sorting", MODE_PRIVATE)

    val albumsList = activityMainVM.currentAlbumsList.observeAsState().value!!


    val gridCellsCount = when(configuration.orientation){

        Configuration.ORIENTATION_PORTRAIT->2
        else->4
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(activityMainVM.surfaceColor.value!!)
            .padding(14.dp)
    ) {

        Column( modifier = Modifier.fillMaxSize() ) {

            Row( modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)) {

                val searchText = activityMainVM.albumsSearchText.observeAsState().value!!
                val hint = activityMainVM.hintAlbumsSearchText.value!!

                CustomTextField(
                    text = searchText,
                    placeholder = hint,
                    textType = "text",
                    onValueChanged = {
                        activityMainVM.albumsSearchText.value = it
                        activityMainVM.filterAlbumsList(sortSharedPrefs.getString("albums", "Recent")!!)
                    },
                    sideIcon = painterResource(id = R.drawable.icon_more_regular),
                    onSideIconClick = { popupMenuExpanded = true }
                )
                DropdownMenu(
                    expanded = popupMenuExpanded,
                    onDismissRequest = { popupMenuExpanded = false }
                ) {

                    DropdownMenuItem(
                        text = { Text(text = "Sort By Recent") },
                        onClick = {
                            sortSharedPrefs.edit().putString("albums", "Recent").apply()
                            activityMainVM.currentAlbumsList.value = activityMainVM.recentAlbumsList
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = "Sort By Oldest") },
                        onClick = {
                            sortSharedPrefs.edit().putString("albums", "Oldest").apply()
                            activityMainVM.currentAlbumsList.value = activityMainVM.oldestAlbumsList
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = "Sort By Ascendent") },
                        onClick = {
                            sortSharedPrefs.edit().putString("albums", "Ascendent").apply()
                            activityMainVM.currentAlbumsList.value = activityMainVM.ascendentAlbumsList
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = "Sort By Descendent") },
                        onClick = {
                            sortSharedPrefs.edit().putString("albums", "Descendent").apply()
                            activityMainVM.currentAlbumsList.value = activityMainVM.descendentAlbumsList
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(30.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(gridCellsCount),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxSize(),
            ){

                items(
                    items = albumsList,
                    key = { album-> album.albumID },
                ){ album->

                    ImageCard(
                        cardImage = remember { activityMainVM.songsImagesList.first { it.albumID == album.albumID }.albumArt },
                        cardText = remember{album.albumName},
                        onCardClicked = {
                            activityMainVM.clickedAlbumID.value = album.albumID
                            onAlbumClicked(album.albumID)
                        }
                    )
                }
            }
        }
    }
}