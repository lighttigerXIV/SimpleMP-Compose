package com.lighttigerxiv.simple.mp.compose.ui.composables

import android.os.Build
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.data.variables.SettingsValues
import com.lighttigerxiv.simple.mp.compose.functions.getAppString

@Composable
fun ThemeSelector(
    selectedTheme: String,
    onThemeClick: (theme: String) -> Unit
) {

    val context = LocalContext.current
    val showCommonThemes = remember { mutableStateOf(true) }
    val showCatppuccinThemes = remember { mutableStateOf(true) }

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
                        .padding(14.dp)
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
                        ) { showCommonThemes.value = !showCommonThemes.value },
                    text = remember { getAppString(context, R.string.Common) },
                    size = 24.sp,
                    weight = FontWeight.Bold
                )
            }


            if (showCommonThemes.value) {

                Spacer(modifier = Modifier.height(10.dp))

                ThemeItem(
                    themeName = remember { getAppString(context, R.string.Blue) },
                    surfaceColor = Color(0xFF225FA6),
                    accentColor = Color(0xFF225FA6),
                    isSelected = selectedTheme == SettingsValues.Themes.BLUE,
                    onSelect = { onThemeClick(SettingsValues.Themes.BLUE) }
                )

                ThemeItem(
                    themeName = remember { getAppString(context, R.string.Red) },
                    surfaceColor = Color(0xFFBF0027),
                    accentColor = Color(0xFFBF0027),
                    isSelected = selectedTheme == SettingsValues.Themes.RED,
                    onSelect = { onThemeClick(SettingsValues.Themes.RED) }
                )

                ThemeItem(
                    themeName = remember { getAppString(context, R.string.Purple) },
                    surfaceColor = Color(0xFF6750A4),
                    accentColor = Color(0xFF6750A4),
                    isSelected = selectedTheme == SettingsValues.Themes.PURPLE,
                    onSelect = { onThemeClick(SettingsValues.Themes.PURPLE) }
                )

                ThemeItem(
                    themeName = remember { getAppString(context, R.string.Orange) },
                    surfaceColor = Color(0xFF924B00),
                    accentColor = Color(0xFF924B00),
                    isSelected = selectedTheme == SettingsValues.Themes.ORANGE,
                    onSelect = { onThemeClick(SettingsValues.Themes.ORANGE) }
                )

                ThemeItem(
                    themeName = remember { getAppString(context, R.string.Yellow) },
                    surfaceColor = Color(0xFF6D5E00),
                    accentColor = Color(0xFF6D5E00),
                    isSelected = selectedTheme == SettingsValues.Themes.YELLOW,
                    onSelect = { onThemeClick(SettingsValues.Themes.YELLOW) }
                )

                ThemeItem(
                    themeName = remember { getAppString(context, R.string.Green) },
                    surfaceColor = Color(0xFF326B00),
                    accentColor = Color(0xFF326B00),
                    isSelected = selectedTheme == SettingsValues.Themes.GREEN,
                    onSelect = { onThemeClick(SettingsValues.Themes.GREEN) }
                )

                ThemeItem(
                    themeName = remember { getAppString(context, R.string.Pink) },
                    surfaceColor = Color(0xFF84468E),
                    accentColor = Color(0xFF84468E),
                    isSelected = selectedTheme == SettingsValues.Themes.PINK,
                    onSelect = { onThemeClick(SettingsValues.Themes.PINK) }
                )
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

                Spacer(modifier = Modifier.height(10.dp))

                ThemeItem(
                    themeName = "Frappe Rosewater",
                    surfaceColor = Color(0xff303446),
                    accentColor = Color(0xfff2d5cf),
                    isSelected = selectedTheme == SettingsValues.Themes.FRAPPE_ROSEWATER,
                    onSelect = { onThemeClick(SettingsValues.Themes.FRAPPE_ROSEWATER) }
                )

                ThemeItem(
                    themeName = "Frappe Flamingo",
                    surfaceColor = Color(0xff303446),
                    accentColor = Color(0xffeebebe),
                    isSelected = selectedTheme == SettingsValues.Themes.FRAPPE_FLAMINGO,
                    onSelect = { onThemeClick(SettingsValues.Themes.FRAPPE_FLAMINGO) }
                )

                ThemeItem(
                    themeName = "Frappe Pink",
                    surfaceColor = Color(0xff303446),
                    accentColor = Color(0xfff4b8e4),
                    isSelected = selectedTheme == SettingsValues.Themes.FRAPPE_PINK,
                    onSelect = { onThemeClick(SettingsValues.Themes.FRAPPE_PINK) }
                )

                ThemeItem(
                    themeName = "Frappe Mauve",
                    surfaceColor = Color(0xff303446),
                    accentColor = Color(0xffca9ee6),
                    isSelected = selectedTheme == SettingsValues.Themes.FRAPPE_MAUVE,
                    onSelect = { onThemeClick(SettingsValues.Themes.FRAPPE_MAUVE) }
                )

                ThemeItem(
                    themeName = "Frappe Red",
                    surfaceColor = Color(0xff303446),
                    accentColor = Color(0xffe78284),
                    isSelected = selectedTheme == SettingsValues.Themes.FRAPPE_RED,
                    onSelect = { onThemeClick(SettingsValues.Themes.FRAPPE_RED) }
                )

                ThemeItem(
                    themeName = "Frappe Maroon",
                    surfaceColor = Color(0xff303446),
                    accentColor = Color(0xffea999c),
                    isSelected = selectedTheme == SettingsValues.Themes.FRAPPE_MAROON,
                    onSelect = { onThemeClick(SettingsValues.Themes.FRAPPE_MAROON) }
                )

                ThemeItem(
                    themeName = "Frappe Peach",
                    surfaceColor = Color(0xff303446),
                    accentColor = Color(0xffef9f76),
                    isSelected = selectedTheme == SettingsValues.Themes.FRAPPE_PEACH,
                    onSelect = { onThemeClick(SettingsValues.Themes.FRAPPE_PEACH) }
                )

                ThemeItem(
                    themeName = "Frappe Yellow",
                    surfaceColor = Color(0xff303446),
                    accentColor = Color(0xffe5c890),
                    isSelected = selectedTheme == SettingsValues.Themes.FRAPPE_YELLOW,
                    onSelect = { onThemeClick(SettingsValues.Themes.FRAPPE_YELLOW) }
                )

                ThemeItem(
                    themeName = "Frappe Green",
                    surfaceColor = Color(0xff303446),
                    accentColor = Color(0xffa6d189),
                    isSelected = selectedTheme == SettingsValues.Themes.FRAPPE_GREEN,
                    onSelect = { onThemeClick(SettingsValues.Themes.FRAPPE_GREEN) }
                )

                ThemeItem(
                    themeName = "Frappe Teal",
                    surfaceColor = Color(0xff303446),
                    accentColor = Color(0xff81c8be),
                    isSelected = selectedTheme == SettingsValues.Themes.FRAPPE_TEAL,
                    onSelect = { onThemeClick(SettingsValues.Themes.FRAPPE_TEAL) }
                )

                ThemeItem(
                    themeName = "Frappe Sky",
                    surfaceColor = Color(0xff303446),
                    accentColor = Color(0xff99d1db),
                    isSelected = selectedTheme == SettingsValues.Themes.FRAPPE_SKY,
                    onSelect = { onThemeClick(SettingsValues.Themes.FRAPPE_SKY) }
                )

                ThemeItem(
                    themeName = "Frappe Sapphire",
                    surfaceColor = Color(0xff303446),
                    accentColor = Color(0xff85c1dc),
                    isSelected = selectedTheme == SettingsValues.Themes.FRAPPE_SAPPHIRE,
                    onSelect = { onThemeClick(SettingsValues.Themes.FRAPPE_SAPPHIRE) }
                )

                ThemeItem(
                    themeName = "Frappe Blue",
                    surfaceColor = Color(0xff303446),
                    accentColor = Color(0xff8caaee),
                    isSelected = selectedTheme == SettingsValues.Themes.FRAPPE_BLUE,
                    onSelect = { onThemeClick(SettingsValues.Themes.FRAPPE_BLUE) }
                )

                ThemeItem(
                    themeName = "Frappe Lavender",
                    surfaceColor = Color(0xff303446),
                    accentColor = Color(0xffbabbf1),
                    isSelected = selectedTheme == SettingsValues.Themes.FRAPPE_LAVENDER,
                    onSelect = { onThemeClick(SettingsValues.Themes.FRAPPE_LAVENDER) }
                )

                Spacer(modifier = Modifier.height(5.dp))
                Divider(color = MaterialTheme.colorScheme.surface)
                Spacer(modifier = Modifier.height(5.dp))

                ThemeItem(
                    themeName = "Macchiato Rosewater",
                    surfaceColor = Color(0xff24273a),
                    accentColor = Color(0xfff5e0dc),
                    isSelected = selectedTheme == SettingsValues.Themes.MACCHIATO_ROSEWATER,
                    onSelect = { onThemeClick(SettingsValues.Themes.MACCHIATO_ROSEWATER) }
                )

                ThemeItem(
                    themeName = "Macchiato Flamingo",
                    surfaceColor = Color(0xff24273a),
                    accentColor = Color(0xfff2cdcd),
                    isSelected = selectedTheme == SettingsValues.Themes.MACCHIATO_FLAMINGO,
                    onSelect = { onThemeClick(SettingsValues.Themes.MACCHIATO_FLAMINGO) }
                )

                ThemeItem(
                    themeName = "Macchiato Pink",
                    surfaceColor = Color(0xff24273a),
                    accentColor = Color(0xfff5c2e7),
                    isSelected = selectedTheme == SettingsValues.Themes.MACCHIATO_PINK,
                    onSelect = { onThemeClick(SettingsValues.Themes.MACCHIATO_PINK) }
                )

                ThemeItem(
                    themeName = "Macchiato Mauve",
                    surfaceColor = Color(0xff24273a),
                    accentColor = Color(0xffcba6f7),
                    isSelected = selectedTheme == SettingsValues.Themes.MACCHIATO_MAUVE,
                    onSelect = { onThemeClick(SettingsValues.Themes.MACCHIATO_MAUVE) }
                )

                ThemeItem(
                    themeName = "Macchiato Red",
                    surfaceColor = Color(0xff24273a),
                    accentColor = Color(0xfff38ba8),
                    isSelected = selectedTheme == SettingsValues.Themes.MACCHIATO_RED,
                    onSelect = { onThemeClick(SettingsValues.Themes.MACCHIATO_RED) }
                )

                ThemeItem(
                    themeName = "Macchiato Maroon",
                    surfaceColor = Color(0xff24273a),
                    accentColor = Color(0xffeba0ac),
                    isSelected = selectedTheme == SettingsValues.Themes.MACCHIATO_MAROON,
                    onSelect = { onThemeClick(SettingsValues.Themes.MACCHIATO_MAROON) }
                )

                ThemeItem(
                    themeName = "Macchiato Peach",
                    surfaceColor = Color(0xff24273a),
                    accentColor = Color(0xfffab387),
                    isSelected = selectedTheme == SettingsValues.Themes.MACCHIATO_PEACH,
                    onSelect = { onThemeClick(SettingsValues.Themes.MACCHIATO_PEACH) }
                )

                ThemeItem(
                    themeName = "Macchiato Yellow",
                    surfaceColor = Color(0xff24273a),
                    accentColor = Color(0xfff9e2af),
                    isSelected = selectedTheme == SettingsValues.Themes.MACCHIATO_YELLOW,
                    onSelect = { onThemeClick(SettingsValues.Themes.MACCHIATO_YELLOW) }
                )

                ThemeItem(
                    themeName = "Macchiato Green",
                    surfaceColor = Color(0xff24273a),
                    accentColor = Color(0xffa6e3a1),
                    isSelected = selectedTheme == SettingsValues.Themes.MACCHIATO_GREEN,
                    onSelect = { onThemeClick(SettingsValues.Themes.MACCHIATO_GREEN) }
                )

                ThemeItem(
                    themeName = "Macchiato Teal",
                    surfaceColor = Color(0xff24273a),
                    accentColor = Color(0xff94e2d5),
                    isSelected = selectedTheme == SettingsValues.Themes.MACCHIATO_TEAL,
                    onSelect = { onThemeClick(SettingsValues.Themes.MACCHIATO_TEAL) }
                )

                ThemeItem(
                    themeName = "Macchiato Sky",
                    surfaceColor = Color(0xff24273a),
                    accentColor = Color(0xff89dceb),
                    isSelected = selectedTheme == SettingsValues.Themes.MACCHIATO_SKY,
                    onSelect = { onThemeClick(SettingsValues.Themes.MACCHIATO_SKY) }
                )

                ThemeItem(
                    themeName = "Macchiato Sapphire",
                    surfaceColor = Color(0xff24273a),
                    accentColor = Color(0xff74c7ec),
                    isSelected = selectedTheme == SettingsValues.Themes.MACCHIATO_SAPPHIRE,
                    onSelect = { onThemeClick(SettingsValues.Themes.MACCHIATO_SAPPHIRE) }
                )

                ThemeItem(
                    themeName = "Macchiato Blue",
                    surfaceColor = Color(0xff24273a),
                    accentColor = Color(0xff89b4fa),
                    isSelected = selectedTheme == SettingsValues.Themes.MACCHIATO_BLUE,
                    onSelect = { onThemeClick(SettingsValues.Themes.MACCHIATO_BLUE) }
                )

                ThemeItem(
                    themeName = "Macchiato Lavender",
                    surfaceColor = Color(0xff24273a),
                    accentColor = Color(0xffb4befe),
                    isSelected = selectedTheme == SettingsValues.Themes.MACCHIATO_LAVENDER,
                    onSelect = { onThemeClick(SettingsValues.Themes.MACCHIATO_LAVENDER) }
                )

                Spacer(modifier = Modifier.height(5.dp))
                Divider(color = MaterialTheme.colorScheme.surface)
                Spacer(modifier = Modifier.height(5.dp))

                ThemeItem(
                    themeName = "Mocha Rosewater",
                    surfaceColor = Color(0xff1e1e2e),
                    accentColor = Color(0xfff5e0dc),
                    isSelected = selectedTheme == SettingsValues.Themes.MOCHA_ROSEWATER,
                    onSelect = { onThemeClick(SettingsValues.Themes.MOCHA_ROSEWATER) }
                )

                ThemeItem(
                    themeName = "Mocha Flamingo",
                    surfaceColor = Color(0xff1e1e2e),
                    accentColor = Color(0xfff2cdcd),
                    isSelected = selectedTheme == SettingsValues.Themes.MOCHA_FLAMINGO,
                    onSelect = { onThemeClick(SettingsValues.Themes.MOCHA_FLAMINGO) }
                )

                ThemeItem(
                    themeName = "Mocha Pink",
                    surfaceColor = Color(0xff1e1e2e),
                    accentColor = Color(0xfff5c2e7),
                    isSelected = selectedTheme == SettingsValues.Themes.MOCHA_PINK,
                    onSelect = { onThemeClick(SettingsValues.Themes.MOCHA_PINK) }
                )

                ThemeItem(
                    themeName = "Mocha Mauve",
                    surfaceColor = Color(0xff1e1e2e),
                    accentColor = Color(0xffcba6f7),
                    isSelected = selectedTheme == SettingsValues.Themes.MOCHA_MAUVE,
                    onSelect = { onThemeClick(SettingsValues.Themes.MOCHA_MAUVE) }
                )

                ThemeItem(
                    themeName = "Mocha Red",
                    surfaceColor = Color(0xff1e1e2e),
                    accentColor = Color(0xfff38ba8),
                    isSelected = selectedTheme == SettingsValues.Themes.MOCHA_RED,
                    onSelect = { onThemeClick(SettingsValues.Themes.MOCHA_RED) }
                )

                ThemeItem(
                    themeName = "Mocha Maroon",
                    surfaceColor = Color(0xff1e1e2e),
                    accentColor = Color(0xffeba0ac),
                    isSelected = selectedTheme == SettingsValues.Themes.MOCHA_MAROON,
                    onSelect = { onThemeClick(SettingsValues.Themes.MOCHA_MAROON) }
                )

                ThemeItem(
                    themeName = "Mocha Peach",
                    surfaceColor = Color(0xff1e1e2e),
                    accentColor = Color(0xfffab387),
                    isSelected = selectedTheme == SettingsValues.Themes.MOCHA_PEACH,
                    onSelect = { onThemeClick(SettingsValues.Themes.MOCHA_PEACH) }
                )

                ThemeItem(
                    themeName = "Mocha Yellow",
                    surfaceColor = Color(0xff1e1e2e),
                    accentColor = Color(0xfff9e2af),
                    isSelected = selectedTheme == SettingsValues.Themes.MOCHA_YELLOW,
                    onSelect = { onThemeClick(SettingsValues.Themes.MOCHA_YELLOW) }
                )

                ThemeItem(
                    themeName = "Mocha Green",
                    surfaceColor = Color(0xff1e1e2e),
                    accentColor = Color(0xffa6e3a1),
                    isSelected = selectedTheme == SettingsValues.Themes.MOCHA_GREEN,
                    onSelect = { onThemeClick(SettingsValues.Themes.MOCHA_GREEN) }
                )

                ThemeItem(
                    themeName = "Mocha Teal",
                    surfaceColor = Color(0xff1e1e2e),
                    accentColor = Color(0xff94e2d5),
                    isSelected = selectedTheme == SettingsValues.Themes.MOCHA_TEAL,
                    onSelect = { onThemeClick(SettingsValues.Themes.MOCHA_TEAL) }
                )

                ThemeItem(
                    themeName = "Mocha Sky",
                    surfaceColor = Color(0xff1e1e2e),
                    accentColor = Color(0xff89dceb),
                    isSelected = selectedTheme == SettingsValues.Themes.MOCHA_SKY,
                    onSelect = { onThemeClick(SettingsValues.Themes.MOCHA_SKY) }
                )

                ThemeItem(
                    themeName = "Mocha Sapphire",
                    surfaceColor = Color(0xff1e1e2e),
                    accentColor = Color(0xff74c7ec),
                    isSelected = selectedTheme == SettingsValues.Themes.MOCHA_SAPPHIRE,
                    onSelect = { onThemeClick(SettingsValues.Themes.MOCHA_SAPPHIRE) }
                )

                ThemeItem(
                    themeName = "Mocha Blue",
                    surfaceColor = Color(0xff1e1e2e),
                    accentColor = Color(0xff89b4fa),
                    isSelected = selectedTheme == SettingsValues.Themes.MOCHA_BLUE,
                    onSelect = { onThemeClick(SettingsValues.Themes.MOCHA_BLUE) }
                )

                ThemeItem(
                    themeName = "Mocha Lavender",
                    surfaceColor = Color(0xff1e1e2e),
                    accentColor = Color(0xffb4befe),
                    isSelected = selectedTheme == SettingsValues.Themes.MOCHA_LAVENDER,
                    onSelect = { onThemeClick(SettingsValues.Themes.MOCHA_LAVENDER) }
                )
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
