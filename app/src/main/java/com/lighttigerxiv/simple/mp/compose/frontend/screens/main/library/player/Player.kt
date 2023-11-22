package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.player

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.constraintlayout.compose.layoutId
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.backend.playback.RepeatSate
import com.lighttigerxiv.simple.mp.compose.frontend.composables.HSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.composables.PlayingListSongCard
import com.lighttigerxiv.simple.mp.compose.frontend.composables.VSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.utils.Sizes
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.drop
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.ReorderableLazyListState
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@OptIn(ExperimentalMotionApi::class)
@Composable
fun Player(
    vm: PlayerVM = viewModel(factory = PlayerVM.Factory),
    hideMiniPlayer: Boolean,
    onOpenPlayer: () -> Unit,
    onClosePlayer: () -> Unit,
    showPlayerProgress: Float
) {

    val uiState = vm.uiState.collectAsState().value
    val playingPlaylistListState = rememberReorderableLazyListState(onMove = { from, to -> vm.reorderPlayingPlaylist(from.key, to.key) })
    val context = LocalContext.current

    LaunchedEffect(uiState.showUpNextPlaylist) {
        playingPlaylistListState.listState.scrollToItem(0)
    }

    if (!hideMiniPlayer) {
        MiniPlayer(vm = vm, uiState = uiState, onClick = { onOpenPlayer() })
    }

    MotionLayout(
        modifier = Modifier
            .fillMaxSize(),
        motionScene = MotionScene(
            content = context.resources
                .openRawResource(R.raw.player_visibility)
                .readBytes()
                .decodeToString()
        ),
        progress = showPlayerProgress
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .layoutId("player")
        ) {

            Column(
                modifier = Modifier

                    .padding(Sizes.XLARGE)
            ) {

                TopRow(
                    vm = vm,
                    uiState = uiState,
                    onClosePlayer = { onClosePlayer() }
                )

                VSpacer(size = Sizes.LARGE)


                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (!uiState.showUpNextPlaylist) {
                        AlbumArtPager(vm = vm, uiState = uiState)

                        VSpacer(size = Sizes.LARGE)

                        NameAndTitleRow(uiState = uiState)

                        VSpacer(size = Sizes.LARGE)

                        PlayerSlider(vm = vm, uiState = uiState)

                        VSpacer(size = Sizes.LARGE)

                        MediaButtons(vm = vm, uiState = uiState)
                    }

                    if (uiState.showUpNextPlaylist) {

                        PlayingPlaylist(uiState, vm, playingPlaylistListState)
                    }
                }
            }
        }
    }
}

