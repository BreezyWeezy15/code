package com.rick.morty

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        Screen.CharacterListScreen,
        Screen.CharacterScreen,
        Screen.InsertCharacterScreen
    )

    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.primary, // Set background color
        elevation = 8.dp, // Add elevation for better depth
        modifier = Modifier.clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)) // Rounded corners
    ) {
        val currentBackStackEntry = navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStackEntry.value?.destination

        items.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        screen.icon,
                        contentDescription = screen.label,
                        tint = if (currentDestination?.route == screen.route) {
                            MaterialTheme.colorScheme.onPrimary // Highlight selected icon
                        } else {
                            MaterialTheme.colorScheme.onSurface // Default icon color
                        }
                    )
                },
                label = {
                    Text(
                        screen.label,
                        color = if (currentDestination?.route == screen.route) {
                            MaterialTheme.colorScheme.onPrimary // Highlight label
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f) // Dim label when unselected
                        }
                    )
                },
                selected = currentDestination?.route == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        // Avoid multiple copies of the same destination
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
