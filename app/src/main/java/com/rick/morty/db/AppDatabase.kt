package com.rick.morty.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rick.morty.models.CharacterEntity

// The Room Database class responsible for handling database operations
// It defines the entities, version, and other database configurations.
@Database(entities = [CharacterEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class) // Specifies custom type converters for Room to handle complex data types
abstract class AppDatabase : RoomDatabase() {

    // Abstract function to access the DAO (Data Access Object) for CharacterEntity
    // This function allows us to perform operations like querying, inserting, and updating CharacterEntity.
    abstract fun characterDao(): CharacterDao
}
