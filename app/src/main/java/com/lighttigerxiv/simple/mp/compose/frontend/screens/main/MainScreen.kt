package com.lighttigerxiv.simple.mp.compose.frontend.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.Routes
import com.lighttigerxiv.simple.mp.compose.frontend.screens.main.about.AboutScreen
import com.lighttigerxiv.simple.mp.compose.frontend.screens.main.add_song_to_playlist.AddSongToPlaylistScreen
import com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.LibraryScreen
import com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.albums.album.AlbumScreen
import com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.playlists.playlist.add_songs.AddSongsToPlaylistScreen
import com.lighttigerxiv.simple.mp.compose.frontend.screens.main.preview_artist.PreviewArtistScreen
import com.lighttigerxiv.simple.mp.compose.frontend.screens.main.settings.SettingsScreen
import com.lighttigerxiv.simple.mp.compose.frontend.utils.Sizes
import org.mongodb.kbson.ObjectId

@ExperimentalMaterial3Api
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    NavHost(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        navController = navController,
        startDestination = Routes.Main.LIBRARY
    ) {

        composable(Routes.Main.LIBRARY) {
            LibraryScreen(navController)
        }

        composable(Routes.Main.ABOUT) {
            AboutScreen(rootController = navController)
        }

        composable(Routes.Main.SETTINGS) {
            SettingsScreen(rootController = navController)
        }

        composable(
            "${Routes.Main.PREVIEW_ARTIST}/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType }
            )
        ) { backstackEntry ->
            val artistId = backstackEntry.arguments?.getLong("id") ?: 0L

            Column(modifier = Modifier.padding(Sizes.LARGE)) {
                PreviewArtistScreen(artistId = artistId, rootController = navController)
            }
        }

        composable(
            "${Routes.Main.PREVIEW_ALBUM}/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType }
            )
        ) { backstackEntry ->
            val albumId = backstackEntry.arguments?.getLong("id") ?: 0L

            Column(modifier = Modifier.padding(Sizes.LARGE)) {
                AlbumScreen(
                    albumId = albumId,
                    navController = navController,
                    showMiniPlayer = false
                )
            }
        }

        composable(
            "${Routes.Main.Library.ADD_SONGS_TO_PLAYLIST}/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.StringType }
            )
        ) {backstackEntry->

            val playlistId = ObjectId(backstackEntry.arguments?.getString("id") ?: "")

            AddSongsToPlaylistScreen(playlistId = playlistId, navController = navController)
        }

        composable(
            "${Routes.Main.ADD_SONG_TO_PLAYLIST}/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType }
            )
        ) {backstackEntry->

            val songId = backstackEntry.arguments?.getLong("id") ?: 0L

            AddSongToPlaylistScreen(
                songId = songId,
                rootController = navController
            )
        }
    }
}