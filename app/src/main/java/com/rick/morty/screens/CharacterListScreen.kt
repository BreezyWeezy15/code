package com.rick.morty.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.rick.morty.business.MainViewModel
import com.rick.morty.models.Result
import com.rick.morty.states.CharactersStates

@Composable
fun CharacterListScreen(viewModel: MainViewModel) {
    val characterState by viewModel.characterState.collectAsState()

    when (characterState) {

        is CharactersStates.LOADING -> {
            CircularProgressIndicator()
        }
        is CharactersStates.SUCCESS -> {

            LazyColumn {
                items((characterState as CharactersStates.SUCCESS).dataModel.results) { character ->
                    CharacterItem(character)
                }
            }
        }
        is CharactersStates.ERROR -> {
            val message = (characterState as CharactersStates.ERROR)
            Text("Error: ${message.error}")
        }
        else -> {

        }
    }
}

@Composable
fun CharacterItem(character: Result) {
    Row(modifier = Modifier.padding(8.dp)) {

        Image(
            painter = rememberImagePainter(character.image),
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = character.name, style = MaterialTheme.typography.bodyMedium)
            Text(text = "Species: ${character.species}")
            Text(text = "Status: ${character.status}")
        }
    }
}