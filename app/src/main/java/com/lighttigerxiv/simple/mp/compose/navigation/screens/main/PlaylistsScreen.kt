package com.lighttigerxiv.simple.mp.compose.navigation.screens.main

import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TabRow
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.SCREEN_PADDING
import com.lighttigerxiv.simple.mp.compose.Song
import com.lighttigerxiv.simple.mp.compose.UsefulFunctions
import com.lighttigerxiv.simple.mp.compose.composables.CustomTextField
import com.lighttigerxiv.simple.mp.compose.composables.ImageCard
import com.lighttigerxiv.simple.mp.compose.viewmodels.ActivityMainVM
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun PlaylistsScreen(
    activityMainVM: ActivityMainVM,
    onGenrePlaylistClick: () -> Unit,
    onPlaylistClick: () -> Unit
) {

    val genresList = activityMainVM.genresList
    val playlists = activityMainVM.playlists.observeAsState().value!!

    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val pagerState = rememberPagerState()
    val createPlaylistsScaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()




    val gridCellsCount = when (configuration.orientation) {

        Configuration.ORIENTATION_PORTRAIT -> 2
        else -> 4
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(activityMainVM.surfaceColor.collectAsState().value)
    ) {

        Column(modifier = Modifier.fillMaxSize()) {

            TabRow(
                selectedTabIndex = pagerState.currentPage,
                contentColor = activityMainVM.surfaceColor.collectAsState().value,
                indicator = {},
            ) {

                val genrePlaylistColor = when(pagerState.currentPage){

                    0-> MaterialTheme.colorScheme.surfaceVariant
                    else-> activityMainVM.surfaceColor.collectAsState().value
                }

                val yourPlaylistsColor = when(pagerState.currentPage){

                    1-> MaterialTheme.colorScheme.surfaceVariant
                    else-> activityMainVM.surfaceColor.collectAsState().value
                }

                Tab(
                    text = { Text("Genres", fontSize = 16.sp) },
                    selected = pagerState.currentPage == 0,
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                    onClick = { scope.launch { pagerState.animateScrollToPage(0) } },
                    modifier = Modifier
                        .background(activityMainVM.surfaceColor.collectAsState().value)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(percent = 100))
                        .background(genrePlaylistColor)
                )
                Tab(
                    text = { Text("Your Playlists", fontSize = 16.sp) },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                    selected = pagerState.currentPage == 1,
                    onClick = { scope.launch { pagerState.animateScrollToPage(1) } },
                    modifier = Modifier
                        .background(activityMainVM.surfaceColor.collectAsState().value)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(percent = 100))
                        .background(yourPlaylistsColor)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalPager(
                count = 2,
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { currentPage ->


                when (currentPage) {

                    0 -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(gridCellsCount),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(14.dp)
                        ) {

                            items(
                                items = genresList,
                                key = { genre -> genre.genreID },
                            ) { genre ->

                                ImageCard(
                                    cardImage = remember { UsefulFunctions.getBitmapFromVectorDrawable(context, R.drawable.icon_playlists) },
                                    imageTint = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                                    cardText = remember { genre.genre },
                                    onCardClicked = {

                                        activityMainVM.clickedGenreID.value = genre.genreID
                                        onGenrePlaylistClick()
                                    }
                                )
                            }
                        }
                    }
                    1 -> {

                        BottomSheetScaffold(
                            scaffoldState = createPlaylistsScaffoldState,
                            sheetShape = RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp),
                            sheetPeekHeight = 0.dp,
                            sheetContent = {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                        .border(2.dp, MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                                        .background(MaterialTheme.colorScheme.background)
                                        .padding(10.dp)

                                ) {

                                    val playlistNameValue = activityMainVM.tfNewPlaylistNameValue.observeAsState().value!!

                                    Spacer(Modifier.height(2.dp))
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .wrapContentHeight(),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .width(40.dp)
                                                .height(5.dp)
                                                .clip(RoundedCornerShape(percent = 100))
                                                .background(MaterialTheme.colorScheme.primary)
                                        ) {}
                                    }

                                    Spacer(Modifier.height(10.dp))

                                    Text(
                                        text = "Playlist Name",
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    CustomTextField(
                                        text = playlistNameValue,
                                        placeholder = "Insert playlist name",
                                        onTextChange = { activityMainVM.tfNewPlaylistNameValue.value = it },
                                        textType = "text"
                                    )

                                    Spacer(Modifier.height(10.dp))

                                    Row(
                                        horizontalArrangement = Arrangement.End,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ) {
                                        Button(
                                            onClick = {

                                                activityMainVM.createPlaylist( playlistNameValue )
                                                scope.launch { createPlaylistsScaffoldState.bottomSheetState.collapse() }
                                            }
                                        ) {

                                            Text(
                                                text = "Create"
                                            )
                                        }
                                    }
                                }
                            },

                        ) { sheetPadding ->

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(activityMainVM.surfaceColor.collectAsState().value)
                                    .padding(sheetPadding)
                                    .padding(14.dp)
                            ) {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {

                                    Button(
                                        onClick = { scope.launch { createPlaylistsScaffoldState.bottomSheetState.expand() } },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.Transparent
                                        ),
                                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                                        modifier = Modifier
                                            .wrapContentWidth()
                                            .height(50.dp)
                                            .clip(RoundedCornerShape(percent = 100))
                                    ) {

                                        Icon(
                                            painter = painterResource(id = R.drawable.icon_plus_regular),
                                            contentDescription = "",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier
                                                .height(14.dp)
                                                .width(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = "Create Playlist",
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(gridCellsCount),
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    content = {

                                        items(playlists) { playlist ->

                                            val playlistID = remember { playlist.id }
                                            val playlistName = remember { playlist.name }


                                            ImageCard(
                                                cardImage = remember { UsefulFunctions.getBitmapFromVectorDrawable(context, R.drawable.icon_playlists) },
                                                imageTint = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                                                cardText = playlistName,
                                                onCardClicked = {
                                                    activityMainVM.clickedPlaylistID.value = playlistID

                                                    if (playlist.songs != null) {
                                                        val playlistSongs = Gson().fromJson(playlist.songs, object : TypeToken<ArrayList<Song>>() {}.type) as ArrayList<Song>
                                                        activityMainVM.playlistSongs.value = playlistSongs
                                                        activityMainVM.currentPlaylistSongs.value = playlistSongs
                                                    } else {
                                                        val playlistSongs = ArrayList<Song>()
                                                        activityMainVM.playlistSongs.value = playlistSongs
                                                        activityMainVM.currentPlaylistSongs.value = playlistSongs
                                                    }

                                                    onPlaylistClick()
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
        }
    }
}