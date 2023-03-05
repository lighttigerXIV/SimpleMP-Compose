package com.lighttigerxiv.simple.mp.compose.screens.setup.themes

import android.content.Context.MODE_PRIVATE
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.data.variables.SCREEN_PADDING
import com.lighttigerxiv.simple.mp.compose.activities.main.MainActivity
import com.lighttigerxiv.simple.mp.compose.ui.composables.ThemeSelector
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.MediumHeightSpacer
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.SmallHeightSpacer
import com.lighttigerxiv.simple.mp.compose.ui.composables.text.TitleMedium
import com.lighttigerxiv.simple.mp.compose.functions.getAppString
import com.lighttigerxiv.simple.mp.compose.activities.setup.ActivitySetupVM

@Composable
fun ThemesScreen(
    setupVM: ActivitySetupVM,
    themesScreenVM: ThemesScreenVM,
    onBackClicked: () -> Unit = {},
    onFinish: () -> Unit = {}
) {

    val context = LocalContext.current
    val selectedTheme = themesScreenVM.selectedTheme.collectAsState().value
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
                    painter = painterResource(id = R.drawable.brush_hd),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                )

                SmallHeightSpacer()

                TitleMedium(
                    text = stringResource(id = R.string.Theming),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            MediumHeightSpacer()

            ThemeSelector(
                selectedTheme = selectedTheme,
                onThemeClick = { accent->
                    themesScreenVM.updateSelectedTheme(accent)
                    setupVM.updateThemeAccent(accent)
                }
            )

        }

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

                    val preferences = context.getSharedPreferences(context.packageName, MODE_PRIVATE)
                    preferences.edit().putString("ThemeAccent", selectedTheme).apply()
                    preferences.edit().putBoolean("setupCompleted", true).apply()

                    context.startActivity(Intent(context, MainActivity::class.java))
                    onFinish()
                },
                enabled = isButtonFinishEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {

                Text(
                    text = remember { getAppString(context, R.string.Finish) },
                    color = if(isButtonFinishEnabled) MaterialTheme.colorScheme.onPrimary else Color.Black
                )
            }
        }
    }
}