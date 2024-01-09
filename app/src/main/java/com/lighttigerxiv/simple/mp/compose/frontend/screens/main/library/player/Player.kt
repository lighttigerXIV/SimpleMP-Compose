@file:OptIn(ExperimentalMaterial3Api::class)

package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.player

import android.graphics.Bitmap
import android.view.View
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.constraintlayout.compose.layoutId
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.backend.playback.RepeatSate
import com.lighttigerxiv.simple.mp.compose.frontend.composables.HSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.composables.PlayingListSongCard
import com.lighttigerxiv.simple.mp.compose.frontend.composables.VSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goToAddSongToPlaylist
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goToPreviewAlbum
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goToPreviewArtist
import com.lighttigerxiv.simple.mp.compose.frontend.utils.Sizes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.ReorderableLazyListState
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@ExperimentalMaterial3Api
@OptIn(ExperimentalMotionApi::class)
@Composable
fun Player(
    vm: PlayerVM = viewModel(factory = PlayerVM.Factory),
    onClosePlayer: () -> Unit,
    showPlayerProgress: Float,
    rootController: NavHostController,
    inLandscape: Boolean
) {

    val uiState = vm.uiState.collectAsState().value
    val playingPlaylistListState = rememberReorderableLazyListState(onMove = { from, to -> vm.reorderPlayingPlaylist(from.key, to.key) })
    val sheetState = rememberModalBottomSheetState()
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)
    val context = LocalContext.current

    LaunchedEffect(uiState.showUpNextPlaylist) {
        playingPlaylistListState.listState.scrollToItem(0)
    }

    MotionLayout(
        modifier = Modifier
            .fillMaxSize(),
        motionScene = MotionScene(
            content = context.resources
                .openRawResource(R.raw.player)
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

            BottomSheetScaffold(
                scaffoldState = scaffoldState,
                sheetPeekHeight = 0.dp,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                sheetContainerColor = MaterialTheme.colorScheme.surface,
                sheetContent = {
                    MenuSheet(
                        vm = vm,
                        sheetState = sheetState,
                        rootController = rootController,
                        uiState = uiState
                    )
                }
            ) { scaffoldPadding ->

                if (inLandscape) {

                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(scaffoldPadding)
                            .padding(Sizes.XLARGE)
                    ) {
                        TopRow(
                            vm = vm,
                            uiState = uiState,
                            onClosePlayer = { onClosePlayer() },
                            sheetState = sheetState
                        )

                        VSpacer(size = Sizes.LARGE)

                        if(!uiState.showUpNextPlaylist){
                            Row(Modifier.fillMaxSize()) {
                                Column(
                                    Modifier
                                        .fillMaxWidth()
                                        .weight(if (uiState.showCarPlayer) 0.5f else 0.3f, fill = true)
                                ) {

                                    AlbumArtPager(vm = vm, uiState = uiState)

                                    VSpacer(size = Sizes.LARGE)

                                    if (!uiState.showCarPlayer) {
                                        NameAndTitleRow(uiState = uiState)
                                    }
                                }

                                HSpacer(size = Sizes.LARGE)

                                Column(
                                    Modifier
                                        .fillMaxWidth()
                                        .weight(1f, fill = true)
                                ) {
                                    PlayerSlider(vm = vm, uiState = uiState)

                                    VSpacer(size = Sizes.LARGE)

                                    MediaButtons(vm = vm, uiState = uiState, inLandscape = true)
                                }
                            }
                        }

                        if(uiState.showUpNextPlaylist){
                            PlayingPlaylist(uiState, vm, playingPlaylistListState)
                        }
                    }

                } else {
                    Column(
                        modifier = Modifier
                            .padding(scaffoldPadding)
                            .padding(Sizes.XLARGE)
                    ) {

                        TopRow(
                            vm = vm,
                            uiState = uiState,
                            onClosePlayer = { onClosePlayer() },
                            sheetState = sheetState
                        )

                        VSpacer(size = Sizes.LARGE)

                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if (!uiState.showUpNextPlaylist) {
                                AlbumArtPager(vm = vm, uiState = uiState)

                                VSpacer(size = Sizes.LARGE)

                                if (!uiState.showCarPlayer) {
                                    NameAndTitleRow(uiState = uiState)
                                }

                                VSpacer(size = Sizes.LARGE)

                                PlayerSlider(vm = vm, uiState = uiState)

                                VSpacer(size = Sizes.LARGE)

                                MediaButtons(vm = vm, uiState = uiState, inLandscape = false)
                            }

                            if (uiState.showUpNextPlaylist) {

                                PlayingPlaylist(uiState, vm, playingPlaylistListState)
                            }
                        }
                    }
                }
            }
        }
    }

    if(uiState.showCarPlayer && uiState.keepScreenOnCarPlayer){
        KeepScreenOn()
    }


}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopRow(
    vm: PlayerVM,
    uiState: PlayerVM.UiState,
    onClosePlayer: () -> Unit,
    sheetState: SheetState
) {

    val scope = rememberCoroutineScope()

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
                .clickable {
                    scope.launch(Dispatchers.IO) {
                        sheetState.expand()
                    }
                },
            painter = painterResource(id = R.drawable.menu),
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
            .collect { scrolling ->
                if (!scrolling) {
                    if (pagerState.settledPage < vm.getSongPosition()) {
                        vm.skipToPrevious(false)
                    }

                    if (pagerState.settledPage > vm.getSongPosition()) {
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

            val song = uiState.playingPlaylist[pagerTabPosition]

            if (uiState.showCarPlayer) {

                CarPlayerTitleAndArtist(
                    songName = song.name,
                    artistName = vm.getArtistName(song.artistId)
                )
            } else {

                val art = vm.getSongArt(song.albumId)
                PlayerAlbumArt(art = art)
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
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
fun CarPlayerTitleAndArtist(songName: String, artistName: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = songName,
            fontWeight = FontWeight.Medium,
            fontSize = 40.sp,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        VSpacer(size = Sizes.SMALL)

        Text(
            text = artistName,
            fontWeight = FontWeight.Medium,
            fontSize = 35.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
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
    uiState: PlayerVM.UiState,
    inLandscape: Boolean
) {
    @Composable
    fun ShuffleButton() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            if (!uiState.showCarPlayer && uiState.shuffle) {
                VSpacer(size = Sizes.SMALL)
            }

            Icon(
                modifier = Modifier
                    .size(if (uiState.showCarPlayer) 80.dp else 40.dp)
                    .clip(CircleShape)
                    .clickable { vm.toggleShuffle() },
                painter = painterResource(id = R.drawable.shuffle),
                contentDescription = null,
                tint = if (uiState.shuffle) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (uiState.shuffle) {
                MediaButtonDot(uiState.showCarPlayer)
            } else {
                if (uiState.showCarPlayer) {
                    VSpacer(size = Sizes.LARGE)
                }
            }
        }
    }

    @Composable
    fun PreviousButton() {
        Icon(
            modifier = Modifier
                .size(if (uiState.showCarPlayer) 80.dp else 40.dp)
                .clip(RoundedCornerShape(Sizes.SMALL))
                .clickable { vm.skipToPrevious() },
            painter = painterResource(id = R.drawable.previous),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }


    @Composable
    fun PlayPauseButton() {
        Icon(
            modifier = Modifier
                .size(if (uiState.showCarPlayer) 120.dp else 80.dp)
                .clip(CircleShape)
                .clickable { vm.pauseOrResume() },
            painter = painterResource(id = if (uiState.isPlaying) R.drawable.pause_round else R.drawable.play_round),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }


    @Composable
    fun NextButton() {
        Icon(
            modifier = Modifier
                .size(if (uiState.showCarPlayer) 80.dp else 40.dp)
                .clip(RoundedCornerShape(Sizes.SMALL))
                .clickable { vm.skipToNext() },
            painter = painterResource(id = R.drawable.next),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }


    @Composable
    fun RepeatButton() {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            if (!uiState.showCarPlayer && uiState.repeatState != RepeatSate.Off) {
                VSpacer(size = Sizes.SMALL)
            }

            Icon(
                modifier = Modifier
                    .size(if (uiState.showCarPlayer) 80.dp else 40.dp)
                    .clip(CircleShape)
                    .clickable { vm.toggleRepeatState() },
                painter = painterResource(id = R.drawable.repeat),
                contentDescription = null,
                tint = if (uiState.repeatState != RepeatSate.Off) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (uiState.repeatState != RepeatSate.Off) {
                Row {
                    MediaButtonDot(uiState.showCarPlayer)

                    HSpacer(size = Sizes.SMALL)

                    if (uiState.repeatState == RepeatSate.Endless) {
                        MediaButtonDot(uiState.showCarPlayer)
                    }
                }
            } else {
                if (uiState.showCarPlayer) {
                    VSpacer(size = Sizes.LARGE)
                }
            }
        }
    }

    if (uiState.showCarPlayer) {

        if(inLandscape){

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ){
                ShuffleButton()
                PreviousButton()
                PlayPauseButton()
                NextButton()
                RepeatButton()
            }

        }else{
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    PreviousButton()
                    PlayPauseButton()
                    NextButton()
                }

                VSpacer(size = Sizes.XLARGE)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ShuffleButton()
                    RepeatButton()
                }
            }
        }

    } else {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            ShuffleButton()
            PreviousButton()
            PlayPauseButton()
            NextButton()
            RepeatButton()
        }
    }
}

@Composable
fun MediaButtonDot(
    carPlayer: Boolean
) {
    Box(
        modifier = Modifier
            .size(if (carPlayer) Sizes.LARGE else Sizes.SMALL)
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
                            isDragging = isDragging,
                            carPlayer = uiState.showCarPlayer,
                            onClick = { vm.moveSongToTop(song.id) }
                        )

                        VSpacer(size = Sizes.SMALL)
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuSheet(
    vm: PlayerVM,
    sheetState: SheetState,
    rootController: NavHostController,
    uiState: PlayerVM.UiState
) {

    val scope = rememberCoroutineScope()

    @Composable
    fun MenuRow(
        iconId: Int,
        text: String,
        onClick: () -> Unit
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(Sizes.LARGE),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                modifier = Modifier.size(30.dp),
                painter = painterResource(id = iconId),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )

            HSpacer(size = Sizes.LARGE)

            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }

    Column(
        modifier = Modifier
            .padding(Sizes.LARGE)
    ) {

        uiState.currentSong?.let { song ->

            MenuRow(iconId = R.drawable.artist, text = stringResource(id = R.string.go_to_artist)) {
                rootController.goToPreviewArtist(song.artistId)
                scope.launch(Dispatchers.IO) { sheetState.hide() }
            }

            MenuRow(iconId = R.drawable.album, text = stringResource(id = R.string.go_to_album)) {
                rootController.goToPreviewAlbum(song.albumId)
                scope.launch(Dispatchers.IO) { sheetState.hide() }
            }

            MenuRow(iconId = R.drawable.playlist, text = stringResource(id = R.string.add_to_playlist)) {
                rootController.goToAddSongToPlaylist(song.id)
                scope.launch(Dispatchers.IO) { sheetState.hide() }
            }

            MenuRow(
                iconId = R.drawable.car,
                text = if (uiState.showCarPlayer) stringResource(id = R.string.hide_car_player) else stringResource(id = R.string.show_car_player)
            ) {
                vm.toggleCarPlayer()
                scope.launch(Dispatchers.IO) { sheetState.hide() }
            }
        }
    }
}

@Composable
fun KeepScreenOn() = AndroidView({ View(it).apply { keepScreenOn = true } })