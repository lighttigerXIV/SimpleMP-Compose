package com.lighttigerxiv.simple.mp.compose.composables

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lighttigerxiv.simple.mp.compose.viewmodels.ActivityMainVM

@Composable
fun AlbumsAndArtistsGrid(
    activityMainVM: ActivityMainVM,
    contentType: String,
    onCardClicked : ()-> Unit
){

    val configuration = LocalConfiguration.current
    val artistsList = activityMainVM.currentArtistsList.observeAsState().value!!
    val albumsList = activityMainVM.currentAlbumsList.observeAsState().value!!

    val gridCellsCount = when(configuration.orientation){

        Configuration.ORIENTATION_PORTRAIT->2
        else->4
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(gridCellsCount),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        content = {

            when(contentType){

                "Artists"->{

                    items(
                        items = artistsList,
                        key = { artist-> artist.artistID },
                    ){ artist->

                        val artistSongAlbumID = artist.albumID
                        val artistName = artist.artistName

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(14.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable {
                                    activityMainVM.clickedArtistID.value = artist.artistID
                                    onCardClicked()
                                }

                        ) {

                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ){
                                Image(
                                    bitmap = remember { activityMainVM.songsImagesList.first { it.albumID == artistSongAlbumID }.albumArt.asImageBitmap() },
                                    contentDescription = "",
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .clip(RoundedCornerShape(14.dp))

                                )
                                Text(
                                    text = artistName,
                                    fontSize = 15.sp,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }
                "Albums"->{

                    items(
                        items = albumsList,
                        key = { album-> album.albumID },
                    ){ album->

                        val albumSongAlbumID = album.albumID
                        val albumName = album.albumName

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(14.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable {
                                    activityMainVM.clickedAlbumID.value = album.albumID
                                    onCardClicked()
                                }

                        ) {

                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ){
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
            }
        }
    )
}

