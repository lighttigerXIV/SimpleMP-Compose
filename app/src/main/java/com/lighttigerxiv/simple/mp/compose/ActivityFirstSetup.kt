package com.lighttigerxiv.simple.mp.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.lighttigerxiv.simple.mp.compose.screens.setup.permissions.PermissionsScreen
import com.lighttigerxiv.simple.mp.compose.screens.setup.themes.ThemesScreen
import com.lighttigerxiv.simple.mp.compose.screens.setup.permissions.PermissionsScreenVM
import com.lighttigerxiv.simple.mp.compose.screens.setup.themes.ThemesScreenVM
import com.lighttigerxiv.simple.mp.compose.screens.setup.welcome.WelcomeScreen
import com.lighttigerxiv.simple.mp.compose.ui.theme.ComposeSimpleMPTheme
import com.lighttigerxiv.simple.mp.compose.app_viewmodels.ActivitySetupVM

class ActivityFirstSetup : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val setupVM = ViewModelProvider(this)[ActivitySetupVM::class.java]
        val activityContext = this


        val themeMode = setupVM.themeModeSetting!!
        val darkMode = setupVM.darkModeSetting!!

        setContent {
            ComposeSimpleMPTheme(
                useDarkTheme = isSystemInDarkTheme(),
                themeMode = themeMode,
                themeAccent = setupVM.themeAccentSetting.collectAsState().value,
                content = {

                    val themeAccent = setupVM.themeAccentSetting.collectAsState().value

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
                        startDestination = "Welcome",
                        modifier = Modifier
                            .fillMaxSize()
                            .background(surfaceColor)
                    ) {

                        composable("Welcome") {
                            WelcomeScreen(
                                onNextClicked = {
                                    navController.navigate("Permissions")
                                }
                            )
                        }

                        composable("Permissions") {
                            PermissionsScreen(
                                permissionsVM = ViewModelProvider(activityContext)[PermissionsScreenVM::class.java],
                                onBackClicked = { navController.navigateUp() },
                                onNextClicked = { navController.navigate("Themes") }
                            )
                        }

                        composable("Themes") {
                            ThemesScreen(
                                setupVM = setupVM,
                                themesScreenVM = ViewModelProvider(activityContext)[ThemesScreenVM::class.java],
                                onBackClicked = { navController.navigateUp() },
                                onFinish = { finish() }
                            )
                        }
                    }
                }
            )
        }
    }
}