package com.lighttigerxiv.simple.mp.compose.frontend.composables

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.backend.settings.SettingsOptions
import com.lighttigerxiv.simple.mp.compose.backend.utils.isAtLeastAndroid12
import com.lighttigerxiv.simple.mp.compose.frontend.theme.AppTheme
import com.lighttigerxiv.simple.mp.compose.frontend.theme.DarkBlueColors
import com.lighttigerxiv.simple.mp.compose.frontend.theme.DarkGreenColors
import com.lighttigerxiv.simple.mp.compose.frontend.theme.DarkOrangeColors
import com.lighttigerxiv.simple.mp.compose.frontend.theme.DarkPinkColors
import com.lighttigerxiv.simple.mp.compose.frontend.theme.DarkPurpleColors
import com.lighttigerxiv.simple.mp.compose.frontend.theme.DarkRedColors
import com.lighttigerxiv.simple.mp.compose.frontend.theme.DarkYellowColors
import com.lighttigerxiv.simple.mp.compose.frontend.theme.LightBlueColors
import com.lighttigerxiv.simple.mp.compose.frontend.theme.LightGreenColors
import com.lighttigerxiv.simple.mp.compose.frontend.theme.LightOrangeColors
import com.lighttigerxiv.simple.mp.compose.frontend.theme.LightPinkColors
import com.lighttigerxiv.simple.mp.compose.frontend.theme.LightPurpleColors
import com.lighttigerxiv.simple.mp.compose.frontend.theme.LightRedColors
import com.lighttigerxiv.simple.mp.compose.frontend.theme.LightYellowColors
import com.lighttigerxiv.simple.mp.compose.frontend.theme.getFrappeScheme
import com.lighttigerxiv.simple.mp.compose.frontend.theme.getLatteScheme
import com.lighttigerxiv.simple.mp.compose.frontend.theme.getMacchiatoScheme
import com.lighttigerxiv.simple.mp.compose.frontend.theme.getMochaScheme
import com.lighttigerxiv.simple.mp.compose.frontend.utils.FontSizes
import com.lighttigerxiv.simple.mp.compose.frontend.utils.Sizes

@Composable
fun ThemeSelector(
    selectedTheme: String,
    onThemeSelected: (theme: String) -> Unit
) {

    val context = LocalContext.current
    val inDarkMode = isSystemInDarkTheme()
    val showCommonThemes = remember { mutableStateOf(true) }
    val showCatppuccinThemes = remember { mutableStateOf(true) }
    val commonThemes = remember{ getCommonColorSchemes(inDarkMode, context) }

    
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

                val theme = Theme(
                    name = stringResource(id = R.string.MaterialYou),
                    colorScheme = if(isSystemInDarkTheme()) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context),
                    setting = SettingsOptions.Themes.MATERIAL_YOU
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(Sizes.MEDIUM)
                ){
                    ThemePreview(
                        selectedTheme = selectedTheme,
                        theme = theme,
                        onClick = { onThemeSelected(theme.setting) }
                    )
                }

            }
        }

        VSpacer(size = Sizes.LARGE)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(Sizes.MEDIUM)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { showCommonThemes.value = !showCommonThemes.value },
                    text = stringResource(id = R.string.Common),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }


            if (showCommonThemes.value) {

                LazyRow {
                    items(items = commonThemes, key = { it.setting }) { theme ->

                        ThemePreview(selectedTheme = selectedTheme, theme = theme, onClick = { onThemeSelected(theme.setting) })

                        HSpacer(size = Sizes.SMALL)
                    }
                }
            }
        }

        VSpacer(size = Sizes.LARGE)

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

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { showCatppuccinThemes.value = !showCatppuccinThemes.value },
                    text = "Catppuccin",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (showCatppuccinThemes.value) {

                Text(text = "Latte", fontSize = FontSizes.HEADER_2, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)

                LazyRow {
                    items(items = getCatppuccinLatteColorSchemes(), key = { it.setting }) { theme ->

                        ThemePreview(selectedTheme = selectedTheme, theme = theme, onClick = { onThemeSelected(theme.setting) })

                        HSpacer(size = Sizes.SMALL)
                    }
                }

                VSpacer(size = Sizes.SMALL)

                Text(text = "Frappe", fontSize = FontSizes.HEADER_2, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)

                LazyRow {
                    items(items = getCatppuccinFrappeColorSchemes(), key = { it.setting }) { theme ->

                        ThemePreview(selectedTheme = selectedTheme, theme = theme, onClick = { onThemeSelected(theme.setting) })

                        HSpacer(size = Sizes.SMALL)
                    }
                }

                VSpacer(size = Sizes.SMALL)

                Text(text = "Macchiato", fontSize = FontSizes.HEADER_2, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)

                LazyRow {
                    items(items = getCatppuccinMacchiatoColorSchemes(), key = { it.setting }) { theme ->

                        ThemePreview(selectedTheme = selectedTheme, theme = theme, onClick = { onThemeSelected(theme.setting) })

                        HSpacer(size = Sizes.SMALL)
                    }
                }

                VSpacer(size = Sizes.SMALL)

                Text(text = "Mocha", fontSize = FontSizes.HEADER_2, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)

                LazyRow {
                    items(items = getCatppuccinMochaColorSchemes(), key = { it.setting }) { theme ->

                        ThemePreview(selectedTheme = selectedTheme, theme = theme, onClick = { onThemeSelected(theme.setting) })

                        HSpacer(size = Sizes.SMALL)
                    }
                }
            }
        }
    }
}

