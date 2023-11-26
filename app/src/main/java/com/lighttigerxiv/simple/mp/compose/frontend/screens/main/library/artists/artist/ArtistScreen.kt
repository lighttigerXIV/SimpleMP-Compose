package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.artists.artist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.frontend.composables.Card
import com.lighttigerxiv.simple.mp.compose.frontend.composables.CollapsableHeader
import com.lighttigerxiv.simple.mp.compose.frontend.composables.MenuItem
import com.lighttigerxiv.simple.mp.compose.frontend.composables.PlayShuffleRow
import com.lighttigerxiv.simple.mp.compose.frontend.composables.SongCard
import com.lighttigerxiv.simple.mp.compose.frontend.composables.Toolbar
import com.lighttigerxiv.simple.mp.compose.frontend.composables.VSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goToArtistAlbum
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goToSelectArtistCover
import com.lighttigerxiv.simple.mp.compose.frontend.utils.FontSizes
import com.lighttigerxiv.simple.mp.compose.frontend.utils.Sizes
import com.lighttigerxiv.simple.mp.compose.frontend.utils.modifyIf

@Composable
fun ArtistScreen(
    artistId: Long,
    navController: NavHostController,
    vm: ArtistScreenVM = viewModel(factory = ArtistScreenVM.Factory)
) {

    val uiState = vm.uiState.collectAsState().value

    LaunchedEffect(uiState.loadingRequested) {
        if (!uiState.loadingRequested) {
            vm.loadScreen(artistId)
        }
    }

    Column {
        Toolbar(
            navController = navController,
            endContent = {
                Column {

                    Icon(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .clickable { vm.updateShowMenu(true) },
                        painter = painterResource(id = R.drawable.menu),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Menu(uiState = uiState, vm = vm, navController = navController, artistId)
                }
            }
        )

        CollapsableHeader(
            header = {

                VSpacer(size = Sizes.LARGE)

                ArtistAlbumAndName(uiState = uiState)
            }
        ) {

            VSpacer(size = Sizes.LARGE)

            SongsAndAlbumsPager(uiState = uiState, vm = vm, navController = navController)
        }
    }

}

@Composable
fun Menu(
    uiState: ArtistScreenVM.UiState,
    vm: ArtistScreenVM,
    navController: NavHostController,
    artistId: Long
) {
    DropdownMenu(
        expanded = uiState.showMenu,
        onDismissRequest = { vm.updateShowMenu(false) }
    ) {
        MenuItem(
            iconId = R.drawable.artist,
            text = stringResource(id = R.string.change_artist_image),
            onClick = {
                navController.goToSelectArtistCover(artistId)
                vm.updateShowMenu(false)
            }
        )
    }
}

@Composable
fun ArtistAlbumAndName(
    uiState: ArtistScreenVM.UiState
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (uiState.artistImage == null) {

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(Sizes.XLARGE))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(Sizes.MEDIUM)
            ) {
                Icon(
                    modifier = Modifier.size(220.dp),
                    painter = painterResource(id = R.drawable.artist),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        VSpacer(size = Sizes.LARGE)

        Text(
            text = uiState.artistName,
            fontSize = FontSizes.HEADER,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun TabRow(
    uiState: ArtistScreenVM.UiState,
    vm: ArtistScreenVM
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = true)
                .clip(CircleShape)
                .modifyIf(uiState.currentPagerTab == 0) {
                    background(MaterialTheme.colorScheme.surfaceVariant)
                }
                .clickable { vm.updateCurrentPagerTab(0) }
                .padding(Sizes.MEDIUM),
            horizontalArrangement = Arrangement.Center
        ) {

            Text(
                text = stringResource(id = R.string.Songs),
                fontWeight = FontWeight.Medium,
                color = if (uiState.currentPagerTab == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = true)
                .clip(CircleShape)
                .modifyIf(uiState.currentPagerTab == 1) {
                    background(MaterialTheme.colorScheme.surfaceVariant)
                }
                .clickable { vm.updateCurrentPagerTab(1) }
                .padding(Sizes.MEDIUM),
            horizontalArrangement = Arrangement.Center
        ) {

            Text(
                text = stringResource(id = R.string.Albums),
                fontWeight = FontWeight.Medium,
                color = if (uiState.currentPagerTab == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongsAndAlbumsPager(
    uiState: ArtistScreenVM.UiState,
    vm: ArtistScreenVM,
    navController: NavHostController
) {

    val pagerState = rememberPagerState(pageCount = { 2 })

    LaunchedEffect(pagerState.currentPage) {
        vm.updateCurrentPagerTab(pagerState.currentPage)
    }

    LaunchedEffect(uiState.currentPagerTab) {
        pagerState.animateScrollToPage(uiState.currentPagerTab)
    }

    TabRow(uiState = uiState, vm = vm)

    VSpacer(size = Sizes.LARGE)

    HorizontalPager(
        state = pagerState
    ) { page ->

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            if (page == 0) {

                PlayShuffleRow(
                    onPlayClick = { vm.playSong(uiState.songs[0]) },
                    onShuffleClick = { vm.shuffle() }
                )

                VSpacer(size = Sizes.LARGE)

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(Sizes.SMALL)
                ) {
                    items(
                        items = uiState.songs,
                        key = { it.id }
                    ) { song ->
                        SongCard(
                            song = song,
                            artistName = uiState.artistName,
                            art = vm.getAlbumArt(song.albumId),
                            onClick = { vm.playSong(song) },
                            highlight = uiState.currentSong == song
                        )
                    }
                }
            }

            if (page == 1) {

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(Sizes.SMALL),
                    horizontalArrangement = Arrangement.spacedBy(Sizes.SMALL)
                ) {
                    items(
                        items = uiState.albums,
                        key = { it.id }
                    ) { album ->

                        Card(
                            image = vm.getAlbumArt(album.id),
                            defaultIconId = R.drawable.album,
                            text = album.name,
                            onClick = { navController.goToArtistAlbum(album.id) }
                        )
                    }
                }
            }
        }
    }
}