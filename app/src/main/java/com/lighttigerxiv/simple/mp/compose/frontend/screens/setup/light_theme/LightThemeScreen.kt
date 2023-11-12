package com.lighttigerxiv.simple.mp.compose.frontend.screens.setup.light_theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.frontend.composables.HSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.composables.PrimaryButton
import com.lighttigerxiv.simple.mp.compose.frontend.composables.VSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goBack
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goToDarkTheme
import com.lighttigerxiv.simple.mp.compose.frontend.utils.Sizes

@Composable
fun LightThemeScreen(
    navController: NavHostController
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f, fill = true)
        ) {

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
                navController.goToDarkTheme()
            }
        }
    }
}