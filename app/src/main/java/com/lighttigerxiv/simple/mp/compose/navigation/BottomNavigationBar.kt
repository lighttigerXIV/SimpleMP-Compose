package com.lighttigerxiv.simple.mp.compose.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(
    navController: NavController,
    items: List<BottomNavItem>,
    onItemClick: (BottomNavItem) -> Unit
) {

    val backStackEntry = navController.currentBackStackEntryAsState()

    Row(
        modifier = Modifier

            .fillMaxWidth()
            .height(55.dp)
            .background(Color.Transparent),
        horizontalArrangement = Arrangement.Center
    ){

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            items.forEach { item ->

                val destinationRoute = if(backStackEntry.value?.destination?.route == null) "" else backStackEntry.value?.destination?.route
                val itemRoute = item.route

                val isItemSelected = when {

                    itemRoute == destinationRoute-> true
                    itemRoute == "Artists" && destinationRoute!!.startsWith("Artist") -> true
                    itemRoute == "Artists" && destinationRoute!!.startsWith("ArtistAlbum") -> true
                    itemRoute == "Albums" && destinationRoute!!.startsWith("Album") -> true
                    itemRoute == "Playlists" && destinationRoute!!.startsWith("GenrePlaylist") -> true
                    itemRoute == "Playlists" && destinationRoute!!.startsWith("Playlist") -> true
                    else -> false
                }


                BottomNavigationItem(
                    selected = isItemSelected,
                    onClick = { onItemClick(item) },
                    icon = {

                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.wrapContentWidth(),
                        ) {


                            Image(
                                bitmap = if (isItemSelected) item.activeIcon else item.inactiveIcon,
                                contentDescription = item.name,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant),
                                modifier = Modifier
                                    .height(26.dp)
                                    .width(26.dp)
                            )

                            Spacer(modifier = Modifier.height(5.dp))

                            if (isItemSelected) {

                                Box(
                                    modifier = Modifier
                                        .width(20.dp)
                                        .height(2.dp)
                                        .clip(RoundedCornerShape(percent = 100))
                                        .background(MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                            }
                            else{

                                Box(modifier = Modifier.height(2.dp))
                            }
                        }
                    }
                )
            }
        }
    }
}