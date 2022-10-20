package com.lighttigerxiv.simple.mp.compose.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.lighttigerxiv.simple.mp.compose.viewmodels.ActivityMainViewModel

@Composable
fun BottomNavigationBar(
    activityMainViewModel: ActivityMainViewModel,
    navController: NavController,
    items: ArrayList<BottomNavItem>,
    onItemClick: (BottomNavItem) -> Unit
) {

    val backStackEntry = navController.currentBackStackEntryAsState()

    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {

        BottomNavigation(
            elevation = 0.dp,
            backgroundColor = MaterialTheme.colorScheme.surfaceVariant
        ) {

            items.forEach { item ->

                val destinationRoute = if(backStackEntry.value?.destination?.route == null) "" else backStackEntry.value?.destination?.route
                val itemRoute = item.route

                println("Destination route => $destinationRoute")

                val isItemSelected = when {

                    itemRoute == destinationRoute-> true
                    itemRoute == "artistsScreen" && destinationRoute!!.startsWith("artistScreen") -> true
                    itemRoute == "artistsScreen" && destinationRoute!!.startsWith("artistAlbumScreen") -> true
                    itemRoute == "albumsScreen" && destinationRoute!!.startsWith("albumScreen") -> true
                    itemRoute == "playlistsScreen" && destinationRoute!!.startsWith("genrePlaylistScreen") -> true
                    itemRoute == "playlistsScreen" && destinationRoute!!.startsWith("playlistScreen") -> true
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
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
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
                                        .background(MaterialTheme.colorScheme.primary)
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