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
import com.lighttigerxiv.simple.mp.compose.data.variables.SCREEN_PADDING
import com.lighttigerxiv.simple.mp.compose.ui.composables.CustomToolbar
import com.lighttigerxiv.simple.mp.compose.ui.composables.CustomTextField
import com.lighttigerxiv.simple.mp.compose.functions.getAppString
import com.lighttigerxiv.simple.mp.compose.activities.main.MainVM
import com.lighttigerxiv.simple.mp.compose.data.variables.MEDIUM_SPACING
import com.lighttigerxiv.simple.mp.compose.data.variables.Routes
import com.lighttigerxiv.simple.mp.compose.data.variables.Settings.Values
import com.lighttigerxiv.simple.mp.compose.functions.isAtLeastAndroid10
import com.lighttigerxiv.simple.mp.compose.settings.SettingsVM
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.MediumHorizontalSpacer
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.MediumVerticalSpacer
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.SmallVerticalSpacer

@Composable
fun SettingsScreen(
    mainVM: MainVM,
    settingsVM: SettingsVM,
    vm: SettingsScreenVM,
    onBackPressed: () -> Unit,
    onOpenScreen: (route: String) -> Unit
) {

    val context = LocalContext.current
    val surfaceColor = mainVM.surfaceColor.collectAsState().value
    val screenLoaded = vm.screenLoaded.collectAsState().value
    val showThemeModeDialog = vm.showThemeModeDialog.collectAsState().value
    val showDarkModeDialog = vm.showDarkModeDialog.collectAsState().value
    val showFilterAudioDialog = vm.showFilterAudioDialog.collectAsState().value
    val themeSetting = settingsVM.colorSchemeSetting.collectAsState().value
    val darkModeSetting = settingsVM.darkModeSetting.collectAsState().value
    val darkColorScheme = settingsVM.darkColorSchemeSetting.collectAsState().value
    val lightColorScheme = settingsVM.lightColorSchemeSetting.collectAsState().value
    val filterAudioSetting = settingsVM.filterAudioSetting.collectAsState().value
    val downloadArtistCoverSetting = settingsVM.downloadArtistCoverSetting.collectAsState().value
    val downloadOverDataSetting = settingsVM.downloadOverDataSetting.collectAsState().value
    val selectedThemeMode = vm.selectedColorScheme.collectAsState().value
    val selectedDarkMode = vm.selectedDarkMode.collectAsState().value
    val filterAudioText = vm.filterAudioText.collectAsState().value


    if (!screenLoaded) {
        vm.loadScreen(settingsVM)
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

        SmallVerticalSpacer()



        Text(
            text = remember { getAppString(context, R.string.Theming) },
            color = MaterialTheme.colorScheme.primary,
            fontSize = 22.sp,
            textAlign = TextAlign.Start,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = 8.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {

            DefaultSettingItem(
                icon = painterResource(id = R.drawable.colorscheme),
                settingText = remember { getAppString(context, R.string.ColorScheme) },
                settingValue = selectedThemeMode,
                onSettingClick = {
                    vm.updateShowThemeModeDialog(true)
                },
            )

            DefaultSettingItem(
                icon = painterResource(id = R.drawable.moon),
                settingText = remember { getAppString(context, R.string.DarkMode) },
                settingValue = selectedDarkMode,
                onSettingClick = {
                    vm.updateShowDarkModeDialog(true)
                }
            )


            if(isAtLeastAndroid10()){
                DefaultSettingItem(
                    icon = painterResource(id = R.drawable.brush),
                    settingText = remember { getAppString(context, R.string.LightColorScheme) },
                    settingValue = lightColorScheme,
                    onSettingClick = { onOpenScreen(Routes.Root.LIGHT_COLOR_SCHEMES) }
                )

                DefaultSettingItem(
                    icon = painterResource(id = R.drawable.brush),
                    settingText = remember { getAppString(context, R.string.DarkColorScheme) },
                    settingValue = darkColorScheme,
                    onSettingClick = { onOpenScreen(Routes.Root.DARK_COLOR_SCHEMES) }
                )
            }else{
                DefaultSettingItem(
                    icon = painterResource(id = R.drawable.brush),
                    settingText = remember { getAppString(context, R.string.ColorScheme) },
                    settingValue = lightColorScheme,
                    onSettingClick = { onOpenScreen(Routes.Root.LIGHT_COLOR_SCHEMES) }
                )
            }
        }


        if (showThemeModeDialog) {

            Dialog(
                onDismissRequest = {

                    vm.updateShowThemeModeDialog(false)

                    vm.updateSelectedThemeMode(themeSetting)
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
                                text = remember { getAppString(context, R.string.SelectColorScheme) },
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
                                    vm.updateSelectedThemeMode(Values.ColorScheme.SYSTEM)
                                }
                        ) {

                            RadioButton(
                                selected = selectedThemeMode == Values.ColorScheme.SYSTEM,
                                onClick = {

                                    vm.updateSelectedThemeMode(Values.ColorScheme.SYSTEM)
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
                                    vm.updateSelectedThemeMode(Values.ColorScheme.LIGHT)
                                }
                        ) {

                            RadioButton(
                                selected = selectedThemeMode == Values.ColorScheme.LIGHT,
                                onClick = {
                                    vm.updateSelectedThemeMode(Values.ColorScheme.LIGHT)
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
                                    vm.updateSelectedThemeMode(Values.ColorScheme.DARK)
                                }
                        ) {

                            RadioButton(
                                selected = selectedThemeMode == Values.ColorScheme.DARK,
                                onClick = {
                                    vm.updateSelectedThemeMode(Values.ColorScheme.DARK)
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

                                    vm.updateShowThemeModeDialog(false)

                                    vm.updateSelectedThemeMode(themeSetting)
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

                                    settingsVM.updateColorSchemeSetting(selectedThemeMode)

                                    vm.updateShowThemeModeDialog(false)
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

        MediumVerticalSpacer()


        Text(
            text = remember { getAppString(context, R.string.Audio) },
            color = MaterialTheme.colorScheme.primary,
            fontSize = 22.sp,
            textAlign = TextAlign.Start,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = 8.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {

            DefaultSettingItem(
                icon = painterResource(id = R.drawable.filter),
                settingText = remember { getAppString(context, R.string.FilterAudioBelow) },
                settingValue = "$filterAudioSetting seconds",
                onSettingClick = {

                    vm.updateShowFilterAudioDialog(true)
                }
            )
        }

        MediumVerticalSpacer()

        Text(
            text = remember { getAppString(context, R.string.Data) },
            color = MaterialTheme.colorScheme.primary,
            fontSize = 22.sp,
            textAlign = TextAlign.Start,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .offset(8.dp)
        )

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

                    vm.updateShowDarkModeDialog(false)

                    vm.updateSelectedDarkMode(darkModeSetting)
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

                                    vm.updateSelectedDarkMode(Values.DarkMode.COLOR)
                                }
                        ) {

                            RadioButton(
                                selected = selectedDarkMode == Values.DarkMode.COLOR,
                                onClick = {

                                    vm.updateSelectedDarkMode(Values.DarkMode.COLOR)
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
                                    vm.updateSelectedDarkMode(Values.DarkMode.OLED)
                                }
                        ) {

                            RadioButton(
                                selected = selectedDarkMode == Values.DarkMode.OLED,
                                onClick = {
                                    vm.updateSelectedDarkMode(Values.DarkMode.OLED)
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

                                    vm.updateShowDarkModeDialog(false)

                                    vm.updateSelectedDarkMode(darkModeSetting)
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

                                    vm.updateShowDarkModeDialog(false)
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

                    vm.updateShowFilterAudioDialog(false)

                    vm.updateFilterAudioText(filterAudioSetting)
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

                                vm.updateFilterAudioText(it)
                            }
                        )

                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Button(
                                onClick = {

                                    vm.updateShowFilterAudioDialog(false)

                                    vm.updateFilterAudioText(filterAudioSetting)
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
                                    vm.applyAudioFilterSetting(settingsVM, mainVM)
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
            .padding(MEDIUM_SPACING)
    ) {


        Image(
            painter = icon,
            contentDescription = "",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .height(30.dp)
                .width(30.dp)
        )

        Spacer(modifier = Modifier.width(MEDIUM_SPACING))

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
            .padding(MEDIUM_SPACING)
    ) {

        Image(
            painter = icon,
            contentDescription = "",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .height(30.dp)
                .width(30.dp)
        )

        Spacer(modifier = Modifier.width(MEDIUM_SPACING))

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

        MediumHorizontalSpacer()

        Switch(
            checked = settingValue,
            onCheckedChange = { onToggle() },
            enabled = enabled,
        )
    }
}

