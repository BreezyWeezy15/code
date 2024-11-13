package com.rick.morty.business

import androidx.lifecycle.ViewModel
import com.rick.morty.states.CharactersStates
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


import androidx.lifecycle.viewModelScope
import com.rick.morty.models.CharacterEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepoImpl: MainRepoImpl
) : ViewModel() {

    private val _characterState = MutableStateFlow<CharactersStates>(CharactersStates.INITIAL)
    val characterState: StateFlow<CharactersStates> = _characterState

    private val _localCharacters = MutableStateFlow<List<CharacterEntity>>(emptyList())
    val localCharacters: StateFlow<List<CharacterEntity>> = _localCharacters

    init {
        getCharacters()
        getLocalCharacters()
    }

    private fun getCharacters() {
        _characterState.value = CharactersStates.LOADING
        viewModelScope.launch {
            try {
                mainRepoImpl.getCharacters()
                    .collectLatest { result ->
                        _characterState.value = CharactersStates.SUCCESS(result)
                    }
            } catch (e: Exception) {
                _characterState.value = CharactersStates.ERROR("Failed to load characters")
            }
        }
    }


    private fun getLocalCharacters() {
        viewModelScope.launch {
            mainRepoImpl.getAllCharactersFromDb().collectLatest { characters ->
                _localCharacters.value = characters
            }
        }
    }

    fun deleteCharacter(character: CharacterEntity) {
        viewModelScope.launch {
            mainRepoImpl.deleteCharacter(character)
        }
    }

    fun insertCharacter(character: CharacterEntity) {
        viewModelScope.launch {
            mainRepoImpl.insertCharacter(character)
        }
    }

    fun deleteAllCharacters() {
        viewModelScope.launch {
            mainRepoImpl.deleteAllCharacters()
        }
    }
}