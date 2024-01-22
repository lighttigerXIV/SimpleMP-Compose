package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.artists.artist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.lighttigerxiv.layout_scaffold.inLandscape
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.frontend.composables.Card
import com.lighttigerxiv.simple.mp.compose.frontend.composables.CollapsableHeader
import com.lighttigerxiv.simple.mp.compose.frontend.composables.DoubleTabRow
import com.lighttigerxiv.simple.mp.compose.frontend.composables.HSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.composables.MenuItem
import com.lighttigerxiv.simple.mp.compose.frontend.composables.MiniPlayerSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.composables.PlayShuffleRow
import com.lighttigerxiv.simple.mp.compose.frontend.composables.SongCard
import com.lighttigerxiv.simple.mp.compose.frontend.composables.Toolbar
import com.lighttigerxiv.simple.mp.compose.frontend.composables.VSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goToArtistAlbum
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goToSelectArtistCover
import com.lighttigerxiv.simple.mp.compose.frontend.utils.FontSizes
import com.lighttigerxiv.simple.mp.compose.frontend.utils.Sizes
import com.lighttigerxiv.simple.mp.compose.frontend.utils.customRememberLazyGridState
import com.lighttigerxiv.simple.mp.compose.frontend.utils.customRememberLazyListState
import com.lighttigerxiv.simple.mp.compose.frontend.utils.customRememberPagerState
import com.lighttigerxiv.simple.mp.compose.frontend.utils.modifyIf

@Composable
fun ArtistScreen(
    artistId: Long,
    navController: NavHostController,
    vm: ArtistScreenVM = viewModel(factory = ArtistScreenVM.Factory),
    showMiniPlayer: Boolean
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

        if (inLandscape()) {

            VSpacer(size = Sizes.LARGE)

            Row(Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.2f)
                ) {
                    ArtistAlbumAndName(uiState = uiState)
                }

                HSpacer(size = Sizes.LARGE)

                Column(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = true)
                ) {
                    SongsAndAlbumsPager(uiState = uiState, vm = vm, navController = navController, showMiniPlayer)
                }
            }

        } else {
            CollapsableHeader(
                header = {

                    VSpacer(size = Sizes.LARGE)

                    ArtistAlbumAndName(uiState = uiState)
                }
            ) {

                VSpacer(size = Sizes.LARGE)

                SongsAndAlbumsPager(uiState = uiState, vm = vm, navController = navController, showMiniPlayer = showMiniPlayer)
            }
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
                    .modifyIf(inLandscape()) {
                        fillMaxWidth()
                        aspectRatio(1f)
                    }
                    .modifyIf(!inLandscape()) {
                        size(220.dp)
                    }
                    .padding(Sizes.MEDIUM)
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = R.drawable.artist),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        } else {
            Image(
                modifier = Modifier
                    .modifyIf(inLandscape()) {
                        fillMaxWidth()
                        aspectRatio(1f)
                    }
                    .modifyIf(!inLandscape()) {
                        size(220.dp)
                    }
                    .clip(RoundedCornerShape(Sizes.XLARGE)),
                bitmap = uiState.artistImage.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }

        VSpacer(size = Sizes.LARGE)

        Text(
            text = uiState.artistName,
            fontSize = FontSizes.HEADER,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongsAndAlbumsPager(
    uiState: ArtistScreenVM.UiState,
    vm: ArtistScreenVM,
    navController: NavHostController,
    showMiniPlayer: Boolean
) {

    val gridCellsCount = if (inLandscape()) 4 else 2
    val pagerState = customRememberPagerState(index = vm.pagerTab, onKill = { vm.pagerTab = it })
    val songsListState = customRememberLazyListState(index = vm.songsPosition, onKill = { vm.songsPosition = it })
    val albumsGridState = customRememberLazyGridState(index = vm.albumsPosition, onKill = { vm.albumsPosition = it })

    DoubleTabRow(
        pagerState = pagerState,
        firstTabTitle = stringResource(id = R.string.songs),
        secondTabTitle = stringResource(id = R.string.albums),
    )

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
                    verticalArrangement = Arrangement.spacedBy(Sizes.SMALL),
                    state = songsListState
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

                    item {
                        MiniPlayerSpacer(isShown = showMiniPlayer)
                    }
                }
            }

            if (page == 1) {

                LazyVerticalGrid(
                    columns = GridCells.Fixed(gridCellsCount),
                    verticalArrangement = Arrangement.spacedBy(Sizes.SMALL),
                    horizontalArrangement = Arrangement.spacedBy(Sizes.SMALL),
                    state = albumsGridState
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

                    item(span = { GridItemSpan(gridCellsCount) }) {
                        MiniPlayerSpacer(isShown = showMiniPlayer)
                    }
                }
            }
        }
    }
}