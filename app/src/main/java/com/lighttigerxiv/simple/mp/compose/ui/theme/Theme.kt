package com.lighttigerxiv.simple.mp.compose.ui.theme

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.lighttigerxiv.simple.mp.compose.data.variables.Settings
import com.lighttigerxiv.simple.mp.compose.functions.isAtLeastAndroid12
import com.lighttigerxiv.simple.mp.compose.settings.SettingsVM


val LightBlueColors = lightColorScheme(
    primary = AppColorScheme.Blue.Light.PRIMARY,
    onPrimary = AppColorScheme.Blue.Light.ON_PRIMARY,
    primaryContainer = AppColorScheme.Blue.Light.PRIMARY_CONTAINER,
    onPrimaryContainer = AppColorScheme.Blue.Light.ON_PRIMARY_CONTAINER,
    secondary = AppColorScheme.Blue.Light.SECONDARY,
    onSecondary = AppColorScheme.Blue.Light.ON_SECONDARY,
    secondaryContainer = AppColorScheme.Blue.Light.SECONDARY_CONTAINER,
    onSecondaryContainer = AppColorScheme.Blue.Light.ON_SECONDARY_CONTAINER,
    tertiary = AppColorScheme.Blue.Light.TERTIARY,
    onTertiary = AppColorScheme.Blue.Light.ON_TERTIARY,
    tertiaryContainer = AppColorScheme.Blue.Light.TERTIARY_CONTAINER,
    onTertiaryContainer = AppColorScheme.Blue.Light.ON_TERTIARY_CONTAINER,
    error = AppColorScheme.Blue.Light.ERROR,
    errorContainer = AppColorScheme.Blue.Light.ERROR_CONTAINER,
    onError = AppColorScheme.Blue.Light.ON_ERROR,
    onErrorContainer = AppColorScheme.Blue.Light.ON_ERROR_CONTAINER,
    background = AppColorScheme.Blue.Light.BACKGROUND,
    onBackground = AppColorScheme.Blue.Light.ON_BACKGROUND,
    surface = AppColorScheme.Blue.Light.SURFACE,
    onSurface = AppColorScheme.Blue.Light.ON_SURFACE,
    surfaceVariant = AppColorScheme.Blue.Light.SURFACE_VARIANT,
    onSurfaceVariant = AppColorScheme.Blue.Light.ON_SURFACE_VARIANT,
    outline = AppColorScheme.Blue.Light.OUTLINE,
    inverseOnSurface = AppColorScheme.Blue.Light.INVERSE_ON_SURFACE,
    inverseSurface = AppColorScheme.Blue.Light.INVERSE_SURFACE,
    inversePrimary = AppColorScheme.Blue.Light.INVERSE_PRIMARY,
    surfaceTint = AppColorScheme.Blue.Light.SURFACE_TINT,
)


val DarkBlueColors = darkColorScheme(
    primary = AppColorScheme.Blue.Dark.PRIMARY,
    onPrimary = AppColorScheme.Blue.Dark.ON_PRIMARY,
    primaryContainer = AppColorScheme.Blue.Dark.PRIMARY_CONTAINER,
    onPrimaryContainer = AppColorScheme.Blue.Dark.ON_PRIMARY_CONTAINER,
    secondary = AppColorScheme.Blue.Dark.SECONDARY,
    onSecondary = AppColorScheme.Blue.Dark.ON_SECONDARY,
    secondaryContainer = AppColorScheme.Blue.Dark.SECONDARY_CONTAINER,
    onSecondaryContainer = AppColorScheme.Blue.Dark.ON_SECONDARY_CONTAINER,
    tertiary = AppColorScheme.Blue.Dark.TERTIARY,
    onTertiary = AppColorScheme.Blue.Dark.ON_TERTIARY,
    tertiaryContainer = AppColorScheme.Blue.Dark.TERTIARY_CONTAINER,
    onTertiaryContainer = AppColorScheme.Blue.Dark.ON_TERTIARY_CONTAINER,
    error = AppColorScheme.Blue.Dark.ERROR,
    errorContainer = AppColorScheme.Blue.Dark.ERROR_CONTAINER,
    onError = AppColorScheme.Blue.Dark.ON_ERROR,
    onErrorContainer = AppColorScheme.Blue.Dark.ON_ERROR_CONTAINER,
    background = AppColorScheme.Blue.Dark.BACKGROUND,
    onBackground = AppColorScheme.Blue.Dark.ON_BACKGROUND,
    surface = AppColorScheme.Blue.Dark.SURFACE,
    onSurface = AppColorScheme.Blue.Dark.ON_SURFACE,
    surfaceVariant = AppColorScheme.Blue.Dark.SURFACE_VARIANT,
    onSurfaceVariant = AppColorScheme.Blue.Dark.ON_SURFACE_VARIANT,
    outline = AppColorScheme.Blue.Dark.OUTLINE,
    inverseOnSurface = AppColorScheme.Blue.Dark.INVERSE_ON_SURFACE,
    inverseSurface = AppColorScheme.Blue.Dark.INVERSE_SURFACE,
    inversePrimary = AppColorScheme.Blue.Dark.INVERSE_PRIMARY,
    surfaceTint = AppColorScheme.Blue.Dark.SURFACE_TINT,
)


