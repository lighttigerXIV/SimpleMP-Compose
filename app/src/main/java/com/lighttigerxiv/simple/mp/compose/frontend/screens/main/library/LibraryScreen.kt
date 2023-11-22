package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.constraintlayout.compose.layoutId
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.lighttigerxiv.simple.mp.compose.R
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
import com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.home.HomeScreen
import com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.player.Player
import com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.playlists.PlaylistsScreen
import com.lighttigerxiv.simple.mp.compose.frontend.utils.ChangeNavigationBarsColor
import com.lighttigerxiv.simple.mp.compose.frontend.utils.Sizes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LibraryScreen(
    vm: LibraryScreenVM = viewModel(factory = LibraryScreenVM.Factory)
) {

    val navController = rememberNavController()
    val uiState = vm.uiState.collectAsState().value
    val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)
    val scope = rememberCoroutineScope()

    ChangeNavigationBarsColor(color = MaterialTheme.colorScheme.surfaceVariant)


    LaunchedEffect(sheetState) {
        snapshotFlow { sheetState.progress }
            .collect { progress ->
                vm.updateNavbarAnimation(progress, sheetState)
            }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colorScheme.surface,
        sheetBackgroundColor = MaterialTheme.colorScheme.surfaceVariant,
        sheetShape = RoundedCornerShape(topStart = Sizes.MEDIUM, topEnd = Sizes.MEDIUM),
        sheetPeekHeight = if (uiState.currentSong != null) 130.dp else 55.dp,
        sheetContent = {

            uiState.currentSong?.let {
                Player(
                    hideMiniPlayer = uiState.hideMiniPlayer,
                    onOpenPlayer = { scope.launch { scaffoldState.bottomSheetState.expand() } },
                    onClosePlayer = { scope.launch { scaffoldState.bottomSheetState.collapse() } },
                    showPlayerProgress = uiState.showPlayerProgress
                )
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)

        ) {

            NavHost(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Sizes.LARGE),
                navController = navController,
                startDestination = Routes.Main.Library.HOME
            ) {
                composable(Routes.Main.Library.HOME) {
                    HomeScreen(navController)
                }

                navigation(
                    route = Routes.Main.Library.ARTISTS_ROOT,
                    startDestination = Routes.Main.Library.ARTISTS
                ){

                    composable(Routes.Main.Library.ARTISTS) {
                        ArtistsScreen(navController = navController)
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
                            navController = navController
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
                            navController = navController
                        )
                    }
                }

                navigation(
                    route = Routes.Main.Library.ALBUMS_ROOT,
                    startDestination = Routes.Main.Library.ALBUMS
                ){

                    composable(Routes.Main.Library.ALBUMS) {
                        AlbumsScreen(navController = navController)
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
                            navController = navController
                        )
                    }
                }


                composable(Routes.Main.Library.PLAYLISTS) {
                    PlaylistsScreen()
                }
            }
        }
    }

    NavigationBar(uiState = uiState, navController = navController)
}

@OptIn(ExperimentalMotionApi::class)
@Composable
fun NavigationBar(
    uiState: LibraryScreenVM.UiState,
    navController: NavHostController,
) {

    val context = LocalContext.current
    val items = remember { getNavbarItems() }
    val backStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""
    val destinationRoute = if (backStackEntry?.destination?.route == null) "" else backStackEntry.destination.route

    MotionLayout(
        modifier = Modifier
            .fillMaxSize(),
        motionScene = MotionScene(
            content = context.resources
                .openRawResource(R.raw.hide_navbar)
                .readBytes()
                .decodeToString()
        ),
        progress = uiState.hideNavBarProgress
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .layoutId("navbar"),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            items.forEach { navbarItem ->

                val isSelected = navbarItem.route == destinationRoute
                        || navbarItem.route == Routes.Main.Library.ARTISTS_ROOT && destinationRoute == Routes.Main.Library.ARTISTS
                        || navbarItem.route == Routes.Main.Library.ARTISTS_ROOT && destinationRoute?.startsWith(Routes.Main.Library.ARTISTS_ARTIST) ?: false
                        || navbarItem.route == Routes.Main.Library.ARTISTS_ROOT && destinationRoute?.startsWith(Routes.Main.Library.ARTISTS_ARTIST_ALBUM) ?: false
                        || navbarItem.route == Routes.Main.Library.ALBUMS_ROOT && destinationRoute == Routes.Main.Library.ALBUMS
                        || navbarItem.route == Routes.Main.Library.ALBUMS_ROOT && destinationRoute?.startsWith(Routes.Main.Library.ALBUMS_ALBUM) ?: false


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
        Routes.Main.Library.PLAYLISTS -> navController.goToPlaylists()
    }
}