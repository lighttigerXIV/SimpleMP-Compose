package com.lighttigerxiv.simple.mp.compose.ui.theme

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext


private val LightBlueColors = lightColorScheme(
    primary = blue_theme_light_primary,
    onPrimary = blue_theme_light_onPrimary,
    primaryContainer = blue_theme_light_primaryContainer,
    onPrimaryContainer = blue_theme_light_onPrimaryContainer,
    secondary = blue_theme_light_secondary,
    onSecondary = blue_theme_light_onSecondary,
    secondaryContainer = blue_theme_light_secondaryContainer,
    onSecondaryContainer = blue_theme_light_onSecondaryContainer,
    tertiary = blue_theme_light_tertiary,
    onTertiary = blue_theme_light_onTertiary,
    tertiaryContainer = blue_theme_light_tertiaryContainer,
    onTertiaryContainer = blue_theme_light_onTertiaryContainer,
    error = blue_theme_light_error,
    errorContainer = blue_theme_light_errorContainer,
    onError = blue_theme_light_onError,
    onErrorContainer = blue_theme_light_onErrorContainer,
    background = blue_theme_light_background,
    onBackground = blue_theme_light_onBackground,
    surface = blue_theme_light_surface,
    onSurface = blue_theme_light_onSurface,
    surfaceVariant = blue_theme_light_surfaceVariant,
    onSurfaceVariant = blue_theme_light_onSurfaceVariant,
    outline = blue_theme_light_outline,
    inverseOnSurface = blue_theme_light_inverseOnSurface,
    inverseSurface = blue_theme_light_inverseSurface,
    inversePrimary = blue_theme_light_inversePrimary,
    surfaceTint = blue_theme_light_surfaceTint,
)


private val DarkBlueColors = darkColorScheme(
    primary = blue_theme_dark_primary,
    onPrimary = blue_theme_dark_onPrimary,
    primaryContainer = blue_theme_dark_primaryContainer,
    onPrimaryContainer = blue_theme_dark_onPrimaryContainer,
    secondary = blue_theme_dark_secondary,
    onSecondary = blue_theme_dark_onSecondary,
    secondaryContainer = blue_theme_dark_secondaryContainer,
    onSecondaryContainer = blue_theme_dark_onSecondaryContainer,
    tertiary = blue_theme_dark_tertiary,
    onTertiary = blue_theme_dark_onTertiary,
    tertiaryContainer = blue_theme_dark_tertiaryContainer,
    onTertiaryContainer = blue_theme_dark_onTertiaryContainer,
    error = blue_theme_dark_error,
    errorContainer = blue_theme_dark_errorContainer,
    onError = blue_theme_dark_onError,
    onErrorContainer = blue_theme_dark_onErrorContainer,
    background = blue_theme_dark_background,
    onBackground = blue_theme_dark_onBackground,
    surface = blue_theme_dark_surface,
    onSurface = blue_theme_dark_onSurface,
    surfaceVariant = blue_theme_dark_surfaceVariant,
    onSurfaceVariant = blue_theme_dark_onSurfaceVariant,
    outline = blue_theme_dark_outline,
    inverseOnSurface = blue_theme_dark_inverseOnSurface,
    inverseSurface = blue_theme_dark_inverseSurface,
    inversePrimary = blue_theme_dark_inversePrimary,
    surfaceTint = blue_theme_dark_surfaceTint,
)


private val LightRedColors = lightColorScheme(
    primary = red_theme_light_primary,
    onPrimary = red_theme_light_onPrimary,
    primaryContainer = red_theme_light_primaryContainer,
    onPrimaryContainer = red_theme_light_onPrimaryContainer,
    secondary = red_theme_light_secondary,
    onSecondary = red_theme_light_onSecondary,
    secondaryContainer = red_theme_light_secondaryContainer,
    onSecondaryContainer = red_theme_light_onSecondaryContainer,
    tertiary = red_theme_light_tertiary,
    onTertiary = red_theme_light_onTertiary,
    tertiaryContainer = red_theme_light_tertiaryContainer,
    onTertiaryContainer = red_theme_light_onTertiaryContainer,
    error = red_theme_light_error,
    errorContainer = red_theme_light_errorContainer,
    onError = red_theme_light_onError,
    onErrorContainer = red_theme_light_onErrorContainer,
    background = red_theme_light_background,
    onBackground = red_theme_light_onBackground,
    surface = red_theme_light_surface,
    onSurface = red_theme_light_onSurface,
    surfaceVariant = red_theme_light_surfaceVariant,
    onSurfaceVariant = red_theme_light_onSurfaceVariant,
    outline = red_theme_light_outline,
    inverseOnSurface = red_theme_light_inverseOnSurface,
    inverseSurface = red_theme_light_inverseSurface,
    inversePrimary = red_theme_light_inversePrimary,
    surfaceTint = red_theme_light_surfaceTint,
)


private val DarkRedColors = darkColorScheme(
    primary = red_theme_dark_primary,
    onPrimary = red_theme_dark_onPrimary,
    primaryContainer = red_theme_dark_primaryContainer,
    onPrimaryContainer = red_theme_dark_onPrimaryContainer,
    secondary = red_theme_dark_secondary,
    onSecondary = red_theme_dark_onSecondary,
    secondaryContainer = red_theme_dark_secondaryContainer,
    onSecondaryContainer = red_theme_dark_onSecondaryContainer,
    tertiary = red_theme_dark_tertiary,
    onTertiary = red_theme_dark_onTertiary,
    tertiaryContainer = red_theme_dark_tertiaryContainer,
    onTertiaryContainer = red_theme_dark_onTertiaryContainer,
    error = red_theme_dark_error,
    errorContainer = red_theme_dark_errorContainer,
    onError = red_theme_dark_onError,
    onErrorContainer = red_theme_dark_onErrorContainer,
    background = red_theme_dark_background,
    onBackground = red_theme_dark_onBackground,
    surface = red_theme_dark_surface,
    onSurface = red_theme_dark_onSurface,
    surfaceVariant = red_theme_dark_surfaceVariant,
    onSurfaceVariant = red_theme_dark_onSurfaceVariant,
    outline = red_theme_dark_outline,
    inverseOnSurface = red_theme_dark_inverseOnSurface,
    inverseSurface = red_theme_dark_inverseSurface,
    inversePrimary = red_theme_dark_inversePrimary,
    surfaceTint = red_theme_dark_surfaceTint,
)

private val LightPurpleColors = lightColorScheme(
    primary = purple_theme_light_primary,
    onPrimary = purple_theme_light_onPrimary,
    primaryContainer = purple_theme_light_primaryContainer,
    onPrimaryContainer = purple_theme_light_onPrimaryContainer,
    secondary = purple_theme_light_secondary,
    onSecondary = purple_theme_light_onSecondary,
    secondaryContainer = purple_theme_light_secondaryContainer,
    onSecondaryContainer = purple_theme_light_onSecondaryContainer,
    tertiary = purple_theme_light_tertiary,
    onTertiary = purple_theme_light_onTertiary,
    tertiaryContainer = purple_theme_light_tertiaryContainer,
    onTertiaryContainer = purple_theme_light_onTertiaryContainer,
    error = purple_theme_light_error,
    errorContainer = purple_theme_light_errorContainer,
    onError = purple_theme_light_onError,
    onErrorContainer = purple_theme_light_onErrorContainer,
    background = purple_theme_light_background,
    onBackground = purple_theme_light_onBackground,
    surface = purple_theme_light_surface,
    onSurface = purple_theme_light_onSurface,
    surfaceVariant = purple_theme_light_surfaceVariant,
    onSurfaceVariant = purple_theme_light_onSurfaceVariant,
    outline = purple_theme_light_outline,
    inverseOnSurface = purple_theme_light_inverseOnSurface,
    inverseSurface = purple_theme_light_inverseSurface,
    inversePrimary = purple_theme_light_inversePrimary,
    surfaceTint = purple_theme_light_surfaceTint,
)