val LightRedColors = lightColorScheme(
    primary = AppColorScheme.Red.Light.PRIMARY,
    onPrimary = AppColorScheme.Red.Light.ON_PRIMARY,
    primaryContainer = AppColorScheme.Red.Light.PRIMARY_CONTAINER,
    onPrimaryContainer = AppColorScheme.Red.Light.ON_PRIMARY_CONTAINER,
    secondary = AppColorScheme.Red.Light.SECONDARY,
    onSecondary = AppColorScheme.Red.Light.ON_SECONDARY,
    secondaryContainer = AppColorScheme.Red.Light.SECONDARY_CONTAINER,
    onSecondaryContainer = AppColorScheme.Red.Light.ON_SECONDARY_CONTAINER,
    tertiary = AppColorScheme.Red.Light.TERTIARY,
    onTertiary = AppColorScheme.Red.Light.ON_TERTIARY,
    tertiaryContainer = AppColorScheme.Red.Light.TERTIARY_CONTAINER,
    onTertiaryContainer = AppColorScheme.Red.Light.ON_TERTIARY_CONTAINER,
    error = AppColorScheme.Red.Light.ERROR,
    errorContainer = AppColorScheme.Red.Light.ERROR_CONTAINER,
    onError = AppColorScheme.Red.Light.ON_ERROR,
    onErrorContainer = AppColorScheme.Red.Light.ON_ERROR_CONTAINER,
    background = AppColorScheme.Red.Light.BACKGROUND,
    onBackground = AppColorScheme.Red.Light.ON_BACKGROUND,
    surface = AppColorScheme.Red.Light.SURFACE,
    onSurface = AppColorScheme.Red.Light.ON_SURFACE,
    surfaceVariant = AppColorScheme.Red.Light.SURFACE_VARIANT,
    onSurfaceVariant = AppColorScheme.Red.Light.ON_SURFACE_VARIANT,
    outline = AppColorScheme.Red.Light.OUTLINE,
    inverseOnSurface = AppColorScheme.Red.Light.INVERSE_ON_SURFACE,
    inverseSurface = AppColorScheme.Red.Light.INVERSE_SURFACE,
    inversePrimary = AppColorScheme.Red.Light.INVERSE_PRIMARY,
    surfaceTint = AppColorScheme.Red.Light.SURFACE_TINT,
)


val DarkRedColors = darkColorScheme(
    primary = AppColorScheme.Red.Dark.PRIMARY,
    onPrimary = AppColorScheme.Red.Dark.ON_PRIMARY,
    primaryContainer = AppColorScheme.Red.Dark.PRIMARY_CONTAINER,
    onPrimaryContainer = AppColorScheme.Red.Dark.ON_PRIMARY_CONTAINER,
    secondary = AppColorScheme.Red.Dark.SECONDARY,
    onSecondary = AppColorScheme.Red.Dark.ON_SECONDARY,
    secondaryContainer = AppColorScheme.Red.Dark.SECONDARY_CONTAINER,
    onSecondaryContainer = AppColorScheme.Red.Dark.ON_SECONDARY_CONTAINER,
    tertiary = AppColorScheme.Red.Dark.TERTIARY,
    onTertiary = AppColorScheme.Red.Dark.ON_TERTIARY,
    tertiaryContainer = AppColorScheme.Red.Dark.TERTIARY_CONTAINER,
    onTertiaryContainer = AppColorScheme.Red.Dark.ON_TERTIARY_CONTAINER,
    error = AppColorScheme.Red.Dark.ERROR,
    errorContainer = AppColorScheme.Red.Dark.ERROR_CONTAINER,
    onError = AppColorScheme.Red.Dark.ON_ERROR,
    onErrorContainer = AppColorScheme.Red.Dark.ON_ERROR_CONTAINER,
    background = AppColorScheme.Red.Dark.BACKGROUND,
    onBackground = AppColorScheme.Red.Dark.ON_BACKGROUND,
    surface = AppColorScheme.Red.Dark.SURFACE,
    onSurface = AppColorScheme.Red.Dark.ON_SURFACE,
    surfaceVariant = AppColorScheme.Red.Dark.SURFACE_VARIANT,
    onSurfaceVariant = AppColorScheme.Red.Dark.ON_SURFACE_VARIANT,
    outline = AppColorScheme.Red.Dark.OUTLINE,
    inverseOnSurface = AppColorScheme.Red.Dark.INVERSE_ON_SURFACE,
    inverseSurface = AppColorScheme.Red.Dark.INVERSE_SURFACE,
    inversePrimary = AppColorScheme.Red.Dark.INVERSE_PRIMARY,
    surfaceTint = AppColorScheme.Red.Dark.SURFACE_TINT,
)

val LightPurpleColors = lightColorScheme(
    primary = AppColorScheme.Purple.Light.PRIMARY,
    onPrimary = AppColorScheme.Purple.Light.ON_PRIMARY,
    primaryContainer = AppColorScheme.Purple.Light.PRIMARY_CONTAINER,
    onPrimaryContainer = AppColorScheme.Purple.Light.ON_PRIMARY_CONTAINER,
    secondary = AppColorScheme.Purple.Light.SECONDARY,
    onSecondary = AppColorScheme.Purple.Light.ON_SECONDARY,
    secondaryContainer = AppColorScheme.Purple.Light.SECONDARY_CONTAINER,
    onSecondaryContainer = AppColorScheme.Purple.Light.ON_SECONDARY_CONTAINER,
    tertiary = AppColorScheme.Purple.Light.TERTIARY,
    onTertiary = AppColorScheme.Purple.Light.ON_TERTIARY,
    tertiaryContainer = AppColorScheme.Purple.Light.TERTIARY_CONTAINER,
    onTertiaryContainer = AppColorScheme.Purple.Light.ON_TERTIARY_CONTAINER,
    error = AppColorScheme.Purple.Light.ERROR,
    errorContainer = AppColorScheme.Purple.Light.ERROR_CONTAINER,
    onError = AppColorScheme.Purple.Light.ON_ERROR,
    onErrorContainer = AppColorScheme.Purple.Light.ON_ERROR_CONTAINER,
    background = AppColorScheme.Purple.Light.BACKGROUND,
    onBackground = AppColorScheme.Purple.Light.ON_BACKGROUND,
    surface = AppColorScheme.Purple.Light.SURFACE,
    onSurface = AppColorScheme.Purple.Light.ON_SURFACE,
    surfaceVariant = AppColorScheme.Purple.Light.SURFACE_VARIANT,
    onSurfaceVariant = AppColorScheme.Purple.Light.ON_SURFACE_VARIANT,
    outline = AppColorScheme.Purple.Light.OUTLINE,
    inverseOnSurface = AppColorScheme.Purple.Light.INVERSE_ON_SURFACE,
    inverseSurface = AppColorScheme.Purple.Light.INVERSE_SURFACE,
    inversePrimary = AppColorScheme.Purple.Light.INVERSE_PRIMARY,
    surfaceTint = AppColorScheme.Purple.Light.SURFACE_TINT,
)


