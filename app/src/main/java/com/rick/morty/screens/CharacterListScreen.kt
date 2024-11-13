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
import coil.compose.rememberAsyncImagePainter
import com.rick.morty.business.MainViewModel
import com.rick.morty.models.Result
import com.rick.morty.states.CharactersStates
// Composable function that represents the Character List screen, displaying different UI states based on character data.
// It observes the characterState from the ViewModel to update the UI accordingly.
@Composable
fun CharacterListScreen(viewModel: MainViewModel) {
    // Collect the character state from the ViewModel as a state to observe.
    val characterState by viewModel.characterState.collectAsState()

    // Based on the current character state, show appropriate UI components.
    when (characterState) {
        // Show loading state when characters are being fetched.
        is CharactersStates.LOADING -> LoadingState()

        // Show character list on successful data retrieval.
        is CharactersStates.SUCCESS -> {
            val characters = (characterState as CharactersStates.SUCCESS).dataModel.results
            // If no characters are available, show a "No Data" icon.
            if (characters.isEmpty()) {
                NoDataIcon(message = "No characters available.")
            } else {
                // Display the character list if characters are available.
                CharacterList(characters)
            }
        }

        // Show an error message if there is a failure in fetching characters.
        is CharactersStates.ERROR -> NoDataIcon(message = (characterState as CharactersStates.ERROR).error)

        // No state to show if the characterState is in an unhandled state.
        else -> { /* No state */ }
    }
}

// Composable function that shows a loading spinner while waiting for data.
@Composable
fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()  // Displays a circular progress indicator.
    }
}

// Composable function to display the list of characters in a LazyColumn.
@Composable
fun CharacterList(characters: List<Result>) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),  // Adds padding around the list.
        verticalArrangement = Arrangement.spacedBy(8.dp)  // Adds spacing between items.
    ) {
        // Display each character in the list.
        items(characters) { character ->
            CharacterItem(character)
        }
    }
}

// Composable function to show a "No Data" icon with a message when no characters are available.
@Composable
fun NoDataIcon(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Display a column with an icon and the provided message.
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Display an info icon.
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)  // Icon color.
            )
            Spacer(modifier = Modifier.height(8.dp))  // Add space between icon and message.
            // Display the provided message.
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)  // Text color.
            )
        }
    }
}

// Composable function to display a single character item, including its image and information.
@Composable
fun CharacterItem(character: Result) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(12.dp),  // Rounded corners for the card.
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)  // Card shadow.
    ) {
        // Row to display the image and the character info.
        Row(modifier = Modifier.padding(12.dp)) {
            CharacterImage(imageUrl = character.image)  // Display the character's image.
            Spacer(modifier = Modifier.width(12.dp))  // Add space between the image and info.
            CharacterInfoColumn(character = character)  // Display the character's information.
        }
    }
}

// Composable function to display the character image with proper sizing and shape.
@Composable
fun CharacterImage(imageUrl: String) {
    Image(
        painter = rememberAsyncImagePainter(imageUrl),  // Asynchronously load the image.
        contentDescription = null,
        modifier = Modifier
            .width(120.dp)  // Set image width.
            .fillMaxHeight()  // Fill available height.
            .clip(RoundedCornerShape(12.dp))  // Apply rounded corners to the image.
            .aspectRatio(1f)  // Keep the aspect ratio square.
    )
}

// Composable function to display the character's details in a column.
@Composable
fun CharacterInfoColumn(character: Result) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),  // Space between info rows.
        modifier = Modifier.fillMaxHeight()  // Make the column fill the available height.
    ) {
        // Display the character's name as a title.
        Text(
            text = character.name,
            style = MaterialTheme.typography.titleMedium
        )
        // Display other character details in individual rows.
        CharacterInfoRow(label = "Gender", value = character.gender)
        CharacterInfoRow(label = "Species", value = character.species)
        CharacterInfoRow(label = "Status", value = character.status)
        CharacterInfoRow(label = "Location", value = character.location.name)
        CharacterInfoRow(label = "Origin", value = character.origin.name)
    }
}

// Composable function to display a label-value pair in a row format.
@Composable
fun CharacterInfoRow(label: String, value: String) {
    Text(
        text = "$label: $value",  // Display the label and its associated value.
        style = MaterialTheme.typography.bodySmall  // Style for body text.
    )
}