@Composable
fun MiniPlayer(
    vm: PlayerVM,
    uiState: PlayerVM.UiState,
    onClick: () -> Unit
) {

    uiState.currentSong?.let {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .clip(RoundedCornerShape(Sizes.SMALL))
                .clickable { onClick() }
                .padding(Sizes.MEDIUM),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (uiState.smallAlbumArt != null) {
                Image(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(Sizes.SMALL)),
                    bitmap = uiState.smallAlbumArt.asImageBitmap(),
                    contentDescription = null
                )
            } else {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(Sizes.SMALL))
                        .size(70.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant)

                ) {
                    Icon(
                        modifier = Modifier.size(70.dp),
                        painter = painterResource(id = R.drawable.album),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }


            HSpacer(size = Sizes.LARGE)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = true)
            ) {

                Text(
                    text = uiState.currentSong.name,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = uiState.currentSongArtistName,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            HSpacer(size = Sizes.LARGE)

            Icon(
                modifier = Modifier
                    .size(22.dp)
                    .clickable { vm.pauseOrResume() },
                painter = painterResource(id = if (uiState.isPlaying) R.drawable.pause else R.drawable.play),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun TopRow(
    vm: PlayerVM,
    uiState: PlayerVM.UiState,
    onClosePlayer: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {

        Icon(
            modifier = Modifier
                .size(36.dp)
                .clickable { onClosePlayer() },
            painter = painterResource(id = R.drawable.icon_arrow_down_solid),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = true)
        )

        Row(
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (!uiState.showUpNextPlaylist) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
                    .clickable {
                        vm.updateShowPlaylist(false)
                    }
                    .padding(Sizes.SMALL)
                    .padding(start = Sizes.LARGE, end = Sizes.LARGE),
            ) {

                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = R.drawable.music_note),
                    contentDescription = null,
                    tint = if (!uiState.showUpNextPlaylist) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                )

                HSpacer(size = Sizes.XSMALL)

                Text(
                    text = stringResource(id = R.string.song),
                    color = if (!uiState.showUpNextPlaylist) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                )
            }

            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (uiState.showUpNextPlaylist) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
                    .clickable {
                        vm.updateShowPlaylist(true)
                    }
                    .padding(Sizes.SMALL)
                    .padding(start = Sizes.LARGE, end = Sizes.LARGE),
            ) {

                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = R.drawable.queue_list),
                    contentDescription = null,
                    tint = if (uiState.showUpNextPlaylist) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                )

                HSpacer(size = Sizes.XSMALL)

                Text(
                    text = stringResource(id = R.string.Playlist),
                    color = if (uiState.showUpNextPlaylist) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = true)
        )

        Icon(
            modifier = Modifier
                .size(36.dp)
                .clickable { onClosePlayer() },
            painter = painterResource(id = R.drawable.vertical_three_dots),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlbumArtPager(
    vm: PlayerVM,
    uiState: PlayerVM.UiState
) {

    val pagerState = rememberPagerState(pageCount = { uiState.playingPlaylist.size })

    LaunchedEffect(uiState.currentSong, uiState.playingPlaylist) {
        if (uiState.playingPlaylist.isNotEmpty()) {
            pagerState.scrollToPage(uiState.currentSongPosition)
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow(pagerState::isScrollInProgress)
            .drop(1)
            .collect {scrolling->
                if(!scrolling){
                    if(pagerState.settledPage < vm.getSongPosition()){
                        vm.skipToPrevious(false)
                    }

                    if(pagerState.settledPage > vm.getSongPosition()){
                        vm.skipToNext()
                    }
                }
            }
    }

    HorizontalPager(
        state = pagerState,
        pageSpacing = 16.dp

    ) { pagerTabPosition ->

        // Explanation:
        // It only loads the current album art and the 2 next to it and shows the current album art on the others,
        // so the effect of flickering disappears since the pager shows the previous pages even without animations

        if (pagerTabPosition in (uiState.currentSongPosition - 1)..(uiState.currentSongPosition + 1)) {
            val art = vm.getSongArt(uiState.playingPlaylist[pagerTabPosition].albumId)

            PlayerAlbumArt(art = art)
        } else {
            PlayerAlbumArt(art = uiState.smallAlbumArt)
        }
    }
}

@Composable
fun PlayerAlbumArt(art: Bitmap?) {
    if (art != null) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(Sizes.LARGE)),
            bitmap = art.asImageBitmap(),
            contentDescription = null
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(MaterialTheme.colorScheme.surface)
                .clip(RoundedCornerShape(Sizes.LARGE))
        ) {

            Icon(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Sizes.LARGE),
                painter = painterResource(id = R.drawable.album),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun NameAndTitleRow(
    uiState: PlayerVM.UiState
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = uiState.currentSong?.name ?: "n/a",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = uiState.currentSongArtistName,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}


@Composable
fun PlayerSlider(
    vm: PlayerVM,
    uiState: PlayerVM.UiState
) {
    var sliderValue by remember { mutableIntStateOf(uiState.currentProgress / 1000) }
    val interactionSource = remember { MutableInteractionSource() }
    val draggingSlider = interactionSource.collectIsDraggedAsState().value

    LaunchedEffect(uiState.currentProgress, uiState.currentSong) {
        if (!draggingSlider) {
            sliderValue = uiState.currentProgress / 1000
            vm.updateCurrentProgressAsTime(uiState.currentProgress)
        }
    }

    uiState.currentSong?.let {
        Column {

            Slider(
                value = sliderValue.toFloat(),
                interactionSource = interactionSource,
                onValueChange = {
                    sliderValue = it.toInt()
                    vm.updateCurrentProgressAsTime(it.toInt() * 1000)
                },
                onValueChangeFinished = {
                    if (!uiState.isPlaying) vm.pauseOrResume()
                    vm.seekTo(sliderValue)
                },
                valueRange = 1f..(uiState.currentSong.duration / 1000).toFloat(),
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary,
                    inactiveTrackColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {

                Text(
                    text = uiState.currentProgressAsTime,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = true)
                )

                Text(
                    text = uiState.songDurationAsTime,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


@Composable
fun MediaButtons(
    vm: PlayerVM,
    uiState: PlayerVM.UiState
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            if (uiState.shuffle) {
                VSpacer(size = Sizes.XSMALL + Sizes.SMALL)
            }

            Icon(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable { vm.toggleShuffle() },
                painter = painterResource(id = R.drawable.shuffle),
                contentDescription = null,
                tint = if (uiState.shuffle) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (uiState.shuffle) {
                VSpacer(size = Sizes.XSMALL)
                MediaButtonDot()
            }
        }

        Icon(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(Sizes.SMALL))
                .clickable { vm.skipToPrevious() },
            painter = painterResource(id = R.drawable.previous),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Icon(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .clickable { vm.pauseOrResume() },
            painter = painterResource(id = if (uiState.isPlaying) R.drawable.pause_round else R.drawable.play_round),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Icon(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(Sizes.SMALL))
                .clickable { vm.skipToNext() },
            painter = painterResource(id = R.drawable.next),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            if (uiState.repeatState != RepeatSate.Off) {
                VSpacer(size = Sizes.XSMALL + Sizes.SMALL)
            }

            Icon(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable { vm.toggleRepeatState() },
                painter = painterResource(id = R.drawable.repeat),
                contentDescription = null,
                tint = if (uiState.repeatState != RepeatSate.Off) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (uiState.repeatState != RepeatSate.Off) {
                VSpacer(size = Sizes.XSMALL)
                Row {
                    MediaButtonDot()
                    if (uiState.repeatState == RepeatSate.Endless) {
                        HSpacer(size = Sizes.XSMALL)
                        MediaButtonDot()
                    }
                }
            }
        }
    }
}

@Composable
fun MediaButtonDot() {
    Box(
        modifier = Modifier
            .size(Sizes.SMALL)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayingPlaylist(
    uiState: PlayerVM.UiState,
    vm: PlayerVM,
    reorderableState: ReorderableLazyListState
) {
    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .then(Modifier.reorderable(reorderableState)),
            state = reorderableState.listState
        ) {
            itemsIndexed(
                items = uiState.upNextPlaylist,
                key = { _, song -> song.id }
            ) { _, song ->

                ReorderableItem(
                    reorderableState = reorderableState,
                    key = { song.id }
                ) { isDragging ->

                    Column {
                        PlayingListSongCard(
                            song = song,
                            artistName = vm.getArtistName(song.artistId),
                            art = vm.getSmallSongArt(song.albumId),
                            state = reorderableState,
                            isDragging = isDragging
                        )

                        VSpacer(size = Sizes.SMALL)
                    }
                }
            }
        }
    }
}