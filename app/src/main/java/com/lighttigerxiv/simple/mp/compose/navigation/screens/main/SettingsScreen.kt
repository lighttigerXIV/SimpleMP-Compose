package com.lighttigerxiv.simple.mp.compose.navigation.screens.main

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.composables.BasicToolbar
import com.lighttigerxiv.simple.mp.compose.composables.CustomTextField
import com.lighttigerxiv.simple.mp.compose.viewmodels.ActivityMainVM

@Composable
fun SettingsScreen(
    activityMainVM: ActivityMainVM,
    onBackPressed: () -> Unit
) {

    val context = LocalContext.current
    val showThemeModeDialog = remember { mutableStateOf(false) }
    val showDarkModeDialog = remember { mutableStateOf(false) }
    val showFilterAudioDialog = remember { mutableStateOf(false) }
    val showThemeAccentDialog = remember { mutableStateOf(false) }
    val selectedThemeMode = activityMainVM.selectedThemeModeDialog.observeAsState().value
    val selectedDarkMode = activityMainVM.selectedDarkModeDialog.observeAsState().value
    val selectedThemeAccent = activityMainVM.selectedThemeAccentDialog.observeAsState().value
    val etFilterAudioValue = activityMainVM.etFilterAudioDialog.observeAsState().value
    val restartAppSnackState = remember { SnackbarHostState() }
    val surfaceColor = activityMainVM.surfaceColor.collectAsState().value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(surfaceColor)
            .padding(14.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Column(modifier = Modifier.fillMaxSize()) {

            BasicToolbar(
                backText = "Home",
                onBackClick = { onBackPressed() }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "Theming",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {

                DefaultSettingItem(
                    icon = painterResource(id = R.drawable.icon_theme_mode_regular),
                    settingText = "Theme Mode",
                    settingValue = activityMainVM.themeModeSetting.collectAsState().value!!,
                    onSettingClick = { showThemeModeDialog.value = true },
                )



                DefaultSettingItem(
                    icon = painterResource(id = R.drawable.icon_moon_regular),
                    settingText = "Dark Mode",
                    settingValue = activityMainVM.darkModeSetting.collectAsState().value!!,
                    onSettingClick = { showDarkModeDialog.value = true }
                )



                DefaultSettingItem(
                    icon = painterResource(id = R.drawable.icon_theme_regular),
                    settingText = "Accent Color",
                    settingValue = activityMainVM.themeAccentSetting.collectAsState().value!!,
                    onSettingClick = { showThemeAccentDialog.value = true }
                )
            }


            if (showThemeModeDialog.value) {

                Dialog(
                    onDismissRequest = {
                        showThemeModeDialog.value = false
                        activityMainVM.selectedThemeModeDialog.value = activityMainVM.themeModeSetting.value
                    }
                ) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {

                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Spacer(Modifier.width(10.dp))
                                Text(
                                    text = "Select Theme Mode",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 20.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        activityMainVM.selectedThemeModeDialog.value =
                                            "System"
                                    }
                            ) {

                                RadioButton(
                                    selected = selectedThemeMode == "System",
                                    onClick = {
                                        activityMainVM.selectedThemeModeDialog.value =
                                            "System"
                                    }
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "System Default",
                                    fontSize = 16.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        activityMainVM.selectedThemeModeDialog.value =
                                            "Light"
                                    }
                            ) {

                                RadioButton(
                                    selected = selectedThemeMode == "Light",
                                    onClick = {
                                        activityMainVM.selectedThemeModeDialog.value =
                                            "Light"
                                    }
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Light Mode",
                                    fontSize = 16.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        activityMainVM.selectedThemeModeDialog.value =
                                            "Dark"
                                    }
                            ) {

                                RadioButton(
                                    selected = selectedThemeMode == "Dark",
                                    onClick = {
                                        activityMainVM.selectedThemeModeDialog.value =
                                            "Dark"
                                    }
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Dark Mode",
                                    fontSize = 16.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                Button(
                                    onClick = {
                                        showThemeModeDialog.value = false
                                        activityMainVM.selectedThemeModeDialog.value = activityMainVM.themeModeSetting.value
                                              },
                                    border = BorderStroke(
                                        1.dp,
                                        MaterialTheme.colorScheme.primary
                                    ),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.surface
                                    ),
                                    modifier = Modifier
                                        .weight(0.5f)
                                        .padding(5.dp)
                                ) {
                                    Text(
                                        text = "Cancel",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                Button(
                                    onClick = {
                                        activityMainVM.setThemeMode(); showThemeModeDialog.value =
                                        false
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    ),
                                    modifier = Modifier
                                        .weight(0.5f)
                                        .padding(5.dp)
                                ) {
                                    Text(
                                        text = "Apply",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "Audio",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {

                DefaultSettingItem(
                    icon = painterResource(id = R.drawable.icon_filter_regular),
                    settingText = "Filter Audio Below",
                    settingValue = "${activityMainVM.filterAudioSetting.collectAsState().value} seconds",
                    onSettingClick = { showFilterAudioDialog.value = true }
                )
            }



            if (showDarkModeDialog.value) {

                Dialog(
                    onDismissRequest = {
                        showDarkModeDialog.value = false
                        activityMainVM.selectedDarkModeDialog.value = activityMainVM.darkModeSetting.value
                    }
                ) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {

                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Spacer(Modifier.width(10.dp))
                                Text(
                                    text = "Select Dark Mode",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 20.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        activityMainVM.selectedDarkModeDialog.value =
                                            "Color"
                                    }
                            ) {

                                RadioButton(
                                    selected = selectedDarkMode == "Color",
                                    onClick = {
                                        activityMainVM.selectedDarkModeDialog.value =
                                            "Color"
                                    }
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Color",
                                    fontSize = 16.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        activityMainVM.selectedDarkModeDialog.value =
                                            "Oled"
                                    }
                            ) {

                                RadioButton(
                                    selected = selectedDarkMode == "Oled",
                                    onClick = {
                                        activityMainVM.selectedDarkModeDialog.value =
                                            "Oled"
                                    }
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Oled",
                                    fontSize = 16.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                Button(
                                    onClick = {
                                        showDarkModeDialog.value = false
                                        activityMainVM.selectedDarkModeDialog.value = activityMainVM.darkModeSetting.value
                                    },
                                    border = BorderStroke(
                                        1.dp,
                                        MaterialTheme.colorScheme.primary
                                    ),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.surface
                                    ),
                                    modifier = Modifier
                                        .weight(0.5f)
                                        .padding(5.dp)
                                ) {
                                    Text(
                                        text = "Cancel",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                Button(
                                    onClick = {
                                        activityMainVM.setDarkMode(); showDarkModeDialog.value =
                                        false
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    ),
                                    modifier = Modifier
                                        .weight(0.5f)
                                        .padding(5.dp)
                                ) {
                                    Text(
                                        text = "Apply",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (showFilterAudioDialog.value) {

                Dialog(
                    onDismissRequest = {
                        showFilterAudioDialog.value = false
                        activityMainVM.etFilterAudioDialog.value = activityMainVM.filterAudioSetting.value
                    }
                ) {

                    Surface(
                        shape = RoundedCornerShape(14.dp),
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {

                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Spacer(Modifier.width(10.dp))
                                Text(
                                    text = "Filter Audio",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 20.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            CustomTextField(
                                text = etFilterAudioValue!!,
                                placeholder = "Insert minimum seconds",
                                textType = "number",
                                onTextChange = {
                                    activityMainVM.etFilterAudioDialog.value = it
                                }
                            )

                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                Button(
                                    onClick = {
                                        showFilterAudioDialog.value = false
                                        activityMainVM.etFilterAudioDialog.value = activityMainVM.filterAudioSetting.value
                                    },
                                    border = BorderStroke(
                                        1.dp,
                                        MaterialTheme.colorScheme.primary
                                    ),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.surface
                                    ),
                                    modifier = Modifier
                                        .weight(0.5f)
                                        .padding(5.dp)
                                ) {
                                    Text(
                                        text = "Cancel",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                Button(
                                    onClick = {
                                        activityMainVM.setFilterAudio()
                                        showFilterAudioDialog.value = false
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    ),
                                    enabled = etFilterAudioValue.trim().isNotEmpty(),
                                    modifier = Modifier
                                        .weight(0.5f)
                                        .padding(5.dp)
                                ) {
                                    Text(
                                        text = "Apply",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (showThemeAccentDialog.value) {

                Dialog(
                    onDismissRequest = {
                        showThemeAccentDialog.value = false
                        activityMainVM.selectedThemeAccentDialog.value = activityMainVM.themeAccentSetting.value
                    }
                ) {

                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxHeight(0.8f)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .weight(1f, fill = true)
                                    .padding(10.dp)
                                    .verticalScroll(rememberScrollState())
                            ) {

                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent!!,
                                    text = "System",
                                    settingValue = "System",
                                    firstColor = MaterialTheme.colorScheme.surface,
                                    secondColor = MaterialTheme.colorScheme.surface
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Blue",
                                    settingValue = "Blue",
                                    firstColor = Color(0xFF225FA6),
                                    secondColor = Color(0xFF225FA6)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Red",
                                    settingValue = "Red",
                                    firstColor = Color(0xFFBF0027),
                                    secondColor = Color(0xFFBF0027)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Purple",
                                    settingValue = "Purple",
                                    firstColor = Color(0xFF6750A4),
                                    secondColor = Color(0xFF6750A4)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Orange",
                                    settingValue = "Orange",
                                    firstColor = Color(0xFF924B00),
                                    secondColor = Color(0xFF924B00)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Yellow",
                                    settingValue = "Yellow",
                                    firstColor = Color(0xFF6D5E00),
                                    secondColor = Color(0xFF6D5E00)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Green",
                                    settingValue = "Green",
                                    firstColor = Color(0xFF326B00),
                                    secondColor = Color(0xFF326B00)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Pink",
                                    settingValue = "Pink",
                                    firstColor = Color(0xFF84468E),
                                    secondColor = Color(0xFF84468E)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Frappe Rosewater",
                                    settingValue = "FrappeRosewater",
                                    firstColor = Color(0xff303446),
                                    secondColor = Color(0xfff2d5cf)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Frappe Flamingo",
                                    settingValue = "FrappeFlamingo",
                                    firstColor = Color(0xff303446),
                                    secondColor = Color(0xffeebebe)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Frappe Pink",
                                    settingValue = "FrappePink",
                                    firstColor = Color(0xff303446),
                                    secondColor = Color(0xfff4b8e4)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Frappe Mauve",
                                    settingValue = "FrappeMauve",
                                    firstColor = Color(0xff303446),
                                    secondColor = Color(0xffca9ee6)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Frappe Red",
                                    settingValue = "FrappeRed",
                                    firstColor = Color(0xff303446),
                                    secondColor = Color(0xffe78284)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Frappe Maroon",
                                    settingValue = "FrappeMaroon",
                                    firstColor = Color(0xff303446),
                                    secondColor = Color(0xffea999c)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Frappe Peach",
                                    settingValue = "FrappePeach",
                                    firstColor = Color(0xff303446),
                                    secondColor = Color(0xffef9f76)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Frappe Yellow",
                                    settingValue = "FrappeYellow",
                                    firstColor = Color(0xff303446),
                                    secondColor = Color(0xffe5c890)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Frappe Green",
                                    settingValue = "FrappeGreen",
                                    firstColor = Color(0xff303446),
                                    secondColor = Color(0xffa6d189)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Frappe Teal",
                                    settingValue = "FrappeTeal",
                                    firstColor = Color(0xff303446),
                                    secondColor = Color(0xff81c8be)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Frappe Sky",
                                    settingValue = "FrappeSky",
                                    firstColor = Color(0xff303446),
                                    secondColor = Color(0xff99d1db)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Frappe Sapphire",
                                    settingValue = "FrappeSapphire",
                                    firstColor = Color(0xff303446),
                                    secondColor = Color(0xff85c1dc)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Frappe Blue",
                                    settingValue = "FrappeBlue",
                                    firstColor = Color(0xff303446),
                                    secondColor = Color(0xff8caaee)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Frappe Lavender",
                                    settingValue = "FrappeLavender",
                                    firstColor = Color(0xff303446),
                                    secondColor = Color(0xffbabbf1)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Macchiato Rosewater",
                                    settingValue = "MacchiatoRosewater",
                                    firstColor = Color(0xff24273a),
                                    secondColor = Color(0xfff5e0dc)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Macchiato Flamingo",
                                    settingValue = "MacchiatoFlamingo",
                                    firstColor = Color(0xff24273a),
                                    secondColor = Color(0xfff2cdcd)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Macchiato Pink",
                                    settingValue = "MacchiatoPink",
                                    firstColor = Color(0xff24273a),
                                    secondColor = Color(0xfff5c2e7)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Macchiato Mauve",
                                    settingValue = "MacchiatoMauve",
                                    firstColor = Color(0xff24273a),
                                    secondColor = Color(0xffcba6f7)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Macchiato Red",
                                    settingValue = "MacchiatoRed",
                                    firstColor = Color(0xff24273a),
                                    secondColor = Color(0xfff38ba8)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Macchiato Maroon",
                                    settingValue = "MacchiatoMaroon",
                                    firstColor = Color(0xff24273a),
                                    secondColor = Color(0xffeba0ac)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Macchiato Peach",
                                    settingValue = "MacchiatoPeach",
                                    firstColor = Color(0xff24273a),
                                    secondColor = Color(0xfffab387)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Macchiato Yellow",
                                    settingValue = "MacchiatoYellow",
                                    firstColor = Color(0xff24273a),
                                    secondColor = Color(0xfff9e2af)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Macchiato Green",
                                    settingValue = "MacchiatoGreen",
                                    firstColor = Color(0xff24273a),
                                    secondColor = Color(0xffa6e3a1)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Macchiato Teal",
                                    settingValue = "MacchiatoTeal",
                                    firstColor = Color(0xff24273a),
                                    secondColor = Color(0xff94e2d5)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Macchiato Sky",
                                    settingValue = "MacchiatoSky",
                                    firstColor = Color(0xff24273a),
                                    secondColor = Color(0xff89dceb)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Macchiato Sapphire",
                                    settingValue = "MacchiatoSapphire",
                                    firstColor = Color(0xff24273a),
                                    secondColor = Color(0xff74c7ec)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Macchiato Blue",
                                    settingValue = "MacchiatoBlue",
                                    firstColor = Color(0xff24273a),
                                    secondColor = Color(0xff89b4fa)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Macchiato Lavender",
                                    settingValue = "MacchiatoLavender",
                                    firstColor = Color(0xff24273a),
                                    secondColor = Color(0xffb4befe)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent!!,
                                    text = "Mocha Rosewater",
                                    settingValue = "MochaRosewater",
                                    firstColor = Color(0xff1e1e2e),
                                    secondColor = Color(0xfff5e0dc)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Mocha Flamingo",
                                    settingValue = "MochaFlamingo",
                                    firstColor = Color(0xff1e1e2e),
                                    secondColor = Color(0xfff2cdcd)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Mocha Pink",
                                    settingValue = "MochaPink",
                                    firstColor = Color(0xff1e1e2e),
                                    secondColor = Color(0xfff5c2e7)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Mocha Mauve",
                                    settingValue = "MochaMauve",
                                    firstColor = Color(0xff1e1e2e),
                                    secondColor = Color(0xffcba6f7)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Mocha Red",
                                    settingValue = "MochaRed",
                                    firstColor = Color(0xff1e1e2e),
                                    secondColor = Color(0xfff38ba8)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Mocha Maroon",
                                    settingValue = "MochaMaroon",
                                    firstColor = Color(0xff1e1e2e),
                                    secondColor = Color(0xffeba0ac)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Mocha Peach",
                                    settingValue = "MochaPeach",
                                    firstColor = Color(0xff1e1e2e),
                                    secondColor = Color(0xfffab387)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Mocha Yellow",
                                    settingValue = "MochaYellow",
                                    firstColor = Color(0xff1e1e2e),
                                    secondColor = Color(0xfff9e2af)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Mocha Green",
                                    settingValue = "MochaGreen",
                                    firstColor = Color(0xff1e1e2e),
                                    secondColor = Color(0xffa6e3a1)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Mocha Teal",
                                    settingValue = "MochaTeal",
                                    firstColor = Color(0xff1e1e2e),
                                    secondColor = Color(0xff94e2d5)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Mocha Sky",
                                    settingValue = "MochaSky",
                                    firstColor = Color(0xff1e1e2e),
                                    secondColor = Color(0xff89dceb)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Mocha Sapphire",
                                    settingValue = "MochaSapphire",
                                    firstColor = Color(0xff1e1e2e),
                                    secondColor = Color(0xff74c7ec)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Mocha Blue",
                                    settingValue = "MochaBlue",
                                    firstColor = Color(0xff1e1e2e),
                                    secondColor = Color(0xff89b4fa)
                                )
                                ThemeAccentItem(
                                    activityMainVM = activityMainVM,
                                    selectedThemeAccent = selectedThemeAccent,
                                    text = "Mocha Lavender",
                                    settingValue = "MochaLavender",
                                    firstColor = Color(0xff1e1e2e),
                                    secondColor = Color(0xffb4befe)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                            ) {

                                Button(
                                    onClick = {
                                        showThemeAccentDialog.value = false
                                        activityMainVM.selectedThemeAccentDialog.value = activityMainVM.themeAccentSetting.value
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.surface
                                    ),
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                                ) {
                                    Text(
                                        text = "Cancel",
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Button(
                                    onClick = {
                                        showThemeAccentDialog.value = false
                                        activityMainVM.setThemeAccent()
                                    }
                                ) {
                                    Text(text = "Apply")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    SnackbarHost(hostState = restartAppSnackState) {
        Snackbar(
            actionColor = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            snackbarData = it,
        )
    }
}

@Composable
fun DefaultSettingItem(

    icon: Painter,
    settingText: String,
    settingValue: String,
    onSettingClick: () -> Unit = {}
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp)
            .clickable { onSettingClick() }
    ) {


        Image(
            painter = icon,
            contentDescription = "",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .height(30.dp)
                .width(30.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Text(
                text = settingText,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = settingValue,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

        }
    }
}

@Composable
fun ThemeAccentItem(
    activityMainVM: ActivityMainVM,
    selectedThemeAccent: String,
    text: String,
    settingValue: String,
    firstColor: Color,
    secondColor: Color
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { activityMainVM.selectedThemeAccentDialog.value = settingValue }
    ) {

        RadioButton(
            selected = selectedThemeAccent == settingValue,
            onClick = { activityMainVM.selectedThemeAccentDialog.value = settingValue }
        )
        Text(
            text = text,
            maxLines = 1,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = true)
        )
        Row(
            modifier = Modifier
                .width(30.dp)
                .height(30.dp)
                .clip(RoundedCornerShape(percent = 100))

        ) {
            Box(
                modifier = Modifier
                    .height(30.dp)
                    .fillMaxWidth(0.5f)
                    .background(firstColor)
            )
            Box(
                modifier = Modifier
                    .height(30.dp)
                    .fillMaxWidth()
                    .background(secondColor)
            )
        }
    }
}



