package com.rick.morty.business

import com.rick.morty.db.CharacterEntity
import com.rick.morty.models.DataModel
import kotlinx.coroutines.flow.Flow

interface MainRepo {

    suspend fun getCharacters(): Flow<DataModel>
    suspend fun insertCharacter(character: CharacterEntity)
    suspend fun deleteCharacter(character: CharacterEntity)
    suspend fun deleteAllCharacters()
    fun getAllCharactersFromDb(): Flow<List<CharacterEntity>>
}