@Composable
fun ThemePreview(selectedTheme: String, theme: Theme, onClick: () -> Unit) {

    Column(
        modifier = Modifier
            .width(IntrinsicSize.Max)
            .clip(RoundedCornerShape(14.dp))
            .background(theme.colorScheme.surface)
            .clickable { onClick() }
            .padding(Sizes.MEDIUM)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(theme.colorScheme.surfaceVariant)
                    .padding(Sizes.SMALL)
            ) {
                Icon(
                    modifier = Modifier
                        .height(60.dp)
                        .width(60.dp),
                    painter = painterResource(id = R.drawable.album),
                    contentDescription = null,
                    tint = theme.colorScheme.primary
                )
            }

            HSpacer(size = Sizes.SMALL)

            Column {
                Row {
                    Text(text = "Abc", color = theme.colorScheme.onSurface, fontSize = 20.sp)

                    HSpacer(size = Sizes.SMALL)

                    Text(text = "Abc", color = theme.colorScheme.primary, fontSize = 20.sp)
                }

                Row {
                    Text(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(theme.colorScheme.surfaceVariant)
                            .padding(4.dp),
                        text = "Abc",
                        color = theme.colorScheme.onSurfaceVariant,
                        fontSize = 20.sp
                    )

                    HSpacer(size = Sizes.SMALL)

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(theme.colorScheme.primary)
                            .padding(Sizes.SMALL)
                    ) {
                        Icon(
                            modifier = Modifier
                                .height(20.dp)
                                .width(20.dp),
                            painter = painterResource(id = R.drawable.shuffle),
                            contentDescription = null,
                            tint = theme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }

        VSpacer(size = Sizes.SMALL)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(theme.colorScheme.surfaceVariant),
            verticalAlignment = Alignment.CenterVertically
        ) {

            RadioButton(
                selected = selectedTheme == theme.setting,
                onClick = { onClick() },
                colors = RadioButtonDefaults.colors(
                    selectedColor = theme.colorScheme.primary,
                    unselectedColor = theme.colorScheme.onSurface
                )
            )

            Text(text = theme.name, color = theme.colorScheme.onSurface)
        }
    }
}

data class Theme(
    val name: String,
    val colorScheme: ColorScheme,
    val setting: String
)

