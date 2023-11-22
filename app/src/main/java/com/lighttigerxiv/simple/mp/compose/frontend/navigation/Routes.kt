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
            const val ARTISTS_ROOT = "artists_root"
            const val ARTISTS = "artists"
            const val ARTISTS_ARTIST = "artists_artist"
            const val ARTISTS_ARTIST_ALBUM = "artists_artist_album"
            const val ALBUMS_ROOT = "albums_root"
            const val ALBUMS = "albums"
            const val ALBUMS_ALBUM = "album"
            const val PLAYLISTS_ROOT = "playlists_root"
            const val PLAYLISTS = "playlists"
            const val PLAYLISTS_GENRE = "playlists_genre"
            const val PLAYLISTS_USER = "playlists_user"
        }
    }
}