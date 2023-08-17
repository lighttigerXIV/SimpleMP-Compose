package com.lighttigerxiv.simple.mp.compose.ui.composables

import android.content.Context
import android.os.Build
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.data.variables.MEDIUM_SPACING
import com.lighttigerxiv.simple.mp.compose.data.variables.MEDIUM_TITLE_SIZE
import com.lighttigerxiv.simple.mp.compose.data.variables.SMALL_SPACING
import com.lighttigerxiv.simple.mp.compose.data.variables.SMALL_TITLE_SIZE
import com.lighttigerxiv.simple.mp.compose.data.variables.SettingsValues
import com.lighttigerxiv.simple.mp.compose.functions.getAppString
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.MediumHorizontalSpacer
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.MediumVerticalSpacer
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.SmallHorizontalSpacer
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.SmallVerticalSpacer
import com.lighttigerxiv.simple.mp.compose.ui.theme.DarkBlueColors
import com.lighttigerxiv.simple.mp.compose.ui.theme.DarkGreenColors
import com.lighttigerxiv.simple.mp.compose.ui.theme.DarkOrangeColors
import com.lighttigerxiv.simple.mp.compose.ui.theme.DarkPinkColors
import com.lighttigerxiv.simple.mp.compose.ui.theme.DarkPurpleColors
import com.lighttigerxiv.simple.mp.compose.ui.theme.DarkRedColors
import com.lighttigerxiv.simple.mp.compose.ui.theme.DarkYellowColors
import com.lighttigerxiv.simple.mp.compose.ui.theme.FrappeBlue
import com.lighttigerxiv.simple.mp.compose.ui.theme.FrappeFlamingo
import com.lighttigerxiv.simple.mp.compose.ui.theme.FrappeGreen
import com.lighttigerxiv.simple.mp.compose.ui.theme.FrappeLavender
import com.lighttigerxiv.simple.mp.compose.ui.theme.FrappeMaroon
import com.lighttigerxiv.simple.mp.compose.ui.theme.FrappeMauve
import com.lighttigerxiv.simple.mp.compose.ui.theme.FrappePeach
import com.lighttigerxiv.simple.mp.compose.ui.theme.FrappePink
import com.lighttigerxiv.simple.mp.compose.ui.theme.FrappeRed
import com.lighttigerxiv.simple.mp.compose.ui.theme.FrappeRosewater
import com.lighttigerxiv.simple.mp.compose.ui.theme.FrappeSapphire
import com.lighttigerxiv.simple.mp.compose.ui.theme.FrappeSky
import com.lighttigerxiv.simple.mp.compose.ui.theme.FrappeTeal
import com.lighttigerxiv.simple.mp.compose.ui.theme.FrappeYellow
import com.lighttigerxiv.simple.mp.compose.ui.theme.LightBlueColors
import com.lighttigerxiv.simple.mp.compose.ui.theme.LightGreenColors
import com.lighttigerxiv.simple.mp.compose.ui.theme.LightOrangeColors
import com.lighttigerxiv.simple.mp.compose.ui.theme.LightPinkColors
import com.lighttigerxiv.simple.mp.compose.ui.theme.LightPurpleColors
import com.lighttigerxiv.simple.mp.compose.ui.theme.LightRedColors
import com.lighttigerxiv.simple.mp.compose.ui.theme.LightYellowColors
import com.lighttigerxiv.simple.mp.compose.ui.theme.MacchiatoBlue
import com.lighttigerxiv.simple.mp.compose.ui.theme.MacchiatoFlamingo
import com.lighttigerxiv.simple.mp.compose.ui.theme.MacchiatoGreen
import com.lighttigerxiv.simple.mp.compose.ui.theme.MacchiatoLavender
import com.lighttigerxiv.simple.mp.compose.ui.theme.MacchiatoMaroon
import com.lighttigerxiv.simple.mp.compose.ui.theme.MacchiatoMauve
import com.lighttigerxiv.simple.mp.compose.ui.theme.MacchiatoPeach
import com.lighttigerxiv.simple.mp.compose.ui.theme.MacchiatoPink
import com.lighttigerxiv.simple.mp.compose.ui.theme.MacchiatoRed
import com.lighttigerxiv.simple.mp.compose.ui.theme.MacchiatoRosewater
import com.lighttigerxiv.simple.mp.compose.ui.theme.MacchiatoSapphire
import com.lighttigerxiv.simple.mp.compose.ui.theme.MacchiatoSky
import com.lighttigerxiv.simple.mp.compose.ui.theme.MacchiatoTeal
import com.lighttigerxiv.simple.mp.compose.ui.theme.MacchiatoYellow
import com.lighttigerxiv.simple.mp.compose.ui.theme.MochaBlue
import com.lighttigerxiv.simple.mp.compose.ui.theme.MochaFlamingo
import com.lighttigerxiv.simple.mp.compose.ui.theme.MochaGreen
import com.lighttigerxiv.simple.mp.compose.ui.theme.MochaLavender
import com.lighttigerxiv.simple.mp.compose.ui.theme.MochaMaroon
import com.lighttigerxiv.simple.mp.compose.ui.theme.MochaMauve
import com.lighttigerxiv.simple.mp.compose.ui.theme.MochaPeach
import com.lighttigerxiv.simple.mp.compose.ui.theme.MochaPink
import com.lighttigerxiv.simple.mp.compose.ui.theme.MochaRed
import com.lighttigerxiv.simple.mp.compose.ui.theme.MochaRosewater
import com.lighttigerxiv.simple.mp.compose.ui.theme.MochaSapphire
import com.lighttigerxiv.simple.mp.compose.ui.theme.MochaSky
import com.lighttigerxiv.simple.mp.compose.ui.theme.MochaTeal
import com.lighttigerxiv.simple.mp.compose.ui.theme.MochaYellow