fun getCommonColorSchemes(inDarkMode: Boolean, context: Context): List<Theme> {

    return if (inDarkMode) {
        listOf(
            Theme(
                context.getString(R.string.blue),
                DarkBlueColors,
                SettingsOptions.Themes.BLUE
            ),
            Theme(
                context.getString(R.string.red),
                DarkRedColors,
                SettingsOptions.Themes.RED
            ),
            Theme(
                context.getString(R.string.purple),
                DarkPurpleColors,
                SettingsOptions.Themes.PURPLE
            ),
            Theme(
                context.getString(R.string.orange),
                DarkOrangeColors,
                SettingsOptions.Themes.ORANGE
            ),
            Theme(
                context.getString(R.string.yellow),
                DarkYellowColors,
                SettingsOptions.Themes.YELLOW
            ),
            Theme(
                context.getString(R.string.green),
                DarkGreenColors,
                SettingsOptions.Themes.GREEN
            ),
            Theme(
                context.getString(R.string.pink),
                DarkPinkColors,
                SettingsOptions.Themes.PINK
            )
        )
    } else {
        listOf(
            Theme(
                context.getString(R.string.blue),
                LightBlueColors,
                SettingsOptions.Themes.BLUE
            ),
            Theme(
                context.getString(R.string.red),
                LightRedColors,
                SettingsOptions.Themes.RED
            ),
            Theme(
                context.getString(R.string.purple),
                LightPurpleColors,
                SettingsOptions.Themes.PURPLE
            ),
            Theme(
                context.getString(R.string.orange),
                LightOrangeColors,
                SettingsOptions.Themes.ORANGE
            ),
            Theme(
                context.getString(R.string.yellow),
                LightYellowColors,
                SettingsOptions.Themes.YELLOW
            ),
            Theme(
                context.getString(R.string.green),
                LightGreenColors,
                SettingsOptions.Themes.GREEN
            ),
            Theme(
                context.getString(R.string.pink),
                LightPinkColors,
                SettingsOptions.Themes.PINK
            )
        )
    }
}

fun getCatppuccinLatteColorSchemes(): List<Theme> {
    return listOf(
        Theme(
            "Rosewater",
            getLatteScheme(AppTheme.CatppuccinLatte.ROSEWATER),
            SettingsOptions.Themes.LATTE_ROSEWATER
        ),
        Theme(
            "Flamingo",
            getLatteScheme(AppTheme.CatppuccinLatte.FLAMINGO),
            SettingsOptions.Themes.LATTE_FLAMINGO
        ),
        Theme(
            "Pink",
            getLatteScheme(AppTheme.CatppuccinLatte.PINK),
            SettingsOptions.Themes.LATTE_PINK
        ),
        Theme(
            "Mauve",
            getLatteScheme(AppTheme.CatppuccinLatte.MAUVE),
            SettingsOptions.Themes.LATTE_MAUVE
        ),
        Theme(
            "Red",
            getLatteScheme(AppTheme.CatppuccinLatte.RED),
            SettingsOptions.Themes.LATTE_RED
        ),
        Theme(
            "Maroon",
            getLatteScheme(AppTheme.CatppuccinLatte.MAROON),
            SettingsOptions.Themes.LATTE_MAROON
        ),
        Theme(
            "Peach",
            getLatteScheme(AppTheme.CatppuccinLatte.PEACH),
            SettingsOptions.Themes.LATTE_PEACH
        ),
        Theme(
            "Yellow",
            getLatteScheme(AppTheme.CatppuccinLatte.YELLOW),
            SettingsOptions.Themes.LATTE_YELLOW
        ),
        Theme(
            "Green",
            getLatteScheme(AppTheme.CatppuccinLatte.GREEN),
            SettingsOptions.Themes.LATTE_GREEN
        ),
        Theme(
            "Teal",
            getLatteScheme(AppTheme.CatppuccinLatte.TEAL),
            SettingsOptions.Themes.LATTE_TEAL
        ),
        Theme(
            "Sky",
            getLatteScheme(AppTheme.CatppuccinLatte.SKY),
            SettingsOptions.Themes.LATTE_SKY
        ),
        Theme(
            "Sapphire",
            getLatteScheme(AppTheme.CatppuccinLatte.SAPPHIRE),
            SettingsOptions.Themes.LATTE_SAPPHIRE
        ),
        Theme(
            "Blue",
            getLatteScheme(AppTheme.CatppuccinLatte.BLUE),
            SettingsOptions.Themes.LATTE_BLUE
        ),
        Theme(
            "Lavender",
            getLatteScheme(AppTheme.CatppuccinLatte.LAVENDER),
            SettingsOptions.Themes.LATTE_LAVENDER
        )
    )
}

