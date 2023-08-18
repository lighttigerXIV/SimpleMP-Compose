package com.lighttigerxiv.simple.mp.compose.screens.main.player

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.activities.main.MainVM
import com.lighttigerxiv.simple.mp.compose.data.data_classes.Song
import com.lighttigerxiv.simple.mp.compose.data.variables.ImageSizes
import com.lighttigerxiv.simple.mp.compose.data.variables.MEDIUM_SPACING
import com.lighttigerxiv.simple.mp.compose.data.variables.Routes
import com.lighttigerxiv.simple.mp.compose.data.variables.SMALL_SPACING
import com.lighttigerxiv.simple.mp.compose.data.variables.XSMALL_SPACING
import com.lighttigerxiv.simple.mp.compose.functions.getImage
import com.lighttigerxiv.simple.mp.compose.screens.main.playlists.playlist.modifyIf
import com.lighttigerxiv.simple.mp.compose.ui.composables.ClickableMediumIcon
import com.lighttigerxiv.simple.mp.compose.ui.composables.CustomText
import com.lighttigerxiv.simple.mp.compose.ui.composables.ReorderableSongItem
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.SmallVerticalSpacer
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.SmallHorizontalSpacer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Player(
    mainVM: MainVM,
    vm: PlayerScreenVM,
    selectedSong: Song,
    queue: List<Song>?,
    upNextQueue: List<Song>?,
    onOpenPage: (page: String) -> Unit,
    onClosePLayer: () -> Unit
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val localConfiguration = LocalConfiguration.current
    val surfaceColor = mainVM.surfaceColor.collectAsState().value
    val songSeconds = mainVM.songSeconds.collectAsState().value
    val songMinutesAndSecondsText = mainVM.songMinutesAndSecondsText.collectAsState().value
    val currentSongMinutesAndSecondsText = mainVM.currentSongMinutesAndSecondsText.collectAsState().value
    val songAndQueuePager = rememberPagerState(2)
    val songsCoversPager = rememberPagerState(pageCount = queue?.size ?: 0)
    val musicPlaying = mainVM.musicPlayling.collectAsState().value
    val queueShuffled = mainVM.queueShuffled.collectAsState().value
    val songOnRepeat = mainVM.songOnRepeat.collectAsState().value
    val inPortrait = localConfiguration.orientation == Configuration.ORIENTATION_PORTRAIT
    val showMenu = vm.showMenu.collectAsState().value

    LaunchedEffect(queue) {
        songsCoversPager.scrollToPage(mainVM.songPosition.value)
    }

    LaunchedEffect(selectedSong) {

        if (mainVM.songPosition.value != songsCoversPager.currentPage) {

            songsCoversPager.scrollToPage(mainVM.songPosition.value)
        }
    }


    LaunchedEffect(songsCoversPager) {
        snapshotFlow(songsCoversPager::isScrollInProgress)
            .drop(1)
            .collect { scrollInProgress ->

                if (!scrollInProgress) {

                    if (songsCoversPager.currentPage > mainVM.songPosition.value) {

                        mainVM.selectNextSong()
                    }

                    if (songsCoversPager.currentPage < mainVM.songPosition.value) {

                        mainVM.selectPreviousSong()
                    }
                }
            }
    }

    LaunchedEffect(songAndQueuePager) {
        snapshotFlow { songAndQueuePager.currentPage }
            .collect { currentTab ->

                if (currentTab == 0) {
                    vm.highlightTab("song")
                }

                if (currentTab == 1) {
                    vm.highlightTab("queue")
                }
            }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {

        val screenHeight = this.maxHeight

        Column(
            modifier = Modifier
                .height(screenHeight)
                .fillMaxWidth()
                .padding(24.dp)
        ) {


            if (inPortrait) {
                PortraitPlayer(
                    context,
                    scope,
                    vm,
                    mainVM,
                    surfaceColor,
                    selectedSong,
                    queue,
                    upNextQueue,
                    musicPlaying,
                    songSeconds,
                    songAndQueuePager,
                    songsCoversPager,
                    showMenu,
                    onOpenPage = { onOpenPage(it) },
                    currentSongMinutesAndSecondsText,
                    songMinutesAndSecondsText,
                    queueShuffled,
                    songOnRepeat,
                    onClosePLayer = { onClosePLayer() }
                )
            } else {
                LandscapePlayer(
                    context,
                    scope,
                    vm,
                    mainVM,
                    surfaceColor,
                    selectedSong,
                    queue,
                    upNextQueue,
                    musicPlaying,
                    songSeconds,
                    songAndQueuePager,
                    songsCoversPager,
                    showMenu,
                    onOpenPage = { onOpenPage(it) },
                    currentSongMinutesAndSecondsText,
                    songMinutesAndSecondsText,
                    queueShuffled,
                    songOnRepeat,
                    onClosePLayer = { onClosePLayer() }
                )
            }
        }
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun PortraitPlayer(
    context: Context,
    scope: CoroutineScope,
    vm: PlayerScreenVM,
    mainVM: MainVM,
    surfaceColor: Color,
    currentSong: Song,
    queue: List<Song>?,
    upNextQueue: List<Song>?,
    musicPlaying: Boolean,
    songSeconds: Float,
    songAndQueuePager: PagerState,
    songsCoversPager: PagerState,
    showMenu: Boolean,
    onOpenPage: (page: String) -> Unit,
    currentSongMinutesAndSecondsText: String,
    songMinutesAndSecondsText: String,
    queueShuffled: Boolean,
    songOnRepeat: Boolean,
    onClosePLayer: () -> Unit
) {

    val defaultAlbumArt = remember { getImage(context, R.drawable.cd, ImageSizes.LARGE).asImageBitmap() }
    var secondsSliderValue by remember { mutableStateOf(songSeconds) }
    val secondsSliderInteractionSource = remember { MutableInteractionSource() }
    val draggingSecondsSlider = secondsSliderInteractionSource.collectIsDraggedAsState().value

    LaunchedEffect(songSeconds) {
        if (!draggingSecondsSlider) {
            secondsSliderValue = songSeconds
        }

        mainVM.updateCurrentSongMinutesAndSecondsText(mainVM.getMinutesAndSeconds((secondsSliderValue).toInt()))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()

        ) {

            ClickableMediumIcon(
                id = R.drawable.icon_arrow_down_solid,
                onClick = { onClosePLayer() },
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = true),
                horizontalArrangement = Arrangement.Center
            ) {

                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                ) {


                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (songAndQueuePager.currentPage == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
                            .clickable {
                                scope.launch {
                                    songAndQueuePager.scrollToPage(0)
                                }
                            }
                            .padding(
                                top = SMALL_SPACING,
                                bottom = SMALL_SPACING,
                                start = MEDIUM_SPACING,
                                end = MEDIUM_SPACING
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            modifier = Modifier
                                .height(18.dp)
                                .width(18.dp),
                            painter = painterResource(id = R.drawable.music_note),
                            contentDescription = null,
                            tint = if (songAndQueuePager.currentPage == 0) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface
                        )

                        SmallHorizontalSpacer()

                        CustomText(

                            text = "Song",
                            color = if (songAndQueuePager.currentPage == 0) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface
                        )
                    }


                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (songAndQueuePager.currentPage == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
                            .clickable {
                                scope.launch {
                                    songAndQueuePager.scrollToPage(1)
                                }
                            }
                            .padding(
                                top = SMALL_SPACING,
                                bottom = SMALL_SPACING,
                                start = MEDIUM_SPACING,
                                end = MEDIUM_SPACING
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            modifier = Modifier
                                .height(18.dp)
                                .width(18.dp),
                            painter = painterResource(id = R.drawable.queue_list),
                            contentDescription = null,
                            tint = if (songAndQueuePager.currentPage == 1) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface
                        )

                        SmallHorizontalSpacer()

                        CustomText(

                            text = "Up Next",
                            color = if (songAndQueuePager.currentPage == 1) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .height(35.dp)
                    .width(35.dp)
            ) {

                if (songAndQueuePager.currentPage == 0) {
                    ClickableMediumIcon(
                        id = R.drawable.vertical_three_dots,
                        onClick = {
                            vm.updateShowMenu(true)
                        },
                        color = MaterialTheme.colorScheme.onSurface
                    )


                    DropdownMenu(
                        modifier = Modifier
                            .background(mainVM.surfaceColor.collectAsState().value),
                        expanded = showMenu,
                        onDismissRequest = {
                            vm.updateShowMenu(false)
                        },
                    ) {

                        DropdownMenuItem(
                            text = {
                                CustomText(text = stringResource(id = R.string.GoToArtist))
                            },
                            onClick = {

                                onOpenPage("${Routes.Root.FLOATING_ARTIST}${currentSong.artistID}")
                                vm.updateShowMenu(false)
                            }
                        )

                        DropdownMenuItem(
                            text = {
                                CustomText(text = stringResource(id = R.string.GoToAlbum))
                            },
                            onClick = {

                                onOpenPage("${Routes.Root.FLOATING_ALBUM}${currentSong.albumID}")
                                vm.updateShowMenu(false)
                            }
                        )

                        DropdownMenuItem(
                            text = {
                                CustomText(text = stringResource(id = R.string.AddToPlaylist))
                            },
                            onClick = {

                                onOpenPage("${Routes.Root.ADD_SONG_TO_PLAYLIST}${currentSong.id}")

                                vm.updateShowMenu(false)
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = songAndQueuePager
        ) { selectedPage ->

            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                //Song Tab
                if (selectedPage == 0) {

                    HorizontalPager(
                        state = songsCoversPager,
                        itemSpacing = SMALL_SPACING
                    ) { currentCoverIndex ->

                        val songAlbumArt = mainVM.getSongArt(queue!![currentCoverIndex])

                        Image(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .modifyIf(songAlbumArt == null) {
                                    background(surfaceColor)
                                }
                                .modifyIf(songAlbumArt == null) {
                                    padding(5.dp)
                                },
                            bitmap = songAlbumArt?.asImageBitmap() ?: defaultAlbumArt,
                            colorFilter = if (songAlbumArt == null) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null,
                            contentDescription = "Album Art"
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f, fill = true)
                        ) {

                            CustomText(
                                text = currentSong.title,
                                size = 24.sp,
                                weight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            CustomText(
                                modifier = Modifier.clickable {
                                    onOpenPage("${Routes.Root.FLOATING_ARTIST}${currentSong.artistID}")
                                },
                                text = mainVM.getSongArtist(currentSong).name,
                                size = 16.sp
                            )
                        }
                    }

                    Slider(
                        value = secondsSliderValue,
                        interactionSource = secondsSliderInteractionSource,
                        onValueChange = {

                            secondsSliderValue = it
                            mainVM.updateCurrentSongMinutesAndSecondsText(mainVM.getMinutesAndSeconds(it.toInt()))
                        },
                        onValueChangeFinished = {

                            if (!musicPlaying)
                                mainVM.pauseResumeMusic()

                            mainVM.seekSongSeconds(secondsSliderValue.toInt())
                        },
                        valueRange = 1f..(currentSong.duration / 1000).toFloat(),
                        colors = SliderDefaults.colors(
                            thumbColor = MaterialTheme.colorScheme.primary,
                            activeTrackColor = MaterialTheme.colorScheme.primary,
                            inactiveTrackColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {

                        Text(
                            modifier = Modifier.offset(x = 8.dp),
                            text = currentSongMinutesAndSecondsText,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.weight(1f, fill = true))

                        Text(
                            modifier = Modifier.offset(x = (-8).dp),
                            text = songMinutesAndSecondsText,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Row(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.wrapContentHeight()
                        ) {

                            if (queueShuffled) {
                                Spacer(modifier = Modifier.height(XSMALL_SPACING)) //Needed to keep shuffle button in place when shuffle is enabled
                            }

                            Icon(
                                painter = painterResource(id = R.drawable.icon_shuffle_solid),
                                contentDescription = "",
                                tint = if (queueShuffled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .height(30.dp)
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) {

                                        mainVM.toggleShuffle()
                                    }
                            )

                            if (queueShuffled) {
                                Dot()
                            }
                        }

                        Icon(
                            painter = painterResource(id = R.drawable.icon_previous_solid),
                            contentDescription = "Select Previous Song",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .height(30.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    mainVM.selectPreviousSong()
                                }
                        )

                        Icon(
                            bitmap = if (musicPlaying) {
                                remember { getImage(context, R.drawable.icon_pause_round_solid, ImageSizes.MEDIUM) }
                            } else {
                                remember { getImage(context, R.drawable.icon_play_round_solid, ImageSizes.MEDIUM) }
                            }.asImageBitmap(),
                            contentDescription = "Play/Pause Button",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .height(80.dp)
                                .aspectRatio(1f)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    mainVM.pauseResumeMusic()
                                }
                        )

                        Icon(
                            painter = painterResource(id = R.drawable.icon_next_solid),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .height(30.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {

                                    mainVM.selectNextSong()
                                }
                        )

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.wrapContentHeight()
                        ) {

                            if (songOnRepeat) {

                                Spacer(modifier = Modifier.height(XSMALL_SPACING)) //Needed to keep repeat button in place when repeat is enabled
                            }

                            Icon(
                                painter = painterResource(id = R.drawable.icon_repeat_solid),
                                contentDescription = "Repeat Song",
                                tint = if (songOnRepeat) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .height(30.dp)
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) {
                                        mainVM.toggleRepeat()
                                    }
                            )

                            if (songOnRepeat) {
                                Dot()
                            }
                        }
                    }


                }

                //Queue Tab
                if (selectedPage == 1) {

                    if (upNextQueue!!.isEmpty()) {

                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            SmallVerticalSpacer()

                            CustomText(text = stringResource(id = R.string.Shrug))
                        }
                    } else {

                        val state = rememberReorderableLazyListState(onMove = mainVM::onUpNextQueueMove)

                        androidx.compose.foundation.lazy.LazyColumn(
                            state = state.listState,
                            modifier = Modifier
                                .fillMaxSize()
                                .then(Modifier.reorderable(state))
                        ) {
                            items(items = upNextQueue, key = { it.id }) { song ->
                                ReorderableItem(state, key = song.id) { isDragging ->

                                    ReorderableSongItem(
                                        mainVM = mainVM,
                                        song = song,
                                        state = state,
                                        isDragging = isDragging
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun LandscapePlayer(
    context: Context,
    scope: CoroutineScope,
    vm: PlayerScreenVM,
    mainVM: MainVM,
    surfaceColor: Color,
    currentSong: Song,
    queue: List<Song>?,
    upNextQueue: List<Song>?,
    musicPlaying: Boolean,
    songSeconds: Float,
    songAndQueuePager: PagerState,
    songsCoversPager: PagerState,
    showMenu: Boolean,
    onOpenPage: (page: String) -> Unit,
    currentSongMinutesAndSecondsText: String,
    songMinutesAndSecondsText: String,
    queueShuffled: Boolean,
    songOnRepeat: Boolean,
    onClosePLayer: () -> Unit
) {

    val defaultAlbumArt = remember { getImage(context, R.drawable.cd, ImageSizes.LARGE).asImageBitmap() }
    var secondsSliderValue by remember { mutableStateOf(songSeconds) }
    val secondsSliderInteractionSource = remember { MutableInteractionSource() }
    val draggingSecondsSlider = secondsSliderInteractionSource.collectIsDraggedAsState().value

    LaunchedEffect(songSeconds) {
        if (!draggingSecondsSlider) {
            secondsSliderValue = songSeconds
        }

        mainVM.updateCurrentSongMinutesAndSecondsText(mainVM.getMinutesAndSeconds((secondsSliderValue).toInt()))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()

        ) {

            ClickableMediumIcon(
                id = R.drawable.icon_arrow_down_solid,
                onClick = { onClosePLayer() },
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = true),
                horizontalArrangement = Arrangement.Center
            ) {

                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                ) {


                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (songAndQueuePager.currentPage == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
                            .clickable {
                                scope.launch {
                                    songAndQueuePager.scrollToPage(0)
                                }
                            }
                            .padding(
                                top = SMALL_SPACING,
                                bottom = SMALL_SPACING,
                                start = MEDIUM_SPACING,
                                end = MEDIUM_SPACING
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            modifier = Modifier
                                .height(18.dp)
                                .width(18.dp),
                            painter = painterResource(id = R.drawable.music_note),
                            contentDescription = null,
                            tint = if (songAndQueuePager.currentPage == 0) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface
                        )

                        SmallHorizontalSpacer()

                        CustomText(

                            text = "Song",
                            color = if (songAndQueuePager.currentPage == 0) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface
                        )
                    }


                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (songAndQueuePager.currentPage == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
                            .clickable {
                                scope.launch {
                                    songAndQueuePager.scrollToPage(1)
                                }
                            }
                            .padding(
                                top = SMALL_SPACING,
                                bottom = SMALL_SPACING,
                                start = MEDIUM_SPACING,
                                end = MEDIUM_SPACING
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            modifier = Modifier
                                .height(18.dp)
                                .width(18.dp),
                            painter = painterResource(id = R.drawable.queue_list),
                            contentDescription = null,
                            tint = if (songAndQueuePager.currentPage == 1) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface
                        )

                        SmallHorizontalSpacer()

                        CustomText(

                            text = "Up Next",
                            color = if (songAndQueuePager.currentPage == 1) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .height(35.dp)
                    .width(35.dp)
            ) {

                if (songAndQueuePager.currentPage == 0) {
                    ClickableMediumIcon(
                        id = R.drawable.vertical_three_dots,
                        onClick = {
                            vm.updateShowMenu(true)
                        },
                        color = MaterialTheme.colorScheme.onSurface
                    )


                    DropdownMenu(
                        modifier = Modifier
                            .background(mainVM.surfaceColor.collectAsState().value),
                        expanded = showMenu,
                        onDismissRequest = {
                            vm.updateShowMenu(false)
                        },
                    ) {

                        DropdownMenuItem(
                            text = {
                                CustomText(text = stringResource(id = R.string.GoToArtist))
                            },
                            onClick = {

                                onOpenPage("${Routes.Root.FLOATING_ARTIST}${currentSong.artistID}")
                                vm.updateShowMenu(false)
                            }
                        )

                        DropdownMenuItem(
                            text = {
                                CustomText(text = stringResource(id = R.string.GoToAlbum))
                            },
                            onClick = {

                                onOpenPage("${Routes.Root.FLOATING_ALBUM}${currentSong.albumID}")
                                vm.updateShowMenu(false)
                            }
                        )

                        DropdownMenuItem(
                            text = {
                                CustomText(text = stringResource(id = R.string.AddToPlaylist))
                            },
                            onClick = {

                                onOpenPage("${Routes.Root.ADD_SONG_TO_PLAYLIST}${currentSong.id}")

                                vm.updateShowMenu(false)
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = songAndQueuePager
        ) { selectedPage ->

            Row(
                modifier = Modifier.fillMaxSize()
            ) {

                //Song Tab
                if (selectedPage == 0) {

                    Row(
                        modifier = Modifier
                            .weight(0.4f)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        HorizontalPager(
                            state = songsCoversPager,
                            itemSpacing = SMALL_SPACING
                        ) { currentCoverIndex ->

                            val songAlbumArt = mainVM.getSongArt(queue!![currentCoverIndex])

                            Image(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(14.dp))
                                    .modifyIf(songAlbumArt == null) {
                                        background(surfaceColor)
                                    }
                                    .modifyIf(songAlbumArt == null) {
                                        padding(5.dp)
                                    },
                                bitmap = songAlbumArt?.asImageBitmap() ?: defaultAlbumArt,
                                colorFilter = if (songAlbumArt == null) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null,
                                contentDescription = "Album Art"
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .weight(0.6f)
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {

                            CustomText(
                                text = currentSong.title,
                                size = 24.sp,
                                weight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            CustomText(
                                modifier = Modifier
                                    .clickable { onOpenPage("${Routes.Root.FLOATING_ARTIST}${currentSong.artistID}") },
                                text = mainVM.getSongArtist(currentSong).name,
                                size = 16.sp
                            )
                        }


                        Slider(
                            value = secondsSliderValue,
                            interactionSource = secondsSliderInteractionSource,
                            onValueChange = {

                                secondsSliderValue = it
                                mainVM.updateCurrentSongMinutesAndSecondsText(mainVM.getMinutesAndSeconds(it.toInt()))
                            },
                            onValueChangeFinished = {

                                if (!musicPlaying)
                                    mainVM.pauseResumeMusic()

                                mainVM.seekSongSeconds(secondsSliderValue.toInt())
                            },
                            valueRange = 1f..(currentSong.duration / 1000).toFloat(),
                            colors = SliderDefaults.colors(
                                thumbColor = MaterialTheme.colorScheme.primary,
                                activeTrackColor = MaterialTheme.colorScheme.primary,
                                inactiveTrackColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        ) {

                            Text(
                                modifier = Modifier.offset(x = 8.dp),
                                text = currentSongMinutesAndSecondsText,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.weight(1f, fill = true))

                            Text(
                                modifier = Modifier.offset(x = (-8).dp),
                                text = songMinutesAndSecondsText,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Row(
                            modifier = Modifier
                                .wrapContentHeight()
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.wrapContentHeight()
                            ) {

                                if (queueShuffled) {
                                    Spacer(modifier = Modifier.height(XSMALL_SPACING)) //Needed to keep shuffle button in place when shuffle is enabled
                                }

                                Icon(
                                    painter = painterResource(id = R.drawable.icon_shuffle_solid),
                                    contentDescription = "",
                                    tint = if (queueShuffled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier
                                        .height(30.dp)
                                        .clickable(
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() }
                                        ) {

                                            mainVM.toggleShuffle()
                                        }
                                )

                                if (queueShuffled) {
                                    Dot()
                                }
                            }

                            Icon(
                                painter = painterResource(id = R.drawable.icon_previous_solid),
                                contentDescription = "Select Previous Song",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .height(30.dp)
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) {
                                        mainVM.selectPreviousSong()
                                    }
                            )

                            Icon(
                                bitmap = if (musicPlaying) {
                                    remember { getImage(context, R.drawable.icon_pause_round_solid, ImageSizes.MEDIUM) }
                                } else {
                                    remember { getImage(context, R.drawable.icon_play_round_solid, ImageSizes.MEDIUM) }
                                }.asImageBitmap(),
                                contentDescription = "Play/Pause Button",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .height(80.dp)
                                    .aspectRatio(1f)
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) {
                                        mainVM.pauseResumeMusic()
                                    }
                            )

                            Icon(
                                painter = painterResource(id = R.drawable.icon_next_solid),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .height(30.dp)
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) {

                                        mainVM.selectNextSong()
                                    }
                            )

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.wrapContentHeight()
                            ) {

                                if (songOnRepeat) {

                                    Spacer(modifier = Modifier.height(XSMALL_SPACING)) //Needed to keep repeat button in place when repeat is enabled
                                }

                                Icon(
                                    painter = painterResource(id = R.drawable.icon_repeat_solid),
                                    contentDescription = "Repeat Song",
                                    tint = if (songOnRepeat) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier
                                        .height(30.dp)
                                        .clickable(
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() }
                                        ) {
                                            mainVM.toggleRepeat()
                                        }
                                )

                                if (songOnRepeat) {
                                    Dot()
                                }
                            }
                        }

                    }

                }

                //Queue Tab
                if (selectedPage == 1) {

                    if (upNextQueue!!.isEmpty()) {

                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            SmallVerticalSpacer()

                            CustomText(text = stringResource(id = R.string.Shrug))
                        }
                    } else {

                        val state = rememberReorderableLazyListState(onMove = mainVM::onUpNextQueueMove)

                        androidx.compose.foundation.lazy.LazyColumn(
                            state = state.listState,
                            modifier = Modifier
                                .fillMaxSize()
                                .then(Modifier.reorderable(state))
                        ) {
                            items(upNextQueue, { it.id }) { song ->
                                ReorderableItem(state, key = song.id) { isDragging ->

                                    ReorderableSongItem(
                                        mainVM = mainVM,
                                        song = song,
                                        state = state,
                                        isDragging = isDragging
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Dot() {
    Box(
        modifier = Modifier
            .height(XSMALL_SPACING)
            .width(XSMALL_SPACING)
            .clip(RoundedCornerShape(percent = 100))
            .background(MaterialTheme.colorScheme.primary)
    )
}