val DarkPurpleColors = darkColorScheme(
    primary = AppColorScheme.Purple.Dark.PRIMARY,
    onPrimary = AppColorScheme.Purple.Dark.ON_PRIMARY,
    primaryContainer = AppColorScheme.Purple.Dark.PRIMARY_CONTAINER,
    onPrimaryContainer = AppColorScheme.Purple.Dark.ON_PRIMARY_CONTAINER,
    secondary = AppColorScheme.Purple.Dark.SECONDARY,
    onSecondary = AppColorScheme.Purple.Dark.ON_SECONDARY,
    secondaryContainer = AppColorScheme.Purple.Dark.SECONDARY_CONTAINER,
    onSecondaryContainer = AppColorScheme.Purple.Dark.ON_SECONDARY_CONTAINER,
    tertiary = AppColorScheme.Purple.Dark.TERTIARY,
    onTertiary = AppColorScheme.Purple.Dark.ON_TERTIARY,
    tertiaryContainer = AppColorScheme.Purple.Dark.TERTIARY_CONTAINER,
    onTertiaryContainer = AppColorScheme.Purple.Dark.ON_TERTIARY_CONTAINER,
    error = AppColorScheme.Purple.Dark.ERROR,
    errorContainer = AppColorScheme.Purple.Dark.ERROR_CONTAINER,
    onError = AppColorScheme.Purple.Dark.ON_ERROR,
    onErrorContainer = AppColorScheme.Purple.Dark.ON_ERROR_CONTAINER,
    background = AppColorScheme.Purple.Dark.BACKGROUND,
    onBackground = AppColorScheme.Purple.Dark.ON_BACKGROUND,
    surface = AppColorScheme.Purple.Dark.SURFACE,
    onSurface = AppColorScheme.Purple.Dark.ON_SURFACE,
    surfaceVariant = AppColorScheme.Purple.Dark.SURFACE_VARIANT,
    onSurfaceVariant = AppColorScheme.Purple.Dark.ON_SURFACE_VARIANT,
    outline = AppColorScheme.Purple.Dark.OUTLINE,
    inverseOnSurface = AppColorScheme.Purple.Dark.INVERSE_ON_SURFACE,
    inverseSurface = AppColorScheme.Purple.Dark.INVERSE_SURFACE,
    inversePrimary = AppColorScheme.Purple.Dark.INVERSE_PRIMARY,
    surfaceTint = AppColorScheme.Purple.Dark.SURFACE_TINT,
)

val LightYellowColors = lightColorScheme(
    primary = AppColorScheme.Yellow.Light.PRIMARY,
    onPrimary = AppColorScheme.Yellow.Light.ON_PRIMARY,
    primaryContainer = AppColorScheme.Yellow.Light.PRIMARY_CONTAINER,
    onPrimaryContainer = AppColorScheme.Yellow.Light.ON_PRIMARY_CONTAINER,
    secondary = AppColorScheme.Yellow.Light.SECONDARY,
    onSecondary = AppColorScheme.Yellow.Light.ON_SECONDARY,
    secondaryContainer = AppColorScheme.Yellow.Light.SECONDARY_CONTAINER,
    onSecondaryContainer = AppColorScheme.Yellow.Light.ON_SECONDARY_CONTAINER,
    tertiary = AppColorScheme.Yellow.Light.TERTIARY,
    onTertiary = AppColorScheme.Yellow.Light.ON_TERTIARY,
    tertiaryContainer = AppColorScheme.Yellow.Light.TERTIARY_CONTAINER,
    onTertiaryContainer = AppColorScheme.Yellow.Light.ON_TERTIARY_CONTAINER,
    error = AppColorScheme.Yellow.Light.ERROR,
    errorContainer = AppColorScheme.Yellow.Light.ERROR_CONTAINER,
    onError = AppColorScheme.Yellow.Light.ON_ERROR,
    onErrorContainer = AppColorScheme.Yellow.Light.ON_ERROR_CONTAINER,
    background = AppColorScheme.Yellow.Light.BACKGROUND,
    onBackground = AppColorScheme.Yellow.Light.ON_BACKGROUND,
    surface = AppColorScheme.Yellow.Light.SURFACE,
    onSurface = AppColorScheme.Yellow.Light.ON_SURFACE,
    surfaceVariant = AppColorScheme.Yellow.Light.SURFACE_VARIANT,
    onSurfaceVariant = AppColorScheme.Yellow.Light.ON_SURFACE_VARIANT,
    outline = AppColorScheme.Yellow.Light.OUTLINE,
    inverseOnSurface = AppColorScheme.Yellow.Light.INVERSE_ON_SURFACE,
    inverseSurface = AppColorScheme.Yellow.Light.INVERSE_SURFACE,
    inversePrimary = AppColorScheme.Yellow.Light.INVERSE_PRIMARY,
    surfaceTint = AppColorScheme.Yellow.Light.SURFACE_TINT,
)


val DarkYellowColors = darkColorScheme(
    primary = AppColorScheme.Yellow.Dark.PRIMARY,
    onPrimary = AppColorScheme.Yellow.Dark.ON_PRIMARY,
    primaryContainer = AppColorScheme.Yellow.Dark.PRIMARY_CONTAINER,
    onPrimaryContainer = AppColorScheme.Yellow.Dark.ON_PRIMARY_CONTAINER,
    secondary = AppColorScheme.Yellow.Dark.SECONDARY,
    onSecondary = AppColorScheme.Yellow.Dark.ON_SECONDARY,
    secondaryContainer = AppColorScheme.Yellow.Dark.SECONDARY_CONTAINER,
    onSecondaryContainer = AppColorScheme.Yellow.Dark.ON_SECONDARY_CONTAINER,
    tertiary = AppColorScheme.Yellow.Dark.TERTIARY,
    onTertiary = AppColorScheme.Yellow.Dark.ON_TERTIARY,
    tertiaryContainer = AppColorScheme.Yellow.Dark.TERTIARY_CONTAINER,
    onTertiaryContainer = AppColorScheme.Yellow.Dark.ON_TERTIARY_CONTAINER,
    error = AppColorScheme.Yellow.Dark.ERROR,
    errorContainer = AppColorScheme.Yellow.Dark.ERROR_CONTAINER,
    onError = AppColorScheme.Yellow.Dark.ON_ERROR,
    onErrorContainer = AppColorScheme.Yellow.Dark.ON_ERROR_CONTAINER,
    background = AppColorScheme.Yellow.Dark.BACKGROUND,
    onBackground = AppColorScheme.Yellow.Dark.ON_BACKGROUND,
    surface = AppColorScheme.Yellow.Dark.SURFACE,
    onSurface = AppColorScheme.Yellow.Dark.ON_SURFACE,
    surfaceVariant = AppColorScheme.Yellow.Dark.SURFACE_VARIANT,
    onSurfaceVariant = AppColorScheme.Yellow.Dark.ON_SURFACE_VARIANT,
    outline = AppColorScheme.Yellow.Dark.OUTLINE,
    inverseOnSurface = AppColorScheme.Yellow.Dark.INVERSE_ON_SURFACE,
    inverseSurface = AppColorScheme.Yellow.Dark.INVERSE_SURFACE,
    inversePrimary = AppColorScheme.Yellow.Dark.INVERSE_PRIMARY,
    surfaceTint = AppColorScheme.Yellow.Dark.SURFACE_TINT,
)


