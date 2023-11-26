package com.lighttigerxiv.simple.mp.compose.backend.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.lighttigerxiv.simple.mp.compose.backend.settings.Settings
import com.lighttigerxiv.simple.mp.compose.backend.settings.SettingsOptions
import com.lighttigerxiv.simple.mp.compose.backend.utils.isAtLeastAndroid12
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class SettingsRepository(private val dataStore: DataStore<Preferences>) {

    private object Keys {
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

    val settingsFlow: Flow<Settings> = dataStore.data
        .catch {
            getDefaultSettings()
        }
        .map {preferences ->
            getSettings(preferences)
        }

    private fun getSettings(preferences: Preferences): Settings {
        return Settings(
            setupCompleted = preferences[Keys.SETUP_COMPLETED] ?: false,
            colorScheme = preferences[Keys.COLOR_SCHEME] ?: SettingsOptions.ColorScheme.SYSTEM,
            useOledOnDarkTheme = preferences[Keys.USE_OLED_IN_DARK_THEME] ?: false,
            lightTheme = preferences[Keys.LIGHT_THEME] ?: if (isAtLeastAndroid12()) SettingsOptions.Themes.MATERIAL_YOU else SettingsOptions.Themes.BLUE,
            darkTheme = preferences[Keys.DARK_THEME] ?: if (isAtLeastAndroid12()) SettingsOptions.Themes.MATERIAL_YOU else SettingsOptions.Themes.BLUE,
            durationFilter = preferences[Keys.DURATION_FILTER] ?: 60,
            downloadArtistCover = preferences[Keys.DOWNLOAD_ARTIST_COVER] ?: false,
            downloadArtistCoverWithData = preferences[Keys.DOWNLOAD_ARTIST_COVER_WITH_DATA] ?: false,
            homeSort = preferences[Keys.HOME_SORT] ?: SettingsOptions.Sort.DEFAULT_REVERSED,
            albumsSort = preferences[Keys.ALBUMS_SORT] ?: SettingsOptions.Sort.DEFAULT_REVERSED,
            artistsSort = preferences[Keys.ARTISTS_SORT] ?: SettingsOptions.Sort.DEFAULT_REVERSED
        )
    }

    private fun getDefaultSettings(): Settings {
        return Settings(
            setupCompleted = false,
            colorScheme = SettingsOptions.ColorScheme.SYSTEM,
            useOledOnDarkTheme = false,
            lightTheme = if (isAtLeastAndroid12()) SettingsOptions.Themes.MATERIAL_YOU else SettingsOptions.Themes.BLUE,
            darkTheme = if (isAtLeastAndroid12()) SettingsOptions.Themes.MATERIAL_YOU else SettingsOptions.Themes.BLUE,
            durationFilter = 60,
            downloadArtistCover = false,
            downloadArtistCoverWithData = false,
            homeSort = SettingsOptions.Sort.DEFAULT,
            albumsSort = SettingsOptions.Sort.DEFAULT,
            artistsSort = SettingsOptions.Sort.DEFAULT
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