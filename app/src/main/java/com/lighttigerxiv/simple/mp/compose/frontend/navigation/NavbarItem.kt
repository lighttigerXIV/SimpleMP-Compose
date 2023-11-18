package com.lighttigerxiv.simple.mp.compose.frontend.navigation

import com.lighttigerxiv.simple.mp.compose.R

data class NavbarItem(
    val route: String,
    val activeIconId: Int,
    val inactiveIconId: Int
)

fun getNavbarItems(): List<NavbarItem>{
    return listOf(
        NavbarItem(
            route = Routes.Main.Library.HOME,
            activeIconId = R.drawable.home_filled,
            inactiveIconId = R.drawable.home
        ),
        NavbarItem(
            route = Routes.Main.Library.ARTISTS,
            activeIconId = R.drawable.person_filled,
            inactiveIconId = R.drawable.person
        ),
        NavbarItem(
            route = Routes.Main.Library.ALBUMS,
            activeIconId = R.drawable.album_filled,
            inactiveIconId = R.drawable.album
        ),
        NavbarItem(
            route = Routes.Main.Library.PLAYLISTS,
            activeIconId = R.drawable.playlist_filled,
            inactiveIconId = R.drawable.playlist
        )
    )
}