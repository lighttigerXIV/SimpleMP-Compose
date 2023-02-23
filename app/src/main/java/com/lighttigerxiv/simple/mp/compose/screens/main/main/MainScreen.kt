package com.lighttigerxiv.simple.mp.compose.screens.main.main

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.app_viewmodels.MainVM
import com.lighttigerxiv.simple.mp.compose.app_viewmodels.SettingsVM
import com.lighttigerxiv.simple.mp.compose.composables.MiniPlayer
import com.lighttigerxiv.simple.mp.compose.composables.Player
import com.lighttigerxiv.simple.mp.compose.getSurfaceColor
import com.lighttigerxiv.simple.mp.compose.navigation.BottomNavItem
import com.lighttigerxiv.simple.mp.compose.navigation.BottomNavigationBar
import com.lighttigerxiv.simple.mp.compose.navigation.screens.main.*
import com.lighttigerxiv.simple.mp.compose.screens.main.albums.AlbumsScreen
import com.lighttigerxiv.simple.mp.compose.screens.main.albums.AlbumsScreenVM
import com.lighttigerxiv.simple.mp.compose.screens.main.albums.album.AlbumScreen
import com.lighttigerxiv.simple.mp.compose.screens.main.albums.album.AlbumScreenVM
import com.lighttigerxiv.simple.mp.compose.screens.main.artists.ArtistsScreen
import com.lighttigerxiv.simple.mp.compose.screens.main.artists.ArtistsScreenVM
import com.lighttigerxiv.simple.mp.compose.screens.main.home.HomeScreen
import com.lighttigerxiv.simple.mp.compose.screens.main.home.HomeScreenVM
import com.lighttigerxiv.simple.mp.compose.screens.main.playlists.PlaylistsScreen
import com.lighttigerxiv.simple.mp.compose.screens.main.playlists.PlaylistsScreenVM
import com.lighttigerxiv.simple.mp.compose.screens.main.settings.SettingsScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    mainVM: MainVM,
    settingsVM: SettingsVM,
    activityContext: ViewModelStoreOwner
) {

    //States
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val playerSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val playerSheetScaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = playerSheetState)

    //Variables
    val surfaceColor = mainVM.surfaceColor.collectAsState().value
    val showNavigationBar = mainVM.showNavigationBar.collectAsState().value
    val miniPlayerHeight = mainVM.miniPlayerHeight.collectAsState().value
    val selectedSong = mainVM.selectedSong.collectAsState().value


    navController.addOnDestinationChangedListener { _, destination, _ ->
        when {
            destination.route == "About" -> mainVM.updateShowNavigationBar(false)
            destination.route == "Settings" -> mainVM.updateShowNavigationBar(false)
            destination.route!!.startsWith("Floating") -> mainVM.updateShowNavigationBar(false)
            destination.route!!.startsWith("AddToPlaylist") -> mainVM.updateShowNavigationBar(false)
            destination.route!!.startsWith("SelectArtistCover") -> mainVM.updateShowNavigationBar(false)
            destination.route == "ThemesScreen" -> mainVM.updateShowNavigationBar(false)
            else -> {
                if (!showNavigationBar) mainVM.updateShowNavigationBar(true)
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {


        BottomSheetScaffold(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .weight(1f, fill = true),
            scaffoldState = playerSheetScaffoldState,
            sheetPeekHeight = miniPlayerHeight,
            sheetContent = {

                //Mini Player
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(getSurfaceColor(settingsVM = settingsVM))
                ) {

                    if (
                        selectedSong != null
                        && playerSheetState.isCollapsed
                        && playerSheetState.progress.fraction == 1.0f
                        && playerSheetState.targetValue == BottomSheetValue.Collapsed
                    ) {

                        if (showNavigationBar) {
                            MiniPlayer(mainVM)
                        }

                    } else {

                        if (showNavigationBar || playerSheetState.isExpanded) {
                            Player(
                                mainVM = mainVM,
                                bottomSheetState = playerSheetState,
                                onGoToPage = {
                                    scope.launch {
                                        playerSheetState.collapse()
                                    }

                                    navController.navigate(it)
                                }
                            )
                        }
                    }
                }
            },
            content = {scaffoldInnerPadding->

                NavHost(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(scaffoldInnerPadding),
                    navController = navController,
                    startDestination = "Home",

                ){

                    composable("Home") {
                        HomeScreen(
                            mainVM = mainVM,
                            homeScreenVM = ViewModelProvider(activityContext)[HomeScreenVM::class.java],
                            openPage = { page -> navController.navigate(page) }
                        )
                    }

                    composable("Artists") {
                        ArtistsScreen(
                            mainVM = mainVM,
                            artistsVM = ViewModelProvider(activityContext)[ArtistsScreenVM::class.java],
                            onArtistClicked = { artistID -> navController.navigate("Artist/$artistID") }
                        )
                    }

                    composable("Albums") {
                        AlbumsScreen(
                            mainVM = mainVM,
                            albumsVM = ViewModelProvider(activityContext)[AlbumsScreenVM::class.java],
                            onAlbumClicked = { albumID ->

                                ViewModelProvider(activityContext)[AlbumScreenVM::class.java].clearScreen()
                                navController.navigate("albumScreen?albumID=$albumID")
                            }
                        )
                    }

                    composable("Playlists") {
                        PlaylistsScreen(
                            mainVM = mainVM,
                            playlistsVM = ViewModelProvider(activityContext)[PlaylistsScreenVM::class.java],
                            onGenrePlaylistClick = { position ->
                                navController.navigate("GenrePlaylistScreen/$position")
                            },
                            onPlaylistClick = { playlistID ->
                                mainVM.loadPlaylistScreen(playlistID)
                                navController.navigate("PlaylistScreen/$playlistID")
                            }
                        )
                    }


                    composable(
                        route = "Artist/{artistID}",
                        arguments = listOf(
                            navArgument("artistID") { type = NavType.LongType }
                        )
                    )
                    { backStackEntry ->
                        ArtistScreen(
                            mainVM = mainVM,
                            backStackEntry = backStackEntry,
                            onBackClicked = { navController.navigateUp() },
                            onSelectArtistCover = { artistName, artistID ->
                                mainVM.resetSACS()
                                navController.navigate("SelectArtistCoverScreen?artistName=$artistName&artistID=$artistID")
                            },
                            onArtistAlbumOpened = { albumID -> navController.navigate("artistAlbumScreen?albumID=$albumID") }
                        )
                    }
                    composable(
                        route = "ArtistAlbum/{albumID}",
                    ) {

                        val albumID = it.arguments?.getString("ArtistID")?.toLongOrNull()

                        if(albumID != null){

                            AlbumScreen(
                                mainVM = mainVM,
                                albumVM = ViewModelProvider(activityContext)[AlbumScreenVM::class.java],
                                albumID = albumID,
                                onBackClicked = { navController.navigateUp() },
                            )
                        }
                    }

                    composable(
                        route = "SelectArtistCoverScreen?artistName={artistName}&artistID={artistID}",
                    ) { backStackEntry ->

                        val artistName = backStackEntry.arguments?.getString("artistName")
                        val artistID = backStackEntry.arguments?.getString("artistID")

                        if(artistName != null && artistID != null){
                            SelectArtistCoverScreen(
                                mainVM = mainVM,
                                artistName = artistName,
                                artistID = artistID.toLong(),
                                onGoBack = {navController.navigateUp()},
                                onGetImage = {
                                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                                        addCategory(Intent.CATEGORY_OPENABLE)
                                        type = "image/*"
                                        addFlags(FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                                        addFlags(FLAG_GRANT_READ_URI_PERMISSION)
                                    }

                                    //activityContext.applicationContext.startActivityForResult(intent, 2)
                                }
                            )
                        }
                    }

                    composable(
                        route = "albumScreen?albumID={albumID}",
                    ) {

                        val albumID = it.arguments?.getString("albumID")?.toLongOrNull()

                        if(albumID != null){

                            AlbumScreen(
                                mainVM = mainVM,
                                albumVM = ViewModelProvider(activityContext)[AlbumScreenVM::class.java],
                                albumID = albumID,
                                onBackClicked = { navController.navigateUp() }
                            )
                        }
                    }
                    composable("GenrePlaylistScreen/{position}") {
                        val position = it.arguments!!.getString("position")

                        if(position != null){
                            GenrePlaylistScreen(
                                mainVM = mainVM,
                                position = position.toInt(),
                                onBackClicked = { navController.navigateUp() }
                            )
                        }
                    }

                    composable("PlaylistScreen/{playlistID}") {

                        PlaylistScreen(
                            mainVM = mainVM,
                            arguments = it.arguments!!,
                            onBackClick = { navController.navigateUp() },
                            onGetImage = {
                                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                                    addCategory(Intent.CATEGORY_OPENABLE)
                                    type = "image/*"
                                    addFlags(FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                                    addFlags(FLAG_GRANT_READ_URI_PERMISSION)
                                }

                                //startActivityForResult(intent, 1)
                            }
                        )
                    }
                    composable(
                        route = "addToPlaylistScreen?songID={songID}",
                        arguments = listOf(
                            navArgument("songID") { type = NavType.LongType }
                        )
                    ) { backStackEntry ->
                        AddToPlaylistScreen(
                            mainVM = mainVM,
                            backStackEntry = backStackEntry,
                            previousPage = "Home",
                            onBackClick = { navController.navigateUp() }
                        )
                    }

                    composable(
                        route = "floatingArtistScreen?artistID={artistID}",
                        arguments = listOf(
                            navArgument("artistID") { type = NavType.LongType }
                        )
                    ) { backStackEntry ->
                        ArtistScreen(
                            mainVM = mainVM,
                            backStackEntry = backStackEntry,
                            onBackClicked = { navController.navigateUp() },
                            onSelectArtistCover = { artistName, artistID ->
                                mainVM.resetSACS()
                                navController.navigate("FloatingSelectArtistCoverScreen?artistName=$artistName&artistID=$artistID")
                            },
                            onArtistAlbumOpened = { albumID -> navController.navigate("floatingArtistAlbumScreen?albumID=$albumID") }
                        )
                    }

                    composable(
                        route = "floatingArtistAlbumScreen?albumID={albumID}",
                    ) {

                        val albumID = it.arguments?.getString("albumID")?.toLongOrNull()

                        if(albumID != null){

                            AlbumScreen(
                                mainVM = mainVM,
                                albumVM = ViewModelProvider(activityContext)[AlbumScreenVM::class.java],
                                albumID = albumID,
                                onBackClicked = { navController.navigateUp() }
                            )
                        }
                    }

                    composable(
                        route = "floatingAlbumScreen?albumID={albumID}",
                    ) {

                        val albumID = it.arguments?.getString("albumID")?.toLongOrNull()

                        if(albumID != null){

                            AlbumScreen(
                                mainVM = mainVM,
                                albumVM = ViewModelProvider(activityContext)[AlbumScreenVM::class.java],
                                albumID = albumID,
                                onBackClicked = { navController.navigateUp() }
                            )
                        }
                    }

                    composable(
                        route = "FloatingSelectArtistCoverScreen?artistName={artistName}&artistID={artistID}",
                    ) { backStackEntry ->

                        val artistName = backStackEntry.arguments?.getString("artistName")
                        val artistID = backStackEntry.arguments?.getLong("artistID")

                        if(artistName != null && artistID != null){
                            SelectArtistCoverScreen(
                                mainVM = mainVM,
                                artistName = artistName,
                                artistID = artistID,
                                onGoBack = {navController.navigateUp()},
                                onGetImage = {
                                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                                        addCategory(Intent.CATEGORY_OPENABLE)
                                        type = "image/*"
                                        addFlags(FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                                        addFlags(FLAG_GRANT_READ_URI_PERMISSION)
                                    }

                                    //startActivityForResult(intent, 2)
                                }
                            )
                        }
                    }

                    composable("Settings") {
                        SettingsScreen(
                            mainVM = mainVM,
                            onBackPressed = { navController.navigateUp() },
                            onOpenScreen = { navController.navigate(it) }
                        )
                    }

                    composable("About") {
                        AboutScreen(
                            mainVM = mainVM,
                            onBackClick = { navController.navigateUp() }
                        )
                    }

                    composable("Themes") {

                        ThemesScreen(
                            mainVM = mainVM,
                            onBackClick = { navController.navigateUp() }
                        )
                    }
                }
            }
        )


        AnimatedVisibility(
            visible = showNavigationBar,
            exit = shrinkVertically(),
            enter = expandVertically()
        ) {

            BottomNavigationBar(
                navController = navController,
                items = getNavigationItems(context),
                onItemClick = { bottomNavItem ->

                    navController.navigate(bottomNavItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}


fun getNavigationItems(context: Context): List<BottomNavItem> {

    val itemsList = ArrayList<BottomNavItem>()

    itemsList.add(
        BottomNavItem(
            name = "Home",
            route = "Home",
            activeIcon = BitmapFactory.decodeResource(context.resources, R.drawable.icon_home_solid).asImageBitmap(),
            inactiveIcon = BitmapFactory.decodeResource(context.resources, R.drawable.icon_home_regular).asImageBitmap()
        )
    )

    itemsList.add(
        BottomNavItem(
            name = "Artists",
            route = "Artists",
            activeIcon = BitmapFactory.decodeResource(context.resources, R.drawable.icon_person_solid).asImageBitmap(),
            inactiveIcon = BitmapFactory.decodeResource(context.resources, R.drawable.icon_person_regular).asImageBitmap()
        )
    )

    itemsList.add(
        BottomNavItem(
            name = "Albums",
            route = "Albums",
            activeIcon = BitmapFactory.decodeResource(context.resources, R.drawable.icon_cd_solid).asImageBitmap(),
            inactiveIcon = BitmapFactory.decodeResource(context.resources, R.drawable.icon_cd_regular).asImageBitmap()
        )
    )

    itemsList.add(
        BottomNavItem(
            name = "Playlists",
            route = "Playlists",
            activeIcon = BitmapFactory.decodeResource(context.resources, R.drawable.icon_playlist_solid).asImageBitmap(),
            inactiveIcon = BitmapFactory.decodeResource(context.resources, R.drawable.icon_playlist_regular).asImageBitmap()
        )
    )

    return itemsList
}