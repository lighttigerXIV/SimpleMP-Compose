package com.lighttigerxiv.simple.mp.compose.navigation.screens.main

import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.Base64
import android.widget.Toast
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.lighttigerxiv.simple.mp.compose.composables.CustomToolbar
import com.lighttigerxiv.simple.mp.compose.composables.PlayAndShuffleRow
import com.lighttigerxiv.simple.mp.compose.composables.SongItem
import com.lighttigerxiv.simple.mp.compose.data.AppDatabase
import com.lighttigerxiv.simple.mp.compose.app_viewmodels.MainVM
import kotlinx.coroutines.launch
import moe.tlaster.nestedscrollview.VerticalNestedScrollView
import moe.tlaster.nestedscrollview.rememberNestedScrollViewState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ArtistScreen(
    mainVM: MainVM,
    backStackEntry: NavBackStackEntry,
    onBackClicked: () -> Unit,
    onSelectArtistCover: (artistName: String, artistID: Long) -> Unit,
    onArtistAlbumOpened: (albumID: Long) -> Unit
) {

    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val artistID = remember { backStackEntry.arguments?.getLong("artistID")!! }
    val artist = mainVM.songs.collectAsState().value?.find { it.artistID == artistID }!!
    val artistName = remember { artist.artist }
    val defaultArtistPicture = remember { BitmapFactory.decodeResource(context.resources, R.drawable.icon_person_regular_highres) }
    val artistPicture = remember { mutableStateOf(defaultArtistPicture) }


    val artistsDao = AppDatabase.getInstance(context).artistsDao
    val artistFromDB = artistsDao.getArtist(id = artistID)

    if (artistFromDB == null) {
        artistsDao.addArtist(artistID)
    } else {

        if (artistFromDB.alreadyRequested) {
            if (artistFromDB.image == null) {
                artistPicture.value = defaultArtistPicture
            } else {
                val encodeByte = Base64.decode(artistFromDB.image, Base64.DEFAULT)
                artistPicture.value = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
            }
        } else {

            val canDownloadArtistCover = mainVM.downloadArtistCoverSetting.collectAsState().value
            val isInternetAvailable = CheckInternet.isNetworkAvailable(context)
            val canDownloadOverData = mainVM.downloadOverDataSetting.collectAsState().value
            val isMobileDataEnabled = CheckInternet.isOnMobileData(context)

            if (isInternetAvailable && canDownloadArtistCover) {
                if ((canDownloadOverData && isMobileDataEnabled) || (!canDownloadOverData && !isMobileDataEnabled)) {
                    getDiscogsRetrofit()
                        .getArtistCover(
                            token = "Discogs token=addIURHUBwvyDlSqWcNqPWkHXUbMgUzNgbpZGZnd",
                            artist = artistName
                        )
                        .enqueue(object : Callback<String> {
                            override fun onResponse(call: Call<String>, response: Response<String>) {
                                if (response.code() == 200) {

                                    try {

                                        val data = Gson().fromJson(response.body(), DiscogsResponse::class.java)


                                        if (data.results.isNotEmpty()) {


                                            val imageUrl = data.results[0].cover_image

                                            if (imageUrl.endsWith(".gif")) {
                                                artistsDao.updateArtistImage(image = null, id = artistID)
                                                artistPicture.value = defaultArtistPicture
                                            } else {
                                                Glide.with(context)
                                                    .asBitmap()
                                                    .load(imageUrl)
                                                    .into(object : CustomTarget<Bitmap>() {
                                                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                                                            val baos = ByteArrayOutputStream()
                                                            resource.compress(Bitmap.CompressFormat.PNG, 50, baos)
                                                            val b = baos.toByteArray()
                                                            val encodedImage = Base64.encodeToString(b, Base64.DEFAULT)

                                                            artistsDao.updateArtistImage(image = encodedImage, id = artistID)
                                                            artistPicture.value = resource
                                                        }

                                                        override fun onLoadCleared(placeholder: Drawable?) {}
                                                    })
                                            }
                                        }

                                    } catch (exc: Exception) {
                                        Toast.makeText(context, exc.message.toString(), Toast.LENGTH_LONG).show()
                                    }
                                }
                            }

                            override fun onFailure(call: Call<String>, t: Throwable) {
                                println("Error while getting artist cover")
                            }
                        })
                }
            }
        }
    }

    val artistSongsList =  mainVM.songs.collectAsState().value!!.filter { it.artistID == artistID } as ArrayList<Song>
    val artistAlbumsList = remember { artistSongsList.distinctBy { it.albumID } }
    val showMoreMenu = remember { mutableStateOf(false) }


    val pagerState = rememberPagerState()
    val nestedScrollViewState = rememberNestedScrollViewState()

    val gridCellsCount = when (configuration.orientation) {

        Configuration.ORIENTATION_PORTRAIT -> 2
        else -> 4
    }

    VerticalNestedScrollView(
        modifier = Modifier
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
                    onBackClick = { onBackClicked() },
                    secondaryContent = {

                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        )

                        Column {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(5.dp)
                                    .clickable {
                                        showMoreMenu.value = true
                                    }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.icon_more_regular),
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .height(25.dp)
                                        .aspectRatio(1f)
                                )
                            }

                            DropdownMenu(
                                expanded = showMoreMenu.value,
                                onDismissRequest = { showMoreMenu.value = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text(text = remember { getAppString(context, R.string.ChangeArtistCover) }) },
                                    onClick = {
                                        mainVM.showHomePopupMenu.value = false
                                        onSelectArtistCover(artistName, artistID)
                                    }
                                )
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))

                Image(
                    bitmap = artistPicture.value.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
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
                    contentColor = mainVM.surfaceColor.collectAsState().value,
                    indicator = {}
                ) {

                    val songsTabColor = when (pagerState.currentPage) {

                        0 -> MaterialTheme.colorScheme.surfaceVariant
                        else -> mainVM.surfaceColor.collectAsState().value
                    }

                    val albumsTabColor = when (pagerState.currentPage) {

                        1 -> MaterialTheme.colorScheme.surfaceVariant
                        else -> mainVM.surfaceColor.collectAsState().value
                    }

                    Tab(
                        text = { Text(remember { getAppString(context, R.string.Songs) }, fontSize = 16.sp) },
                        selected = pagerState.currentPage == 0,
                        selectedContentColor = MaterialTheme.colorScheme.primary,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                        onClick = { scope.launch { pagerState.animateScrollToPage(0) } },
                        modifier = Modifier
                            .background(mainVM.surfaceColor.collectAsState().value)
                            .padding(10.dp)
                            .clip(RoundedCornerShape(percent = 100))
                            .background(songsTabColor)
                    )
                    Tab(
                        text = { Text(remember { getAppString(context, R.string.Albums) }, fontSize = 16.sp) },
                        selectedContentColor = MaterialTheme.colorScheme.primary,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                        selected = pagerState.currentPage == 1,
                        onClick = { scope.launch { pagerState.animateScrollToPage(1) } },
                        modifier = Modifier
                            .background(mainVM.surfaceColor.collectAsState().value)
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

                                PlayAndShuffleRow(
                                    surfaceColor = mainVM.surfaceColor.collectAsState().value,
                                    onPlayClick = { mainVM.unshuffleAndPlay(artistSongsList, 0) },
                                    onSuffleClick = { mainVM.shuffleAndPlay(artistSongsList) }
                                )

                                Spacer(modifier = Modifier.height(20.dp))
                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(5.dp),
                                    content = {

                                        items(
                                            items = artistSongsList,
                                            key = { song -> song.id }
                                        ) { song ->

                                            SongItem(
                                                song = song,
                                                songAlbumArt = remember { mainVM.songsImagesList.find { it.albumID == song.albumID }!!.albumArt },
                                                highlight = song.path == mainVM.selectedSongPath.observeAsState().value,
                                                onSongClick = { mainVM.selectSong(artistSongsList, artistSongsList.indexOf(song)) }
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
                                    verticalArrangement = Arrangement.spacedBy(5.dp),
                                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                                    content = {

                                        items(
                                            items = artistAlbumsList,
                                            key = { album -> album.albumID },
                                        ) { album ->

                                            val albumSongAlbumID = album.albumID
                                            val albumName = album.albumName
                                            val albumArt = mainVM.songsImagesList.first { it.albumID == albumSongAlbumID }.albumArt

                                            Box(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .clip(RoundedCornerShape(14.dp))
                                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                                    .clickable {
                                                        onArtistAlbumOpened(album.albumID)
                                                    }

                                            ) {

                                                Column(
                                                    modifier = Modifier.fillMaxSize(),
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                    Image(
                                                        bitmap = remember { (albumArt ?: BitmapFactory.decodeResource(context.resources, R.drawable.icon_music_record)).asImageBitmap() },
                                                        colorFilter = if (albumArt == null) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null,
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
        }
    )
}

