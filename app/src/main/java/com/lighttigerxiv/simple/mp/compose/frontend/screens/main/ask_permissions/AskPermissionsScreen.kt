package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.ask_permissions

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.backend.utils.isAtLeastAndroid13
import com.lighttigerxiv.simple.mp.compose.frontend.composables.PermissionCard
import com.lighttigerxiv.simple.mp.compose.frontend.composables.VSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.screens.main.MainScreenVM
import com.lighttigerxiv.simple.mp.compose.frontend.utils.Sizes

@Composable
fun AskPermissionsScreen(
    vm: MainScreenVM
) {

    val uiState = vm.uiState.collectAsState().value

    val storagePermissionlauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { vm.reloadPermissions() }
    )

    val notificationPermissionlauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { vm.reloadPermissions() }
    )

    Column(Modifier.padding(Sizes.LARGE)){
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
                    painter = painterResource(id = R.drawable.folder),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = stringResource(id = R.string.permissions),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 24.sp
                )
            }

            VSpacer(size = Sizes.LARGE)

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(Sizes.XLARGE))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {

                PermissionCard(
                    checked = uiState.hasStoragePermission,
                    iconId = R.drawable.folder,
                    description = stringResource(R.string.EnableStorageExplanation)
                ) {
                    if (isAtLeastAndroid13()) {
                        storagePermissionlauncher.launch(Manifest.permission.READ_MEDIA_AUDIO)
                    } else {
                        storagePermissionlauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                }

                if (isAtLeastAndroid13()) {

                    PermissionCard(
                        checked = uiState.hasNotificationsPermission,
                        iconId = R.drawable.notification,
                        description = stringResource(R.string.EnableNotificationExplanation)
                    ) {
                        notificationPermissionlauncher.launch((Manifest.permission.POST_NOTIFICATIONS))
                    }
                }
            }

        }

        VSpacer(size = Sizes.LARGE)
        /*

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {

            PrimaryButton(
                text = stringResource(id = R.string.back),
                disabled =
            ) {
                onReloadPermissions()
            }
        }

         */
    }
}