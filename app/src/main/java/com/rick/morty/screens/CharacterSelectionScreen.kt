package com.rick.morty.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.rick.morty.business.MainViewModel
import com.rick.morty.models.*
import com.rick.morty.states.CharactersStates

@Composable
fun CharacterSelectionScreen(mainViewModel: MainViewModel) {
    // State to track selected characters and visibility of the bottom sheet
    var selectedCharacters by remember { mutableStateOf<List<Result>>(emptyList()) }
    var isBottomSheetVisible by remember { mutableStateOf(false) }

    // Observing the state of character data from ViewModel
    val characterState = mainViewModel.characterState.collectAsState()

    // Handle different states of character data (loading, success, error, initial)
    when (characterState.value) {
        is CharactersStates.LOADING -> {
            // Show loading indicator while data is being fetched
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator() // Circular progress indicator to indicate loading
            }
        }

        is CharactersStates.SUCCESS -> {
            // When characters data is successfully fetched
            val characters = (characterState.value as CharactersStates.SUCCESS).dataModel.results

            // If no characters are available, show a no-data message
            if (characters.isEmpty()) {
                NoDataIcon(message = "No characters available.")
            } else {
                // Show the character grid for selection
                CharacterGrid(
                    characters = characters,
                    selectedCharacters = selectedCharacters,
                    onCharacterSelect = { character ->
                        // Handle character selection (up to 2 characters)
                        if (!selectedCharacters.contains(character) && selectedCharacters.size < 2) {
                            selectedCharacters = selectedCharacters + character
                        }
                        // When 2 characters are selected, show the bottom sheet
                        if (selectedCharacters.size == 2) {
                            isBottomSheetVisible = true
                        }
                    }
                )
            }
        }

        is CharactersStates.ERROR -> {
            // Show error message if there was an issue fetching data
            NoDataIcon(message = (characterState.value as CharactersStates.ERROR).error)
        }

        // Placeholder for the initial state (no data)
        CharactersStates.INITIAL -> Unit
    }

    // Show Bottom Sheet when two characters are selected
    if (isBottomSheetVisible) {
        BottomSheetDialog(selectedCharacters) {
            // Reset selected characters and hide the bottom sheet
            selectedCharacters = emptyList()
            isBottomSheetVisible = false
        }
    }
}

@Composable
fun CharacterGrid(
    characters: List<Result>,
    selectedCharacters: List<Result>,
    onCharacterSelect: (Result) -> Unit
) {
    // A grid to display the list of characters
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // 2 columns in the grid
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentPadding = PaddingValues(8.dp), // Padding inside the grid
        verticalArrangement = Arrangement.spacedBy(8.dp), // Space between rows
        horizontalArrangement = Arrangement.spacedBy(8.dp) // Space between columns
    ) {
        // Loop through the characters and display each one
        items(characters) { character ->
            // Check if the character is selected
            val isSelected = selectedCharacters.contains(character)
            CharacterItem(
                character = character,
                isSelected = isSelected,
                onSelect = { onCharacterSelect(character) }
            )
        }
    }
}

@Composable
fun CharacterItem(character: Result, isSelected: Boolean, onSelect: () -> Unit) {
    // A card that represents each character item in the grid
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onSelect() }, // On click, select the character
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 8.dp else 4.dp),
        colors = if (isSelected) CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
        else CardDefaults.cardColors() // Change appearance if selected
    ) {
        // Column layout to display character image and name
        Column(
            modifier = Modifier
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val painter: Painter = rememberImagePainter(character.image) // Load character image
            Image(
                painter = painter,
                contentDescription = character.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)) // Clip the image with rounded corners
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = character.name, // Display character name
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis // Ellipsis if name is too long
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetDialog(selectedCharacters: List<Result>, onDismiss: () -> Unit) {
    if (selectedCharacters.size == 2) {
        // Show bottom sheet with details of the two selected characters
        ModalBottomSheet(
            onDismissRequest = onDismiss,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Display character details in columns
                CharacterDetailsColumn(character = selectedCharacters[1], modifier = Modifier.weight(1f))
                Text(
                    text = "VS", // Separator between the two characters
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                CharacterDetailsColumn(character = selectedCharacters[0], modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun CharacterDetailsColumn(character: Result, modifier: Modifier) {
    // A column displaying detailed information about a character
    Card(
        modifier = modifier
            .padding(4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            val painter: Painter = rememberImagePainter(character.image) // Load character image
            Box(
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Image(
                    painter = painter,
                    contentDescription = character.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize() // Fill the box with the image
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = character.name, // Display character name
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 4.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis // Ellipsis for long names
            )
            Divider() // Divider between character name and details
            // Display various character attributes
            CharacterInfoText(label = "Species", value = character.species)
            CharacterInfoText(label = "Gender", value = character.gender)
            CharacterInfoText(label = "Status", value = character.status)
            CharacterInfoText(label = "Origin", value = character.origin.name)
            CharacterInfoText(label = "Location", value = character.location.name)
        }
    }
}

@Composable
fun CharacterInfoText(label: String, value: String) {
    // Display a label-value pair for character details (e.g., species, gender)
    Text(
        text = "$label: $value",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(vertical = 2.dp),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis // Ellipsis for long values
    )
}
