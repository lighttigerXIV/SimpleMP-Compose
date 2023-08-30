package com.lighttigerxiv.simple.mp.compose.navigation

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.lighttigerxiv.simple.mp.compose.data.variables.Routes

@Composable
fun BottomNavigationBar(
    navController: NavController,
    items: List<BottomNavItem>,
    onItemClick: (BottomNavItem) -> Unit
) {

    val configuration = LocalConfiguration.current
    val inPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val backStackEntry = navController.currentBackStackEntryAsState()

    if(inPortrait){
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .layoutId("navbar"),
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
                        itemRoute == Routes.Main.ARTISTS && destinationRoute!!.startsWith(Routes.Main.ARTIST) -> true
                        itemRoute == Routes.Main.ARTISTS && destinationRoute!!.startsWith(Routes.Main.ARTIST_ALBUM) -> true
                        itemRoute == Routes.Main.ALBUMS && destinationRoute!!.startsWith(Routes.Main.ALBUM) -> true
                        itemRoute == Routes.Main.PLAYLISTS && destinationRoute!!.startsWith(Routes.Main.GENRE_PLAYLIST) -> true
                        itemRoute == Routes.Main.PLAYLISTS && destinationRoute!!.startsWith(Routes.Main.PLAYLIST) -> true
                        itemRoute == Routes.Main.ARTISTS && destinationRoute!!.startsWith(Routes.Main.SELECT_ARTIST_COVER) -> true
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

                                if(isItemSelected){
                                    Image(
                                        bitmap = remember{item.activeIcon},
                                        contentDescription = item.name,
                                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant),
                                        modifier = Modifier
                                            .height(30.dp)
                                            .width(30.dp)
                                    )
                                } else{
                                    Image(
                                        bitmap = remember{item.inactiveIcon},
                                        contentDescription = item.name,
                                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant),
                                        modifier = Modifier
                                            .height(30.dp)
                                            .width(30.dp)
                                    )
                                }

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
    } else{

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(60.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            items.forEach { item ->

                val destinationRoute = if(backStackEntry.value?.destination?.route == null) "" else backStackEntry.value?.destination?.route
                val itemRoute = item.route

                val isItemSelected = when {

                    itemRoute == destinationRoute-> true
                    itemRoute == Routes.Main.ARTISTS && destinationRoute!!.startsWith(Routes.Main.ARTIST) -> true
                    itemRoute == Routes.Main.ARTISTS && destinationRoute!!.startsWith(Routes.Main.ARTIST_ALBUM) -> true
                    itemRoute == Routes.Main.ALBUMS && destinationRoute!!.startsWith(Routes.Main.ALBUM) -> true
                    itemRoute == Routes.Main.PLAYLISTS && destinationRoute!!.startsWith(Routes.Main.GENRE_PLAYLIST) -> true
                    itemRoute == Routes.Main.PLAYLISTS && destinationRoute!!.startsWith(Routes.Main.PLAYLIST) -> true
                    itemRoute == Routes.Main.ARTISTS && destinationRoute!!.startsWith(Routes.Main.SELECT_ARTIST_COVER) -> true
                    else -> false
                }

                Row(
                    modifier = Modifier.height(55.dp)
                ) {
                    BottomNavigationItem(
                        selected = isItemSelected,
                        onClick = { onItemClick(item) },
                        icon = {

                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.wrapContentWidth(),
                            ) {


                                if(isItemSelected){
                                    Image(
                                        bitmap = remember{item.activeIcon},
                                        contentDescription = item.name,
                                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant),
                                        modifier = Modifier
                                            .height(26.dp)
                                            .width(26.dp)
                                    )
                                } else{
                                    Image(
                                        bitmap = remember{item.inactiveIcon},
                                        contentDescription = item.name,
                                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant),
                                        modifier = Modifier
                                            .height(26.dp)
                                            .width(26.dp)
                                    )
                                }

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
}