@Composable
fun ThemeSelector(
    selectedTheme: String,
    onThemeClick: (theme: String) -> Unit
) {

    val context = LocalContext.current
    val inDarkMode = isSystemInDarkTheme()
    val showCommonThemes = remember { mutableStateOf(true) }
    val showCatppuccinThemes = remember { mutableStateOf(true) }
    val commonThemes = remember { getCommonThemes(inDarkMode, context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { onThemeClick(SettingsValues.Themes.SYSTEM) }
                        .padding(MEDIUM_SPACING)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        RadioButton(
                            selected = selectedTheme == SettingsValues.Themes.SYSTEM,
                            onClick = { onThemeClick(SettingsValues.Themes.SYSTEM) }
                        )

                        CustomText(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f, fill = true),
                            text = remember { getAppString(context, R.string.System) },
                        )

                        Spacer(modifier = Modifier.width(5.dp))
                    }
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

                        ThemePreview(selectedTheme = selectedTheme, theme = theme, onClick = { onThemeClick(theme.setting) })
                        SmallHorizontalSpacer()
                    }
                }
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

                Text(text = "Frappe", fontSize = SMALL_TITLE_SIZE, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)

                LazyRow {
                    items(items = getCatppuccinFrappeThemes(), key = { it.setting }) { theme ->

                        ThemePreview(selectedTheme = selectedTheme, theme = theme, onClick = { onThemeClick(theme.setting) })
                        SmallHorizontalSpacer()
                    }
                }

                SmallVerticalSpacer()

                Text(text = "Macchiato", fontSize = SMALL_TITLE_SIZE, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)

                LazyRow {
                    items(items = getCatppuccinMacchiatoThemes(), key = { it.setting }) { theme ->

                        ThemePreview(selectedTheme = selectedTheme, theme = theme, onClick = { onThemeClick(theme.setting) })
                        SmallHorizontalSpacer()
                    }
                }

                SmallVerticalSpacer()

                Text(text = "Mocha", fontSize = SMALL_TITLE_SIZE, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)

                LazyRow {
                    items(items = getCatppuccinMochaThemes(), key = { it.setting }) { theme ->

                        ThemePreview(selectedTheme = selectedTheme, theme = theme, onClick = { onThemeClick(theme.setting) })
                        SmallHorizontalSpacer()
                    }
                }
            }
        }
    }
}

