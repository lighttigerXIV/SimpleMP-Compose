package com.lighttigerxiv.simple.mp.compose.screens.main.albums.album

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.SCREEN_PADDING
import com.lighttigerxiv.simple.mp.compose.composables.CustomToolbar
import com.lighttigerxiv.simple.mp.compose.composables.CustomText
import com.lighttigerxiv.simple.mp.compose.composables.PlayAndShuffleRow
import com.lighttigerxiv.simple.mp.compose.composables.SongItem
import com.lighttigerxiv.simple.mp.compose.app_viewmodels.MainVM
import com.lighttigerxiv.simple.mp.compose.composables.spacers.MediumHeightSpacer
import com.lighttigerxiv.simple.mp.compose.composables.spacers.SmallHeightSpacer
import moe.tlaster.nestedscrollview.VerticalNestedScrollView
import moe.tlaster.nestedscrollview.rememberNestedScrollViewState


@Composable
fun AlbumScreen(
    mainVM: MainVM,
    albumVM: AlbumScreenVM,
    albumID: Long,
    onBackClicked: () -> Unit
) {

    val context = LocalContext.current

    val surfaceColor = mainVM.surfaceColor.collectAsState().value

    val selectedSong = mainVM.selectedSong.collectAsState().value

    val screenLoaded = albumVM.screenLoaded.collectAsState().value

    val albumArt = albumVM.albumArt.collectAsState().value

    val albumName = albumVM.albumName.collectAsState().value

    val artistName = albumVM.artistName.collectAsState().value

    val songs = albumVM.albumSongs.collectAsState().value

    val songsImages = mainVM.songsImages.collectAsState().value


    if (!screenLoaded) {
        albumVM.loadScreen(mainVM, albumID)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(surfaceColor)
            .padding(SCREEN_PADDING)
    ) {


        if (screenLoaded) {

            VerticalNestedScrollView(
                state = rememberNestedScrollViewState(),
                header = {

                    Column(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        CustomToolbar(backText = "Albums", onBackClick = { onBackClicked() })

                        MediumHeightSpacer()

                        Image(
                            bitmap = (albumArt ?: BitmapFactory.decodeResource(context.resources, R.drawable.record)).asImageBitmap(),
                            colorFilter = if (albumArt == null) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null,
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .clip(RoundedCornerShape(14.dp))
                                .aspectRatio(1f)
                                .align(Alignment.CenterHorizontally)
                        )

                        SmallHeightSpacer()

                        CustomText(
                            text = albumName,
                            weight = FontWeight.Bold,
                            size = 18.sp
                        )

                        CustomText(
                            text = artistName
                        )

                        MediumHeightSpacer()
                    }
                },
                content = {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        ) {

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                SmallHeightSpacer()

                                PlayAndShuffleRow(
                                    surfaceColor = surfaceColor,
                                    onPlayClick = { mainVM.unshuffleAndPlay(songs!!, 0) },
                                    onSuffleClick = { mainVM.shuffleAndPlay(songs!!) }
                                )
                            }
                        }
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(5.dp),
                            content = {

                                item {
                                    MediumHeightSpacer()
                                }

                                items(
                                    items = songs!!,
                                    key = { song -> song.id }
                                ) { song ->

                                    SongItem(
                                        song = song,
                                        songAlbumArt = remember { songsImages?.first { it.albumID == song.albumID }?.albumArt },
                                        highlight = song.path == selectedSong?.path,
                                        onSongClick = { mainVM.selectSong(songs, songs.indexOf(song)) }
                                    )
                                }
                            }
                        )
                    }
                }
            )
        }
    }
}