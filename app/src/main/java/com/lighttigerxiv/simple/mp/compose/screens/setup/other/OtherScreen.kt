package com.lighttigerxiv.simple.mp.compose.screens.setup.other

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.data.variables.ImageSizes
import com.lighttigerxiv.simple.mp.compose.data.variables.Routes
import com.lighttigerxiv.simple.mp.compose.data.variables.SCREEN_PADDING
import com.lighttigerxiv.simple.mp.compose.functions.getAppString
import com.lighttigerxiv.simple.mp.compose.functions.getImage
import com.lighttigerxiv.simple.mp.compose.screens.main.settings.SwitchSettingItem
import com.lighttigerxiv.simple.mp.compose.settings.SettingsVM
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.MediumVerticalSpacer
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.SmallVerticalSpacer
import com.lighttigerxiv.simple.mp.compose.ui.composables.text.TitleMedium

@Composable
fun OtherScreen(
    settingsVM: SettingsVM,
    navController: NavHostController
) {

    val context = LocalContext.current
    val downloadArtistCoverSetting = settingsVM.downloadArtistCoverSetting.collectAsState().value
    val downloadOverDataSetting = settingsVM.downloadOverDataSetting.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(SCREEN_PADDING)
            .background(MaterialTheme.colorScheme.surface)
    ) {


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
                    bitmap = remember { getImage(context, R.drawable.other_filled, ImageSizes.LARGE).asImageBitmap() },
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                )

                SmallVerticalSpacer()

                TitleMedium(
                    text = stringResource(id = R.string.OtherSettings),
                    color = MaterialTheme.colorScheme.primary
                )
            }


            Column(
                modifier = Modifier
            ) {
                MediumVerticalSpacer()

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {

                    SwitchSettingItem(
                        icon = painterResource(id = R.drawable.icon_database),
                        settingText = remember { getAppString(context, R.string.DownloadArtistCoverFromInternet) },
                        settingValue = downloadArtistCoverSetting,
                        onToggle = {

                            settingsVM.updateDownloadArtistCoverSetting(!downloadArtistCoverSetting)
                        }
                    )

                    SwitchSettingItem(
                        icon = painterResource(id = R.drawable.icon_database),
                        settingText = remember { getAppString(context, R.string.DownloadArtistCoverOnData) },
                        settingValue = downloadOverDataSetting,
                        onToggle = {

                            settingsVM.updateDownloadOverDataSetting(!downloadOverDataSetting)
                        },
                        enabled = downloadArtistCoverSetting
                    )
                }
            }
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


            Button(
                onClick = {
                    navController.navigate(Routes.Setup.PERMISSIONS)
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
                onClick = { navController.navigate(Routes.Setup.THEMES) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {

                Text(
                    text = remember { getAppString(context, R.string.Next) },
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}