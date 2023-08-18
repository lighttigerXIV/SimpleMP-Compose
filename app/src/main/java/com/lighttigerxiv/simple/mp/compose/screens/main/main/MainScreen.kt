package com.lighttigerxiv.simple.mp.compose.screens.main.main

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.activities.main.MainVM
import com.lighttigerxiv.simple.mp.compose.data.data_classes.Song
import com.lighttigerxiv.simple.mp.compose.data.variables.ImageSizes
import com.lighttigerxiv.simple.mp.compose.data.variables.Routes
import com.lighttigerxiv.simple.mp.compose.functions.getImage
import com.lighttigerxiv.simple.mp.compose.functions.openScreen
import com.lighttigerxiv.simple.mp.compose.settings.SettingsVM
import com.lighttigerxiv.simple.mp.compose.ui.composables.MiniPlayer
import com.lighttigerxiv.simple.mp.compose.navigation.BottomNavItem
import com.lighttigerxiv.simple.mp.compose.navigation.BottomNavigationBar
import com.lighttigerxiv.simple.mp.compose.screens.main.add_song_to_playlist.AddSongToPlaylistScreenVM
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
import com.lighttigerxiv.simple.mp.compose.screens.main.floating_album.FloatingAlbumScreenVM
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
import kotlinx.coroutines.launch
import java.net.URLDecoder

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    mainVM: MainVM,
    activityContext: ViewModelStoreOwner,
    rootNavController: NavHostController,
    onGetPlaylistImage: () -> Unit,
    onGetArtistCover: () -> Unit
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val mainNavController = rememberNavController()
    val playerSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val playerSheetScaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = playerSheetState)
    val miniPlayerHeight = mainVM.miniPlayerPeekHeight.collectAsState().value
    val hideNavbarMotionProgress = mainVM.hideNavProgress.collectAsState().value
    val showMainPlayer = mainVM.showMainPlayer.collectAsState().value
    val queue = mainVM.queue.collectAsState().value
    val upNextQueue = mainVM.upNextQueue.collectAsState().value
    val currentSong = mainVM.currentSong.collectAsState().value

    mainVM.updateMiniPlayerPeekHeight()


    LaunchedEffect(playerSheetState) {
        snapshotFlow { playerSheetState.progress.fraction }
            .collect {
                mainVM.onSheetChange(it, playerSheetState)
            }
    }

    mainVM.onFinish = {
        scope.launch {
            playerSheetState.collapse()
        }
    }


    if (isPortrait) {
        PortraitScreen(
            context,
            activityContext,
            mainVM,
            hideNavbarMotionProgress,
            playerSheetScaffoldState,
            miniPlayerHeight,
            currentSong,
            queue,
            upNextQueue,
            showMainPlayer,
            mainNavController,
            rootNavController,
            onGetArtistCover = { onGetArtistCover() },
            onGetPlaylistImage = { onGetPlaylistImage() }
        )
    } else {
        LandscapeScreen(
            context,
            activityContext,
            mainVM,
            playerSheetScaffoldState,
            miniPlayerHeight,
            currentSong,
            queue,
            upNextQueue,
            showMainPlayer,
            mainNavController,
            rootNavController,
            onGetArtistCover = { onGetArtistCover() },
            onGetPlaylistImage = { onGetPlaylistImage() }
        )
    }
}

