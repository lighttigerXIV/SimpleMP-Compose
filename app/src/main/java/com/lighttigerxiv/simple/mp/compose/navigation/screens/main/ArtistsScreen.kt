package com.lighttigerxiv.simple.mp.compose.navigation.screens.main

import android.content.Context
import android.content.res.Configuration
import android.graphics.BitmapFactory
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
import com.lighttigerxiv.simple.mp.compose.viewmodels.ActivityMainVM
import com.lighttigerxiv.simple.mp.compose.composables.CustomTextField
import com.lighttigerxiv.simple.mp.compose.composables.ImageCard
import com.lighttigerxiv.simple.mp.compose.getAppString
import kotlinx.coroutines.launch

@Composable
fun ArtistsScreen(
    activityMainVM: ActivityMainVM,
    onArtistClicked: (artistID: Long) -> Unit
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    var popupMenuExpanded by remember { mutableStateOf(false) }
    val sortSharedPrefs = context.getSharedPreferences("sorting", Context.MODE_PRIVATE)
    val gridState = rememberLazyGridState()
    val artistsList = activityMainVM.currentArtistsList.observeAsState().value!!


    val gridCellsCount = when (configuration.orientation) {

        Configuration.ORIENTATION_PORTRAIT -> 2
        else -> 4
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(activityMainVM.surfaceColor.collectAsState().value)
            .padding(SCREEN_PADDING)
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

                CustomTextField(
                    text = searchText,
                    placeholder = remember { getAppString(context, R.string.SearchArtists) },
                    textType = "text",
                    onTextChange = {
                        activityMainVM.artistsSearchText.value = it
                        activityMainVM.filterArtistsList(sortSharedPrefs.getString("artists", "Recent")!!)
                        scope.launch { gridState.scrollToItem(0) }
                    },
                    sideIcon = R.drawable.icon_sort_solid,
                    onSideIconClick = { popupMenuExpanded = true },
                )
                DropdownMenu(
                    expanded = popupMenuExpanded,
                    onDismissRequest = { popupMenuExpanded = false }
                ) {

                    DropdownMenuItem(
                        text = { Text(text = remember { getAppString(context, R.string.SortByRecentlyAdded) }) },
                        onClick = {
                            sortSharedPrefs.edit().putString("artists", "Recent").apply()
                            activityMainVM.currentArtistsList.value = activityMainVM.recentArtistsList
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = remember { getAppString(context, R.string.SortByOldestAdded) }) },
                        onClick = {
                            sortSharedPrefs.edit().putString("artists", "Oldest").apply()
                            activityMainVM.currentArtistsList.value = activityMainVM.oldestArtistsList
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = remember { getAppString(context, R.string.SortByAscendent) }) },
                        onClick = {
                            sortSharedPrefs.edit().putString("artists", "Ascendent").apply()
                            activityMainVM.currentArtistsList.value = activityMainVM.ascendentArtistsList
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = remember { getAppString(context, R.string.SortByDescendent) }) },
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
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                state = gridState,
                modifier = Modifier
                    .fillMaxSize(),
            ) {

                items(
                    items = artistsList,
                    key = { artist -> artist.artistID },
                ) { artist ->

                    val albumArt = activityMainVM.songsImagesList.first { it.albumID == artist.albumID }.albumArt

                    ImageCard(
                        cardImage = remember { albumArt ?: BitmapFactory.decodeResource(context.resources, R.drawable.icon_music_record) },
                        imageTint = if(albumArt == null) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null,
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