package com.lighttigerxiv.simple.mp.compose.frontend.screens.setup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.Routes
import com.lighttigerxiv.simple.mp.compose.frontend.screens.setup.dark_theme.DarkThemeScreen
import com.lighttigerxiv.simple.mp.compose.frontend.screens.setup.light_theme.LightThemeScreen
import com.lighttigerxiv.simple.mp.compose.frontend.screens.setup.other_settings.OtherSettingsScreen
import com.lighttigerxiv.simple.mp.compose.frontend.screens.setup.permissions.PermissionsScreen
import com.lighttigerxiv.simple.mp.compose.frontend.screens.setup.sync_library.SyncLibraryScreen
import com.lighttigerxiv.simple.mp.compose.frontend.screens.setup.welcome.WelcomeScreen

@Composable
fun SetupScreen() {

    val navController = rememberNavController()

    NavHost(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface),
        navController = navController,
        startDestination = Routes.Setup.WELCOME
    ) {

        composable(Routes.Setup.WELCOME) {
            WelcomeScreen(navController = navController)
        }

        composable(Routes.Setup.PERMISSIONS){
            PermissionsScreen(navController = navController)
        }

        composable(Routes.Setup.LIGHT_THEME){
            LightThemeScreen(navController = navController)
        }

        composable(Routes.Setup.DARK_THEME){
            DarkThemeScreen(navController = navController)
        }

        composable(Routes.Setup.OTHER_SETTINGS){
            OtherSettingsScreen(navController = navController)
        }

        composable(Routes.Setup.SYNC_LIBRARY){
            SyncLibraryScreen()
        }
    }
}