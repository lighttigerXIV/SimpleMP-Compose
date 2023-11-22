package com.lighttigerxiv.simple.mp.compose.frontend.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.frontend.utils.Sizes

@Composable
fun PlayShuffleRow(
    onPlayClick: () -> Unit,
    onShuffleClick: () -> Unit
) {

    Row(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = true)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .clickable { onPlayClick() }
                .padding(Sizes.MEDIUM),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                modifier = Modifier.size(18.dp),
                painter = painterResource(id = R.drawable.play),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )

            HSpacer(size = Sizes.SMALL)

            Text(
                text = stringResource(id = R.string.play),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        HSpacer(size = Sizes.SMALL)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = true)
                .clip(CircleShape)
                .border(1.dp, MaterialTheme.colorScheme.primary, shape = CircleShape)
                .background(MaterialTheme.colorScheme.surface)
                .clickable { onShuffleClick() }
                .padding(Sizes.MEDIUM),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                modifier = Modifier.size(18.dp),
                painter = painterResource(id = R.drawable.shuffle),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            HSpacer(size = Sizes.SMALL)

            Text(
                text = stringResource(id = R.string.Shuffle),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}