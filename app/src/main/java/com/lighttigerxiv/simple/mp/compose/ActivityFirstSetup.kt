package com.lighttigerxiv.simple.mp.compose

import android.Manifest.permission.*
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.lighttigerxiv.simple.mp.compose.navigation.screens.setup.PermissionsScreen
import com.lighttigerxiv.simple.mp.compose.navigation.screens.setup.ThemesScreen
import com.lighttigerxiv.simple.mp.compose.navigation.screens.setup.WelcomeScreen
import com.lighttigerxiv.simple.mp.compose.ui.theme.ComposeSimpleMPTheme
import com.lighttigerxiv.simple.mp.compose.viewmodels.ActivityFirstSetupViewModel
import com.lighttigerxiv.simple.mp.compose.viewmodels.ThemeViewModel

class ActivityFirstSetup : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val themeViewModel = ViewModelProvider(this)[ThemeViewModel::class.java]
        val activityFirstSetupViewModel = ViewModelProvider(this)[ActivityFirstSetupViewModel::class.java]

        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) {

                activityFirstSetupViewModel.checkPermissions()
            }


        setContent {
            ComposeSimpleMPTheme {

                val surfaceColor = if (themeViewModel.currentThemeSetting.value == "Dark" && themeViewModel.darkModeSetting.value == "Oled") {
                    Color.Black
                } else if (themeViewModel.currentThemeSetting.value == "Dark" && themeViewModel.darkModeSetting.value == "Oled" && isSystemInDarkTheme()) {
                    Color.Black
                } else if (themeViewModel.currentThemeSetting.value == "Light" && themeViewModel.themeAccentSetting.value == "Blue") {
                    Color(0xFFFEFBFF)
                } else if (themeViewModel.currentThemeSetting.value == "Light" && themeViewModel.themeAccentSetting.value == "Red") {
                    Color(0xFFFFFBFF)
                } else if (themeViewModel.currentThemeSetting.value == "Light" && themeViewModel.themeAccentSetting.value == "Purple") {
                    Color(0xFFFFFBFF)
                } else if (themeViewModel.currentThemeSetting.value == "Light" && themeViewModel.themeAccentSetting.value == "Yellow") {
                    Color(0xFFFFFBFF)
                } else if (themeViewModel.currentThemeSetting.value == "Light" && themeViewModel.themeAccentSetting.value == "Orange") {
                    Color(0xFFFFFBFF)
                } else if (themeViewModel.currentThemeSetting.value == "Light" && themeViewModel.themeAccentSetting.value == "Green") {
                    Color(0xFFFDFDF5)
                } else if (themeViewModel.currentThemeSetting.value == "Light" && themeViewModel.themeAccentSetting.value == "Pink") {
                    Color(0xFFFFFBFF)
                } else if (themeViewModel.darkModeSetting.value == "Oled" && themeViewModel.currentThemeSetting.value == "Light" && isSystemInDarkTheme()) {
                    Color(0xFFFFFBFF)
                } else if (themeViewModel.darkModeSetting.value == "Oled" && isSystemInDarkTheme()) {
                    Color.Black
                } else {
                    MaterialTheme.colorScheme.surface
                }

                rememberSystemUiController().setStatusBarColor(surfaceColor)

                val navController = rememberNavController()


                NavHost(
                    navController = navController,
                    startDestination = "welcomeScreen",
                    modifier = Modifier
                        .fillMaxSize()
                        .background(surfaceColor)
                ) {

                    composable("welcomeScreen") {
                        WelcomeScreen(
                            onNextClicked = {
                                navController.navigate("permissionsScreen")
                            }
                        )
                    }

                    composable("permissionsScreen") {
                        PermissionsScreen(
                            activityFirstSetupViewModel = activityFirstSetupViewModel,
                            onBackClicked = { navController.navigateUp() },
                            onNextClicked = { navController.navigate("themesScreen") },
                            onRequestNotificationPermission = { requestPermissionLauncher.launch(POST_NOTIFICATIONS) },
                            onRequestStoragePermission = {

                                if (Build.VERSION.SDK_INT >= 33)
                                    requestPermissionLauncher.launch(READ_MEDIA_AUDIO)
                                else
                                    requestPermissionLauncher.launch(READ_EXTERNAL_STORAGE)
                            }
                        )
                    }

                    composable("themesScreen") {
                        ThemesScreen(
                            activityFirstSetupViewModel = activityFirstSetupViewModel,
                            onBackClicked = { navController.navigateUp() },
                            onKillActivity = { finish() }
                        )
                    }
                }
            }
        }
    }
}