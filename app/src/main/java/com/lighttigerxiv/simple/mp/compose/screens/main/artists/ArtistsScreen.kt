package com.lighttigerxiv.simple.mp.compose.screens.main.artists

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.data.variables.SCREEN_PADDING
import com.lighttigerxiv.simple.mp.compose.data.variables.XSMALL_SPACING
import com.lighttigerxiv.simple.mp.compose.activities.main.MainVM
import com.lighttigerxiv.simple.mp.compose.data.variables.ImageSizes
import com.lighttigerxiv.simple.mp.compose.data.variables.Settings
import com.lighttigerxiv.simple.mp.compose.ui.composables.CustomTextField
import com.lighttigerxiv.simple.mp.compose.ui.composables.ImageCard
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.MediumVerticalSpacer
import com.lighttigerxiv.simple.mp.compose.functions.getAppString
import com.lighttigerxiv.simple.mp.compose.functions.getImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArtistsScreen(
    mainVM: MainVM,
    vm: ArtistsScreenVM,
    activityContext: ViewModelStoreOwner,
    navController: NavHostController
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val gridState = rememberLazyGridState()
    val screenLoaded = vm.screenLoaded.collectAsState().value
    val searchText = vm.searchText.collectAsState().value
    val menuExpanded = vm.menuExpanded.collectAsState().value
    val artists = vm.currentArtists.collectAsState().value
    val recentArtists = vm.recentArtists.collectAsState().value
    val oldestArtists = vm.oldestArtists.collectAsState().value
    val ascendentArtists = vm.ascendentArtists.collectAsState().value
    val descendentArtists = vm.descendentArtists.collectAsState().value
    val surfaceColor = mainVM.surfaceColor.collectAsState().value


    val gridCellsCount = when (configuration.orientation) {

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

                            vm.updateSearchText(it)

                            vm.filterArtists()

                            scope.launch { gridState.scrollToItem(0) }
                        },
                        sideIcon = R.drawable.sort,
                        onSideIconClick = {
                            vm.updateMenuExpanded(true)
                        },
                    )
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = {
                            vm.updateMenuExpanded(false)
                        }
                    ) {

                        /*
                        DropdownMenuItem(
                            text = { Text(text = remember { getAppString(context, R.string.SortByRecentlyAdded) }) },
                            onClick = {

                                vm.updateSortType(Sorts.RECENT)
                                vm.updateCurrentArtists(recentArtists)
                                scope.launch {
                                    vm.updateMenuExpanded(false)
                                    delay(200)
                                    gridState.scrollToItem(index = 0)
                                }
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = remember { getAppString(context, R.string.SortByOldestAdded) }) },
                            onClick = {

                                vm.updateSortType(Sorts.OLDEST)
                                vm.updateCurrentArtists(oldestArtists)
                                scope.launch {
                                    vm.updateMenuExpanded(false)
                                    delay(200)
                                    gridState.scrollToItem(index = 0)
                                }
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = remember { getAppString(context, R.string.SortByAscendent) }) },
                            onClick = {

                                vm.updateSortType(Sorts.ASCENDENT)
                                vm.updateCurrentArtists(ascendentArtists)
                                scope.launch {
                                    vm.updateMenuExpanded(false)
                                    delay(200)
                                    gridState.scrollToItem(index = 0)
                                }
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = remember { getAppString(context, R.string.SortByDescendent) }) },
                            onClick = {

                                vm.updateSortType(Sorts.DESCENDENT)
                                vm.updateCurrentArtists(descendentArtists)
                                scope.launch {
                                    vm.updateMenuExpanded(false)
                                    delay(200)
                                    gridState.scrollToItem(index = 0)
                                }
                            }
                        )

                         */

                        DropdownMenuItem(
                            text = {
                                Text(text = stringResource(id = R.string.ModificationDate))
                            },
                            onClick = {

                                val currentSort = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE).getString(Settings.ARTISTS_SORT, Settings.Values.Sort.RECENT)
                                val filterAlgorithm = if (currentSort == Settings.Values.Sort.RECENT) Settings.Values.Sort.OLDEST else Settings.Values.Sort.RECENT
                                val filterArtists = if(currentSort == Settings.Values.Sort.RECENT) oldestArtists else recentArtists

                                vm.updateSortType(filterAlgorithm)
                                vm.updateCurrentArtists(filterArtists)
                                scope.launch {
                                    vm.updateMenuExpanded(false)
                                    delay(200)
                                    gridState.scrollToItem(index = 0)
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

                                val currentSort = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE).getString(Settings.ARTISTS_SORT, Settings.Values.Sort.RECENT)
                                val filterAlgorithm = if (currentSort == Settings.Values.Sort.ASCENDENT) Settings.Values.Sort.DESCENDENT else Settings.Values.Sort.ASCENDENT
                                val filterArtists = if(currentSort == Settings.Values.Sort.ASCENDENT) descendentArtists else ascendentArtists

                                vm.updateSortType(filterAlgorithm)
                                vm.updateCurrentArtists(filterArtists)
                                scope.launch {
                                    vm.updateMenuExpanded(false)
                                    delay(200)
                                    gridState.scrollToItem(index = 0)
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
                    }
                }

                MediumVerticalSpacer()

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
                        key = { artist -> artist.id },
                    ) { artist ->

                        ImageCard(
                            modifier = Modifier.animateItemPlacement(),
                            cardImage = remember { getImage(context, R.drawable.person, ImageSizes.MEDIUM) },
                            imageTint = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                            cardText = remember { artist.name },
                            onCardClicked = {
                                vm.openArtist(activityContext, navController, artist.id)
                            }
                        )
                    }
                }
            }
        }
    }
}