val LightOrangeColors = lightColorScheme(
    primary = AppColorScheme.Orange.Light.PRIMARY,
    onPrimary = AppColorScheme.Orange.Light.ON_PRIMARY,
    primaryContainer = AppColorScheme.Orange.Light.PRIMARY_CONTAINER,
    onPrimaryContainer = AppColorScheme.Orange.Light.ON_PRIMARY_CONTAINER,
    secondary = AppColorScheme.Orange.Light.SECONDARY,
    onSecondary = AppColorScheme.Orange.Light.ON_SECONDARY,
    secondaryContainer = AppColorScheme.Orange.Light.SECONDARY_CONTAINER,
    onSecondaryContainer = AppColorScheme.Orange.Light.ON_SECONDARY_CONTAINER,
    tertiary = AppColorScheme.Orange.Light.TERTIARY,
    onTertiary = AppColorScheme.Orange.Light.ON_TERTIARY,
    tertiaryContainer = AppColorScheme.Orange.Light.TERTIARY_CONTAINER,
    onTertiaryContainer = AppColorScheme.Orange.Light.ON_TERTIARY_CONTAINER,
    error = AppColorScheme.Orange.Light.ERROR,
    errorContainer = AppColorScheme.Orange.Light.ERROR_CONTAINER,
    onError = AppColorScheme.Orange.Light.ON_ERROR,
    onErrorContainer = AppColorScheme.Orange.Light.ON_ERROR_CONTAINER,
    background = AppColorScheme.Orange.Light.BACKGROUND,
    onBackground = AppColorScheme.Orange.Light.ON_BACKGROUND,
    surface = AppColorScheme.Orange.Light.SURFACE,
    onSurface = AppColorScheme.Orange.Light.ON_SURFACE,
    surfaceVariant = AppColorScheme.Orange.Light.SURFACE_VARIANT,
    onSurfaceVariant = AppColorScheme.Orange.Light.ON_SURFACE_VARIANT,
    outline = AppColorScheme.Orange.Light.OUTLINE,
    inverseOnSurface = AppColorScheme.Orange.Light.INVERSE_ON_SURFACE,
    inverseSurface = AppColorScheme.Orange.Light.INVERSE_SURFACE,
    inversePrimary = AppColorScheme.Orange.Light.INVERSE_PRIMARY,
    surfaceTint = AppColorScheme.Orange.Light.SURFACE_TINT,
)


val DarkOrangeColors = darkColorScheme(
    primary = AppColorScheme.Orange.Dark.PRIMARY,
    onPrimary = AppColorScheme.Orange.Dark.ON_PRIMARY,
    primaryContainer = AppColorScheme.Orange.Dark.PRIMARY_CONTAINER,
    onPrimaryContainer = AppColorScheme.Orange.Dark.ON_PRIMARY_CONTAINER,
    secondary = AppColorScheme.Orange.Dark.SECONDARY,
    onSecondary = AppColorScheme.Orange.Dark.ON_SECONDARY,
    secondaryContainer = AppColorScheme.Orange.Dark.SECONDARY_CONTAINER,
    onSecondaryContainer = AppColorScheme.Orange.Dark.ON_SECONDARY_CONTAINER,
    tertiary = AppColorScheme.Orange.Dark.TERTIARY,
    onTertiary = AppColorScheme.Orange.Dark.ON_TERTIARY,
    tertiaryContainer = AppColorScheme.Orange.Dark.TERTIARY_CONTAINER,
    onTertiaryContainer = AppColorScheme.Orange.Dark.ON_TERTIARY_CONTAINER,
    error = AppColorScheme.Orange.Dark.ERROR,
    errorContainer = AppColorScheme.Orange.Dark.ERROR_CONTAINER,
    onError = AppColorScheme.Orange.Dark.ON_ERROR,
    onErrorContainer = AppColorScheme.Orange.Dark.ON_ERROR_CONTAINER,
    background = AppColorScheme.Orange.Dark.BACKGROUND,
    onBackground = AppColorScheme.Orange.Dark.ON_BACKGROUND,
    surface = AppColorScheme.Orange.Dark.SURFACE,
    onSurface = AppColorScheme.Orange.Dark.ON_SURFACE,
    surfaceVariant = AppColorScheme.Orange.Dark.SURFACE_VARIANT,
    onSurfaceVariant = AppColorScheme.Orange.Dark.ON_SURFACE_VARIANT,
    outline = AppColorScheme.Orange.Dark.OUTLINE,
    inverseOnSurface = AppColorScheme.Orange.Dark.INVERSE_ON_SURFACE,
    inverseSurface = AppColorScheme.Orange.Dark.INVERSE_SURFACE,
    inversePrimary = AppColorScheme.Orange.Dark.INVERSE_PRIMARY,
    surfaceTint = AppColorScheme.Orange.Dark.SURFACE_TINT,
)

