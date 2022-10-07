package com.lighttigerxiv.simple.mp.compose.navigation.screens

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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.lighttigerxiv.simple.mp.compose.Playlist
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.UsefulFunctions
import com.lighttigerxiv.simple.mp.compose.composables.CustomTextField
import com.lighttigerxiv.simple.mp.compose.composables.ImageCard
import com.lighttigerxiv.simple.mp.compose.viewmodels.ActivityMainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun PlaylistsScreen(
    activityMainViewModel: ActivityMainViewModel,
    onGenrePlaylistClick: () -> Unit,
    onPlaylistClick: () -> Unit
){

    val genresList = activityMainViewModel.genresList
    val playlists = activityMainViewModel.playlists.observeAsState().value!!

    println("Playlists => $playlists")
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val playlistIcon = remember { UsefulFunctions.getBitmapFromVectorDrawable(context, R.drawable.icon_playlists).asImageBitmap() }
    val pagerState = rememberPagerState()
    val createPlaylistsScaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()


    val gridCellsCount = when(configuration.orientation){

        Configuration.ORIENTATION_PORTRAIT->2
        else->4
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(activityMainViewModel.surfaceColor.value!!)
    ) {

        Column(modifier = Modifier.fillMaxSize()) {

            TabRow(
                selectedTabIndex = pagerState.currentPage,
                contentColor = activityMainViewModel.surfaceColor.value!!,
                indicator = { tabPositions ->


                    Box(
                        modifier = Modifier
                            .pagerTabIndicatorOffset(pagerState, tabPositions)
                            .height(4.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(10.dp),
                            )
                    )
                }
            ) {

                Tab(
                    text = { Text("Genres", fontSize = 16.sp) },
                    selected = pagerState.currentPage == 0,
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                    onClick = { scope.launch { pagerState.animateScrollToPage(0) } },
                    modifier = Modifier.background(activityMainViewModel.surfaceColor.value!!)
                )
                Tab(
                    text = { Text("Your Playlists", fontSize = 16.sp) },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                    selected = pagerState.currentPage == 1,
                    onClick = { scope.launch { pagerState.animateScrollToPage(1) } },
                    modifier = Modifier.background(activityMainViewModel.surfaceColor.value!!)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            HorizontalPager(
                count = 2,
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { currentPage->


                when(currentPage){

                    0->{
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(gridCellsCount),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.padding(10.dp)
                        ) {

                            items(
                                items = genresList,
                                key = { genre -> genre.genreID },
                            ) { genre ->

                                ImageCard(
                                    cardImage = remember{ UsefulFunctions.getBitmapFromVectorDrawable( context, R.drawable.icon_playlists ) } ,
                                    imageTint = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                                    cardText = remember{ genre.genre },
                                    onCardClicked = {

                                        activityMainViewModel.clickedGenreID.value = genre.genreID
                                        onGenrePlaylistClick()
                                    }
                                )
                            }
                        }
                    }
                    1->{

                        BottomSheetScaffold(
                            scaffoldState = createPlaylistsScaffoldState,
                            sheetShape = RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp),
                            sheetElevation = 10.dp,
                            sheetContent = {
                               Column(
                                   modifier = Modifier
                                       .fillMaxWidth()
                                       .wrapContentHeight()
                                       .background(MaterialTheme.colorScheme.background)
                                       .padding(10.dp)

                               ) {

                                   val playlistNameValue = activityMainViewModel.tfNewPlaylistNameValue.observeAsState().value!!

                                   Spacer(Modifier.height(2.dp))
                                   Row(
                                       modifier = Modifier
                                           .fillMaxWidth()
                                           .wrapContentHeight(),
                                       horizontalArrangement = Arrangement.Center
                                   ){
                                       Row(
                                           modifier = Modifier
                                               .width(40.dp)
                                               .height(5.dp)
                                               .clip(RoundedCornerShape(percent = 100))
                                               .background(MaterialTheme.colorScheme.primary)
                                       ){}
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
                                       onValueChanged = { activityMainViewModel.tfNewPlaylistNameValue.value = it },
                                       textType = "text"
                                   )

                                   Spacer(Modifier.height(10.dp))

                                   Row(
                                       horizontalArrangement = Arrangement.End,
                                       modifier = Modifier
                                           .fillMaxWidth()
                                   ){
                                       Button(
                                           onClick = {

                                               val lastID = if(playlists.size > 0) playlists.maxBy { it.id }.id else 0
                                               val newPlaylistID = lastID + 1

                                               val newPlaylist = Playlist(
                                                   id = newPlaylistID,
                                                   name = playlistNameValue,
                                                   image = null,
                                                   songs = ArrayList()
                                               )

                                               activityMainViewModel.updatePlaylists(newPlaylist)

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
                            sheetPeekHeight = 0.dp,
                        ) { sheetPadding ->

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(activityMainViewModel.surfaceColor.value!!)
                                    .padding(
                                        bottom = sheetPadding.calculateBottomPadding() + 10.dp,
                                        start = sheetPadding.calculateStartPadding(LayoutDirection.Rtl),
                                        end = sheetPadding.calculateEndPadding(LayoutDirection.Ltr),
                                        top = sheetPadding.calculateTopPadding() + 10.dp
                                    )
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
                                    modifier = Modifier.padding(10.dp),
                                    content = {

                                        items(playlists){ playlist ->

                                            val playlistID = remember{playlist.id}
                                            val playlistName = remember{playlist.name}


                                            ImageCard(
                                                cardImage = remember{ UsefulFunctions.getBitmapFromVectorDrawable(context, R.drawable.icon_playlists) },
                                                imageTint = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                                                cardText = playlistName,
                                                onCardClicked = {
                                                    activityMainViewModel.clickedPlaylistID.value = playlistID
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