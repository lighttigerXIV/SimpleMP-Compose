package com.lighttigerxiv.simple.mp.compose.data.variables

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


val SCREEN_PADDING = 16.dp
val XSMALL_SPACING = 4.dp
val SMALL_SPACING = 8.dp
val MEDIUM_SPACING = 16.dp
val MEDIUM_TITLE_SIZE = 22.sp
val SMALL_TITLE_SIZE = 20.sp
val MEDIUM_RADIUS = 14.dp

object Routes{
    object Setup{
        const val WELCOME = "Welcome"
        const val PERMISSIONS = "Permissions"
        const val OTHER = "Other"
        const val LIGHT_COLOR_SCHEMES = "LightColorSchemes"
        const val DARK_COLOR_SCHEMES = "DarkColorSchemes"
    }

    object Root{
        const val MAIN = "Main"
        const val SETTINGS = "Settings"
        const val ABOUT = "About"
        const val DARK_COLOR_SCHEMES = "DarkColorSchemes"
        const val LIGHT_COLOR_SCHEMES = "LightColorSchemes"
        const val FLOATING_ARTIST = "FloatingArtist/"
        const val FLOATING_ALBUM = "FloatingAlbum/"
        const val ADD_SONGS_TO_PLAYLIST = "AddSongsToPlaylist/"
        const val ADD_SONG_TO_PLAYLIST = "AddSongToPlaylist/"
    }

    object Main{
        const val HOME = "Home"
        const val ARTISTS = "Artists"
        const val ARTIST = "Artist/"
        const val ARTIST_ALBUM = "ArtistAlbum/"
        const val SELECT_ARTIST_COVER = "SelectArtistCover/"
        const val ALBUMS = "Albums"
        const val ALBUM = "Album/"
        const val PLAYLISTS = "Playlists"
        const val GENRE_PLAYLIST = "GenrePlaylist/"
        const val PLAYLIST = "Playlist/"
    }
}

object Settings{
    const val COLOR_SCHEME = "ColorScheme"
    const val DARK_MODE_TYPE = "DarkModeType"
    const val FILTER_AUDIO = "FilterAudio"
    const val DARK_COLOR_SCHEME = "DarkColorScheme"
    const val LIGHT_COLOR_SCHEME = "LightColorScheme"
    const val DOWNLOAD_COVER = "DownloadArtistCover"
    const val DOWNLOAD_OVER_DATA = "DownloadOverData"
    const val HOME_SORT = "HomeSort"
    const val ARTISTS_SORT = "ArtistsSort"
    const val ALBUMS_SORT = "AlbumsSort"

    object Values{
        object ColorScheme{
            const val SYSTEM = "System"
            const val LIGHT = "Light"
            const val DARK = "Dark"
        }

        object DarkMode{
            const val COLOR = "Color"
            const val OLED = "Oled"
        }

        object ColorSchemes{
            const val MATERIAL_YOU = "MaterialYou"
            const val BLUE = "Blue"
            const val RED = "Red"
            const val PURPLE = "Purple"
            const val ORANGE = "Orange"
            const val YELLOW = "Yellow"
            const val GREEN = "Green"
            const val PINK = "Pink"
            const val LATTE_ROSEWATER = "LatteRosewater"
            const val LATTE_FLAMINGO = "LatteFlamingo"
            const val LATTE_PINK = "LattePink"
            const val LATTE_MAUVE = "LatteMauve"
            const val LATTE_RED = "LatteRed"
            const val LATTE_MAROON = "LatteMaroon"
            const val LATTE_PEACH = "LattePeach"
            const val LATTE_YELLOW = "LatteYellow"
            const val LATTE_GREEN = "LatteGreen"
            const val LATTE_TEAL = "LatteTeal"
            const val LATTE_SKY = "LatteSky"
            const val LATTE_SAPPHIRE = "LatteSapphire"
            const val LATTE_BLUE = "LatteBlue"
            const val LATTE_LAVENDER = "LatteLavender"
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

        object Sort{
            const val RECENT = "recent"
            const val OLDEST = "oldest"
            const val ASCENDENT = "ascendent"
            const val DESCENDENT = "descendent"
        }
    }
}

object ImageSizes{
    const val SMALL = 100
    const val MEDIUM = 200
    const val LARGE = 300
}

