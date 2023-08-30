package com.lighttigerxiv.simple.mp.compose.ui.composables

import android.content.Context
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.data.variables.MEDIUM_SPACING
import com.lighttigerxiv.simple.mp.compose.data.variables.SMALL_SPACING
import com.lighttigerxiv.simple.mp.compose.data.variables.SMALL_TITLE_SIZE
import com.lighttigerxiv.simple.mp.compose.data.variables.Settings
import com.lighttigerxiv.simple.mp.compose.functions.getAppString
import com.lighttigerxiv.simple.mp.compose.functions.isAtLeastAndroid12
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.MediumHorizontalSpacer
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.MediumVerticalSpacer
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.SmallHorizontalSpacer
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.SmallVerticalSpacer
import com.lighttigerxiv.simple.mp.compose.ui.theme.AppColorScheme
import com.lighttigerxiv.simple.mp.compose.ui.theme.DarkBlueColors
import com.lighttigerxiv.simple.mp.compose.ui.theme.DarkGreenColors
import com.lighttigerxiv.simple.mp.compose.ui.theme.DarkOrangeColors
import com.lighttigerxiv.simple.mp.compose.ui.theme.DarkPinkColors
import com.lighttigerxiv.simple.mp.compose.ui.theme.DarkPurpleColors
import com.lighttigerxiv.simple.mp.compose.ui.theme.DarkRedColors
import com.lighttigerxiv.simple.mp.compose.ui.theme.DarkYellowColors
import com.lighttigerxiv.simple.mp.compose.ui.theme.LightBlueColors
import com.lighttigerxiv.simple.mp.compose.ui.theme.LightGreenColors
import com.lighttigerxiv.simple.mp.compose.ui.theme.LightOrangeColors
import com.lighttigerxiv.simple.mp.compose.ui.theme.LightPinkColors
import com.lighttigerxiv.simple.mp.compose.ui.theme.LightPurpleColors
import com.lighttigerxiv.simple.mp.compose.ui.theme.LightRedColors
import com.lighttigerxiv.simple.mp.compose.ui.theme.LightYellowColors
import com.lighttigerxiv.simple.mp.compose.ui.theme.getFrappeScheme
import com.lighttigerxiv.simple.mp.compose.ui.theme.getLatteScheme
import com.lighttigerxiv.simple.mp.compose.ui.theme.getMacchiatoScheme
import com.lighttigerxiv.simple.mp.compose.ui.theme.getMochaScheme


@Composable
fun ThemeSelector(
    selectedTheme: String,
    onClick: (theme: String) -> Unit
) {

    val context = LocalContext.current
    val inDarkMode = isSystemInDarkTheme()
    val showCommonThemes = remember { mutableStateOf(true) }
    val showCatppuccinThemes = remember { mutableStateOf(true) }
    val commonThemes = remember { getCommonColorSchemes(inDarkMode, context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (isAtLeastAndroid12()) {

                val scheme = Scheme(
                    name = getAppString(context, R.string.MaterialYou),
                    colorScheme = if(isSystemInDarkTheme()) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context),
                    setting = Settings.Values.ColorSchemes.MATERIAL_YOU
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(MEDIUM_SPACING)
                ){
                    ThemePreview(
                        selectedTheme = selectedTheme,
                        scheme = scheme,
                        onClick = { onClick(scheme.setting) }
                    )
                }

            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(MEDIUM_SPACING)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                CustomText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { showCommonThemes.value = !showCommonThemes.value },
                    text = remember { getAppString(context, R.string.Common) },
                    size = 24.sp,
                    weight = FontWeight.Bold
                )
            }


            if (showCommonThemes.value) {

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow {
                    items(items = commonThemes, key = { it.setting }) { theme ->

                        ThemePreview(selectedTheme = selectedTheme, scheme = theme, onClick = { onClick(theme.setting) })
                        SmallHorizontalSpacer()
                    }
                }

                SmallVerticalSpacer()
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                CustomText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { showCatppuccinThemes.value = !showCatppuccinThemes.value },
                    text = "Catppuccin",
                    size = 24.sp,
                    weight = FontWeight.Bold
                )
            }

            if (showCatppuccinThemes.value) {

                MediumVerticalSpacer()

                Text(text = "Latte", fontSize = SMALL_TITLE_SIZE, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)

                LazyRow {
                    items(items = getCatppuccinLatteColorSchemes(), key = { it.setting }) { theme ->

                        ThemePreview(selectedTheme = selectedTheme, scheme = theme, onClick = { onClick(theme.setting) })
                        SmallHorizontalSpacer()
                    }
                }

                SmallVerticalSpacer()

                Text(text = "Frappe", fontSize = SMALL_TITLE_SIZE, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)

                LazyRow {
                    items(items = getCatppuccinFrappeColorSchemes(), key = { it.setting }) { theme ->

                        ThemePreview(selectedTheme = selectedTheme, scheme = theme, onClick = { onClick(theme.setting) })
                        SmallHorizontalSpacer()
                    }
                }

                SmallVerticalSpacer()

                Text(text = "Macchiato", fontSize = SMALL_TITLE_SIZE, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)

                LazyRow {
                    items(items = getCatppuccinMacchiatoColorSchemes(), key = { it.setting }) { theme ->

                        ThemePreview(selectedTheme = selectedTheme, scheme = theme, onClick = { onClick(theme.setting) })
                        SmallHorizontalSpacer()
                    }
                }

                SmallVerticalSpacer()

                Text(text = "Mocha", fontSize = SMALL_TITLE_SIZE, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)

                LazyRow {
                    items(items = getCatppuccinMochaColorSchemes(), key = { it.setting }) { theme ->

                        ThemePreview(selectedTheme = selectedTheme, scheme = theme, onClick = { onClick(theme.setting) })
                        SmallHorizontalSpacer()
                    }
                }
            }
        }
    }
}

