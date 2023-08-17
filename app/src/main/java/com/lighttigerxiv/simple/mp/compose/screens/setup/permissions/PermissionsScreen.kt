package com.lighttigerxiv.simple.mp.compose.screens.setup.permissions

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.data.variables.MEDIUM_RADIUS
import com.lighttigerxiv.simple.mp.compose.data.variables.MEDIUM_SPACING
import com.lighttigerxiv.simple.mp.compose.data.variables.Routes
import com.lighttigerxiv.simple.mp.compose.data.variables.SCREEN_PADDING
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.MediumVerticalSpacer
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.SmallVerticalSpacer
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.SmallHorizontalSpacer
import com.lighttigerxiv.simple.mp.compose.ui.composables.text.TitleMedium
import com.lighttigerxiv.simple.mp.compose.functions.getAppString
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.MediumHorizontalSpacer

@Composable
fun PermissionsScreen(
    permissionsVM: PermissionsScreenVM,
    navController: NavHostController
) {

    val context = LocalContext.current
    val screenLoaded = permissionsVM.screenLoaded.collectAsState().value

    val storagePermissionGranted = permissionsVM.storagePermissionGranted.collectAsState().value

    val storagePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> permissionsVM.updateStoragePermissionGranted(granted) }
    )

    val allPermissionsGranted = if(Build.VERSION.SDK_INT >= 33){
        storagePermissionGranted && permissionsVM.notificationsPermissionGranted.collectAsState().value
    }else{
        storagePermissionGranted
    }

    //Loading...
    if (!screenLoaded) {
        permissionsVM.loadScreen()
    }




    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(SCREEN_PADDING)
            .background(MaterialTheme.colorScheme.surface)
    ) {

        if (screenLoaded) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .weight(1f, fill = true)
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
                        painter = painterResource(id = R.drawable.folder),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                    )

                    SmallVerticalSpacer()

                    TitleMedium(
                        text = stringResource(id = R.string.Permissions),
                        color = MaterialTheme.colorScheme.primary
                    )
                }


                Column(
                    modifier = Modifier
                ) {
                    MediumVerticalSpacer()

                    //Storage Permission Card
                    Row(
                        modifier = Modifier
                            .height(IntrinsicSize.Min)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(MEDIUM_RADIUS))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(MEDIUM_SPACING)
                    ) {

                        Text(
                            text = stringResource(R.string.EnableStorageExplanation),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(weight = 1f, fill = true)
                        )

                        MediumHorizontalSpacer()

                        Column(
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxHeight()
                        ) {

                            Switch(
                                checked = storagePermissionGranted,
                                onCheckedChange = {
                                    if (Build.VERSION.SDK_INT >= 33) {
                                        storagePermissionLauncher.launch(Manifest.permission.READ_MEDIA_AUDIO)
                                    } else {
                                        storagePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                                    }
                                }
                            )
                        }
                    }

                    //Notifications Permission Card
                    if (Build.VERSION.SDK_INT >= 33) {

                        val notificationsPermissionGranted = permissionsVM.notificationsPermissionGranted.collectAsState().value

                        val notificationsPermissionLauncher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.RequestPermission(),
                            onResult = { granted -> permissionsVM.updateNotificationsPermissionGranted(granted) }
                        )

                        SmallVerticalSpacer()

                        Row(
                            modifier = Modifier
                                .height(IntrinsicSize.Min)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(MEDIUM_SPACING)
                        ) {

                            Text(
                                text = stringResource(R.string.EnableNotificationExplanation),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(weight = 1f, fill = true)
                            )

                            MediumHorizontalSpacer()

                            Column(
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxHeight()
                            ) {

                                Switch(checked = notificationsPermissionGranted, onCheckedChange = {
                                    notificationsPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                })
                            }
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


                Button(
                    onClick = {
                        navController.navigate(Routes.Setup.WELCOME)
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

                Button(
                    onClick = { navController.navigate(Routes.Setup.OTHER) },
                    enabled = allPermissionsGranted,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {

                    Text(
                        text = remember { getAppString(context, R.string.Next) },
                        color = if (allPermissionsGranted) MaterialTheme.colorScheme.onPrimary else Color.Black
                    )
                }
            }
        }
    }
}