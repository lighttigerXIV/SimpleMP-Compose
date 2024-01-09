package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.lighttigerxiv.layout_scaffold.LayoutScaffold
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.frontend.composables.HSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.composables.VSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.Routes
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.getNavbarItems
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goToAlbums
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goToArtists
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goToHome
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goToPlaylists
import com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.albums.AlbumsScreen
import com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.albums.album.AlbumScreen
import com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.artists.ArtistsScreen
import com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.artists.artist.ArtistScreen
import com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.artists.artist.select_cover.SelectArtistCoverScreen
import com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.home.HomeScreen
import com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.player.Player
import com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.playlists.PlaylistsScreen
import com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.playlists.genre_playlist.GenrePlaylistScreen
import com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.playlists.playlist.PlaylistScreen
import com.lighttigerxiv.simple.mp.compose.frontend.utils.ChangeNavigationBarsColor
import com.lighttigerxiv.simple.mp.compose.frontend.utils.Sizes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.mongodb.kbson.BsonObjectId

@ExperimentalMaterial3Api
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LibraryScreen(
    rootController: NavHostController,
    vm: LibraryScreenVM = viewModel(factory = LibraryScreenVM.Factory)
) {

    val navController = rememberNavController()
    val uiState = vm.uiState.collectAsState().value
    val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)
    val scope = rememberCoroutineScope()

    ChangeNavigationBarsColor(color = MaterialTheme.colorScheme.surfaceVariant)

    LayoutScaffold(
        landscapeNavigationBar = {
            NavigationBar(navController = navController, inLandscape = true)
        }
    ) { _, inLandscape ->

        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            backgroundColor = MaterialTheme.colorScheme.surface,
            sheetBackgroundColor = MaterialTheme.colorScheme.surfaceVariant,
            sheetShape = RoundedCornerShape(topStart = Sizes.MEDIUM, topEnd = Sizes.MEDIUM),
            sheetPeekHeight = 0.dp,
            sheetElevation = 0.dp,
            sheetContent = {

                uiState.currentSong?.let {
                    Player(
                        onClosePlayer = { scope.launch { scaffoldState.bottomSheetState.collapse() } },
                        rootController = rootController,
                        inLandscape = inLandscape
                    )
                }
            }
        ) { paddingValues ->

            Box(modifier = Modifier.fillMaxSize()) {

                Column(Modifier.fillMaxSize()) {

                    NavHost(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f, fill = true)
                            .padding(paddingValues)
                            .padding(Sizes.LARGE),
                        navController = navController,
                        startDestination = Routes.Main.Library.HOME
                    ) {

                        composable(Routes.Main.Library.HOME) {
                            HomeScreen(rootController)
                        }

                        navigation(
                            route = Routes.Main.Library.ARTISTS_ROOT,
                            startDestination = Routes.Main.Library.ARTISTS
                        ) {

                            composable(Routes.Main.Library.ARTISTS) {
                                ArtistsScreen(navController = navController, inLandscape = inLandscape, showMiniPlayer = uiState.showMiniPlayer)
                            }

                            composable(
                                "${Routes.Main.Library.ARTISTS_ARTIST}/{id}",
                                arguments = listOf(
                                    navArgument("id") { type = NavType.LongType }
                                )
                            ) { navBackStackEntry ->

                                val id = navBackStackEntry.arguments?.getLong("id") ?: 0L

                                ArtistScreen(
                                    artistId = id,
                                    navController = navController,
                                    showMiniPlayer = uiState.showMiniPlayer
                                )
                            }

                            composable(
                                "${Routes.Main.Library.ARTISTS_ARTIST_ALBUM}/{id}",
                                arguments = listOf(
                                    navArgument("id") { type = NavType.LongType }
                                )
                            ) { navBackStackEntry ->

                                val id = navBackStackEntry.arguments?.getLong("id") ?: 0L

                                AlbumScreen(
                                    albumId = id,
                                    navController = navController,
                                    showMiniPlayer = uiState.showMiniPlayer
                                )
                            }

                            composable(
                                "${Routes.Main.Library.ARTISTS_ARTIST_SELECT_COVER}/{id}",
                                arguments = listOf(
                                    navArgument("id") { type = NavType.LongType }
                                )
                            ) { navBackStackEntry ->

                                val id = navBackStackEntry.arguments?.getLong("id") ?: 0L

                                SelectArtistCoverScreen(artistId = id, navController = navController, inLandscape = inLandscape)
                            }
                        }

                        navigation(
                            route = Routes.Main.Library.ALBUMS_ROOT,
                            startDestination = Routes.Main.Library.ALBUMS
                        ) {

                            composable(Routes.Main.Library.ALBUMS) {
                                AlbumsScreen(
                                    navController = navController,
                                    inLandscape = inLandscape,
                                    showMiniPlayer = uiState.showMiniPlayer
                                )
                            }

                            composable(
                                "${Routes.Main.Library.ALBUMS_ALBUM}/{id}",
                                arguments = listOf(
                                    navArgument("id") { type = NavType.LongType }
                                )
                            ) { navBackStackEntry ->

                                val id = navBackStackEntry.arguments?.getLong("id") ?: 0L

                                AlbumScreen(
                                    albumId = id,
                                    navController = navController,
                                    showMiniPlayer = uiState.showMiniPlayer
                                )
                            }
                        }

                        navigation(
                            route = Routes.Main.Library.PLAYLISTS_ROOT,
                            startDestination = Routes.Main.Library.PLAYLISTS
                        ) {
                            composable(Routes.Main.Library.PLAYLISTS) {
                                PlaylistsScreen(
                                    navController = navController,
                                    inLandscape = inLandscape,
                                    showMiniPlayer = uiState.showMiniPlayer
                                )
                            }

                            composable(
                                "${Routes.Main.Library.PLAYLISTS_GENRE}/{id}",
                                arguments = listOf(
                                    navArgument("id") { type = NavType.StringType }
                                )
                            ) { backStackEntry ->

                                GenrePlaylistScreen(
                                    navController = navController,
                                    playlistId = BsonObjectId(backStackEntry.arguments?.getString("id") ?: "n/a"),
                                    showMiniPlayer = uiState.showMiniPlayer
                                )
                            }

                            composable(
                                "${Routes.Main.Library.PLAYLISTS_USER}/{id}",
                                arguments = listOf(
                                    navArgument("id") { type = NavType.StringType }
                                )
                            ) { backStackEntry ->

                                PlaylistScreen(
                                    navController = navController,
                                    rootController = rootController,
                                    playlistId = BsonObjectId(backStackEntry.arguments?.getString("id") ?: "n/a"),
                                    showMiniPlayer = uiState.showMiniPlayer
                                )
                            }
                        }
                    }

                    if (!inLandscape) {
                        NavigationBar(navController = navController)
                    }
                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {

                    Column(
                        Modifier
                            .offset(y = if (inLandscape) (-5).dp else (-65).dp)
                            .padding(start = Sizes.SMALL, end = Sizes.SMALL)
                    ) {
                        MiniPlayer(vm = vm, uiState = uiState, sheetState = sheetState) {
                            scope.launch(Dispatchers.IO) {
                                sheetState.expand()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NavigationBar(
    navController: NavHostController,
    inLandscape: Boolean = false
) {

    val items = remember { getNavbarItems() }
    val backStackEntry = navController.currentBackStackEntryAsState().value
    val destinationRoute = if (backStackEntry?.destination?.route == null) "" else backStackEntry.destination.route

    if (inLandscape) {

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(60.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            items.forEach { navbarItem ->

                val isSelected = navbarItem.route == destinationRoute
                        || navbarItem.route == Routes.Main.Library.ARTISTS_ROOT && destinationRoute == Routes.Main.Library.ARTISTS
                        || navbarItem.route == Routes.Main.Library.ARTISTS_ROOT && destinationRoute?.startsWith(Routes.Main.Library.ARTISTS_ARTIST) ?: false
                        || navbarItem.route == Routes.Main.Library.ARTISTS_ROOT && destinationRoute?.startsWith(Routes.Main.Library.ARTISTS_ARTIST_ALBUM) ?: false
                        || navbarItem.route == Routes.Main.Library.ALBUMS_ROOT && destinationRoute == Routes.Main.Library.ALBUMS
                        || navbarItem.route == Routes.Main.Library.ALBUMS_ROOT && destinationRoute?.startsWith(Routes.Main.Library.ALBUMS_ALBUM) ?: false
                        || navbarItem.route == Routes.Main.Library.PLAYLISTS_ROOT && destinationRoute == Routes.Main.Library.PLAYLISTS
                        || navbarItem.route == Routes.Main.Library.PLAYLISTS_ROOT && destinationRoute?.startsWith(Routes.Main.Library.PLAYLISTS_GENRE) ?: false
                        || navbarItem.route == Routes.Main.Library.PLAYLISTS_ROOT && destinationRoute?.startsWith(Routes.Main.Library.PLAYLISTS_USER) ?: false


                val icon = if (isSelected) navbarItem.activeIconId else navbarItem.inactiveIconId
                val tintColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = true)
                        .clickable { openNavbarRoute(navController, navbarItem.route) }
                        .padding(Sizes.SMALL),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Icon(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        tint = tintColor
                    )

                    VSpacer(size = Sizes.SMALL)

                    Box(
                        modifier = Modifier
                            .width(20.dp)
                            .height(2.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                    )
                }
            }
        }

    } else {


        Row(
            modifier = Modifier
                .layoutId("navbar")
                .background(MaterialTheme.colorScheme.surfaceVariant),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            items.forEach { navbarItem ->

                val isSelected = navbarItem.route == destinationRoute
                        || navbarItem.route == Routes.Main.Library.ARTISTS_ROOT && destinationRoute == Routes.Main.Library.ARTISTS
                        || navbarItem.route == Routes.Main.Library.ARTISTS_ROOT && destinationRoute?.startsWith(Routes.Main.Library.ARTISTS_ARTIST) ?: false
                        || navbarItem.route == Routes.Main.Library.ARTISTS_ROOT && destinationRoute?.startsWith(Routes.Main.Library.ARTISTS_ARTIST_ALBUM) ?: false
                        || navbarItem.route == Routes.Main.Library.ALBUMS_ROOT && destinationRoute == Routes.Main.Library.ALBUMS
                        || navbarItem.route == Routes.Main.Library.ALBUMS_ROOT && destinationRoute?.startsWith(Routes.Main.Library.ALBUMS_ALBUM) ?: false
                        || navbarItem.route == Routes.Main.Library.PLAYLISTS_ROOT && destinationRoute == Routes.Main.Library.PLAYLISTS
                        || navbarItem.route == Routes.Main.Library.PLAYLISTS_ROOT && destinationRoute?.startsWith(Routes.Main.Library.PLAYLISTS_GENRE) ?: false
                        || navbarItem.route == Routes.Main.Library.PLAYLISTS_ROOT && destinationRoute?.startsWith(Routes.Main.Library.PLAYLISTS_USER) ?: false


                val icon = if (isSelected) navbarItem.activeIconId else navbarItem.inactiveIconId
                val tintColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = true)
                        .clickable { openNavbarRoute(navController, navbarItem.route) }
                        .padding(Sizes.SMALL),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        tint = tintColor
                    )

                    VSpacer(size = Sizes.SMALL)

                    Box(
                        modifier = Modifier
                            .width(20.dp)
                            .height(2.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                    )
                }
            }
        }
    }
}

fun openNavbarRoute(navController: NavHostController, route: String) {
    when (route) {
        Routes.Main.Library.HOME -> navController.goToHome()
        Routes.Main.Library.ARTISTS_ROOT -> navController.goToArtists()
        Routes.Main.Library.ALBUMS_ROOT -> navController.goToAlbums()
        Routes.Main.Library.PLAYLISTS_ROOT -> navController.goToPlaylists()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MiniPlayer(
    vm: LibraryScreenVM,
    uiState: LibraryScreenVM.UiState,
    sheetState: BottomSheetState,
    onClick: () -> Unit
) {


    uiState.currentSong?.let {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .clip(RoundedCornerShape(Sizes.SMALL))
                .clickable { onClick() }
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .pointerInput(Unit) {
                    detectVerticalDragGestures { _, dragAmount ->
                        if (dragAmount < -40 && sheetState.isCollapsed ) {
                            onClick()
                        }
                    }
                }
                .padding(Sizes.MEDIUM),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (uiState.smallAlbumArt != null) {
                Image(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(Sizes.SMALL)),
                    bitmap = uiState.smallAlbumArt.asImageBitmap(),
                    contentDescription = null
                )
            } else {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(Sizes.SMALL))
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .background(MaterialTheme.colorScheme.surfaceVariant)

                ) {
                    androidx.compose.material3.Icon(
                        modifier = Modifier.fillMaxHeight(),
                        painter = painterResource(id = R.drawable.album),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }


            HSpacer(size = Sizes.LARGE)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = true)
            ) {

                Text(
                    text = uiState.currentSong.name,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = uiState.currentSongArtistName,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            HSpacer(size = Sizes.LARGE)

            androidx.compose.material3.Icon(
                modifier = Modifier
                    .size(22.dp)
                    .clickable { vm.pauseOrResume() },
                painter = painterResource(id = if (uiState.isPlaying) R.drawable.pause else R.drawable.play),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}