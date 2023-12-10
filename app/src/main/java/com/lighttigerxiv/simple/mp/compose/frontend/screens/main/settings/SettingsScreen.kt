package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.backend.settings.SettingsOptions
import com.lighttigerxiv.simple.mp.compose.frontend.composables.FullscreenDialogToolbar
import com.lighttigerxiv.simple.mp.compose.frontend.composables.HSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.composables.PrimaryButton
import com.lighttigerxiv.simple.mp.compose.frontend.composables.SecondaryButton
import com.lighttigerxiv.simple.mp.compose.frontend.composables.TextField
import com.lighttigerxiv.simple.mp.compose.frontend.composables.ThemeSelector
import com.lighttigerxiv.simple.mp.compose.frontend.composables.Toolbar
import com.lighttigerxiv.simple.mp.compose.frontend.composables.VSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.theme.PreviewTheme
import com.lighttigerxiv.simple.mp.compose.frontend.utils.ChangeNavigationBarsColor
import com.lighttigerxiv.simple.mp.compose.frontend.utils.FontSizes
import com.lighttigerxiv.simple.mp.compose.frontend.utils.Sizes

@Composable
fun SettingsScreen(
    rootController: NavHostController,
    vm: SettingsScreenVM = viewModel(factory = SettingsScreenVM.Factory)
) {

    val uiState = vm.uiState.collectAsState().value

    ChangeNavigationBarsColor(color = MaterialTheme.colorScheme.surface)

    Column(
        modifier = Modifier.padding(Sizes.LARGE)
    ) {
        Toolbar(navController = rootController)

        VSpacer(size = Sizes.LARGE)

        if (!uiState.isLoading) {

            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

                Text(
                    modifier = Modifier.offset(x = 8.dp),
                    text = stringResource(id = R.string.Theming),
                    fontWeight = FontWeight.Bold,
                    fontSize = FontSizes.HEADER,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                VSpacer(size = Sizes.SMALL)

                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(Sizes.XLARGE))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {

                    SelectSettingCard(
                        iconId = R.drawable.colorscheme,
                        title = stringResource(id = R.string.color_scheme),
                        settingValue = when (uiState.colorScheme) {
                            SettingsOptions.ColorScheme.SYSTEM -> stringResource(id = R.string.system)
                            SettingsOptions.ColorScheme.LIGHT -> stringResource(id = R.string.light)
                            SettingsOptions.ColorScheme.DARK -> stringResource(id = R.string.dark)
                            else -> "n/a"
                        },
                        onClick = { vm.updateShowColorSchemeDialog(true) }
                    )

                    SwitchSettingCard(
                        iconId = R.drawable.moon,
                        title = stringResource(id = R.string.use_oled_in_dark_theme),
                        settingValue = uiState.useOledInDarkTheme,
                        onCheckedChange = { checked -> vm.updateUseOledInDarkTheme(checked) }
                    )

                    SelectSettingCard(
                        iconId = R.drawable.brush,
                        title = stringResource(id = R.string.color_scheme),
                        settingValue = vm.getThemeName(theme = uiState.lightTheme),
                        onClick = { vm.updateShowLightThemeDialog(true) }
                    )

                    SelectSettingCard(
                        iconId = R.drawable.brush,
                        title = stringResource(id = R.string.color_scheme),
                        settingValue = vm.getThemeName(theme = uiState.darkTheme),
                        onClick = { vm.updateShowDarkThemeDialog(true) }
                    )
                }

                VSpacer(size = Sizes.LARGE)

                Text(
                    modifier = Modifier.offset(x = 8.dp),
                    text = stringResource(id = R.string.Audio),
                    fontWeight = FontWeight.Bold,
                    fontSize = FontSizes.HEADER,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                VSpacer(size = Sizes.SMALL)


                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(Sizes.XLARGE))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {

                    SelectSettingCard(
                        iconId = R.drawable.filter,
                        title = stringResource(id = R.string.FilterAudio),
                        settingValue = uiState.durationFilter.toString(),
                        onClick = { vm.updateShowDurationFilterDialog(true) }
                    )
                }

                VSpacer(size = Sizes.LARGE)

                Text(
                    modifier = Modifier.offset(x = 8.dp),
                    text = stringResource(id = R.string.Data),
                    fontWeight = FontWeight.Bold,
                    fontSize = FontSizes.HEADER,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                VSpacer(size = Sizes.SMALL)

                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(Sizes.XLARGE))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    SwitchSettingCard(
                        iconId = R.drawable.database,
                        title = stringResource(id = R.string.download_artist_cover),
                        settingValue = uiState.downloadArtistCover,
                        onCheckedChange = { checked -> vm.updateDownloadArtistCover(checked) }
                    )

                    SwitchSettingCard(
                        iconId = R.drawable.database,
                        title = stringResource(id = R.string.download_artist_cover_on_mobile_data),
                        settingValue = uiState.downloadArtistCoverWithData,
                        onCheckedChange = { checked -> vm.updateDownloadArtistCoverWithData(checked) },
                        disabled = !uiState.downloadArtistCover
                    )
                }
            }
        }

        ColorSchemeDialog(uiState = uiState, vm = vm)

        LightThemeDialog(uiState = uiState, vm = vm)

        DarkThemeDialog(uiState = uiState, vm = vm)

        DurationFilterDialog(uiState = uiState, vm = vm)
    }
}

