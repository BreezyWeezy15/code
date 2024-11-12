package com.rick.morty.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.rick.morty.business.MainViewModel
import com.rick.morty.db.CharacterEntity
import com.rick.morty.states.CharactersStates

@Composable
fun CharacterScreen(viewModel: MainViewModel) {
    val localCharacters by viewModel.localCharacters.collectAsState()

    // Confirmation dialog state
    var showDeleteAllDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Show local database characters
        LazyColumn {
            items(localCharacters, key = { it.characterId!! }) { character ->
                SwipeToDismissCard(character, onDelete = {
                    viewModel.deleteCharacter(character)
                })
            }
        }

        // Button to delete all characters with a confirmation dialog
        Button(
            onClick = { showDeleteAllDialog = true },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Delete All Characters")
        }

        // Dialog for confirmation
        if (showDeleteAllDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteAllDialog = false },
                title = { Text("Confirm Delete") },
                text = { Text("Are you sure you want to delete all characters? This action cannot be undone.") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.deleteAllCharacters()
                        showDeleteAllDialog = false
                    }) {
                        Text("Yes", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteAllDialog = false }) {
                        Text("No")
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeToDismissCard(character: CharacterEntity, onDelete: () -> Unit) {
    val dismissState = rememberDismissState(
        confirmStateChange = { dismissValue ->
            if (dismissValue == DismissValue.DismissedToStart) {
                onDelete()
                true
            } else {
                false
            }
        }
    )

    SwipeToDismiss(
        state = dismissState,
        background = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
            }
        },
        dismissContent = {
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Image(
                        painter = rememberImagePainter(character.image),
                        contentDescription = "Character Image",
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = character.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                        Text(text = "Species: ${character.species}")
                        Text(text = "Gender: ${character.gender}")
                        Text(text = "Status: ${character.status}")
                    }
                }
            }
        }
    )
}

