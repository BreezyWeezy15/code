package com.rick.morty.states

import com.rick.morty.models.DataModel

// Sealed class to represent different states of the character data
sealed class CharactersStates {

    // Represents the initial state, typically before any data is loaded
    data object INITIAL : CharactersStates()

    // Represents the loading state, typically used when fetching data
    data object LOADING : CharactersStates()

    // Represents a successful state, containing the data model with the fetched data
    data class SUCCESS(var dataModel: DataModel) : CharactersStates()

    // Represents an error state, containing an error message
    data class ERROR(var error: String) : CharactersStates()
}
