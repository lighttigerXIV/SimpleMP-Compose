package com.lighttigerxiv.simple.mp.compose

import android.Manifest.permission.*
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
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

        val activityFirstSetupViewModel = ViewModelProvider(this)[ActivityFirstSetupViewModel::class.java]

        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) {

                activityFirstSetupViewModel.checkPermissions()
            }


        val themeMode = activityFirstSetupViewModel.themeModeSetting!!
        val darkMode = activityFirstSetupViewModel.darkModeSetting!!

        setContent {
            ComposeSimpleMPTheme(
                useDarkTheme = isSystemInDarkTheme(),
                themeMode = themeMode,
                themeAccent = activityFirstSetupViewModel.themeAccentSetting.collectAsState().value!!,
                content = {

                    val themeAccent = activityFirstSetupViewModel.themeAccentSetting.collectAsState().value

                    val surfaceColor = if (themeMode == "Dark" && darkMode == "Oled") {
                        Color.Black
                    } else if (themeMode == "Light" && themeAccent == "Blue") {
                        Color(0xFFFEFBFF)
                    } else if (themeMode == "Light" && themeAccent == "Red") {
                        Color(0xFFFFFBFF)
                    } else if (themeMode == "Light" && themeAccent == "Purple") {
                        Color(0xFFFFFBFF)
                    } else if (themeMode == "Light" && themeAccent == "Yellow") {
                        Color(0xFFFFFBFF)
                    } else if (themeMode == "Light" && themeAccent == "Orange") {
                        Color(0xFFFFFBFF)
                    } else if (themeMode == "Light" && themeAccent == "Green") {
                        Color(0xFFFDFDF5)
                    } else if (themeMode == "Light" && themeAccent == "Pink") {
                        Color(0xFFFFFBFF)
                    } else if (darkMode == "Oled" && themeMode == "Light" && isSystemInDarkTheme()) {
                        Color(0xFFFFFBFF)
                    } else if (darkMode == "Oled" && isSystemInDarkTheme()) {
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
            )
        }
    }
}