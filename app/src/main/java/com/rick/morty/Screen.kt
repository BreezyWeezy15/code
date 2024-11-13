package com.rick.morty

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    data object CharacterListScreen : Screen("character_home", "Home", Icons.Default.List)
    data object CharacterScreen : Screen("character_screen", "Characters", Icons.Default.Person)
    data object InsertCharacterScreen : Screen("insert_character", "Add", Icons.Default.Add)
    data object CustomFeatureScreen : Screen("custom_feature", "Quiz", Icons.Default.Star) // New screen
}