@Composable
fun ThemeItem(
    themeName: String,
    surfaceColor: Color,
    accentColor: Color,
    isSelected: Boolean,
    onSelect: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .clickable { onSelect() }
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                RadioButton(
                    selected = isSelected,
                    onClick = { onSelect() }
                )

                CustomText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = true),
                    text = themeName,
                )

                Spacer(modifier = Modifier.width(5.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = true)
                        .border(1.dp, MaterialTheme.colorScheme.onSurfaceVariant, shape = RoundedCornerShape(10.dp))
                        .height(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .weight(1f, fill = true)
                            .background(surfaceColor)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .weight(1f, fill = true)
                            .background(accentColor)
                    )
                }
            }


        }

        Spacer(modifier = Modifier.height(10.dp))
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
            .padding(MEDIUM_SPACING)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(theme.colorScheme.surfaceVariant)
                    .padding(SMALL_SPACING)
            ) {
                Icon(
                    modifier = Modifier
                        .height(60.dp)
                        .width(60.dp),
                    painter = painterResource(id = R.drawable.cd),
                    contentDescription = null,
                    tint = theme.colorScheme.primary
                )
            }

            MediumHorizontalSpacer()

            Column {
                Row {
                    Text(text = "Abc", color = theme.colorScheme.onSurface, fontSize = 20.sp)

                    MediumHorizontalSpacer()

                    Text(text = "Abc", color = theme.colorScheme.primary, fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.height(4.dp))

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

                    MediumHorizontalSpacer()

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(theme.colorScheme.primary)
                            .padding(SMALL_SPACING)
                    ) {
                        Icon(
                            modifier = Modifier
                                .height(20.dp)
                                .width(20.dp),
                            painter = painterResource(id = R.drawable.icon_shuffle_solid),
                            contentDescription = null,
                            tint = theme.colorScheme.onPrimary
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


fun getCommonThemes(inDarkMode: Boolean, context: Context): List<Theme> {

    return if (inDarkMode) {
        listOf(
            Theme(
                getAppString(context, R.string.Blue),
                DarkBlueColors,
                SettingsValues.Themes.BLUE
            ),
            Theme(
                getAppString(context, R.string.Red),
                DarkRedColors,
                SettingsValues.Themes.RED
            ),
            Theme(
                getAppString(context, R.string.Purple),
                DarkPurpleColors,
                SettingsValues.Themes.PURPLE
            ),
            Theme(
                getAppString(context, R.string.Orange),
                DarkOrangeColors,
                SettingsValues.Themes.ORANGE
            ),
            Theme(
                getAppString(context, R.string.Yellow),
                DarkYellowColors,
                SettingsValues.Themes.YELLOW
            ),
            Theme(
                getAppString(context, R.string.Green),
                DarkGreenColors,
                SettingsValues.Themes.GREEN
            ),
            Theme(
                getAppString(context, R.string.Pink),
                DarkPinkColors,
                SettingsValues.Themes.PINK
            )
        )
    } else {
        listOf(
            Theme(
                getAppString(context, R.string.Blue),
                LightBlueColors,
                SettingsValues.Themes.BLUE
            ),
            Theme(
                getAppString(context, R.string.Red),
                LightRedColors,
                SettingsValues.Themes.RED
            ),
            Theme(
                getAppString(context, R.string.Purple),
                LightPurpleColors,
                SettingsValues.Themes.PURPLE
            ),
            Theme(
                getAppString(context, R.string.Orange),
                LightOrangeColors,
                SettingsValues.Themes.ORANGE
            ),
            Theme(
                getAppString(context, R.string.Yellow),
                LightYellowColors,
                SettingsValues.Themes.YELLOW
            ),
            Theme(
                getAppString(context, R.string.Green),
                LightGreenColors,
                SettingsValues.Themes.GREEN
            ),
            Theme(
                getAppString(context, R.string.Pink),
                LightPinkColors,
                SettingsValues.Themes.PINK
            )
        )
    }
}

fun getCatppuccinFrappeThemes(): List<Theme> {
    return listOf(
        Theme(
            "Rosewater",
            FrappeRosewater,
            SettingsValues.Themes.FRAPPE_ROSEWATER
        ),
        Theme(
            "Flamingo",
            FrappeFlamingo,
            SettingsValues.Themes.FRAPPE_FLAMINGO
        ),
        Theme(
            "Pink",
            FrappePink,
            SettingsValues.Themes.FRAPPE_PINK
        ),
        Theme(
            "Mauve",
            FrappeMauve,
            SettingsValues.Themes.FRAPPE_MAUVE
        ),
        Theme(
            "Red",
            FrappeRed,
            SettingsValues.Themes.FRAPPE_RED
        ),
        Theme(
            "Maroon",
            FrappeMaroon,
            SettingsValues.Themes.FRAPPE_MAROON
        ),
        Theme(
            "Peach",
            FrappePeach,
            SettingsValues.Themes.FRAPPE_PEACH
        ),
        Theme(
            "Yellow",
            FrappeYellow,
            SettingsValues.Themes.FRAPPE_YELLOW
        ),
        Theme(
            "Green",
            FrappeGreen,
            SettingsValues.Themes.FRAPPE_GREEN
        ),
        Theme(
            "Teal",
            FrappeTeal,
            SettingsValues.Themes.FRAPPE_TEAL
        ),
        Theme(
            "Sky",
            FrappeSky,
            SettingsValues.Themes.FRAPPE_SKY
        ),
        Theme(
            "Sapphire",
            FrappeSapphire,
            SettingsValues.Themes.FRAPPE_SAPPHIRE
        ),
        Theme(
            "Blue",
            FrappeBlue,
            SettingsValues.Themes.FRAPPE_BLUE
        ),
        Theme(
            "Lavender",
            FrappeLavender,
            SettingsValues.Themes.FRAPPE_LAVENDER
        )
    )
}

fun getCatppuccinMacchiatoThemes(): List<Theme> {
    return listOf(
        Theme(
            "Rosewater",
            MacchiatoRosewater,
            SettingsValues.Themes.MACCHIATO_ROSEWATER
        ),
        Theme(
            "Flamingo",
            MacchiatoFlamingo,
            SettingsValues.Themes.MACCHIATO_FLAMINGO
        ),
        Theme(
            "Pink",
            MacchiatoPink,
            SettingsValues.Themes.MACCHIATO_PINK
        ),
        Theme(
            "Mauve",
            MacchiatoMauve,
            SettingsValues.Themes.MACCHIATO_MAUVE
        ),
        Theme(
            "Red",
            MacchiatoRed,
            SettingsValues.Themes.MACCHIATO_RED
        ),
        Theme(
            "Maroon",
            MacchiatoMaroon,
            SettingsValues.Themes.MACCHIATO_MAROON
        ),
        Theme(
            "Peach",
            MacchiatoPeach,
            SettingsValues.Themes.MACCHIATO_PEACH
        ),
        Theme(
            "Yellow",
            MacchiatoYellow,
            SettingsValues.Themes.MACCHIATO_YELLOW
        ),
        Theme(
            "Green",
            MacchiatoGreen,
            SettingsValues.Themes.MACCHIATO_GREEN
        ),
        Theme(
            "Teal",
            MacchiatoTeal,
            SettingsValues.Themes.MACCHIATO_TEAL
        ),
        Theme(
            "Sky",
            MacchiatoSky,
            SettingsValues.Themes.MACCHIATO_SKY
        ),
        Theme(
            "Sapphire",
            MacchiatoSapphire,
            SettingsValues.Themes.MACCHIATO_SAPPHIRE
        ),
        Theme(
            "Blue",
            MacchiatoBlue,
            SettingsValues.Themes.MACCHIATO_BLUE
        ),
        Theme(
            "Lavender",
            MacchiatoLavender,
            SettingsValues.Themes.MACCHIATO_LAVENDER
        )
    )
}

fun getCatppuccinMochaThemes(): List<Theme> {
    return listOf(
        Theme(
            "Rosewater",
            MochaRosewater,
            SettingsValues.Themes.MOCHA_ROSEWATER
        ),
        Theme(
            "Flamingo",
            MochaFlamingo,
            SettingsValues.Themes.MOCHA_FLAMINGO
        ),
        Theme(
            "Pink",
            MochaPink,
            SettingsValues.Themes.MOCHA_PINK
        ),
        Theme(
            "Mauve",
            MochaMauve,
            SettingsValues.Themes.MOCHA_MAUVE
        ),
        Theme(
            "Red",
            MochaRed,
            SettingsValues.Themes.MOCHA_RED
        ),
        Theme(
            "Maroon",
            MochaMaroon,
            SettingsValues.Themes.MOCHA_MAROON
        ),
        Theme(
            "Peach",
            MochaPeach,
            SettingsValues.Themes.MOCHA_PEACH
        ),
        Theme(
            "Yellow",
            MochaYellow,
            SettingsValues.Themes.MOCHA_YELLOW
        ),
        Theme(
            "Green",
            MochaGreen,
            SettingsValues.Themes.MOCHA_GREEN
        ),
        Theme(
            "Teal",
            MochaTeal,
            SettingsValues.Themes.MOCHA_TEAL
        ),
        Theme(
            "Sky",
            MochaSky,
            SettingsValues.Themes.MOCHA_SKY
        ),
        Theme(
            "Sapphire",
            MochaSapphire,
            SettingsValues.Themes.MOCHA_SAPPHIRE
        ),
        Theme(
            "Blue",
            MochaBlue,
            SettingsValues.Themes.MOCHA_BLUE
        ),
        Theme(
            "Lavender",
            MochaLavender,
            SettingsValues.Themes.MOCHA_LAVENDER
        )
    )
}