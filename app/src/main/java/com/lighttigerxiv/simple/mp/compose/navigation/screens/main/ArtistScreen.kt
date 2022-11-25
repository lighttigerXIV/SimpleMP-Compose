package com.lighttigerxiv.simple.mp.compose.navigation.screens.main

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.Base64
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.gson.Gson
import com.lighttigerxiv.simple.mp.compose.*
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.composables.BasicToolbar
import com.lighttigerxiv.simple.mp.compose.composables.SongItem
import com.lighttigerxiv.simple.mp.compose.viewmodels.ActivityMainVM
import kotlinx.coroutines.launch
import moe.tlaster.nestedscrollview.VerticalNestedScrollView
import moe.tlaster.nestedscrollview.rememberNestedScrollViewState
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ArtistScreen(
    activityMainVM: ActivityMainVM,
    backStackEntry: NavBackStackEntry,
    onBackClicked: () -> Unit,
    onArtistAlbumOpened: (albumID: Long) -> Unit
) {

    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val artistID = remember { backStackEntry.arguments?.getLong("artistID") }
    val artist = remember { activityMainVM.songsList.find { it.artistID == artistID }!! }
    val artistName = remember { artist.artistName }
    val defaultArtistPicture = remember { BitmapFactory.decodeResource(context.resources, R.drawable.icon_person_regular_highres) }
    val artistPicture = remember { mutableStateOf(defaultArtistPicture) }
    getArtistPicture(context, artistID!!, artistName, artistPicture)
    val artistSongsList = remember { activityMainVM.songsList.filter { it.artistID == artistID } as ArrayList<Song> }
    val artistAlbumsList = remember { artistSongsList.distinctBy { it.albumID } }

    val pagerState = rememberPagerState()
    val nestedScrollViewState = rememberNestedScrollViewState()

    val gridCellsCount = when (configuration.orientation) {

        Configuration.ORIENTATION_PORTRAIT -> 2
        else -> 4
    }

    VerticalNestedScrollView(
        state = nestedScrollViewState,
        header = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                BasicToolbar(backButtonText = "Artists", onBackClicked = { onBackClicked() })
                Spacer(modifier = Modifier.height(10.dp))

                Image(
                    bitmap = artistPicture.value.asImageBitmap(),
                    contentDescription = "",
                    colorFilter = if (artistPicture.value == defaultArtistPicture) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null,
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .aspectRatio(1f)
                        .align(Alignment.CenterHorizontally)
                        .padding(20.dp)
                        .clip(RoundedCornerShape(14.dp))
                )

                Text(
                    text = artistName,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        },
        content = {

            val scope = rememberCoroutineScope()

            Column(modifier = Modifier.fillMaxSize()) {

                androidx.compose.material.TabRow(
                    selectedTabIndex = pagerState.currentPage,
                    contentColor = activityMainVM.surfaceColor.value!!,
                    indicator = {}
                ) {

                    val songsTabColor = when(pagerState.currentPage){

                        0-> MaterialTheme.colorScheme.surfaceVariant
                        else-> activityMainVM.surfaceColor.value!!
                    }

                    val albumsTabColor = when(pagerState.currentPage){

                        1-> MaterialTheme.colorScheme.surfaceVariant
                        else-> activityMainVM.surfaceColor.value!!
                    }

                    Tab(
                        text = { Text("Songs", fontSize = 16.sp) },
                        selected = pagerState.currentPage == 0,
                        selectedContentColor = MaterialTheme.colorScheme.primary,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                        onClick = { scope.launch { pagerState.animateScrollToPage(0) } },
                        modifier = Modifier
                            .background(activityMainVM.surfaceColor.value!!)
                            .padding(10.dp)
                            .clip(RoundedCornerShape(percent = 100))
                            .background(songsTabColor)
                    )
                    Tab(
                        text = { Text("Albums", fontSize = 16.sp) },
                        selectedContentColor = MaterialTheme.colorScheme.primary,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                        selected = pagerState.currentPage == 1,
                        onClick = { scope.launch { pagerState.animateScrollToPage(1) } },
                        modifier = Modifier
                            .background(activityMainVM.surfaceColor.value!!)
                            .padding(10.dp)
                            .clip(RoundedCornerShape(percent = 100))
                            .background(albumsTabColor)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                HorizontalPager(
                    count = 2,
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { currentPage ->


                    when (currentPage) {

                        0 -> {

                            Column(
                                modifier = Modifier.fillMaxSize()
                            ) {

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                ) {
                                    Button(
                                        onClick = { activityMainVM.selectSong(artistSongsList, 0) },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = activityMainVM.surfaceColor.value!!
                                        ),
                                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(50.dp)
                                    ) {
                                        Icon(
                                            bitmap = activityMainVM.miniPlayerPlayIcon,
                                            contentDescription = "",
                                            modifier = Modifier.height(20.dp),
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(text = "Play All Songs", color = MaterialTheme.colorScheme.primary)
                                    }
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                                LazyColumn(
                                    content = {

                                        items(
                                            items = artistSongsList,
                                            key = { song -> song.id }
                                        ) { song ->

                                            SongItem(
                                                song = song,
                                                songAlbumArt = remember { activityMainVM.songsImagesList.find { it.albumID == song.albumID }!!.albumArt },
                                                highlight = song.path == activityMainVM.selectedSongPath.observeAsState().value,
                                                onSongClick = { activityMainVM.selectSong(artistSongsList, artistSongsList.indexOf(song)) }
                                            )
                                        }
                                    }
                                )
                            }
                        }
                        1 -> {
                            Spacer(modifier = Modifier.height(20.dp))
                            Column(modifier = Modifier.fillMaxSize()) {

                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(gridCellsCount),
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    content = {

                                        items(
                                            items = artistAlbumsList,
                                            key = { album -> album.albumID },
                                        ) { album ->

                                            val albumSongAlbumID = album.albumID
                                            val albumName = album.albumName

                                            Box(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .clip(RoundedCornerShape(14.dp))
                                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                                    .clickable {
                                                        activityMainVM.clickedArtistAlbumID.value =
                                                            album.albumID
                                                        onArtistAlbumOpened(album.albumID)
                                                    }

                                            ) {

                                                Column(
                                                    modifier = Modifier.fillMaxSize(),
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                    Image(
                                                        bitmap = remember { activityMainVM.songsImagesList.first { it.albumID == albumSongAlbumID }.albumArt.asImageBitmap() },
                                                        contentDescription = "",
                                                        modifier = Modifier
                                                            .padding(10.dp)
                                                            .clip(RoundedCornerShape(14.dp))
                                                    )
                                                    Text(
                                                        text = albumName,
                                                        fontSize = 15.sp,
                                                        textAlign = TextAlign.Center,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis,
                                                        color = MaterialTheme.colorScheme.onSurface,
                                                        fontWeight = FontWeight.Medium,
                                                        modifier = Modifier.padding(2.dp)
                                                    )
                                                    Spacer(modifier = Modifier.height(10.dp))
                                                }
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        },
        modifier = Modifier.padding(14.dp)
    )
}

fun getArtistPicture(
    context: Context,
    artistID: Long,
    artistName: String,
    artistPicture: MutableState<Bitmap>
) {

    val spArtists = context.getSharedPreferences("artists", Context.MODE_PRIVATE)
    val artistPictureString = spArtists.getString(artistID.toString(), null)


    if (artistPictureString != null) {

        val encodeByte = Base64.decode(artistPictureString, Base64.DEFAULT)
        artistPicture.value = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
    } else {

        if (CheckInternet.isNetworkAvailable(context)) {

            val url = "https://www.theaudiodb.com/api/v1/json/2/search.php?s=$artistName"

            MakeRequest(context, url, onResponse = { responseCode, responseJson ->

                if (responseCode == 200) {

                    val responseAudioDB = Gson().fromJson(responseJson, ResponseAudioDB::class.java)


                    if (responseAudioDB != null) {

                        if (responseAudioDB.artists?.get(0)?.strArtistThumb != null) {

                            try {

                                val artistImageURL = responseAudioDB.artists[0].strArtistThumb

                                Glide.with(context)
                                    .asBitmap()
                                    .load(artistImageURL)
                                    .into(object : CustomTarget<Bitmap>() {
                                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                                            val baos = ByteArrayOutputStream()
                                            resource.compress(Bitmap.CompressFormat.PNG, 50, baos)
                                            val b = baos.toByteArray()
                                            val encodedImage = Base64.encodeToString(b, Base64.DEFAULT)

                                            spArtists.edit().putString(artistID.toString(), encodedImage).apply()

                                            artistPicture.value = resource
                                        }

                                        override fun onLoadCleared(placeholder: Drawable?) {}
                                    })
                            } catch (ignore: Exception) {}
                        }
                    }
                }
            }).get()
        }
    }
}