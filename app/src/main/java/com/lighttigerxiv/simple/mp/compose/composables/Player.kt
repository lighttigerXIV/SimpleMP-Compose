package com.lighttigerxiv.simple.mp.compose.composables

import android.content.res.Configuration
import android.graphics.BitmapFactory
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.getAppString
import com.lighttigerxiv.simple.mp.compose.app_viewmodels.MainVM
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun Player(
    mainVM: MainVM,
    bottomSheetState: BottomSheetState,
    onGoToPage: (page: String) -> Unit
) {

    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val songTitle = mainVM.selectedSongTitle.observeAsState().value
    val songArtistName = mainVM.selectedSongArtistName.observeAsState().value
    val songDuration = mainVM.selectedSongDuration.observeAsState().value
    val currentMediaPlayerPosition = mainVM.currentMediaPlayerPosition.collectAsState().value
    val songMinutesAndSeconds = mainVM.selectedSongMinutesAndSeconds.observeAsState().value
    val songCurrentMinutesAndSeconds = mainVM.selectedSongCurrentMinutesAndSeconds.observeAsState().value
    val menuOpened = remember{ mutableStateOf(false)}

    val queueListState = rememberLazyListState()
    val songsPagerState = rememberPagerState()

    //------ Playback States --------------//
    val isMusicPlaying = mainVM.isMusicPlaying.collectAsState().value
    val isMusicShuffled = mainVM.isMusicShuffled.collectAsState().value
    val isMusicOnRepeat = mainVM.isMusicOnRepeat.collectAsState().value


    val currentPlayerIcon = remember{ mutableStateOf(if (isMusicPlaying)
        R.drawable.icon_pause_round_solid
    else
        R.drawable.icon_play_round_solid) }


    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    val interactionSource = remember { MutableInteractionSource() }
    val isDragged by interactionSource.collectIsDraggedAsState()

    val queueList = mainVM.queueList.observeAsState().value!!
    val upNextQueueList = mainVM.upNextQueueList.observeAsState().value!!
    val sliderValue = remember { mutableStateOf(currentMediaPlayerPosition / 1000.toFloat()) }
    val currentMinutesAndSecondsValue = remember { mutableStateOf(songCurrentMinutesAndSeconds) }

    val primaryColor = MaterialTheme.colorScheme.primary
    val iconsColor = MaterialTheme.colorScheme.onSurface

    val isDeviceOnLandscape = when (configuration.orientation) {

        Configuration.ORIENTATION_PORTRAIT -> false
        else -> true
    }

    LaunchedEffect(isMusicPlaying) {
        currentPlayerIcon.value =
            if (isMusicPlaying)
                R.drawable.icon_pause_round_solid
            else
                R.drawable.icon_play_round_solid
    }

    LaunchedEffect(queueList) {
        try {
            if (mainVM.getCurrentSongPosition() != -1) {
                songsPagerState.scrollToPage(mainVM.getCurrentSongPosition())

            }
        } catch (ignore: Exception) {
        }
    }

    LaunchedEffect(mainVM.selectedSong.collectAsState().value) {
        scope.launch {
            if (mainVM.getCurrentSongPosition() > -1)
                songsPagerState.scrollToPage(mainVM.getCurrentSongPosition())
        }
    }


    LaunchedEffect(songsPagerState.currentPage) {

        if (bottomSheetState.isExpanded) {

            if (songsPagerState.currentPage > mainVM.getCurrentSongPosition()) {
                mainVM.selectNextSong()
            }
            if (songsPagerState.currentPage < mainVM.getCurrentSongPosition()) {
                mainVM.selectPreviousSong()
            }
        }
    }

    val shuffleColor =
        if (isMusicShuffled) primaryColor
        else iconsColor

    val repeatColor =
        if (isMusicOnRepeat) primaryColor
        else iconsColor


    BoxWithConstraints(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceVariant)
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
                            text = { Text(remember { getAppString(context, R.string.Song) }, fontSize = 16.sp) },
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
                            text = { Text(remember { getAppString(context, R.string.QueueList) }, fontSize = 16.sp) },
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

                                if (mainVM.selectedSong.collectAsState().value != null) {
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
                                                val pagerAlbumArt = mainVM.songsImagesList.find { it.albumID == pagerSong.albumID }?.albumArt


                                                AsyncImage(
                                                    model = pagerAlbumArt ?: remember{BitmapFactory.decodeResource(context.resources, R.drawable.icon_music_record)},
                                                    colorFilter = if(pagerAlbumArt == null) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null,
                                                    contentDescription = "",
                                                    modifier = Modifier
                                                        .fillMaxHeight()
                                                        .aspectRatio(1f)
                                                        .clip(RoundedCornerShape(14.dp))
                                                )
                                            }
                                        }
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            verticalArrangement = Arrangement.Center
                                        ) {

                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .weight(1f, fill = true)
                                            ) {

                                                Column(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .weight(1f, fill = true)
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
                                                }

                                                Column(
                                                    verticalArrangement = Arrangement.Center
                                                ) {

                                                    Icon(
                                                        modifier = Modifier
                                                            .height(20.dp)
                                                            .aspectRatio(1f)
                                                            .clickable {
                                                                menuOpened.value = true
                                                            },
                                                        painter = painterResource(id = R.drawable.icon_three_dots_solid),
                                                        contentDescription = null,
                                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )

                                                    DropdownMenu(
                                                        modifier = Modifier
                                                            .background(MaterialTheme.colorScheme.surface),
                                                        expanded = menuOpened.value,
                                                        onDismissRequest = {menuOpened.value = false},
                                                    ) {

                                                        DropdownMenuItem(
                                                            text = { Text(text = remember { getAppString(context, R.string.GoToArtist) }) },
                                                            onClick = {onGoToPage("floatingArtistScreen?artistID=${mainVM.selectedSong.value!!.artistID}")}
                                                        )

                                                        DropdownMenuItem(
                                                            text = { Text(text = remember { getAppString(context, R.string.GoToAlbum) }) },
                                                            onClick = {onGoToPage("floatingArtistAlbumScreen?albumID=${mainVM.selectedSong.value!!.albumID}")}
                                                        )

                                                        DropdownMenuItem(
                                                            text = { Text(text = remember { getAppString(context, R.string.AddToPlaylist) }) },
                                                            onClick = {onGoToPage("addToPlaylistScreen?songID=${mainVM.selectedSong.value!!.id}")}
                                                        )
                                                    }
                                                }
                                            }

                                            /*

                                            Slider(
                                                value = sliderValue.value,
                                                onValueChange = {
                                                    sliderValue.value = it
                                                    currentMinutesAndSecondsValue.value = mainVM.getMinutesAndSecondsFromPosition(sliderValue.value.toInt())
                                                },
                                                onValueChangeFinished = {

                                                    if (mainVM.getIsMusicPaused())
                                                        mainVM.pauseResumeMusic()

                                                    mainVM.seekSongPosition(sliderValue.value.toInt())
                                                },
                                                valueRange = 1f..(songDuration!! / 1000).toFloat(),
                                                interactionSource = interactionSource,
                                                colors = SliderDefaults.colors(
                                                    thumbColor = MaterialTheme.colorScheme.primary,
                                                    activeTrackColor = MaterialTheme.colorScheme.primary,
                                                    inactiveTrackColor = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            )

                                             */

                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .wrapContentHeight()
                                            ) {

                                                Text(
                                                    text = currentMinutesAndSecondsValue.value!!,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                                Spacer(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .weight(1f, fill = true)
                                                )
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
                                                    Icon(
                                                        painter = painterResource(id = R.drawable.icon_shuffle_solid),
                                                        contentDescription = "",
                                                        tint = shuffleColor,
                                                        modifier = Modifier
                                                            .height(30.dp)
                                                            .clickable(
                                                                indication = null,
                                                                interactionSource = remember { MutableInteractionSource() }
                                                            ) {
                                                                mainVM.toggleShuffle()
                                                            }
                                                    )
                                                    if (isMusicShuffled) {
                                                        Dot()
                                                    }
                                                }

                                                Icon(
                                                    painter = painterResource(id = R.drawable.icon_previous_solid),
                                                    contentDescription = "",
                                                    tint = MaterialTheme.colorScheme.onSurface,
                                                    modifier = Modifier
                                                        .height(30.dp)
                                                        .clickable(
                                                            indication = null,
                                                            interactionSource = remember { MutableInteractionSource() }
                                                        ) {
                                                            mainVM.selectPreviousSong()
                                                            scope.launch { songsPagerState.scrollToPage(mainVM.getCurrentSongPosition()) }
                                                        }
                                                )
                                                Icon(
                                                    painter = painterResource(id = currentPlayerIcon.value),
                                                    contentDescription = "",
                                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    modifier = Modifier
                                                        .height(60.dp)
                                                        .aspectRatio(1f)
                                                        .clickable(
                                                            indication = null,
                                                            interactionSource = remember { MutableInteractionSource() }
                                                        ) { mainVM.pauseResumeMusic() }
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
                                                            scope.launch { songsPagerState.scrollToPage(mainVM.getCurrentSongPosition()) }
                                                        }
                                                )

                                                Column(
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    modifier = Modifier.wrapContentHeight()
                                                ) {

                                                    if (isMusicOnRepeat) {
                                                        Spacer(modifier = Modifier.height(5.dp)) //Needed to keep repeat button in place when repeat is enabled
                                                    }
                                                    Icon(
                                                        painter = painterResource(id = R.drawable.icon_repeat_solid),
                                                        contentDescription = "",
                                                        tint = repeatColor,
                                                        modifier = Modifier
                                                            .height(30.dp)
                                                            .clickable(
                                                                indication = null,
                                                                interactionSource = remember { MutableInteractionSource() }
                                                            ) { mainVM.toggleRepeat() }
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
                                    verticalArrangement = Arrangement.spacedBy(5.dp),
                                    content = {

                                        items(
                                            items = upNextQueueList,
                                            key = { song -> song.id }
                                        ) { song ->

                                            SongItem(
                                                song = song,
                                                songAlbumArt = remember { mainVM.compressedImagesList.find { it.albumID == song.albumID }?.albumArt },
                                                highlight = mainVM.selectedSongPath.value!! == song.path
                                            )
                                        }
                                    })
                            }
                        }
                    }
                }
            }

            //Device in portrait
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
                            text = { Text(remember { getAppString(context, R.string.Song) }, fontSize = 16.sp) },
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
                            text = { Text(remember { getAppString(context, R.string.QueueList) }, fontSize = 16.sp) },
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

                                    if (mainVM.selectedSong.collectAsState().value != null) {

                                        HorizontalPager(
                                            state = songsPagerState,
                                            count = queueList.size,
                                            itemSpacing = 14.dp,
                                            modifier = Modifier
                                                .wrapContentHeight()
                                                .wrapContentWidth()
                                        ) { currentPage ->

                                            val pagerSong = queueList[currentPage]
                                            val pagerAlbumArt = mainVM.songsImagesList.find { it.albumID == pagerSong.albumID }?.albumArt


                                            AsyncImage(
                                                model = pagerAlbumArt ?: remember{BitmapFactory.decodeResource(context.resources,R.drawable.icon_music_record)},
                                                contentDescription = "",
                                                colorFilter = if(pagerAlbumArt == null) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .aspectRatio(1f)
                                                    .clip(RoundedCornerShape(14.dp))
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {

                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .weight(1f, fill = true),
                                                verticalArrangement = Arrangement.Top
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
                                            }

                                            Spacer(modifier = Modifier.width(5.dp))

                                            Column(
                                                verticalArrangement = Arrangement.Center
                                            ) {

                                                Icon(
                                                    modifier = Modifier
                                                        .height(20.dp)
                                                        .aspectRatio(1f)
                                                        .clickable {
                                                            menuOpened.value = true
                                                        },
                                                    painter = painterResource(id = R.drawable.icon_three_dots_solid),
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                                )

                                                DropdownMenu(
                                                    modifier = Modifier
                                                        .background(MaterialTheme.colorScheme.surface),
                                                    expanded = menuOpened.value,
                                                    onDismissRequest = {menuOpened.value = false},
                                                ) {

                                                    DropdownMenuItem(
                                                        text = { Text(text = remember { getAppString(context, R.string.GoToArtist) }) },
                                                        onClick = {onGoToPage("floatingArtistScreen?artistID=${mainVM.selectedSong.value!!.artistID}")}
                                                    )

                                                    DropdownMenuItem(
                                                        text = { Text(text = remember { getAppString(context, R.string.GoToAlbum) }) },
                                                        onClick = {onGoToPage("floatingArtistAlbumScreen?albumID=${mainVM.selectedSong.value!!.albumID}")}
                                                    )

                                                    DropdownMenuItem(
                                                        text = { Text(text = remember { getAppString(context, R.string.AddToPlaylist) }) },
                                                        onClick = {onGoToPage("addToPlaylistScreen?songID=${mainVM.selectedSong.value!!.id}")}
                                                    )
                                                }
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))

                                        /*
                                        Slider(
                                            value = sliderValue.value,
                                            onValueChange = {
                                                sliderValue.value = it
                                                currentMinutesAndSecondsValue.value = mainVM.getMinutesAndSecondsFromPosition(sliderValue.value.toInt())
                                            },
                                            onValueChangeFinished = {

                                                if (mainVM.getIsMusicPaused())
                                                    mainVM.pauseResumeMusic()

                                                mainVM.seekSongPosition(sliderValue.value.toInt())
                                            },
                                            valueRange = 1f..(songDuration!! / 1000).toFloat(),
                                            interactionSource = interactionSource,
                                            colors = SliderDefaults.colors(
                                                thumbColor = MaterialTheme.colorScheme.primary,
                                                activeTrackColor = MaterialTheme.colorScheme.primary,
                                                inactiveTrackColor = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        )

                                         */

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
                                            Spacer(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .weight(1f)
                                            )
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
                                                Icon(
                                                    painter = painterResource(id = R.drawable.icon_shuffle_solid),
                                                    contentDescription = "",
                                                    tint = shuffleColor,
                                                    modifier = Modifier
                                                        .height(30.dp)
                                                        .clickable(
                                                            indication = null,
                                                            interactionSource = remember { MutableInteractionSource() }
                                                        ) {
                                                            mainVM.toggleShuffle()
                                                        }
                                                )
                                                if (isMusicShuffled) {
                                                    Dot()
                                                }
                                            }

                                            Icon(
                                                painter = painterResource(id = R.drawable.icon_previous_solid),
                                                contentDescription = "",
                                                tint = MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier
                                                    .height(30.dp)
                                                    .clickable(
                                                        indication = null,
                                                        interactionSource = remember { MutableInteractionSource() }
                                                    ) {
                                                        mainVM.selectPreviousSong()
                                                        scope.launch { songsPagerState.scrollToPage(mainVM.getCurrentSongPosition()) }
                                                    }
                                            )
                                            Icon(
                                                painter = painterResource(id = currentPlayerIcon.value),
                                                contentDescription = "",
                                                tint = MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier
                                                    .height(60.dp)
                                                    .aspectRatio(1f)
                                                    .clickable(
                                                        indication = null,
                                                        interactionSource = remember { MutableInteractionSource() }
                                                    ) { mainVM.pauseResumeMusic() }
                                            )
                                            Icon(
                                                painter = painterResource(id = R.drawable.icon_next_solid),
                                                contentDescription = "",
                                                tint = MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier
                                                    .height(30.dp)
                                                    .clickable(
                                                        indication = null,
                                                        interactionSource = remember { MutableInteractionSource() }
                                                    ) {
                                                        mainVM.selectNextSong()
                                                        scope.launch { songsPagerState.scrollToPage(mainVM.getCurrentSongPosition()) }
                                                    }
                                            )

                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                modifier = Modifier.wrapContentHeight()
                                            ) {

                                                if (isMusicOnRepeat) {
                                                    Spacer(modifier = Modifier.height(5.dp)) //Needed to keep repeat button in place when repeat is enabled
                                                }
                                                Icon(
                                                    painter = painterResource(id = R.drawable.icon_repeat_solid),
                                                    contentDescription = "",
                                                    tint = repeatColor,
                                                    modifier = Modifier
                                                        .height(30.dp)
                                                        .clickable(
                                                            indication = null,
                                                            interactionSource = remember { MutableInteractionSource() }
                                                        ) { mainVM.toggleRepeat() }
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
                                    verticalArrangement = Arrangement.spacedBy(5.dp),
                                    content = {

                                        items(
                                            items = upNextQueueList,
                                            key = { song -> song.id }
                                        ) { song ->

                                            SongItem(
                                                song = song,
                                                songAlbumArt = remember { mainVM.compressedImagesList.find { it.albumID == song.albumID }!!.albumArt },
                                                highlight = mainVM.selectedSongPath.value!! == song.path
                                            )
                                        }
                                    })
                            }
                        }
                    }
                }
            }
        }
        mainVM.onSongSecondPassed = { position ->

            if (!isDragged) {
                sliderValue.value = position.toFloat()
            }

            currentMinutesAndSecondsValue.value = mainVM.getMinutesAndSecondsFromPosition(sliderValue.value.toInt())
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