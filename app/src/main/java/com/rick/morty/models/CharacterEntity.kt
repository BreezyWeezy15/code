package com.rick.morty.models

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
    val image: ByteArray
) {
    @PrimaryKey(autoGenerate = true)
    var characterId: Long? = null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CharacterEntity

        if (name != other.name) return false
        if (species != other.species) return false
        if (gender != other.gender) return false
        if (status != other.status) return false
        if (type != other.type) return false
        if (created != other.created) return false
        if (!image.contentEquals(other.image)) return false
        if (characterId != other.characterId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + species.hashCode()
        result = 31 * result + gender.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + created.hashCode()
        result = 31 * result + image.contentHashCode()
        result = 31 * result + (characterId?.hashCode() ?: 0)
        return result
    }
}