private val DarkPurpleColors = darkColorScheme(
    primary = purple_theme_dark_primary,
    onPrimary = purple_theme_dark_onPrimary,
    primaryContainer = purple_theme_dark_primaryContainer,
    onPrimaryContainer = purple_theme_dark_onPrimaryContainer,
    secondary = purple_theme_dark_secondary,
    onSecondary = purple_theme_dark_onSecondary,
    secondaryContainer = purple_theme_dark_secondaryContainer,
    onSecondaryContainer = purple_theme_dark_onSecondaryContainer,
    tertiary = purple_theme_dark_tertiary,
    onTertiary = purple_theme_dark_onTertiary,
    tertiaryContainer = purple_theme_dark_tertiaryContainer,
    onTertiaryContainer = purple_theme_dark_onTertiaryContainer,
    error = purple_theme_dark_error,
    errorContainer = purple_theme_dark_errorContainer,
    onError = purple_theme_dark_onError,
    onErrorContainer = purple_theme_dark_onErrorContainer,
    background = purple_theme_dark_background,
    onBackground = purple_theme_dark_onBackground,
    surface = purple_theme_dark_surface,
    onSurface = purple_theme_dark_onSurface,
    surfaceVariant = purple_theme_dark_surfaceVariant,
    onSurfaceVariant = purple_theme_dark_onSurfaceVariant,
    outline = purple_theme_dark_outline,
    inverseOnSurface = purple_theme_dark_inverseOnSurface,
    inverseSurface = purple_theme_dark_inverseSurface,
    inversePrimary = purple_theme_dark_inversePrimary,
    surfaceTint = purple_theme_dark_surfaceTint,
)

private val LightYellowColors = lightColorScheme(
    primary = yellow_theme_light_primary,
    onPrimary = yellow_theme_light_onPrimary,
    primaryContainer = yellow_theme_light_primaryContainer,
    onPrimaryContainer = yellow_theme_light_onPrimaryContainer,
    secondary = yellow_theme_light_secondary,
    onSecondary = yellow_theme_light_onSecondary,
    secondaryContainer = yellow_theme_light_secondaryContainer,
    onSecondaryContainer = yellow_theme_light_onSecondaryContainer,
    tertiary = yellow_theme_light_tertiary,
    onTertiary = yellow_theme_light_onTertiary,
    tertiaryContainer = yellow_theme_light_tertiaryContainer,
    onTertiaryContainer = yellow_theme_light_onTertiaryContainer,
    error = yellow_theme_light_error,
    errorContainer = yellow_theme_light_errorContainer,
    onError = yellow_theme_light_onError,
    onErrorContainer = yellow_theme_light_onErrorContainer,
    background = yellow_theme_light_background,
    onBackground = yellow_theme_light_onBackground,
    surface = yellow_theme_light_surface,
    onSurface = yellow_theme_light_onSurface,
    surfaceVariant = yellow_theme_light_surfaceVariant,
    onSurfaceVariant = yellow_theme_light_onSurfaceVariant,
    outline = yellow_theme_light_outline,
    inverseOnSurface = yellow_theme_light_inverseOnSurface,
    inverseSurface = yellow_theme_light_inverseSurface,
    inversePrimary = yellow_theme_light_inversePrimary,
    surfaceTint = yellow_theme_light_surfaceTint,
)


private val DarkYellowColors = darkColorScheme(
    primary = yellow_theme_dark_primary,
    onPrimary = yellow_theme_dark_onPrimary,
    primaryContainer = yellow_theme_dark_primaryContainer,
    onPrimaryContainer = yellow_theme_dark_onPrimaryContainer,
    secondary = yellow_theme_dark_secondary,
    onSecondary = yellow_theme_dark_onSecondary,
    secondaryContainer = yellow_theme_dark_secondaryContainer,
    onSecondaryContainer = yellow_theme_dark_onSecondaryContainer,
    tertiary = yellow_theme_dark_tertiary,
    onTertiary = yellow_theme_dark_onTertiary,
    tertiaryContainer = yellow_theme_dark_tertiaryContainer,
    onTertiaryContainer = yellow_theme_dark_onTertiaryContainer,
    error = yellow_theme_dark_error,
    errorContainer = yellow_theme_dark_errorContainer,
    onError = yellow_theme_dark_onError,
    onErrorContainer = yellow_theme_dark_onErrorContainer,
    background = yellow_theme_dark_background,
    onBackground = yellow_theme_dark_onBackground,
    surface = yellow_theme_dark_surface,
    onSurface = yellow_theme_dark_onSurface,
    surfaceVariant = yellow_theme_dark_surfaceVariant,
    onSurfaceVariant = yellow_theme_dark_onSurfaceVariant,
    outline = yellow_theme_dark_outline,
    inverseOnSurface = yellow_theme_dark_inverseOnSurface,
    inverseSurface = yellow_theme_dark_inverseSurface,
    inversePrimary = yellow_theme_dark_inversePrimary,
    surfaceTint = yellow_theme_dark_surfaceTint,
)


private val LightOrangeColors = lightColorScheme(
    primary = orange_theme_light_primary,
    onPrimary = orange_theme_light_onPrimary,
    primaryContainer = orange_theme_light_primaryContainer,
    onPrimaryContainer = orange_theme_light_onPrimaryContainer,
    secondary = orange_theme_light_secondary,
    onSecondary = orange_theme_light_onSecondary,
    secondaryContainer = orange_theme_light_secondaryContainer,
    onSecondaryContainer = orange_theme_light_onSecondaryContainer,
    tertiary = orange_theme_light_tertiary,
    onTertiary = orange_theme_light_onTertiary,
    tertiaryContainer = orange_theme_light_tertiaryContainer,
    onTertiaryContainer = orange_theme_light_onTertiaryContainer,
    error = orange_theme_light_error,
    errorContainer = orange_theme_light_errorContainer,
    onError = orange_theme_light_onError,
    onErrorContainer = orange_theme_light_onErrorContainer,
    background = orange_theme_light_background,
    onBackground = orange_theme_light_onBackground,
    surface = orange_theme_light_surface,
    onSurface = orange_theme_light_onSurface,
    surfaceVariant = orange_theme_light_surfaceVariant,
    onSurfaceVariant = orange_theme_light_onSurfaceVariant,
    outline = orange_theme_light_outline,
    inverseOnSurface = orange_theme_light_inverseOnSurface,
    inverseSurface = orange_theme_light_inverseSurface,
    inversePrimary = orange_theme_light_inversePrimary,
    surfaceTint = orange_theme_light_surfaceTint,
)