@Composable
fun SelectSettingCard(
    iconId: Int,
    title: String,
    settingValue: String,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(Sizes.LARGE),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(30.dp),
            painter = painterResource(id = iconId),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        HSpacer(size = Sizes.SMALL)

        Column {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = settingValue,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun SwitchSettingCard(
    iconId: Int,
    title: String,
    settingValue: Boolean,
    onCheckedChange: (newValue: Boolean) -> Unit,
    disabled: Boolean = false
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { if (!disabled) onCheckedChange(!settingValue) }
            .padding(Sizes.LARGE),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(30.dp),
            painter = painterResource(id = iconId),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        HSpacer(size = Sizes.SMALL)

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = true),
            text = title,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        HSpacer(size = Sizes.SMALL)

        Switch(
            checked = settingValue,
            onCheckedChange = { if (!disabled) onCheckedChange(!settingValue) },
            enabled = !disabled
        )
    }
}


@Composable
fun ColorSchemeDialog(
    uiState: SettingsScreenVM.UiState,
    vm: SettingsScreenVM
) {

    if (uiState.showColorSchemeDialog) {
        Dialog(
            onDismissRequest = {
                vm.updateShowColorSchemeDialog(false)
                vm.updateColorSchemeDialogSelectedRadioButton(uiState.colorScheme)
            }
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(Sizes.XLARGE))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(Sizes.LARGE)
            ) {
                Column {

                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

                        Icon(
                            painter = painterResource(id = R.drawable.colorscheme),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )

                        VSpacer(size = Sizes.SMALL)

                        Text(
                            text = stringResource(id = R.string.color_scheme),
                            fontWeight = FontWeight.Medium,
                            fontSize = FontSizes.HEADER,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    VSpacer(size = Sizes.SMALL)

                    Divider(Modifier.height(1.dp), color = MaterialTheme.colorScheme.surfaceVariant)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { vm.updateColorSchemeDialogSelectedRadioButton(SettingsOptions.ColorScheme.SYSTEM) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = uiState.colorSchemeDialogSelectedRadioButton == SettingsOptions.ColorScheme.SYSTEM,
                            onClick = { vm.updateColorSchemeDialogSelectedRadioButton(SettingsOptions.ColorScheme.SYSTEM) }
                        )

                        HSpacer(size = Sizes.SMALL)

                        Text(
                            text = stringResource(id = R.string.system),
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { vm.updateColorSchemeDialogSelectedRadioButton(SettingsOptions.ColorScheme.LIGHT) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = uiState.colorSchemeDialogSelectedRadioButton == SettingsOptions.ColorScheme.LIGHT,
                            onClick = { vm.updateColorSchemeDialogSelectedRadioButton(SettingsOptions.ColorScheme.LIGHT) }
                        )

                        HSpacer(size = Sizes.SMALL)

                        Text(
                            text = stringResource(id = R.string.light),
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { vm.updateColorSchemeDialogSelectedRadioButton(SettingsOptions.ColorScheme.DARK) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = uiState.colorSchemeDialogSelectedRadioButton == SettingsOptions.ColorScheme.DARK,
                            onClick = { vm.updateColorSchemeDialogSelectedRadioButton(SettingsOptions.ColorScheme.DARK) }
                        )

                        HSpacer(size = Sizes.SMALL)

                        Text(
                            text = stringResource(id = R.string.dark),
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Divider(Modifier.height(1.dp), color = MaterialTheme.colorScheme.surfaceVariant)

                    VSpacer(size = Sizes.LARGE)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {

                        SecondaryButton(
                            text = stringResource(id = R.string.cancel),
                            onClick = {
                                vm.updateShowColorSchemeDialog(false)
                                vm.updateColorSchemeDialogSelectedRadioButton(uiState.colorScheme)
                            }
                        )

                        HSpacer(size = Sizes.SMALL)

                        PrimaryButton(
                            text = stringResource(id = R.string.save),
                            onClick = {
                                vm.updateShowColorSchemeDialog(false)
                                vm.updateColorScheme(uiState.colorSchemeDialogSelectedRadioButton)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LightThemeDialog(
    uiState: SettingsScreenVM.UiState,
    vm: SettingsScreenVM
) {

    if (uiState.showLightThemeDialog) {

        PreviewTheme(
            themeId = uiState.lightThemeDialogSelectedTheme,
            darkTheme = false
        ) {

            Dialog(
                properties = DialogProperties(usePlatformDefaultWidth = false),
                onDismissRequest = { vm.updateShowLightThemeDialog(false) }
            ) {

                Surface(
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(Sizes.LARGE)
                ) {
                    Column {

                        FullscreenDialogToolbar(
                            onCancelClick = {
                                vm.updateShowLightThemeDialog(false)
                                vm.updateLightThemeDialogSelectedTheme(uiState.lightTheme)
                            },
                            onSaveClick = {
                                vm.updateShowLightThemeDialog(false)
                                vm.updateLightTheme(uiState.lightThemeDialogSelectedTheme)
                            }
                        )

                        VSpacer(size = Sizes.LARGE)

                        Column(Modifier.verticalScroll(rememberScrollState())) {
                            ThemeSelector(
                                selectedTheme = uiState.lightThemeDialogSelectedTheme,
                                onThemeSelected = { theme -> vm.updateLightThemeDialogSelectedTheme(theme) },
                                darkTheme = false
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DarkThemeDialog(
    uiState: SettingsScreenVM.UiState,
    vm: SettingsScreenVM
) {

    if (uiState.showDarkThemeDialog) {

        PreviewTheme(
            themeId = uiState.darkThemeDialogSelectedTheme,
            darkTheme = true
        ) {

            Dialog(
                properties = DialogProperties(usePlatformDefaultWidth = false),
                onDismissRequest = { vm.updateShowDarkThemeDialog(false) }
            ) {

                Surface(
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(Sizes.LARGE)
                ) {
                    Column {

                        FullscreenDialogToolbar(
                            onCancelClick = {
                                vm.updateShowDarkThemeDialog(false)
                                vm.updateDarkThemeDialogSelectedTheme(uiState.darkTheme)
                            },
                            onSaveClick = {
                                vm.updateShowDarkThemeDialog(false)
                                vm.updateDarkTheme(uiState.darkThemeDialogSelectedTheme)
                            }
                        )

                        VSpacer(size = Sizes.LARGE)

                        Column(Modifier.verticalScroll(rememberScrollState())) {
                            ThemeSelector(
                                selectedTheme = uiState.darkThemeDialogSelectedTheme,
                                onThemeSelected = { theme -> vm.updateDarkThemeDialogSelectedTheme(theme) },
                                darkTheme = true
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DurationFilterDialog(
    uiState: SettingsScreenVM.UiState,
    vm: SettingsScreenVM
) {

    if (uiState.showDurationFilterDialog) {
        Dialog(
            onDismissRequest = {
                vm.updateShowDurationFilterDialog(false)
                vm.updateDurationFilterDialogText(uiState.durationFilter.toString())
            }
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(Sizes.XLARGE))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(Sizes.LARGE)
            ){

                Column {
                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

                        Icon(
                            painter = painterResource(id = R.drawable.filter),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )

                        VSpacer(size = Sizes.SMALL)

                        Text(
                            text = stringResource(id = R.string.filter_audio_below),
                            fontWeight = FontWeight.Medium,
                            fontSize = FontSizes.HEADER,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    VSpacer(size = Sizes.SMALL)

                    TextField(
                        text = uiState.durationFilterDialogText,
                        onTextChange = { vm.updateDurationFilterDialogText(it) },
                        numberField = true,
                        placeholder = stringResource(id = R.string.seconds)
                    )

                    VSpacer(size = Sizes.LARGE)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {

                        SecondaryButton(
                            text = stringResource(id = R.string.cancel),
                            onClick = {
                                vm.updateShowDurationFilterDialog(false)
                                vm.updateDurationFilterDialogText(uiState.durationFilter.toString())
                            }
                        )

                        HSpacer(size = Sizes.SMALL)

                        PrimaryButton(
                            text = stringResource(id = R.string.save),
                            onClick = {
                                vm.updateShowDurationFilterDialog(false)
                                vm.updateDurationFilter()
                            },
                            disabled = uiState.durationFilterDialogText.isEmpty()
                        )
                    }
                }
            }
        }
    }
}