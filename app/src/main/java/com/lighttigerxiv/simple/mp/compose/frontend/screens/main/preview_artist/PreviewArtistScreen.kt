package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.preview_artist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.lighttigerxiv.layout_scaffold.inLandscape
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.frontend.composables.CollapsableHeader
import com.lighttigerxiv.simple.mp.compose.frontend.composables.HSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.composables.PlayShuffleRow
import com.lighttigerxiv.simple.mp.compose.frontend.composables.SongCard
import com.lighttigerxiv.simple.mp.compose.frontend.composables.Toolbar
import com.lighttigerxiv.simple.mp.compose.frontend.composables.VSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.utils.FontSizes
import com.lighttigerxiv.simple.mp.compose.frontend.utils.Sizes
import com.lighttigerxiv.simple.mp.compose.frontend.utils.modifyIf

@Composable
fun PreviewArtistScreen(
    artistId: Long,
    rootController: NavHostController,
    vm: PreviewArtistScreenVM = viewModel(factory = PreviewArtistScreenVM.Factory)
) {

    val uiState = vm.uiState.collectAsState().value

    LaunchedEffect(uiState.requestedLoading) {
        if (!uiState.requestedLoading) {
            vm.load(artistId)
        }
    }

    Column {
        Toolbar(navController = rootController)

        if (!uiState.isLoading) {

            VSpacer(size = Sizes.LARGE)

            if (inLandscape()) {

                Row(Modifier.fillMaxWidth()) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .weight(0.2f, fill = true)
                    ) {
                        ArtistAlbumArtAndName(uiState = uiState)
                    }

                    HSpacer(size = Sizes.LARGE)

                    Column(
                        Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = true)
                    ) {
                        Songs(uiState = uiState, vm = vm)
                    }
                }

            } else {
                CollapsableHeader(
                    header = {
                        ArtistAlbumArtAndName(uiState = uiState)
                    }
                ) {

                    VSpacer(size = Sizes.LARGE)

                    Songs(uiState = uiState, vm = vm)
                }
            }
        }
    }
}

@Composable
fun ArtistAlbumArtAndName(
    uiState: PreviewArtistScreenVM.UiState
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
                    modifier = Modifier
                        .modifyIf(inLandscape()) {
                            fillMaxWidth()
                            aspectRatio(1f)
                        }
                        .modifyIf(!inLandscape()) {
                            size(220.dp)
                        },
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


@Composable
fun Songs(
    uiState: PreviewArtistScreenVM.UiState,
    vm: PreviewArtistScreenVM
) {
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