
package com.rick.morty.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.rick.morty.R
import com.rick.morty.business.MainViewModel
import com.rick.morty.models.CharacterEntity

// Main composable function for displaying the character screen, managing character data
// and interacting with the UI to delete characters or show the delete all dialog.
@Composable
fun CharacterScreen(viewModel: MainViewModel) {
    // Collect the list of local characters from the ViewModel.
    val localCharacters by viewModel.localCharacters.collectAsState(emptyList())
    var showDeleteAllDialog by remember { mutableStateOf(false) }  // State for controlling the delete all dialog visibility.
    val snackbarHostState = remember { SnackbarHostState() }  // State for displaying snackbars.
    var deletedCharacter: CharacterEntity? by remember { mutableStateOf(null) }  // Holds the deleted character for undoing.
    val context = LocalContext.current  // Get the current context for Toasts.

    // Handle the snackbar action when a character is deleted.
    LaunchedEffect(deletedCharacter) {
        deletedCharacter?.let {
            // Show a snackbar with an "UNDO" action to restore the deleted character.
            val result = snackbarHostState.showSnackbar(
                message = "Character deleted",
                actionLabel = "UNDO"
            )
            if (result == SnackbarResult.ActionPerformed) {
                // If the "UNDO" action is performed, insert the deleted character back into the database.
                viewModel.insertCharacter(it)
            }
            deletedCharacter = null  // Reset deleted character state after the snackbar is shown.
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },  // Pass the snackbarHost to display the snackbar.
        content = { paddingValues ->
            // Main content layout for the screen.
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(paddingValues)
            ) {
                // Header section that includes the title and delete all button.
                HeaderSection(
                    localCharacters = localCharacters,
                    onDeleteAllClick = { showDeleteAllDialog = true },  // Trigger the delete all dialog when clicked.
                    context = context
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Show an empty state view if there are no characters, otherwise display the list of characters.
                if (localCharacters.isEmpty()) {
                    EmptyStateView()
                } else {
                    // Display the list of characters with swipe-to-delete functionality.
                    CharacterList(localCharacters, onDeleteCharacter = { character ->
                        deletedCharacter = character  // Set the character to be deleted.
                        viewModel.deleteCharacter(character)  // Delete the character from the database.
                    })
                }

                // Dialog to confirm the deletion of all characters.
                DeleteAllDialog(
                    showDialog = showDeleteAllDialog,
                    onDismiss = { showDeleteAllDialog = false },
                    onDeleteAll = {
                        viewModel.deleteAllCharacters()  // Delete all characters.
                        showDeleteAllDialog = false  // Close the dialog.
                    }
                )
            }
        }
    )
}

// Header section of the screen with the title "My Characters" and the delete all button.
@Composable
fun HeaderSection(
    localCharacters: List<CharacterEntity>,
    onDeleteAllClick: () -> Unit,
    context: android.content.Context
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "My Characters",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f)
        )
        IconButton(
            onClick = {
                // Show a toast if there are no characters to delete.
                if (localCharacters.isEmpty()) {
                    Toast.makeText(context, "There are no characters to remove", Toast.LENGTH_SHORT).show()
                } else {
                    onDeleteAllClick()  // Trigger the delete all dialog.
                }
            },
            modifier = Modifier
                .background(Color.Gray, CircleShape)
                .size(40.dp)
                .padding(8.dp)
        ) {
            // Delete icon for triggering the delete all dialog.
            Icon(Icons.Default.Delete, contentDescription = "Delete All", tint = Color.White, modifier = Modifier.size(25.dp))
        }
    }
}

// Composable function to show the empty state when there are no characters.
@Composable
fun EmptyStateView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Display an image as a placeholder for no data available.
        Image(
            painter = painterResource(id = R.drawable.no_data),
            contentDescription = "No Data",
            modifier = Modifier.size(140.dp)
        )
    }
}

// Composable function to display a list of local characters with swipe-to-dismiss functionality.
@Composable
fun CharacterList(
    localCharacters: List<CharacterEntity>,
    onDeleteCharacter: (CharacterEntity) -> Unit  // Callback when a character is deleted.
) {
    LazyColumn {
        // Display each character with swipe-to-dismiss functionality.
        items(localCharacters, key = { it.characterId!! }) { character ->
            SwipeToDismissCard(character, onDelete = { onDeleteCharacter(character) })
        }
    }
}

// Composable function to handle swipe-to-dismiss behavior for deleting a character.
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeToDismissCard(character: CharacterEntity, onDelete: () -> Unit) {
    val dismissState = rememberDismissState(
        confirmStateChange = { dismissValue ->
            // Trigger deletion when the card is swiped.
            if (dismissValue == DismissValue.DismissedToStart || dismissValue == DismissValue.DismissedToEnd) {
                onDelete()  // Delete the character.
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
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                // Custom background can be added here if necessary.
            }
        },
        dismissContent = {
            // Display the character's details inside the swipeable card.
            CharacterRow(character = character)
        }
    )
}

// Composable function to display a character's details inside a card.
@Composable
fun CharacterRow(character: CharacterEntity) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            // Display the character's image with a circular shape.
            Image(
                painter = rememberAsyncImagePainter(character.image),
                contentDescription = "Character Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            )
            Spacer(modifier = Modifier.width(16.dp))

            // Display the character's details in a column.
            Column(modifier = Modifier.weight(1f)) {
                Text(text = character.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                Text(text = "Species: ${character.species}")
                Text(text = "Gender: ${character.gender}")
                Text(text = "Status: ${character.status}")
            }
        }
    }
}

// Composable function for displaying the "Delete All" confirmation dialog.
@Composable
fun DeleteAllDialog(
    showDialog: Boolean,  // Controls the visibility of the dialog.
    onDismiss: () -> Unit,  // Dismiss the dialog.
    onDeleteAll: () -> Unit  // Action to delete all characters.
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Confirm Delete") },
            text = { Text("Are you sure you want to delete all characters? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = onDeleteAll) {
                    Text("Yes", color = MaterialTheme.colorScheme.error)  // Red color for the "Yes" button.
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("No")  // Dismiss button to cancel the action.
                }
            }
        )
    }
}
