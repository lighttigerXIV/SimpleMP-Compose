package com.lighttigerxiv.simple.mp.compose.navigation.screens.main

import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lighttigerxiv.simple.mp.compose.*
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.composables.CustomTextField
import com.lighttigerxiv.simple.mp.compose.composables.ImageCard
import com.lighttigerxiv.simple.mp.compose.app_viewmodels.MainVM
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun PlaylistsScreen(
    mainVM: MainVM,
    onGenrePlaylistClick: (position: Int) -> Unit,
    onPlaylistClick: (playlistID: Int) -> Unit
) {

    val genresList = mainVM.genresList
    val playlists = mainVM.currentPlaylistsPLSS.collectAsState().value

    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val pagerState = rememberPagerState()
    val createPlaylistsScaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()
    val userPlaylistsGridState = rememberLazyGridState()



    val gridCellsCount = when (configuration.orientation) {

        Configuration.ORIENTATION_PORTRAIT -> 2
        else -> 4
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(mainVM.surfaceColor.collectAsState().value)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            TabRow(
                modifier = Modifier
                    .padding(
                        top = SCREEN_PADDING,
                        start = SCREEN_PADDING,
                        end = SCREEN_PADDING
                    ),
                selectedTabIndex = pagerState.currentPage,
                contentColor = mainVM.surfaceColor.collectAsState().value,
                indicator = {},
            ) {

                val genrePlaylistColor = when(pagerState.currentPage){

                    0-> MaterialTheme.colorScheme.surfaceVariant
                    else-> mainVM.surfaceColor.collectAsState().value
                }

                val yourPlaylistsColor = when(pagerState.currentPage){

                    1-> MaterialTheme.colorScheme.surfaceVariant
                    else-> mainVM.surfaceColor.collectAsState().value
                }

                Tab(
                    text = { Text(remember { getAppString(context, R.string.Genres) }, fontSize = 16.sp) },
                    selected = pagerState.currentPage == 0,
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                    onClick = { scope.launch { pagerState.animateScrollToPage(0) } },
                    modifier = Modifier
                        .background(mainVM.surfaceColor.collectAsState().value)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(percent = 100))
                        .background(genrePlaylistColor)
                )
                Tab(
                    text = { Text(remember { getAppString(context, R.string.YourPlaylists) }, fontSize = 16.sp) },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                    selected = pagerState.currentPage == 1,
                    onClick = { scope.launch { pagerState.animateScrollToPage(1) } },
                    modifier = Modifier
                        .background(mainVM.surfaceColor.collectAsState().value)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(percent = 100))
                        .background(yourPlaylistsColor)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalPager(
                modifier = Modifier
                    .fillMaxSize(),
                count = 2,
                state = pagerState,
            ) { currentPage ->


                when (currentPage) {

                    0 -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(gridCellsCount),
                            verticalArrangement = Arrangement.spacedBy(5.dp),
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    start = SCREEN_PADDING,
                                    end = SCREEN_PADDING,
                                    bottom = SCREEN_PADDING
                                )
                        ) {

                            itemsIndexed(
                                items = genresList,
                                key = { _, genre -> genre },
                            ) { index, genre ->

                                ImageCard(
                                    cardImage = remember { getBitmapFromVectorDrawable(context, R.drawable.icon_playlists) },
                                    imageTint = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                                    cardText = remember { genre },
                                    onCardClicked = {
                                        onGenrePlaylistClick(index)
                                    }
                                )
                            }
                        }
                    }
                    1 -> {

                        val searchValue = mainVM.searchValuePLSS.collectAsState().value

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

                                    val playlistNameValue = mainVM.tfNewPlaylistNameValue.observeAsState().value!!

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
                                        text = remember { getAppString(context, R.string.PlaylistName) },
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    CustomTextField(
                                        text = playlistNameValue,
                                        placeholder = remember { getAppString(context, R.string.InsertPlaylistName) },
                                        onTextChange = { mainVM.tfNewPlaylistNameValue.value = it },
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

                                                mainVM.createPlaylist( playlistNameValue )
                                                scope.launch { createPlaylistsScaffoldState.bottomSheetState.collapse() }
                                            },
                                            enabled = playlistNameValue.isNotEmpty()
                                        ) {

                                            Text(
                                                text = remember { getAppString(context, R.string.Create) }
                                            )
                                        }
                                    }
                                }
                            },

                        ) { sheetPadding ->

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(mainVM.surfaceColor.collectAsState().value)
                                    .padding(sheetPadding)
                                    .padding(
                                        start = SCREEN_PADDING,
                                        end = SCREEN_PADDING,
                                        bottom = SCREEN_PADDING
                                    )
                            ) {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {

                                    CustomTextField(
                                        text = searchValue,
                                        onTextChange = {
                                            mainVM.setSearchValuePLSS(it)
                                            mainVM.filterPlaylistsPLSS()
                                            scope.launch { userPlaylistsGridState.scrollToItem(0) }
                                        },
                                        placeholder = remember { getAppString(context, R.string.SearchPlaylists) },
                                        sideIcon = R.drawable.icon_plus_solid,
                                        onSideIconClick = {
                                            scope.launch {
                                                createPlaylistsScaffoldState.bottomSheetState.expand()
                                            }
                                        }
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                LazyVerticalGrid(
                                    state = userPlaylistsGridState,
                                    columns = GridCells.Fixed(gridCellsCount),
                                    verticalArrangement = Arrangement.spacedBy(5.dp),
                                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                                    content = {

                                        items(playlists) { playlist ->

                                            val playlistImage = if(playlist.image.isNullOrEmpty()){
                                                getBitmapFromVectorDrawable(context, R.drawable.icon_playlists)
                                            }
                                            else {
                                                val imageBytes = Base64.decode(playlist.image, Base64.DEFAULT)
                                                BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                                            }


                                            ImageCard(
                                                modifier = Modifier.animateItemPlacement(),
                                                cardImage = playlistImage,
                                                imageTint = if(playlist.image.isNullOrEmpty()) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null,
                                                cardText = playlist.name,
                                                onCardClicked = {

                                                    if (playlist.songs != null) {
                                                        val playlistSongs = Gson().fromJson(playlist.songs, object : TypeToken<ArrayList<Song>>() {}.type) as ArrayList<Song>
                                                        mainVM.playlistSongs.value = playlistSongs
                                                        mainVM.currentPlaylistSongs.value = playlistSongs
                                                    } else {
                                                        val playlistSongs = ArrayList<Song>()
                                                        mainVM.playlistSongs.value = playlistSongs
                                                        mainVM.currentPlaylistSongs.value = playlistSongs
                                                    }

                                                    onPlaylistClick(playlist.id)
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