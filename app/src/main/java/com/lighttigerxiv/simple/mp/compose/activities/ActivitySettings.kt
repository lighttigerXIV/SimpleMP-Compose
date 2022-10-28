package com.lighttigerxiv.simple.mp.compose.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModelProvider
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.composables.BasicToolbar
import com.lighttigerxiv.simple.mp.compose.composables.CustomTextField
import com.lighttigerxiv.simple.mp.compose.ui.theme.ComposeSimpleMPTheme
import com.lighttigerxiv.simple.mp.compose.viewmodels.ActivitySettingsViewModel
import com.lighttigerxiv.simple.mp.compose.viewmodels.ThemeViewModel

class ActivitySettings : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val activitySettingsViewModel = ViewModelProvider(this)[ActivitySettingsViewModel::class.java]
        val themeViewModel = ViewModelProvider(this)[ThemeViewModel::class.java]

        setContent {

            ComposeSimpleMPTheme(
                currentTheme = themeViewModel.currentThemeSetting.observeAsState().value!!
            ){

                val showThemeModeDialog = remember{ mutableStateOf(false)}
                val showDarkModeDialog = remember{ mutableStateOf(false) }
                val showFilterAudioDialog = remember{ mutableStateOf(false) }
                val showThemeAccentDialog = remember{ mutableStateOf(false) }
                val selectedThemeMode = activitySettingsViewModel.selectedThemeModelDialog.observeAsState().value
                val selectedDarkMode = activitySettingsViewModel.selectedDarkModeDialog.observeAsState().value
                val etFilterAudioValue = activitySettingsViewModel.etFilterAudioDialog.observeAsState().value
                val restartAppSnack = activitySettingsViewModel.showRestartSnack.observeAsState().value
                val restartAppSnackState = remember{SnackbarHostState()}

                val surfaceColor =
                    if(themeViewModel.currentThemeSetting.value == "Dark" && themeViewModel.darkModeSetting.value == "Oled"){ Color.Black }
                    else if(themeViewModel.currentThemeSetting.value == "Dark" && themeViewModel.darkModeSetting.value == "Oled" && isSystemInDarkTheme()) { Color.Black }
                    else if(themeViewModel.currentThemeSetting.value == "Light" && themeViewModel.themeAccentSetting.value == "Blue") { Color(0xFFFEFBFF) }
                    else if(themeViewModel.currentThemeSetting.value == "Light" && themeViewModel.themeAccentSetting.value == "Red") { Color(0xFFFFFBFF) }
                    else if(themeViewModel.currentThemeSetting.value == "Light" && themeViewModel.themeAccentSetting.value == "Purple") { Color(0xFFFFFBFF) }
                    else if(themeViewModel.currentThemeSetting.value == "Light" && themeViewModel.themeAccentSetting.value == "Yellow") { Color(0xFFFFFBFF) }
                    else if(themeViewModel.currentThemeSetting.value == "Light" && themeViewModel.themeAccentSetting.value == "Orange") { Color(0xFFFFFBFF) }
                    else if(themeViewModel.currentThemeSetting.value == "Light" && themeViewModel.themeAccentSetting.value == "Green") { Color(0xFFFDFDF5) }
                    else if(themeViewModel.currentThemeSetting.value == "Light" && themeViewModel.themeAccentSetting.value == "Pink") { Color(0xFFFFFBFF) }
                    else if(themeViewModel.darkModeSetting.value == "Oled" && themeViewModel.currentThemeSetting.value == "Light" && isSystemInDarkTheme()){ Color(0xFFFFFBFF) }
                    else if(themeViewModel.darkModeSetting.value == "Oled" && isSystemInDarkTheme()){Color.Black}
                    else{ MaterialTheme.colorScheme.surface }

                rememberSystemUiController().setStatusBarColor(surfaceColor)


                LaunchedEffect(key1 = restartAppSnack){

                    if(restartAppSnack!!){

                        val snackBarResult = restartAppSnackState.showSnackbar(
                            message = "Restart to apply changes",
                            actionLabel = "Restart",
                        )

                        when(snackBarResult){
                            SnackbarResult.ActionPerformed -> {
                                startActivity(Intent(applicationContext, MainActivity::class.java))
                                finish()
                            }
                            else -> {}
                        }
                    }
                }

                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(surfaceColor)
                    .padding(14.dp)
                    .verticalScroll(rememberScrollState())
                ) {

                    Column(modifier = Modifier.fillMaxSize()) {

                        BasicToolbar(
                            backButtonText = "Home",
                            onBackClicked = { onBackPressed() }
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
                        ){

                            DefaultSettingItem(
                                icon = painterResource(id = R.drawable.icon_theme_mode_regular),
                                settingText = "Theme Mode",
                                settingValue = activitySettingsViewModel.themeModeSetting.value!!,
                                onSettingClick = { showThemeModeDialog.value = true },
                            )



                            DefaultSettingItem(
                                icon = painterResource(id = R.drawable.icon_moon_regular),
                                settingText = "Dark Mode",
                                settingValue = activitySettingsViewModel.darkModeSetting.value!!,
                                onSettingClick = { showDarkModeDialog.value = true }
                            )



                            DefaultSettingItem(
                                icon = painterResource(id = R.drawable.icon_theme_regular),
                                settingText = "Accent Color",
                                settingValue = activitySettingsViewModel.themeAccentSetting.value!!,
                                onSettingClick = { showThemeAccentDialog.value = true }
                            )
                        }


                        if (showThemeModeDialog.value) {

                            Dialog(
                                onDismissRequest = { showThemeModeDialog.value = false }
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
                                            verticalAlignment = CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    activitySettingsViewModel.selectedThemeModelDialog.value =
                                                        "System"
                                                }
                                        ) {

                                            RadioButton(
                                                selected = selectedThemeMode == "System",
                                                onClick = {
                                                    activitySettingsViewModel.selectedThemeModelDialog.value =
                                                        "System"
                                                }
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text(
                                                text = "System Default",
                                                fontSize = 16.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }

                                        Row(
                                            verticalAlignment = CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    activitySettingsViewModel.selectedThemeModelDialog.value =
                                                        "Light"
                                                }
                                        ) {

                                            RadioButton(
                                                selected = selectedThemeMode == "Light",
                                                onClick = {
                                                    activitySettingsViewModel.selectedThemeModelDialog.value =
                                                        "Light"
                                                }
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text(
                                                text = "Light Mode",
                                                fontSize = 16.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }

                                        Row(
                                            verticalAlignment = CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    activitySettingsViewModel.selectedThemeModelDialog.value =
                                                        "Dark"
                                                }
                                        ) {

                                            RadioButton(
                                                selected = selectedThemeMode == "Dark",
                                                onClick = {
                                                    activitySettingsViewModel.selectedThemeModelDialog.value =
                                                        "Dark"
                                                }
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text(
                                                text = "Dark Mode",
                                                fontSize = 16.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                            )
                                        }

                                        Row(
                                            horizontalArrangement = Arrangement.SpaceEvenly,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {

                                            Button(
                                                onClick = { showThemeModeDialog.value = false },
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
                                                    activitySettingsViewModel.setThemeMode(); showThemeModeDialog.value =
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
                                settingValue = "${activitySettingsViewModel.filterAudioSetting.value!!} seconds",
                                onSettingClick = { showFilterAudioDialog.value = true }
                            )
                        }



                        if( showDarkModeDialog.value ){

                            Dialog(
                                onDismissRequest = { showDarkModeDialog.value = false }
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
                                            verticalAlignment = CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    activitySettingsViewModel.selectedDarkModeDialog.value =
                                                        "Color"
                                                }
                                        ) {

                                            RadioButton(
                                                selected = selectedDarkMode == "Color",
                                                onClick = {
                                                    activitySettingsViewModel.selectedDarkModeDialog.value =
                                                        "Color"
                                                }
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text(
                                                text = "Color",
                                                fontSize = 16.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }

                                        Row(
                                            verticalAlignment = CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    activitySettingsViewModel.selectedDarkModeDialog.value =
                                                        "Oled"
                                                }
                                        ) {

                                            RadioButton(
                                                selected = selectedDarkMode == "Oled",
                                                onClick = {
                                                    activitySettingsViewModel.selectedDarkModeDialog.value =
                                                        "Oled"
                                                }
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text(
                                                text = "Oled",
                                                fontSize = 16.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }

                                        Row(
                                            horizontalArrangement = Arrangement.SpaceEvenly,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {

                                            Button(
                                                onClick = { showDarkModeDialog.value = false },
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
                                                    activitySettingsViewModel.setDarkMode(); showDarkModeDialog.value =
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

                        if( showFilterAudioDialog.value ){

                            Dialog(
                                onDismissRequest = { showFilterAudioDialog.value = false }
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
                                            onValueChanged = {
                                                activitySettingsViewModel.etFilterAudioDialog.value = it
                                            }
                                        )

                                        Row(
                                            horizontalArrangement = Arrangement.SpaceEvenly,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {

                                            Button(
                                                onClick = {
                                                    showFilterAudioDialog.value = false
                                                    activitySettingsViewModel.etFilterAudioDialog.value = activitySettingsViewModel.filterAudioSetting.value
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
                                                    activitySettingsViewModel.setFilterAudio()
                                                    showFilterAudioDialog.value = false
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

                        if( showThemeAccentDialog.value ){

                            Dialog(
                                onDismissRequest = { showThemeAccentDialog.value = false }
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
                                                text = "Select Accent Color",
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                fontSize = 20.sp,
                                                color = MaterialTheme.colorScheme.primary,
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .wrapContentHeight()
                                        ) {

                                            Row(
                                                modifier = Modifier.fillMaxWidth()
                                            ) {

                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(10.dp)
                                                        .weight(0.25f)
                                                        .aspectRatio(1f)
                                                        .clip(RoundedCornerShape(100))
                                                        .background(Color(0xFF0058CC))
                                                        .clickable {
                                                            showThemeAccentDialog.value = false
                                                            activitySettingsViewModel.selectedThemeAccentDialog.value = "Blue"
                                                            activitySettingsViewModel.setThemeAccent()
                                                        }
                                                )
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(10.dp)
                                                        .weight(0.25f)
                                                        .aspectRatio(1f)
                                                        .clip(RoundedCornerShape(100))
                                                        .background(Color(0xFFC0001B))
                                                        .clickable {
                                                            showThemeAccentDialog.value = false
                                                            activitySettingsViewModel.selectedThemeAccentDialog.value = "Red"
                                                            activitySettingsViewModel.setThemeAccent()
                                                        }
                                                )
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(10.dp)
                                                        .weight(0.25f)
                                                        .aspectRatio(1f)
                                                        .clip(RoundedCornerShape(100))
                                                        .background(Color(0xFF682BF5))
                                                        .clickable {
                                                            showThemeAccentDialog.value = false
                                                            activitySettingsViewModel.selectedThemeAccentDialog.value = "Purple"
                                                            activitySettingsViewModel.setThemeAccent()
                                                        }
                                                )
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(10.dp)
                                                        .weight(0.25f)
                                                        .aspectRatio(1f)
                                                        .clip(RoundedCornerShape(100))
                                                        .background(Color(0xFF835400))
                                                        .clickable {
                                                            showThemeAccentDialog.value = false
                                                            activitySettingsViewModel.selectedThemeAccentDialog.value = "Yellow"
                                                            activitySettingsViewModel.setThemeAccent()
                                                        }
                                                )
                                            }
                                            Row(
                                                modifier = Modifier.fillMaxWidth()
                                            ) {

                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(10.dp)
                                                        .weight(0.25f)
                                                        .aspectRatio(1f)
                                                        .clip(RoundedCornerShape(100))
                                                        .background(Color(0xFF9C4400))
                                                        .clickable {
                                                            showThemeAccentDialog.value = false
                                                            activitySettingsViewModel.selectedThemeAccentDialog.value = "Orange"
                                                            activitySettingsViewModel.setThemeAccent()
                                                        }
                                                )
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(10.dp)
                                                        .weight(0.25f)
                                                        .aspectRatio(1f)
                                                        .clip(RoundedCornerShape(100))
                                                        .background(Color(0xFF326B00))
                                                        .clickable {
                                                            showThemeAccentDialog.value = false
                                                            activitySettingsViewModel.selectedThemeAccentDialog.value = "Green"
                                                            activitySettingsViewModel.setThemeAccent()
                                                        }
                                                )
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(10.dp)
                                                        .weight(0.25f)
                                                        .aspectRatio(1f)
                                                        .clip(RoundedCornerShape(100))
                                                        .background(Color(0xFFB90063))
                                                        .clickable {
                                                            showThemeAccentDialog.value = false
                                                            activitySettingsViewModel.selectedThemeAccentDialog.value = "Pink"
                                                            activitySettingsViewModel.setThemeAccent()
                                                        }
                                                )
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(10.dp)
                                                        .weight(0.25f)
                                                        .aspectRatio(1f)
                                                        .clip(RoundedCornerShape(100))
                                                        .background(Color.Magenta)
                                                        .clickable {
                                                            showThemeAccentDialog.value = false
                                                            activitySettingsViewModel.selectedThemeAccentDialog.value = "Macchiato"
                                                            activitySettingsViewModel.setThemeAccent()
                                                        }
                                                )
                                            }
                                        }

                                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){

                                            Button(
                                                onClick = {
                                                    showThemeAccentDialog.value = false
                                                    activitySettingsViewModel.selectedThemeAccentDialog.value = "Default"
                                                    activitySettingsViewModel.setThemeAccent()
                                                },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = MaterialTheme.colorScheme.primary
                                                ),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(5.dp)
                                            ) {
                                                Text(
                                                    text = "Use system theme",
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis,
                                                    color = MaterialTheme.colorScheme.onPrimary
                                                )
                                            }
                                        }

                                        Button(
                                            onClick = {
                                                showThemeAccentDialog.value = false
                                                activitySettingsViewModel.selectedThemeAccentDialog.value = activitySettingsViewModel.themeAccentSetting.value
                                            },
                                            border = BorderStroke(
                                                1.dp,
                                                MaterialTheme.colorScheme.primary
                                            ),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.surface
                                            ),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(5.dp)
                                        ) {
                                            Text(
                                                text = "Cancel",
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
                SnackbarHost(hostState = restartAppSnackState)
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
){

    Row(
        verticalAlignment = CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp)
            .clickable { onSettingClick() }
    ){


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