package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.albums.album

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.frontend.composables.CollapsableHeader
import com.lighttigerxiv.simple.mp.compose.frontend.composables.PlayShuffleRow
import com.lighttigerxiv.simple.mp.compose.frontend.composables.SongCard
import com.lighttigerxiv.simple.mp.compose.frontend.composables.Toolbar
import com.lighttigerxiv.simple.mp.compose.frontend.composables.VSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.utils.FontSizes
import com.lighttigerxiv.simple.mp.compose.frontend.utils.Sizes

@Composable
fun AlbumScreen(
    albumId: Long,
    navController: NavHostController,
    vm: AlbumScreenVM = viewModel(factory = AlbumScreenVM.Factory)
) {
    val uiState = vm.uiState.collectAsState().value

    LaunchedEffect(uiState.loadingRequested) {
        if (!uiState.loadingRequested) {
            vm.loadScreen(albumId)
        }
    }

    Column {
        Toolbar(navController = navController)

        if (!uiState.isLoading) {
            CollapsableHeader(
                header = {
                    AlbumArtAndName(uiState = uiState)
                }
            ) {

                VSpacer(size = Sizes.LARGE)

                PlayShuffleRow(
                    onPlayClick = { vm.playSong(uiState.songs[0]) },
                    onShuffleClick = { vm.shuffle() }
                )

                VSpacer(size = Sizes.LARGE)

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(Sizes.SMALL)
                ) {
                    items(
                        items = uiState.songs,
                        key = { it.id }
                    ) {song->

                        SongCard(
                            song = song,
                            artistName = uiState.artistName,
                            art = uiState.smallAlbumArt,
                            onClick = { vm.playSong(song) },
                            highlight = uiState.currentSong?.id == song.id
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AlbumArtAndName(
    uiState: AlbumScreenVM.UiState
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (uiState.largeAlbumArt == null) {

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(Sizes.XLARGE))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(Sizes.MEDIUM)
            ) {
                Icon(
                    modifier = Modifier.size(180.dp),
                    painter = painterResource(id = R.drawable.album),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        } else {
            Image(
                modifier = Modifier
                    .size(180.dp)
                    .clip(RoundedCornerShape(Sizes.XLARGE)),
                bitmap = uiState.largeAlbumArt.asImageBitmap(),
                contentDescription = null,
            )
        }

        VSpacer(size = Sizes.LARGE)

        Text(
            text = uiState.albumName,
            fontSize = FontSizes.HEADER,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}