fun getCatppuccinFrappeColorSchemes(): List<Theme> {
    return listOf(
        Theme(
            "Rosewater",
            getFrappeScheme(AppTheme.CatppuccinFrappe.ROSEWATER),
            SettingsOptions.Themes.FRAPPE_ROSEWATER
        ),
        Theme(
            "Flamingo",
            getFrappeScheme(AppTheme.CatppuccinFrappe.FLAMINGO),
            SettingsOptions.Themes.FRAPPE_FLAMINGO
        ),
        Theme(
            "Pink",
            getFrappeScheme(AppTheme.CatppuccinFrappe.PINK),
            SettingsOptions.Themes.FRAPPE_PINK
        ),
        Theme(
            "Mauve",
            getFrappeScheme(AppTheme.CatppuccinFrappe.MAUVE),
            SettingsOptions.Themes.FRAPPE_MAUVE
        ),
        Theme(
            "Red",
            getFrappeScheme(AppTheme.CatppuccinFrappe.RED),
            SettingsOptions.Themes.FRAPPE_RED
        ),
        Theme(
            "Maroon",
            getFrappeScheme(AppTheme.CatppuccinFrappe.MAROON),
            SettingsOptions.Themes.FRAPPE_MAROON
        ),
        Theme(
            "Peach",
            getFrappeScheme(AppTheme.CatppuccinFrappe.PEACH),
            SettingsOptions.Themes.FRAPPE_PEACH
        ),
        Theme(
            "Yellow",
            getFrappeScheme(AppTheme.CatppuccinFrappe.YELLOW),
            SettingsOptions.Themes.FRAPPE_YELLOW
        ),
        Theme(
            "Green",
            getFrappeScheme(AppTheme.CatppuccinFrappe.GREEN),
            SettingsOptions.Themes.FRAPPE_GREEN
        ),
        Theme(
            "Teal",
            getFrappeScheme(AppTheme.CatppuccinFrappe.TEAL),
            SettingsOptions.Themes.FRAPPE_TEAL
        ),
        Theme(
            "Sky",
            getFrappeScheme(AppTheme.CatppuccinFrappe.SKY),
            SettingsOptions.Themes.FRAPPE_SKY
        ),
        Theme(
            "Sapphire",
            getFrappeScheme(AppTheme.CatppuccinFrappe.SAPPHIRE),
            SettingsOptions.Themes.FRAPPE_SAPPHIRE
        ),
        Theme(
            "Blue",
            getFrappeScheme(AppTheme.CatppuccinFrappe.BLUE),
            SettingsOptions.Themes.FRAPPE_BLUE
        ),
        Theme(
            "Lavender",
            getFrappeScheme(AppTheme.CatppuccinFrappe.LAVENDER),
            SettingsOptions.Themes.FRAPPE_LAVENDER
        )
    )
}

