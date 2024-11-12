package com.rick.morty.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class CharacterEntity(
    val name: String,
    val species: String,
    val gender: String,
    val status: String,
    val type: String,
    val created: String,
    val image: String
){

    @PrimaryKey(autoGenerate = true)
    var characterId : Long? = null
}