package com.lighttigerxiv.simple.mp.compose.frontend.screens.setup.dark_theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.frontend.composables.HSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.composables.PrimaryButton
import com.lighttigerxiv.simple.mp.compose.frontend.composables.ThemeSelector
import com.lighttigerxiv.simple.mp.compose.frontend.composables.VSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goBack
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goToOtherSettings
import com.lighttigerxiv.simple.mp.compose.frontend.theme.PreviewTheme
import com.lighttigerxiv.simple.mp.compose.frontend.utils.ChangeNavigationBarsColor
import com.lighttigerxiv.simple.mp.compose.frontend.utils.ChangeStatusBarColor
import com.lighttigerxiv.simple.mp.compose.frontend.utils.Sizes

@Composable
fun DarkThemeScreen(
    navController: NavHostController,
    vm: DarkThemeScreenVM = viewModel(factory = DarkThemeScreenVM.Factory)
) {

    val settings = vm.settings.collectAsState().value



    if(settings != null){

        PreviewTheme(themeId = settings.darkTheme, darkTheme = true) {

            ChangeNavigationBarsColor(color = MaterialTheme.colorScheme.surface)
            ChangeStatusBarColor(color = MaterialTheme.colorScheme.surface)
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(Sizes.LARGE)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f, fill = true)
                        .verticalScroll(rememberScrollState())
                ) {

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Icon(
                            modifier = Modifier.size(80.dp),
                            painter = painterResource(id = R.drawable.brush_filled),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = stringResource(id = R.string.dark_theme),
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 24.sp
                        )
                    }

                    VSpacer(size = Sizes.LARGE)

                    ThemeSelector(
                        selectedTheme = settings.darkTheme,
                        onThemeSelected = { vm.updateTheme(it) },
                        darkTheme = true
                    )
                }

                VSpacer(size = Sizes.LARGE)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ){

                    PrimaryButton(text = stringResource(id = R.string.back)) {
                        navController.goBack()
                    }

                    HSpacer(size = Sizes.MEDIUM)

                    PrimaryButton(text = stringResource(id = R.string.next)) {
                        navController.goToOtherSettings()
                    }
                }
            }
        }
    }
}