package com.lighttigerxiv.simple.mp.compose.screens.main.player

import android.content.res.Configuration
import android.graphics.BitmapFactory
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.google.accompanist.pager.rememberPagerState
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.activities.main.MainVM
import com.lighttigerxiv.simple.mp.compose.data.variables.SCREEN_PADDING
import com.lighttigerxiv.simple.mp.compose.data.variables.SMALL_SPACING
import com.lighttigerxiv.simple.mp.compose.data.variables.XSMALL_SPACING
import com.lighttigerxiv.simple.mp.compose.functions.getAppString
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.SmallWidthSpacer
import com.lighttigerxiv.simple.mp.compose.functions.getBitmapFromVector
import com.lighttigerxiv.simple.mp.compose.ui.composables.BottomSheetHandle
import com.lighttigerxiv.simple.mp.compose.ui.composables.ClickableMediumIcon
import com.lighttigerxiv.simple.mp.compose.ui.composables.CustomText
import com.lighttigerxiv.simple.mp.compose.ui.composables.SongItem
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.MediumWidthSpacer
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.SmallHeightSpacer
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoScrollEffect(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
        content()
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Player(
    mainVM: MainVM,
    playerVM: PlayerScreenVM,
    onGoToPage: (page: String) -> Unit
) {

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    val localConfiguration = LocalConfiguration.current

    val surfaceColor = mainVM.surfaceColor.collectAsState().value

    val screenLoaded = playerVM.screenLoaded.collectAsState().value

    val songAndQueuePager = rememberPagerState()

    val songsPager = rememberPagerState()

    val selectedSong = mainVM.selectedSong.collectAsState().value

    val songSeconds = mainVM.songSeconds.collectAsState().value

    val songMinutesAndSecondsText = mainVM.songMinutesAndSecondsText.collectAsState().value

    val currentSongMinutesAndSecondsText = mainVM.currentSongMinutesAndSecondsText.collectAsState().value

    val queue = mainVM.queue.collectAsState().value

    val upNextQueue = mainVM.upNextQueue.collectAsState().value

    val compressedSongsImages = mainVM.compressedSongsImages.collectAsState().value

    val songsImages = mainVM.songsImages.collectAsState().value

    val musicPlaying = mainVM.musicPlayling.collectAsState().value

    val queueShuffled = mainVM.queueShuffled.collectAsState().value

    val songOnRepeat = mainVM.songOnRepeat.collectAsState().value

    val inPortrait = localConfiguration.orientation == Configuration.ORIENTATION_PORTRAIT

    val inLandscape = localConfiguration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val highlightSongTitle = songAndQueuePager.currentPage == 0

    val highlightQueueTitle = songAndQueuePager.currentPage == 1

    val defaultAlbumArt = remember { BitmapFactory.decodeResource(context.resources, R.drawable.record) }

    val sliderInteractionSource = remember { MutableInteractionSource() }

    val draggingSlider by sliderInteractionSource.collectIsDraggedAsState()

    val menuOpened = playerVM.menuOpened.collectAsState().value

    val queueState = rememberLazyListState()

    var sliderValue by remember { mutableStateOf(songSeconds) }

    val playPauseIcon = if (musicPlaying) {
        remember { getBitmapFromVector(context, R.drawable.icon_pause_round_solid) }
    } else {
        remember { getBitmapFromVector(context, R.drawable.icon_play_round_solid) }
    }


    if (!screenLoaded) {
        playerVM.loadScreen(mainVM)
    }

    LaunchedEffect(songSeconds) {

        if (!draggingSlider) {
            sliderValue = songSeconds
        }

        mainVM.updateCurrentSongMinutesAndSecondsText(mainVM.getMinutesAndSeconds((sliderValue).toInt()))
    }

    LaunchedEffect(queue) {

        selectedSong?.let {

            songsPager.scrollToPage(mainVM.songPosition.value)
        }
    }

    LaunchedEffect(selectedSong){

        if(mainVM.songPosition.value != songsPager.currentPage){

            songsPager.scrollToPage(mainVM.songPosition.value)
        }
    }


    LaunchedEffect(songsPager) {
        snapshotFlow(songsPager::isScrollInProgress)
            .drop(1)
            .collect { scrollInProgress ->

                if (!scrollInProgress) {

                    selectedSong?.let {

                        if (songsPager.currentPage > mainVM.songPosition.value) {

                            mainVM.selectNextSong()
                        }

                        if (songsPager.currentPage < mainVM.songPosition.value) {

                            mainVM.selectPreviousSong()
                        }
                    }
                }
            }
    }


    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
    ) {

        val screenHeight = this.maxHeight

        Column(
            modifier = Modifier
                .height(screenHeight)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(SCREEN_PADDING)
        ) {

            if (selectedSong != null && queue != null && upNextQueue != null && screenLoaded) {

                BottomSheetHandle(width = 100.dp)

                //************************************************
                // Portrait Layout
                //************************************************

                if (inPortrait) {

                    TabRow(
                        selectedTabIndex = songAndQueuePager.currentPage,
                        divider = {},
                        indicator = {}
                    ) {

                        Tab(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(SMALL_SPACING)
                                .clip(CircleShape),
                            text = {
                                CustomText(
                                    text = getAppString(context, R.string.Song),
                                    color = if (highlightSongTitle)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurface,
                                    weight = if (highlightSongTitle)
                                        FontWeight.Bold
                                    else
                                        FontWeight.Normal
                                )
                            },
                            selected = songAndQueuePager.currentPage == 0,
                            onClick = {
                                scope.launch {
                                    songAndQueuePager.animateScrollToPage(0)
                                }
                            }
                        )

                        Tab(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(SMALL_SPACING)
                                .clip(CircleShape),
                            text = {
                                CustomText(
                                    text = getAppString(context, R.string.Queue),
                                    color = if (highlightQueueTitle)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurface,
                                    weight = if (highlightQueueTitle)
                                        FontWeight.Bold
                                    else
                                        FontWeight.Normal
                                )
                            },
                            selected = songAndQueuePager.currentPage == 1,
                            onClick = {
                                scope.launch {
                                    songAndQueuePager.animateScrollToPage(1)
                                }
                            }
                        )
                    }


                    HorizontalPager(
                        modifier = Modifier
                            .fillMaxSize(),
                        count = 2,
                        state = songAndQueuePager,
                        userScrollEnabled = false
                    ) { selectedPage ->

                        //************************************************
                        // Song
                        //************************************************
                        if (selectedPage == 0) {

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {

                                Column(
                                    modifier = Modifier
                                        .weight(0.6f)
                                        .fillMaxWidth(),
                                    verticalArrangement = Arrangement.Center
                                ) {


                                    HorizontalPager(
                                        state = songsPager,
                                        count = queue.size,
                                        itemSpacing = SMALL_SPACING,
                                        key = {it},

                                    ) { currentSongPage ->

                                        val songAlbumArt = songsImages?.first { it.albumID == queue[currentSongPage].albumID }?.albumArt

                                        Image(
                                            modifier = Modifier
                                                .fillMaxHeight()
                                                .aspectRatio(1f)
                                                .clip(RoundedCornerShape(14.dp)),
                                            bitmap = songAlbumArt?.asImageBitmap() ?: defaultAlbumArt.asImageBitmap(),
                                            colorFilter = if (songAlbumArt == null) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null,
                                            contentDescription = "Album Art"
                                        )
                                    }
                                }

                                Column(
                                    modifier = Modifier
                                        .weight(0.4f)
                                        .fillMaxWidth()
                                ) {

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
                                                text = selectedSong.title,
                                                size = 18.sp,
                                                weight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.primary
                                            )

                                            CustomText(
                                                text = selectedSong.artist,
                                                size = 18.sp
                                            )
                                        }

                                        SmallWidthSpacer()

                                        Column(
                                            modifier = Modifier
                                                .wrapContentSize()
                                        ) {

                                            ClickableMediumIcon(
                                                id = R.drawable.vertical_three_dots,
                                                onClick = {

                                                    playerVM.updateMenuOpened(true)
                                                },
                                                color = MaterialTheme.colorScheme.onSurface
                                            )


                                            DropdownMenu(
                                                modifier = Modifier
                                                    .background(surfaceColor),
                                                expanded = menuOpened,
                                                onDismissRequest = {

                                                    playerVM.updateMenuOpened(false)
                                                },
                                            ) {

                                                DropdownMenuItem(
                                                    text = {
                                                        CustomText(text = stringResource(id = R.string.GoToArtist))
                                                    },
                                                    onClick = {

                                                        onGoToPage("FloatingArtist/${selectedSong.artistID}")

                                                        playerVM.updateMenuOpened(false)
                                                    }
                                                )

                                                DropdownMenuItem(
                                                    text = {
                                                        CustomText(text = stringResource(id = R.string.GoToAlbum))
                                                    },
                                                    onClick = {

                                                        onGoToPage("FloatingAlbum/${selectedSong.albumID}")

                                                        playerVM.updateMenuOpened(false)
                                                    }
                                                )

                                                DropdownMenuItem(
                                                    text = {
                                                        CustomText(text = stringResource(id = R.string.AddToPlaylist))
                                                    },
                                                    onClick = {

                                                        onGoToPage("AddToPlaylist/${selectedSong.id}")

                                                        playerVM.updateMenuOpened(false)
                                                    }
                                                )
                                            }
                                        }
                                    }

                                    Slider(
                                        value = sliderValue,
                                        interactionSource = sliderInteractionSource,
                                        onValueChange = {

                                            sliderValue = it

                                            mainVM.updateCurrentSongMinutesAndSecondsText(mainVM.getMinutesAndSeconds(it.toInt()))
                                        },
                                        onValueChangeFinished = {

                                            if (!musicPlaying)
                                                mainVM.pauseResumeMusic()

                                            mainVM.seekSongSeconds(sliderValue.toInt())
                                        },
                                        valueRange = 1f..(selectedSong.duration / 1000).toFloat(),
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
                                            text = currentSongMinutesAndSecondsText,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )

                                        Spacer(modifier = Modifier.weight(1f, fill = true))

                                        Text(
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
                                            bitmap = playPauseIcon.asImageBitmap(),
                                            contentDescription = "Play/Pause Button",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier
                                                .height(60.dp)
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
                        }

                        //************************************************
                        // Queue
                        //************************************************
                        if (selectedPage == 1) {

                            if (upNextQueue.isEmpty()) {

                                Column(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    verticalArrangement = Arrangement.Top,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                    SmallHeightSpacer()

                                    CustomText(text = stringResource(id = R.string.Shrug))
                                }
                            } else {

                                androidx.compose.foundation.lazy.LazyColumn(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    state = queueState,
                                    content = {

                                        items(
                                            items = upNextQueue,
                                            key = { it.id }
                                        ) { song ->

                                            SongItem(
                                                song = song,
                                                songAlbumArt = remember{compressedSongsImages?.find { it.albumID == song.albumID }?.albumArt},
                                                highlight = selectedSong.path == song.path
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                //************************************************
                // Landscape Layout
                //************************************************

                if (inLandscape) {

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth(0.3f)
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.Center
                        ) {

                            NoScrollEffect {

                                HorizontalPager(
                                    state = songsPager,
                                    count = queue.size,
                                    key = { it },
                                    itemSpacing = SMALL_SPACING

                                ) { currentSongPage ->

                                    val songAlbumArt = songsImages?.first { it.albumID == queue[currentSongPage].albumID }?.albumArt

                                    Image(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .aspectRatio(1f)
                                            .clip(RoundedCornerShape(14.dp)),
                                        bitmap = songAlbumArt?.asImageBitmap() ?: defaultAlbumArt.asImageBitmap(),
                                        colorFilter = if (songAlbumArt == null) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null,
                                        contentDescription = "Album Art"
                                    )
                                }
                            }
                        }

                        MediumWidthSpacer()

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {

                            TabRow(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                selectedTabIndex = songAndQueuePager.currentPage,
                                divider = {},
                                indicator = {}
                            ) {

                                Tab(
                                    modifier = Modifier
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .padding(SMALL_SPACING)
                                        .clip(CircleShape),
                                    text = {
                                        CustomText(
                                            text = stringResource(id = R.string.Song),
                                            color = if (highlightSongTitle)
                                                MaterialTheme.colorScheme.primary
                                            else
                                                MaterialTheme.colorScheme.onSurface,
                                            weight = if (highlightSongTitle)
                                                FontWeight.Bold
                                            else
                                                FontWeight.Normal
                                        )
                                    },
                                    selected = songAndQueuePager.currentPage == 0,
                                    onClick = {
                                        scope.launch {
                                            songAndQueuePager.animateScrollToPage(0)
                                        }
                                    }
                                )

                                Tab(
                                    modifier = Modifier
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .padding(SMALL_SPACING)
                                        .clip(CircleShape),
                                    text = {
                                        CustomText(
                                            text = stringResource(id = R.string.Queue),
                                            color = if (highlightQueueTitle)
                                                MaterialTheme.colorScheme.primary
                                            else
                                                MaterialTheme.colorScheme.onSurface,
                                            weight = if (highlightQueueTitle)
                                                FontWeight.Bold
                                            else
                                                FontWeight.Normal
                                        )
                                    },
                                    selected = songAndQueuePager.currentPage == 1,
                                    onClick = {
                                        scope.launch {
                                            songAndQueuePager.animateScrollToPage(1)
                                        }
                                    }
                                )
                            }


                            HorizontalPager(
                                modifier = Modifier
                                    .fillMaxSize(),
                                count = 2,
                                state = songAndQueuePager,
                                userScrollEnabled = false
                            ) { selectedPage ->

                                //************************************************
                                // Song
                                //************************************************
                                if (selectedPage == 0) {

                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        verticalArrangement = Arrangement.Bottom
                                    ) {

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
                                                    text = selectedSong.title,
                                                    size = 16.sp,
                                                    weight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.primary
                                                )

                                                CustomText(
                                                    text = selectedSong.artist,
                                                    size = 16.sp
                                                )
                                            }

                                            SmallWidthSpacer()

                                            Column {

                                                ClickableMediumIcon(
                                                    id = R.drawable.vertical_three_dots,
                                                    onClick = {

                                                        playerVM.updateMenuOpened(true)
                                                    },
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )

                                                DropdownMenu(
                                                    modifier = Modifier
                                                        .background(surfaceColor),
                                                    expanded = menuOpened,
                                                    onDismissRequest = {

                                                        playerVM.updateMenuOpened(false)
                                                    },
                                                ) {

                                                    DropdownMenuItem(
                                                        text = {
                                                            CustomText(text = stringResource(id = R.string.GoToArtist))
                                                        },
                                                        onClick = {

                                                            onGoToPage("FloatingArtist/${selectedSong.artistID}")

                                                            playerVM.updateMenuOpened(false)
                                                        }
                                                    )

                                                    DropdownMenuItem(
                                                        text = {
                                                            CustomText(text = stringResource(id = R.string.GoToAlbum))
                                                        },
                                                        onClick = {

                                                            onGoToPage("FloatingAlbum/${selectedSong.albumID}")

                                                            playerVM.updateMenuOpened(false)
                                                        }
                                                    )

                                                    DropdownMenuItem(
                                                        text = {
                                                            CustomText(text = stringResource(id = R.string.AddToPlaylist))
                                                        },
                                                        onClick = {

                                                            onGoToPage("AddToPlaylist/${selectedSong.id}")

                                                            playerVM.updateMenuOpened(false)
                                                        }
                                                    )
                                                }
                                            }
                                        }

                                        Slider(
                                            value = sliderValue,
                                            interactionSource = sliderInteractionSource,
                                            onValueChange = {

                                                sliderValue = it

                                                mainVM.updateCurrentSongMinutesAndSecondsText(mainVM.getMinutesAndSeconds(it.toInt()))
                                            },
                                            onValueChangeFinished = {

                                                if (!musicPlaying)
                                                    mainVM.pauseResumeMusic()

                                                mainVM.seekSongSeconds(sliderValue.toInt())
                                            },
                                            valueRange = 1f..(selectedSong.duration / 1000).toFloat(),
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
                                                text = currentSongMinutesAndSecondsText,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )

                                            Spacer(modifier = Modifier.weight(1f, fill = true))

                                            Text(
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
                                                        .height(25.dp)
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
                                                    .height(25.dp)
                                                    .clickable(
                                                        indication = null,
                                                        interactionSource = remember { MutableInteractionSource() }
                                                    ) {
                                                        mainVM.selectPreviousSong()
                                                    }
                                            )

                                            Icon(
                                                bitmap = playPauseIcon.asImageBitmap(),
                                                contentDescription = "Play/Pause Button",
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier
                                                    .height(50.dp)
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
                                                    .height(25.dp)
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
                                                        .height(25.dp)
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

                                //************************************************
                                // Queue
                                //************************************************
                                if (selectedPage == 1) {

                                    if (upNextQueue.isEmpty()) {

                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize(),
                                            verticalArrangement = Arrangement.Top,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {

                                            SmallHeightSpacer()

                                            CustomText(text = stringResource(id = R.string.Shrug))
                                        }
                                    }

                                    androidx.compose.foundation.lazy.LazyColumn(
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        state = queueState,
                                        content = {

                                            items(
                                                items = upNextQueue,
                                                key = { it.id }
                                            ) { song ->

                                                SongItem(
                                                    song = song,
                                                    songAlbumArt = compressedSongsImages?.find { it.albumID == song.albumID }?.albumArt,
                                                    highlight = selectedSong.path == song.path
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
