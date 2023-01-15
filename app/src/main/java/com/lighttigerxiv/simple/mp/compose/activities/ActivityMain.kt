package com.lighttigerxiv.simple.mp.compose.activities

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
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
import com.lighttigerxiv.simple.mp.compose.viewmodels.ActivityMainVM
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.composables.MiniPlayer
import com.lighttigerxiv.simple.mp.compose.composables.Player
import com.lighttigerxiv.simple.mp.compose.getAppString
import com.lighttigerxiv.simple.mp.compose.navigation.BottomNavItem
import com.lighttigerxiv.simple.mp.compose.navigation.BottomNavigationBar
import com.lighttigerxiv.simple.mp.compose.navigation.screens.*
import com.lighttigerxiv.simple.mp.compose.navigation.screens.main.*
import com.lighttigerxiv.simple.mp.compose.ui.theme.ComposeSimpleMPTheme
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
class MainActivity : ComponentActivity() {

    private lateinit var mainVM: ActivityMainVM


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
        mainVM = ViewModelProvider(this)[ActivityMainVM::class.java]


        setContent {
            ComposeSimpleMPTheme(
                useDarkTheme = isSystemInDarkTheme(),
                themeMode = mainVM.themeModeSetting.collectAsState().value!!,
                themeAccent = mainVM.themeAccentSetting.collectAsState().value!!,
                content = {

                    val themeMode = mainVM.themeModeSetting.collectAsState().value
                    val themeAccent = mainVM.themeAccentSetting.collectAsState().value
                    val darkMode = mainVM.darkModeSetting.collectAsState().value

                    val context = LocalContext.current
                    val scope = rememberCoroutineScope()
                    val navController = rememberNavController()
                    mainVM.navController = navController
                    val scaffoldState = rememberScaffoldState()
                    val bottomNavigationItems = remember { ArrayList<BottomNavItem>(getNavigationItems(context)) }
                    val bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
                    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)


                    val showNavigationBar = mainVM.showNavigationBar.collectAsState().value

                    val surfaceColor =
                        if (themeMode == "Dark" && darkMode == "Oled") {
                            Color.Black
                        } else if (themeMode == "Light" && themeAccent == "Blue") {
                            Color(0xFFFEFBFF)
                        } else if (themeMode == "Light" && themeAccent == "Red") {
                            Color(0xFFFFFBFF)
                        } else if (themeMode == "Light" && themeAccent == "Purple") {
                            Color(0xFFFFFBFF)
                        } else if (themeMode == "Light" && themeAccent == "Yellow") {
                            Color(0xFFFFFBFF)
                        } else if (themeMode == "Light" && themeAccent == "Orange") {
                            Color(0xFFFFFBFF)
                        } else if (themeMode == "Light" && themeAccent == "Green") {
                            Color(0xFFFDFDF5)
                        } else if (themeMode == "Light" && themeAccent == "Pink") {
                            Color(0xFFFFFBFF)
                        } else if (darkMode == "Oled" && themeMode == "Light" && isSystemInDarkTheme()) {
                            Color(0xFFFFFBFF)
                        } else if (darkMode == "Oled" && themeAccent!!.startsWith("Macchiato")) {
                            Color.Black
                        } else if (darkMode == "Oled" && themeAccent!!.startsWith("Frappe")) {
                            Color.Black
                        } else if (darkMode == "Oled" && themeAccent!!.startsWith("Mocha")) {
                            Color.Black
                        } else if (darkMode == "Oled" && isSystemInDarkTheme()) {
                            Color.Black
                        } else {
                            MaterialTheme.colorScheme.surface
                        }

                    val surfaceVariantColor = MaterialTheme.colorScheme.surfaceVariant

                    mainVM.setSurfaceColor(surfaceColor)

                    val miniPlayerHeight by mainVM.miniPlayerHeight.observeAsState()
                    val selectedSong by mainVM.selectedSong.observeAsState()

                    rememberSystemUiController().setStatusBarColor(surfaceColor)
                    rememberSystemUiController().setNavigationBarColor(surfaceVariantColor)


                    navController.addOnDestinationChangedListener { _, destination, _ ->

                        when {
                            destination.route == "About" -> mainVM.setShowNavigationBar(false)
                            destination.route == "Settings" -> mainVM.setShowNavigationBar(false)
                            destination.route!!.startsWith("floating") -> mainVM.setShowNavigationBar(false)
                            destination.route!!.startsWith("addToPlaylistScreen") -> mainVM.setShowNavigationBar(false)
                            destination.route!!.startsWith("SelectArtistCoverScreen") -> mainVM.setShowNavigationBar(false)
                            destination.route == "ThemesScreen" -> mainVM.setShowNavigationBar(false)
                            else -> {
                                if (!showNavigationBar) mainVM.setShowNavigationBar(true)
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(surfaceColor)
                    ) {

                        Scaffold(
                            scaffoldState = scaffoldState,
                            modifier = Modifier.fillMaxSize(),
                            bottomBar = {

                                AnimatedVisibility(
                                    visible = showNavigationBar,
                                    exit = shrinkVertically(),
                                    enter = expandVertically()
                                ) {

                                    BottomNavigationBar(
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
                            }
                        ) { mainScaffoldPadding ->

                            BottomSheetScaffold(
                                scaffoldState = bottomSheetScaffoldState,
                                sheetContent = {

                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(surfaceColor)
                                    ) {

                                        if (
                                            selectedSong != null
                                            && bottomSheetState.isCollapsed
                                            && bottomSheetState.progress.fraction == 1.0f
                                            && bottomSheetState.targetValue == BottomSheetValue.Collapsed
                                        ) {

                                            if (showNavigationBar)
                                                MiniPlayer(mainVM)
                                        } else {

                                            if (showNavigationBar || bottomSheetState.isExpanded) {
                                                Player(
                                                    activityMainVM = mainVM,
                                                    bottomSheetState = bottomSheetState,
                                                    onGoToPage = {
                                                        scope.launch {
                                                            bottomSheetState.collapse()
                                                        }
                                                        navController.navigate(it)
                                                    }
                                                )
                                            }
                                        }
                                    }
                                },
                                sheetPeekHeight = miniPlayerHeight!!,
                                modifier = Modifier
                                    .padding(mainScaffoldPadding)
                            ) { bottomSheetPadding ->

                                NavHost(
                                    navController = navController,
                                    startDestination = "HomeScreen",
                                    modifier = Modifier
                                        .background(surfaceColor)
                                        .padding(bottomSheetPadding)
                                ) {

                                    composable("HomeScreen") {
                                        HomeScreen(
                                            activityMainVM = mainVM,
                                            openPage = { page -> navController.navigate(page) }
                                        )
                                    }
                                    composable("artistsScreen") {
                                        ArtistsScreen(
                                            activityMainVM = mainVM,
                                            onArtistClicked = { artistID -> navController.navigate("artistScreen?artistID=$artistID") }
                                        )
                                    }
                                    composable("albumsScreen") {
                                        AlbumsScreen(
                                            activityMainVM = mainVM,
                                            onAlbumClicked = { albumID -> navController.navigate("albumScreen?albumID=$albumID") }
                                        )
                                    }
                                    composable("playlistsScreen") {
                                        PlaylistsScreen(
                                            mainVM = mainVM,
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
                                        route = "artistScreen?artistID={artistID}",
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
                                        route = "artistAlbumScreen?albumID={albumID}",
                                        arguments = listOf(
                                            navArgument("albumID") { type = NavType.LongType }
                                        )
                                    ) { backStackEntry ->
                                        AlbumScreen(
                                            activityMainVM = mainVM,
                                            backStackEntry = backStackEntry,
                                            onBackClicked = { navController.navigateUp() },
                                        )
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

                                                    startActivityForResult(intent, 2)
                                                }
                                            )
                                        }
                                    }

                                    composable(
                                        route = "albumScreen?albumID={albumID}",
                                        arguments = listOf(
                                            navArgument("albumID") { type = NavType.LongType }
                                        )
                                    ) { backStackEntry ->
                                        AlbumScreen(
                                            activityMainVM = mainVM,
                                            backStackEntry = backStackEntry,
                                            onBackClicked = { navController.navigateUp() }
                                        )
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
                                            activityMainVM = mainVM,
                                            arguments = it.arguments!!,
                                            onBackClick = { navController.navigateUp() },
                                            onGetImage = {
                                                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                                                    addCategory(Intent.CATEGORY_OPENABLE)
                                                    type = "image/*"
                                                    addFlags(FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                                                    addFlags(FLAG_GRANT_READ_URI_PERMISSION)
                                                }

                                                startActivityForResult(intent, 1)
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
                                            activityMainVM = mainVM,
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
                                        arguments = listOf(
                                            navArgument("albumID") { type = NavType.LongType }
                                        )
                                    ) { backStackEntry ->
                                        AlbumScreen(
                                            activityMainVM = mainVM,
                                            backStackEntry = backStackEntry,
                                            onBackClicked = { navController.navigateUp() }
                                        )
                                    }

                                    composable(
                                        route = "floatingAlbumScreen?albumID={albumID}",
                                        arguments = listOf(
                                            navArgument("albumID") { type = NavType.LongType }
                                        )
                                    ) { backStackEntry ->
                                        AlbumScreen(
                                            activityMainVM = mainVM,
                                            backStackEntry = backStackEntry,
                                            onBackClicked = { navController.navigateUp() }
                                        )
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

                                                    startActivityForResult(intent, 2)
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
                                            activityMainVM = mainVM,
                                            onBackClick = { navController.navigateUp() }
                                        )
                                    }
                                    composable("ThemesScreen") {

                                        ThemesScreen(
                                            activityMainVM = mainVM,
                                            onBackClick = { navController.navigateUp() }
                                        )
                                    }
                                }
                            }
                        }

                        mainVM.onSongSelected = {
                            mainVM.miniPlayerHeight.value = 60.dp
                        }

                        mainVM.onMediaPlayerStopped = {
                            scope.launch { bottomSheetState.collapse() }
                            mainVM.miniPlayerHeight.value = 0.dp
                        }
                    }
                }
            )
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

    @Deprecated("Deprecated in Java", ReplaceWith("super.onActivityResult(requestCode, resultCode, data)", "androidx.activity.ComponentActivity"))
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == Activity.RESULT_OK && requestCode == 1) {

            try {

                val uri = data!!.data!!
                contentResolver.takePersistableUriPermission(uri, FLAG_GRANT_READ_URI_PERMISSION)

                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                val bitmapBytes = baos.toByteArray()

                if (bitmapBytes.size > 1048576) {
                    Toast.makeText(applicationContext, getAppString(applicationContext, R.string.ImageTooBig), Toast.LENGTH_LONG).show()
                } else {
                    val bitmapString = Base64.encodeToString(bitmapBytes, Base64.DEFAULT)
                    mainVM.onPlaylistImageSelected(bitmapString)
                }


            } catch (_: Exception) {}
        }

        if (resultCode == Activity.RESULT_OK && requestCode == 2){
            try {
                val uri = data!!.data!!
                contentResolver.takePersistableUriPermission(uri, FLAG_GRANT_READ_URI_PERMISSION)

                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                val bitmapBytes = baos.toByteArray()

                if (bitmapBytes.size > 1048576) {
                    Toast.makeText(applicationContext, getAppString(applicationContext, R.string.ImageTooBig), Toast.LENGTH_LONG).show()
                } else {
                    val bitmapString = Base64.encodeToString(bitmapBytes, Base64.DEFAULT)
                    mainVM.onArtistImageSelected(bitmapString)
                }
            } catch (_: Exception) {}
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}


fun getNavigationItems(context: Context): ArrayList<BottomNavItem> {

    val itemsList = ArrayList<BottomNavItem>()

    itemsList.add(
        BottomNavItem(
            name = "Home",
            route = "HomeScreen",
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




