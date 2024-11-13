package com.rick.morty.business

import com.rick.morty.models.CharacterEntity
import com.rick.morty.models.DataModel
import kotlinx.coroutines.flow.Flow

// Interface representing the main repository for handling character data.
interface MainRepo {

    // Suspend function to fetch character data from a remote source or repository.
    // Returns a Flow that emits a DataModel containing the characters.
    suspend fun getCharacters(): Flow<DataModel>

    // Suspend function to insert a new character into the database.
    // Takes a CharacterEntity object representing the character to be inserted.
    suspend fun insertCharacter(character: CharacterEntity)

    // Suspend function to delete a specific character from the database.
    // Takes a CharacterEntity object representing the character to be deleted.
    suspend fun deleteCharacter(character: CharacterEntity)

    // Suspend function to delete all characters from the database.
    suspend fun deleteAllCharacters()

    // Function to get all characters from the local database.
    // Returns a Flow that emits a list of CharacterEntity objects representing all stored characters.
    fun getAllCharactersFromDb(): Flow<List<CharacterEntity>>
}
