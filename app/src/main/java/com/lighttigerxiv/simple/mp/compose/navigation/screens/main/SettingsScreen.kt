package com.lighttigerxiv.simple.mp.compose.navigation.screens.main

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import com.lighttigerxiv.simple.mp.compose.viewmodels.ActivityMainVM

@Composable
fun SettingsScreen(
    mainVM: ActivityMainVM,
    onBackPressed: () -> Unit,
    onOpenScreen: (route: String) -> Unit
) {

    val context = LocalContext.current
    val showThemeModeDialog = remember { mutableStateOf(false) }
    val showDarkModeDialog = remember { mutableStateOf(false) }
    val showFilterAudioDialog = remember { mutableStateOf(false) }
    val showThemeAccentDialog = remember { mutableStateOf(false) }
    val selectedThemeMode = mainVM.selectedThemeModeDialog.observeAsState().value
    val selectedDarkMode = mainVM.selectedDarkModeDialog.observeAsState().value
    val selectedThemeAccent = mainVM.selectedThemeAccentDialog.observeAsState().value
    val etFilterAudioValue = mainVM.etFilterAudioDialog.observeAsState().value
    val restartAppSnackState = remember { SnackbarHostState() }
    val surfaceColor = mainVM.surfaceColor.collectAsState().value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(surfaceColor)
            .padding(SCREEN_PADDING)
            .verticalScroll(rememberScrollState())
    ) {

        Column(modifier = Modifier.fillMaxSize()) {

            CustomToolbar(
                backText = remember { getAppString(context, R.string.Home) },
                onBackClick = { onBackPressed() }
            )

            Spacer(modifier = Modifier.height(10.dp))

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
            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {

                DefaultSettingItem(
                    icon = painterResource(id = R.drawable.icon_theme_mode_regular),
                    settingText = remember { getAppString(context, R.string.Theme) },
                    settingValue = mainVM.themeModeSetting.collectAsState().value!!,
                    onSettingClick = { showThemeModeDialog.value = true },
                )



                DefaultSettingItem(
                    icon = painterResource(id = R.drawable.icon_moon_regular),
                    settingText = remember { getAppString(context, R.string.DarkMode) },
                    settingValue = mainVM.darkModeSetting.collectAsState().value!!,
                    onSettingClick = { showDarkModeDialog.value = true }
                )



                DefaultSettingItem(
                    icon = painterResource(id = R.drawable.icon_theme_regular),
                    settingText = remember { getAppString(context, R.string.AccentColor) },
                    settingValue = mainVM.themeAccentSetting.collectAsState().value!!,
                    onSettingClick = { onOpenScreen("ThemesScreen") }
                )
            }


            if (showThemeModeDialog.value) {

                Dialog(
                    onDismissRequest = {
                        showThemeModeDialog.value = false
                        mainVM.selectedThemeModeDialog.value = mainVM.themeModeSetting.value
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
                                        mainVM.selectedThemeModeDialog.value =
                                            "System"
                                    }
                            ) {

                                RadioButton(
                                    selected = selectedThemeMode == "System",
                                    onClick = {
                                        mainVM.selectedThemeModeDialog.value =
                                            "System"
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
                                        mainVM.selectedThemeModeDialog.value =
                                            "Light"
                                    }
                            ) {

                                RadioButton(
                                    selected = selectedThemeMode == "Light",
                                    onClick = {
                                        mainVM.selectedThemeModeDialog.value =
                                            "Light"
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
                                        mainVM.selectedThemeModeDialog.value =
                                            "Dark"
                                    }
                            ) {

                                RadioButton(
                                    selected = selectedThemeMode == "Dark",
                                    onClick = {
                                        mainVM.selectedThemeModeDialog.value =
                                            "Dark"
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
                                        showThemeModeDialog.value = false
                                        mainVM.selectedThemeModeDialog.value = mainVM.themeModeSetting.value
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
                                        mainVM.setThemeMode(); showThemeModeDialog.value =
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
                    settingValue = "${mainVM.filterAudioSetting.collectAsState().value} seconds",
                    onSettingClick = { showFilterAudioDialog.value = true }
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
                    settingText = remember{ getAppString(context, R.string.DownloadArtistCoverFromInternet) },
                    settingValue = mainVM.downloadArtistCoverSetting.collectAsState().value,
                    onToggle = {mainVM.toggleDownloadArtistCoverSetting()}
                )

                SwitchSettingItem(
                    icon = painterResource(id = R.drawable.icon_database),
                    settingText = remember{ getAppString(context, R.string.DownloadArtistCoverOnData) },
                    settingValue = mainVM.downloadOverDataSetting.collectAsState().value,
                    onToggle = {mainVM.toggleDownloadOverDataSetting()},
                    enabled = mainVM.downloadArtistCoverSetting.collectAsState().value
                )
            }


            if (showDarkModeDialog.value) {

                Dialog(
                    onDismissRequest = {
                        showDarkModeDialog.value = false
                        mainVM.selectedDarkModeDialog.value = mainVM.darkModeSetting.value
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
                                        mainVM.selectedDarkModeDialog.value =
                                            "Color"
                                    }
                            ) {

                                RadioButton(
                                    selected = selectedDarkMode == "Color",
                                    onClick = {
                                        mainVM.selectedDarkModeDialog.value =
                                            "Color"
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
                                        mainVM.selectedDarkModeDialog.value =
                                            "Oled"
                                    }
                            ) {

                                RadioButton(
                                    selected = selectedDarkMode == "Oled",
                                    onClick = {
                                        mainVM.selectedDarkModeDialog.value =
                                            "Oled"
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
                                        showDarkModeDialog.value = false
                                        mainVM.selectedDarkModeDialog.value = mainVM.darkModeSetting.value
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
                                        mainVM.setDarkMode(); showDarkModeDialog.value =
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

            if (showFilterAudioDialog.value) {

                Dialog(
                    onDismissRequest = {
                        showFilterAudioDialog.value = false
                        mainVM.etFilterAudioDialog.value = mainVM.filterAudioSetting.value
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
                                text = etFilterAudioValue!!,
                                placeholder = remember { getAppString(context, R.string.InsertMinimumSeconds) },
                                textType = "number",
                                onTextChange = {
                                    mainVM.etFilterAudioDialog.value = it
                                }
                            )

                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                Button(
                                    onClick = {
                                        showFilterAudioDialog.value = false
                                        mainVM.etFilterAudioDialog.value = mainVM.filterAudioSetting.value
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
                                        mainVM.setFilterAudio()
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
            .clickable { if(enabled) onToggle() }
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
            onCheckedChange = {onToggle()},
            enabled = enabled
        )
    }
}

