package com.lighttigerxiv.simple.mp.compose.frontend.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.frontend.utils.FontSizes
import com.lighttigerxiv.simple.mp.compose.frontend.utils.Sizes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    iconId: Int,
    title: String,
    primaryButtonText: String,
    disablePrimaryButton: Boolean = false,
    onPrimaryButtonClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {

    if (show) {
        Dialog(
            onDismissRequest = {onDismiss()},
            properties = DialogProperties(decorFitsSystemWindows = true)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(Sizes.XLARGE))
                    .clip(RoundedCornerShape(Sizes.XLARGE))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(Sizes.LARGE)
            ) {

                Column {
                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

                        Icon(
                            painter = painterResource(id = iconId),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )

                        VSpacer(size = Sizes.SMALL)

                        Text(
                            text = title,
                            fontWeight = FontWeight.Medium,
                            fontSize = FontSizes.HEADER,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    VSpacer(size = Sizes.SMALL)

                    Column { content() }

                    VSpacer(size = Sizes.LARGE)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {

                        SecondaryButton(
                            text = stringResource(id = R.string.cancel),
                            onClick = { onDismiss() }
                        )

                        HSpacer(size = Sizes.SMALL)

                        PrimaryButton(
                            text = primaryButtonText,
                            onClick = { onPrimaryButtonClick() },
                            disabled = disablePrimaryButton
                        )
                    }
                }
            }
        }
    }
}