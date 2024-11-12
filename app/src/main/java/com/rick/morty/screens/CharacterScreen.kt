package com.rick.morty.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.rick.morty.business.MainViewModel
import com.rick.morty.db.CharacterEntity
import com.rick.morty.states.CharactersStates

@Composable
fun CharacterScreen(viewModel: MainViewModel) {
//    val characterState by viewModel.characterState.collectAsState()
//    val localCharacters by viewModel.localCharacters.collectAsState()
//
//    Column(modifier = Modifier.fillMaxSize()) {
//        when (characterState) {
//            is CharactersStates.LOADING -> {
//                CircularProgressIndicator()
//            }
//            is CharactersStates.SUCCESS -> {
//                val dataModel = (characterState as CharactersStates.SUCCESS).data
//                LazyColumn {
//                    items(dataModel.results) { character ->
//                        Text(character.name)
//                    }
//                }
//            }
//            else -> {
//                Text("Error loading characters")
//            }
//        }
//
//        // Show local database characters
//        LazyColumn {
//            items(localCharacters) { character ->
//                Row(modifier = Modifier.fillMaxWidth()) {
//                    Text(text = character.name, modifier = Modifier.weight(1f))
//                    IconButton(onClick = {
//                        viewModel.deleteCharacter(character)
//                    }) {
//                        Icon(Icons.Default.Delete, contentDescription = "Delete")
//                    }
//                }
//            }
//        }
//
//        // Button to delete all characters from the database
//        Button(onClick = {
//            viewModel.deleteAllCharacters()  // Delete all characters
//        }) {
//            Text("Delete All Characters")
//        }
//
//        // Add button to insert a new character (for testing purposes)
//        Button(onClick = {
//            val newCharacter = CharacterEntity(
//                id = 999, name = "Test Character", species = "Human", gender = "Male",
//                status = "Alive", type = "Normal", created = "2024-11-12", image = "test_image_url"
//            )
//            viewModel.insertCharacter(newCharacter)
//        }) {
//            Text("Add Character")
//        }
//    }
}
