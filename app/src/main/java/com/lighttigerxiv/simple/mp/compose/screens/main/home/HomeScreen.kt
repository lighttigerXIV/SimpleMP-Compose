package com.lighttigerxiv.simple.mp.compose.screens.main.home

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.data.variables.SCREEN_PADDING
import com.lighttigerxiv.simple.mp.compose.ui.composables.CustomTextField
import com.lighttigerxiv.simple.mp.compose.activities.main.MainVM
import com.lighttigerxiv.simple.mp.compose.data.variables.Routes
import com.lighttigerxiv.simple.mp.compose.data.variables.Settings
import com.lighttigerxiv.simple.mp.compose.ui.composables.NewSongItem
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.MediumVerticalSpacer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("FrequentlyChangedStateReadInComposition")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    mainVM: MainVM,
    vm: HomeScreenVM,
    onOpenScreen: (screen: String) -> Unit
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val screenLoaded = vm.screenLoaded.collectAsState().value
    val searchText = vm.searchText.collectAsState().value
    val menuExpanded = vm.menuExpanded.collectAsState().value
    val songs = mainVM.songsData.collectAsState().value?.songs
    val currentSongs = vm.currentSongs.collectAsState().value
    val recentSongs = vm.recentSongs.collectAsState().value
    val oldestSongs = vm.oldestSongs.collectAsState().value
    val ascendentSongs = vm.ascendentSongs.collectAsState().value
    val descendentSongs = vm.descendentSongs.collectAsState().value
    val syncingSongs = vm.syncingSongs.collectAsState().value

    val pullRefreshState = rememberPullRefreshState(
        refreshing = syncingSongs,
        onRefresh = {
            vm.reloadSongs(mainVM)
        }
    )

    if (!screenLoaded) {
        vm.loadScreen(mainVM)
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
            .fillMaxSize()
            .background(mainVM.surfaceColor.collectAsState().value)
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
                            shape = RoundedCornerShape(14.dp),
                            containerColor = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(60.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.shuffle),
                                contentDescription = "Shuffle",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier
                                    .size(50.dp)
                                    .fillMaxSize()
                            )
                        }
                    }
                }
            ) { scaffoldPadding ->

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(mainVM.surfaceColor.collectAsState().value)
                        .padding(scaffoldPadding)
                        .pullRefresh(pullRefreshState)
                ) {

                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {

                            CustomTextField(
                                text = searchText,
                                placeholder = stringResource(id = R.string.SearchSongs),
                                onTextChange = {

                                    vm.updateSearchText(it)

                                    vm.filterSongs()

                                    scope.launch {
                                        listState.scrollToItem(0)
                                    }
                                },
                                sideIcon = R.drawable.menu,
                                onSideIconClick = {
                                    vm.updateMenuExpanded(true)
                                }
                            )
                            DropdownMenu(
                                expanded = menuExpanded,
                                onDismissRequest = {
                                    vm.updateMenuExpanded(false)
                                }
                            ) {

                                DropdownMenuItem(
                                    text = {
                                        Text(text = stringResource(id = R.string.ModificationDate))
                                    },
                                    onClick = {

                                        val currentSort = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE).getString(Settings.HOME_SORT, Settings.Values.Sort.RECENT)
                                        val filterAlgorithm = if (currentSort == Settings.Values.Sort.RECENT) Settings.Values.Sort.OLDEST else Settings.Values.Sort.RECENT
                                        val filterSongs = if(currentSort == Settings.Values.Sort.RECENT) oldestSongs else recentSongs

                                        vm.updateSortType(filterAlgorithm)
                                        vm.updateCurrentSongs(filterSongs)
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

                                        val currentSort = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE).getString(Settings.HOME_SORT, Settings.Values.Sort.RECENT)
                                        val filterAlgorithm = if (currentSort == Settings.Values.Sort.ASCENDENT) Settings.Values.Sort.DESCENDENT else Settings.Values.Sort.ASCENDENT
                                        val filterSongs = if(currentSort == Settings.Values.Sort.ASCENDENT) descendentSongs else ascendentSongs

                                        vm.updateSortType(filterAlgorithm)
                                        vm.updateCurrentSongs(filterSongs)
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
                                        Text(text = stringResource(id = R.string.ReloadSongs))
                                    },
                                    onClick = {
                                        vm.reloadSongs(mainVM)
                                    },
                                    leadingIcon = {
                                        Icon(
                                            modifier = Modifier
                                                .height(20.dp)
                                                .width(20.dp),
                                            painter = painterResource(id = R.drawable.reload),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                )

                                DropdownMenuItem(
                                    text = {
                                        Text(text = stringResource(id = R.string.Settings))
                                    },
                                    onClick = {

                                        vm.updateMenuExpanded(false)
                                        onOpenScreen(Routes.Root.SETTINGS)
                                    },
                                    leadingIcon = {
                                        Icon(
                                            modifier = Modifier
                                                .height(20.dp)
                                                .width(20.dp),
                                            painter = painterResource(id = R.drawable.settings),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                )

                                DropdownMenuItem(
                                    text = {
                                        Text(text = stringResource(id = R.string.About))
                                    },
                                    onClick = {

                                        vm.updateMenuExpanded(false)

                                        onOpenScreen(Routes.Root.ABOUT)
                                    },
                                    leadingIcon = {
                                        Icon(
                                            modifier = Modifier
                                                .height(20.dp)
                                                .width(20.dp),
                                            painter = painterResource(id = R.drawable.info),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                )
                            }
                        }

                        MediumVerticalSpacer()

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize(),
                            state = listState,
                            content = {

                                items(
                                    items = currentSongs,
                                    key = { song -> song.id }
                                ) { song ->

                                    NewSongItem(
                                        modifier = Modifier.animateItemPlacement(),
                                        song = song,
                                        mainVM = mainVM,
                                        menuEntries = menuEntries,
                                        onMenuClicked = { option ->

                                            when (option) {
                                                "Artist" -> onOpenScreen("${Routes.Root.FLOATING_ARTIST}${song.artistID}")
                                                "Album" -> onOpenScreen("${Routes.Root.FLOATING_ALBUM}${song.albumID}")
                                                "Playlist" -> onOpenScreen("${Routes.Root.ADD_SONG_TO_PLAYLIST}${song.id}")
                                            }
                                        },
                                        onSongClick = {
                                            vm.selectSong(song, mainVM)
                                        }
                                    )
                                }
                            }
                        )
                    }

                    PullRefreshIndicator(
                        refreshing = syncingSongs,
                        state = pullRefreshState,
                        backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                }
            }
        }
    }
}


