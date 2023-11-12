package com.lighttigerxiv.simple.mp.compose.backend.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(private val dataStore: DataStore<Preferences>) {

    object Keys {
        val SETUP_COMPLETED = booleanPreferencesKey("setup_completed")
        val COLOR_SCHEME = stringPreferencesKey("color_scheme")
        val USE_OLED_IN_DARK_THEME = booleanPreferencesKey("use_oled_in_dark_theme")
        val LIGHT_THEME = stringPreferencesKey("light_theme")
        val DARK_THEME = stringPreferencesKey("dark_theme")
        val DURATION_FILTER = intPreferencesKey("duration_filter")
        val DOWNLOAD_ARTIST_COVER = booleanPreferencesKey("download_artist_cover")
        val DOWNLOAD_ARTIST_COVER_WITH_DATA = booleanPreferencesKey("download_artist_cover_with_data")
        val HOME_SORT = stringPreferencesKey("home_sort")
        val ALBUMS_SORT = stringPreferencesKey("albums_sort")
        val ARTISTS_SORT = stringPreferencesKey("artists_sort")
    }

    val settingsFlow: Flow<Settings> = dataStore.data.map { settings ->
        Settings(
            setupCompleted = settings[Keys.SETUP_COMPLETED] ?: false,
            colorScheme = settings[Keys.COLOR_SCHEME] ?: SettingsOptions.ColorScheme.SYSTEM,
            useOledOnDarkTheme = settings[Keys.USE_OLED_IN_DARK_THEME] ?: false,
            lightTheme = settings[Keys.LIGHT_THEME] ?: SettingsOptions.Themes.LATTE_ROSEWATER,
            darkTheme = settings[Keys.DARK_THEME] ?: SettingsOptions.Themes.MACCHIATO_ROSEWATER,
            durationFilter = settings[Keys.DURATION_FILTER] ?: 60,
            downloadArtistCover = settings[Keys.DOWNLOAD_ARTIST_COVER] ?: false,
            downloadArtistCoverWithData = settings[Keys.DOWNLOAD_ARTIST_COVER_WITH_DATA] ?: false,
            homeSort = settings[Keys.HOME_SORT] ?: SettingsOptions.Sort.DEFAULT,
            albumsSort = settings[Keys.ALBUMS_SORT] ?: SettingsOptions.Sort.DEFAULT,
            artistsSort = settings[Keys.ARTISTS_SORT] ?: SettingsOptions.Sort.DEFAULT
        )
    }

    suspend fun updateSetupCompleted(v: Boolean) {
        dataStore.edit { it[Keys.SETUP_COMPLETED] = v }
    }

    suspend fun updateColorScheme(v: String) {
        dataStore.edit { it[Keys.COLOR_SCHEME] = v }
    }

    suspend fun updateUseOledInDarkTheme(v: Boolean) {
        dataStore.edit { it[Keys.USE_OLED_IN_DARK_THEME] = v }
    }

    suspend fun updateLightTheme(v: String) {
        dataStore.edit { it[Keys.LIGHT_THEME] = v }
    }

    suspend fun updateDarkTheme(v: String) {
        dataStore.edit { it[Keys.DARK_THEME] = v }
    }

    suspend fun updateDurationFilter(v: Int) {
        dataStore.edit { it[Keys.DURATION_FILTER] = v }
    }

    suspend fun updateDownloadArtistCover(v: Boolean) {
        dataStore.edit { it[Keys.DOWNLOAD_ARTIST_COVER] = v }
    }

    suspend fun updateDownloadArtistCoverWithData(v: Boolean) {
        dataStore.edit { it[Keys.DOWNLOAD_ARTIST_COVER_WITH_DATA] = v }
    }

    suspend fun updateHomeSort(v: String) {
        dataStore.edit { it[Keys.HOME_SORT] = v }
    }

    suspend fun updateAlbumsSort(v: String) {
        dataStore.edit { it[Keys.ALBUMS_SORT] = v }
    }

    suspend fun updateArtistsSort(v: String) {
        dataStore.edit { it[Keys.ARTISTS_SORT] = v }
    }
}