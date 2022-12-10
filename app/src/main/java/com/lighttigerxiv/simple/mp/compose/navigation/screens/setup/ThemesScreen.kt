package com.lighttigerxiv.simple.mp.compose.navigation.screens.setup

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.activities.MainActivity
import com.lighttigerxiv.simple.mp.compose.composables.ThemeSelector
import com.lighttigerxiv.simple.mp.compose.getAppString
import com.lighttigerxiv.simple.mp.compose.viewmodels.ActivityFirstSetupViewModel

@Composable
fun ThemesScreen(
    activityFirstSetupViewModel: ActivityFirstSetupViewModel,
    onBackClicked: () -> Unit = {},
    onKillActivity: () -> Unit = {}
) {

    val context = LocalContext.current
    val selectedTheme = activityFirstSetupViewModel.selectedTheme.observeAsState().value!!
    val isButtonFinishEnabled = when {

        selectedTheme != "" -> true
        else -> false
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .weight(weight = 1f, fill = true)
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = remember { getAppString(context, R.string.Theming) },
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.icon_theme_regular_highres),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            ThemeSelector(
                selectedTheme = selectedTheme,
                onThemeClick = { activityFirstSetupViewModel.updateThemeAccent(it) }
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
                    onKillActivity()
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