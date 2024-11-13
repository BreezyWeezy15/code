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
    var selectedCharacters by remember { mutableStateOf<List<Result>>(emptyList()) }
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    val characterState = mainViewModel.characterState.collectAsState()

    when (characterState.value) {
        is CharactersStates.LOADING -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is CharactersStates.SUCCESS -> {
            val characters = (characterState.value as CharactersStates.SUCCESS).dataModel.results
            if (characters.isEmpty()) {
                NoDataIcon(message = "No characters available.")
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(characters) { character ->
                        val isSelected = selectedCharacters.contains(character)
                        CharacterItem(
                            character = character,
                            isSelected = isSelected,
                            onSelect = {
                                if (!isSelected && selectedCharacters.size < 2) {
                                    selectedCharacters = selectedCharacters + character
                                }
                                if (selectedCharacters.size == 2) {
                                    isBottomSheetVisible = true
                                }
                            }
                        )
                    }
                }
            }
        }
        is CharactersStates.ERROR -> {
            NoDataIcon(message = (characterState.value as CharactersStates.ERROR).error)
        }

        CharactersStates.INITIAL -> TODO()
    }

    if (isBottomSheetVisible) {
        BottomSheetDialog(selectedCharacters) {
            selectedCharacters = emptyList()
            isBottomSheetVisible = false
        }
    }
}

@Composable
fun CharacterItem(character: Result, isSelected: Boolean, onSelect: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onSelect() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 8.dp else 4.dp),
        colors = if (isSelected) CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
        else CardDefaults.cardColors()
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val painter: Painter = rememberImagePainter(character.image)
            Image(
                painter = painter,
                contentDescription = character.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = character.name,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetDialog(selectedCharacters: List<Result>, onDismiss: () -> Unit) {
    if (selectedCharacters.size == 2) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp), // Reduce padding for equal width
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CharacterDetailsColumn(character = selectedCharacters[1], modifier = Modifier.weight(1f))
                Text(
                    text = "VS",
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
            val painter: Painter = rememberImagePainter(character.image)
            Box(
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)) // Apply the clip to the Box container
            ) {
                Image(
                    painter = painter,
                    contentDescription = character.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize() // Fill the Box, ensuring clipping happens
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = character.name,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 4.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Divider()
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
    Text(
        text = "$label: $value",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(vertical = 2.dp),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}