@Composable
fun ThemePreview(selectedTheme: String, scheme: Scheme, onClick: () -> Unit) {

    Column(
        modifier = Modifier
            .width(IntrinsicSize.Max)
            .clip(RoundedCornerShape(14.dp))
            .background(scheme.colorScheme.surface)
            .clickable { onClick() }
            .padding(MEDIUM_SPACING)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(scheme.colorScheme.surfaceVariant)
                    .padding(SMALL_SPACING)
            ) {
                Icon(
                    modifier = Modifier
                        .height(60.dp)
                        .width(60.dp),
                    painter = painterResource(id = R.drawable.cd),
                    contentDescription = null,
                    tint = scheme.colorScheme.primary
                )
            }

            MediumHorizontalSpacer()

            Column {
                Row {
                    Text(text = "Abc", color = scheme.colorScheme.onSurface, fontSize = 20.sp)

                    MediumHorizontalSpacer()

                    Text(text = "Abc", color = scheme.colorScheme.primary, fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row {
                    Text(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(scheme.colorScheme.surfaceVariant)
                            .padding(4.dp),
                        text = "Abc",
                        color = scheme.colorScheme.onSurfaceVariant,
                        fontSize = 20.sp
                    )

                    MediumHorizontalSpacer()

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(scheme.colorScheme.primary)
                            .padding(SMALL_SPACING)
                    ) {
                        Icon(
                            modifier = Modifier
                                .height(20.dp)
                                .width(20.dp),
                            painter = painterResource(id = R.drawable.shuffle),
                            contentDescription = null,
                            tint = scheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }

        SmallVerticalSpacer()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(scheme.colorScheme.surfaceVariant),
            verticalAlignment = Alignment.CenterVertically
        ) {

            RadioButton(
                selected = selectedTheme == scheme.setting,
                onClick = { onClick() },
                colors = RadioButtonDefaults.colors(
                    selectedColor = scheme.colorScheme.primary,
                    unselectedColor = scheme.colorScheme.onSurface
                )
            )

            Text(text = scheme.name, color = scheme.colorScheme.onSurface)
        }
    }
}

data class Scheme(
    val name: String,
    val colorScheme: ColorScheme,
    val setting: String
)


fun getCommonColorSchemes(inDarkMode: Boolean, context: Context): List<Scheme> {

    return if (inDarkMode) {
        listOf(
            Scheme(
                getAppString(context, R.string.Blue),
                DarkBlueColors,
                Settings.Values.ColorSchemes.BLUE
            ),
            Scheme(
                getAppString(context, R.string.Red),
                DarkRedColors,
                Settings.Values.ColorSchemes.RED
            ),
            Scheme(
                getAppString(context, R.string.Purple),
                DarkPurpleColors,
                Settings.Values.ColorSchemes.PURPLE
            ),
            Scheme(
                getAppString(context, R.string.Orange),
                DarkOrangeColors,
                Settings.Values.ColorSchemes.ORANGE
            ),
            Scheme(
                getAppString(context, R.string.Yellow),
                DarkYellowColors,
                Settings.Values.ColorSchemes.YELLOW
            ),
            Scheme(
                getAppString(context, R.string.Green),
                DarkGreenColors,
                Settings.Values.ColorSchemes.GREEN
            ),
            Scheme(
                getAppString(context, R.string.Pink),
                DarkPinkColors,
                Settings.Values.ColorSchemes.PINK
            )
        )
    } else {
        listOf(
            Scheme(
                getAppString(context, R.string.Blue),
                LightBlueColors,
                Settings.Values.ColorSchemes.BLUE
            ),
            Scheme(
                getAppString(context, R.string.Red),
                LightRedColors,
                Settings.Values.ColorSchemes.RED
            ),
            Scheme(
                getAppString(context, R.string.Purple),
                LightPurpleColors,
                Settings.Values.ColorSchemes.PURPLE
            ),
            Scheme(
                getAppString(context, R.string.Orange),
                LightOrangeColors,
                Settings.Values.ColorSchemes.ORANGE
            ),
            Scheme(
                getAppString(context, R.string.Yellow),
                LightYellowColors,
                Settings.Values.ColorSchemes.YELLOW
            ),
            Scheme(
                getAppString(context, R.string.Green),
                LightGreenColors,
                Settings.Values.ColorSchemes.GREEN
            ),
            Scheme(
                getAppString(context, R.string.Pink),
                LightPinkColors,
                Settings.Values.ColorSchemes.PINK
            )
        )
    }
}

fun getCatppuccinLatteColorSchemes(): List<Scheme> {
    return listOf(
        Scheme(
            "Rosewater",
            getLatteScheme(AppColorScheme.CatppuccinLatte.ROSEWATER),
            Settings.Values.ColorSchemes.LATTE_ROSEWATER
        ),
        Scheme(
            "Flamingo",
            getLatteScheme(AppColorScheme.CatppuccinLatte.FLAMINGO),
            Settings.Values.ColorSchemes.LATTE_FLAMINGO
        ),
        Scheme(
            "Pink",
            getLatteScheme(AppColorScheme.CatppuccinLatte.PINK),
            Settings.Values.ColorSchemes.LATTE_PINK
        ),
        Scheme(
            "Mauve",
            getLatteScheme(AppColorScheme.CatppuccinLatte.MAUVE),
            Settings.Values.ColorSchemes.LATTE_MAUVE
        ),
        Scheme(
            "Red",
            getLatteScheme(AppColorScheme.CatppuccinLatte.RED),
            Settings.Values.ColorSchemes.LATTE_RED
        ),
        Scheme(
            "Maroon",
            getLatteScheme(AppColorScheme.CatppuccinLatte.MAROON),
            Settings.Values.ColorSchemes.LATTE_MAROON
        ),
        Scheme(
            "Peach",
            getLatteScheme(AppColorScheme.CatppuccinLatte.PEACH),
            Settings.Values.ColorSchemes.LATTE_PEACH
        ),
        Scheme(
            "Yellow",
            getLatteScheme(AppColorScheme.CatppuccinLatte.YELLOW),
            Settings.Values.ColorSchemes.LATTE_YELLOW
        ),
        Scheme(
            "Green",
            getLatteScheme(AppColorScheme.CatppuccinLatte.GREEN),
            Settings.Values.ColorSchemes.LATTE_GREEN
        ),
        Scheme(
            "Teal",
            getLatteScheme(AppColorScheme.CatppuccinLatte.TEAL),
            Settings.Values.ColorSchemes.LATTE_TEAL
        ),
        Scheme(
            "Sky",
            getLatteScheme(AppColorScheme.CatppuccinLatte.SKY),
            Settings.Values.ColorSchemes.LATTE_SKY
        ),
        Scheme(
            "Sapphire",
            getLatteScheme(AppColorScheme.CatppuccinLatte.SAPPHIRE),
            Settings.Values.ColorSchemes.LATTE_SAPPHIRE
        ),
        Scheme(
            "Blue",
            getLatteScheme(AppColorScheme.CatppuccinLatte.BLUE),
            Settings.Values.ColorSchemes.LATTE_BLUE
        ),
        Scheme(
            "Lavender",
            getLatteScheme(AppColorScheme.CatppuccinLatte.LAVENDER),
            Settings.Values.ColorSchemes.LATTE_LAVENDER
        )
    )
}

fun getCatppuccinFrappeColorSchemes(): List<Scheme> {
    return listOf(
        Scheme(
            "Rosewater",
            getFrappeScheme(AppColorScheme.CatppuccinFrappe.ROSEWATER),
            Settings.Values.ColorSchemes.FRAPPE_ROSEWATER
        ),
        Scheme(
            "Flamingo",
            getFrappeScheme(AppColorScheme.CatppuccinFrappe.FLAMINGO),
            Settings.Values.ColorSchemes.FRAPPE_FLAMINGO
        ),
        Scheme(
            "Pink",
            getFrappeScheme(AppColorScheme.CatppuccinFrappe.PINK),
            Settings.Values.ColorSchemes.FRAPPE_PINK
        ),
        Scheme(
            "Mauve",
            getFrappeScheme(AppColorScheme.CatppuccinFrappe.MAUVE),
            Settings.Values.ColorSchemes.FRAPPE_MAUVE
        ),
        Scheme(
            "Red",
            getFrappeScheme(AppColorScheme.CatppuccinFrappe.RED),
            Settings.Values.ColorSchemes.FRAPPE_RED
        ),
        Scheme(
            "Maroon",
            getFrappeScheme(AppColorScheme.CatppuccinFrappe.MAROON),
            Settings.Values.ColorSchemes.FRAPPE_MAROON
        ),
        Scheme(
            "Peach",
            getFrappeScheme(AppColorScheme.CatppuccinFrappe.PEACH),
            Settings.Values.ColorSchemes.FRAPPE_PEACH
        ),
        Scheme(
            "Yellow",
            getFrappeScheme(AppColorScheme.CatppuccinFrappe.YELLOW),
            Settings.Values.ColorSchemes.FRAPPE_YELLOW
        ),
        Scheme(
            "Green",
            getFrappeScheme(AppColorScheme.CatppuccinFrappe.GREEN),
            Settings.Values.ColorSchemes.FRAPPE_GREEN
        ),
        Scheme(
            "Teal",
            getFrappeScheme(AppColorScheme.CatppuccinFrappe.TEAL),
            Settings.Values.ColorSchemes.FRAPPE_TEAL
        ),
        Scheme(
            "Sky",
            getFrappeScheme(AppColorScheme.CatppuccinFrappe.SKY),
            Settings.Values.ColorSchemes.FRAPPE_SKY
        ),
        Scheme(
            "Sapphire",
            getFrappeScheme(AppColorScheme.CatppuccinFrappe.SAPPHIRE),
            Settings.Values.ColorSchemes.FRAPPE_SAPPHIRE
        ),
        Scheme(
            "Blue",
            getFrappeScheme(AppColorScheme.CatppuccinFrappe.BLUE),
            Settings.Values.ColorSchemes.FRAPPE_BLUE
        ),
        Scheme(
            "Lavender",
            getFrappeScheme(AppColorScheme.CatppuccinFrappe.LAVENDER),
            Settings.Values.ColorSchemes.FRAPPE_LAVENDER
        )
    )
}

fun getCatppuccinMacchiatoColorSchemes(): List<Scheme> {
    return listOf(
        Scheme(
            "Rosewater",
            getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.ROSEWATER),
            Settings.Values.ColorSchemes.MACCHIATO_ROSEWATER
        ),
        Scheme(
            "Flamingo",
            getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.FLAMINGO),
            Settings.Values.ColorSchemes.MACCHIATO_FLAMINGO
        ),
        Scheme(
            "Pink",
            getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.PINK),
            Settings.Values.ColorSchemes.MACCHIATO_PINK
        ),
        Scheme(
            "Mauve",
            getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.MAUVE),
            Settings.Values.ColorSchemes.MACCHIATO_MAUVE
        ),
        Scheme(
            "Red",
            getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.RED),
            Settings.Values.ColorSchemes.MACCHIATO_RED
        ),
        Scheme(
            "Maroon",
            getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.MAROON),
            Settings.Values.ColorSchemes.MACCHIATO_MAROON
        ),
        Scheme(
            "Peach",
            getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.PEACH),
            Settings.Values.ColorSchemes.MACCHIATO_PEACH
        ),
        Scheme(
            "Yellow",
            getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.YELLOW),
            Settings.Values.ColorSchemes.MACCHIATO_YELLOW
        ),
        Scheme(
            "Green",
            getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.GREEN),
            Settings.Values.ColorSchemes.MACCHIATO_GREEN
        ),
        Scheme(
            "Teal",
            getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.TEAL),
            Settings.Values.ColorSchemes.MACCHIATO_TEAL
        ),
        Scheme(
            "Sky",
            getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.SKY),
            Settings.Values.ColorSchemes.MACCHIATO_SKY
        ),
        Scheme(
            "Sapphire",
            getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.SAPPHIRE),
            Settings.Values.ColorSchemes.MACCHIATO_SAPPHIRE
        ),
        Scheme(
            "Blue",
            getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.BLUE),
            Settings.Values.ColorSchemes.MACCHIATO_BLUE
        ),
        Scheme(
            "Lavender",
            getMacchiatoScheme(AppColorScheme.CatppuccinMacchiato.LAVENDER),
            Settings.Values.ColorSchemes.MACCHIATO_LAVENDER
        )
    )
}