private val DarkOrangeColors = darkColorScheme(
    primary = orange_theme_dark_primary,
    onPrimary = orange_theme_dark_onPrimary,
    primaryContainer = orange_theme_dark_primaryContainer,
    onPrimaryContainer = orange_theme_dark_onPrimaryContainer,
    secondary = orange_theme_dark_secondary,
    onSecondary = orange_theme_dark_onSecondary,
    secondaryContainer = orange_theme_dark_secondaryContainer,
    onSecondaryContainer = orange_theme_dark_onSecondaryContainer,
    tertiary = orange_theme_dark_tertiary,
    onTertiary = orange_theme_dark_onTertiary,
    tertiaryContainer = orange_theme_dark_tertiaryContainer,
    onTertiaryContainer = orange_theme_dark_onTertiaryContainer,
    error = orange_theme_dark_error,
    errorContainer = orange_theme_dark_errorContainer,
    onError = orange_theme_dark_onError,
    onErrorContainer = orange_theme_dark_onErrorContainer,
    background = orange_theme_dark_background,
    onBackground = orange_theme_dark_onBackground,
    surface = orange_theme_dark_surface,
    onSurface = orange_theme_dark_onSurface,
    surfaceVariant = orange_theme_dark_surfaceVariant,
    onSurfaceVariant = orange_theme_dark_onSurfaceVariant,
    outline = orange_theme_dark_outline,
    inverseOnSurface = orange_theme_dark_inverseOnSurface,
    inverseSurface = orange_theme_dark_inverseSurface,
    inversePrimary = orange_theme_dark_inversePrimary,
    surfaceTint = orange_theme_dark_surfaceTint,
)

private val LightGreenColors = lightColorScheme(
    primary = green_theme_light_primary,
    onPrimary = green_theme_light_onPrimary,
    primaryContainer = green_theme_light_primaryContainer,
    onPrimaryContainer = green_theme_light_onPrimaryContainer,
    secondary = green_theme_light_secondary,
    onSecondary = green_theme_light_onSecondary,
    secondaryContainer = green_theme_light_secondaryContainer,
    onSecondaryContainer = green_theme_light_onSecondaryContainer,
    tertiary = green_theme_light_tertiary,
    onTertiary = green_theme_light_onTertiary,
    tertiaryContainer = green_theme_light_tertiaryContainer,
    onTertiaryContainer = green_theme_light_onTertiaryContainer,
    error = green_theme_light_error,
    errorContainer = green_theme_light_errorContainer,
    onError = green_theme_light_onError,
    onErrorContainer = green_theme_light_onErrorContainer,
    background = green_theme_light_background,
    onBackground = green_theme_light_onBackground,
    surface = green_theme_light_surface,
    onSurface = green_theme_light_onSurface,
    surfaceVariant = green_theme_light_surfaceVariant,
    onSurfaceVariant = green_theme_light_onSurfaceVariant,
    outline = green_theme_light_outline,
    inverseOnSurface = green_theme_light_inverseOnSurface,
    inverseSurface = green_theme_light_inverseSurface,
    inversePrimary = green_theme_light_inversePrimary,
    surfaceTint = green_theme_light_surfaceTint,
)


private val DarkGreenColors = darkColorScheme(
    primary = green_theme_dark_primary,
    onPrimary = green_theme_dark_onPrimary,
    primaryContainer = green_theme_dark_primaryContainer,
    onPrimaryContainer = green_theme_dark_onPrimaryContainer,
    secondary = green_theme_dark_secondary,
    onSecondary = green_theme_dark_onSecondary,
    secondaryContainer = green_theme_dark_secondaryContainer,
    onSecondaryContainer = green_theme_dark_onSecondaryContainer,
    tertiary = green_theme_dark_tertiary,
    onTertiary = green_theme_dark_onTertiary,
    tertiaryContainer = green_theme_dark_tertiaryContainer,
    onTertiaryContainer = green_theme_dark_onTertiaryContainer,
    error = green_theme_dark_error,
    errorContainer = green_theme_dark_errorContainer,
    onError = green_theme_dark_onError,
    onErrorContainer = green_theme_dark_onErrorContainer,
    background = green_theme_dark_background,
    onBackground = green_theme_dark_onBackground,
    surface = green_theme_dark_surface,
    onSurface = green_theme_dark_onSurface,
    surfaceVariant = green_theme_dark_surfaceVariant,
    onSurfaceVariant = green_theme_dark_onSurfaceVariant,
    outline = green_theme_dark_outline,
    inverseOnSurface = green_theme_dark_inverseOnSurface,
    inverseSurface = green_theme_dark_inverseSurface,
    inversePrimary = green_theme_dark_inversePrimary,
    surfaceTint = green_theme_dark_surfaceTint,
)

private val LightPinkColors = lightColorScheme(
    primary = pink_theme_light_primary,
    onPrimary = pink_theme_light_onPrimary,
    primaryContainer = pink_theme_light_primaryContainer,
    onPrimaryContainer = pink_theme_light_onPrimaryContainer,
    secondary = pink_theme_light_secondary,
    onSecondary = pink_theme_light_onSecondary,
    secondaryContainer = pink_theme_light_secondaryContainer,
    onSecondaryContainer = pink_theme_light_onSecondaryContainer,
    tertiary = pink_theme_light_tertiary,
    onTertiary = pink_theme_light_onTertiary,
    tertiaryContainer = pink_theme_light_tertiaryContainer,
    onTertiaryContainer = pink_theme_light_onTertiaryContainer,
    error = pink_theme_light_error,
    errorContainer = pink_theme_light_errorContainer,
    onError = pink_theme_light_onError,
    onErrorContainer = pink_theme_light_onErrorContainer,
    background = pink_theme_light_background,
    onBackground = pink_theme_light_onBackground,
    surface = pink_theme_light_surface,
    onSurface = pink_theme_light_onSurface,
    surfaceVariant = pink_theme_light_surfaceVariant,
    onSurfaceVariant = pink_theme_light_onSurfaceVariant,
    outline = pink_theme_light_outline,
    inverseOnSurface = pink_theme_light_inverseOnSurface,
    inverseSurface = pink_theme_light_inverseSurface,
    inversePrimary = pink_theme_light_inversePrimary,
    surfaceTint = pink_theme_light_surfaceTint,
)


private val DarkPinkColors = darkColorScheme(
    primary = pink_theme_dark_primary,
    onPrimary = pink_theme_dark_onPrimary,
    primaryContainer = pink_theme_dark_primaryContainer,
    onPrimaryContainer = pink_theme_dark_onPrimaryContainer,
    secondary = pink_theme_dark_secondary,
    onSecondary = pink_theme_dark_onSecondary,
    secondaryContainer = pink_theme_dark_secondaryContainer,
    onSecondaryContainer = pink_theme_dark_onSecondaryContainer,
    tertiary = pink_theme_dark_tertiary,
    onTertiary = pink_theme_dark_onTertiary,
    tertiaryContainer = pink_theme_dark_tertiaryContainer,
    onTertiaryContainer = pink_theme_dark_onTertiaryContainer,
    error = pink_theme_dark_error,
    errorContainer = pink_theme_dark_errorContainer,
    onError = pink_theme_dark_onError,
    onErrorContainer = pink_theme_dark_onErrorContainer,
    background = pink_theme_dark_background,
    onBackground = pink_theme_dark_onBackground,
    surface = pink_theme_dark_surface,
    onSurface = pink_theme_dark_onSurface,
    surfaceVariant = pink_theme_dark_surfaceVariant,
    onSurfaceVariant = pink_theme_dark_onSurfaceVariant,
    outline = pink_theme_dark_outline,
    inverseOnSurface = pink_theme_dark_inverseOnSurface,
    inverseSurface = pink_theme_dark_inverseSurface,
    inversePrimary = pink_theme_dark_inversePrimary,
    surfaceTint = pink_theme_dark_surfaceTint,
)


