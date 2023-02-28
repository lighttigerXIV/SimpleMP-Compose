package com.lighttigerxiv.simple.mp.compose.screens.main.settings

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.lighttigerxiv.simple.mp.compose.SCREEN_PADDING
import com.lighttigerxiv.simple.mp.compose.composables.CustomToolbar
import com.lighttigerxiv.simple.mp.compose.composables.CustomTextField
import com.lighttigerxiv.simple.mp.compose.getAppString
import com.lighttigerxiv.simple.mp.compose.app_viewmodels.MainVM
import com.lighttigerxiv.simple.mp.compose.app_viewmodels.SettingsVM
import com.lighttigerxiv.simple.mp.compose.composables.spacers.SmallHeightSpacer

@Composable
fun SettingsScreen(
    mainVM: MainVM,
    settingsVM: SettingsVM,
    settingsScreenVM: SettingsScreenVM,
    onBackPressed: () -> Unit,
    onOpenScreen: (route: String) -> Unit
) {

    val context = LocalContext.current

    val surfaceColor = mainVM.surfaceColor.collectAsState().value

    val screenLoaded = settingsScreenVM.screenLoaded.collectAsState().value

    val showThemeModeDialog = settingsScreenVM.showThemeModeDialog.collectAsState().value

    val showDarkModeDialog = settingsScreenVM.showDarkModeDialog.collectAsState().value

    val showFilterAudioDialog = settingsScreenVM.showFilterAudioDialog.collectAsState().value

    val themeModeSetting = settingsVM.themeModeSetting.collectAsState().value

    val darkModeSetting = settingsVM.darkModeSetting.collectAsState().value

    val themeAccentSetting = settingsVM.themeAccentSetting.collectAsState().value

    val filterAudioSetting = settingsVM.filterAudioSetting.collectAsState().value

    val downloadArtistCoverSetting = settingsVM.downloadArtistCoverSetting.collectAsState().value

    val downloadOverDataSetting = settingsVM.downloadOverDataSetting.collectAsState().value

    val selectedThemeMode = settingsScreenVM.selectedThemeMode.collectAsState().value

    val selectedDarkMode = settingsScreenVM.selectedDarkMode.collectAsState().value

    val filterAudioText = settingsScreenVM.filterAudioText.collectAsState().value


    if (!screenLoaded) {
        settingsScreenVM.loadScreen(settingsVM)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(surfaceColor)
            .padding(SCREEN_PADDING)
            .verticalScroll(rememberScrollState())
    ) {


        CustomToolbar(
            backText = remember { getAppString(context, R.string.Home) },
            onBackClick = { onBackPressed() }
        )

        SmallHeightSpacer()

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = remember { getAppString(context, R.string.Theming) },
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp,
                textAlign = TextAlign.Start,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
        }

        SmallHeightSpacer()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {

            DefaultSettingItem(
                icon = painterResource(id = R.drawable.icon_theme_mode_regular),
                settingText = remember { getAppString(context, R.string.Theme) },
                settingValue = selectedThemeMode,
                onSettingClick = {
                    settingsScreenVM.updateShowThemeModeDialog(true)
                },
            )

            DefaultSettingItem(
                icon = painterResource(id = R.drawable.icon_moon_regular),
                settingText = remember { getAppString(context, R.string.DarkMode) },
                settingValue = selectedDarkMode,
                onSettingClick = {
                    settingsScreenVM.updateShowDarkModeDialog(true)
                }
            )

            DefaultSettingItem(
                icon = painterResource(id = R.drawable.brush),
                settingText = remember { getAppString(context, R.string.AccentColor) },
                settingValue = themeAccentSetting,
                onSettingClick = { onOpenScreen("Themes") }
            )
        }


        if (showThemeModeDialog) {

            Dialog(
                onDismissRequest = {

                    settingsScreenVM.updateShowThemeModeDialog(false)

                    settingsScreenVM.updateSelectedThemeMode(themeModeSetting)
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
                                text = remember { getAppString(context, R.string.SelectThemeMode) },
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
                                    settingsScreenVM.updateSelectedThemeMode("System")
                                }
                        ) {

                            RadioButton(
                                selected = selectedThemeMode == "System",
                                onClick = {

                                    settingsScreenVM.updateSelectedThemeMode("System")
                                }
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = remember { getAppString(context, R.string.SystemDefault) },
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
                                    settingsScreenVM.updateSelectedThemeMode("Light")
                                }
                        ) {

                            RadioButton(
                                selected = selectedThemeMode == "Light",
                                onClick = {
                                    settingsScreenVM.updateSelectedThemeMode("Light")
                                }
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = remember { getAppString(context, R.string.LightMode) },
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
                                    settingsScreenVM.updateSelectedThemeMode("Dark")
                                }
                        ) {

                            RadioButton(
                                selected = selectedThemeMode == "Dark",
                                onClick = {
                                    settingsScreenVM.updateSelectedThemeMode("Dark")
                                }
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = remember { getAppString(context, R.string.DarkMode) },
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

                                    settingsScreenVM.updateShowThemeModeDialog(false)

                                    settingsScreenVM.updateSelectedThemeMode(themeModeSetting)
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
                                    text = remember { getAppString(context, R.string.Cancel) },
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Button(
                                onClick = {

                                    settingsVM.updateThemeModeSetting(selectedThemeMode)

                                    settingsScreenVM.updateShowThemeModeDialog(false)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                ),
                                modifier = Modifier
                                    .weight(0.5f)
                                    .padding(5.dp)
                            ) {
                                Text(
                                    text = remember { getAppString(context, R.string.Apply) },
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
                text = remember { getAppString(context, R.string.Audio) },
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
                settingText = remember { getAppString(context, R.string.FilterAudioBelow) },
                settingValue = "$filterAudioSetting seconds",
                onSettingClick = {

                    settingsScreenVM.updateShowFilterAudioDialog(true)
                }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = remember { getAppString(context, R.string.Data) },
            color = MaterialTheme.colorScheme.primary,
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {

            SwitchSettingItem(
                icon = painterResource(id = R.drawable.icon_database),
                settingText = remember { getAppString(context, R.string.DownloadArtistCoverFromInternet) },
                settingValue = downloadArtistCoverSetting,
                onToggle = {

                    settingsVM.updateDownloadArtistCoverSetting(!downloadArtistCoverSetting)
                }
            )

            SwitchSettingItem(
                icon = painterResource(id = R.drawable.icon_database),
                settingText = remember { getAppString(context, R.string.DownloadArtistCoverOnData) },
                settingValue = downloadOverDataSetting,
                onToggle = {

                    settingsVM.updateDownloadOverDataSetting(!downloadOverDataSetting)
                },
                enabled = downloadArtistCoverSetting
            )
        }


        if (showDarkModeDialog) {

            Dialog(
                onDismissRequest = {

                    settingsScreenVM.updateShowDarkModeDialog(false)

                    settingsScreenVM.updateSelectedDarkMode(darkModeSetting)
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
                                text = remember { getAppString(context, R.string.SelectDarkMode) },
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

                                    settingsScreenVM.updateSelectedDarkMode("Color")
                                }
                        ) {

                            RadioButton(
                                selected = selectedDarkMode == "Color",
                                onClick = {

                                    settingsScreenVM.updateSelectedDarkMode("Color")
                                }
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = remember { getAppString(context, R.string.Color) },
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
                                    settingsScreenVM.updateSelectedDarkMode("Oled")
                                }
                        ) {

                            RadioButton(
                                selected = selectedDarkMode == "Oled",
                                onClick = {
                                    settingsScreenVM.updateSelectedDarkMode("Oled")
                                }
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = remember { getAppString(context, R.string.Oled) },
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

                                    settingsScreenVM.updateShowDarkModeDialog(false)

                                    settingsScreenVM.updateSelectedDarkMode(darkModeSetting)
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
                                    text = remember { getAppString(context, R.string.Cancel) },
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Button(
                                onClick = {

                                    settingsVM.updateDarkModeSetting(selectedDarkMode)

                                    settingsScreenVM.updateShowDarkModeDialog(false)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                ),
                                modifier = Modifier
                                    .weight(0.5f)
                                    .padding(5.dp)
                            ) {
                                Text(
                                    text = remember { getAppString(context, R.string.Apply) },
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }

        if (showFilterAudioDialog) {

            Dialog(
                onDismissRequest = {

                    settingsScreenVM.updateShowFilterAudioDialog(false)

                    settingsScreenVM.updateFilterAudioText(filterAudioSetting)
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
                                text = remember { getAppString(context, R.string.FilterAudio) },
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        CustomTextField(
                            text = filterAudioText,
                            placeholder = remember { getAppString(context, R.string.InsertMinimumSeconds) },
                            textType = "number",
                            onTextChange = {

                                settingsScreenVM.updateFilterAudioText(it)
                            }
                        )

                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Button(
                                onClick = {

                                    settingsScreenVM.updateShowFilterAudioDialog(false)

                                    settingsScreenVM.updateFilterAudioText(filterAudioSetting)
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
                                    text = remember { getAppString(context, R.string.Cancel) },
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Button(
                                onClick = {

                                    settingsVM.updateFilterAudioSetting(filterAudioText)

                                    settingsScreenVM.updateShowFilterAudioDialog(false)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                ),
                                enabled = filterAudioText.trim().isNotEmpty(),
                                modifier = Modifier
                                    .weight(0.5f)
                                    .padding(5.dp)
                            ) {
                                Text(
                                    text = remember { getAppString(context, R.string.Apply) },
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
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
            .clickable { onSettingClick() }
            .padding(10.dp)
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
fun SwitchSettingItem(
    icon: Painter,
    settingText: String,
    settingValue: Boolean,
    onToggle: () -> Unit = {},
    enabled: Boolean = true
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { if (enabled) onToggle() }
            .padding(10.dp)
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

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            text = settingText,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(Modifier.width(10.dp))

        Switch(
            checked = settingValue,
            onCheckedChange = { onToggle() },
            enabled = enabled
        )
    }
}

