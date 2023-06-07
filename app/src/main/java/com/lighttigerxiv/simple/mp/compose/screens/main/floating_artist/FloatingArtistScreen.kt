package com.lighttigerxiv.simple.mp.compose.screens.main.floating_artist

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.data.variables.SCREEN_PADDING
import com.lighttigerxiv.simple.mp.compose.activities.main.MainVM
import com.lighttigerxiv.simple.mp.compose.settings.SettingsVM
import com.lighttigerxiv.simple.mp.compose.ui.composables.CustomText
import com.lighttigerxiv.simple.mp.compose.ui.composables.CustomToolbar
import com.lighttigerxiv.simple.mp.compose.ui.composables.PlayAndShuffleRow
import com.lighttigerxiv.simple.mp.compose.ui.composables.SongItem
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.MediumHeightSpacer
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.SmallHeightSpacer
import com.lighttigerxiv.simple.mp.compose.functions.getAppString
import com.lighttigerxiv.simple.mp.compose.screens.main.playlists.playlist.modifyIf
import moe.tlaster.nestedscrollview.VerticalNestedScrollView
import moe.tlaster.nestedscrollview.rememberNestedScrollViewState

@Composable
fun FloatingArtistScreen(
    mainVM: MainVM,
    settingsVM: SettingsVM,
    artistID: Long,
    vm: FloatingArtistScreenVM,
    onBackClicked: () -> Unit
) {

    val context = LocalContext.current
    val inPortrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
    val nestedScrollViewState = rememberNestedScrollViewState()
    val surfaceColor = mainVM.surfaceColor.collectAsState().value
    val surfaceVariantColor = MaterialTheme.colorScheme.surfaceVariant
    val screenLoaded = vm.screenLoaded.collectAsState().value
    val selectedSong = mainVM.currentSong.collectAsState().value
    val artistName = vm.artistName.collectAsState().value
    val artistCover = vm.artistCover.collectAsState().value
    val tintCover = vm.tintCover.collectAsState().value
    val songs = vm.artistSongs.collectAsState().value


    if (!screenLoaded) {
        vm.loadScreen(artistID, mainVM, settingsVM)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        if (screenLoaded) {
            if (inPortrait) {
                VerticalNestedScrollView(
                    modifier = Modifier
                        .background(surfaceColor)
                        .padding(SCREEN_PADDING),
                    state = nestedScrollViewState,
                    header = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        ) {

                            CustomToolbar(
                                backText = remember { getAppString(context, R.string.Artists) },
                                onBackClick = { onBackClicked() }
                            )

                            SmallHeightSpacer()

                            Image(
                                bitmap = artistCover.asImageBitmap(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                colorFilter = if (tintCover) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null,
                                modifier = Modifier
                                    .fillMaxWidth(0.6f)
                                    .aspectRatio(1f)
                                    .align(Alignment.CenterHorizontally)
                                    .padding(20.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .modifyIf(tintCover) {
                                        background(surfaceVariantColor)
                                    }
                                    .modifyIf(tintCover) {
                                        padding(5.dp)
                                    }
                            )

                            CustomText(
                                modifier = Modifier.fillMaxWidth(),
                                text = artistName,
                                textAlign = TextAlign.Center,
                                size = 20.sp,
                                weight = FontWeight.Bold
                            )

                            MediumHeightSpacer()
                        }
                    },
                    content = {

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {


                            PlayAndShuffleRow(
                                surfaceColor = surfaceColor,
                                onPlayClick = {
                                    mainVM.unshuffleAndPlay(songs!!, 0)
                                },
                                onSuffleClick = {
                                    mainVM.shuffleAndPlay(songs!!)
                                }
                            )

                            MediumHeightSpacer()

                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(5.dp),
                                content = {

                                    items(
                                        items = songs!!,
                                        key = { song -> song.id }
                                    ) { song ->

                                        SongItem(
                                            song = song,
                                            songAlbumArt = mainVM.songsCovers.collectAsState().value?.find { it.albumID == song.albumID }!!.albumArt,
                                            highlight = song.path == selectedSong?.path,
                                            onSongClick = { mainVM.selectSong(songs, songs.indexOf(song)) }
                                        )
                                    }
                                }
                            )
                        }
                    }
                )
            } else {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(SCREEN_PADDING)
                ) {
                    CustomToolbar(
                        backText = remember { getAppString(context, R.string.Artists) },
                        onBackClick = { onBackClicked() }
                    )

                    SmallHeightSpacer()

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                                .weight(0.4f, fill = true),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                bitmap = artistCover.asImageBitmap(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                colorFilter = if (tintCover) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null,
                                modifier = Modifier
                                    .fillMaxHeight(0.7f)
                                    .aspectRatio(1f)
                                    .align(Alignment.CenterHorizontally)
                                    .padding(20.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .modifyIf(tintCover) {
                                        background(surfaceVariantColor)
                                    }
                                    .modifyIf(tintCover) {
                                        padding(5.dp)
                                    }
                            )

                            CustomText(
                                modifier = Modifier.fillMaxWidth(),
                                text = artistName,
                                textAlign = TextAlign.Center,
                                size = 20.sp,
                                weight = FontWeight.Bold
                            )

                            MediumHeightSpacer()
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                                .weight(0.6f, fill = true)
                        ) {

                            PlayAndShuffleRow(
                                surfaceColor = surfaceColor,
                                onPlayClick = {
                                    mainVM.unshuffleAndPlay(songs!!, 0)
                                },
                                onSuffleClick = {
                                    mainVM.shuffleAndPlay(songs!!)
                                }
                            )

                            MediumHeightSpacer()

                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(5.dp),
                                content = {

                                    items(
                                        items = songs!!,
                                        key = { song -> song.id }
                                    ) { song ->

                                        SongItem(
                                            song = song,
                                            songAlbumArt = mainVM.songsCovers.collectAsState().value?.find { it.albumID == song.albumID }!!.albumArt,
                                            highlight = song.path == selectedSong?.path,
                                            onSongClick = { mainVM.selectSong(songs, songs.indexOf(song)) }
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