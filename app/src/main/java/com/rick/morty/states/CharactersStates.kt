package com.rick.morty.states

import com.rick.morty.models.DataModel

sealed class CharactersStates {

    data object INITIAL : CharactersStates()
    data object LOADING : CharactersStates()
    data class SUCCESS(var dataModel: DataModel) : CharactersStates()
    data class ERROR(var error : String) : CharactersStates()

}