package com.lighttigerxiv.simple.mp.compose.screens.main.artist

import android.content.res.Configuration
import android.graphics.BitmapFactory
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.lighttigerxiv.simple.mp.compose.*
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.ui.composables.CustomToolbar
import com.lighttigerxiv.simple.mp.compose.ui.composables.PlayAndShuffleRow
import com.lighttigerxiv.simple.mp.compose.ui.composables.SongItem
import com.lighttigerxiv.simple.mp.compose.activities.main.MainVM
import com.lighttigerxiv.simple.mp.compose.data.variables.SCREEN_PADDING
import com.lighttigerxiv.simple.mp.compose.data.variables.SMALL_SPACING
import com.lighttigerxiv.simple.mp.compose.settings.SettingsVM
import com.lighttigerxiv.simple.mp.compose.ui.composables.CustomText
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.MediumHeightSpacer
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.SmallHeightSpacer
import com.lighttigerxiv.simple.mp.compose.functions.getAppString
import com.lighttigerxiv.simple.mp.compose.screens.main.playlists.playlist.modifyIf
import kotlinx.coroutines.launch
import moe.tlaster.nestedscrollview.VerticalNestedScrollView
import moe.tlaster.nestedscrollview.rememberNestedScrollViewState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ArtistScreen(
    mainVM: MainVM,
    settingsVM: SettingsVM,
    artistID: Long,
    artistVM: ArtistScreenVM,
    onBackClicked: () -> Unit,
    onSelectArtistCover: (artistName: String, artistID: Long) -> Unit,
    onOpenAlbum: (albumID: Long) -> Unit
) {

    val context = LocalContext.current

    val configuration = LocalConfiguration.current

    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState()

    val nestedScrollViewState = rememberNestedScrollViewState()

    val surfaceColor = mainVM.surfaceColor.collectAsState().value

    val screenLoaded = artistVM.screenLoaded.collectAsState().value

    val selectedSong = mainVM.selectedSong.collectAsState().value

    val artistName = artistVM.artistName.collectAsState().value

    val artistCover = artistVM.artistCover.collectAsState().value

    val tintCover = artistVM.tintCover.collectAsState().value

    val songs = artistVM.artistSongs.collectAsState().value

    val albums = artistVM.artistAlbums.collectAsState().value

    val showMenu = artistVM.showMenu.collectAsState().value

    val gridCellsCount = when (configuration.orientation) {

        Configuration.ORIENTATION_PORTRAIT -> 2
        else -> 4
    }

    if (!screenLoaded) {
        artistVM.loadScreen(artistID, mainVM, settingsVM)
    }


    VerticalNestedScrollView(
        modifier = Modifier
            .background(surfaceColor)
            .padding(SCREEN_PADDING),
        state = nestedScrollViewState,
        header = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                CustomToolbar(
                    backText = remember { getAppString(context, R.string.Artists) },
                    onBackClick = { onBackClicked() },
                    secondaryContent = {

                        if (screenLoaded) {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            )

                            Column {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(14))
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .padding(5.dp)
                                        .clickable {

                                            artistVM.updateShowMenu(true)
                                        }
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.menu),
                                        contentDescription = "",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier
                                            .height(25.dp)
                                            .aspectRatio(1f)
                                    )
                                }

                                DropdownMenu(
                                    expanded = showMenu,
                                    onDismissRequest = {

                                        artistVM.updateShowMenu(false)
                                    }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text(text = remember { getAppString(context, R.string.ChangeArtistCover) }) },
                                        onClick = {

                                            artistVM.updateShowMenu(false)

                                            onSelectArtistCover(artistName, artistID)
                                        }
                                    )
                                }
                            }
                        }
                    }
                )

                SmallHeightSpacer()

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {

                    Image(
                        bitmap = artistCover.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        colorFilter = if (tintCover) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null,
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .aspectRatio(1f)
                            .padding(5.dp)
                            .clip(RoundedCornerShape(14.dp))
                    )
                }

                MediumHeightSpacer()

                CustomText(
                    modifier = Modifier.fillMaxWidth(),
                    text = artistName,
                    textAlign = TextAlign.Center,
                    size = 20.sp,
                    weight = FontWeight.Bold
                )

                MediumHeightSpacer()
            }
        },
        content = {

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                if (screenLoaded) {
                    androidx.compose.material.TabRow(
                        selectedTabIndex = pagerState.currentPage,
                        contentColor = surfaceColor,
                        indicator = {}
                    ) {

                        val songsTabColor = when (pagerState.currentPage) {

                            0 -> MaterialTheme.colorScheme.surfaceVariant
                            else -> mainVM.surfaceColor.collectAsState().value
                        }

                        val albumsTabColor = when (pagerState.currentPage) {

                            1 -> MaterialTheme.colorScheme.surfaceVariant
                            else -> mainVM.surfaceColor.collectAsState().value
                        }

                        Tab(
                            text = { Text(remember { getAppString(context, R.string.Songs) }, fontSize = 16.sp) },
                            selected = pagerState.currentPage == 0,
                            selectedContentColor = MaterialTheme.colorScheme.primary,
                            unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                            onClick = { scope.launch { pagerState.animateScrollToPage(0) } },
                            modifier = Modifier
                                .background(mainVM.surfaceColor.collectAsState().value)
                                .padding(SMALL_SPACING)
                                .clip(RoundedCornerShape(percent = 100))
                                .background(songsTabColor)
                        )
                        Tab(
                            text = { Text(remember { getAppString(context, R.string.Albums) }, fontSize = 16.sp) },
                            selectedContentColor = MaterialTheme.colorScheme.primary,
                            unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                            selected = pagerState.currentPage == 1,
                            onClick = { scope.launch { pagerState.animateScrollToPage(1) } },
                            modifier = Modifier
                                .background(mainVM.surfaceColor.collectAsState().value)
                                .padding(SMALL_SPACING)
                                .clip(RoundedCornerShape(percent = 100))
                                .background(albumsTabColor)
                        )
                    }

                    MediumHeightSpacer()

                    HorizontalPager(
                        count = 2,
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { currentPage ->

                        when (currentPage) {

                            //************************************************
                            // Songs
                            //************************************************

                            0 -> {

                                Column(
                                    modifier = Modifier.fillMaxSize()
                                ) {

                                    PlayAndShuffleRow(
                                        surfaceColor = surfaceColor,
                                        onPlayClick = {
                                            mainVM.unshuffleAndPlay(songs!!, 0)
                                        },
                                        onSuffleClick = {
                                            mainVM.shuffleAndPlay(songs!!)
                                        }
                                    )

                                    MediumHeightSpacer()

                                    LazyColumn(
                                        verticalArrangement = Arrangement.spacedBy(5.dp),
                                        content = {

                                            items(
                                                items = songs!!,
                                                key = { song -> song.id }
                                            ) { song ->

                                                SongItem(
                                                    song = song,
                                                    songAlbumArt = mainVM.songsImages.collectAsState().value?.find { it.albumID == song.albumID }!!.albumArt,
                                                    highlight = song.path == selectedSong?.path,
                                                    onSongClick = { mainVM.selectSong(songs, songs.indexOf(song)) }
                                                )
                                            }
                                        }
                                    )
                                }
                            }

                            //************************************************
                            // Albums
                            //************************************************

                            1 -> {
                                Spacer(modifier = Modifier.height(20.dp))
                                Column(modifier = Modifier.fillMaxSize()) {

                                    LazyVerticalGrid(
                                        columns = GridCells.Fixed(gridCellsCount),
                                        verticalArrangement = Arrangement.spacedBy(5.dp),
                                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                                        content = {

                                            items(
                                                items = albums!!,
                                                key = { album -> album.albumID },
                                            ) { album ->

                                                val albumSongAlbumID = album.albumID
                                                val albumName = album.album
                                                val albumArt = mainVM.songsImages.collectAsState().value?.first { it.albumID == albumSongAlbumID }?.albumArt

                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .clip(RoundedCornerShape(14.dp))
                                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                                        .clickable {
                                                            onOpenAlbum(album.albumID)
                                                        }

                                                ) {

                                                    Column(
                                                        modifier = Modifier.fillMaxSize(),
                                                        horizontalAlignment = Alignment.CenterHorizontally
                                                    ) {
                                                        Image(
                                                            bitmap = remember { (albumArt ?: BitmapFactory.decodeResource(context.resources, R.drawable.record)).asImageBitmap() },
                                                            colorFilter = if (albumArt == null) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null,
                                                            contentDescription = "",
                                                            modifier = Modifier
                                                                .padding(10.dp)
                                                                .clip(RoundedCornerShape(14.dp))
                                                        )
                                                        Text(
                                                            text = albumName,
                                                            fontSize = 15.sp,
                                                            textAlign = TextAlign.Center,
                                                            maxLines = 1,
                                                            overflow = TextOverflow.Ellipsis,
                                                            color = MaterialTheme.colorScheme.onSurface,
                                                            fontWeight = FontWeight.Medium,
                                                            modifier = Modifier.padding(2.dp)
                                                        )
                                                        Spacer(modifier = Modifier.height(10.dp))
                                                    }
                                                }
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
    )
}

