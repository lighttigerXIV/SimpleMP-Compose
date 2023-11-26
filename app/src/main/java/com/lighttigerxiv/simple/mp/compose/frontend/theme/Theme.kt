package com.lighttigerxiv.simple.mp.compose.frontend.theme

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.lighttigerxiv.simple.mp.compose.backend.settings.Settings
import com.lighttigerxiv.simple.mp.compose.backend.settings.SettingsOptions
import com.lighttigerxiv.simple.mp.compose.backend.utils.isAtLeastAndroid12
import com.lighttigerxiv.simple.mp.compose.frontend.utils.ChangeStatusBarColor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun SimpleMPTheme(
    settings: Settings?,
    content: @Composable () -> Unit
) {

    settings?.let {

        val context = LocalContext.current
        val colorScheme = it.colorScheme
        val darkTheme = it.darkTheme
        val lightTheme = it.lightTheme
        val supportsMaterialYou = isAtLeastAndroid12()

        val nightMode = when (colorScheme) {
            SettingsOptions.ColorScheme.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            SettingsOptions.ColorScheme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_NO
        }

        AppCompatDelegate.setDefaultNightMode(nightMode)

        val useDarkTheme = colorScheme == SettingsOptions.ColorScheme.SYSTEM && isSystemInDarkTheme() || colorScheme == SettingsOptions.ColorScheme.DARK
        val useLightTheme = colorScheme == SettingsOptions.ColorScheme.SYSTEM && !isSystemInDarkTheme() || colorScheme == SettingsOptions.ColorScheme.LIGHT


        val theme = when {
            useDarkTheme && darkTheme == SettingsOptions.Themes.BLUE -> DarkBlueColors
            useDarkTheme && darkTheme == SettingsOptions.Themes.RED -> DarkRedColors
            useDarkTheme && darkTheme == SettingsOptions.Themes.PURPLE -> DarkPurpleColors
            useDarkTheme && darkTheme == SettingsOptions.Themes.YELLOW -> DarkYellowColors
            useDarkTheme && darkTheme == SettingsOptions.Themes.ORANGE -> DarkOrangeColors
            useDarkTheme && darkTheme == SettingsOptions.Themes.GREEN -> DarkGreenColors
            useDarkTheme && darkTheme == SettingsOptions.Themes.PINK -> DarkPinkColors
            useDarkTheme && darkTheme == SettingsOptions.Themes.MOCHA_ROSEWATER -> getMochaScheme(AppTheme.CatppuccinMocha.ROSEWATER)
            useDarkTheme && darkTheme == SettingsOptions.Themes.MOCHA_FLAMINGO -> getMochaScheme(AppTheme.CatppuccinMocha.FLAMINGO)
            useDarkTheme && darkTheme == SettingsOptions.Themes.MOCHA_PINK -> getMochaScheme(AppTheme.CatppuccinMocha.PINK)
            useDarkTheme && darkTheme == SettingsOptions.Themes.MOCHA_MAUVE -> getMochaScheme(AppTheme.CatppuccinMocha.MAUVE)
            useDarkTheme && darkTheme == SettingsOptions.Themes.MOCHA_RED -> getMochaScheme(AppTheme.CatppuccinMocha.RED)
            useDarkTheme && darkTheme == SettingsOptions.Themes.MOCHA_MAROON -> getMochaScheme(AppTheme.CatppuccinMocha.MAROON)
            useDarkTheme && darkTheme == SettingsOptions.Themes.MOCHA_PEACH -> getMochaScheme(AppTheme.CatppuccinMocha.PEACH)
            useDarkTheme && darkTheme == SettingsOptions.Themes.MOCHA_YELLOW -> getMochaScheme(AppTheme.CatppuccinMocha.YELLOW)
            useDarkTheme && darkTheme == SettingsOptions.Themes.MOCHA_GREEN -> getMochaScheme(AppTheme.CatppuccinMocha.GREEN)
            useDarkTheme && darkTheme == SettingsOptions.Themes.MOCHA_TEAL -> getMochaScheme(AppTheme.CatppuccinMocha.TEAL)
            useDarkTheme && darkTheme == SettingsOptions.Themes.MOCHA_SKY -> getMochaScheme(AppTheme.CatppuccinMocha.SKY)
            useDarkTheme && darkTheme == SettingsOptions.Themes.MOCHA_SAPPHIRE -> getMochaScheme(AppTheme.CatppuccinMocha.SAPPHIRE)
            useDarkTheme && darkTheme == SettingsOptions.Themes.MOCHA_BLUE -> getMochaScheme(AppTheme.CatppuccinMocha.BLUE)
            useDarkTheme && darkTheme == SettingsOptions.Themes.MOCHA_LAVENDER -> getMochaScheme(AppTheme.CatppuccinMocha.LAVENDER)
            useDarkTheme && darkTheme == SettingsOptions.Themes.MACCHIATO_ROSEWATER -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.ROSEWATER)
            useDarkTheme && darkTheme == SettingsOptions.Themes.MACCHIATO_FLAMINGO -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.FLAMINGO)
            useDarkTheme && darkTheme == SettingsOptions.Themes.MACCHIATO_PINK -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.PINK)
            useDarkTheme && darkTheme == SettingsOptions.Themes.MACCHIATO_MAUVE -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.MAUVE)
            useDarkTheme && darkTheme == SettingsOptions.Themes.MACCHIATO_RED -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.RED)
            useDarkTheme && darkTheme == SettingsOptions.Themes.MACCHIATO_MAROON -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.MAROON)
            useDarkTheme && darkTheme == SettingsOptions.Themes.MACCHIATO_PEACH -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.PEACH)
            useDarkTheme && darkTheme == SettingsOptions.Themes.MACCHIATO_YELLOW -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.YELLOW)
            useDarkTheme && darkTheme == SettingsOptions.Themes.MACCHIATO_GREEN -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.GREEN)
            useDarkTheme && darkTheme == SettingsOptions.Themes.MACCHIATO_TEAL -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.TEAL)
            useDarkTheme && darkTheme == SettingsOptions.Themes.MACCHIATO_SKY -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.SKY)
            useDarkTheme && darkTheme == SettingsOptions.Themes.MACCHIATO_SAPPHIRE -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.SAPPHIRE)
            useDarkTheme && darkTheme == SettingsOptions.Themes.MACCHIATO_BLUE -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.BLUE)
            useDarkTheme && darkTheme == SettingsOptions.Themes.MACCHIATO_LAVENDER -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.LAVENDER)
            useDarkTheme && darkTheme == SettingsOptions.Themes.FRAPPE_ROSEWATER -> getFrappeScheme(AppTheme.CatppuccinFrappe.ROSEWATER)
            useDarkTheme && darkTheme == SettingsOptions.Themes.FRAPPE_FLAMINGO -> getFrappeScheme(AppTheme.CatppuccinFrappe.FLAMINGO)
            useDarkTheme && darkTheme == SettingsOptions.Themes.FRAPPE_PINK -> getFrappeScheme(AppTheme.CatppuccinFrappe.PINK)
            useDarkTheme && darkTheme == SettingsOptions.Themes.FRAPPE_MAUVE -> getFrappeScheme(AppTheme.CatppuccinFrappe.MAUVE)
            useDarkTheme && darkTheme == SettingsOptions.Themes.FRAPPE_RED -> getFrappeScheme(AppTheme.CatppuccinFrappe.RED)
            useDarkTheme && darkTheme == SettingsOptions.Themes.FRAPPE_MAROON -> getFrappeScheme(AppTheme.CatppuccinFrappe.MAROON)
            useDarkTheme && darkTheme == SettingsOptions.Themes.FRAPPE_PEACH -> getFrappeScheme(AppTheme.CatppuccinFrappe.PEACH)
            useDarkTheme && darkTheme == SettingsOptions.Themes.FRAPPE_YELLOW -> getFrappeScheme(AppTheme.CatppuccinFrappe.YELLOW)
            useDarkTheme && darkTheme == SettingsOptions.Themes.FRAPPE_GREEN -> getFrappeScheme(AppTheme.CatppuccinFrappe.GREEN)
            useDarkTheme && darkTheme == SettingsOptions.Themes.FRAPPE_TEAL -> getFrappeScheme(AppTheme.CatppuccinFrappe.TEAL)
            useDarkTheme && darkTheme == SettingsOptions.Themes.FRAPPE_SKY -> getFrappeScheme(AppTheme.CatppuccinFrappe.SKY)
            useDarkTheme && darkTheme == SettingsOptions.Themes.FRAPPE_SAPPHIRE -> getFrappeScheme(AppTheme.CatppuccinFrappe.SAPPHIRE)
            useDarkTheme && darkTheme == SettingsOptions.Themes.FRAPPE_BLUE -> getFrappeScheme(AppTheme.CatppuccinFrappe.BLUE)
            useDarkTheme && darkTheme == SettingsOptions.Themes.FRAPPE_LAVENDER -> getFrappeScheme(AppTheme.CatppuccinFrappe.LAVENDER)
            useDarkTheme && darkTheme == SettingsOptions.Themes.LATTE_ROSEWATER -> getLatteScheme(AppTheme.CatppuccinLatte.ROSEWATER)
            useDarkTheme && darkTheme == SettingsOptions.Themes.LATTE_FLAMINGO -> getLatteScheme(AppTheme.CatppuccinLatte.FLAMINGO)
            useDarkTheme && darkTheme == SettingsOptions.Themes.LATTE_PINK -> getLatteScheme(AppTheme.CatppuccinLatte.PINK)
            useDarkTheme && darkTheme == SettingsOptions.Themes.LATTE_MAUVE -> getLatteScheme(AppTheme.CatppuccinLatte.MAUVE)
            useDarkTheme && darkTheme == SettingsOptions.Themes.LATTE_RED -> getLatteScheme(AppTheme.CatppuccinLatte.RED)
            useDarkTheme && darkTheme == SettingsOptions.Themes.LATTE_MAROON -> getLatteScheme(AppTheme.CatppuccinLatte.MAROON)
            useDarkTheme && darkTheme == SettingsOptions.Themes.LATTE_PEACH -> getLatteScheme(AppTheme.CatppuccinLatte.PEACH)
            useDarkTheme && darkTheme == SettingsOptions.Themes.LATTE_YELLOW -> getLatteScheme(AppTheme.CatppuccinLatte.YELLOW)
            useDarkTheme && darkTheme == SettingsOptions.Themes.LATTE_GREEN -> getLatteScheme(AppTheme.CatppuccinLatte.GREEN)
            useDarkTheme && darkTheme == SettingsOptions.Themes.LATTE_TEAL -> getLatteScheme(AppTheme.CatppuccinLatte.TEAL)
            useDarkTheme && darkTheme == SettingsOptions.Themes.LATTE_SKY -> getLatteScheme(AppTheme.CatppuccinLatte.SKY)
            useDarkTheme && darkTheme == SettingsOptions.Themes.LATTE_SAPPHIRE -> getLatteScheme(AppTheme.CatppuccinLatte.SAPPHIRE)
            useDarkTheme && darkTheme == SettingsOptions.Themes.LATTE_BLUE -> getLatteScheme(AppTheme.CatppuccinLatte.BLUE)
            useDarkTheme && darkTheme == SettingsOptions.Themes.LATTE_LAVENDER -> getLatteScheme(AppTheme.CatppuccinLatte.LAVENDER)
            useDarkTheme && darkTheme == SettingsOptions.Themes.MATERIAL_YOU && supportsMaterialYou -> dynamicDarkColorScheme(context)

            useDarkTheme -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.ROSEWATER)

            useLightTheme && lightTheme == SettingsOptions.Themes.BLUE -> LightBlueColors
            useLightTheme && lightTheme == SettingsOptions.Themes.RED -> LightRedColors
            useLightTheme && lightTheme == SettingsOptions.Themes.PURPLE -> LightPurpleColors
            useLightTheme && lightTheme == SettingsOptions.Themes.YELLOW -> LightYellowColors
            useLightTheme && lightTheme == SettingsOptions.Themes.ORANGE -> LightOrangeColors
            useLightTheme && lightTheme == SettingsOptions.Themes.GREEN -> LightGreenColors
            useLightTheme && lightTheme == SettingsOptions.Themes.PINK -> LightPinkColors
            useLightTheme && lightTheme == SettingsOptions.Themes.MOCHA_ROSEWATER -> getMochaScheme(AppTheme.CatppuccinMocha.ROSEWATER)
            useLightTheme && lightTheme == SettingsOptions.Themes.MOCHA_FLAMINGO -> getMochaScheme(AppTheme.CatppuccinMocha.FLAMINGO)
            useLightTheme && lightTheme == SettingsOptions.Themes.MOCHA_PINK -> getMochaScheme(AppTheme.CatppuccinMocha.PINK)
            useLightTheme && lightTheme == SettingsOptions.Themes.MOCHA_MAUVE -> getMochaScheme(AppTheme.CatppuccinMocha.MAUVE)
            useLightTheme && lightTheme == SettingsOptions.Themes.MOCHA_RED -> getMochaScheme(AppTheme.CatppuccinMocha.RED)
            useLightTheme && lightTheme == SettingsOptions.Themes.MOCHA_MAROON -> getMochaScheme(AppTheme.CatppuccinMocha.MAROON)
            useLightTheme && lightTheme == SettingsOptions.Themes.MOCHA_PEACH -> getMochaScheme(AppTheme.CatppuccinMocha.PEACH)
            useLightTheme && lightTheme == SettingsOptions.Themes.MOCHA_YELLOW -> getMochaScheme(AppTheme.CatppuccinMocha.YELLOW)
            useLightTheme && lightTheme == SettingsOptions.Themes.MOCHA_GREEN -> getMochaScheme(AppTheme.CatppuccinMocha.GREEN)
            useLightTheme && lightTheme == SettingsOptions.Themes.MOCHA_TEAL -> getMochaScheme(AppTheme.CatppuccinMocha.TEAL)
            useLightTheme && lightTheme == SettingsOptions.Themes.MOCHA_SKY -> getMochaScheme(AppTheme.CatppuccinMocha.SKY)
            useLightTheme && lightTheme == SettingsOptions.Themes.MOCHA_SAPPHIRE -> getMochaScheme(AppTheme.CatppuccinMocha.SAPPHIRE)
            useLightTheme && lightTheme == SettingsOptions.Themes.MOCHA_BLUE -> getMochaScheme(AppTheme.CatppuccinMocha.BLUE)
            useLightTheme && lightTheme == SettingsOptions.Themes.MOCHA_LAVENDER -> getMochaScheme(AppTheme.CatppuccinMocha.LAVENDER)
            useLightTheme && lightTheme == SettingsOptions.Themes.MACCHIATO_ROSEWATER -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.ROSEWATER)
            useLightTheme && lightTheme == SettingsOptions.Themes.MACCHIATO_FLAMINGO -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.FLAMINGO)
            useLightTheme && lightTheme == SettingsOptions.Themes.MACCHIATO_PINK -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.PINK)
            useLightTheme && lightTheme == SettingsOptions.Themes.MACCHIATO_MAUVE -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.MAUVE)
            useLightTheme && lightTheme == SettingsOptions.Themes.MACCHIATO_RED -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.RED)
            useLightTheme && lightTheme == SettingsOptions.Themes.MACCHIATO_MAROON -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.MAROON)
            useLightTheme && lightTheme == SettingsOptions.Themes.MACCHIATO_PEACH -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.PEACH)
            useLightTheme && lightTheme == SettingsOptions.Themes.MACCHIATO_YELLOW -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.YELLOW)
            useLightTheme && lightTheme == SettingsOptions.Themes.MACCHIATO_GREEN -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.GREEN)
            useLightTheme && lightTheme == SettingsOptions.Themes.MACCHIATO_TEAL -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.TEAL)
            useLightTheme && lightTheme == SettingsOptions.Themes.MACCHIATO_SKY -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.SKY)
            useLightTheme && lightTheme == SettingsOptions.Themes.MACCHIATO_SAPPHIRE -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.SAPPHIRE)
            useLightTheme && lightTheme == SettingsOptions.Themes.MACCHIATO_BLUE -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.BLUE)
            useLightTheme && lightTheme == SettingsOptions.Themes.MACCHIATO_LAVENDER -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.LAVENDER)
            useLightTheme && lightTheme == SettingsOptions.Themes.FRAPPE_ROSEWATER -> getFrappeScheme(AppTheme.CatppuccinFrappe.ROSEWATER)
            useLightTheme && lightTheme == SettingsOptions.Themes.FRAPPE_FLAMINGO -> getFrappeScheme(AppTheme.CatppuccinFrappe.FLAMINGO)
            useLightTheme && lightTheme == SettingsOptions.Themes.FRAPPE_PINK -> getFrappeScheme(AppTheme.CatppuccinFrappe.PINK)
            useLightTheme && lightTheme == SettingsOptions.Themes.FRAPPE_MAUVE -> getFrappeScheme(AppTheme.CatppuccinFrappe.MAUVE)
            useLightTheme && lightTheme == SettingsOptions.Themes.FRAPPE_RED -> getFrappeScheme(AppTheme.CatppuccinFrappe.RED)
            useLightTheme && lightTheme == SettingsOptions.Themes.FRAPPE_MAROON -> getFrappeScheme(AppTheme.CatppuccinFrappe.MAROON)
            useLightTheme && lightTheme == SettingsOptions.Themes.FRAPPE_PEACH -> getFrappeScheme(AppTheme.CatppuccinFrappe.PEACH)
            useLightTheme && lightTheme == SettingsOptions.Themes.FRAPPE_YELLOW -> getFrappeScheme(AppTheme.CatppuccinFrappe.YELLOW)
            useLightTheme && lightTheme == SettingsOptions.Themes.FRAPPE_GREEN -> getFrappeScheme(AppTheme.CatppuccinFrappe.GREEN)
            useLightTheme && lightTheme == SettingsOptions.Themes.FRAPPE_TEAL -> getFrappeScheme(AppTheme.CatppuccinFrappe.TEAL)
            useLightTheme && lightTheme == SettingsOptions.Themes.FRAPPE_SKY -> getFrappeScheme(AppTheme.CatppuccinFrappe.SKY)
            useLightTheme && lightTheme == SettingsOptions.Themes.FRAPPE_SAPPHIRE -> getFrappeScheme(AppTheme.CatppuccinFrappe.SAPPHIRE)
            useLightTheme && lightTheme == SettingsOptions.Themes.FRAPPE_BLUE -> getFrappeScheme(AppTheme.CatppuccinFrappe.BLUE)
            useLightTheme && lightTheme == SettingsOptions.Themes.FRAPPE_LAVENDER -> getFrappeScheme(AppTheme.CatppuccinFrappe.LAVENDER)
            useLightTheme && lightTheme == SettingsOptions.Themes.LATTE_ROSEWATER -> getLatteScheme(AppTheme.CatppuccinLatte.ROSEWATER)
            useLightTheme && lightTheme == SettingsOptions.Themes.LATTE_FLAMINGO -> getLatteScheme(AppTheme.CatppuccinLatte.FLAMINGO)
            useLightTheme && lightTheme == SettingsOptions.Themes.LATTE_PINK -> getLatteScheme(AppTheme.CatppuccinLatte.PINK)
            useLightTheme && lightTheme == SettingsOptions.Themes.LATTE_MAUVE -> getLatteScheme(AppTheme.CatppuccinLatte.MAUVE)
            useLightTheme && lightTheme == SettingsOptions.Themes.LATTE_RED -> getLatteScheme(AppTheme.CatppuccinLatte.RED)
            useLightTheme && lightTheme == SettingsOptions.Themes.LATTE_MAROON -> getLatteScheme(AppTheme.CatppuccinLatte.MAROON)
            useLightTheme && lightTheme == SettingsOptions.Themes.LATTE_PEACH -> getLatteScheme(AppTheme.CatppuccinLatte.PEACH)
            useLightTheme && lightTheme == SettingsOptions.Themes.LATTE_YELLOW -> getLatteScheme(AppTheme.CatppuccinLatte.YELLOW)
            useLightTheme && lightTheme == SettingsOptions.Themes.LATTE_GREEN -> getLatteScheme(AppTheme.CatppuccinLatte.GREEN)
            useLightTheme && lightTheme == SettingsOptions.Themes.LATTE_TEAL -> getLatteScheme(AppTheme.CatppuccinLatte.TEAL)
            useLightTheme && lightTheme == SettingsOptions.Themes.LATTE_SKY -> getLatteScheme(AppTheme.CatppuccinLatte.SKY)
            useLightTheme && lightTheme == SettingsOptions.Themes.LATTE_SAPPHIRE -> getLatteScheme(AppTheme.CatppuccinLatte.SAPPHIRE)
            useLightTheme && lightTheme == SettingsOptions.Themes.LATTE_BLUE -> getLatteScheme(AppTheme.CatppuccinLatte.BLUE)
            useLightTheme && lightTheme == SettingsOptions.Themes.LATTE_LAVENDER -> getLatteScheme(AppTheme.CatppuccinLatte.LAVENDER)
            useLightTheme && lightTheme == SettingsOptions.Themes.MATERIAL_YOU && supportsMaterialYou -> dynamicLightColorScheme(context)

            else -> getLatteScheme(AppTheme.CatppuccinLatte.ROSEWATER)
        }

        val surfaceColor = if (useDarkTheme && it.useOledOnDarkTheme) Color(0xff000000) else theme.surface

        MaterialTheme(
            colorScheme = theme.copy(surface = surfaceColor),
            content = content
        )
    }
}

