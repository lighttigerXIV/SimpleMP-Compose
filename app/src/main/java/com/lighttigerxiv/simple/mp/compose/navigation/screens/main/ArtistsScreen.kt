package com.lighttigerxiv.simple.mp.compose.navigation.screens.main

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.viewmodels.ActivityMainVM
import com.lighttigerxiv.simple.mp.compose.composables.CustomTextField
import com.lighttigerxiv.simple.mp.compose.composables.ImageCard

@Composable
fun ArtistsScreen(
    activityMainVM: ActivityMainVM,
    onArtistClicked: (artistID: Long) -> Unit
) {

    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    var popupMenuExpanded by remember { mutableStateOf(false) }
    val sortSharedPrefs = context.getSharedPreferences("sorting", Context.MODE_PRIVATE)

    val artistsList = activityMainVM.currentArtistsList.observeAsState().value!!


    val gridCellsCount = when (configuration.orientation) {

        Configuration.ORIENTATION_PORTRAIT -> 2
        else -> 4
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(activityMainVM.surfaceColor.collectAsState().value)
            .padding(14.dp)
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {

                val searchText = activityMainVM.artistsSearchText.observeAsState().value!!
                val hint = activityMainVM.hintArtistsSearchText.value!!

                CustomTextField(
                    text = searchText,
                    placeholder = hint,
                    textType = "text",
                    onTextChange = {
                        activityMainVM.artistsSearchText.value = it
                        activityMainVM.filterArtistsList(sortSharedPrefs.getString("artists", "Recent")!!)
                    },
                    sideIcon = R.drawable.icon_more_regular,
                    onSideIconClick = { popupMenuExpanded = true },
                )
                DropdownMenu(
                    expanded = popupMenuExpanded,
                    onDismissRequest = { popupMenuExpanded = false }
                ) {

                    DropdownMenuItem(
                        text = { Text(text = "Sort By Recent") },
                        onClick = {
                            sortSharedPrefs.edit().putString("artists", "Recent").apply()
                            activityMainVM.currentArtistsList.value = activityMainVM.recentArtistsList
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = "Sort By Oldest") },
                        onClick = {
                            sortSharedPrefs.edit().putString("artists", "Oldest").apply()
                            activityMainVM.currentArtistsList.value = activityMainVM.oldestArtistsList
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = "Sort By Ascendent") },
                        onClick = {
                            sortSharedPrefs.edit().putString("artists", "Ascendent").apply()
                            activityMainVM.currentArtistsList.value = activityMainVM.ascendentArtistsList
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = "Sort By Descendent") },
                        onClick = {
                            sortSharedPrefs.edit().putString("artists", "Descendent").apply()
                            activityMainVM.currentArtistsList.value = activityMainVM.descendentArtistsList
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
            ) {

                items(
                    items = artistsList,
                    key = { artist -> artist.artistID },
                ) { artist ->

                    ImageCard(
                        cardImage = remember { activityMainVM.songsImagesList.first { it.albumID == artist.albumID }.albumArt },
                        cardText = remember { artist.artistName },
                        onCardClicked = {

                            onArtistClicked(artist.artistID)
                        }
                    )
                }
            }
        }
    }
}