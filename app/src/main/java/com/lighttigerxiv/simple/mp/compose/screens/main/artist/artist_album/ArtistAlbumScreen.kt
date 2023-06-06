package com.lighttigerxiv.simple.mp.compose.screens.main.artist.artist_album

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
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
import com.lighttigerxiv.simple.mp.compose.data.variables.SCREEN_PADDING
import com.lighttigerxiv.simple.mp.compose.activities.main.MainVM
import com.lighttigerxiv.simple.mp.compose.functions.getBitmapFromVector
import com.lighttigerxiv.simple.mp.compose.ui.composables.CustomText
import com.lighttigerxiv.simple.mp.compose.ui.composables.CustomToolbar
import com.lighttigerxiv.simple.mp.compose.ui.composables.PlayAndShuffleRow
import com.lighttigerxiv.simple.mp.compose.ui.composables.SongItem
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.MediumHeightSpacer
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.SmallHeightSpacer
import moe.tlaster.nestedscrollview.VerticalNestedScrollView
import moe.tlaster.nestedscrollview.rememberNestedScrollViewState

@Composable
fun ArtistAlbumScreen(
    mainVM: MainVM,
    albumVM: ArtistAlbumScreenVM,
    albumID: Long,
    onBackClicked: () -> Unit
) {

    val context = LocalContext.current

    val surfaceColor = mainVM.surfaceColor.collectAsState().value

    val screenLoaded = albumVM.screenLoaded.collectAsState().value

    val selectedSong = mainVM.currentSong.collectAsState().value

    val albumArt = albumVM.albumArt.collectAsState().value

    val albumName = albumVM.albumName.collectAsState().value

    val artistName = albumVM.artistName.collectAsState().value

    val songs = albumVM.albumSongs.collectAsState().value

    val songsImages = mainVM.songsCovers.collectAsState().value


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
                            bitmap = (albumArt ?: getBitmapFromVector(context, R.drawable.record)).asImageBitmap(),
                            colorFilter = if (albumArt == null) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null,
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .clip(RoundedCornerShape(14.dp))
                                .aspectRatio(1f)
                                .align(Alignment.CenterHorizontally)
                        )

                        MediumHeightSpacer()

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