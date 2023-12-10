package com.lighttigerxiv.simple.mp.compose.frontend.composables

import android.graphics.Bitmap
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.lighttigerxiv.simple.mp.compose.R
import com.lighttigerxiv.simple.mp.compose.backend.realm.collections.Song
import com.lighttigerxiv.simple.mp.compose.frontend.utils.Sizes
import com.lighttigerxiv.simple.mp.compose.frontend.utils.modifyIf
import org.burnoutcrew.reorderable.ReorderableLazyListState
import org.burnoutcrew.reorderable.detectReorder


data class SongMenuItem(
    val id: String,
    val text: String,
    val iconId: Int
)


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongCard(
    song: Song,
    artistName: String,
    art: Bitmap?,
    highlight: Boolean = false,
    onClick: () -> Unit,
    menuItems: List<SongMenuItem>? = null,
    onMenuItemClick: (id: String) -> Unit = {}
) {

    var showMenu by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Sizes.SMALL))
            .combinedClickable(
                onClick = { onClick() },
                onLongClick = {
                    if (menuItems != null) {
                        showMenu = true
                    }
                }
            )
    ) {

        if (art != null) {
            Box(contentAlignment = Alignment.Center) {
                Image(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(RoundedCornerShape(Sizes.SMALL)),
                    bitmap = remember { mutableStateOf(art.asImageBitmap()) }.value,
                    contentDescription = null,
                    alpha = if (highlight) 0.1f else 1f
                )

                if (highlight) {
                    Icon(
                        modifier = Modifier.size(50.dp),
                        painter = painterResource(id = R.drawable.sound),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(Sizes.SMALL))
                    .size(70.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {

                if (highlight) {
                    Icon(
                        modifier = Modifier.size(50.dp),
                        painter = painterResource(id = R.drawable.sound),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Icon(
                        modifier = Modifier.size(50.dp),
                        painter = painterResource(id = R.drawable.album),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
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
                fontWeight = if (highlight) FontWeight.Bold else FontWeight.Medium,
                color = if (highlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = artistName,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        DropdownMenu(
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant),
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
        ) {
            menuItems?.forEach { menuItem ->
                MenuItem(
                    iconId = menuItem.iconId,
                    text = menuItem.text,
                    onClick = {
                        showMenu = false
                        onMenuItemClick(menuItem.id)
                    }
                )
            }
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
                bitmap = remember { art }.asImageBitmap(),
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


@Composable
fun AddToPlaylistSongCard(
    song: Song,
    artistName: String,
    art: Bitmap?,
    selected: Boolean,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clip(RoundedCornerShape(Sizes.SMALL))
            .clickable { onClick() }
    ) {

        if (art != null) {
            Box(contentAlignment = Alignment.Center) {
                Image(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(RoundedCornerShape(Sizes.SMALL)),
                    bitmap = remember { mutableStateOf(art.asImageBitmap()) }.value,
                    contentDescription = null
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(Sizes.SMALL))
                    .size(70.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    modifier = Modifier.size(50.dp),
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
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        HSpacer(size = Sizes.LARGE)

        Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
            Box(modifier = Modifier.size(30.dp)) {
                if (selected) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(id = R.drawable.check),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        HSpacer(size = Sizes.LARGE)
    }
}


@Composable
fun RemoveFromPlaylistSongCard(
    song: Song,
    artistName: String,
    art: Bitmap?,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clip(RoundedCornerShape(Sizes.SMALL))
    ) {

        if (art != null) {
            Box(contentAlignment = Alignment.Center) {
                Image(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(RoundedCornerShape(Sizes.SMALL)),
                    bitmap = remember { mutableStateOf(art.asImageBitmap()) }.value,
                    contentDescription = null
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(Sizes.SMALL))
                    .size(70.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    modifier = Modifier.size(50.dp),
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
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        HSpacer(size = Sizes.LARGE)

        Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {

            Icon(
                modifier = Modifier
                    .size(30.dp)
                    .clickable { onClick() },
                painter = painterResource(id = R.drawable.remove),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }

        HSpacer(size = Sizes.LARGE)
    }
}