@OptIn(ExperimentalMotionApi::class, ExperimentalMaterialApi::class)
@Composable
fun PortraitScreen(
    context: Context,
    activityContext: ViewModelStoreOwner,
    mainVM: MainVM,
    hideNavbarMotionProgress: Float,
    playerSheetScaffoldState: BottomSheetScaffoldState,
    miniPlayerHeight: Dp,
    currentSong: Song?,
    queue: List<Song>?,
    upNextQueue: List<Song>?,
    showMainPlayer: Boolean,
    navController: NavHostController,
    rootNavController: NavHostController,
    onGetArtistCover: () -> Unit,
    onGetPlaylistImage: () -> Unit
) {


    MotionLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(mainVM.surfaceColor.collectAsState().value),
        motionScene = MotionScene(
            content = context.resources
                .openRawResource(R.raw.hide_navbar_motion)
                .readBytes()
                .decodeToString()
        ),
        progress = hideNavbarMotionProgress
    ) {

        MainScaffold(
            activityContext,
            mainVM,
            playerSheetScaffoldState,
            miniPlayerHeight,
            currentSong,
            queue,
            upNextQueue,
            showMainPlayer,
            navController,
            rootNavController,
            onGetArtistCover = { onGetArtistCover() },
            onGetPlaylistImage = { onGetPlaylistImage() }
        )


        BottomNavigationBar(
            navController = navController,
            items = getNavigationItems(context),
            onItemClick = { navItem ->
                openScreen(navController, navItem.route)
            }
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LandscapeScreen(
    context: Context,
    activityContext: ViewModelStoreOwner,
    mainVM: MainVM,
    playerSheetScaffoldState: BottomSheetScaffoldState,
    miniPlayerHeight: Dp,
    currentSong: Song?,
    queue: List<Song>?,
    upNextQueue: List<Song>?,
    showMainPlayer: Boolean,
    navController: NavHostController,
    rootNavController: NavHostController,
    onGetArtistCover: () -> Unit,
    onGetPlaylistImage: () -> Unit
) {


    Row(
        modifier = Modifier
            .fillMaxSize()
    ) {

        BottomNavigationBar(
            navController = navController,
            items = getNavigationItems(context),
            onItemClick = { navItem ->
                navController.navigate(navItem.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )

        MainScaffold(
            activityContext,
            mainVM,
            playerSheetScaffoldState,
            miniPlayerHeight,
            currentSong,
            queue,
            upNextQueue,
            showMainPlayer,
            navController,
            rootNavController,
            onGetArtistCover = { onGetArtistCover() },
            onGetPlaylistImage = { onGetPlaylistImage() }
        )
    }
}

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun MainScaffold(
    activityContext: ViewModelStoreOwner,
    mainVM: MainVM,
    playerSheetState: BottomSheetScaffoldState,
    miniPlayerHeight: Dp,
    currentSong: Song?,
    queue: List<Song>?,
    upNextQueue: List<Song>?,
    showMainPlayer: Boolean,
    navController: NavHostController,
    rootNavController: NavHostController,
    onGetArtistCover: () -> Unit,
    onGetPlaylistImage: () -> Unit
) {

    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        modifier = Modifier
            .fillMaxSize()
            .layoutId("player"),
        scaffoldState = playerSheetState,
        sheetPeekHeight = miniPlayerHeight,
        backgroundColor = mainVM.surfaceColor.collectAsState().value,
        sheetBackgroundColor = mainVM.surfaceColor.collectAsState().value,
        sheetShape = RoundedCornerShape(
            topStart = 14.dp,
            topEnd = 14.dp
        ),
        sheetContent = {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {

                if (currentSong != null && !showMainPlayer && queue != null && upNextQueue != null) {

                    MiniPlayer(
                        mainVM = mainVM,
                        onClick = {
                            scope.launch { playerSheetState.bottomSheetState.expand() }
                        }
                    )
                }

                if (currentSong != null && queue != null  && upNextQueue != null) {

                    Player(
                        mainVM = mainVM,
                        vm = ViewModelProvider(activityContext)[PlayerScreenVM::class.java],
                        selectedSong = currentSong,
                        queue = queue,
                        upNextQueue = upNextQueue,
                        onOpenPage = { route ->

                            if (route.startsWith(Routes.Root.FLOATING_ARTIST)) {
                                ViewModelProvider(activityContext)[FloatingArtistScreenVM::class.java].clearScreen()
                            }

                            if (route.startsWith(Routes.Root.FLOATING_ALBUM)) {
                                ViewModelProvider(activityContext)[FloatingAlbumScreenVM::class.java].clearScreen()
                            }

                            if (route.startsWith(Routes.Root.ADD_SONG_TO_PLAYLIST)) {
                                ViewModelProvider(activityContext)[AddSongToPlaylistScreenVM::class.java].clearScreen()
                            }

                            openScreen(rootNavController, route)
                        },
                        onClosePLayer = {
                            scope.launch {
                                playerSheetState.bottomSheetState.collapse()
                            }
                        }
                    )
                }
            }
        },
        content = { scaffoldPadding ->

            NavHost(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(scaffoldPadding),
                navController = navController,
                startDestination = Routes.Main.HOME
            ) {

                composable(Routes.Main.HOME) {

                    HomeScreen(
                        mainVM = mainVM,
                        vm = ViewModelProvider(activityContext)[HomeScreenVM::class.java],
                        onOpenScreen = { route ->

                            if (route.startsWith(Routes.Root.FLOATING_ARTIST)) {
                                ViewModelProvider(activityContext)[FloatingArtistScreenVM::class.java].clearScreen()
                            }

                            if (route.startsWith(Routes.Root.FLOATING_ALBUM)) {
                                ViewModelProvider(activityContext)[FloatingAlbumScreenVM::class.java].clearScreen()
                            }

                            if (route.startsWith(Routes.Root.ADD_SONG_TO_PLAYLIST)) {
                                ViewModelProvider(activityContext)[AddSongToPlaylistScreenVM::class.java].clearScreen()
                            }

                            rootNavController.navigate(route)
                        }
                    )
                }

                composable(Routes.Main.ARTISTS) {
                    ArtistsScreen(
                        mainVM,
                        ViewModelProvider(activityContext)[ArtistsScreenVM::class.java],
                        activityContext,
                        navController
                    )
                }

                composable(Routes.Main.ALBUMS) {
                    AlbumsScreen(
                        mainVM = mainVM,
                        vm = ViewModelProvider(activityContext)[AlbumsScreenVM::class.java],
                        onAlbumClicked = { albumID ->

                            ViewModelProvider(activityContext)[AlbumScreenVM::class.java].clearScreen()
                            navController.navigate("Album/$albumID")
                        }
                    )
                }

                composable(Routes.Main.PLAYLISTS) {
                    PlaylistsScreen(
                        mainVM,
                        ViewModelProvider(activityContext)[PlaylistsScreenVM::class.java],
                        activityContext,
                        navController
                    )
                }


                composable(route = "${Routes.Main.ARTIST}{id}")
                {

                    val id = it.arguments?.getString("id")?.toLongOrNull()

                    id?.let {
                        ArtistScreen(
                            mainVM,
                            ViewModelProvider(activityContext)[SettingsVM::class.java],
                            id,
                            ViewModelProvider(activityContext)[ArtistScreenVM::class.java],
                            activityContext,
                            navController,
                            onBackClicked = { navController.navigateUp() }
                        )
                    }
                }

                composable(route = "${Routes.Main.ARTIST_ALBUM}{id}") {

                    val id = it.arguments?.getString("id")?.toLongOrNull()

                    id?.let {

                        ArtistAlbumScreen(
                            mainVM = mainVM,
                            albumVM = ViewModelProvider(activityContext)[ArtistAlbumScreenVM::class.java],
                            albumID = id,
                            onBackClicked = { navController.navigateUp() },
                        )
                    }
                }

                composable(
                    route = "${Routes.Main.SELECT_ARTIST_COVER}name={name}&id={id}",
                ) {

                    val name = it.arguments?.getString("name")
                    val id = it.arguments?.getString("id")?.toLongOrNull()


                    if (name != null && id != null) {
                        SelectArtistCoverScreen(
                            mainVM = mainVM,
                            selectArtistCoverVM = ViewModelProvider(activityContext)[SelectArtistCoverScreenVM::class.java],
                            artistVM = ViewModelProvider(activityContext)[ArtistScreenVM::class.java],
                            artistName = name,
                            artistID = id,
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
                    route = "${Routes.Main.ALBUM}{albumID}",
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
                composable("${Routes.Main.GENRE_PLAYLIST}{genre}") {
                    var genre = it.arguments?.getString("genre")

                    if (genre != null) {

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

                composable("${Routes.Main.PLAYLIST}{playlistID}") {

                    val playlistID = it.arguments?.getString("playlistID")

                    playlistID?.let {

                        PlaylistScreen(
                            mainVM = mainVM,
                            playlistsVM = ViewModelProvider(activityContext)[PlaylistsScreenVM::class.java],
                            vm = ViewModelProvider(activityContext)[PlaylistScreenVM::class.java],
                            activityContext,
                            navController,
                            rootNavController,
                            playlistID = playlistID,
                            onGetImage = { onGetPlaylistImage() }
                        )
                    }
                }
            }
        }
    )
}


fun getNavigationItems(context: Context): List<BottomNavItem> {

    val itemsList = ArrayList<BottomNavItem>()

    itemsList.add(
        BottomNavItem(
            name = "Home",
            route = Routes.Main.HOME,
            activeIcon = getImage(context, R.drawable.home_filled, ImageSizes.SMALL).asImageBitmap(),
            inactiveIcon = getImage(context, R.drawable.home, ImageSizes.SMALL).asImageBitmap()
        )
    )

    itemsList.add(
        BottomNavItem(
            name = "Artists",
            route = Routes.Main.ARTISTS,
            activeIcon = getImage(context, R.drawable.person_filled, ImageSizes.SMALL).asImageBitmap(),
            inactiveIcon = getImage(context, R.drawable.person, ImageSizes.SMALL).asImageBitmap()
        )
    )

    itemsList.add(
        BottomNavItem(
            name = "Albums",
            route = Routes.Main.ALBUMS,
            activeIcon = getImage(context, R.drawable.cd_filled, ImageSizes.SMALL).asImageBitmap(),
            inactiveIcon = getImage(context, R.drawable.cd, ImageSizes.SMALL).asImageBitmap()
        )
    )

    itemsList.add(
        BottomNavItem(
            name = "Playlists",
            route = Routes.Main.PLAYLISTS,
            activeIcon = getImage(context, R.drawable.playlist_filled, ImageSizes.SMALL).asImageBitmap(),
            inactiveIcon = getImage(context, R.drawable.playlist, ImageSizes.SMALL).asImageBitmap()
        )
    )

    return itemsList
}