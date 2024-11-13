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
    private val characterDao: CharacterDao
) : MainRepo {

    override suspend fun getCharacters(): Flow<DataModel> {
        return flow {
            emit(authService.getCharacters())
        }
    }

    override suspend fun insertCharacter(character: CharacterEntity) {
        characterDao.insertCharacter(character)
    }

    override suspend fun deleteCharacter(character: CharacterEntity) {
        characterDao.deleteCharacter(character)
    }

    override suspend fun deleteAllCharacters() {
        characterDao.deleteAllCharacters()
    }

    override fun getAllCharactersFromDb(): Flow<List<CharacterEntity>> {
        return characterDao.getAllCharacters()
    }
}
