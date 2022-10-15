package com.lighttigerxiv.simple.mp.compose.navigation.screens.setup

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Build
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.activities.MainActivity
import com.lighttigerxiv.simple.mp.compose.viewmodels.ActivityFirstSetupViewModel
import moe.tlaster.nestedscrollview.VerticalNestedScrollView
import moe.tlaster.nestedscrollview.rememberNestedScrollViewState

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
                .fillMaxWidth()
                .weight(weight = 1f, fill = true)
        ) {

            VerticalNestedScrollView(
                state = rememberNestedScrollViewState(),
                content = {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .weight(weight = 1f, fill = true)
                    ) {

                        Spacer(modifier = Modifier.height(40.dp))

                        Text(
                            text = "Theming",
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


                        if (Build.VERSION.SDK_INT >= 31) {

                            Text(
                                text = "System",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 18.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            LazyVerticalGrid(
                                columns = GridCells.Fixed(5),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                content = {

                                    item {
                                        ThemeBall(
                                            color = MaterialTheme.colorScheme.primary,
                                            selected = selectedTheme == "Default",
                                            onClick = { activityFirstSetupViewModel.selectedTheme.value = "Default" }
                                        )
                                    }
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Accents",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 18.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        LazyVerticalGrid(
                            modifier = Modifier.fillMaxWidth(),
                            columns = GridCells.Fixed(5),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            content = {

                                item {

                                    ThemeBall(
                                        color = Color(0xFF0058CC),
                                        selected = selectedTheme == "Blue",
                                        onClick = { activityFirstSetupViewModel.selectedTheme.value = "Blue" }
                                    )
                                }

                                item {

                                    ThemeBall(
                                        color = Color(0xFFC0001B),
                                        selected = selectedTheme == "Red",
                                        onClick = { activityFirstSetupViewModel.selectedTheme.value = "Red" }
                                    )
                                }

                                item {

                                    ThemeBall(
                                        color = Color(0xFF682BF5),
                                        selected = selectedTheme == "Purple",
                                        onClick = { activityFirstSetupViewModel.selectedTheme.value = "Purple" }
                                    )
                                }

                                item {

                                    ThemeBall(
                                        color = Color(0xFF835400),
                                        selected = selectedTheme == "Yellow",
                                        onClick = { activityFirstSetupViewModel.selectedTheme.value = "Yellow" }
                                    )
                                }

                                item {

                                    ThemeBall(
                                        color = Color(0xFF9C4400),
                                        selected = selectedTheme == "Orange",
                                        onClick = { activityFirstSetupViewModel.selectedTheme.value = "Orange" }
                                    )
                                }

                                item {

                                    ThemeBall(
                                        color = Color(0xFF326B00),
                                        selected = selectedTheme == "Green",
                                        onClick = { activityFirstSetupViewModel.selectedTheme.value = "Green" }
                                    )
                                }

                                item {

                                    ThemeBall(
                                        color = Color(0xFFB90063),
                                        selected = selectedTheme == "Pink",
                                        onClick = { activityFirstSetupViewModel.selectedTheme.value = "Pink" }
                                    )
                                }
                            }
                        )
                    }
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
                    text = "Back",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.width(5.dp))

            androidx.compose.material3.Button(
                onClick = {

                    val preferences = context.getSharedPreferences(context.packageName, MODE_PRIVATE)
                    preferences.edit().putString("ThemeAccent", selectedTheme).apply()
                    preferences.edit().putBoolean("setupCompleted", true).apply()

                    context.startActivity( Intent( context, MainActivity::class.java ) )
                    onKillActivity()
                },
                enabled = isButtonFinishEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {

                Text(
                    text = "Finish",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}


@Composable
fun ThemeBall(
    color: Color = Color.Transparent,
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {

    when {

        selected -> {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .border(
                        width = 4.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = RoundedCornerShape(percent = 100)
                    )
                    .clip(RoundedCornerShape(percent = 100))
                    .background(color)
                    .clickable { onClick() }
            )
        }

        else -> {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(percent = 100))
                    .background(color)
                    .clickable { onClick() }
            )
        }
    }
}