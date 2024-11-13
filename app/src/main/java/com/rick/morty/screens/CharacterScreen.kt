
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

@Composable
fun CharacterScreen(viewModel: MainViewModel) {
    val localCharacters by viewModel.localCharacters.collectAsState(emptyList())
    var showDeleteAllDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    var deletedCharacter: CharacterEntity? by remember { mutableStateOf(null) }
    val context = LocalContext.current

    LaunchedEffect(deletedCharacter) {
        deletedCharacter?.let {
            val result = snackbarHostState.showSnackbar(
                message = "Character deleted",
                actionLabel = "UNDO"
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.insertCharacter(it)
            }
            deletedCharacter = null
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(paddingValues)
            ) {
                // Pass only the callback to trigger the delete all dialog
                HeaderSection(
                    localCharacters = localCharacters,
                    onDeleteAllClick = { showDeleteAllDialog = true },
                    context = context
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (localCharacters.isEmpty()) {
                    EmptyStateView()
                } else {
                    CharacterList(localCharacters, onDeleteCharacter = { character ->
                        deletedCharacter = character
                        viewModel.deleteCharacter(character)
                    })
                }

                // Delete All Dialog managed by CharacterScreen
                DeleteAllDialog(
                    showDialog = showDeleteAllDialog,
                    onDismiss = { showDeleteAllDialog = false },
                    onDeleteAll = {
                        viewModel.deleteAllCharacters()
                        showDeleteAllDialog = false
                    }
                )
            }
        }
    )
}

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
                if (localCharacters.isEmpty()) {
                    Toast.makeText(context, "There are no characters to remove", Toast.LENGTH_SHORT).show()
                } else {
                    onDeleteAllClick() // Triggers the delete all dialog
                }
            },
            modifier = Modifier
                .background(Color.Gray, CircleShape)
                .size(40.dp)
                .padding(8.dp)
        ) {
            Icon(Icons.Default.Delete, contentDescription = "Delete All", tint = Color.White, modifier = Modifier.size(25.dp))
        }
    }
}

@Composable
fun EmptyStateView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.no_data),
            contentDescription = "No Data",
            modifier = Modifier.size(140.dp)
        )
    }
}

@Composable
fun CharacterList(
    localCharacters: List<CharacterEntity>,
    onDeleteCharacter: (CharacterEntity) -> Unit
) {
    LazyColumn {
        items(localCharacters, key = { it.characterId!! }) { character ->
            SwipeToDismissCard(character, onDelete = { onDeleteCharacter(character) })
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeToDismissCard(character: CharacterEntity, onDelete: () -> Unit) {
    val dismissState = rememberDismissState(
        confirmStateChange = { dismissValue ->
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {}
        },
        dismissContent = {
            CharacterRow(character = character)
        }
    )
}

@Composable
fun CharacterRow(character: CharacterEntity) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
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

            Column(modifier = Modifier.weight(1f)) {
                Text(text = character.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                Text(text = "Species: ${character.species}")
                Text(text = "Gender: ${character.gender}")
                Text(text = "Status: ${character.status}")
            }
        }
    }
}

@Composable
fun DeleteAllDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onDeleteAll: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Confirm Delete") },
            text = { Text("Are you sure you want to delete all characters? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = onDeleteAll) {
                    Text("Yes", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("No")
                }
            }
        )
    }
}
