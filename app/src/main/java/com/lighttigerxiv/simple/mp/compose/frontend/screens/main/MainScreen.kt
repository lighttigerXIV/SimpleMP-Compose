package com.lighttigerxiv.simple.mp.compose.frontend.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.Routes
import com.lighttigerxiv.simple.mp.compose.frontend.screens.main.library.LibraryScreen

@Composable
fun MainScreen(){
    val navController = rememberNavController()

    NavHost(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        navController = navController,
        startDestination =  Routes.Main.LIBRARY
    ){

        composable(Routes.Main.LIBRARY){
            LibraryScreen()
        }
    }
}