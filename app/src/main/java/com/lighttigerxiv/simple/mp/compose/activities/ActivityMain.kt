package com.lighttigerxiv.simple.mp.compose.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.lighttigerxiv.simple.mp.compose.ActivityFirstSetup
import com.lighttigerxiv.simple.mp.compose.viewmodels.ActivityMainViewModel
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.composables.MiniPlayer
import com.lighttigerxiv.simple.mp.compose.composables.Player
import com.lighttigerxiv.simple.mp.compose.navigation.BottomNavItem
import com.lighttigerxiv.simple.mp.compose.navigation.BottomNavigationBar
import com.lighttigerxiv.simple.mp.compose.navigation.screens.*
import com.lighttigerxiv.simple.mp.compose.navigation.screens.main.*
import com.lighttigerxiv.simple.mp.compose.ui.theme.ComposeSimpleMPTheme
import com.lighttigerxiv.simple.mp.compose.viewmodels.ThemeViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var activityMainViewModel: ActivityMainViewModel


    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (!getSharedPreferences(packageName, MODE_PRIVATE).getBoolean("setupCompleted", false)) {

            startActivity(
                Intent(applicationContext, ActivityFirstSetup::class.java)
            )

            finish()
        }

        createNotificationChannel()
        activityMainViewModel = ViewModelProvider(this)[ActivityMainViewModel::class.java]
        val themeViewModel = ViewModelProvider(this)[ThemeViewModel::class.java]

        setContent {
            ComposeSimpleMPTheme {

                val context = LocalContext.current
                val scope = rememberCoroutineScope()
                val navController = rememberNavController()
                activityMainViewModel.navController = navController
                val scaffoldState = rememberScaffoldState()
                val bottomNavigationItems = remember { ArrayList<BottomNavItem>(getNavigationItems(context)) }
                val bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
                val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)
                val surfaceColor =
                    if (themeViewModel.currentThemeSetting.value == "Dark" && themeViewModel.darkModeSetting.value == "Oled") {
                        Color.Black
                    } else if (themeViewModel.currentThemeSetting.value == "Dark" && themeViewModel.darkModeSetting.value == "Oled" && isSystemInDarkTheme()) {
                        Color.Black
                    } else if (themeViewModel.currentThemeSetting.value == "Light" && themeViewModel.themeAccentSetting.value == "Blue") {
                        Color(0xFFFEFBFF)
                    } else if (themeViewModel.currentThemeSetting.value == "Light" && themeViewModel.themeAccentSetting.value == "Red") {
                        Color(0xFFFFFBFF)
                    } else if (themeViewModel.currentThemeSetting.value == "Light" && themeViewModel.themeAccentSetting.value == "Purple") {
                        Color(0xFFFFFBFF)
                    } else if (themeViewModel.currentThemeSetting.value == "Light" && themeViewModel.themeAccentSetting.value == "Yellow") {
                        Color(0xFFFFFBFF)
                    } else if (themeViewModel.currentThemeSetting.value == "Light" && themeViewModel.themeAccentSetting.value == "Orange") {
                        Color(0xFFFFFBFF)
                    } else if (themeViewModel.currentThemeSetting.value == "Light" && themeViewModel.themeAccentSetting.value == "Green") {
                        Color(0xFFFDFDF5)
                    } else if (themeViewModel.currentThemeSetting.value == "Light" && themeViewModel.themeAccentSetting.value == "Pink") {
                        Color(0xFFFFFBFF)
                    } else if (themeViewModel.darkModeSetting.value == "Oled" && themeViewModel.currentThemeSetting.value == "Light" && isSystemInDarkTheme()) {
                        Color(0xFFFFFBFF)
                    } else if (themeViewModel.darkModeSetting.value == "Oled" && isSystemInDarkTheme()) {
                        Color.Black
                    } else {
                        MaterialTheme.colorScheme.surface
                    }


                activityMainViewModel.surfaceColor.value = surfaceColor

                val miniPlayerHeight by activityMainViewModel.miniPlayerHeight.observeAsState()
                val selectedSong by activityMainViewModel.selectedSong.observeAsState()

                rememberSystemUiController().setStatusBarColor(surfaceColor)


                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(surfaceColor)
                ) {

                    Scaffold(
                        scaffoldState = scaffoldState,
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {

                            BottomNavigationBar(
                                activityMainViewModel = activityMainViewModel,
                                navController = navController,
                                items = bottomNavigationItems,
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
                    ) { mainScaffoldPadding ->

                        BottomSheetScaffold(
                            scaffoldState = bottomSheetScaffoldState,
                            sheetContent = {

                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                ) {

                                    if (
                                        selectedSong != null
                                        && bottomSheetState.isCollapsed
                                        && bottomSheetState.progress.fraction == 1.0f
                                        && bottomSheetState.targetValue == BottomSheetValue.Collapsed
                                    ) {

                                        MiniPlayer(activityMainViewModel)
                                    } else {

                                        Player(
                                            activityMainViewModel,
                                            bottomSheetState
                                        )
                                    }
                                }
                            },
                            sheetPeekHeight = miniPlayerHeight!!,
                            modifier = Modifier
                                .padding(mainScaffoldPadding)
                        ) { bottomSheetPadding ->

                            NavHost(
                                navController = navController,
                                startDestination = "homeScreen",
                                modifier = Modifier
                                    .background(surfaceColor)
                                    .padding(
                                        top = bottomSheetPadding.calculateTopPadding(),
                                        bottom = bottomSheetPadding.calculateBottomPadding(),
                                        end = bottomSheetPadding.calculateEndPadding(LayoutDirection.Rtl),
                                        start = bottomSheetPadding.calculateStartPadding(
                                            LayoutDirection.Ltr
                                        )
                                    )
                            ) {
                                composable("homeScreen") {
                                    HomeScreen(
                                        activityMainViewModel = activityMainViewModel
                                    )
                                }
                                composable("artistsScreen") {
                                    ArtistsScreen(
                                        activityMainViewModel = activityMainViewModel,
                                        onArtistClicked = { artistID -> navController.navigate("artistScreen?artistID=$artistID") }
                                    )
                                }
                                composable("albumsScreen") {
                                    AlbumsScreen(
                                        activityMainViewModel = activityMainViewModel,
                                        onAlbumClicked = { albumID -> navController.navigate("albumScreen?albumID=$albumID") }
                                    )
                                }
                                composable("playlistsScreen") {
                                    PlaylistsScreen(
                                        activityMainViewModel = activityMainViewModel,
                                        onGenrePlaylistClick = { navController.navigate("genrePlaylistScreen") },
                                        onPlaylistClick = { navController.navigate("PlaylistScreen") }
                                    )
                                }
                                composable(
                                    route = "artistScreen?artistID={artistID}",
                                    arguments = listOf(
                                        navArgument("artistID") { type = NavType.LongType }
                                    )
                                )
                                { backStackEntry ->
                                    ArtistScreen(
                                        activityMainViewModel = activityMainViewModel,
                                        backStackEntry = backStackEntry,
                                        onBackClicked = { navController.navigateUp() },
                                        onArtistAlbumOpened = { albumID -> navController.navigate("artistAlbumScreen?albumID=$albumID") }
                                    )
                                }
                                composable(
                                    route = "artistAlbumScreen?albumID={albumID}",
                                    arguments = listOf(
                                        navArgument("albumID") { type = NavType.LongType }
                                    )
                                ) { backStackEntry ->
                                    AlbumScreen(
                                        activityMainViewModel = activityMainViewModel,
                                        backStackEntry = backStackEntry,
                                        onBackClicked = { navController.navigateUp() },
                                    )
                                }
                                composable(
                                    route = "albumScreen?albumID={albumID}",
                                    arguments = listOf(
                                        navArgument("albumID") { type = NavType.LongType }
                                    )
                                ) { backStackEntry ->
                                    AlbumScreen(
                                        activityMainViewModel = activityMainViewModel,
                                        backStackEntry = backStackEntry,
                                        onBackClicked = { navController.navigateUp() }
                                    )
                                }
                                composable("genrePlaylistScreen") {
                                    GenrePlaylistScreen(
                                        activityMainViewModel = activityMainViewModel,
                                        onBackClicked = { navController.navigateUp() }
                                    )
                                }

                                composable("playlistScreen") {
                                    PlaylistScreen(
                                        activityMainViewModel = activityMainViewModel,
                                        onBackClicked = { navController.navigateUp() }
                                    )
                                }
                                composable(
                                    route = "addToPlaylistScreen?songID={songID}",
                                    arguments = listOf(
                                        navArgument("songID") { type = NavType.LongType }
                                    )
                                ) { backStackEntry ->
                                    AddToPlaylistScreen(
                                        activityMainViewModel = activityMainViewModel,
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
                                ){ backStackEntry ->
                                    ArtistScreen(
                                        activityMainViewModel = activityMainViewModel,
                                        backStackEntry = backStackEntry,
                                        onBackClicked = { navController.navigateUp() },
                                        onArtistAlbumOpened = { albumID -> navController.navigate("floatingArtistAlbumScreen?albumID=$albumID") }
                                    )
                                }

                                composable(
                                    route = "floatingArtistAlbumScreen?albumID={albumID}",
                                    arguments = listOf(
                                        navArgument("albumID") { type = NavType.LongType }
                                    )
                                ){ backStackEntry ->
                                    AlbumScreen(
                                        activityMainViewModel = activityMainViewModel,
                                        backStackEntry = backStackEntry,
                                        onBackClicked = {navController.navigateUp()}
                                    )
                                }

                                composable(
                                    route = "floatingAlbumScreen?albumID={albumID}",
                                    arguments = listOf(
                                        navArgument("albumID") { type = NavType.LongType }
                                    )
                                ){ backStackEntry ->
                                    AlbumScreen(
                                        activityMainViewModel = activityMainViewModel,
                                        backStackEntry = backStackEntry,
                                        onBackClicked = {navController.navigateUp()}
                                    )
                                }
                            }
                        }
                    }

                    activityMainViewModel.onSongSelected = {
                        activityMainViewModel.miniPlayerHeight.value = 60.dp
                    }

                    activityMainViewModel.onMediaPlayerStopped = {
                        scope.launch { bottomSheetState.collapse() }
                        activityMainViewModel.miniPlayerHeight.value = 0.dp
                    }
                }
            }
        }
    }

    private fun createNotificationChannel() {

        val channelName = getString(R.string.notificationChannelName)
        val channelDescription = getString(R.string.notificationChannelDescription)
        val channelImportance = NotificationManager.IMPORTANCE_LOW
        val mChannel = NotificationChannel("Playback", channelName, channelImportance)

        mChannel.description = channelDescription

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
    }
}


