package com.lighttigerxiv.simple.mp.compose.frontend.navigation

import androidx.navigation.NavController



fun NavController.goBack(){
    this.navigateUp()
}

fun NavController.goToPermissions(){
    this.navigate(Routes.Setup.PERMISSIONS)
}

fun NavController.goToLightTheme(){
    this.navigate(Routes.Setup.LIGHT_THEME)
}

fun NavController.goToDarkTheme(){
    this.navigate(Routes.Setup.DARK_THEME)
}

fun NavController.goToOtherSettings(){
    this.navigate(Routes.Setup.OTHER_SETTINGS)
}

fun NavController.goToSyncLibrary(){
    this.navigate(Routes.Setup.SYNC_LIBRARY){
        popBackStack()
    }
}