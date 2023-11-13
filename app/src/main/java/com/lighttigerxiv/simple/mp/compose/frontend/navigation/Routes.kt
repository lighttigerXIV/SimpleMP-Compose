package com.lighttigerxiv.simple.mp.compose.frontend.navigation

object Routes{
    object Setup{
        const val WELCOME = "welcome"
        const val PERMISSIONS = "permissions"
        const val LIGHT_THEME = "light_theme"
        const val DARK_THEME = "dark_theme"
        const val OTHER_SETTINGS = "other_settings"
        const val SYNC_LIBRARY = "sync_library"
    }

    object Main{
        const val SETTINGS = "settings"
        const val LIBRARY = "library"

        object Library{
            const val HOME = "home"
            const val ARTISTS = "artists"
            const val ALBUMS = "albums"
            const val PLAYLISTS = "playlists"
        }

    }
}