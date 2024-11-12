package com.rick.morty.business

import com.rick.morty.db.CharacterDao
import com.rick.morty.db.CharacterEntity
import com.rick.morty.models.DataModel
import com.rick.morty.models.Info
import com.rick.morty.services.AuthService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MainRepoImpl @Inject constructor(
    private val authService: AuthService,
    private val characterDao: CharacterDao  // DAO for local database operations
) : MainRepo {

    // API-related function (fetch characters from the API)
    override suspend fun getCharacters(): Flow<DataModel> {
        return flow {
            emit(authService.getCharacters())
        }
    }

    // Database-related functions

    override suspend fun insertCharacter(character: CharacterEntity) {
        characterDao.insertCharacter(character)  // Insert a single character into the DB
    }

    override suspend fun deleteCharacter(character: CharacterEntity) {
        characterDao.deleteCharacter(character)  // Delete a specific character from the DB
    }

    override suspend fun deleteAllCharacters() {
        characterDao.deleteAllCharacters()  // Delete all characters from the DB
    }

    override fun getAllCharactersFromDb(): Flow<List<CharacterEntity>> {
        return characterDao.getAllCharacters()  // Retrieve all characters from the DB
    }
}