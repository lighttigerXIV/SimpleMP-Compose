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
                        .clickable { onThemeClick("System") }
                        .padding(14.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        RadioButton(
                            selected = selectedTheme == "System",
                            onClick = { onThemeClick("System") }
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
                    isSelected = selectedTheme == "Blue",
                    onSelect = { onThemeClick("Blue") }
                )

                ThemeItem(
                    themeName = remember { getAppString(context, R.string.Red) },
                    surfaceColor = Color(0xFFBF0027),
                    accentColor = Color(0xFFBF0027),
                    isSelected = selectedTheme == "Red",
                    onSelect = { onThemeClick("Red") }
                )

                ThemeItem(
                    themeName = remember { getAppString(context, R.string.Purple) },
                    surfaceColor = Color(0xFF6750A4),
                    accentColor = Color(0xFF6750A4),
                    isSelected = selectedTheme == "Purple",
                    onSelect = { onThemeClick("Purple") }
                )

                ThemeItem(
                    themeName = remember { getAppString(context, R.string.Orange) },
                    surfaceColor = Color(0xFF924B00),
                    accentColor = Color(0xFF924B00),
                    isSelected = selectedTheme == "Orange",
                    onSelect = { onThemeClick("Orange") }
                )

                ThemeItem(
                    themeName = remember { getAppString(context, R.string.Yellow) },
                    surfaceColor = Color(0xFF6D5E00),
                    accentColor = Color(0xFF6D5E00),
                    isSelected = selectedTheme == "Yellow",
                    onSelect = { onThemeClick("Yellow") }
                )

                ThemeItem(
                    themeName = remember { getAppString(context, R.string.Green) },
                    surfaceColor = Color(0xFF326B00),
                    accentColor = Color(0xFF326B00),
                    isSelected = selectedTheme == "Green",
                    onSelect = { onThemeClick("Green") }
                )

                ThemeItem(
                    themeName = remember { getAppString(context, R.string.Pink) },
                    surfaceColor = Color(0xFF84468E),
                    accentColor = Color(0xFF84468E),
                    isSelected = selectedTheme == "Pink",
                    onSelect = { onThemeClick("Pink") }
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
                    isSelected = selectedTheme == "FrappeRosewater",
                    onSelect = { onThemeClick("FrappeRosewater") }
                )

                ThemeItem(
                    themeName = "Frappe Flamingo",
                    surfaceColor = Color(0xff303446),
                    accentColor = Color(0xffeebebe),
                    isSelected = selectedTheme == "FrappeFlamingo",
                    onSelect = { onThemeClick("FrappeFlamingo") }
                )

                ThemeItem(
                    themeName = "Frappe Pink",
                    surfaceColor = Color(0xff303446),
                    accentColor = Color(0xfff4b8e4),
                    isSelected = selectedTheme == "FrappePink",
                    onSelect = { onThemeClick("FrappePink") }
                )

                ThemeItem(
                    themeName = "Frappe Mauve",
                    surfaceColor = Color(0xff303446),
                    accentColor = Color(0xffca9ee6),
                    isSelected = selectedTheme == "FrappeMauve",
                    onSelect = { onThemeClick("FrappeMauve") }
                )

                ThemeItem(
                    themeName = "Frappe Red",
                    surfaceColor = Color(0xff303446),
                    accentColor = Color(0xffe78284),
                    isSelected = selectedTheme == "FrappeRed",
                    onSelect = { onThemeClick("FrappeRed") }
                )

                ThemeItem(
                    themeName = "Frappe Maroon",
                    surfaceColor = Color(0xff303446),
                    accentColor = Color(0xffea999c),
                    isSelected = selectedTheme == "FrappeMaroon",
                    onSelect = { onThemeClick("FrappeMaroon") }
                )

                ThemeItem(
                    themeName = "Frappe Peach",
                    surfaceColor = Color(0xff303446),
                    accentColor = Color(0xffef9f76),
                    isSelected = selectedTheme == "FrappePeach",
                    onSelect = { onThemeClick("FrappePeach") }
                )

                ThemeItem(
                    themeName = "Frappe Yellow",
                    surfaceColor = Color(0xff303446),
                    accentColor = Color(0xffe5c890),
                    isSelected = selectedTheme == "FrappeYellow",
                    onSelect = { onThemeClick("FrappeYellow") }
                )

                ThemeItem(
                    themeName = "Frappe Green",
                    surfaceColor = Color(0xff303446),
                    accentColor = Color(0xffa6d189),
                    isSelected = selectedTheme == "FrappeGreen",
                    onSelect = { onThemeClick("FrappeGreen") }
                )

                ThemeItem(
                    themeName = "Frappe Teal",
                    surfaceColor = Color(0xff303446),
                    accentColor = Color(0xff81c8be),
                    isSelected = selectedTheme == "FrappeTeal",
                    onSelect = { onThemeClick("FrappeTeal") }
                )

                ThemeItem(
                    themeName = "Frappe Sky",
                    surfaceColor = Color(0xff303446),
                    accentColor = Color(0xff99d1db),
                    isSelected = selectedTheme == "FrappeSky",
                    onSelect = { onThemeClick("FrappeSky") }
                )

                ThemeItem(
                    themeName = "Frappe Sapphire",
                    surfaceColor = Color(0xff303446),
                    accentColor = Color(0xff85c1dc),
                    isSelected = selectedTheme == "FrappeSapphire",
                    onSelect = { onThemeClick("FrappeSapphire") }
                )

                ThemeItem(
                    themeName = "Frappe Blue",
                    surfaceColor = Color(0xff303446),
                    accentColor = Color(0xff8caaee),
                    isSelected = selectedTheme == "FrappeBlue",
                    onSelect = { onThemeClick("FrappeBlue") }
                )

                ThemeItem(
                    themeName = "Frappe Lavender",
                    surfaceColor = Color(0xff303446),
                    accentColor = Color(0xffbabbf1),
                    isSelected = selectedTheme == "FrappeLavender",
                    onSelect = { onThemeClick("FrappeLavender") }
                )

                Spacer(modifier = Modifier.height(5.dp))
                Divider(color = MaterialTheme.colorScheme.surface)
                Spacer(modifier = Modifier.height(5.dp))

                ThemeItem(
                    themeName = "Macchiato Rosewater",
                    surfaceColor = Color(0xff24273a),
                    accentColor = Color(0xfff5e0dc),
                    isSelected = selectedTheme == "MacchiatoRosewater",
                    onSelect = { onThemeClick("MacchiatoRosewater") }
                )

                ThemeItem(
                    themeName = "Macchiato Flamingo",
                    surfaceColor = Color(0xff24273a),
                    accentColor = Color(0xfff2cdcd),
                    isSelected = selectedTheme == "MacchiatoFlamingo",
                    onSelect = { onThemeClick("MacchiatoFlamingo") }
                )

                ThemeItem(
                    themeName = "Macchiato Pink",
                    surfaceColor = Color(0xff24273a),
                    accentColor = Color(0xfff5c2e7),
                    isSelected = selectedTheme == "MacchiatoPink",
                    onSelect = { onThemeClick("MacchiatoPink") }
                )

                ThemeItem(
                    themeName = "Macchiato Mauve",
                    surfaceColor = Color(0xff24273a),
                    accentColor = Color(0xffcba6f7),
                    isSelected = selectedTheme == "MacchiatoMauve",
                    onSelect = { onThemeClick("MacchiatoMauve") }
                )

                ThemeItem(
                    themeName = "Macchiato Red",
                    surfaceColor = Color(0xff24273a),
                    accentColor = Color(0xfff38ba8),
                    isSelected = selectedTheme == "MacchiatoRed",
                    onSelect = { onThemeClick("MacchiatoRed") }
                )

                ThemeItem(
                    themeName = "Macchiato Maroon",
                    surfaceColor = Color(0xff24273a),
                    accentColor = Color(0xffeba0ac),
                    isSelected = selectedTheme == "MacchiatoMaroon",
                    onSelect = { onThemeClick("MacchiatoMaroon") }
                )

                ThemeItem(
                    themeName = "Macchiato Peach",
                    surfaceColor = Color(0xff24273a),
                    accentColor = Color(0xfffab387),
                    isSelected = selectedTheme == "MacchiatoPeach",
                    onSelect = { onThemeClick("MacchiatoPeach") }
                )

                ThemeItem(
                    themeName = "Macchiato Yellow",
                    surfaceColor = Color(0xff24273a),
                    accentColor = Color(0xfff9e2af),
                    isSelected = selectedTheme == "MacchiatoYellow",
                    onSelect = { onThemeClick("MacchiatoYellow") }
                )

                ThemeItem(
                    themeName = "Macchiato Green",
                    surfaceColor = Color(0xff24273a),
                    accentColor = Color(0xffa6e3a1),
                    isSelected = selectedTheme == "MacchiatoGreen",
                    onSelect = { onThemeClick("MacchiatoGreen") }
                )

                ThemeItem(
                    themeName = "Macchiato Teal",
                    surfaceColor = Color(0xff24273a),
                    accentColor = Color(0xff94e2d5),
                    isSelected = selectedTheme == "MacchiatoTeal",
                    onSelect = { onThemeClick("MacchiatoTeal") }
                )

                ThemeItem(
                    themeName = "Macchiato Sky",
                    surfaceColor = Color(0xff24273a),
                    accentColor = Color(0xff89dceb),
                    isSelected = selectedTheme == "MacchiatoSky",
                    onSelect = { onThemeClick("MacchiatoSky") }
                )

                ThemeItem(
                    themeName = "Macchiato Sapphire",
                    surfaceColor = Color(0xff24273a),
                    accentColor = Color(0xff74c7ec),
                    isSelected = selectedTheme == "MacchiatoSapphire",
                    onSelect = { onThemeClick("MacchiatoSapphire") }
                )

                ThemeItem(
                    themeName = "Macchiato Blue",
                    surfaceColor = Color(0xff24273a),
                    accentColor = Color(0xff89b4fa),
                    isSelected = selectedTheme == "MacchiatoBlue",
                    onSelect = { onThemeClick("MacchiatoBlue") }
                )

                ThemeItem(
                    themeName = "Macchiato Lavender",
                    surfaceColor = Color(0xff24273a),
                    accentColor = Color(0xffb4befe),
                    isSelected = selectedTheme == "MacchiatoLavender",
                    onSelect = { onThemeClick("MacchiatoLavender") }
                )

                Spacer(modifier = Modifier.height(5.dp))
                Divider(color = MaterialTheme.colorScheme.surface)
                Spacer(modifier = Modifier.height(5.dp))

                ThemeItem(
                    themeName = "Mocha Rosewater",
                    surfaceColor = Color(0xff1e1e2e),
                    accentColor = Color(0xfff5e0dc),
                    isSelected = selectedTheme == "MochaRosewater",
                    onSelect = { onThemeClick("MochaRosewater") }
                )

                ThemeItem(
                    themeName = "Mocha Flamingo",
                    surfaceColor = Color(0xff1e1e2e),
                    accentColor = Color(0xfff2cdcd),
                    isSelected = selectedTheme == "MochaFlamingo",
                    onSelect = { onThemeClick("MochaFlamingo") }
                )

                ThemeItem(
                    themeName = "Mocha Pink",
                    surfaceColor = Color(0xff1e1e2e),
                    accentColor = Color(0xfff5c2e7),
                    isSelected = selectedTheme == "MochaPink",
                    onSelect = { onThemeClick("MochaPink") }
                )

                ThemeItem(
                    themeName = "Mocha Mauve",
                    surfaceColor = Color(0xff1e1e2e),
                    accentColor = Color(0xffcba6f7),
                    isSelected = selectedTheme == "MochaMauve",
                    onSelect = { onThemeClick("MochaMauve") }
                )

                ThemeItem(
                    themeName = "Mocha Red",
                    surfaceColor = Color(0xff1e1e2e),
                    accentColor = Color(0xfff38ba8),
                    isSelected = selectedTheme == "MochaRed",
                    onSelect = { onThemeClick("MochaRed") }
                )

                ThemeItem(
                    themeName = "Mocha Maroon",
                    surfaceColor = Color(0xff1e1e2e),
                    accentColor = Color(0xffeba0ac),
                    isSelected = selectedTheme == "MochaMaroon",
                    onSelect = { onThemeClick("MochaMaroon") }
                )

                ThemeItem(
                    themeName = "Mocha Peach",
                    surfaceColor = Color(0xff1e1e2e),
                    accentColor = Color(0xfffab387),
                    isSelected = selectedTheme == "MochaPeach",
                    onSelect = { onThemeClick("MochaPeach") }
                )

                ThemeItem(
                    themeName = "Mocha Yellow",
                    surfaceColor = Color(0xff1e1e2e),
                    accentColor = Color(0xfff9e2af),
                    isSelected = selectedTheme == "MochaYellow",
                    onSelect = { onThemeClick("MochaYellow") }
                )

                ThemeItem(
                    themeName = "Mocha Green",
                    surfaceColor = Color(0xff1e1e2e),
                    accentColor = Color(0xffa6e3a1),
                    isSelected = selectedTheme == "MochaGreen",
                    onSelect = { onThemeClick("MochaGreen") }
                )

                ThemeItem(
                    themeName = "Mocha Teal",
                    surfaceColor = Color(0xff1e1e2e),
                    accentColor = Color(0xff94e2d5),
                    isSelected = selectedTheme == "MochaTeal",
                    onSelect = { onThemeClick("MochaTeal") }
                )

                ThemeItem(
                    themeName = "Mocha Sky",
                    surfaceColor = Color(0xff1e1e2e),
                    accentColor = Color(0xff89dceb),
                    isSelected = selectedTheme == "MochaSky",
                    onSelect = { onThemeClick("MochaSky") }
                )

                ThemeItem(
                    themeName = "Mocha Sapphire",
                    surfaceColor = Color(0xff1e1e2e),
                    accentColor = Color(0xff74c7ec),
                    isSelected = selectedTheme == "MochaSapphire",
                    onSelect = { onThemeClick("MochaSapphire") }
                )

                ThemeItem(
                    themeName = "Mocha Blue",
                    surfaceColor = Color(0xff1e1e2e),
                    accentColor = Color(0xff89b4fa),
                    isSelected = selectedTheme == "MochaBlue",
                    onSelect = { onThemeClick("MochaBlue") }
                )

                ThemeItem(
                    themeName = "Mocha Lavender",
                    surfaceColor = Color(0xff1e1e2e),
                    accentColor = Color(0xffb4befe),
                    isSelected = selectedTheme == "MochaLavender",
                    onSelect = { onThemeClick("MochaLavender") }
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
