package com.lighttigerxiv.simple.mp.compose.screens.main.main

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.activities.main.MainVM
import com.lighttigerxiv.simple.mp.compose.settings.SettingsVM
import com.lighttigerxiv.simple.mp.compose.ui.composables.MiniPlayer
import com.lighttigerxiv.simple.mp.compose.navigation.BottomNavItem
import com.lighttigerxiv.simple.mp.compose.navigation.BottomNavigationBar
import com.lighttigerxiv.simple.mp.compose.screens.main.about.AboutScreen
import com.lighttigerxiv.simple.mp.compose.screens.main.add_to_playlist.AddToPlaylistScreen
import com.lighttigerxiv.simple.mp.compose.screens.main.add_to_playlist.AddToPlaylistScreenVM
import com.lighttigerxiv.simple.mp.compose.screens.main.albums.AlbumsScreen
import com.lighttigerxiv.simple.mp.compose.screens.main.albums.AlbumsScreenVM
import com.lighttigerxiv.simple.mp.compose.screens.main.albums.album.AlbumScreen
import com.lighttigerxiv.simple.mp.compose.screens.main.albums.album.AlbumScreenVM
import com.lighttigerxiv.simple.mp.compose.screens.main.artist.ArtistScreen
import com.lighttigerxiv.simple.mp.compose.screens.main.artist.ArtistScreenVM
import com.lighttigerxiv.simple.mp.compose.screens.main.artist.artist_album.ArtistAlbumScreen
import com.lighttigerxiv.simple.mp.compose.screens.main.artist.artist_album.ArtistAlbumScreenVM
import com.lighttigerxiv.simple.mp.compose.screens.main.artist.artist_select_cover.SelectArtistCoverScreen
import com.lighttigerxiv.simple.mp.compose.screens.main.artist.artist_select_cover.SelectArtistCoverScreenVM
import com.lighttigerxiv.simple.mp.compose.screens.main.artists.ArtistsScreen
import com.lighttigerxiv.simple.mp.compose.screens.main.artists.ArtistsScreenVM
import com.lighttigerxiv.simple.mp.compose.screens.main.floating_album.FloatingAlbumScreen
import com.lighttigerxiv.simple.mp.compose.screens.main.floating_album.FloatingAlbumScreenVM
import com.lighttigerxiv.simple.mp.compose.screens.main.floating_artist.FloatingArtistScreen
import com.lighttigerxiv.simple.mp.compose.screens.main.floating_artist.FloatingArtistScreenVM
import com.lighttigerxiv.simple.mp.compose.screens.main.home.HomeScreen
import com.lighttigerxiv.simple.mp.compose.screens.main.home.HomeScreenVM
import com.lighttigerxiv.simple.mp.compose.screens.main.player.Player
import com.lighttigerxiv.simple.mp.compose.screens.main.player.PlayerScreenVM
import com.lighttigerxiv.simple.mp.compose.screens.main.playlists.PlaylistsScreen
import com.lighttigerxiv.simple.mp.compose.screens.main.playlists.PlaylistsScreenVM
import com.lighttigerxiv.simple.mp.compose.screens.main.playlists.genre_playlists.GenrePlaylistScreen
import com.lighttigerxiv.simple.mp.compose.screens.main.playlists.playlist.PlaylistScreen
import com.lighttigerxiv.simple.mp.compose.screens.main.playlists.playlist.PlaylistScreenVM
import com.lighttigerxiv.simple.mp.compose.screens.main.playlists.playlist.add_songs.AddSongsScreen
import com.lighttigerxiv.simple.mp.compose.screens.main.playlists.playlist.add_songs.AddSongsScreenVM
import com.lighttigerxiv.simple.mp.compose.screens.main.settings.SettingsScreen
import com.lighttigerxiv.simple.mp.compose.screens.main.settings.SettingsScreenVM
import com.lighttigerxiv.simple.mp.compose.screens.main.settings.themes.ThemesScreen
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.net.URLEncoder

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    mainVM: MainVM,
    activityContext: ViewModelStoreOwner,
    onGetPlaylistImage: () -> Unit,
    onGetArtistCover: () -> Unit
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

    val navBarHeight = mainVM.navBarHeight.collectAsState().value


    navController.addOnDestinationChangedListener { _, destination, _ ->
        when {
            destination.route == "About" -> mainVM.updateShowNavigationBar(false)
            destination.route == "Settings" -> mainVM.updateShowNavigationBar(false)
            destination.route!!.startsWith("Floating") -> mainVM.updateShowNavigationBar(false)
            destination.route!!.startsWith("AddToPlaylist") -> mainVM.updateShowNavigationBar(false)
            destination.route!!.startsWith("SelectArtistCover") -> mainVM.updateShowNavigationBar(false)
            destination.route == "Themes" -> mainVM.updateShowNavigationBar(false)
            destination.route!!.startsWith("AddSongsToPlaylist") -> mainVM.updateShowNavigationBar(false)
            else -> {
                if (!showNavigationBar) mainVM.updateShowNavigationBar(true)
            }
        }
    }

    LaunchedEffect(playerSheetState.currentValue){

        if (playerSheetState.currentValue == BottomSheetValue.Collapsed){

            mainVM.updateNavbarHeight(55.dp)
        }else{

            mainVM.updateNavbarHeight(0.dp)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(surfaceColor)
    ) {


        BottomSheetScaffold(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = true),
            scaffoldState = playerSheetScaffoldState,
            sheetPeekHeight = miniPlayerHeight,
            sheetShape = RoundedCornerShape(
                topStart = 14.dp,
                topEnd = 14.dp
            ),
            sheetContent = {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant)
                ) {

                    if (
                        selectedSong != null
                        && playerSheetState.isCollapsed
                        && playerSheetState.progress.fraction == 1.0f
                        && playerSheetState.targetValue == BottomSheetValue.Collapsed
                    ) {

                        if (showNavigationBar) {
                            MiniPlayer(
                                mainVM = mainVM
                            )
                        }

                    } else {

                        if (showNavigationBar || playerSheetState.isExpanded) {
                            Player(
                                mainVM = mainVM,
                                playerVM = ViewModelProvider(activityContext)[PlayerScreenVM::class.java],
                                onGoToPage = {page->

                                    scope.launch {
                                        playerSheetState.collapse()
                                    }

                                    if(page.startsWith("FloatingArtist")){
                                        ViewModelProvider(activityContext)[FloatingArtistScreenVM::class.java].clearScreen()
                                    }

                                    if (page.startsWith("FloatingAlbum")) {
                                        ViewModelProvider(activityContext)[FloatingAlbumScreenVM::class.java].clearScreen()
                                    }

                                    if (page.startsWith("AddToPlaylist")) {
                                        ViewModelProvider(activityContext)[AddToPlaylistScreenVM::class.java].clearScreen()
                                    }

                                    navController.navigate(page)
                                }
                            )
                        }
                    }
                }
            },
            content = { scaffoldInnerPadding ->

                NavHost(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(surfaceColor)
                        .padding(scaffoldInnerPadding),
                    navController = navController,
                    startDestination = "Home",

                    ) {

                    composable("Home") {
                        HomeScreen(
                            mainVM = mainVM,
                            homeScreenVM = ViewModelProvider(activityContext)[HomeScreenVM::class.java],
                            openPage = { page ->

                                if(page.startsWith("FloatingArtist")){
                                    ViewModelProvider(activityContext)[FloatingArtistScreenVM::class.java].clearScreen()
                                }

                                if (page.startsWith("FloatingAlbum")) {
                                    ViewModelProvider(activityContext)[FloatingAlbumScreenVM::class.java].clearScreen()
                                }

                                if (page.startsWith("AddToPlaylist")) {
                                    ViewModelProvider(activityContext)[AddToPlaylistScreenVM::class.java].clearScreen()
                                }

                                navController.navigate(page)
                            }
                        )
                    }

                    composable("Artists") {
                        ArtistsScreen(
                            mainVM = mainVM,
                            artistsVM = ViewModelProvider(activityContext)[ArtistsScreenVM::class.java],
                            onArtistClicked = { artistID ->

                                ViewModelProvider(activityContext)[ArtistScreenVM::class.java].clearScreen()
                                navController.navigate("Artist/$artistID")
                            }
                        )
                    }

                    composable("Albums") {
                        AlbumsScreen(
                            mainVM = mainVM,
                            albumsVM = ViewModelProvider(activityContext)[AlbumsScreenVM::class.java],
                            onAlbumClicked = { albumID ->

                                ViewModelProvider(activityContext)[AlbumScreenVM::class.java].clearScreen()
                                navController.navigate("Album/$albumID")
                            }
                        )
                    }

                    composable("Playlists") {
                        PlaylistsScreen(
                            mainVM = mainVM,
                            playlistsVM = ViewModelProvider(activityContext)[PlaylistsScreenVM::class.java],
                            onGenrePlaylistClick = {

                                //Prevents crash with genres with "/"
                                val encodedGenre = URLEncoder.encode(it, "UTF-8")

                                navController.navigate("GenrePlaylist/$encodedGenre")
                            },
                            onPlaylistClick = { playlistID ->

                                ViewModelProvider(activityContext)[PlaylistScreenVM::class.java].clearScreen()
                                navController.navigate("Playlist/$playlistID")
                            }
                        )
                    }


                    composable(route = "Artist/{artistID}")
                    {

                        val artistID = it.arguments?.getString("artistID")?.toLongOrNull()

                        artistID?.let {
                            ArtistScreen(
                                mainVM = mainVM,
                                settingsVM = ViewModelProvider(activityContext)[SettingsVM::class.java],
                                artistID = artistID,
                                artistVM = ViewModelProvider(activityContext)[ArtistScreenVM::class.java],
                                onBackClicked = { navController.navigateUp() },
                                onSelectArtistCover = { artistName, artistID ->

                                    ViewModelProvider(activityContext)[SelectArtistCoverScreenVM::class.java].clearScreen()

                                    navController.navigate("SelectArtistCover/artist=$artistName&artistID=$artistID")
                                },
                                onOpenAlbum = { albumID ->

                                    ViewModelProvider(activityContext)[ArtistAlbumScreenVM::class.java].clearScreen()
                                    navController.navigate("ArtistAlbum/$albumID")
                                }
                            )
                        }
                    }

                    composable(route = "ArtistAlbum/{albumID}") {

                        val albumID = it.arguments?.getString("albumID")?.toLongOrNull()

                        albumID?.let {

                            ArtistAlbumScreen(
                                mainVM = mainVM,
                                albumVM = ViewModelProvider(activityContext)[ArtistAlbumScreenVM::class.java],
                                albumID = albumID,
                                onBackClicked = { navController.navigateUp() },
                            )
                        }
                    }

                    composable(
                        route = "SelectArtistCover/artist={artistName}&artistID={artistID}",
                    ) {

                        val artistName = it.arguments?.getString("artistName")
                        val artistID = it.arguments?.getString("artistID")?.toLongOrNull()


                        if (artistName != null && artistID != null) {
                            SelectArtistCoverScreen(
                                mainVM = mainVM,
                                selectArtistCoverVM = ViewModelProvider(activityContext)[SelectArtistCoverScreenVM::class.java],
                                artistVM = ViewModelProvider(activityContext)[ArtistScreenVM::class.java],
                                artistName = artistName,
                                artistID = artistID,
                                onGoBack = {
                                    navController.navigateUp()
                                },
                                onGetArtistCover = {
                                    onGetArtistCover()
                                }
                            )
                        }
                    }

                    composable(
                        route = "Album/{albumID}",
                    ) {

                        val albumID = it.arguments?.getString("albumID")?.toLongOrNull()

                        albumID?.let {

                            AlbumScreen(
                                mainVM = mainVM,
                                albumVM = ViewModelProvider(activityContext)[AlbumScreenVM::class.java],
                                albumID = albumID,
                                onBackClicked = { navController.navigateUp() }
                            )
                        }
                    }
                    composable("GenrePlaylist/{genre}") {
                        var genre = it.arguments?.getString("genre")

                        if(genre != null){

                            genre = URLDecoder.decode(genre, "UTF-8")
                        }

                        genre?.let {
                            GenrePlaylistScreen(
                                mainVM = mainVM,
                                genre = genre,
                                onBackClicked = { navController.navigateUp() }
                            )
                        }
                    }

                    composable("Playlist/{playlistID}") {

                        val playlistID = it.arguments?.getString("playlistID")

                        playlistID?.let {

                            PlaylistScreen(
                                mainVM = mainVM,
                                playlistsVM = ViewModelProvider(activityContext)[PlaylistsScreenVM::class.java],
                                playlistVM = ViewModelProvider(activityContext)[PlaylistScreenVM::class.java],
                                playlistID = playlistID,
                                onGoBack = { navController.navigateUp() },
                                onGetImage = {
                                    onGetPlaylistImage()
                                },
                                onAddSongs = {

                                    ViewModelProvider(activityContext)[AddSongsScreenVM::class.java].clearScreen()

                                    navController.navigate("AddSongsToPlaylist/$playlistID")
                                }
                            )
                        }
                    }

                    composable("AddSongsToPlaylist/{playlistID}") {

                        val playlistID = it.arguments?.getString("playlistID")

                        playlistID?.let {

                            AddSongsScreen(
                                mainVM = mainVM,
                                addSongsVM = ViewModelProvider(activityContext)[AddSongsScreenVM::class.java],
                                playlistVM = ViewModelProvider(activityContext)[PlaylistScreenVM::class.java],
                                playlistID = playlistID,
                                onGoBack = {

                                    navController.navigateUp()
                                }
                            )
                        }
                    }

                    composable(
                        route = "AddToPlaylist/{songID}"
                    ) {

                        val songID = it.arguments?.getString("songID")?.toLongOrNull()

                        songID?.let {
                            AddToPlaylistScreen(
                                mainVM = mainVM,
                                songID = songID,
                                playlistsVM = ViewModelProvider(activityContext)[PlaylistsScreenVM::class.java],
                                addToPlaylistVM = ViewModelProvider(activityContext)[AddToPlaylistScreenVM::class.java],
                                previousPage = "Home",
                                onBackClick = { navController.navigateUp() }
                            )
                        }
                    }

                    composable(
                        route = "FloatingArtist/{artistID}"
                    ) {


                        val artistID = it.arguments?.getString("artistID")?.toLongOrNull()

                        artistID?.let {

                            FloatingArtistScreen(
                                mainVM = mainVM,
                                settingsVM = ViewModelProvider(activityContext)[SettingsVM::class.java],
                                artistID = artistID,
                                artistVM = ViewModelProvider(activityContext)[FloatingArtistScreenVM::class.java],
                                onBackClicked = {

                                    navController.navigateUp()
                                }
                            )
                        }
                    }

                    composable(
                        route = "FloatingAlbum/{albumID}",
                    ) {

                        val albumID = it.arguments?.getString("albumID")?.toLongOrNull()

                        albumID?.let {

                            FloatingAlbumScreen(
                                mainVM = mainVM,
                                albumVM = ViewModelProvider(activityContext)[FloatingAlbumScreenVM::class.java],
                                albumID = albumID,
                                onBackClicked = { navController.navigateUp() }
                            )
                        }
                    }

                    composable("Settings") {

                        SettingsScreen(
                            mainVM = mainVM,
                            settingsVM = ViewModelProvider(activityContext)[SettingsVM::class.java],
                            settingsScreenVM = ViewModelProvider(activityContext)[SettingsScreenVM::class.java],
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
                            settingsVM = ViewModelProvider(activityContext)[SettingsVM::class.java],
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
                height = navBarHeight,
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