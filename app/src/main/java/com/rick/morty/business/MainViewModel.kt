package com.rick.morty.business

import androidx.lifecycle.ViewModel
import com.rick.morty.states.CharactersStates
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


import androidx.lifecycle.viewModelScope
import com.rick.morty.models.CharacterEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


// ViewModel class for managing UI-related data for characters, interacting with the MainRepoImpl
// to perform operations like fetching characters, inserting, deleting, and updating the UI state.
@HiltViewModel
class MainViewModel @Inject constructor(
    // MainRepoImpl is injected to handle the character data operations.
    private val mainRepoImpl: MainRepoImpl
) : ViewModel() {

    // MutableStateFlow to hold the current state of character data, including loading, success, or error states.
    private val _characterState = MutableStateFlow<CharactersStates>(CharactersStates.INITIAL)

    // StateFlow to expose the character state to the UI in an immutable form.
    val characterState: StateFlow<CharactersStates> = _characterState

    // MutableStateFlow to hold the list of local characters retrieved from the database.
    private val _localCharacters = MutableStateFlow<List<CharacterEntity>>(emptyList())

    // StateFlow to expose the local characters list to the UI in an immutable form.
    val localCharacters: StateFlow<List<CharacterEntity>> = _localCharacters

    // Initializing the ViewModel by fetching characters from the remote source and local database.
    init {
        getCharacters()
        getLocalCharacters()
    }

    // Private function to fetch characters from the remote service.
    private fun getCharacters() {
        // Update the character state to loading before starting the operation.
        _characterState.value = CharactersStates.LOADING

        // Launching a coroutine to fetch characters asynchronously.
        viewModelScope.launch {
            try {
                // Collecting the result from the repository's getCharacters flow.
                mainRepoImpl.getCharacters()
                    .collectLatest { result ->
                        // On success, update the state with the fetched result.
                        _characterState.value = CharactersStates.SUCCESS(result)
                    }
            } catch (e: Exception) {
                // In case of an error, update the state to reflect the failure.
                _characterState.value = CharactersStates.ERROR("Failed to load characters")
            }
        }
    }

    // Private function to fetch characters stored locally in the database.
    private fun getLocalCharacters() {
        // Launching a coroutine to collect characters from the local database.
        viewModelScope.launch {
            mainRepoImpl.getAllCharactersFromDb().collectLatest { characters ->
                // Update the local characters list when new data is collected.
                _localCharacters.value = characters
            }
        }
    }

    // Function to delete a character from the database.
    fun deleteCharacter(character: CharacterEntity) {
        // Launching a coroutine to perform the delete operation asynchronously.
        viewModelScope.launch {
            mainRepoImpl.deleteCharacter(character)
        }
    }

    // Function to insert a new character into the database.
    fun insertCharacter(character: CharacterEntity) {
        // Launching a coroutine to perform the insert operation asynchronously.
        viewModelScope.launch {
            mainRepoImpl.insertCharacter(character)
        }
    }

    // Function to delete all characters from the database.
    fun deleteAllCharacters() {
        // Launching a coroutine to perform the delete all operation asynchronously.
        viewModelScope.launch {
            mainRepoImpl.deleteAllCharacters()
        }
    }
}
