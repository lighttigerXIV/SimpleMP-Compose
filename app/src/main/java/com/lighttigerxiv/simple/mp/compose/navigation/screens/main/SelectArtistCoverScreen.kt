package com.lighttigerxiv.simple.mp.compose.navigation.screens.main

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.lighttigerxiv.simple.mp.compose.*
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.composables.CustomText
import com.lighttigerxiv.simple.mp.compose.composables.CustomToolbar
import com.lighttigerxiv.simple.mp.compose.data.AppDatabase
import com.lighttigerxiv.simple.mp.compose.viewmodels.ActivityMainVM
import java.io.ByteArrayOutputStream

@Composable
fun SelectArtistCoverScreen(
    mainVM: ActivityMainVM,
    artistName: String,
    artistID: Long,
    onGoBack: () -> Unit,
    onGetImage: () -> Unit
){
    val context = LocalContext.current
    val onlineCovers = mainVM.onlineCoversSACS.collectAsState().value
    val artistDao = AppDatabase.getInstance(context).artistsDao

    val canDownloadArtistCover = mainVM.downloadArtistCoverSetting.collectAsState().value
    val isInternetAvailable = CheckInternet.isNetworkAvailable(context)
    val canDownloadOverData = mainVM.downloadOverDataSetting.collectAsState().value
    val isMobileDataEnabled = CheckInternet.isOnMobileData(context)

    mainVM.onArtistImageSelected = {bitmapString ->
        artistDao.updateArtistImage(image = bitmapString, id = artistID)
        onGoBack()
    }

    LaunchedEffect(onlineCovers){
        if(isInternetAvailable && canDownloadArtistCover){
            if((canDownloadOverData && isMobileDataEnabled) || (!canDownloadOverData && !isMobileDataEnabled)){
                if(onlineCovers == null){
                    mainVM.loadOnlineCovers(artistName)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(mainVM.surfaceColor.collectAsState().value)
            .padding(SCREEN_PADDING)
    ) {

        CustomToolbar(
            backText = remember { getAppString(context, R.string.Artist) },
            onBackClick = {onGoBack()},
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(percent = 100))
                    .clip(RoundedCornerShape(percent = 100))
                    .clickable {

                        artistDao.updateArtistImage(image = null, id = artistID)
                        onGoBack()
                    }
                    .padding(14.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                CustomText(
                    text = remember{ getAppString(context, R.string.UseDefault) },
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(percent = 100))
                    .clip(RoundedCornerShape(percent = 100))
                    .clickable {
                        onGetImage()
                    }
                    .padding(14.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                CustomText(
                    text = remember{ getAppString(context, R.string.SelectLocalCover) },
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            if(onlineCovers != null && isInternetAvailable){

                CustomText(
                    text = remember{ getAppString(context, R.string.OnlineResults) },
                    weight = FontWeight.Bold,
                    size = 20.sp
                )

                androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    content = {

                        items(
                            items = onlineCovers.results,
                            key = {it.cover_image}
                        ){ result ->

                            val artistPicture = remember{ mutableStateOf(getBitmapFromVectorDrawable(context, R.drawable.icon_loading)) }

                            Image(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(14.dp))
                                    .clickable {

                                        val baos = ByteArrayOutputStream()
                                        artistPicture.value.compress(Bitmap.CompressFormat.PNG, 100, baos)
                                        val b = baos.toByteArray()
                                        val encodedImage = Base64.encodeToString(b, Base64.DEFAULT)

                                        artistDao.updateArtistImage(image = encodedImage, id = artistID)

                                        onGoBack()
                                    },
                                bitmap = artistPicture.value.asImageBitmap(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )

                            try {
                                val imageUrl = result.cover_image

                                Glide.with(context)
                                    .asBitmap()
                                    .load(imageUrl)
                                    .into(object : CustomTarget<Bitmap>() {
                                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {

                                            artistPicture.value = resource
                                        }

                                        override fun onLoadCleared(placeholder: Drawable?) {}
                                    })
                            } catch (ignore: Exception) {}
                        }
                    }
                )
            }
        }
    }
}