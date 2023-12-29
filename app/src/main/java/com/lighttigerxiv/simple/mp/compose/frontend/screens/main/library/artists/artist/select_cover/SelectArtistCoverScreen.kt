package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.artists.artist.select_cover

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.frontend.composables.SecondaryButton
import com.lighttigerxiv.simple.mp.compose.frontend.composables.Toolbar
import com.lighttigerxiv.simple.mp.compose.frontend.composables.VSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goBack
import com.lighttigerxiv.simple.mp.compose.frontend.utils.FontSizes
import com.lighttigerxiv.simple.mp.compose.frontend.utils.Sizes
import com.lighttigerxiv.simple.mp.compose.frontend.utils.modifyIf

@Composable
fun SelectArtistCoverScreen(
    artistId: Long,
    navController: NavHostController,
    vm: SelectArtistCoverScreenVM = viewModel(factory = SelectArtistCoverScreenVM.Factory)
) {

    val uiState = vm.uiState.collectAsState().value
    val context = LocalContext.current
    val askImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {

                val art = if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                } else {
                    ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
                }

                vm.changeArtistImage(art, artistId)
                navController.goBack()
            }
        })

    LaunchedEffect(uiState.requestedLoading) {
        if (!uiState.requestedLoading) {
            vm.load(artistId)
        }
    }

    Column {
        Toolbar(navController = navController)

        if (!uiState.isLoading) {

            VSpacer(size = Sizes.LARGE)

            SecondaryButton(
                text = stringResource(id = R.string.use_default_cover),
                onClick = {
                    vm.clearArtistImage(artistId)
                    navController.goBack()
                },
                fillWidth = true
            )

            VSpacer(size = Sizes.SMALL)

            SecondaryButton(
                text = stringResource(id = R.string.select_local_cover),
                onClick = {
                    askImageLauncher.launch("image/*")
                },
                fillWidth = true
            )

            VSpacer(size = Sizes.LARGE)

            if (uiState.canDownloadImages) {

                Text(
                    text = stringResource(id = R.string.online_results),
                    fontWeight = FontWeight.Medium,
                    fontSize = FontSizes.HEADER,
                    color = MaterialTheme.colorScheme.onSurface
                )

                VSpacer(size = Sizes.SMALL)

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(Sizes.SMALL),
                    horizontalArrangement = Arrangement.spacedBy(Sizes.SMALL)
                ) {
                    itemsIndexed(items = uiState.imagesUrls, key = { i, _ -> i }) { _, url ->

                        val imageLoader = remember { ImageLoader(context) }
                        val artistBitmap = remember { mutableStateOf<Bitmap?>(null) }
                        val request = ImageRequest.Builder(context)
                            .data(url)
                            .target { drawable -> artistBitmap.value = drawable.toBitmap() }
                            .build()

                        imageLoader.enqueue(request)

                        AsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(Sizes.LARGE))
                                .modifyIf(artistBitmap.value == null) {
                                    background(MaterialTheme.colorScheme.surfaceVariant)
                                }
                                .clickable {
                                    vm.changeArtistImage(artistBitmap.value, artistId)
                                    navController.goBack()
                                },
                            model = artistBitmap.value,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                        )
                    }
                }
            }
        }
    }
}