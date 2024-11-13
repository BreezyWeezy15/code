package com.rick.morty.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.rick.morty.R
import com.rick.morty.business.MainViewModel
import com.rick.morty.db.CharacterEntity

@Composable
fun CharacterScreen(viewModel: MainViewModel) {
    val localCharacters by viewModel.localCharacters.collectAsState(emptyList()) // Default to empty list

    // Confirmation dialog state
    var showDeleteAllDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    var deletedCharacter: CharacterEntity? by remember { mutableStateOf(null) }

    // Toast state
    val context = LocalContext.current

    // Handle undo snack action
    LaunchedEffect(deletedCharacter) {
        deletedCharacter?.let {
            val result = snackbarHostState.showSnackbar(
                message = "Character deleted",
                actionLabel = "UNDO"
            )
            if (result == SnackbarResult.ActionPerformed) {
                // If undo was clicked, restore the deleted character
                viewModel.insertCharacter(it)
            }
            // Reset deleted character after snackbar action
            deletedCharacter = null
        }
    }

    // Wrap your content inside Scaffold
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { paddingValues ->  // Add content block and pass paddingValues to handle padding
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(paddingValues) // Ensure correct padding is applied
            ) {
                // Row with "My Characters" text and "Delete All" icon button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "My Characters",
                        style = MaterialTheme.typography.titleLarge, // Made the title slightly bigger
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = {
                            if (localCharacters.isEmpty()) {
                                // Show toast if there are no characters
                                Toast.makeText(context, "There are no characters to remove", Toast.LENGTH_SHORT).show()
                            } else {
                                showDeleteAllDialog = true
                            }
                        },
                        modifier = Modifier
                            .background(Color.Gray, CircleShape)
                            .size(40.dp)
                            .padding(8.dp) // Added padding for circular effect
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete All", tint = Color.White,
                            modifier = Modifier.size(25.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp)) // Spacer between title and the list

                // Check if the list is empty
                if (localCharacters.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.no_data), // Drawable image for no data
                            contentDescription = "No Data",
                            modifier = Modifier.size(140.dp) // Adjust size of the image to make it smaller
                        )
                    }
                } else {
                    // Show local database characters in a LazyColumn
                    LazyColumn {
                        items(localCharacters, key = { it.characterId!! }) { character ->
                            SwipeToDismissCard(character, onDelete = {
                                deletedCharacter = character
                                viewModel.deleteCharacter(character)
                            })
                        }
                    }
                }

                // Dialog for confirmation to delete all characters
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
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeToDismissCard(character: CharacterEntity, onDelete: () -> Unit) {
    val dismissState = rememberDismissState(
        confirmStateChange = { dismissValue ->
            // Handle swipe action to delete
            if (dismissValue == DismissValue.DismissedToStart || dismissValue == DismissValue.DismissedToEnd) {
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
            // Box remains empty or can be customized further if needed
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                // No Icon or additional elements
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
                    // Image
                    Image(
                        painter = rememberImagePainter(character.image),
                        contentDescription = "Character Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                    )
                    Spacer(modifier = Modifier.width(16.dp))

                    // Character details
                    Column(
                        modifier = Modifier.weight(1f) // Ensure the text takes available space
                    ) {
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



