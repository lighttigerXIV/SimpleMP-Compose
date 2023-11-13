package com.lighttigerxiv.simple.mp.compose.frontend.screens.setup.sync_library

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.backend.settings.SettingsVM
import com.lighttigerxiv.simple.mp.compose.backend.viewmodels.AppVM
import com.lighttigerxiv.simple.mp.compose.frontend.composables.VSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.utils.FontSizes
import com.lighttigerxiv.simple.mp.compose.frontend.utils.Sizes

@Composable
fun SyncLibrarySscreen(
    appVM: AppVM,
    settingsVM: SettingsVM
) {

    val vm: SyncLibraryScreenVM = viewModel()
    val syncingLibrary = vm.syncingLibrary.collectAsState().value

    LaunchedEffect(syncingLibrary) {
        if (!syncingLibrary) vm.syncLibrary(settingsVM, appVM)
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f, fill = true)
        )

        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                modifier = Modifier.size(150.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                strokeWidth = 6.dp
            )

            Icon(
                modifier = Modifier.size(100.dp),
                painter = painterResource(id = R.drawable.play_empty),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }

        VSpacer(size = Sizes.LARGE)

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f, fill = true)
        )

        Text(
            text = stringResource(id = R.string.loading_library),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium,
            fontSize = FontSizes.HEADER_2
        )
    }
}