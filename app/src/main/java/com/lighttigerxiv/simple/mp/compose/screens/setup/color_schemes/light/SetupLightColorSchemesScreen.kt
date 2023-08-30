package com.lighttigerxiv.simple.mp.compose.screens.setup.color_schemes.light

import android.content.Intent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.data.variables.SCREEN_PADDING
import com.lighttigerxiv.simple.mp.compose.activities.main.MainActivity
import com.lighttigerxiv.simple.mp.compose.ui.composables.ThemeSelector
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.MediumVerticalSpacer
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.SmallVerticalSpacer
import com.lighttigerxiv.simple.mp.compose.ui.composables.text.TitleMedium
import com.lighttigerxiv.simple.mp.compose.functions.getAppString
import com.lighttigerxiv.simple.mp.compose.data.variables.ImageSizes
import com.lighttigerxiv.simple.mp.compose.functions.getImage
import com.lighttigerxiv.simple.mp.compose.functions.isAtLeastAndroid10
import com.lighttigerxiv.simple.mp.compose.settings.SettingsVM

@Composable
fun SetupLightColorSchemesScreen(
    vm: SetupLightColorSchemesScreenVM,
    settingsVM: SettingsVM,
    onBackClicked: () -> Unit = {},
    onNext: () -> Unit,
    onFinish: () -> Unit = {}
) {

    val context = LocalContext.current
    val selectedTheme = vm.selectedTheme.collectAsState().value
    val isButtonFinishEnabled = when {

        selectedTheme != "" -> true
        else -> false
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(SCREEN_PADDING)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .weight(weight = 1f, fill = true)
                .verticalScroll(rememberScrollState())
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    modifier = Modifier
                        .height(80.dp)
                        .width(80.dp),
                    contentScale = ContentScale.Fit,
                    bitmap = remember{ getImage(context, R.drawable.brush_filled, ImageSizes.LARGE).asImageBitmap() },
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                )

                SmallVerticalSpacer()

                TitleMedium(
                    text = stringResource(id = if(isAtLeastAndroid10()) R.string.LightColorScheme else R.string.ColorScheme),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            MediumVerticalSpacer()

            ThemeSelector(
                selectedTheme = selectedTheme,
                onClick = { colorScheme->
                    vm.applyColorScheme(colorScheme, settingsVM)
                }
            )

        }

        MediumVerticalSpacer()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(fill = true, weight = 1f)
            )


            androidx.compose.material3.Button(
                onClick = {
                    onBackClicked()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {

                Text(
                    text = remember { getAppString(context, R.string.Back) },
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.width(5.dp))

            androidx.compose.material3.Button(
                onClick = {

                    if(isAtLeastAndroid10()){
                        onNext()
                    }else{
                        context.startActivity(Intent(context, MainActivity::class.java))
                        onFinish()
                    }
                },
                enabled = isButtonFinishEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {

                Text(
                    text = remember { if(isAtLeastAndroid10()) getAppString(context, R.string.Next) else getAppString(context, R.string.Finish) },
                    color = if(isButtonFinishEnabled) MaterialTheme.colorScheme.onPrimary else Color.Black
                )
            }
        }
    }
}