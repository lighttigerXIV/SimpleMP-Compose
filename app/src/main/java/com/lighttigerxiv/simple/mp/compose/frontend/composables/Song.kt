package com.lighttigerxiv.simple.mp.compose.frontend.composables

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Song
import com.lighttigerxiv.simple.mp.compose.frontend.utils.Sizes
import com.lighttigerxiv.simple.mp.compose.frontend.utils.modifyIf
import org.burnoutcrew.reorderable.ReorderableLazyListState
import org.burnoutcrew.reorderable.detectReorder

@Composable
fun SongCard(
    song: Song,
    artistName: String,
    art: Bitmap?,
    highlight: Boolean = false,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Sizes.SMALL))
            .clickable { onClick() }
    ) {

        if (art != null) {
            Image(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(Sizes.SMALL)),
                bitmap = remember { mutableStateOf(art.asImageBitmap()) }.value,
                contentDescription = null
            )
        } else {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(Sizes.SMALL))
                    .size(70.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)

            ) {
                Icon(
                    modifier = Modifier.size(70.dp),
                    painter = painterResource(id = R.drawable.album),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }


        HSpacer(size = Sizes.LARGE)

        Column {

            Text(
                text = song.name,
                fontWeight = if (highlight) FontWeight.Bold else FontWeight.Medium,
                color = if (highlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = artistName,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Composable
fun PlayingListSongCard(
    song: Song,
    artistName: String,
    art: Bitmap?,
    state: ReorderableLazyListState,
    isDragging: Boolean
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .clip(RoundedCornerShape(Sizes.SMALL))
            .background(if (isDragging) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant)
    ) {

        if (art != null) {
            Image(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(Sizes.SMALL)),
                bitmap = remember { mutableStateOf(art.asImageBitmap()) }.value,
                contentDescription = null
            )
        } else {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(Sizes.SMALL))
                    .size(70.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)

            ) {
                Icon(
                    modifier = Modifier.size(70.dp),
                    painter = painterResource(id = R.drawable.album),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }


        HSpacer(size = Sizes.LARGE)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = true)
        ) {

            Text(
                text = song.name,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = artistName,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {

            Icon(
                modifier = Modifier
                    .size(30.dp)
                    .detectReorder(state),
                painter = painterResource(id = R.drawable.drag),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}