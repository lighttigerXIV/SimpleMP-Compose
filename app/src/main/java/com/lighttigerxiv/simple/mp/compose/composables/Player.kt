package com.lighttigerxiv.simple.mp.compose.composables

import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lighttigerxiv.simple.mp.compose.viewmodels.ActivityMainViewModel

@Composable
fun Player(
    activityMainViewModel: ActivityMainViewModel,
    onClosePlayer: () -> Unit
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

    val interactionSource = remember { MutableInteractionSource() }
    val isDragged by interactionSource.collectIsDraggedAsState()


    val sliderValue = remember { mutableStateOf(currentMediaPlayerPosition!! / 1000.toFloat()) }
    val currentMinutesAndSecondsValue = remember { mutableStateOf(songCurrentMinutesAndSeconds) }

    val primaryColor = MaterialTheme.colorScheme.primary
    val iconsColor = MaterialTheme.colorScheme.onSurface


    val shuffleColor =
        if (isMusicShuffled!!) primaryColor
        else iconsColor

    val repeatColor =
        if (isMusicOnRepeat!!) primaryColor
        else iconsColor


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Column(modifier = Modifier.fillMaxSize()) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {


                Image(
                    bitmap = remember { activityMainViewModel.closePlayerIcon },
                    contentDescription = "",
                    contentScale = ContentScale.FillHeight,
                    colorFilter = remember { ColorFilter.tint(iconsColor) },
                    modifier = Modifier
                        .height(45.dp)
                        .width(45.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            onClosePlayer()
                        }
                )
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    bitmap = remember { activityMainViewModel.queueListIcon },
                    contentDescription = "",
                    contentScale = ContentScale.FillHeight,
                    colorFilter = remember { ColorFilter.tint(iconsColor) },
                    modifier = Modifier
                        .height(45.dp)
                        .width(45.dp)
                        .padding(10.dp)
                        .clickable {}
                )
            }

            when (configuration.orientation) {

                Configuration.ORIENTATION_PORTRAIT -> {

                    if (songAlbumArt != null) {

                        Image(
                            bitmap = songAlbumArt.asImageBitmap(),
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(14.dp))
                        )

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
                            onValueChangeFinished = { activityMainViewModel.seekSongPosition(sliderValue.value.toInt()) },
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
                                        ) { activityMainViewModel.toggleShuffle() }
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
                                    ) { activityMainViewModel.selectPreviousSong() }
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
                                    ) { activityMainViewModel.selectNextSong() }
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
                else -> {

                    if (songAlbumArt != null) {

                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {

                            Column(
                                modifier = Modifier.fillMaxWidth(0.15f)
                            ) {

                                Image(
                                    bitmap = songAlbumArt.asImageBitmap(),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(14.dp))
                                )

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
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(
                                modifier = Modifier.fillMaxSize()
                            ) {


                                Slider(
                                    value = sliderValue.value,
                                    onValueChange = {
                                        sliderValue.value = it
                                        currentMinutesAndSecondsValue.value = activityMainViewModel.getMinutesAndSecondsFromPosition(sliderValue.value.toInt())
                                    },
                                    onValueChangeFinished = { activityMainViewModel.seekSongPosition(sliderValue.value.toInt()) },
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
                                            bitmap = activityMainViewModel.shuffleIcon,
                                            contentDescription = "",
                                            colorFilter = ColorFilter.tint(shuffleColor),
                                            modifier = Modifier
                                                .height(30.dp)
                                                .clickable(
                                                    indication = null,
                                                    interactionSource = remember { MutableInteractionSource() }
                                                ) { activityMainViewModel.toggleShuffle() }
                                        )
                                        if (isMusicShuffled) {
                                            Dot()
                                        }
                                    }

                                    Image(
                                        bitmap = activityMainViewModel.previousIcon,
                                        contentDescription = "",
                                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                                        modifier = Modifier
                                            .height(30.dp)
                                            .clickable(
                                                indication = null,
                                                interactionSource = remember { MutableInteractionSource() }
                                            ) { activityMainViewModel.selectPreviousSong() }
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
                                        bitmap = activityMainViewModel.nextIcon,
                                        contentDescription = "",
                                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                                        modifier = Modifier
                                            .height(30.dp)
                                            .clickable(
                                                indication = null,
                                                interactionSource = remember { MutableInteractionSource() }
                                            ) { activityMainViewModel.selectNextSong() }
                                    )

                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.wrapContentHeight()
                                    ) {

                                        if (isMusicOnRepeat) {
                                            Spacer(modifier = Modifier.height(5.dp)) //Needed to keep repeat button in place when repeat is enabled
                                        }
                                        Image(
                                            bitmap = activityMainViewModel.repeatIcon,
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