fun getCatppuccinMochaColorSchemes(): List<Scheme> {
    return listOf(
        Scheme(
            "Rosewater",
            getMochaScheme(AppColorScheme.CatppuccinMocha.ROSEWATER),
            Settings.Values.ColorSchemes.MOCHA_ROSEWATER
        ),
        Scheme(
            "Flamingo",
            getMochaScheme(AppColorScheme.CatppuccinMocha.FLAMINGO),
            Settings.Values.ColorSchemes.MOCHA_FLAMINGO
        ),
        Scheme(
            "Pink",
            getMochaScheme(AppColorScheme.CatppuccinMocha.PINK),
            Settings.Values.ColorSchemes.MOCHA_PINK
        ),
        Scheme(
            "Mauve",
            getMochaScheme(AppColorScheme.CatppuccinMocha.MAUVE),
            Settings.Values.ColorSchemes.MOCHA_MAUVE
        ),
        Scheme(
            "Red",
            getMochaScheme(AppColorScheme.CatppuccinMocha.RED),
            Settings.Values.ColorSchemes.MOCHA_RED
        ),
        Scheme(
            "Maroon",
            getMochaScheme(AppColorScheme.CatppuccinMocha.MAROON),
            Settings.Values.ColorSchemes.MOCHA_MAROON
        ),
        Scheme(
            "Peach",
            getMochaScheme(AppColorScheme.CatppuccinMocha.PEACH),
            Settings.Values.ColorSchemes.MOCHA_PEACH
        ),
        Scheme(
            "Yellow",
            getMochaScheme(AppColorScheme.CatppuccinMocha.YELLOW),
            Settings.Values.ColorSchemes.MOCHA_YELLOW
        ),
        Scheme(
            "Green",
            getMochaScheme(AppColorScheme.CatppuccinMocha.GREEN),
            Settings.Values.ColorSchemes.MOCHA_GREEN
        ),
        Scheme(
            "Teal",
            getMochaScheme(AppColorScheme.CatppuccinMocha.TEAL),
            Settings.Values.ColorSchemes.MOCHA_TEAL
        ),
        Scheme(
            "Sky",
            getMochaScheme(AppColorScheme.CatppuccinMocha.SKY),
            Settings.Values.ColorSchemes.MOCHA_SKY
        ),
        Scheme(
            "Sapphire",
            getMochaScheme(AppColorScheme.CatppuccinMocha.SAPPHIRE),
            Settings.Values.ColorSchemes.MOCHA_SAPPHIRE
        ),
        Scheme(
            "Blue",
            getMochaScheme(AppColorScheme.CatppuccinMocha.BLUE),
            Settings.Values.ColorSchemes.MOCHA_BLUE
        ),
        Scheme(
            "Lavender",
            getMochaScheme(AppColorScheme.CatppuccinMocha.LAVENDER),
            Settings.Values.ColorSchemes.MOCHA_LAVENDER
        )
    )
}