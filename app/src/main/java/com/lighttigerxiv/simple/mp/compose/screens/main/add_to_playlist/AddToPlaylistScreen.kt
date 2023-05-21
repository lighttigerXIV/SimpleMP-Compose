package com.lighttigerxiv.simple.mp.compose.screens.main.add_to_playlist

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.lighttigerxiv.simple.mp.compose.*
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.activities.main.MainVM
import com.lighttigerxiv.simple.mp.compose.data.variables.SCREEN_PADDING
import com.lighttigerxiv.simple.mp.compose.data.variables.SMALL_SPACING
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.MediumHeightSpacer
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.SmallHeightSpacer
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.SmallHorizontalSpacer
import com.lighttigerxiv.simple.mp.compose.functions.getAppString
import com.lighttigerxiv.simple.mp.compose.functions.getBitmapFromVector
import com.lighttigerxiv.simple.mp.compose.screens.main.playlists.PlaylistsScreenVM
import com.lighttigerxiv.simple.mp.compose.ui.composables.*
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddToPlaylistScreen(
    mainVM: MainVM,
    songID: Long,
    playlistsVM: PlaylistsScreenVM,
    addToPlaylistVM: AddToPlaylistScreenVM,
    previousPage: String,
    onBackClick: () -> Unit
) {

    val context = LocalContext.current

    val sheetState = rememberBottomSheetScaffoldState()

    val scope = rememberCoroutineScope()

    val surfaceColor = mainVM.surfaceColor.collectAsState().value

    val screenLoaded = addToPlaylistVM.screenLoaded.collectAsState().value

    val playlists = addToPlaylistVM.playlists.collectAsState().value

    val playlistNameText = addToPlaylistVM.playlistNameText.collectAsState().value


    if (!screenLoaded) {
        addToPlaylistVM.loadScreen()
    }

    
    if(screenLoaded){
        CustomText(text = "Caralho")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(surfaceColor)
    ) {

        Column(
            modifier = Modifier
                .padding(
                    top = SCREEN_PADDING,
                    start = SCREEN_PADDING,
                    end = SCREEN_PADDING
                )
        ) {
            CustomToolbar(
                backText = remember { previousPage },
                onBackClick = { onBackClick() },
                secondaryContent = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = true),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        if (screenLoaded) {
                            Row(
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .clip(RoundedCornerShape(100))
                                    .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(100))
                                    .clickable {
                                        scope.launch {
                                            sheetState.bottomSheetState.expand()
                                        }
                                    }
                                    .padding(SMALL_SPACING),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {

                                SmallIcon(id = R.drawable.plus)

                                SmallHorizontalSpacer()

                                CustomText(
                                    text = "Create Playlist",
                                    color = MaterialTheme.colorScheme.primary,
                                    size = 14.sp,
                                    maxLines = 1
                                )
                            }
                        }

                    }
                }
            )
        }


        BottomSheetScaffold(
            scaffoldState = sheetState,
            sheetShape = RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp),
            sheetPeekHeight = 0.dp,
            sheetContent = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .border(2.dp, MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .padding(10.dp)

                ) {

                    BottomSheetHandle()

                    SmallHeightSpacer()

                    Text(
                        text = "Playlist Name",
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    CustomTextField(
                        text = playlistNameText,
                        placeholder = "Insert playlist name",
                        onTextChange = {
                            addToPlaylistVM.updatePlaylistNameText(it)
                        },
                        textType = "text"
                    )

                    SmallHeightSpacer()

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Button(
                            onClick = {

                                scope.launch {

                                    addToPlaylistVM.createPlaylist(playlistsVM)

                                    sheetState.bottomSheetState.collapse()
                                }
                            },
                            enabled = playlistNameText.isNotEmpty()
                        ) {

                            Text(
                                text = stringResource(id = R.string.Create)
                            )
                        }
                    }
                }
            }
        ) { sheetPadding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(surfaceColor)
                    .padding(sheetPadding)
                    .padding(
                        start = SCREEN_PADDING,
                        end = SCREEN_PADDING,
                        bottom = SCREEN_PADDING
                    )
            ) {

                if (screenLoaded) {
                    MediumHeightSpacer()

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(2.5.dp),
                    ) {

                        items(
                            items = playlists!!
                        ) { playlist ->

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {

                                        scope.launch {

                                            addToPlaylistVM.addSong(
                                                songID = songID,
                                                playlist = playlist,
                                                onSuccess = {
                                                    onBackClick()
                                                },
                                                onError = {

                                                    Toast
                                                        .makeText(context, getAppString(context, id = R.string.SongAlreadyInPlaylist), Toast.LENGTH_SHORT)
                                                        .show()
                                                }
                                            )
                                        }
                                    }
                            ) {

                                val playlistImage = remember {
                                    if (playlist.image.isNullOrEmpty()) {
                                        getBitmapFromVector(context, R.drawable.playlist)
                                    } else {
                                        val imageBytes = Base64.decode(playlist.image, Base64.DEFAULT)
                                        BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size).apply {
                                            compress(Bitmap.CompressFormat.PNG, 40, ByteArrayOutputStream())
                                        }
                                    }
                                }

                                AsyncImage(
                                    model = playlistImage,
                                    contentDescription = "",
                                    colorFilter = if (playlist.image.isNullOrEmpty()) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .width(80.dp)
                                        .aspectRatio(1f)
                                        .padding(5.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                )

                                SmallHeightSpacer()

                                Text(
                                    text = playlist.name,
                                    fontSize = 18.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}