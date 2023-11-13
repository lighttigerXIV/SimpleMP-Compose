package com.lighttigerxiv.simple.mp.compose.frontend.screens.setup.other_settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.navigation.NavHostController
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.backend.settings.SettingsVM
import com.lighttigerxiv.simple.mp.compose.frontend.composables.HSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.composables.PrimaryButton
import com.lighttigerxiv.simple.mp.compose.frontend.composables.SettingsSwitch
import com.lighttigerxiv.simple.mp.compose.frontend.composables.VSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goBack
import com.lighttigerxiv.simple.mp.compose.frontend.navigation.goToSyncLibrary
import com.lighttigerxiv.simple.mp.compose.frontend.utils.Sizes

@Composable
fun OtherSettingsScreen(
    navController: NavHostController,
    settingsVM: SettingsVM
){

    val settings = settingsVM.settings.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
    ){

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f, fill = true)
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Icon(
                    modifier = Modifier.size(80.dp),
                    painter = painterResource(id = R.drawable.other_filled),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = stringResource(id = R.string.other_settings),
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
            ){

                SettingsSwitch(
                    checked = settings!!.downloadArtistCover,
                    iconId = R.drawable.database,
                    text = stringResource(id = R.string.download_artist_cover),
                    onCheckedChange = { settingsVM.updateDownloadArtistCover(it) }
                )

                SettingsSwitch(
                    checked = settings.downloadArtistCoverWithData,
                    iconId = R.drawable.database,
                    text = stringResource(id = R.string.download_artist_cover),
                    enabled = settings.downloadArtistCover,
                    onCheckedChange = { settingsVM.updateDownloadArtistCoverWithData(it) }
                )
            }

        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ){

            PrimaryButton(text = stringResource(id = R.string.back)) {
                navController.goBack()
            }

            HSpacer(size = Sizes.MEDIUM)

            PrimaryButton(text = stringResource(id = R.string.next)) {
                navController.goToSyncLibrary()
            }
        }
    }
}
