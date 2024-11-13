package com.rick.morty.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rick.morty.models.CharacterEntity
import kotlinx.coroutines.flow.Flow

// The Data Access Object (DAO) for handling operations on the CharacterEntity table in the database.
// This interface defines the database queries and operations that can be performed on the 'characters' table.
@Dao
interface CharacterDao {

    // Inserts a new character into the 'characters' table.
    // If a character already exists (based on primary key), it replaces the existing one.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: CharacterEntity)

    // Fetches all characters from the 'characters' table as a Flow,
    // allowing for asynchronous updates to the UI when the data changes.
    @Query("SELECT * FROM characters")
    fun getAllCharacters(): Flow<List<CharacterEntity>>

    // Deletes a specific character from the 'characters' table.
    @Delete
    suspend fun deleteCharacter(character: CharacterEntity)

    // Deletes all characters from the 'characters' table.
    @Query("DELETE FROM characters")
    suspend fun deleteAllCharacters()
}