private val MacchiatoRosewater = darkColorScheme(
    background = macchiatoBackground,
    primary = macchiatoRosewater,
    onPrimary = macchiatoOnPrimary,
    surface = macchiatoSurface,
    onSurface = macchiatoOnSurface,
    surfaceVariant = macchiatoSurfaceVariant,
    onSurfaceVariant = macchiatoOnSurfaceVariant
)

private val MacchiatoFlamingo = darkColorScheme(
    background = macchiatoBackground,
    primary = macchiatoFlamingo,
    onPrimary = macchiatoOnPrimary,
    surface = macchiatoSurface,
    onSurface = macchiatoOnSurface,
    surfaceVariant = macchiatoSurfaceVariant,
    onSurfaceVariant = macchiatoOnSurfaceVariant
)

private val MacchiatoPink = darkColorScheme(
    background = macchiatoBackground,
    primary = macchiatoPink,
    onPrimary = macchiatoOnPrimary,
    surface = macchiatoSurface,
    onSurface = macchiatoOnSurface,
    surfaceVariant = macchiatoSurfaceVariant,
    onSurfaceVariant = macchiatoOnSurfaceVariant
)

private val MacchiatoMauve = darkColorScheme(
    background = macchiatoBackground,
    primary = macchiatoMauve,
    onPrimary = macchiatoOnPrimary,
    surface = macchiatoSurface,
    onSurface = macchiatoOnSurface,
    surfaceVariant = macchiatoSurfaceVariant,
    onSurfaceVariant = macchiatoOnSurfaceVariant
)

private val MacchiatoRed = darkColorScheme(
    background = macchiatoBackground,
    primary = macchiatoRed,
    onPrimary = macchiatoOnPrimary,
    surface = macchiatoSurface,
    onSurface = macchiatoOnSurface,
    surfaceVariant = macchiatoSurfaceVariant,
    onSurfaceVariant = macchiatoOnSurfaceVariant
)

private val MacchiatoMaroon = darkColorScheme(
    background = macchiatoBackground,
    primary = macchiatoMaroon,
    onPrimary = macchiatoOnPrimary,
    surface = macchiatoSurface,
    onSurface = macchiatoOnSurface,
    surfaceVariant = macchiatoSurfaceVariant,
    onSurfaceVariant = macchiatoOnSurfaceVariant
)

private val MacchiatoPeach = darkColorScheme(
    background = macchiatoBackground,
    primary = macchiatoPeach,
    onPrimary = macchiatoOnPrimary,
    surface = macchiatoSurface,
    onSurface = macchiatoOnSurface,
    surfaceVariant = macchiatoSurfaceVariant,
    onSurfaceVariant = macchiatoOnSurfaceVariant
)

private val MacchiatoYellow = darkColorScheme(
    background = macchiatoBackground,
    primary = macchiatoYellow,
    onPrimary = macchiatoOnPrimary,
    surface = macchiatoSurface,
    onSurface = macchiatoOnSurface,
    surfaceVariant = macchiatoSurfaceVariant,
    onSurfaceVariant = macchiatoOnSurfaceVariant
)

private val MacchiatoGreen = darkColorScheme(
    background = macchiatoBackground,
    primary = macchiatoGreen,
    onPrimary = macchiatoOnPrimary,
    surface = macchiatoSurface,
    onSurface = macchiatoOnSurface,
    surfaceVariant = macchiatoSurfaceVariant,
    onSurfaceVariant = macchiatoOnSurfaceVariant
)

private val MacchiatoTeal = darkColorScheme(
    background = macchiatoBackground,
    primary = macchiatoTeal,
    onPrimary = macchiatoOnPrimary,
    surface = macchiatoSurface,
    onSurface = macchiatoOnSurface,
    surfaceVariant = macchiatoSurfaceVariant,
    onSurfaceVariant = macchiatoOnSurfaceVariant
)

private val MacchiatoSky = darkColorScheme(
    background = macchiatoBackground,
    primary = macchiatoSky,
    onPrimary = macchiatoOnPrimary,
    surface = macchiatoSurface,
    onSurface = macchiatoOnSurface,
    surfaceVariant = macchiatoSurfaceVariant,
    onSurfaceVariant = macchiatoOnSurfaceVariant
)

private val MacchiatoSapphire = darkColorScheme(
    background = macchiatoBackground,
    primary = macchiatoSapphire,
    onPrimary = macchiatoOnPrimary,
    surface = macchiatoSurface,
    onSurface = macchiatoOnSurface,
    surfaceVariant = macchiatoSurfaceVariant,
    onSurfaceVariant = macchiatoOnSurfaceVariant
)

private val MacchiatoBlue = darkColorScheme(
    background = macchiatoBackground,
    primary = macchiatoBlue,
    onPrimary = macchiatoOnPrimary,
    surface = macchiatoSurface,
    onSurface = macchiatoOnSurface,
    surfaceVariant = macchiatoSurfaceVariant,
    onSurfaceVariant = macchiatoOnSurfaceVariant
)

private val MacchiatoLavender = darkColorScheme(
    background = macchiatoBackground,
    primary = macchiatoLavender,
    onPrimary = macchiatoOnPrimary,
    surface = macchiatoSurface,
    onSurface = macchiatoOnSurface,
    surfaceVariant = macchiatoSurfaceVariant,
    onSurfaceVariant = macchiatoOnSurfaceVariant
)

private val MochaRosewater = darkColorScheme(
    background = mochaBackground,
    primary = mochaRosewater,
    onPrimary = mochaOnPrimary,
    surface = mochaSurface,
    onSurface = mochaOnSurface,
    surfaceVariant = mochaSurfaceVariant,
    onSurfaceVariant = mochaOnSurfaceVariant
)

private val MochaFlamingo = darkColorScheme(
    background = mochaBackground,
    primary = mochaFlamingo,
    onPrimary = mochaOnPrimary,
    surface = mochaSurface,
    onSurface = mochaOnSurface,
    surfaceVariant = mochaSurfaceVariant,
    onSurfaceVariant = mochaOnSurfaceVariant
)

private val MochaPink = darkColorScheme(
    background = mochaBackground,
    primary = mochaPink,
    onPrimary = mochaOnPrimary,
    surface = mochaSurface,
    onSurface = mochaOnSurface,
    surfaceVariant = mochaSurfaceVariant,
    onSurfaceVariant = mochaOnSurfaceVariant
)

