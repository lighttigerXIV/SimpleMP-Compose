package com.lighttigerxiv.simple.mp.compose.backend.settings

data class Settings(
    val setupCompleted: Boolean,
    val colorScheme: String,
    val useOledOnDarkTheme: Boolean,
    val lightTheme: String,
    val darkTheme: String,
    val durationFilter: Int,
    val downloadArtistCover: Boolean,
    val downloadArtistCoverWithData: Boolean,
    val homeSort: String,
    val albumsSort: String,
    val artistsSort: String,
    val keepScreenOnCarPlayer: Boolean
)

object SettingsOptions{
    object ColorScheme{
        const val SYSTEM = "system"
        const val LIGHT = "light"
        const val DARK = "dark"
    }

    object Themes{
        const val MATERIAL_YOU = "material_you"
        const val RED = "red"
        const val GREEN = "green"
        const val BLUE = "blue"
        const val PURPLE = "purple"
        const val PINK = "pink"
        const val YELLOW = "yellow"
        const val ORANGE = "orange"
        const val LATTE_ROSEWATER = "latte_rosewater"
        const val LATTE_FLAMINGO = "latte_flamingo"
        const val LATTE_PINK = "latte_pink"
        const val LATTE_MAUVE = "latte_mauve"
        const val LATTE_RED = "latte_red"
        const val LATTE_MAROON = "latte_maroon"
        const val LATTE_PEACH = "latte_peach"
        const val LATTE_YELLOW = "latte_yellow"
        const val LATTE_GREEN = "latte_green"
        const val LATTE_TEAL = "latte_teal"
        const val LATTE_SKY = "latte_sky"
        const val LATTE_SAPPHIRE = "latte_sapphire"
        const val LATTE_BLUE = "latte_blue"
        const val LATTE_LAVENDER = "latte_lavender"
        const val FRAPPE_ROSEWATER = "frappe_rosewater"
        const val FRAPPE_FLAMINGO = "frappe_flamingo"
        const val FRAPPE_PINK = "frappe_pink"
        const val FRAPPE_MAUVE = "frappe_mauve"
        const val FRAPPE_RED = "frappe_red"
        const val FRAPPE_MAROON = "frappe_maroon"
        const val FRAPPE_PEACH = "frappe_peach"
        const val FRAPPE_YELLOW = "frappe_yellow"
        const val FRAPPE_GREEN = "frappe_green"
        const val FRAPPE_TEAL = "frappe_teal"
        const val FRAPPE_SKY = "frappe_sky"
        const val FRAPPE_SAPPHIRE = "frappe_sapphire"
        const val FRAPPE_BLUE = "frappe_blue"
        const val FRAPPE_LAVENDER = "frappe_lavender"
        const val MACCHIATO_ROSEWATER = "macchiato_rosewater"
        const val MACCHIATO_FLAMINGO = "macchiato_flamingo"
        const val MACCHIATO_PINK = "macchiato_pink"
        const val MACCHIATO_MAUVE = "macchiato_mauve"
        const val MACCHIATO_RED = "macchiato_red"
        const val MACCHIATO_MAROON = "macchiato_maroon"
        const val MACCHIATO_PEACH = "macchiato_peach"
        const val MACCHIATO_YELLOW = "macchiato_yellow"
        const val MACCHIATO_GREEN = "macchiato_green"
        const val MACCHIATO_TEAL = "macchiato_teal"
        const val MACCHIATO_SKY = "macchiato_sky"
        const val MACCHIATO_SAPPHIRE = "macchiato_sapphire"
        const val MACCHIATO_BLUE = "macchiato_blue"
        const val MACCHIATO_LAVENDER = "macchiato_lavender"
        const val MOCHA_ROSEWATER = "mocha_rosewater"
        const val MOCHA_FLAMINGO = "mocha_flamingo"
        const val MOCHA_PINK = "mocha_pink"
        const val MOCHA_MAUVE = "mocha_mauve"
        const val MOCHA_RED = "mocha_red"
        const val MOCHA_MAROON = "mocha_maroon"
        const val MOCHA_PEACH = "mocha_peach"
        const val MOCHA_YELLOW = "mocha_yellow"
        const val MOCHA_GREEN = "mocha_green"
        const val MOCHA_TEAL = "mocha_teal"
        const val MOCHA_SKY = "mocha_sky"
        const val MOCHA_SAPPHIRE = "mocha_sapphire"
        const val MOCHA_BLUE = "mocha_blue"
        const val MOCHA_LAVENDER = "mocha_lavender"
    }

    object Sort{
        const val DEFAULT = "default"
        const val DEFAULT_REVERSED = "default_reversed"
        const val MODIFICATION_DATE_RECENT = "modification_date_recent"
        const val MODIFICATION_DATE_OLD = "modification_date_old"
        const val YEAR_RECENT = "year_recent"
        const val YEAR_OLD = "year_old"
        const val ALPHABETICALLY_ASCENDENT = "alphabetically_ascendent"
        const val ALPHABETICALLY_DESCENDENT = "alphabetically_descendent"
    }
}
