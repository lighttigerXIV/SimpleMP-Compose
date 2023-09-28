package com.lighttigerxiv.simple.mp.compose.screens.main.about

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lighttigerxiv.simple.mp.compose.BuildConfig
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.data.variables.SCREEN_PADDING
import com.lighttigerxiv.simple.mp.compose.ui.composables.CustomToolbar
import com.lighttigerxiv.simple.mp.compose.ui.composables.CustomText
import com.lighttigerxiv.simple.mp.compose.functions.getAppString
import com.lighttigerxiv.simple.mp.compose.activities.main.MainVM
import com.lighttigerxiv.simple.mp.compose.data.variables.MEDIUM_RADIUS
import com.lighttigerxiv.simple.mp.compose.data.variables.MEDIUM_SPACING
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.MediumVerticalSpacer
import com.lighttigerxiv.simple.mp.compose.ui.composables.spacers.SmallVerticalSpacer

@Composable
fun AboutScreen(
    mainVM: MainVM,
    onBackClick: () -> Unit,
) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(mainVM.surfaceColor.collectAsState().value)
            .padding(SCREEN_PADDING)
            .verticalScroll(rememberScrollState())
    ) {

        CustomToolbar(
            backText = remember { getAppString(context, R.string.Home) },
            onBackClick = { onBackClick() }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .weight(1f, fill = true)
                .verticalScroll(rememberScrollState())
        ) {


            MediumVerticalSpacer()

            CustomText(
                modifier = Modifier.offset(8.dp),
                text = remember { getAppString(context, R.string.AppVersion) },
                weight = FontWeight.Bold
            )

            CustomText(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(MEDIUM_RADIUS))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(MEDIUM_SPACING),
                text = remember { BuildConfig.VERSION_NAME }
            )

            MediumVerticalSpacer()

            CustomText(
                modifier = Modifier.offset(8.dp),
                text = remember { getAppString(context, R.string.Changelog) },
                weight = FontWeight.Bold
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(MEDIUM_RADIUS))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(MEDIUM_SPACING),
                color = MaterialTheme.colorScheme.onSurface,
                text = "- Added Experimental Car Player Mode - (It should open when connecting to android auto)\n\n" +
                        "- Added sort by artist option in Albums\n\n" +
                        "- Added German Translation (@Integraluminium)\n\n" +
                        "- Fixed local artist image not showing\n\n" +
                        "- Fixed other small bugs"
            )

            MediumVerticalSpacer()

            CustomText(
                modifier = Modifier.offset(8.dp),
                text = remember { getAppString(context, R.string.AppSource) },
                weight = FontWeight.Bold
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW).apply { data = Uri.parse("https://github.com/lighttigerXIV/SimpleMP-Compose") }
                        context.startActivity(intent)
                    }
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    modifier = Modifier
                        .height(30.dp)
                        .width(30.dp),
                    painter = painterResource(id = R.drawable.github),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.width(14.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = true)
                ) {

                    CustomText(
                        text = "GitHub",
                        weight = FontWeight.Bold
                    )

                    CustomText(
                        text = remember { getAppString(context, R.string.GetAppSourceOnGitHub) },
                    )
                }
            }

            MediumVerticalSpacer()

            CustomText(
                modifier = Modifier.offset(8.dp),
                text = remember { getAppString(context, R.string.Download) },
                weight = FontWeight.Bold
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW).apply { data = Uri.parse("https://github.com/lighttigerXIV/SimpleMP-Compose/releases") }
                        context.startActivity(intent)
                    }
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    modifier = Modifier
                        .height(30.dp)
                        .width(30.dp),
                    painter = painterResource(id = R.drawable.github),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.width(14.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = true)
                ) {

                    CustomText(
                        text = remember { getAppString(context, R.string.GitHubReleases) },
                        weight = FontWeight.Bold
                    )

                    CustomText(
                        text = remember { getAppString(context, R.string.DownloadInGitHubReleases) },
                    )
                }
            }

            SmallVerticalSpacer()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW).apply { data = Uri.parse("https://play.google.com/store/apps/details?id=com.lighttigerxiv.simple.mp") }
                        context.startActivity(intent)
                    }
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    modifier = Modifier
                        .height(30.dp)
                        .width(30.dp),
                    painter = painterResource(id = R.drawable.icon_play_store),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.width(14.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = true)
                ) {

                    CustomText(
                        text = remember { getAppString(context, R.string.PlayStore) },
                        weight = FontWeight.Bold
                    )

                    CustomText(
                        text = remember { getAppString(context, R.string.DownloadInPlayStore) },
                    )
                }
            }

            SmallVerticalSpacer()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW).apply { data = Uri.parse("https://f-droid.org/en/packages/com.lighttigerxiv.simple.mp/") }
                        context.startActivity(intent)
                    }
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    modifier = Modifier
                        .height(30.dp)
                        .width(30.dp),
                    painter = painterResource(id = R.drawable.icon_fdroid),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.width(14.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = true)
                ) {

                    CustomText(
                        text = remember { getAppString(context, R.string.FDroid) },
                        weight = FontWeight.Bold
                    )

                    CustomText(
                        text = remember { getAppString(context, R.string.DownloadInFDroid) },
                    )
                }
            }
        }
    }
}