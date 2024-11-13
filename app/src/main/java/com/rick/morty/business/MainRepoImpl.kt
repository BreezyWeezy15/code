package com.rick.morty.business

import com.rick.morty.db.CharacterDao
import com.rick.morty.models.CharacterEntity
import com.rick.morty.models.DataModel
import com.rick.morty.services.AuthService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

// Implementation of the MainRepo interface, handling the data operations
// for characters using AuthService for network operations and CharacterDao for local database operations.
class MainRepoImpl @Inject constructor(
    // AuthService is injected for performing authentication or network-related operations.
    private val authService: AuthService,

    // CharacterDao is injected for interacting with the local database for character operations.
    private val characterDao: CharacterDao
) : MainRepo {

    // Override function to get characters from a remote source (via AuthService).
    // This function returns a Flow of DataModel containing characters fetched from the remote source.
    override suspend fun getCharacters(): Flow<DataModel> {
        return flow {
            // Emit the result of the getCharacters call from AuthService.
            emit(authService.getCharacters())
        }
    }

    // Override function to insert a new character into the local database via CharacterDao.
    // Takes a CharacterEntity object representing the character to be inserted.
    override suspend fun insertCharacter(character: CharacterEntity) {
        // Calls the insertCharacter method from CharacterDao to insert the character into the local database.
        characterDao.insertCharacter(character)
    }

    // Override function to delete a specific character from the local database via CharacterDao.
    // Takes a CharacterEntity object representing the character to be deleted.
    override suspend fun deleteCharacter(character: CharacterEntity) {
        // Calls the deleteCharacter method from CharacterDao to remove the character from the local database.
        characterDao.deleteCharacter(character)
    }

    // Override function to delete all characters from the local database via CharacterDao.
    override suspend fun deleteAllCharacters() {
        // Calls the deleteAllCharacters method from CharacterDao to remove all characters from the local database.
        characterDao.deleteAllCharacters()
    }

    // Override function to fetch all characters stored in the local database.
    // Returns a Flow of a list of CharacterEntity objects.
    override fun getAllCharactersFromDb(): Flow<List<CharacterEntity>> {
        // Calls the getAllCharacters method from CharacterDao to retrieve all characters from the local database.
        return characterDao.getAllCharacters()
    }
}
