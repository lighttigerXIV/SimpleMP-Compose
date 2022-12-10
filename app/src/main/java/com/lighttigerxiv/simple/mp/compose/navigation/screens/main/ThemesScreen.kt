package com.lighttigerxiv.simple.mp.compose.navigation.screens.main

import android.os.Build
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.lighttigerxiv.simple.mp.compose.SCREEN_PADDING
import com.lighttigerxiv.simple.mp.compose.composables.CustomToolbar
import com.lighttigerxiv.simple.mp.compose.composables.CustomText
import com.lighttigerxiv.simple.mp.compose.getAppString
import com.lighttigerxiv.simple.mp.compose.viewmodels.ActivityMainVM

@Composable
fun ThemesScreen(
    activityMainVM: ActivityMainVM,
    onBackClick: () -> Unit
) {

    val context = LocalContext.current
    val themeAccent = activityMainVM.themeAccentSetting.collectAsState().value
    val showCommonThemes = activityMainVM.showCommonThemesInThemesScreen.collectAsState().value
    val showCatppuccinThemes = activityMainVM.showCatppuccinThemesInThemesScreen.collectAsState().value


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(activityMainVM.surfaceColor.collectAsState().value)
            .padding(SCREEN_PADDING)
    ) {

        CustomToolbar(
            backText = remember { getAppString(context, R.string.Settings) },
            onBackClick = { onBackClick() }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
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
                            .clickable { activityMainVM.setThemeAccent("System") }
                            .padding(14.dp)
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            RadioButton(
                                selected = themeAccent == "System",
                                onClick = { activityMainVM.setThemeAccent("System") }
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
                            ) { activityMainVM.toggleShowCommonThemeInThemesScreen() },
                        text = remember { getAppString(context, R.string.Common) },
                        size = 24.sp,
                        weight = FontWeight.Bold
                    )
                }


                if (showCommonThemes) {

                    Spacer(modifier = Modifier.height(10.dp))

                    ThemeItem(
                        themeName = remember { getAppString(context, R.string.Blue) },
                        surfaceColor = Color(0xFF225FA6),
                        accentColor = Color(0xFF225FA6),
                        isSelected = themeAccent == "Blue",
                        onSelect = { activityMainVM.setThemeAccent("Blue") }
                    )

                    ThemeItem(
                        themeName = remember { getAppString(context, R.string.Red) },
                        surfaceColor = Color(0xFFBF0027),
                        accentColor = Color(0xFFBF0027),
                        isSelected = themeAccent == "Red",
                        onSelect = { activityMainVM.setThemeAccent("Red") }
                    )

                    ThemeItem(
                        themeName = remember { getAppString(context, R.string.Purple) },
                        surfaceColor = Color(0xFF6750A4),
                        accentColor = Color(0xFF6750A4),
                        isSelected = themeAccent == "Purple",
                        onSelect = { activityMainVM.setThemeAccent("Purple") }
                    )

                    ThemeItem(
                        themeName = remember { getAppString(context, R.string.Orange) },
                        surfaceColor = Color(0xFF924B00),
                        accentColor = Color(0xFF924B00),
                        isSelected = themeAccent == "Orange",
                        onSelect = { activityMainVM.setThemeAccent("Orange") }
                    )

                    ThemeItem(
                        themeName = remember { getAppString(context, R.string.Yellow) },
                        surfaceColor = Color(0xFF6D5E00),
                        accentColor = Color(0xFF6D5E00),
                        isSelected = themeAccent == "Yellow",
                        onSelect = { activityMainVM.setThemeAccent("Yellow") }
                    )

                    ThemeItem(
                        themeName = remember { getAppString(context, R.string.Green) },
                        surfaceColor = Color(0xFF326B00),
                        accentColor = Color(0xFF326B00),
                        isSelected = themeAccent == "Green",
                        onSelect = { activityMainVM.setThemeAccent("Green") }
                    )

                    ThemeItem(
                        themeName = remember { getAppString(context, R.string.Pink) },
                        surfaceColor = Color(0xFF84468E),
                        accentColor = Color(0xFF84468E),
                        isSelected = themeAccent == "Pink",
                        onSelect = { activityMainVM.setThemeAccent("Pink") }
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
            ){
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
                            ) { activityMainVM.toggleShowCatppuccinThemeInThemesScreen() },
                        text = "Catppuccin",
                        size = 24.sp,
                        weight = FontWeight.Bold
                    )
                }

                if (showCatppuccinThemes) {

                    Spacer(modifier = Modifier.height(10.dp))

                    ThemeItem(
                        themeName = "Frappe Rosewater",
                        surfaceColor = Color(0xff303446),
                        accentColor = Color(0xfff2d5cf),
                        isSelected = themeAccent == "FrappeRosewater",
                        onSelect = { activityMainVM.setThemeAccent("FrappeRosewater") }
                    )

                    ThemeItem(
                        themeName = "Frappe Flamingo",
                        surfaceColor = Color(0xff303446),
                        accentColor = Color(0xffeebebe),
                        isSelected = themeAccent == "FrappeFlamingo",
                        onSelect = { activityMainVM.setThemeAccent("FrappeFlamingo") }
                    )

                    ThemeItem(
                        themeName = "Frappe Pink",
                        surfaceColor = Color(0xff303446),
                        accentColor = Color(0xfff4b8e4),
                        isSelected = themeAccent == "FrappePink",
                        onSelect = { activityMainVM.setThemeAccent("FrappePink") }
                    )

                    ThemeItem(
                        themeName = "Frappe Mauve",
                        surfaceColor = Color(0xff303446),
                        accentColor = Color(0xffca9ee6),
                        isSelected = themeAccent == "FrappeMauve",
                        onSelect = { activityMainVM.setThemeAccent("FrappeMauve") }
                    )

                    ThemeItem(
                        themeName = "Frappe Red",
                        surfaceColor = Color(0xff303446),
                        accentColor = Color(0xffe78284),
                        isSelected = themeAccent == "FrappeRed",
                        onSelect = { activityMainVM.setThemeAccent("FrappeRed") }
                    )

                    ThemeItem(
                        themeName = "Frappe Maroon",
                        surfaceColor = Color(0xff303446),
                        accentColor = Color(0xffea999c),
                        isSelected = themeAccent == "FrappeMaroon",
                        onSelect = { activityMainVM.setThemeAccent("FrappeMaroon") }
                    )

                    ThemeItem(
                        themeName = "Frappe Peach",
                        surfaceColor = Color(0xff303446),
                        accentColor = Color(0xffef9f76),
                        isSelected = themeAccent == "FrappePeach",
                        onSelect = { activityMainVM.setThemeAccent("FrappePeach") }
                    )

                    ThemeItem(
                        themeName = "Frappe Yellow",
                        surfaceColor = Color(0xff303446),
                        accentColor = Color(0xffe5c890),
                        isSelected = themeAccent == "FrappeYellow",
                        onSelect = { activityMainVM.setThemeAccent("FrappeYellow") }
                    )

                    ThemeItem(
                        themeName = "Frappe Green",
                        surfaceColor = Color(0xff303446),
                        accentColor = Color(0xffa6d189),
                        isSelected = themeAccent == "FrappeGreen",
                        onSelect = { activityMainVM.setThemeAccent("FrappeGreen") }
                    )

                    ThemeItem(
                        themeName = "Frappe Teal",
                        surfaceColor = Color(0xff303446),
                        accentColor = Color(0xff81c8be),
                        isSelected = themeAccent == "FrappeTeal",
                        onSelect = { activityMainVM.setThemeAccent("FrappeTeal") }
                    )

                    ThemeItem(
                        themeName = "Frappe Sky",
                        surfaceColor = Color(0xff303446),
                        accentColor = Color(0xff99d1db),
                        isSelected = themeAccent == "FrappeSky",
                        onSelect = { activityMainVM.setThemeAccent("FrappeSky") }
                    )

                    ThemeItem(
                        themeName = "Frappe Sapphire",
                        surfaceColor = Color(0xff303446),
                        accentColor = Color(0xff85c1dc),
                        isSelected = themeAccent == "FrappeSapphire",
                        onSelect = { activityMainVM.setThemeAccent("FrappeSapphire") }
                    )

                    ThemeItem(
                        themeName = "Frappe Blue",
                        surfaceColor = Color(0xff303446),
                        accentColor = Color(0xff8caaee),
                        isSelected = themeAccent == "FrappeBlue",
                        onSelect = { activityMainVM.setThemeAccent("FrappeBlue") }
                    )

                    ThemeItem(
                        themeName = "Frappe Lavender",
                        surfaceColor = Color(0xff303446),
                        accentColor = Color(0xffbabbf1),
                        isSelected = themeAccent == "FrappeLavender",
                        onSelect = { activityMainVM.setThemeAccent("FrappeLavender") }
                    )

                    Spacer(modifier = Modifier.height(5.dp))
                    Divider(color = MaterialTheme.colorScheme.surface)
                    Spacer(modifier = Modifier.height(5.dp))

                    ThemeItem(
                        themeName = "Macchiato Rosewater",
                        surfaceColor = Color(0xff24273a),
                        accentColor = Color(0xfff5e0dc),
                        isSelected = themeAccent == "MacchiatoRosewater",
                        onSelect = { activityMainVM.setThemeAccent("MacchiatoRosewater") }
                    )

                    ThemeItem(
                        themeName = "Macchiato Flamingo",
                        surfaceColor = Color(0xff24273a),
                        accentColor = Color(0xfff2cdcd),
                        isSelected = themeAccent == "MacchiatoFlamingo",
                        onSelect = { activityMainVM.setThemeAccent("MacchiatoFlamingo") }
                    )

                    ThemeItem(
                        themeName = "Macchiato Pink",
                        surfaceColor = Color(0xff24273a),
                        accentColor = Color(0xfff5c2e7),
                        isSelected = themeAccent == "MacchiatoPink",
                        onSelect = { activityMainVM.setThemeAccent("MacchiatoPink") }
                    )

                    ThemeItem(
                        themeName = "Macchiato Mauve",
                        surfaceColor = Color(0xff24273a),
                        accentColor = Color(0xffcba6f7),
                        isSelected = themeAccent == "MacchiatoMauve",
                        onSelect = { activityMainVM.setThemeAccent("MacchiatoMauve") }
                    )

                    ThemeItem(
                        themeName = "Macchiato Red",
                        surfaceColor = Color(0xff24273a),
                        accentColor = Color(0xfff38ba8),
                        isSelected = themeAccent == "MacchiatoRed",
                        onSelect = { activityMainVM.setThemeAccent("MacchiatoRed") }
                    )

                    ThemeItem(
                        themeName = "Macchiato Maroon",
                        surfaceColor = Color(0xff24273a),
                        accentColor = Color(0xffeba0ac),
                        isSelected = themeAccent == "MacchiatoMaroon",
                        onSelect = { activityMainVM.setThemeAccent("MacchiatoMaroon") }
                    )

                    ThemeItem(
                        themeName = "Macchiato Peach",
                        surfaceColor = Color(0xff24273a),
                        accentColor = Color(0xfffab387),
                        isSelected = themeAccent == "MacchiatoPeach",
                        onSelect = { activityMainVM.setThemeAccent("MacchiatoPeach") }
                    )

                    ThemeItem(
                        themeName = "Macchiato Yellow",
                        surfaceColor = Color(0xff24273a),
                        accentColor = Color(0xfff9e2af),
                        isSelected = themeAccent == "MacchiatoYellow",
                        onSelect = { activityMainVM.setThemeAccent("MacchiatoYellow") }
                    )

                    ThemeItem(
                        themeName = "Macchiato Green",
                        surfaceColor = Color(0xff24273a),
                        accentColor = Color(0xffa6e3a1),
                        isSelected = themeAccent == "MacchiatoGreen",
                        onSelect = { activityMainVM.setThemeAccent("MacchiatoGreen") }
                    )

                    ThemeItem(
                        themeName = "Macchiato Teal",
                        surfaceColor = Color(0xff24273a),
                        accentColor = Color(0xff94e2d5),
                        isSelected = themeAccent == "MacchiatoTeal",
                        onSelect = { activityMainVM.setThemeAccent("MacchiatoTeal") }
                    )

                    ThemeItem(
                        themeName = "Macchiato Sky",
                        surfaceColor = Color(0xff24273a),
                        accentColor = Color(0xff89dceb),
                        isSelected = themeAccent == "MacchiatoSky",
                        onSelect = { activityMainVM.setThemeAccent("MacchiatoSky") }
                    )

                    ThemeItem(
                        themeName = "Macchiato Sapphire",
                        surfaceColor = Color(0xff24273a),
                        accentColor = Color(0xff74c7ec),
                        isSelected = themeAccent == "MacchiatoSapphire",
                        onSelect = { activityMainVM.setThemeAccent("MacchiatoSapphire") }
                    )

                    ThemeItem(
                        themeName = "Macchiato Blue",
                        surfaceColor = Color(0xff24273a),
                        accentColor = Color(0xff89b4fa),
                        isSelected = themeAccent == "MacchiatoBlue",
                        onSelect = { activityMainVM.setThemeAccent("MacchiatoBlue") }
                    )

                    ThemeItem(
                        themeName = "Macchiato Lavender",
                        surfaceColor = Color(0xff24273a),
                        accentColor = Color(0xffb4befe),
                        isSelected = themeAccent == "MacchiatoLavender",
                        onSelect = { activityMainVM.setThemeAccent("MacchiatoLavender") }
                    )

                    Spacer(modifier = Modifier.height(5.dp))
                    Divider(color = MaterialTheme.colorScheme.surface)
                    Spacer(modifier = Modifier.height(5.dp))

                    ThemeItem(
                        themeName = "Mocha Rosewater",
                        surfaceColor = Color(0xff1e1e2e),
                        accentColor = Color(0xfff5e0dc),
                        isSelected = themeAccent == "MochaRosewater",
                        onSelect = { activityMainVM.setThemeAccent("MochaRosewater") }
                    )

                    ThemeItem(
                        themeName = "Mocha Flamingo",
                        surfaceColor = Color(0xff1e1e2e),
                        accentColor = Color(0xfff2cdcd),
                        isSelected = themeAccent == "MochaFlamingo",
                        onSelect = { activityMainVM.setThemeAccent("MochaFlamingo") }
                    )

                    ThemeItem(
                        themeName = "Mocha Pink",
                        surfaceColor = Color(0xff1e1e2e),
                        accentColor = Color(0xfff5c2e7),
                        isSelected = themeAccent == "MochaPink",
                        onSelect = { activityMainVM.setThemeAccent("MochaPink") }
                    )

                    ThemeItem(
                        themeName = "Mocha Mauve",
                        surfaceColor = Color(0xff1e1e2e),
                        accentColor = Color(0xffcba6f7),
                        isSelected = themeAccent == "MochaMauve",
                        onSelect = { activityMainVM.setThemeAccent("MochaMauve") }
                    )

                    ThemeItem(
                        themeName = "Mocha Red",
                        surfaceColor = Color(0xff1e1e2e),
                        accentColor = Color(0xfff38ba8),
                        isSelected = themeAccent == "MochaRed",
                        onSelect = { activityMainVM.setThemeAccent("MochaRed") }
                    )

                    ThemeItem(
                        themeName = "Mocha Maroon",
                        surfaceColor = Color(0xff1e1e2e),
                        accentColor = Color(0xffeba0ac),
                        isSelected = themeAccent == "MochaMaroon",
                        onSelect = { activityMainVM.setThemeAccent("MochaMaroon") }
                    )

                    ThemeItem(
                        themeName = "Mocha Peach",
                        surfaceColor = Color(0xff1e1e2e),
                        accentColor = Color(0xfffab387),
                        isSelected = themeAccent == "MochaPeach",
                        onSelect = { activityMainVM.setThemeAccent("MochaPeach") }
                    )

                    ThemeItem(
                        themeName = "Mocha Yellow",
                        surfaceColor = Color(0xff1e1e2e),
                        accentColor = Color(0xfff9e2af),
                        isSelected = themeAccent == "MochaYellow",
                        onSelect = { activityMainVM.setThemeAccent("MochaYellow") }
                    )

                    ThemeItem(
                        themeName = "Mocha Green",
                        surfaceColor = Color(0xff1e1e2e),
                        accentColor = Color(0xffa6e3a1),
                        isSelected = themeAccent == "MochaGreen",
                        onSelect = { activityMainVM.setThemeAccent("MochaGreen") }
                    )

                    ThemeItem(
                        themeName = "Mocha Teal",
                        surfaceColor = Color(0xff1e1e2e),
                        accentColor = Color(0xff94e2d5),
                        isSelected = themeAccent == "MochaTeal",
                        onSelect = { activityMainVM.setThemeAccent("MochaTeal") }
                    )

                    ThemeItem(
                        themeName = "Mocha Sky",
                        surfaceColor = Color(0xff1e1e2e),
                        accentColor = Color(0xff89dceb),
                        isSelected = themeAccent == "MochaSky",
                        onSelect = { activityMainVM.setThemeAccent("MochaSky") }
                    )

                    ThemeItem(
                        themeName = "Mocha Sapphire",
                        surfaceColor = Color(0xff1e1e2e),
                        accentColor = Color(0xff74c7ec),
                        isSelected = themeAccent == "MochaSapphire",
                        onSelect = { activityMainVM.setThemeAccent("MochaSapphire") }
                    )

                    ThemeItem(
                        themeName = "Mocha Blue",
                        surfaceColor = Color(0xff1e1e2e),
                        accentColor = Color(0xff89b4fa),
                        isSelected = themeAccent == "MochaBlue",
                        onSelect = { activityMainVM.setThemeAccent("MochaBlue") }
                    )

                    ThemeItem(
                        themeName = "Mocha Lavender",
                        surfaceColor = Color(0xff1e1e2e),
                        accentColor = Color(0xffb4befe),
                        isSelected = themeAccent == "MochaLavender",
                        onSelect = { activityMainVM.setThemeAccent("MochaLavender") }
                    )
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