@Composable
fun PreviewTheme(
    themeId: String,
    darkTheme: Boolean,
    content: @Composable () -> Unit,
) {

    val context = LocalContext.current

    val theme = when {
        themeId == SettingsOptions.Themes.MATERIAL_YOU && darkTheme -> dynamicDarkColorScheme(context)
        themeId == SettingsOptions.Themes.MATERIAL_YOU && !darkTheme -> dynamicLightColorScheme(context)
        themeId == SettingsOptions.Themes.RED && darkTheme -> DarkRedColors
        themeId == SettingsOptions.Themes.RED && !darkTheme -> LightRedColors
        themeId == SettingsOptions.Themes.GREEN && darkTheme -> DarkGreenColors
        themeId == SettingsOptions.Themes.GREEN && !darkTheme -> LightGreenColors
        themeId == SettingsOptions.Themes.BLUE && darkTheme -> DarkBlueColors
        themeId == SettingsOptions.Themes.BLUE && !darkTheme -> LightBlueColors
        themeId == SettingsOptions.Themes.PURPLE && darkTheme -> DarkPurpleColors
        themeId == SettingsOptions.Themes.PURPLE && !darkTheme -> LightPurpleColors
        themeId == SettingsOptions.Themes.PINK && darkTheme -> DarkPinkColors
        themeId == SettingsOptions.Themes.PINK && !darkTheme -> LightPinkColors
        themeId == SettingsOptions.Themes.YELLOW && darkTheme -> DarkYellowColors
        themeId == SettingsOptions.Themes.YELLOW && !darkTheme -> LightYellowColors
        themeId == SettingsOptions.Themes.ORANGE && darkTheme -> DarkOrangeColors
        themeId == SettingsOptions.Themes.ORANGE && !darkTheme -> LightOrangeColors
        themeId == SettingsOptions.Themes.LATTE_ROSEWATER -> getLatteScheme(AppTheme.CatppuccinLatte.ROSEWATER)
        themeId == SettingsOptions.Themes.LATTE_FLAMINGO -> getLatteScheme(AppTheme.CatppuccinLatte.FLAMINGO)
        themeId == SettingsOptions.Themes.LATTE_PINK -> getLatteScheme(AppTheme.CatppuccinLatte.PINK)
        themeId == SettingsOptions.Themes.LATTE_MAUVE -> getLatteScheme(AppTheme.CatppuccinLatte.MAUVE)
        themeId == SettingsOptions.Themes.LATTE_RED -> getLatteScheme(AppTheme.CatppuccinLatte.RED)
        themeId == SettingsOptions.Themes.LATTE_MAROON -> getLatteScheme(AppTheme.CatppuccinLatte.MAROON)
        themeId == SettingsOptions.Themes.LATTE_PEACH -> getLatteScheme(AppTheme.CatppuccinLatte.PEACH)
        themeId == SettingsOptions.Themes.LATTE_YELLOW -> getLatteScheme(AppTheme.CatppuccinLatte.YELLOW)
        themeId == SettingsOptions.Themes.LATTE_GREEN -> getLatteScheme(AppTheme.CatppuccinLatte.GREEN)
        themeId == SettingsOptions.Themes.LATTE_TEAL -> getLatteScheme(AppTheme.CatppuccinLatte.TEAL)
        themeId == SettingsOptions.Themes.LATTE_SKY -> getLatteScheme(AppTheme.CatppuccinLatte.SKY)
        themeId == SettingsOptions.Themes.LATTE_SAPPHIRE -> getLatteScheme(AppTheme.CatppuccinLatte.SAPPHIRE)
        themeId == SettingsOptions.Themes.LATTE_BLUE -> getLatteScheme(AppTheme.CatppuccinLatte.BLUE)
        themeId == SettingsOptions.Themes.LATTE_LAVENDER -> getLatteScheme(AppTheme.CatppuccinLatte.LAVENDER)
        themeId == SettingsOptions.Themes.FRAPPE_ROSEWATER -> getFrappeScheme(AppTheme.CatppuccinFrappe.ROSEWATER)
        themeId == SettingsOptions.Themes.FRAPPE_FLAMINGO -> getFrappeScheme(AppTheme.CatppuccinFrappe.FLAMINGO)
        themeId == SettingsOptions.Themes.FRAPPE_PINK -> getFrappeScheme(AppTheme.CatppuccinFrappe.PINK)
        themeId == SettingsOptions.Themes.FRAPPE_MAUVE -> getFrappeScheme(AppTheme.CatppuccinFrappe.MAUVE)
        themeId == SettingsOptions.Themes.FRAPPE_RED -> getFrappeScheme(AppTheme.CatppuccinFrappe.RED)
        themeId == SettingsOptions.Themes.FRAPPE_MAROON -> getFrappeScheme(AppTheme.CatppuccinFrappe.MAROON)
        themeId == SettingsOptions.Themes.FRAPPE_PEACH -> getFrappeScheme(AppTheme.CatppuccinFrappe.PEACH)
        themeId == SettingsOptions.Themes.FRAPPE_YELLOW -> getFrappeScheme(AppTheme.CatppuccinFrappe.YELLOW)
        themeId == SettingsOptions.Themes.FRAPPE_GREEN -> getFrappeScheme(AppTheme.CatppuccinFrappe.GREEN)
        themeId == SettingsOptions.Themes.FRAPPE_TEAL -> getFrappeScheme(AppTheme.CatppuccinFrappe.TEAL)
        themeId == SettingsOptions.Themes.FRAPPE_SKY -> getFrappeScheme(AppTheme.CatppuccinFrappe.SKY)
        themeId == SettingsOptions.Themes.FRAPPE_SAPPHIRE -> getFrappeScheme(AppTheme.CatppuccinFrappe.SAPPHIRE)
        themeId == SettingsOptions.Themes.FRAPPE_BLUE -> getFrappeScheme(AppTheme.CatppuccinFrappe.BLUE)
        themeId == SettingsOptions.Themes.FRAPPE_LAVENDER -> getFrappeScheme(AppTheme.CatppuccinFrappe.LAVENDER)
        themeId == SettingsOptions.Themes.MACCHIATO_ROSEWATER -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.ROSEWATER)
        themeId == SettingsOptions.Themes.MACCHIATO_FLAMINGO -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.FLAMINGO)
        themeId == SettingsOptions.Themes.MACCHIATO_PINK -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.PINK)
        themeId == SettingsOptions.Themes.MACCHIATO_MAUVE -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.MAUVE)
        themeId == SettingsOptions.Themes.MACCHIATO_RED -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.RED)
        themeId == SettingsOptions.Themes.MACCHIATO_MAROON -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.MAROON)
        themeId == SettingsOptions.Themes.MACCHIATO_PEACH -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.PEACH)
        themeId == SettingsOptions.Themes.MACCHIATO_YELLOW -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.YELLOW)
        themeId == SettingsOptions.Themes.MACCHIATO_GREEN -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.GREEN)
        themeId == SettingsOptions.Themes.MACCHIATO_TEAL -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.TEAL)
        themeId == SettingsOptions.Themes.MACCHIATO_SKY -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.SKY)
        themeId == SettingsOptions.Themes.MACCHIATO_SAPPHIRE -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.SAPPHIRE)
        themeId == SettingsOptions.Themes.MACCHIATO_BLUE -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.BLUE)
        themeId == SettingsOptions.Themes.MACCHIATO_LAVENDER -> getMacchiatoScheme(AppTheme.CatppuccinMacchiato.LAVENDER)
        themeId == SettingsOptions.Themes.MOCHA_ROSEWATER -> getMochaScheme(AppTheme.CatppuccinMocha.ROSEWATER)
        themeId == SettingsOptions.Themes.MOCHA_FLAMINGO -> getMochaScheme(AppTheme.CatppuccinMocha.FLAMINGO)
        themeId == SettingsOptions.Themes.MOCHA_PINK -> getMochaScheme(AppTheme.CatppuccinMocha.PINK)
        themeId == SettingsOptions.Themes.MOCHA_MAUVE -> getMochaScheme(AppTheme.CatppuccinMocha.MAUVE)
        themeId == SettingsOptions.Themes.MOCHA_RED -> getMochaScheme(AppTheme.CatppuccinMocha.RED)
        themeId == SettingsOptions.Themes.MOCHA_MAROON -> getMochaScheme(AppTheme.CatppuccinMocha.MAROON)
        themeId == SettingsOptions.Themes.MOCHA_PEACH -> getMochaScheme(AppTheme.CatppuccinMocha.PEACH)
        themeId == SettingsOptions.Themes.MOCHA_YELLOW -> getMochaScheme(AppTheme.CatppuccinMocha.YELLOW)
        themeId == SettingsOptions.Themes.MOCHA_GREEN -> getMochaScheme(AppTheme.CatppuccinMocha.GREEN)
        themeId == SettingsOptions.Themes.MOCHA_TEAL -> getMochaScheme(AppTheme.CatppuccinMocha.TEAL)
        themeId == SettingsOptions.Themes.MOCHA_SKY -> getMochaScheme(AppTheme.CatppuccinMocha.SKY)
        themeId == SettingsOptions.Themes.MOCHA_SAPPHIRE -> getMochaScheme(AppTheme.CatppuccinMocha.SAPPHIRE)
        themeId == SettingsOptions.Themes.MOCHA_BLUE -> getMochaScheme(AppTheme.CatppuccinMocha.BLUE)
        themeId == SettingsOptions.Themes.MOCHA_LAVENDER -> getMochaScheme(AppTheme.CatppuccinMocha.LAVENDER)
        else -> getLatteScheme(AppTheme.CatppuccinLatte.ROSEWATER)
    }

    MaterialTheme(
        colorScheme = theme,
        content = content
    )
}