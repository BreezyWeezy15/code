package com.rick.morty.screens

import android.content.Context
import android.net.Uri
import android.widget.Toast
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.rick.morty.business.MainViewModel
import com.rick.morty.models.CharacterEntity

@Composable
fun InsertCharacterScreen(viewModel: MainViewModel) {
    // Local context to access application resources
    val context = LocalContext.current

    // State variables to hold character details
    var characterName by remember { mutableStateOf("") }
    var species by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    var characterType by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageByteArray by remember { mutableStateOf<ByteArray?>(null) }

    // Launcher for image picker
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageUri = it
            val inputStream = context.contentResolver.openInputStream(it)
            imageByteArray = inputStream?.readBytes()
            inputStream?.close()
        }
    }

    // Scrollable column for the form
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState), // Allow vertical scrolling
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title at the top of the screen
        TitleText()

        // Image selector for profile picture
        ProfileImageSelector(imageUri, launcher)

        // Input fields for character details
        InputField(label = "Character Name", value = characterName, onValueChange = { characterName = it })
        InputField(label = "Species", value = species, onValueChange = { species = it })
        InputField(label = "Gender", value = gender, onValueChange = { gender = it })
        InputField(label = "Status", value = status, onValueChange = { status = it })
        InputField(label = "Character Type", value = characterType, onValueChange = { characterType = it })

        // Save button for character details
        SaveButton(
            characterName,
            species,
            gender,
            status,
            characterType,
            imageByteArray,
            context,
            viewModel,
            onSuccess = {
                // Reset fields after successful save
                characterName = ""
                species = ""
                gender = ""
                status = ""
                characterType = ""
                imageUri = null
                imageByteArray = null
            }
        )
    }
}

@Composable
fun TitleText() {
    Text(
        text = "Create New Character",
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        modifier = Modifier.padding(bottom = 16.dp) // Adds padding below the title
    )
}

@Composable
fun ProfileImageSelector(imageUri: Uri?, launcher: ManagedActivityResultLauncher<String, Uri?>) {
    // Box to handle image picker and display selected image
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
            .background(Color.Gray) // Placeholder background color
            .clickable { launcher.launch("image/*") }, // Trigger image picker
        contentAlignment = Alignment.Center
    ) {
        // If an image URI is selected, show it, otherwise show placeholder text
        if (imageUri != null) {
            Image(
                painter = rememberAsyncImagePainter(imageUri),
                contentDescription = "Selected Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
                    .size(64.dp)
                    .aspectRatio(1f)
                    .clip(CircleShape)
            )
        } else {
            Text(text = "Pick Image", color = Color.White) // Display text when no image is selected
        }
    }
}

@Composable
fun InputField(label: String, value: String, onValueChange: (String) -> Unit) {
    // Text field for character information input
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) }, // Label for the text field
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp) // Rounded corners for text field
    )
}

@Composable
fun SaveButton(
    characterName: String,
    species: String,
    gender: String,
    status: String,
    characterType: String,
    imageByteArray: ByteArray?,
    context: Context,
    viewModel: MainViewModel,
    onSuccess: () -> Unit
) {
    // Button to save character details
    Button(
        onClick = {
            // Check if all fields are filled before saving
            if (characterName.isNotBlank() && species.isNotBlank() && gender.isNotBlank() && status.isNotBlank() && characterType.isNotBlank() && imageByteArray != null) {

                // Get the current date and time, then format it as a string
                val currentDate = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss") // You can adjust the pattern as needed
                val formattedDate = currentDate.format(formatter)

                // Create a new character entity with the formatted date
                val newCharacter = CharacterEntity(
                    name = characterName,
                    species = species,
                    gender = gender,
                    status = status,
                    type = characterType,
                    created = formattedDate, // Use the formatted current date and time
                    image = imageByteArray // Save the image as byte array
                )

                // Insert the character into the viewModel
                viewModel.insertCharacter(newCharacter)
                // Reset fields after successful save
                onSuccess()

                // Show a toast notification after saving
                Toast.makeText(context, "Character inserted", Toast.LENGTH_SHORT).show()
            } else {
                // Show a toast if any field is empty
                Toast.makeText(context, "All fields and an image must be filled", Toast.LENGTH_SHORT).show()
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
            .clip(RoundedCornerShape(12.dp)) // Rounded corners for button
    ) {
        Text(
            text = "Save Character",
            color = Color.White, // Text color
            style = MaterialTheme.typography.button // Button text style
        )
    }
}
