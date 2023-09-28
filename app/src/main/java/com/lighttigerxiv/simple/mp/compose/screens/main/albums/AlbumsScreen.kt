package com.lighttigerxiv.simple.mp.compose.screens.main.albums

import android.content.Context
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.data.variables.SCREEN_PADDING
import com.lighttigerxiv.simple.mp.compose.ui.composables.CustomTextField
import com.lighttigerxiv.simple.mp.compose.ui.composables.ImageCard
import com.lighttigerxiv.simple.mp.compose.functions.getAppString
import com.lighttigerxiv.simple.mp.compose.activities.main.MainVM
import com.lighttigerxiv.simple.mp.compose.data.variables.ImageSizes
import com.lighttigerxiv.simple.mp.compose.data.variables.Settings
import com.lighttigerxiv.simple.mp.compose.functions.getImage
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.MediumVerticalSpacer
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
    val artistAscendentAlbums = vm.artistAscendentAlbums.collectAsState().value
    val artistDescendentAlbums = vm.artistDescendentAlbums.collectAsState().value

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
                        text = {
                            Text(text = stringResource(id = R.string.ModificationDate))
                        },
                        onClick = {

                            val currentSort = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE).getString(Settings.ALBUMS_SORT, Settings.Values.Sort.RECENT)
                            val filterAlgorithm = if (currentSort == Settings.Values.Sort.RECENT) Settings.Values.Sort.OLDEST else Settings.Values.Sort.RECENT
                            val filterAlbums = if(currentSort == Settings.Values.Sort.RECENT) oldestAlbums else recentAlbums

                            vm.updateSortType(filterAlgorithm)
                            vm.updateCurrentAlbums(filterAlbums)
                            scope.launch {
                                vm.updateMenuExpanded(false)
                                delay(200)
                                listState.scrollToItem(index = 0)
                            }
                        },
                        leadingIcon = {
                            Icon(
                                modifier = Modifier
                                    .height(20.dp)
                                    .width(20.dp),
                                painter = painterResource(id = R.drawable.date_sort),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    )

                    DropdownMenuItem(
                        text = {
                            Text(text = stringResource(id = R.string.SortAlphabetically))
                        },
                        onClick = {

                            val currentSort = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE).getString(Settings.ALBUMS_SORT, Settings.Values.Sort.RECENT)
                            val filterAlgorithm = if (currentSort == Settings.Values.Sort.ASCENDENT) Settings.Values.Sort.DESCENDENT else Settings.Values.Sort.ASCENDENT
                            val filterAlbums = if(currentSort == Settings.Values.Sort.ASCENDENT) descendentAlbums else ascendentAlbums

                            vm.updateSortType(filterAlgorithm)
                            vm.updateCurrentAlbums(filterAlbums)
                            scope.launch {
                                vm.updateMenuExpanded(false)
                                delay(200)
                                listState.scrollToItem(index = 0)
                            }
                        },
                        leadingIcon = {
                            Icon(
                                modifier = Modifier
                                    .height(20.dp)
                                    .width(20.dp),
                                painter = painterResource(id = R.drawable.alphabetic_sort),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    )

                    DropdownMenuItem(
                        text = {
                            Text(text = stringResource(id = R.string.SortByArtist))
                        },
                        onClick = {

                            val currentSort = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE).getString(Settings.ALBUMS_SORT, Settings.Values.Sort.RECENT)
                            val filterAlgorithm = if (currentSort == Settings.Values.Sort.ARTIST_ASCENDENT) Settings.Values.Sort.ARTIST_DESCENDENT else Settings.Values.Sort.ARTIST_ASCENDENT
                            val filterAlbums = if(currentSort == Settings.Values.Sort.ARTIST_ASCENDENT) artistDescendentAlbums else artistAscendentAlbums

                            vm.updateSortType(filterAlgorithm)
                            vm.updateCurrentAlbums(filterAlbums)
                            scope.launch {
                                vm.updateMenuExpanded(false)
                                delay(200)
                                listState.scrollToItem(index = 0)
                            }
                        },
                        leadingIcon = {
                            Icon(
                                modifier = Modifier
                                    .height(20.dp)
                                    .width(20.dp),
                                painter = painterResource(id = R.drawable.person),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    )
                }
            }

            MediumVerticalSpacer()

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
                    key = { album -> album.id },
                ) { album ->

                    ImageCard(
                        modifier = Modifier.animateItemPlacement(),
                        cardImage = remember { album.art ?: getImage(context, R.drawable.cd, ImageSizes.MEDIUM) },
                        imageTint = if (album.art == null) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null,
                        cardText = remember { album.title },
                        onCardClicked = {
                            onAlbumClicked(album.id)
                        }
                    )
                }
            }
        }
    }
}