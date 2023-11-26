package com.lighttigerxiv.simple.mp.compose.frontend.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination


fun NavController.openNavbarRoute(route: String) {
    val controller = this

    this.navigate(route) {
        popUpTo(controller.graph.findStartDestination().id) {
            saveState = true
        }
        restoreState = true
        launchSingleTop = true
    }
}

fun NavController.goBack() {
    this.navigateUp()
}

fun NavController.goToPermissions() {
    this.navigate(Routes.Setup.PERMISSIONS)
}

fun NavController.goToLightTheme() {
    this.navigate(Routes.Setup.LIGHT_THEME)
}

fun NavController.goToDarkTheme() {
    this.navigate(Routes.Setup.DARK_THEME)
}

fun NavController.goToOtherSettings() {
    this.navigate(Routes.Setup.OTHER_SETTINGS)
}

fun NavController.goToSyncLibrary() {
    this.navigate(Routes.Setup.SYNC_LIBRARY) {
        popBackStack()
    }
}

fun NavController.goToHome() {
    this.openNavbarRoute(Routes.Main.Library.HOME)
}

fun NavController.goToArtists() {
    this.openNavbarRoute(Routes.Main.Library.ARTISTS_ROOT)
}

fun NavController.goToAlbums() {
    this.openNavbarRoute(Routes.Main.Library.ALBUMS_ROOT)
}

fun NavController.goToPlaylists() {
    this.openNavbarRoute(Routes.Main.Library.PLAYLISTS)
}

fun NavController.goToArtist(artistId: Long) {
    this.navigate("${Routes.Main.Library.ARTISTS_ARTIST}/$artistId")
}


fun NavController.goToArtistAlbum(albumId: Long) {
    this.navigate("${Routes.Main.Library.ARTISTS_ARTIST_ALBUM}/$albumId")
}

fun NavController.goToAlbum(albumId: Long) {
    this.navigate("${Routes.Main.Library.ALBUMS_ALBUM}/$albumId")
}

fun NavController.goToSelectArtistCover(artistId: Long){
    this.navigate("${Routes.Main.Library.ARTISTS_ARTIST_SELECT_COVER}/$artistId")
}

fun NavController.goToAbout(){
    this.navigate(Routes.Main.ABOUT)
}

fun NavController.goToSettings(){
    this.navigate(Routes.Main.SETTINGS)
}
