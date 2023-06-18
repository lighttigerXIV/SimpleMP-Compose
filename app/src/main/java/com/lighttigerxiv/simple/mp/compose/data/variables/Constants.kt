package com.lighttigerxiv.simple.mp.compose.data.variables

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


val SCREEN_PADDING = 16.dp
val XSMALL_SPACING = 4.dp
val SMALL_SPACING = 8.dp
val MEDIUM_SPACING = 16.dp
val MEDIUM_TITLE_SIZE = 22.sp
val MEDIUM_RADIUS = 14.dp

object Routes{

    object Setup{
        const val WELCOME = "welcome"
        const val PERMISSIONS = "permissions"
        const val OTHER = "other"
        const val THEMES = "themes"
    }

    object Root{
        const val MAIN = "main"
        const val SETTINGS = "settings"
        const val ABOUT = "about"
        const val THEMES = "themes"
        const val FLOATING_ARTIST = "floating_artist/"
        const val FLOATING_ALBUM = "floating_album/"
        const val ADD_SONGS_TO_PLAYLIST = "add_songs_to_playlist/"
        const val ADD_SONG_TO_PLAYLIST = "add_song_to_playlist/"
    }
    object Main{
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

object Settings{
    const val THEME_MODE = "ThemeMode"
    const val DARK_MODE = "DarkMode"
    const val FILTER_AUDIO = "FilterAudio"
    const val THEME_ACCENT = "ThemeAccent"
    const val DOWNLOAD_COVER = "DownloadArtistCover"
    const val DOWNLOAD_OVER_DATA = "DownloadOverData"
}

object SettingsValues{
    object ThemeMode{
        const val SYSTEM = "System"
        const val LIGHT = "Light"
        const val DARK = "Dark"
    }

    object DarkMode{
        const val COLOR = "Color"
        const val OLED = "Oled"
    }

    object Themes{
        const val DEFAULT = "Default"
        const val SYSTEM = "System"
        const val BLUE = "Blue"
        const val RED = "Red"
        const val PURPLE = "Purple"
        const val ORANGE = "Orange"
        const val YELLOW = "Yellow"
        const val GREEN = "Green"
        const val PINK = "Pink"
        const val FRAPPE_ROSEWATER = "FrappeRosewater"
        const val FRAPPE_FLAMINGO = "FrappeFlamingo"
        const val FRAPPE_PINK = "FrappePink"
        const val FRAPPE_MAUVE = "FrappeMauve"
        const val FRAPPE_RED = "FrappeRed"
        const val FRAPPE_MAROON = "FrappeMaroon"
        const val FRAPPE_PEACH = "FrappePeach"
        const val FRAPPE_YELLOW = "FrappeYellow"
        const val FRAPPE_GREEN = "FrappeGreen"
        const val FRAPPE_TEAL = "FrappeTeal"
        const val FRAPPE_SKY = "FrappeSky"
        const val FRAPPE_SAPPHIRE = "FrappeSapphire"
        const val FRAPPE_BLUE = "FrappeBlue"
        const val FRAPPE_LAVENDER = "FrappeLavender"
        const val MACCHIATO_ROSEWATER = "MacchiatoRosewater"
        const val MACCHIATO_FLAMINGO = "MacchiatoFlamingo"
        const val MACCHIATO_PINK = "MacchiatoPink"
        const val MACCHIATO_MAUVE = "MacchiatoMauve"
        const val MACCHIATO_RED = "MacchiatoRed"
        const val MACCHIATO_MAROON = "MacchiatoMaroon"
        const val MACCHIATO_PEACH = "MacchiatoPeach"
        const val MACCHIATO_YELLOW = "MacchiatoYellow"
        const val MACCHIATO_GREEN = "MacchiatoGreen"
        const val MACCHIATO_TEAL = "MacchiatoTeal"
        const val MACCHIATO_SKY = "MacchiatoSky"
        const val MACCHIATO_SAPPHIRE = "MacchiatoSapphire"
        const val MACCHIATO_BLUE = "MacchiatoBlue"
        const val MACCHIATO_LAVENDER = "MacchiatoLavender"
        const val MOCHA_ROSEWATER = "MochaRosewater"
        const val MOCHA_FLAMINGO = "MochaFlamingo"
        const val MOCHA_PINK = "MochaPink"
        const val MOCHA_MAUVE = "MochaMauve"
        const val MOCHA_RED = "MochaRed"
        const val MOCHA_MAROON = "MochaMaroon"
        const val MOCHA_PEACH = "MochaPeach"
        const val MOCHA_YELLOW = "MochaYellow"
        const val MOCHA_GREEN = "MochaGreen"
        const val MOCHA_TEAL = "MochaTeal"
        const val MOCHA_SKY = "MochaSky"
        const val MOCHA_SAPPHIRE = "MochaSapphire"
        const val MOCHA_BLUE = "MochaBlue"
        const val MOCHA_LAVENDER = "MochaLavender"
    }
}

object Sorts{
    const val RECENT = "recent"
    const val OLDEST = "oldest"
    const val ASCENDENT = "ascendent"
    const val DESCENDENT = "descendent"
}

object ImageSizes{
    const val SMALL = 100
    const val MEDIUM = 200
    const val LARGE = 300
}

