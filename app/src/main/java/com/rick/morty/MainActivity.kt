package com.rick.morty

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rick.morty.business.MainViewModel
import com.rick.morty.screens.CharacterListScreen
import com.rick.morty.screens.CharacterScreen
import com.rick.morty.screens.InsertCharacterScreen
import com.rick.morty.screens.QuizScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val viewModel: MainViewModel = hiltViewModel()

            Scaffold(
                bottomBar = {
                    BottomNavigationBar(navController)
                }
            ) { paddingValues ->
                NavHost(
                    navController = navController,
                    startDestination = Screen.CharacterListScreen.route,
                    modifier = Modifier.padding(paddingValues)
                ) {
                    composable(Screen.CharacterListScreen.route) {
                        CharacterListScreen(viewModel = viewModel)
                    }
                    composable(Screen.CharacterScreen.route) {
                        CharacterScreen(viewModel = viewModel)
                    }
                    composable(Screen.InsertCharacterScreen.route) {
                        InsertCharacterScreen(
                            viewModel = viewModel
                        )
                    }
                    composable(Screen.CustomFeatureScreen.route) {
                        QuizScreen()
                    }
                }
            }
        }
    }
}
