package com.lighttigerxiv.simple.mp.compose.frontend.screens.main.about

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.lighttigerxiv.simple.mp.compose.BuildConfig
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.frontend.composables.CollapsableHeader
import com.lighttigerxiv.simple.mp.compose.frontend.composables.HSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.composables.Toolbar
import com.lighttigerxiv.simple.mp.compose.frontend.composables.VSpacer
import com.lighttigerxiv.simple.mp.compose.frontend.utils.FontSizes
import com.lighttigerxiv.simple.mp.compose.frontend.utils.Sizes

@Composable
fun AboutScreen(
    rootController: NavHostController
) {

    val context = LocalContext.current

    Column(modifier = Modifier
        .padding(Sizes.LARGE)
    ) {

        Toolbar(navController = rootController)

        CollapsableHeader(
            header = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier
                        .clip(CircleShape)
                        .border(4.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        .padding(Sizes.LARGE)
                    ) {
                        Icon(
                            modifier = Modifier.size(90.dp),
                            painter = painterResource(id = R.drawable.play_empty),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    VSpacer(size = Sizes.LARGE)

                    Text(
                        modifier = Modifier.offset(x = 8.dp),
                        text = "Simple MP",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium,
                        fontSize = 35.sp
                    )
                }
            }
        ) {

            VSpacer(size = Sizes.LARGE)

            Text(
                modifier = Modifier.offset(x = 8.dp),
                text = stringResource(id = R.string.AppVersion),
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(Sizes.LARGE))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(Sizes.LARGE)
            ) {
                Text(
                    text = remember { BuildConfig.VERSION_NAME },
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
            }

            VSpacer(size = Sizes.LARGE)

            Text(
                modifier = Modifier.offset(x = 8.dp),
                text = stringResource(id = R.string.source_code),
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(Sizes.LARGE))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable {
                        context.startActivity(
                            Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/lighttigerXIV/SimpleMP-Compose"))
                        )
                    }
                    .padding(Sizes.LARGE),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(id = R.drawable.github),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )

                HSpacer(size = Sizes.SMALL)

                Text(
                    text = "GitHub",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}