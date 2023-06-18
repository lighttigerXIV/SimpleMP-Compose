package com.lighttigerxiv.simple.mp.compose.activities.setup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.lighttigerxiv.simple.mp.compose.data.variables.Routes
import com.lighttigerxiv.simple.mp.compose.functions.getSurfaceColor
import com.lighttigerxiv.simple.mp.compose.screens.setup.other.OtherScreen
import com.lighttigerxiv.simple.mp.compose.screens.setup.permissions.PermissionsScreen
import com.lighttigerxiv.simple.mp.compose.screens.setup.themes.ThemesScreen
import com.lighttigerxiv.simple.mp.compose.screens.setup.permissions.PermissionsScreenVM
import com.lighttigerxiv.simple.mp.compose.screens.setup.themes.ThemesScreenVM
import com.lighttigerxiv.simple.mp.compose.screens.setup.welcome.WelcomeScreen
import com.lighttigerxiv.simple.mp.compose.settings.SettingsVM
import com.lighttigerxiv.simple.mp.compose.ui.theme.ComposeSimpleMPTheme

class ActivitySetup : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val vm = ViewModelProvider(this)[ActivitySetupVM::class.java]
        val settingsVM = ViewModelProvider(this)[SettingsVM::class.java]
        val activityContext = this


        val themeMode = vm.themeModeSetting!!


        setContent {
            ComposeSimpleMPTheme(
                useDarkTheme = isSystemInDarkTheme(),
                themeMode = themeMode,
                themeAccent = vm.themeAccentSetting.collectAsState().value,
                content = {

                    val surfaceColor = getSurfaceColor(settingsVM = settingsVM)
                    rememberSystemUiController().setStatusBarColor(surfaceColor)

                    val navController = rememberNavController()


                    NavHost(
                        navController = navController,
                        startDestination = Routes.Setup.WELCOME,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(surfaceColor)
                    ) {

                        composable(Routes.Setup.WELCOME) {
                            WelcomeScreen(navController)
                        }

                        composable(Routes.Setup.PERMISSIONS) {
                            PermissionsScreen(
                                permissionsVM = ViewModelProvider(activityContext)[PermissionsScreenVM::class.java],
                                navController
                            )
                        }

                        composable(Routes.Setup.OTHER) {
                            OtherScreen(
                                settingsVM,
                                navController
                            )
                        }

                        composable(Routes.Setup.THEMES) {
                            ThemesScreen(
                                setupVM = vm,
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