private val MochaMauve = darkColorScheme(
    background = mochaBackground,
    primary = mochaMauve,
    onPrimary = mochaOnPrimary,
    surface = mochaSurface,
    onSurface = mochaOnSurface,
    surfaceVariant = mochaSurfaceVariant,
    onSurfaceVariant = mochaOnSurfaceVariant
)

private val MochaRed = darkColorScheme(
    background = mochaBackground,
    primary = mochaRed,
    onPrimary = mochaOnPrimary,
    surface = mochaSurface,
    onSurface = mochaOnSurface,
    surfaceVariant = mochaSurfaceVariant,
    onSurfaceVariant = mochaOnSurfaceVariant
)

private val MochaMaroon = darkColorScheme(
    background = mochaBackground,
    primary = mochaMaroon,
    onPrimary = mochaOnPrimary,
    surface = mochaSurface,
    onSurface = mochaOnSurface,
    surfaceVariant = mochaSurfaceVariant,
    onSurfaceVariant = mochaOnSurfaceVariant
)

private val MochaPeach = darkColorScheme(
    background = mochaBackground,
    primary = mochaPeach,
    onPrimary = mochaOnPrimary,
    surface = mochaSurface,
    onSurface = mochaOnSurface,
    surfaceVariant = mochaSurfaceVariant,
    onSurfaceVariant = mochaOnSurfaceVariant
)

private val MochaYellow = darkColorScheme(
    background = mochaBackground,
    primary = mochaYellow,
    onPrimary = mochaOnPrimary,
    surface = mochaSurface,
    onSurface = mochaOnSurface,
    surfaceVariant = mochaSurfaceVariant,
    onSurfaceVariant = mochaOnSurfaceVariant
)


private val MochaGreen = darkColorScheme(
    background = mochaBackground,
    primary = mochaGreen,
    onPrimary = mochaOnPrimary,
    surface = mochaSurface,
    onSurface = mochaOnSurface,
    surfaceVariant = mochaSurfaceVariant,
    onSurfaceVariant = mochaOnSurfaceVariant
)

private val MochaTeal = darkColorScheme(
    background = mochaBackground,
    primary = mochaTeal,
    onPrimary = mochaOnPrimary,
    surface = mochaSurface,
    onSurface = mochaOnSurface,
    surfaceVariant = mochaSurfaceVariant,
    onSurfaceVariant = mochaOnSurfaceVariant
)

private val MochaSky = darkColorScheme(
    background = mochaBackground,
    primary = mochaSky,
    onPrimary = mochaOnPrimary,
    surface = mochaSurface,
    onSurface = mochaOnSurface,
    surfaceVariant = mochaSurfaceVariant,
    onSurfaceVariant = mochaOnSurfaceVariant
)

private val MochaSapphire = darkColorScheme(
    background = mochaBackground,
    primary = mochaSapphire,
    onPrimary = mochaOnPrimary,
    surface = mochaSurface,
    onSurface = mochaOnSurface,
    surfaceVariant = mochaSurfaceVariant,
    onSurfaceVariant = mochaOnSurfaceVariant
)

private val MochaBlue = darkColorScheme(
    background = mochaBackground,
    primary = mochaBlue,
    onPrimary = mochaOnPrimary,
    surface = mochaSurface,
    onSurface = mochaOnSurface,
    surfaceVariant = mochaSurfaceVariant,
    onSurfaceVariant = mochaOnSurfaceVariant
)

private val MochaLavender = darkColorScheme(
    background = mochaBackground,
    primary = mochaLavender,
    onPrimary = mochaOnPrimary,
    surface = mochaSurface,
    onSurface = mochaOnSurface,
    surfaceVariant = mochaSurfaceVariant,
    onSurfaceVariant = mochaOnSurfaceVariant
)


private val FrappeRosewater = darkColorScheme(
    background = frappeBackground,
    primary = frappeRosewater,
    onPrimary = frappeOnPrimary,
    surface = frappeSurface,
    onSurface = frappeOnSurface,
    surfaceVariant = frappeSurfaceVariant,
    onSurfaceVariant = frappeOnSurfaceVariant
)

private val FrappeFlamingo = darkColorScheme(
    background = frappeBackground,
    primary = frappeFlamingo,
    onPrimary = frappeOnPrimary,
    surface = frappeSurface,
    onSurface = frappeOnSurface,
    surfaceVariant = frappeSurfaceVariant,
    onSurfaceVariant = frappeOnSurfaceVariant
)

private val FrappePink = darkColorScheme(
    background = frappeBackground,
    primary = frappePink,
    onPrimary = frappeOnPrimary,
    surface = frappeSurface,
    onSurface = frappeOnSurface,
    surfaceVariant = frappeSurfaceVariant,
    onSurfaceVariant = frappeOnSurfaceVariant
)

private val FrappeMauve = darkColorScheme(
    background = frappeBackground,
    primary = frappeMauve,
    onPrimary = frappeOnPrimary,
    surface = frappeSurface,
    onSurface = frappeOnSurface,
    surfaceVariant = frappeSurfaceVariant,
    onSurfaceVariant = frappeOnSurfaceVariant
)

private val FrappeRed = darkColorScheme(
    background = frappeBackground,
    primary = frappeRed,
    onPrimary = frappeOnPrimary,
    surface = frappeSurface,
    onSurface = frappeOnSurface,
    surfaceVariant = frappeSurfaceVariant,
    onSurfaceVariant = frappeOnSurfaceVariant
)

private val FrappeMaroon = darkColorScheme(
    background = frappeBackground,
    primary = frappeMaroon,
    onPrimary = frappeOnPrimary,
    surface = frappeSurface,
    onSurface = frappeOnSurface,
    surfaceVariant = frappeSurfaceVariant,
    onSurfaceVariant = frappeOnSurfaceVariant
)

private val FrappePeach = darkColorScheme(
    background = frappeBackground,
    primary = frappePeach,
    onPrimary = frappeOnPrimary,
    surface = frappeSurface,
    onSurface = frappeOnSurface,
    surfaceVariant = frappeSurfaceVariant,
    onSurfaceVariant = frappeOnSurfaceVariant
)

private val FrappeYellow = darkColorScheme(
    background = frappeBackground,
    primary = frappeYellow,
    onPrimary = frappeOnPrimary,
    surface = frappeSurface,
    onSurface = frappeOnSurface,
    surfaceVariant = frappeSurfaceVariant,
    onSurfaceVariant = frappeOnSurfaceVariant
)


private val FrappeGreen = darkColorScheme(
    background = frappeBackground,
    primary = frappeGreen,
    onPrimary = frappeOnPrimary,
    surface = frappeSurface,
    onSurface = frappeOnSurface,
    surfaceVariant = frappeSurfaceVariant,
    onSurfaceVariant = frappeOnSurfaceVariant
)

private val FrappeTeal = darkColorScheme(
    background = frappeBackground,
    primary = frappeTeal,
    onPrimary = frappeOnPrimary,
    surface = frappeSurface,
    onSurface = frappeOnSurface,
    surfaceVariant = frappeSurfaceVariant,
    onSurfaceVariant = frappeOnSurfaceVariant
)

private val FrappeSky = darkColorScheme(
    background = frappeBackground,
    primary = frappeSky,
    onPrimary = frappeOnPrimary,
    surface = frappeSurface,
    onSurface = frappeOnSurface,
    surfaceVariant = frappeSurfaceVariant,
    onSurfaceVariant = frappeOnSurfaceVariant
)