val LightGreenColors = lightColorScheme(
    primary = AppColorScheme.Green.Light.PRIMARY,
    onPrimary = AppColorScheme.Green.Light.ON_PRIMARY,
    primaryContainer = AppColorScheme.Green.Light.PRIMARY_CONTAINER,
    onPrimaryContainer = AppColorScheme.Green.Light.ON_PRIMARY_CONTAINER,
    secondary = AppColorScheme.Green.Light.SECONDARY,
    onSecondary = AppColorScheme.Green.Light.ON_SECONDARY,
    secondaryContainer = AppColorScheme.Green.Light.SECONDARY_CONTAINER,
    onSecondaryContainer = AppColorScheme.Green.Light.ON_SECONDARY_CONTAINER,
    tertiary = AppColorScheme.Green.Light.TERTIARY,
    onTertiary = AppColorScheme.Green.Light.ON_TERTIARY,
    tertiaryContainer = AppColorScheme.Green.Light.TERTIARY_CONTAINER,
    onTertiaryContainer = AppColorScheme.Green.Light.ON_TERTIARY_CONTAINER,
    error = AppColorScheme.Green.Light.ERROR,
    errorContainer = AppColorScheme.Green.Light.ERROR_CONTAINER,
    onError = AppColorScheme.Green.Light.ON_ERROR,
    onErrorContainer = AppColorScheme.Green.Light.ON_ERROR_CONTAINER,
    background = AppColorScheme.Green.Light.BACKGROUND,
    onBackground = AppColorScheme.Green.Light.ON_BACKGROUND,
    surface = AppColorScheme.Green.Light.SURFACE,
    onSurface = AppColorScheme.Green.Light.ON_SURFACE,
    surfaceVariant = AppColorScheme.Green.Light.SURFACE_VARIANT,
    onSurfaceVariant = AppColorScheme.Green.Light.ON_SURFACE_VARIANT,
    outline = AppColorScheme.Green.Light.OUTLINE,
    inverseOnSurface = AppColorScheme.Green.Light.INVERSE_ON_SURFACE,
    inverseSurface = AppColorScheme.Green.Light.INVERSE_SURFACE,
    inversePrimary = AppColorScheme.Green.Light.INVERSE_PRIMARY,
    surfaceTint = AppColorScheme.Green.Light.SURFACE_TINT,
)


val DarkGreenColors = darkColorScheme(
    primary = AppColorScheme.Green.Dark.PRIMARY,
    onPrimary = AppColorScheme.Green.Dark.ON_PRIMARY,
    primaryContainer = AppColorScheme.Green.Dark.PRIMARY_CONTAINER,
    onPrimaryContainer = AppColorScheme.Green.Dark.ON_PRIMARY_CONTAINER,
    secondary = AppColorScheme.Green.Dark.SECONDARY,
    onSecondary = AppColorScheme.Green.Dark.ON_SECONDARY,
    secondaryContainer = AppColorScheme.Green.Dark.SECONDARY_CONTAINER,
    onSecondaryContainer = AppColorScheme.Green.Dark.ON_SECONDARY_CONTAINER,
    tertiary = AppColorScheme.Green.Dark.TERTIARY,
    onTertiary = AppColorScheme.Green.Dark.ON_TERTIARY,
    tertiaryContainer = AppColorScheme.Green.Dark.TERTIARY_CONTAINER,
    onTertiaryContainer = AppColorScheme.Green.Dark.ON_TERTIARY_CONTAINER,
    error = AppColorScheme.Green.Dark.ERROR,
    errorContainer = AppColorScheme.Green.Dark.ERROR_CONTAINER,
    onError = AppColorScheme.Green.Dark.ON_ERROR,
    onErrorContainer = AppColorScheme.Green.Dark.ON_ERROR_CONTAINER,
    background = AppColorScheme.Green.Dark.BACKGROUND,
    onBackground = AppColorScheme.Green.Dark.ON_BACKGROUND,
    surface = AppColorScheme.Green.Dark.SURFACE,
    onSurface = AppColorScheme.Green.Dark.ON_SURFACE,
    surfaceVariant = AppColorScheme.Green.Dark.SURFACE_VARIANT,
    onSurfaceVariant = AppColorScheme.Green.Dark.ON_SURFACE_VARIANT,
    outline = AppColorScheme.Green.Dark.OUTLINE,
    inverseOnSurface = AppColorScheme.Green.Dark.INVERSE_ON_SURFACE,
    inverseSurface = AppColorScheme.Green.Dark.INVERSE_SURFACE,
    inversePrimary = AppColorScheme.Green.Dark.INVERSE_PRIMARY,
    surfaceTint = AppColorScheme.Green.Dark.SURFACE_TINT,
)

val LightPinkColors = lightColorScheme(
    primary = AppColorScheme.Pink.Light.PRIMARY,
    onPrimary = AppColorScheme.Pink.Light.ON_PRIMARY,
    primaryContainer = AppColorScheme.Pink.Light.PRIMARY_CONTAINER,
    onPrimaryContainer = AppColorScheme.Pink.Light.ON_PRIMARY_CONTAINER,
    secondary = AppColorScheme.Pink.Light.SECONDARY,
    onSecondary = AppColorScheme.Pink.Light.ON_SECONDARY,
    secondaryContainer = AppColorScheme.Pink.Light.SECONDARY_CONTAINER,
    onSecondaryContainer = AppColorScheme.Pink.Light.ON_SECONDARY_CONTAINER,
    tertiary = AppColorScheme.Pink.Light.TERTIARY,
    onTertiary = AppColorScheme.Pink.Light.ON_TERTIARY,
    tertiaryContainer = AppColorScheme.Pink.Light.TERTIARY_CONTAINER,
    onTertiaryContainer = AppColorScheme.Pink.Light.ON_TERTIARY_CONTAINER,
    error = AppColorScheme.Pink.Light.ERROR,
    errorContainer = AppColorScheme.Pink.Light.ERROR_CONTAINER,
    onError = AppColorScheme.Pink.Light.ON_ERROR,
    onErrorContainer = AppColorScheme.Pink.Light.ON_ERROR_CONTAINER,
    background = AppColorScheme.Pink.Light.BACKGROUND,
    onBackground = AppColorScheme.Pink.Light.ON_BACKGROUND,
    surface = AppColorScheme.Pink.Light.SURFACE,
    onSurface = AppColorScheme.Pink.Light.ON_SURFACE,
    surfaceVariant = AppColorScheme.Pink.Light.SURFACE_VARIANT,
    onSurfaceVariant = AppColorScheme.Pink.Light.ON_SURFACE_VARIANT,
    outline = AppColorScheme.Pink.Light.OUTLINE,
    inverseOnSurface = AppColorScheme.Pink.Light.INVERSE_ON_SURFACE,
    inverseSurface = AppColorScheme.Pink.Light.INVERSE_SURFACE,
    inversePrimary = AppColorScheme.Pink.Light.INVERSE_PRIMARY,
    surfaceTint = AppColorScheme.Pink.Light.SURFACE_TINT,
)