fun getCatppuccinMacchiatoColorSchemes(): List<Theme> {
    return listOf(
        Theme(
            "Rosewater",
            getMacchiatoScheme(AppTheme.CatppuccinMacchiato.ROSEWATER),
            SettingsOptions.Themes.MACCHIATO_ROSEWATER
        ),
        Theme(
            "Flamingo",
            getMacchiatoScheme(AppTheme.CatppuccinMacchiato.FLAMINGO),
            SettingsOptions.Themes.MACCHIATO_FLAMINGO
        ),
        Theme(
            "Pink",
            getMacchiatoScheme(AppTheme.CatppuccinMacchiato.PINK),
            SettingsOptions.Themes.MACCHIATO_PINK
        ),
        Theme(
            "Mauve",
            getMacchiatoScheme(AppTheme.CatppuccinMacchiato.MAUVE),
            SettingsOptions.Themes.MACCHIATO_MAUVE
        ),
        Theme(
            "Red",
            getMacchiatoScheme(AppTheme.CatppuccinMacchiato.RED),
            SettingsOptions.Themes.MACCHIATO_RED
        ),
        Theme(
            "Maroon",
            getMacchiatoScheme(AppTheme.CatppuccinMacchiato.MAROON),
            SettingsOptions.Themes.MACCHIATO_MAROON
        ),
        Theme(
            "Peach",
            getMacchiatoScheme(AppTheme.CatppuccinMacchiato.PEACH),
            SettingsOptions.Themes.MACCHIATO_PEACH
        ),
        Theme(
            "Yellow",
            getMacchiatoScheme(AppTheme.CatppuccinMacchiato.YELLOW),
            SettingsOptions.Themes.MACCHIATO_YELLOW
        ),
        Theme(
            "Green",
            getMacchiatoScheme(AppTheme.CatppuccinMacchiato.GREEN),
            SettingsOptions.Themes.MACCHIATO_GREEN
        ),
        Theme(
            "Teal",
            getMacchiatoScheme(AppTheme.CatppuccinMacchiato.TEAL),
            SettingsOptions.Themes.MACCHIATO_TEAL
        ),
        Theme(
            "Sky",
            getMacchiatoScheme(AppTheme.CatppuccinMacchiato.SKY),
            SettingsOptions.Themes.MACCHIATO_SKY
        ),
        Theme(
            "Sapphire",
            getMacchiatoScheme(AppTheme.CatppuccinMacchiato.SAPPHIRE),
            SettingsOptions.Themes.MACCHIATO_SAPPHIRE
        ),
        Theme(
            "Blue",
            getMacchiatoScheme(AppTheme.CatppuccinMacchiato.BLUE),
            SettingsOptions.Themes.MACCHIATO_BLUE
        ),
        Theme(
            "Lavender",
            getMacchiatoScheme(AppTheme.CatppuccinMacchiato.LAVENDER),
            SettingsOptions.Themes.MACCHIATO_LAVENDER
        )
    )
}

fun getCatppuccinMochaColorSchemes(): List<Theme> {
    return listOf(
        Theme(
            "Rosewater",
            getMochaScheme(AppTheme.CatppuccinMocha.ROSEWATER),
            SettingsOptions.Themes.MOCHA_ROSEWATER
        ),
        Theme(
            "Flamingo",
            getMochaScheme(AppTheme.CatppuccinMocha.FLAMINGO),
            SettingsOptions.Themes.MOCHA_FLAMINGO
        ),
        Theme(
            "Pink",
            getMochaScheme(AppTheme.CatppuccinMocha.PINK),
            SettingsOptions.Themes.MOCHA_PINK
        ),
        Theme(
            "Mauve",
            getMochaScheme(AppTheme.CatppuccinMocha.MAUVE),
            SettingsOptions.Themes.MOCHA_MAUVE
        ),
        Theme(
            "Red",
            getMochaScheme(AppTheme.CatppuccinMocha.RED),
            SettingsOptions.Themes.MOCHA_RED
        ),
        Theme(
            "Maroon",
            getMochaScheme(AppTheme.CatppuccinMocha.MAROON),
            SettingsOptions.Themes.MOCHA_MAROON
        ),
        Theme(
            "Peach",
            getMochaScheme(AppTheme.CatppuccinMocha.PEACH),
            SettingsOptions.Themes.MOCHA_PEACH
        ),
        Theme(
            "Yellow",
            getMochaScheme(AppTheme.CatppuccinMocha.YELLOW),
            SettingsOptions.Themes.MOCHA_YELLOW
        ),
        Theme(
            "Green",
            getMochaScheme(AppTheme.CatppuccinMocha.GREEN),
            SettingsOptions.Themes.MOCHA_GREEN
        ),
        Theme(
            "Teal",
            getMochaScheme(AppTheme.CatppuccinMocha.TEAL),
            SettingsOptions.Themes.MOCHA_TEAL
        ),
        Theme(
            "Sky",
            getMochaScheme(AppTheme.CatppuccinMocha.SKY),
            SettingsOptions.Themes.MOCHA_SKY
        ),
        Theme(
            "Sapphire",
            getMochaScheme(AppTheme.CatppuccinMocha.SAPPHIRE),
            SettingsOptions.Themes.MOCHA_SAPPHIRE
        ),
        Theme(
            "Blue",
            getMochaScheme(AppTheme.CatppuccinMocha.BLUE),
            SettingsOptions.Themes.MOCHA_BLUE
        ),
        Theme(
            "Lavender",
            getMochaScheme(AppTheme.CatppuccinMocha.LAVENDER),
            SettingsOptions.Themes.MOCHA_LAVENDER
        )
    )
}