private val FrappeSapphire = darkColorScheme(
    background = frappeBackground,
    primary = frappeSapphire,
    onPrimary = frappeOnPrimary,
    surface = frappeSurface,
    onSurface = frappeOnSurface,
    surfaceVariant = frappeSurfaceVariant,
    onSurfaceVariant = frappeOnSurfaceVariant
)

private val FrappeBlue = darkColorScheme(
    background = frappeBackground,
    primary = frappeBlue,
    onPrimary = frappeOnPrimary,
    surface = frappeSurface,
    onSurface = frappeOnSurface,
    surfaceVariant = frappeSurfaceVariant,
    onSurfaceVariant = frappeOnSurfaceVariant
)

private val FrappeLavender = darkColorScheme(
    background = frappeBackground,
    primary = frappeLavender,
    onPrimary = frappeOnPrimary,
    surface = frappeSurface,
    onSurface = frappeOnSurface,
    surfaceVariant = frappeSurfaceVariant,
    onSurfaceVariant = frappeOnSurfaceVariant
)

private val LatteRosewater = darkColorScheme(
    background = latteBackground,
    primary = latteRosewater,
    onPrimary = latteOnPrimary,
    surface = latteSurface,
    onSurface = latteOnSurface,
    surfaceVariant = latteSurfaceVariant,
    onSurfaceVariant = latteOnSurfaceVariant
)

private val LatteFlamingo = darkColorScheme(
    background = latteBackground,
    primary = latteFlamingo,
    onPrimary = latteOnPrimary,
    surface = latteSurface,
    onSurface = latteOnSurface,
    surfaceVariant = latteSurfaceVariant,
    onSurfaceVariant = latteOnSurfaceVariant
)

private val LattePink = darkColorScheme(
    background = latteBackground,
    primary = lattePink,
    onPrimary = latteOnPrimary,
    surface = latteSurface,
    onSurface = latteOnSurface,
    surfaceVariant = latteSurfaceVariant,
    onSurfaceVariant = latteOnSurfaceVariant
)

private val LatteMauve = darkColorScheme(
    background = latteBackground,
    primary = latteMauve,
    onPrimary = latteOnPrimary,
    surface = latteSurface,
    onSurface = latteOnSurface,
    surfaceVariant = latteSurfaceVariant,
    onSurfaceVariant = latteOnSurfaceVariant
)

private val LatteRed = darkColorScheme(
    background = latteBackground,
    primary = latteRed,
    onPrimary = latteOnPrimary,
    surface = latteSurface,
    onSurface = latteOnSurface,
    surfaceVariant = latteSurfaceVariant,
    onSurfaceVariant = latteOnSurfaceVariant
)

private val LatteMaroon = darkColorScheme(
    background = latteBackground,
    primary = latteMaroon,
    onPrimary = latteOnPrimary,
    surface = latteSurface,
    onSurface = latteOnSurface,
    surfaceVariant = latteSurfaceVariant,
    onSurfaceVariant = latteOnSurfaceVariant
)

private val LattePeach = darkColorScheme(
    background = latteBackground,
    primary = lattePeach,
    onPrimary = latteOnPrimary,
    surface = latteSurface,
    onSurface = latteOnSurface,
    surfaceVariant = latteSurfaceVariant,
    onSurfaceVariant = latteOnSurfaceVariant
)

private val LatteYellow = darkColorScheme(
    background = latteBackground,
    primary = latteYellow,
    onPrimary = latteOnPrimary,
    surface = latteSurface,
    onSurface = latteOnSurface,
    surfaceVariant = latteSurfaceVariant,
    onSurfaceVariant = latteOnSurfaceVariant
)


private val LatteGreen = darkColorScheme(
    background = latteBackground,
    primary = latteGreen,
    onPrimary = latteOnPrimary,
    surface = latteSurface,
    onSurface = latteOnSurface,
    surfaceVariant = latteSurfaceVariant,
    onSurfaceVariant = latteOnSurfaceVariant
)

private val LatteTeal = darkColorScheme(
    background = latteBackground,
    primary = latteTeal,
    onPrimary = latteOnPrimary,
    surface = latteSurface,
    onSurface = latteOnSurface,
    surfaceVariant = latteSurfaceVariant,
    onSurfaceVariant = latteOnSurfaceVariant
)

private val LatteSky = darkColorScheme(
    background = latteBackground,
    primary = latteSky,
    onPrimary = latteOnPrimary,
    surface = latteSurface,
    onSurface = latteOnSurface,
    surfaceVariant = latteSurfaceVariant,
    onSurfaceVariant = latteOnSurfaceVariant
)

private val LatteSapphire = darkColorScheme(
    background = latteBackground,
    primary = latteSapphire,
    onPrimary = latteOnPrimary,
    surface = latteSurface,
    onSurface = latteOnSurface,
    surfaceVariant = latteSurfaceVariant,
    onSurfaceVariant = latteOnSurfaceVariant
)

private val LatteBlue = darkColorScheme(
    background = latteBackground,
    primary = latteBlue,
    onPrimary = latteOnPrimary,
    surface = latteSurface,
    onSurface = latteOnSurface,
    surfaceVariant = latteSurfaceVariant,
    onSurfaceVariant = latteOnSurfaceVariant
)

private val LatteLavender = darkColorScheme(
    background = latteBackground,
    primary = latteLavender,
    onPrimary = latteOnPrimary,
    surface = latteSurface,
    onSurface = latteOnSurface,
    surfaceVariant = latteSurfaceVariant,
    onSurfaceVariant = latteOnSurfaceVariant
)