val DarkPinkColors = darkColorScheme(
    primary = AppColorScheme.Pink.Dark.PRIMARY,
    onPrimary = AppColorScheme.Pink.Dark.ON_PRIMARY,
    primaryContainer = AppColorScheme.Pink.Dark.PRIMARY_CONTAINER,
    onPrimaryContainer = AppColorScheme.Pink.Dark.ON_PRIMARY_CONTAINER,
    secondary = AppColorScheme.Pink.Dark.SECONDARY,
    onSecondary = AppColorScheme.Pink.Dark.ON_SECONDARY,
    secondaryContainer = AppColorScheme.Pink.Dark.SECONDARY_CONTAINER,
    onSecondaryContainer = AppColorScheme.Pink.Dark.ON_SECONDARY_CONTAINER,
    tertiary = AppColorScheme.Pink.Dark.TERTIARY,
    onTertiary = AppColorScheme.Pink.Dark.ON_TERTIARY,
    tertiaryContainer = AppColorScheme.Pink.Dark.TERTIARY_CONTAINER,
    onTertiaryContainer = AppColorScheme.Pink.Dark.ON_TERTIARY_CONTAINER,
    error = AppColorScheme.Pink.Dark.ERROR,
    errorContainer = AppColorScheme.Pink.Dark.ERROR_CONTAINER,
    onError = AppColorScheme.Pink.Dark.ON_ERROR,
    onErrorContainer = AppColorScheme.Pink.Dark.ON_ERROR_CONTAINER,
    background = AppColorScheme.Pink.Dark.BACKGROUND,
    onBackground = AppColorScheme.Pink.Dark.ON_BACKGROUND,
    surface = AppColorScheme.Pink.Dark.SURFACE,
    onSurface = AppColorScheme.Pink.Dark.ON_SURFACE,
    surfaceVariant = AppColorScheme.Pink.Dark.SURFACE_VARIANT,
    onSurfaceVariant = AppColorScheme.Pink.Dark.ON_SURFACE_VARIANT,
    outline = AppColorScheme.Pink.Dark.OUTLINE,
    inverseOnSurface = AppColorScheme.Pink.Dark.INVERSE_ON_SURFACE,
    inverseSurface = AppColorScheme.Pink.Dark.INVERSE_SURFACE,
    inversePrimary = AppColorScheme.Pink.Dark.INVERSE_PRIMARY,
    surfaceTint = AppColorScheme.Pink.Dark.SURFACE_TINT,
)

fun getMochaScheme(accent: Color): ColorScheme {

    return darkColorScheme(
        background = AppColorScheme.CatppuccinMocha.BACKGROUND,
        primary = accent,
        onPrimary = AppColorScheme.CatppuccinMocha.ON_PRIMARY,
        surface = AppColorScheme.CatppuccinMocha.SURFACE,
        onSurface = AppColorScheme.CatppuccinMocha.ON_SURFACE,
        surfaceVariant = AppColorScheme.CatppuccinMocha.SURFACE_VARIANT,
        onSurfaceVariant = AppColorScheme.CatppuccinMocha.ON_SURFACE_VARIANT
    )
}

fun getMacchiatoScheme(accent: Color): ColorScheme {
    return darkColorScheme(
        background = AppColorScheme.CatppuccinMacchiato.BACKGROUND,
        primary = accent,
        onPrimary = AppColorScheme.CatppuccinMacchiato.ON_PRIMARY,
        surface = AppColorScheme.CatppuccinMacchiato.SURFACE,
        onSurface = AppColorScheme.CatppuccinMacchiato.ON_SURFACE,
        surfaceVariant = AppColorScheme.CatppuccinMacchiato.SURFACE_VARIANT,
        onSurfaceVariant = AppColorScheme.CatppuccinMacchiato.ON_SURFACE_VARIANT
    )
}

fun getFrappeScheme(accent: Color): ColorScheme {
    return darkColorScheme(
        background = AppColorScheme.CatppuccinFrappe.BACKGROUND,
        primary = accent,
        onPrimary = AppColorScheme.CatppuccinFrappe.ON_PRIMARY,
        surface = AppColorScheme.CatppuccinFrappe.SURFACE,
        onSurface = AppColorScheme.CatppuccinFrappe.ON_SURFACE,
        surfaceVariant = AppColorScheme.CatppuccinFrappe.SURFACE_VARIANT,
        onSurfaceVariant = AppColorScheme.CatppuccinFrappe.ON_SURFACE_VARIANT
    )
}

fun getLatteScheme(accent: Color): ColorScheme {
    return darkColorScheme(
        background = AppColorScheme.CatppuccinLatte.BACKGROUND,
        primary = accent,
        onPrimary = AppColorScheme.CatppuccinLatte.ON_PRIMARY,
        surface = AppColorScheme.CatppuccinLatte.SURFACE,
        onSurface = AppColorScheme.CatppuccinLatte.ON_SURFACE,
        surfaceVariant = AppColorScheme.CatppuccinLatte.SURFACE_VARIANT,
        onSurfaceVariant = AppColorScheme.CatppuccinLatte.ON_SURFACE_VARIANT
    )
}

