package com.lighttigerxiv.simple.mp.compose.frontend.activities.main


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.backend.settings.SettingsVM
import com.lighttigerxiv.simple.mp.compose.backend.viewmodels.AppVM
import com.lighttigerxiv.simple.mp.compose.frontend.screens.main.MainScreen
import com.lighttigerxiv.simple.mp.compose.frontend.screens.setup.SetupScreen
import com.lighttigerxiv.simple.mp.compose.frontend.theme.SimpleMPTheme
import com.lighttigerxiv.simple.mp.compose.frontend.utils.ChangeNavigationBarsColor
import com.lighttigerxiv.simple.mp.compose.frontend.utils.ChangeStatusBarColor


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val vm = ViewModelProvider(this)[AppVM::class.java]
        val settingsVM = ViewModelProvider(this)[SettingsVM::class.java]

        setContent {

            val settings = settingsVM.settings.collectAsState().value

            SimpleMPTheme(settings){

                ChangeStatusBarColor(color = MaterialTheme.colorScheme.surface)
                ChangeNavigationBarsColor(color = MaterialTheme.colorScheme.surface)

                val initialized = vm.initialized.collectAsState().value
                
                if(initialized && settings != null){
                    
                    if(!settings.setupCompleted){
                        SetupScreen(appVM = vm,settingsVM = settingsVM)
                    }else{
                        MainScreen(appVM = vm, settingsVM = settingsVM)
                    }

                }else{

                    //Splash Screen
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        
                        Icon(
                            modifier = Modifier.size(200.dp),
                            painter = painterResource(id = R.drawable.play_empty),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}