package com.lighttigerxiv.simple.mp.compose.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(
    navController: NavController,
    items: ArrayList<BottomNavItem>,
    onItemClick: (BottomNavItem) -> Unit
){

    val backStackEntry = navController.currentBackStackEntryAsState()

    BottomNavigation(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant,
        elevation = 2.dp
    ) {

        items.forEach { item->

            val isItemSelected = when{

                item.route == backStackEntry.value?.destination?.route -> true
                item.route == "artistsScreen" && backStackEntry.value?.destination?.route == "artistScreen"-> true
                item.route == "artistsScreen" && backStackEntry.value?.destination?.route == "artistAlbumScreen"-> true
                item.route == "albumsScreen" && backStackEntry.value?.destination?.route == "albumScreen"-> true
                item.route == "playlistsScreen" && backStackEntry.value?.destination?.route == "genrePlaylistScreen" -> true
                item.route == "playlistsScreen" && backStackEntry.value?.destination?.route == "playlistScreen" -> true
                else -> false
            }


            BottomNavigationItem(
                selected = isItemSelected,
                onClick = { onItemClick(item) },
                icon = {

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        if(isItemSelected){

                            Image(
                                bitmap = item.activeIcon,
                                contentDescription = item.name,
                                colorFilter = ColorFilter.tint(androidx.compose.material3.MaterialTheme.colorScheme.onSurface),
                                modifier = Modifier
                                    .height(25.dp)
                                    .width(25.dp)
                            )
                        }
                        else{

                            Image(
                                bitmap = item.inactiveIcon,
                                contentDescription = item.name,
                                colorFilter = ColorFilter.tint(color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface),
                                modifier = Modifier
                                    .height(25.dp)
                                    .width(25.dp)
                            )
                        }
                        Text(
                            text = item.name,
                            textAlign = TextAlign.Center,
                            fontSize = 15.sp,
                            color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    }
}