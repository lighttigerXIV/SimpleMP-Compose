package com.lighttigerxiv.simple.mp.compose.data.variables

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


val SCREEN_PADDING = 16.dp
val XSMALL_SPACING = 4.dp
val SMALL_SPACING = 8.dp
val MEDIUM_SPACING = 16.dp
val MEDIUM_TITLE_SIZE = 22.sp
val MEDIUM_RADIUS = 14.dp

object ROUTES{
    object ROOT{
        const val MAIN = "main"
        const val SETTINGS = "settings"
        const val ABOUT = "about"
        const val THEMES = "themes"
        const val FLOATING_ARTIST = "floating_artist/"
        const val FLOATING_ALBUM = "floating_album/"
        const val ADD_SONGS_TO_PLAYLIST = "add_songs_to_playlist/"
        const val ADD_SONG_TO_PLAYLIST = "add_song_to_playlist/"
    }
    object MAIN{
        const val HOME = "home"
        const val ARTISTS = "artists"
        const val ARTIST = "artist/"
        const val ARTIST_ALBUM = "artist_album/"
        const val SELECT_ARTIST_COVER = "select_artist_cover/"
        const val ALBUMS = "albums"
        const val ALBUM = "album/"
        const val PLAYLISTS = "playlists"
        const val GENRE_PLAYLIST = "genre_playlist/"
        const val PLAYLIST = "playlist/"
    }
}

object SORTS{
    const val RECENT = "recent"
    const val OLDEST = "oldest"
    const val ASCENDENT = "ascendent"
    const val DESCENDENT = "descendent"
}