fun getNavigationItems(context: Context): ArrayList<BottomNavItem> {

    val itemsList = ArrayList<BottomNavItem>()

    itemsList.add(
        BottomNavItem(
            name = "Home",
            route = "homeScreen",
            activeIcon = BitmapFactory.decodeResource(context.resources, R.drawable.icon_home_solid).asImageBitmap(),
            inactiveIcon = BitmapFactory.decodeResource(context.resources, R.drawable.icon_home_regular).asImageBitmap()
        )
    )

    itemsList.add(
        BottomNavItem(
            name = "Artists",
            route = "artistsScreen",
            activeIcon = BitmapFactory.decodeResource(context.resources, R.drawable.icon_person_solid).asImageBitmap(),
            inactiveIcon = BitmapFactory.decodeResource(context.resources, R.drawable.icon_person_regular).asImageBitmap()
        )
    )

    itemsList.add(
        BottomNavItem(
            name = "Albums",
            route = "albumsScreen",
            activeIcon = BitmapFactory.decodeResource(context.resources, R.drawable.icon_cd_solid).asImageBitmap(),
            inactiveIcon = BitmapFactory.decodeResource(context.resources, R.drawable.icon_cd_regular).asImageBitmap()
        )
    )

    itemsList.add(
        BottomNavItem(
            name = "Playlists",
            route = "playlistsScreen",
            activeIcon = BitmapFactory.decodeResource(context.resources, R.drawable.icon_playlist_solid).asImageBitmap(),
            inactiveIcon = BitmapFactory.decodeResource(context.resources, R.drawable.icon_playlist_regular).asImageBitmap()
        )
    )

    return itemsList
}




