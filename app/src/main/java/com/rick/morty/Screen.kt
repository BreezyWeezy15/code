package com.rick.morty

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val icon: ImageVector, val label: String) {
    data object CharacterListScreen : Screen("character_list_screen", Icons.Default.List, "Characters")
    data object CharacterScreen : Screen("character_screen", Icons.Default.Person, "Details")
    data object InsertCharacterScreen : Screen("insert_character_screen", Icons.Default.Add, "Add")
}
