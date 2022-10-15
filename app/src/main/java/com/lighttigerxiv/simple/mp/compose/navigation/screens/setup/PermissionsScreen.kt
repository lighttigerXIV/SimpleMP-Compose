package com.lighttigerxiv.simple.mp.compose.navigation.screens.setup

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lighttigerxiv.simple.mp.compose.viewmodels.ActivityFirstSetupViewModel

@Composable
fun PermissionsScreen(
    activityFirstSetupViewModel: ActivityFirstSetupViewModel,
    onBackClicked: () -> Unit = {},
    onNextClicked: () -> Unit = {},
    onRequestNotificationPermission: () -> Unit = {},
    onRequestStoragePermission: () -> Unit = {}
) {

    val storagePermissionText = when (activityFirstSetupViewModel.isStoragePermissionGranted.observeAsState().value!!) {

        true -> "Granted"
        false -> "Grant"
    }

    val isButtonNextEnabled = when{

        (Build.VERSION.SDK_INT >= 33)
                && activityFirstSetupViewModel.isStoragePermissionGranted.observeAsState().value!!
                && activityFirstSetupViewModel.isNotificationPermissionGranted.observeAsState().value!! -> true

        (Build.VERSION.SDK_INT < 33) && activityFirstSetupViewModel
            .isStoragePermissionGranted.observeAsState().value!! -> true

        else -> false
    }


    println("Is button enabled -> $isButtonNextEnabled")


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .weight(fill = true, weight = 1f)
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Permissions",
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
                    painter = painterResource(id = com.lighttigerxiv.simple.mp.compose.R.drawable.icon_folder_regular),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))


            //Storage Permission Card
            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(
                        top = 20.dp,
                        bottom = 20.dp,
                        start = 10.dp,
                        end = 10.dp
                    )
            ) {

                Text(
                    text = "Please enable storage permission in order to read all music files in your device",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(weight = 1f, fill = true)
                )

                Spacer(modifier = Modifier.width(5.dp))

                Row(
                    modifier = Modifier
                        .width(1.dp)
                ) {
                    Divider(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxHeight()
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Text(
                        text = storagePermissionText,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onRequestStoragePermission()
                        }
                    )
                }
            }

            //Notifications Permission Card

            if (Build.VERSION.SDK_INT >= 33) {

                val notificationPermissionText = when (activityFirstSetupViewModel.isNotificationPermissionGranted.observeAsState().value!!) {

                    true -> "Granted"
                    false -> "Grant"
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .height(IntrinsicSize.Min)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(
                            top = 20.dp,
                            bottom = 20.dp,
                            start = 10.dp,
                            end = 10.dp
                        )
                ) {

                    Text(
                        text = "Please enable notifications permission in order to make the app work properly when playing music",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(weight = 1f, fill = true)
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Row(
                        modifier = Modifier
                            .width(1.dp)
                    ) {
                        Divider(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.fillMaxHeight()
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Text(
                            text = notificationPermissionText,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                onRequestNotificationPermission()
                            }
                        )
                    }
                }
            }
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
                onClick = { onNextClicked() },
                enabled = isButtonNextEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {

                Text(
                    text = "Next",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}