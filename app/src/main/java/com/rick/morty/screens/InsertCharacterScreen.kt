package com.rick.morty.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rick.morty.business.MainViewModel
import com.rick.morty.db.CharacterEntity

@Composable
fun InsertCharacterScreen(viewModel: MainViewModel, onCharacterInserted: () -> Unit) {
    val context = LocalContext.current
    var characterName by remember { mutableStateOf("") }
    var species by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    var characterType by remember { mutableStateOf("") }

    // Create a new character entity to store the data
    val newCharacter = CharacterEntity(
        name = characterName,
        species = species,
        gender = gender,
        status = status,
        type = characterType,
        created = "2024-11-12", // Placeholder date
        image = "" // For simplicity, leave this blank for now
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .clip(RoundedCornerShape(12.dp))
            .padding(16.dp), // Inner padding for content
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title
        Text(
            text = "Create a New Character",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Character Name
        OutlinedTextField(
            value = characterName,
            onValueChange = { characterName = it },
            label = { Text("Character Name") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp) // Rounded corners for edit text
        )

        // Species
        OutlinedTextField(
            value = species,
            onValueChange = { species = it },
            label = { Text("Species") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp) // Rounded corners for edit text
        )

        // Gender
        OutlinedTextField(
            value = gender,
            onValueChange = { gender = it },
            label = { Text("Gender") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp) // Rounded corners for edit text
        )

        // Status
        OutlinedTextField(
            value = status,
            onValueChange = { status = it },
            label = { Text("Status") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp) // Rounded corners for edit text
        )

        // Type
        OutlinedTextField(
            value = characterType,
            onValueChange = { characterType = it },
            label = { Text("Character Type") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp) // Rounded corners for edit text
        )

        // Save button
        Button(
            onClick = {
                if (characterName.isNotBlank() && species.isNotBlank() && gender.isNotBlank() && status.isNotBlank() && characterType.isNotBlank()) {
                    viewModel.insertCharacter(newCharacter) // Save the character to DB
                    onCharacterInserted() // Callback to navigate back or show a success message
                } else {
                    Toast.makeText(context, "All fields must be filled", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp) // Increased height for better appearance
                .clip(RoundedCornerShape(12.dp)) // Rounded corners
        ) {
            Text(
                text = "Save Character",
                color = Color.White, // White text
                style = MaterialTheme.typography.button
            )
        }
    }
}