@Composable
fun ComposeSimpleMPTheme(
    settingsVM: SettingsVM,
    content: @Composable () -> Unit
) {

    val colorScheme = settingsVM.colorSchemeSetting.collectAsState().value
    val darkColorScheme = settingsVM.darkColorSchemeSetting.collectAsState().value
    val lightColorScheme = settingsVM.lightColorSchemeSetting.collectAsState().value
    val supportsMaterialYou = isAtLeastAndroid12()


    val nightMode = when (colorScheme) {
        Settings.Values.ColorScheme.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        Settings.Values.ColorScheme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
        else -> AppCompatDelegate.MODE_NIGHT_NO
    }

    AppCompatDelegate.setDefaultNightMode(nightMode)

    val context = LocalContext.current

    val useDarkTheme = colorScheme == Settings.Values.ColorScheme.SYSTEM && isSystemInDarkTheme() || colorScheme == Settings.Values.ColorScheme.DARK
    val useLightTheme = colorScheme == Settings.Values.ColorScheme.SYSTEM && !isSystemInDarkTheme() || colorScheme == Settings.Values.ColorScheme.LIGHT

    val colors = when {
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.BLUE -> DarkBlueColors
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.RED -> DarkRedColors
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.PURPLE -> DarkPurpleColors
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.YELLOW -> DarkYellowColors
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.ORANGE -> DarkOrangeColors
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.GREEN -> DarkGreenColors
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.PINK -> DarkPinkColors
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.MOCHA_ROSEWATER -> getMochaScheme(AppColorScheme.CatppuccinMocha.ROSEWATER)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.MOCHA_FLAMINGO -> getMochaScheme(AppColorScheme.CatppuccinMocha.FLAMINGO)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.MOCHA_PINK -> getMochaScheme(AppColorScheme.CatppuccinMocha.PINK)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.MOCHA_MAUVE -> getMochaScheme(AppColorScheme.CatppuccinMocha.MAUVE)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.MOCHA_RED -> getMochaScheme(AppColorScheme.CatppuccinMocha.RED)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.MOCHA_MAROON -> getMochaScheme(AppColorScheme.CatppuccinMocha.MAROON)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.MOCHA_PEACH -> getMochaScheme(AppColorScheme.CatppuccinMocha.PEACH)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.MOCHA_YELLOW -> getMochaScheme(AppColorScheme.CatppuccinMocha.YELLOW)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.MOCHA_GREEN -> getMochaScheme(AppColorScheme.CatppuccinMocha.GREEN)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.MOCHA_TEAL -> getMochaScheme(AppColorScheme.CatppuccinMocha.TEAL)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.MOCHA_SKY -> getMochaScheme(AppColorScheme.CatppuccinMocha.SKY)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.MOCHA_SAPPHIRE -> getMochaScheme(AppColorScheme.CatppuccinMocha.SAPPHIRE)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.MOCHA_BLUE -> getMochaScheme(AppColorScheme.CatppuccinMocha.BLUE)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.MOCHA_LAVENDER -> getMochaScheme(AppColorScheme.CatppuccinMocha.LAVENDER)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.MACCHIATO_ROSEWATER -> getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.ROSEWATER)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.MACCHIATO_FLAMINGO -> getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.FLAMINGO)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.MACCHIATO_PINK -> getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.PINK)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.MACCHIATO_MAUVE -> getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.MAUVE)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.MACCHIATO_RED -> getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.RED)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.MACCHIATO_MAROON -> getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.MAROON)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.MACCHIATO_PEACH -> getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.PEACH)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.MACCHIATO_YELLOW -> getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.YELLOW)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.MACCHIATO_GREEN -> getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.GREEN)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.MACCHIATO_TEAL -> getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.TEAL)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.MACCHIATO_SKY -> getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.SKY)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.MACCHIATO_SAPPHIRE -> getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.SAPPHIRE)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.MACCHIATO_BLUE -> getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.BLUE)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.MACCHIATO_LAVENDER -> getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.LAVENDER)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.FRAPPE_ROSEWATER -> getFrappeScheme(AppColorScheme.CatppuccinFrappe.ROSEWATER)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.FRAPPE_FLAMINGO -> getFrappeScheme(AppColorScheme.CatppuccinFrappe.FLAMINGO)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.FRAPPE_PINK -> getFrappeScheme(AppColorScheme.CatppuccinFrappe.PINK)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.FRAPPE_MAUVE -> getFrappeScheme(AppColorScheme.CatppuccinFrappe.MAUVE)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.FRAPPE_RED -> getFrappeScheme(AppColorScheme.CatppuccinFrappe.RED)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.FRAPPE_MAROON -> getFrappeScheme(AppColorScheme.CatppuccinFrappe.MAROON)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.FRAPPE_PEACH -> getFrappeScheme(AppColorScheme.CatppuccinFrappe.PEACH)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.FRAPPE_YELLOW -> getFrappeScheme(AppColorScheme.CatppuccinFrappe.YELLOW)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.FRAPPE_GREEN -> getFrappeScheme(AppColorScheme.CatppuccinFrappe.GREEN)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.FRAPPE_TEAL -> getFrappeScheme(AppColorScheme.CatppuccinFrappe.TEAL)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.FRAPPE_SKY -> getFrappeScheme(AppColorScheme.CatppuccinFrappe.SKY)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.FRAPPE_SAPPHIRE -> getFrappeScheme(AppColorScheme.CatppuccinFrappe.SAPPHIRE)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.FRAPPE_BLUE -> getFrappeScheme(AppColorScheme.CatppuccinFrappe.BLUE)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.FRAPPE_LAVENDER -> getFrappeScheme(AppColorScheme.CatppuccinFrappe.LAVENDER)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.LATTE_ROSEWATER -> getLatteScheme(AppColorScheme.CatppuccinLatte.ROSEWATER)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.LATTE_FLAMINGO -> getLatteScheme(AppColorScheme.CatppuccinLatte.FLAMINGO)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.LATTE_PINK -> getLatteScheme(AppColorScheme.CatppuccinLatte.PINK)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.LATTE_MAUVE -> getLatteScheme(AppColorScheme.CatppuccinLatte.MAUVE)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.LATTE_RED -> getLatteScheme(AppColorScheme.CatppuccinLatte.RED)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.LATTE_MAROON -> getLatteScheme(AppColorScheme.CatppuccinLatte.MAROON)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.LATTE_PEACH -> getLatteScheme(AppColorScheme.CatppuccinLatte.PEACH)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.LATTE_YELLOW -> getLatteScheme(AppColorScheme.CatppuccinLatte.YELLOW)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.LATTE_GREEN -> getLatteScheme(AppColorScheme.CatppuccinLatte.GREEN)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.LATTE_TEAL -> getLatteScheme(AppColorScheme.CatppuccinLatte.TEAL)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.LATTE_SKY -> getLatteScheme(AppColorScheme.CatppuccinLatte.SKY)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.LATTE_SAPPHIRE -> getLatteScheme(AppColorScheme.CatppuccinLatte.SAPPHIRE)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.LATTE_BLUE -> getLatteScheme(AppColorScheme.CatppuccinLatte.BLUE)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.LATTE_LAVENDER -> getLatteScheme(AppColorScheme.CatppuccinLatte.LAVENDER)
        useDarkTheme && darkColorScheme == Settings.Values.ColorSchemes.MATERIAL_YOU && supportsMaterialYou -> dynamicDarkColorScheme(context)

        useDarkTheme -> DarkBlueColors

        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.BLUE -> LightBlueColors
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.RED -> LightRedColors
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.PURPLE -> LightPurpleColors
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.YELLOW -> LightYellowColors
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.ORANGE -> LightOrangeColors
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.GREEN -> LightGreenColors
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.PINK -> LightPinkColors
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.MOCHA_ROSEWATER -> getMochaScheme(AppColorScheme.CatppuccinMocha.ROSEWATER)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.MOCHA_FLAMINGO -> getMochaScheme(AppColorScheme.CatppuccinMocha.FLAMINGO)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.MOCHA_PINK -> getMochaScheme(AppColorScheme.CatppuccinMocha.PINK)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.MOCHA_MAUVE -> getMochaScheme(AppColorScheme.CatppuccinMocha.MAUVE)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.MOCHA_RED -> getMochaScheme(AppColorScheme.CatppuccinMocha.RED)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.MOCHA_MAROON -> getMochaScheme(AppColorScheme.CatppuccinMocha.MAROON)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.MOCHA_PEACH -> getMochaScheme(AppColorScheme.CatppuccinMocha.PEACH)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.MOCHA_YELLOW -> getMochaScheme(AppColorScheme.CatppuccinMocha.YELLOW)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.MOCHA_GREEN -> getMochaScheme(AppColorScheme.CatppuccinMocha.GREEN)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.MOCHA_TEAL -> getMochaScheme(AppColorScheme.CatppuccinMocha.TEAL)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.MOCHA_SKY -> getMochaScheme(AppColorScheme.CatppuccinMocha.SKY)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.MOCHA_SAPPHIRE -> getMochaScheme(AppColorScheme.CatppuccinMocha.SAPPHIRE)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.MOCHA_BLUE -> getMochaScheme(AppColorScheme.CatppuccinMocha.BLUE)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.MOCHA_LAVENDER -> getMochaScheme(AppColorScheme.CatppuccinMocha.LAVENDER)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.MACCHIATO_ROSEWATER -> getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.ROSEWATER)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.MACCHIATO_FLAMINGO -> getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.FLAMINGO)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.MACCHIATO_PINK -> getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.PINK)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.MACCHIATO_MAUVE -> getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.MAUVE)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.MACCHIATO_RED -> getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.RED)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.MACCHIATO_MAROON -> getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.MAROON)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.MACCHIATO_PEACH -> getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.PEACH)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.MACCHIATO_YELLOW -> getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.YELLOW)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.MACCHIATO_GREEN -> getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.GREEN)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.MACCHIATO_TEAL -> getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.TEAL)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.MACCHIATO_SKY -> getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.SKY)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.MACCHIATO_SAPPHIRE -> getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.SAPPHIRE)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.MACCHIATO_BLUE -> getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.BLUE)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.MACCHIATO_LAVENDER -> getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.LAVENDER)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.FRAPPE_ROSEWATER -> getFrappeScheme(AppColorScheme.CatppuccinFrappe.ROSEWATER)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.FRAPPE_FLAMINGO -> getFrappeScheme(AppColorScheme.CatppuccinFrappe.FLAMINGO)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.FRAPPE_PINK -> getFrappeScheme(AppColorScheme.CatppuccinFrappe.PINK)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.FRAPPE_MAUVE -> getFrappeScheme(AppColorScheme.CatppuccinFrappe.MAUVE)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.FRAPPE_RED -> getFrappeScheme(AppColorScheme.CatppuccinFrappe.RED)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.FRAPPE_MAROON -> getFrappeScheme(AppColorScheme.CatppuccinFrappe.MAROON)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.FRAPPE_PEACH -> getFrappeScheme(AppColorScheme.CatppuccinFrappe.PEACH)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.FRAPPE_YELLOW -> getFrappeScheme(AppColorScheme.CatppuccinFrappe.YELLOW)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.FRAPPE_GREEN -> getFrappeScheme(AppColorScheme.CatppuccinFrappe.GREEN)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.FRAPPE_TEAL -> getFrappeScheme(AppColorScheme.CatppuccinFrappe.TEAL)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.FRAPPE_SKY -> getFrappeScheme(AppColorScheme.CatppuccinFrappe.SKY)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.FRAPPE_SAPPHIRE -> getFrappeScheme(AppColorScheme.CatppuccinFrappe.SAPPHIRE)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.FRAPPE_BLUE -> getFrappeScheme(AppColorScheme.CatppuccinFrappe.BLUE)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.FRAPPE_LAVENDER -> getFrappeScheme(AppColorScheme.CatppuccinFrappe.LAVENDER)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.LATTE_ROSEWATER -> getLatteScheme(AppColorScheme.CatppuccinLatte.ROSEWATER)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.LATTE_FLAMINGO -> getLatteScheme(AppColorScheme.CatppuccinLatte.FLAMINGO)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.LATTE_PINK -> getLatteScheme(AppColorScheme.CatppuccinLatte.PINK)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.LATTE_MAUVE -> getLatteScheme(AppColorScheme.CatppuccinLatte.MAUVE)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.LATTE_RED -> getLatteScheme(AppColorScheme.CatppuccinLatte.RED)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.LATTE_MAROON -> getLatteScheme(AppColorScheme.CatppuccinLatte.MAROON)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.LATTE_PEACH -> getLatteScheme(AppColorScheme.CatppuccinLatte.PEACH)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.LATTE_YELLOW -> getLatteScheme(AppColorScheme.CatppuccinLatte.YELLOW)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.LATTE_GREEN -> getLatteScheme(AppColorScheme.CatppuccinLatte.GREEN)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.LATTE_TEAL -> getLatteScheme(AppColorScheme.CatppuccinLatte.TEAL)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.LATTE_SKY -> getLatteScheme(AppColorScheme.CatppuccinLatte.SKY)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.LATTE_SAPPHIRE -> getLatteScheme(AppColorScheme.CatppuccinLatte.SAPPHIRE)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.LATTE_BLUE -> getLatteScheme(AppColorScheme.CatppuccinLatte.BLUE)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.LATTE_LAVENDER -> getLatteScheme(AppColorScheme.CatppuccinLatte.LAVENDER)
        useLightTheme && lightColorScheme == Settings.Values.ColorSchemes.MATERIAL_YOU && supportsMaterialYou -> dynamicLightColorScheme(context)

        else -> LightBlueColors
    }


    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}