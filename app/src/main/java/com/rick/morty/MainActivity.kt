package com.rick.morty

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rick.morty.business.MainViewModel
import com.rick.morty.components.BottomNavigationBar
import com.rick.morty.components.Screen
import com.rick.morty.screens.CharacterListScreen
import com.rick.morty.screens.CharacterScreen
import com.rick.morty.screens.CharacterSelectionScreen
import com.rick.morty.screens.InsertCharacterScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Override the onCreate method to set up the initial screen
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up the content view for the activity
        setContent {
            // Initialize the navigation controller for handling screen transitions
            val navController = rememberNavController()

            // Get the ViewModel using Hilt for dependency injection
            val viewModel: MainViewModel = hiltViewModel()

            // Scaffold provides a basic layout structure with support for bottom navigation bar
            Scaffold(
                bottomBar = {
                    // Add the bottom navigation bar to the scaffold
                    BottomNavigationBar(navController)
                }
            ) { paddingValues ->
                // Set up the navigation graph with padding from the scaffold
                AppNavigation(
                    navController = navController,
                    viewModel = viewModel,
                    paddingValues = paddingValues
                )
            }
        }
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: MainViewModel,
    paddingValues: PaddingValues
) {
    // Set up the navigation host with the start destination and padding
    NavHost(
        navController = navController,
        startDestination = Screen.CharacterListScreen.route, // Starting screen of the app
        modifier = Modifier.padding(paddingValues) // Apply the padding received from the scaffold
    ) {
        // Define the composables for each screen in the app
        composable(Screen.CharacterListScreen.route) {
            // Navigate to the Character List screen
            CharacterListScreen(viewModel = viewModel)
        }
        composable(Screen.CharacterScreen.route) {
            // Navigate to a specific character screen
            CharacterScreen(viewModel = viewModel)
        }
        composable(Screen.InsertCharacterScreen.route) {
            // Navigate to the screen for inserting a new character
            InsertCharacterScreen(viewModel = viewModel)
        }
        composable(Screen.CustomFeatureScreen.route) {
            // Navigate to a custom feature screen (Character Selection in this case)
            CharacterSelectionScreen(viewModel)
        }
    }
}
