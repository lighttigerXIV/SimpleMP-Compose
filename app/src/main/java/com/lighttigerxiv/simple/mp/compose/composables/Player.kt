package com.lighttigerxiv.simple.mp.compose.composables

import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.lighttigerxiv.simple.mp.compose.viewmodels.ActivityMainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun Player(
    activityMainViewModel: ActivityMainViewModel,
    bottomSheetState: BottomSheetState
) {

    val configuration = LocalConfiguration.current
    val songTitle = activityMainViewModel.selectedSongTitle.observeAsState().value
    val songArtistName = activityMainViewModel.selectedSongArtistName.observeAsState().value
    val songAlbumArt = activityMainViewModel.selectedSongAlbumArt.observeAsState().value
    val songDuration = activityMainViewModel.selectedSongDuration.observeAsState().value
    val currentMediaPlayerPosition = activityMainViewModel.currentMediaPlayerPosition.observeAsState().value
    val songMinutesAndSeconds = activityMainViewModel.selectedSongMinutesAndSeconds.observeAsState().value
    val songCurrentMinutesAndSeconds = activityMainViewModel.selectedSongCurrentMinutesAndSeconds.observeAsState().value
    val currentPlayerIcon = activityMainViewModel.currentPlayerIcon.observeAsState().value
    val isMusicShuffled = activityMainViewModel.isMusicShuffled.observeAsState().value
    val isMusicOnRepeat = activityMainViewModel.isMusicOnRepeat.observeAsState().value
    val queueListState = rememberLazyListState()
    val songsPagerState = rememberPagerState()

    val context = LocalContext.current
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    val interactionSource = remember { MutableInteractionSource() }
    val isDragged by interactionSource.collectIsDraggedAsState()

    val queueList = activityMainViewModel.queueList.observeAsState().value!!
    val upNextQueueList = activityMainViewModel.upNextQueueList.observeAsState().value!!
    val sliderValue = remember { mutableStateOf(currentMediaPlayerPosition!! / 1000.toFloat()) }
    val currentMinutesAndSecondsValue = remember { mutableStateOf(songCurrentMinutesAndSeconds) }

    val primaryColor = MaterialTheme.colorScheme.primary
    val iconsColor = MaterialTheme.colorScheme.onSurface

    val isDeviceOnLandscape = when (configuration.orientation) {

        Configuration.ORIENTATION_PORTRAIT -> false
        else -> true
    }

    LaunchedEffect(queueList) {

        try {

            if (activityMainViewModel.getCurrentSongPosition() != -1) {
                songsPagerState.scrollToPage(activityMainViewModel.getCurrentSongPosition())

            }
        } catch (ignore: Exception) {
        }
    }

    LaunchedEffect(activityMainViewModel.selectedSong.observeAsState().value) {

        try {
            scope.launch {
                songsPagerState.scrollToPage(activityMainViewModel.getCurrentSongPosition())
            }
        } catch (ignore: Exception) {
        }
    }


    LaunchedEffect(songsPagerState.currentPage) {

        if (bottomSheetState.isExpanded) {

            if (songsPagerState.currentPage > activityMainViewModel.getCurrentSongPosition()) {
                activityMainViewModel.selectNextSong()
            }
            if (songsPagerState.currentPage < activityMainViewModel.getCurrentSongPosition()) {
                activityMainViewModel.selectPreviousSong()
            }
        }
    }

    val shuffleColor =
        if (isMusicShuffled!!) primaryColor
        else iconsColor

    val repeatColor =
        if (isMusicOnRepeat!!) primaryColor
        else iconsColor


    BoxWithConstraints(
        modifier = Modifier
            .padding(14.dp)
    ) {

        val screenHeight = maxHeight

        when {

            isDeviceOnLandscape -> {

                Column(
                    modifier = Modifier.height(screenHeight)
                ) {

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                    ) {

                        Box(
                            modifier = Modifier
                                .width(100.dp)
                                .height(6.dp)
                                .clip(RoundedCornerShape(percent = 100))
                                .background(MaterialTheme.colorScheme.primary)
                        )
                    }

                    TabRow(
                        selectedTabIndex = pagerState.currentPage,
                        divider = {},
                        indicator = {},
                    ) {

                        val songsColor = when (pagerState.currentPage) {

                            0 -> MaterialTheme.colorScheme.surfaceVariant
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }

                        val queueColor = when (pagerState.currentPage) {

                            1 -> MaterialTheme.colorScheme.surfaceVariant
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }

                        Tab(
                            text = { Text("Song", fontSize = 16.sp) },
                            selected = pagerState.currentPage == 0,
                            selectedContentColor = MaterialTheme.colorScheme.primary,
                            unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                            onClick = { scope.launch { pagerState.animateScrollToPage(0) } },
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(10.dp)
                                .clip(RoundedCornerShape(percent = 100))
                                .background(songsColor)
                        )
                        Tab(
                            text = { Text("Queue List", fontSize = 16.sp) },
                            selectedContentColor = MaterialTheme.colorScheme.primary,
                            unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                            selected = pagerState.currentPage == 1,
                            onClick = { scope.launch { pagerState.animateScrollToPage(1) } },
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(10.dp)
                                .clip(RoundedCornerShape(percent = 100))
                                .background(queueColor)
                        )
                    }
                    HorizontalPager(
                        count = 2,
                        state = pagerState,
                        userScrollEnabled = false,
                        modifier = Modifier.fillMaxSize()
                    ) { currentPage ->

                        when (currentPage) {

                            0 -> {

                                if (songAlbumArt != null) {
                                    Row(
                                        modifier = Modifier.fillMaxSize()
                                    ) {

                                        Column(
                                            modifier = Modifier.fillMaxWidth(0.3f)
                                        ) {


                                            HorizontalPager(
                                                state = songsPagerState,
                                                count = queueList.size,
                                                itemSpacing = 14.dp,
                                                modifier = Modifier
                                                    .wrapContentHeight()
                                                    .wrapContentWidth()
                                            ) { currentPage ->

                                                val pagerSong = queueList[currentPage]
                                                val pagerAlbumArt = activityMainViewModel.songsImagesList.find { it.albumID == pagerSong.albumID }!!.albumArt


                                                AsyncImage(
                                                    model = pagerAlbumArt,
                                                    contentDescription = "",
                                                    modifier = Modifier
                                                        .fillMaxHeight()
                                                        .aspectRatio(1f)
                                                        .clip(RoundedCornerShape(14.dp))
                                                )
                                            }
                                        }
                                        Column(
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = songTitle!!,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                color = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            )
                                            Text(
                                                text = songArtistName!!,
                                                fontSize = 18.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            )
                                            Slider(
                                                value = sliderValue.value,
                                                onValueChange = {
                                                    sliderValue.value = it
                                                    currentMinutesAndSecondsValue.value = activityMainViewModel.getMinutesAndSecondsFromPosition(sliderValue.value.toInt())
                                                },
                                                onValueChangeFinished = {

                                                    if (activityMainViewModel.getIsMusicPaused())
                                                        activityMainViewModel.pauseResumeMusic()

                                                    activityMainViewModel.seekSongPosition(sliderValue.value.toInt())
                                                },
                                                valueRange = 1f..(songDuration!! / 1000).toFloat(),
                                                interactionSource = interactionSource,
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
                                                    text = currentMinutesAndSecondsValue.value!!,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                                Spacer(modifier = Modifier.weight(1f))
                                                Text(
                                                    text = songMinutesAndSeconds!!,
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

                                                    if (isMusicShuffled) {
                                                        Spacer(modifier = Modifier.height(5.dp)) //Needed to keep shuffle button in place when shuffle is enabled
                                                    }
                                                    Image(
                                                        bitmap = remember { activityMainViewModel.shuffleIcon },
                                                        contentDescription = "",
                                                        colorFilter = ColorFilter.tint(shuffleColor),
                                                        modifier = Modifier
                                                            .height(30.dp)
                                                            .clickable(
                                                                indication = null,
                                                                interactionSource = remember { MutableInteractionSource() }
                                                            ) {
                                                                activityMainViewModel.toggleShuffle()
                                                            }
                                                    )
                                                    if (isMusicShuffled) {
                                                        Dot()
                                                    }
                                                }

                                                Image(
                                                    bitmap = remember { activityMainViewModel.previousIcon },
                                                    contentDescription = "",
                                                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                                                    modifier = Modifier
                                                        .height(30.dp)
                                                        .clickable(
                                                            indication = null,
                                                            interactionSource = remember { MutableInteractionSource() }
                                                        ) {
                                                            activityMainViewModel.selectPreviousSong()
                                                            scope.launch { songsPagerState.scrollToPage(activityMainViewModel.getCurrentSongPosition()) }
                                                        }
                                                )
                                                Image(
                                                    bitmap = currentPlayerIcon!!,
                                                    contentDescription = "",
                                                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                                                    modifier = Modifier
                                                        .height(60.dp)
                                                        .clickable(
                                                            indication = null,
                                                            interactionSource = remember { MutableInteractionSource() }
                                                        ) { activityMainViewModel.pauseResumeMusic() }
                                                )
                                                Image(
                                                    bitmap = remember { activityMainViewModel.nextIcon },
                                                    contentDescription = "",
                                                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                                                    modifier = Modifier
                                                        .height(30.dp)
                                                        .clickable(
                                                            indication = null,
                                                            interactionSource = remember { MutableInteractionSource() }
                                                        ) {
                                                            activityMainViewModel.selectNextSong()
                                                            scope.launch { songsPagerState.scrollToPage(activityMainViewModel.getCurrentSongPosition()) }
                                                        }
                                                )

                                                Column(
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    modifier = Modifier.wrapContentHeight()
                                                ) {

                                                    if (isMusicOnRepeat) {
                                                        Spacer(modifier = Modifier.height(5.dp)) //Needed to keep repeat button in place when repeat is enabled
                                                    }
                                                    Image(
                                                        bitmap = remember { activityMainViewModel.repeatIcon },
                                                        contentDescription = "",
                                                        colorFilter = ColorFilter.tint(repeatColor),
                                                        modifier = Modifier
                                                            .height(30.dp)
                                                            .clickable(
                                                                indication = null,
                                                                interactionSource = remember { MutableInteractionSource() }
                                                            ) { activityMainViewModel.toggleRepeat() }
                                                    )
                                                    if (isMusicOnRepeat) {
                                                        Dot()
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            1 -> {

                                LazyColumn(
                                    state = queueListState,
                                    modifier = Modifier.fillMaxSize(),
                                    content = {

                                        items(
                                            items = upNextQueueList,
                                            key = { song -> song.id }
                                        ) { song ->

                                            SongItem(
                                                song = song,
                                                songAlbumArt = remember { activityMainViewModel.compressedImagesList.find { it.albumID == song.albumID }!!.albumArt },
                                                highlight = activityMainViewModel.selectedSongPath.value!! == song.path
                                            )
                                        }
                                    })
                            }
                        }
                    }
                }
            }

            else -> {
                Column(
                    modifier = Modifier.height(screenHeight)
                ) {

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                    ) {

                        Box(
                            modifier = Modifier
                                .width(100.dp)
                                .height(6.dp)
                                .clip(RoundedCornerShape(percent = 100))
                                .background(MaterialTheme.colorScheme.primary)
                        )
                    }


                    TabRow(
                        selectedTabIndex = pagerState.currentPage,
                        divider = {},
                        indicator = {},
                    ) {

                        val songsColor = when (pagerState.currentPage) {

                            0 -> MaterialTheme.colorScheme.surfaceVariant
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }

                        val queueColor = when (pagerState.currentPage) {

                            1 -> MaterialTheme.colorScheme.surfaceVariant
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }

                        Tab(
                            text = { Text("Song", fontSize = 16.sp) },
                            selected = pagerState.currentPage == 0,
                            selectedContentColor = MaterialTheme.colorScheme.primary,
                            unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                            onClick = { scope.launch { pagerState.animateScrollToPage(0) } },
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(10.dp)
                                .clip(RoundedCornerShape(percent = 100))
                                .background(songsColor)
                        )
                        Tab(
                            text = { Text("Queue List", fontSize = 16.sp) },
                            selectedContentColor = MaterialTheme.colorScheme.primary,
                            unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                            selected = pagerState.currentPage == 1,
                            onClick = { scope.launch { pagerState.animateScrollToPage(1) } },
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(10.dp)
                                .clip(RoundedCornerShape(percent = 100))
                                .background(queueColor)
                        )
                    }
                    HorizontalPager(
                        count = 2,
                        state = pagerState,
                        userScrollEnabled = false,
                    ) { currentPage ->

                        when (currentPage) {

                            0 -> {

                                Column {

                                    if (songAlbumArt != null) {

                                        HorizontalPager(
                                            state = songsPagerState,
                                            count = queueList.size,
                                            itemSpacing = 14.dp,
                                            modifier = Modifier
                                                .wrapContentHeight()
                                                .wrapContentWidth()
                                        ) { currentPage ->

                                            val pagerSong = queueList[currentPage]
                                            val pagerAlbumArt = activityMainViewModel.songsImagesList.find { it.albumID == pagerSong.albumID }!!.albumArt


                                            AsyncImage(
                                                model = pagerAlbumArt,
                                                contentDescription = "",
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .aspectRatio(1f)
                                                    .clip(RoundedCornerShape(14.dp))
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))
                                        Text(
                                            text = songTitle!!,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            color = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        )
                                        Text(
                                            text = songArtistName!!,
                                            fontSize = 18.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))

                                        Slider(
                                            value = sliderValue.value,
                                            onValueChange = {
                                                sliderValue.value = it
                                                currentMinutesAndSecondsValue.value = activityMainViewModel.getMinutesAndSecondsFromPosition(sliderValue.value.toInt())
                                            },
                                            onValueChangeFinished = {

                                                if (activityMainViewModel.getIsMusicPaused())
                                                    activityMainViewModel.pauseResumeMusic()

                                                activityMainViewModel.seekSongPosition(sliderValue.value.toInt())
                                            },
                                            valueRange = 1f..(songDuration!! / 1000).toFloat(),
                                            interactionSource = interactionSource,
                                            colors = SliderDefaults.colors(
                                                thumbColor = MaterialTheme.colorScheme.primary,
                                                activeTrackColor = MaterialTheme.colorScheme.primary,
                                                inactiveTrackColor = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        )



                                        Spacer(modifier = Modifier.height(5.dp))
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .wrapContentHeight()
                                        ) {

                                            Text(
                                                text = currentMinutesAndSecondsValue.value!!,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Spacer(modifier = Modifier.weight(1f))
                                            Text(
                                                text = songMinutesAndSeconds!!,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }




                                        Spacer(modifier = Modifier.height(20.dp))
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

                                                if (isMusicShuffled) {
                                                    Spacer(modifier = Modifier.height(5.dp)) //Needed to keep shuffle button in place when shuffle is enabled
                                                }
                                                Image(
                                                    bitmap = remember { activityMainViewModel.shuffleIcon },
                                                    contentDescription = "",
                                                    colorFilter = ColorFilter.tint(shuffleColor),
                                                    modifier = Modifier
                                                        .height(30.dp)
                                                        .clickable(
                                                            indication = null,
                                                            interactionSource = remember { MutableInteractionSource() }
                                                        ) {
                                                            activityMainViewModel.toggleShuffle()
                                                        }
                                                )
                                                if (isMusicShuffled) {
                                                    Dot()
                                                }
                                            }

                                            Image(
                                                bitmap = remember { activityMainViewModel.previousIcon },
                                                contentDescription = "",
                                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                                                modifier = Modifier
                                                    .height(30.dp)
                                                    .clickable(
                                                        indication = null,
                                                        interactionSource = remember { MutableInteractionSource() }
                                                    ) {
                                                        activityMainViewModel.selectPreviousSong()
                                                        scope.launch { songsPagerState.scrollToPage(activityMainViewModel.getCurrentSongPosition()) }
                                                    }
                                            )
                                            Image(
                                                bitmap = currentPlayerIcon!!,
                                                contentDescription = "",
                                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                                                modifier = Modifier
                                                    .height(60.dp)
                                                    .clickable(
                                                        indication = null,
                                                        interactionSource = remember { MutableInteractionSource() }
                                                    ) { activityMainViewModel.pauseResumeMusic() }
                                            )
                                            Image(
                                                bitmap = remember { activityMainViewModel.nextIcon },
                                                contentDescription = "",
                                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                                                modifier = Modifier
                                                    .height(30.dp)
                                                    .clickable(
                                                        indication = null,
                                                        interactionSource = remember { MutableInteractionSource() }
                                                    ) {
                                                        activityMainViewModel.selectNextSong()
                                                        scope.launch { songsPagerState.scrollToPage(activityMainViewModel.getCurrentSongPosition()) }
                                                    }
                                            )

                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                modifier = Modifier.wrapContentHeight()
                                            ) {

                                                if (isMusicOnRepeat) {
                                                    Spacer(modifier = Modifier.height(5.dp)) //Needed to keep repeat button in place when repeat is enabled
                                                }
                                                Image(
                                                    bitmap = remember { activityMainViewModel.repeatIcon },
                                                    contentDescription = "",
                                                    colorFilter = ColorFilter.tint(repeatColor),
                                                    modifier = Modifier
                                                        .height(30.dp)
                                                        .clickable(
                                                            indication = null,
                                                            interactionSource = remember { MutableInteractionSource() }
                                                        ) { activityMainViewModel.toggleRepeat() }
                                                )
                                                if (isMusicOnRepeat) {
                                                    Dot()
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            1 -> {

                                LazyColumn(
                                    state = queueListState,
                                    modifier = Modifier.fillMaxSize(),
                                    content = {

                                        items(
                                            items = upNextQueueList,
                                            key = { song -> song.id }
                                        ) { song ->

                                            SongItem(
                                                song = song,
                                                songAlbumArt = remember { activityMainViewModel.compressedImagesList.find { it.albumID == song.albumID }!!.albumArt },
                                                highlight = activityMainViewModel.selectedSongPath.value!! == song.path
                                            )
                                        }
                                    })
                            }
                        }
                    }

                    activityMainViewModel.onSongSecondPassed = { position ->

                        if (!isDragged) {
                            sliderValue.value = position.toFloat()
                        }

                        currentMinutesAndSecondsValue.value = activityMainViewModel.getMinutesAndSecondsFromPosition(sliderValue.value.toInt())
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
            .height(5.dp)
            .width(5.dp)
            .clip(RoundedCornerShape(percent = 100))
            .background(MaterialTheme.colorScheme.primary)
    )
}