@Composable
fun ComposeSimpleMPTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    currentTheme: String = "System",
    content: @Composable () -> Unit
) {

    val context = LocalContext.current
    val supportsMaterialYou = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE).edit().putString("ThemeAccent", "MacchiatoLavender").apply()
    val themeAccent = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE).getString("ThemeAccent", "Default")


    val colors = when {


        currentTheme == "Dark" && themeAccent == "Blue" -> DarkBlueColors
        currentTheme == "Dark" && themeAccent == "Red" -> DarkRedColors
        currentTheme == "Dark" && themeAccent == "Purple" -> DarkPurpleColors
        currentTheme == "Dark" && themeAccent == "Yellow" -> DarkYellowColors
        currentTheme == "Dark" && themeAccent == "Orange" -> DarkOrangeColors
        currentTheme == "Dark" && themeAccent == "Green" -> DarkGreenColors
        currentTheme == "Dark" && themeAccent == "Pink" -> DarkPinkColors
        currentTheme == "Dark" && themeAccent == "MacchiatoRosewater" -> MacchiatoRosewater
        currentTheme == "Dark" && themeAccent == "MacchiatoFlamingo" -> MacchiatoFlamingo
        currentTheme == "Dark" && themeAccent == "MacchiatoPink" -> MacchiatoRosewater
        currentTheme == "Dark" && themeAccent == "MacchiatoMauve" -> MacchiatoRosewater
        currentTheme == "Dark" && themeAccent == "MacchiatoRed" -> MacchiatoRosewater
        currentTheme == "Dark" && themeAccent == "MacchiatoMaroon" -> MacchiatoMaroon
        currentTheme == "Dark" && themeAccent == "MacchiatoPeach" -> MacchiatoPeach
        currentTheme == "Dark" && themeAccent == "MacchiatoYellow" -> MacchiatoYellow
        currentTheme == "Dark" && themeAccent == "MacchiatoGreen" -> MacchiatoGreen
        currentTheme == "Dark" && themeAccent == "MacchiatoTeal" -> MacchiatoTeal
        currentTheme == "Dark" && themeAccent == "MacchiatoSky" -> MacchiatoSky
        currentTheme == "Dark" && themeAccent == "MacchiatoSapphire" -> MacchiatoSapphire
        currentTheme == "Dark" && themeAccent == "MacchiatoBlue" -> MacchiatoBlue
        currentTheme == "Dark" && themeAccent == "MacchiatoLavender" -> MacchiatoLavender
        currentTheme == "Dark" && themeAccent == "MochaRosewater" -> MochaRosewater
        currentTheme == "Dark" && themeAccent == "MochaFlamingo" -> MochaFlamingo
        currentTheme == "Dark" && themeAccent == "MochaPink" -> MochaRosewater
        currentTheme == "Dark" && themeAccent == "MochaMauve" -> MochaRosewater
        currentTheme == "Dark" && themeAccent == "MochaRed" -> MochaRosewater
        currentTheme == "Dark" && themeAccent == "MochaMaroon" -> MochaMaroon
        currentTheme == "Dark" && themeAccent == "MochaPeach" -> MochaPeach
        currentTheme == "Dark" && themeAccent == "MochaYellow" -> MochaYellow
        currentTheme == "Dark" && themeAccent == "MochaGreen" -> MochaGreen
        currentTheme == "Dark" && themeAccent == "MochaTeal" -> MochaTeal
        currentTheme == "Dark" && themeAccent == "MochaSky" -> MochaSky
        currentTheme == "Dark" && themeAccent == "MochaSapphire" -> MochaSapphire
        currentTheme == "Dark" && themeAccent == "MochaBlue" -> MochaBlue
        currentTheme == "Dark" && themeAccent == "MochaLavender" -> MochaLavender
        currentTheme == "Dark" && themeAccent == "FrappeRosewater" -> FrappeRosewater
        currentTheme == "Dark" && themeAccent == "FrappeFlamingo" -> FrappeFlamingo
        currentTheme == "Dark" && themeAccent == "FrappePink" -> FrappeRosewater
        currentTheme == "Dark" && themeAccent == "FrappeMauve" -> FrappeRosewater
        currentTheme == "Dark" && themeAccent == "FrappeRed" -> FrappeRosewater
        currentTheme == "Dark" && themeAccent == "FrappeMaroon" -> FrappeMaroon
        currentTheme == "Dark" && themeAccent == "FrappePeach" -> FrappePeach
        currentTheme == "Dark" && themeAccent == "FrappeYellow" -> FrappeYellow
        currentTheme == "Dark" && themeAccent == "FrappeGreen" -> FrappeGreen
        currentTheme == "Dark" && themeAccent == "FrappeTeal" -> FrappeTeal
        currentTheme == "Dark" && themeAccent == "FrappeSky" -> FrappeSky
        currentTheme == "Dark" && themeAccent == "FrappeSapphire" -> FrappeSapphire
        currentTheme == "Dark" && themeAccent == "FrappeBlue" -> FrappeBlue
        currentTheme == "Dark" && themeAccent == "FrappeLavender" -> FrappeLavender

        supportsMaterialYou && currentTheme == "Dark" -> dynamicDarkColorScheme(context)

        currentTheme == "Light" && themeAccent == "Blue" -> LightBlueColors
        currentTheme == "Light" && themeAccent == "Red" -> LightRedColors
        currentTheme == "Light" && themeAccent == "Purple" -> LightPurpleColors
        currentTheme == "Light" && themeAccent == "Yellow" -> LightYellowColors
        currentTheme == "Light" && themeAccent == "Orange" -> LightOrangeColors
        currentTheme == "Light" && themeAccent == "Green" -> LightGreenColors
        currentTheme == "Light" && themeAccent == "Pink" -> LightPinkColors
        currentTheme == "Light" && themeAccent == "MacchiatoRosewater" -> MacchiatoRosewater
        currentTheme == "Light" && themeAccent == "MacchiatoFlamingo" -> MacchiatoFlamingo
        currentTheme == "Light" && themeAccent == "MacchiatoPink" -> MacchiatoRosewater
        currentTheme == "Light" && themeAccent == "MacchiatoMauve" -> MacchiatoRosewater
        currentTheme == "Light" && themeAccent == "MacchiatoRed" -> MacchiatoRosewater
        currentTheme == "Light" && themeAccent == "MacchiatoMaroon" -> MacchiatoMaroon
        currentTheme == "Light" && themeAccent == "MacchiatoPeach" -> MacchiatoPeach
        currentTheme == "Light" && themeAccent == "MacchiatoYellow" -> MacchiatoYellow
        currentTheme == "Light" && themeAccent == "MacchiatoGreen" -> MacchiatoGreen
        currentTheme == "Light" && themeAccent == "MacchiatoTeal" -> MacchiatoTeal
        currentTheme == "Light" && themeAccent == "MacchiatoSky" -> MacchiatoSky
        currentTheme == "Light" && themeAccent == "MacchiatoSapphire" -> MacchiatoSapphire
        currentTheme == "Light" && themeAccent == "MacchiatoBlue" -> MacchiatoBlue
        currentTheme == "Light" && themeAccent == "MacchiatoLavender" -> MacchiatoLavender
        currentTheme == "Light" && themeAccent == "MochaRosewater" -> MochaRosewater
        currentTheme == "Light" && themeAccent == "MochaFlamingo" -> MochaFlamingo
        currentTheme == "Light" && themeAccent == "MochaPink" -> MochaRosewater
        currentTheme == "Light" && themeAccent == "MochaMauve" -> MochaRosewater
        currentTheme == "Light" && themeAccent == "MochaRed" -> MochaRosewater
        currentTheme == "Light" && themeAccent == "MochaMaroon" -> MochaMaroon
        currentTheme == "Light" && themeAccent == "MochaPeach" -> MochaPeach
        currentTheme == "Light" && themeAccent == "MochaYellow" -> MochaYellow
        currentTheme == "Light" && themeAccent == "MochaGreen" -> MochaGreen
        currentTheme == "Light" && themeAccent == "MochaTeal" -> MochaTeal
        currentTheme == "Light" && themeAccent == "MochaSky" -> MochaSky
        currentTheme == "Light" && themeAccent == "MochaSapphire" -> MochaSapphire
        currentTheme == "Light" && themeAccent == "MochaBlue" -> MochaBlue
        currentTheme == "Light" && themeAccent == "MochaLavender" -> MochaLavender
        currentTheme == "Light" && themeAccent == "FrappeRosewater" -> FrappeRosewater
        currentTheme == "Light" && themeAccent == "FrappeFlamingo" -> FrappeFlamingo
        currentTheme == "Light" && themeAccent == "FrappePink" -> FrappeRosewater
        currentTheme == "Light" && themeAccent == "FrappeMauve" -> FrappeRosewater
        currentTheme == "Light" && themeAccent == "FrappeRed" -> FrappeRosewater
        currentTheme == "Light" && themeAccent == "FrappeMaroon" -> FrappeMaroon
        currentTheme == "Light" && themeAccent == "FrappePeach" -> FrappePeach
        currentTheme == "Light" && themeAccent == "FrappeYellow" -> FrappeYellow
        currentTheme == "Light" && themeAccent == "FrappeGreen" -> FrappeGreen
        currentTheme == "Light" && themeAccent == "FrappeTeal" -> FrappeTeal
        currentTheme == "Light" && themeAccent == "FrappeSky" -> FrappeSky
        currentTheme == "Light" && themeAccent == "FrappeSapphire" -> FrappeSapphire
        currentTheme == "Light" && themeAccent == "FrappeBlue" -> FrappeBlue
        currentTheme == "Light" && themeAccent == "FrappeLavender" -> FrappeLavender

        supportsMaterialYou && currentTheme == "Light" -> dynamicLightColorScheme(context)

        themeAccent == "Blue" && useDarkTheme -> DarkBlueColors
        themeAccent == "Red" && useDarkTheme -> DarkRedColors
        themeAccent == "Purple" && useDarkTheme -> DarkPurpleColors
        themeAccent == "Yellow" && useDarkTheme -> DarkYellowColors
        themeAccent == "Orange" && useDarkTheme -> DarkOrangeColors
        themeAccent == "Green" && useDarkTheme -> DarkGreenColors
        themeAccent == "Pink" && useDarkTheme -> DarkPinkColors
        themeAccent == "MacchiatoRosewater" && useDarkTheme -> MacchiatoRosewater
        themeAccent == "MacchiatoFlamingo" && useDarkTheme -> MacchiatoFlamingo
        themeAccent == "MacchiatoPink" && useDarkTheme -> MacchiatoPink
        themeAccent == "MacchiatoMauve" && useDarkTheme -> MacchiatoMauve
        themeAccent == "MacchiatoRed" && useDarkTheme -> MacchiatoRed
        themeAccent == "MacchiatoMaroon" && useDarkTheme -> MacchiatoMaroon
        themeAccent == "MacchiatoPeach" && useDarkTheme -> MacchiatoPeach
        themeAccent == "MacchiatoYellow" && useDarkTheme -> MacchiatoYellow
        themeAccent == "MacchiatoGreen" && useDarkTheme -> MacchiatoGreen
        themeAccent == "MacchiatoTeal" && useDarkTheme -> MacchiatoTeal
        themeAccent == "MacchiatoSky" && useDarkTheme -> MacchiatoSky
        themeAccent == "MacchiatoSapphire" && useDarkTheme -> MacchiatoSapphire
        themeAccent == "MacchiatoBlue" && useDarkTheme -> MacchiatoBlue
        themeAccent == "MacchiatoLavender" && useDarkTheme -> MacchiatoLavender
        themeAccent == "MochaRosewater" && useDarkTheme -> MochaRosewater
        themeAccent == "MochaFlamingo" && useDarkTheme -> MochaFlamingo
        themeAccent == "MochaPink" && useDarkTheme -> MochaPink
        themeAccent == "MochaMauve" && useDarkTheme -> MochaMauve
        themeAccent == "MochaRed" && useDarkTheme -> MochaRed
        themeAccent == "MochaMaroon" && useDarkTheme -> MochaMaroon
        themeAccent == "MochaPeach" && useDarkTheme -> MochaPeach
        themeAccent == "MochaYellow" && useDarkTheme -> MochaYellow
        themeAccent == "MochaGreen" && useDarkTheme -> MochaGreen
        themeAccent == "MochaTeal" && useDarkTheme -> MochaTeal
        themeAccent == "MochaSky" && useDarkTheme -> MochaSky
        themeAccent == "MochaSapphire" && useDarkTheme -> MochaSapphire
        themeAccent == "MochaBlue" && useDarkTheme -> MochaBlue
        themeAccent == "MochaLavender" && useDarkTheme -> MochaLavender
        themeAccent == "FrappeRosewater" && useDarkTheme -> FrappeRosewater
        themeAccent == "FrappeFlamingo" && useDarkTheme -> FrappeFlamingo
        themeAccent == "FrappePink" && useDarkTheme -> FrappePink
        themeAccent == "FrappeMauve" && useDarkTheme -> FrappeMauve
        themeAccent == "FrappeRed" && useDarkTheme -> FrappeRed
        themeAccent == "FrappeMaroon" && useDarkTheme -> FrappeMaroon
        themeAccent == "FrappePeach" && useDarkTheme -> FrappePeach
        themeAccent == "FrappeYellow" && useDarkTheme -> FrappeYellow
        themeAccent == "FrappeGreen" && useDarkTheme -> FrappeGreen
        themeAccent == "FrappeTeal" && useDarkTheme -> FrappeTeal
        themeAccent == "FrappeSky" && useDarkTheme -> FrappeSky
        themeAccent == "FrappeSapphire" && useDarkTheme -> FrappeSapphire
        themeAccent == "FrappeBlue" && useDarkTheme -> FrappeBlue
        themeAccent == "FrappeLavender" && useDarkTheme -> FrappeLavender

        supportsMaterialYou && useDarkTheme -> dynamicDarkColorScheme(context)

        themeAccent == "Blue" && !useDarkTheme -> LightBlueColors
        themeAccent == "Red" && !useDarkTheme -> LightRedColors
        themeAccent == "Purple" && !useDarkTheme -> LightPurpleColors
        themeAccent == "Yellow" && !useDarkTheme -> LightYellowColors
        themeAccent == "Orange" && !useDarkTheme -> LightOrangeColors
        themeAccent == "Green" && !useDarkTheme -> LightGreenColors
        themeAccent == "Pink" && !useDarkTheme -> LightPinkColors
        themeAccent!!.contains("Rosewater") && !useDarkTheme -> LatteRosewater
        themeAccent.contains("Flamingo") && !useDarkTheme -> LatteFlamingo
        themeAccent.contains("Pink") && !useDarkTheme -> LattePink
        themeAccent.contains("Mauve") && !useDarkTheme -> LatteMauve
        themeAccent.contains("Red") && !useDarkTheme -> LatteRed
        themeAccent.contains("Maroon") && !useDarkTheme -> LatteMaroon
        themeAccent.contains("Peach") && !useDarkTheme -> LattePeach
        themeAccent.contains("Yellow") && !useDarkTheme -> LatteYellow
        themeAccent.contains("Green") && !useDarkTheme -> LatteGreen
        themeAccent.contains("Teal") && !useDarkTheme -> LatteTeal
        themeAccent.contains("Sky") && !useDarkTheme -> LatteSky
        themeAccent.contains("Sapphire") && !useDarkTheme -> LatteSapphire
        themeAccent.contains("Blue") && !useDarkTheme -> LatteBlue
        themeAccent.contains("Lavender") && !useDarkTheme -> LatteLavender

        supportsMaterialYou && !useDarkTheme -> dynamicLightColorScheme(context)

        useDarkTheme -> DarkBlueColors

        else -> LightBlueColors
    }

    when (currentTheme) {

        "Dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        "Light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }


    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}