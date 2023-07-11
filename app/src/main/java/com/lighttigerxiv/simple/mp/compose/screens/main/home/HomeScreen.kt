package com.lighttigerxiv.simple.mp.compose.screens.main.home

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.data.variables.SCREEN_PADDING
import com.lighttigerxiv.simple.mp.compose.ui.composables.CustomTextField
import com.lighttigerxiv.simple.mp.compose.activities.main.MainVM
import com.lighttigerxiv.simple.mp.compose.data.variables.Routes
import com.lighttigerxiv.simple.mp.compose.data.variables.SMALL_SPACING
import com.lighttigerxiv.simple.mp.compose.data.variables.Sorts
import com.lighttigerxiv.simple.mp.compose.functions.getAppString
import com.lighttigerxiv.simple.mp.compose.ui.composables.CustomText
import com.lighttigerxiv.simple.mp.compose.ui.composables.NewSongItem
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.MediumVerticalSpacer
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.SmallHorizontalSpacer
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.SmallVerticalSpacer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("FrequentlyChangedStateReadInComposition")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
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
    val showReloadSongs = vm.showReloadSongsDialog.collectAsState().value
    val songCount = mainVM.songCount.collectAsState().value
    val indexedSongsCount = mainVM.indexedSongsCount.collectAsState().value

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
                                    Text(text = stringResource(id = R.string.SortByRecentlyAdded))
                                },
                                onClick = {
                                    vm.updateSortType(Sorts.RECENT)
                                    vm.updateCurrentSongs(recentSongs)
                                    scope.launch {
                                        vm.updateMenuExpanded(false)
                                        delay(200)
                                        listState.scrollToItem(index = 0)
                                    }
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(text = stringResource(id = R.string.SortByOldestAdded))
                                },
                                onClick = {
                                    vm.updateSortType(Sorts.OLDEST)
                                    vm.updateCurrentSongs(oldestSongs)
                                    scope.launch {
                                        vm.updateMenuExpanded(false)
                                        delay(200)
                                        listState.scrollToItem(index = 0)
                                    }
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(text = stringResource(id = R.string.SortByAscendent))
                                },
                                onClick = {
                                    vm.updateSortType(Sorts.ASCENDENT)
                                    vm.updateCurrentSongs(ascendentSongs)
                                    scope.launch {
                                        vm.updateMenuExpanded(false)
                                        delay(200)
                                        listState.scrollToItem(index = 0)
                                    }
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(text = stringResource(id = R.string.SortByDescendent))
                                },
                                onClick = {
                                    vm.updateSortType(Sorts.DESCENDENT)
                                    vm.updateCurrentSongs(descendentSongs)
                                    scope.launch {
                                        vm.updateMenuExpanded(false)
                                        delay(200)
                                        listState.scrollToItem(index = 0)
                                    }
                                }
                            )

                            DropdownMenuItem(
                                text = {
                                    Text(text = stringResource(id = R.string.ReloadSongs))
                                },
                                onClick = {
                                    vm.reloadSongs(mainVM)
                                }
                            )

                            DropdownMenuItem(
                                text = {
                                    Text(text = stringResource(id = R.string.Settings))
                                },
                                onClick = {

                                    vm.updateMenuExpanded(false)
                                    onOpenScreen(Routes.Root.SETTINGS)
                                }
                            )

                            DropdownMenuItem(
                                text = {
                                    Text(text = stringResource(id = R.string.About))
                                },
                                onClick = {

                                    vm.updateMenuExpanded(false)

                                    onOpenScreen(Routes.Root.ABOUT)
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
            }

            if (showReloadSongs) {

                Dialog(
                    onDismissRequest = { vm.updateShowReloadSongsDialog(false) },
                    properties = DialogProperties(
                        dismissOnClickOutside = false,
                        dismissOnBackPress = false
                    )
                ) {

                    Surface(shape = RoundedCornerShape(14.dp)) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(SMALL_SPACING)
                        ) {

                            CustomText(text = getAppString(context, R.string.ReloadingSongs), weight = FontWeight.Bold)

                            Text(color = MaterialTheme.colorScheme.onSurface, text = getAppString(context, R.string.DontCloseAppWhileReloading))

                            SmallVerticalSpacer()

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                CustomText(text = indexedSongsCount.toString())

                                SmallHorizontalSpacer()

                                LinearProgressIndicator(
                                    modifier = Modifier.fillMaxWidth().weight(1f, fill = true),
                                    color = MaterialTheme.colorScheme.primary,
                                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                                )

                                SmallHorizontalSpacer()

                                CustomText(text = songCount.toString())
                            }

                            SmallVerticalSpacer()


                        }
                    }
                }
            }
        }
    }
}


