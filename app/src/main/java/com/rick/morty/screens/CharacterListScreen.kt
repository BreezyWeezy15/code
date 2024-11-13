package com.rick.morty.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is CharactersStates.SUCCESS -> {
            val characters = (characterState as CharactersStates.SUCCESS).dataModel.results
            if (characters.isEmpty()) {
                NoDataIcon(message = "No characters available.")
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        top = 16.dp,
                        end = 16.dp,
                        bottom = 80.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(characters) { character ->
                        CharacterItem(character)
                    }
                }
            }
        }
        is CharactersStates.ERROR -> {
            NoDataIcon(message = (characterState as CharactersStates.ERROR).error)
        }
        else -> { /* No state */ }
    }
}

@Composable
fun NoDataIcon(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun CharacterItem(character: Result) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            // Image with dynamic height to fit based on content
            Image(
                painter = rememberImagePainter(character.image),
                contentDescription = null,
                modifier = Modifier
                    .width(120.dp) // Fixed width
                    .fillMaxHeight() // Make sure it fits vertically within the row
                    .clip(RoundedCornerShape(12.dp))
                    .aspectRatio(1f) // Keep aspect ratio consistent
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Gender: ${character.gender}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Species: ${character.species}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Status: ${character.status}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Location: ${character.location.name}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Origin: ${character.origin.name}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

