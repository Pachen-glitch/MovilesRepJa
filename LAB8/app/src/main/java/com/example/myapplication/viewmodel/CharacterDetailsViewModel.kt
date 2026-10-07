package com.example.myapplication.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.myapplication.db.Character
import com.example.myapplication.db.CharacterDb
import com.example.myapplication.ui.CharacterDetailsRoute
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CharacterDetailsUiState(
    val isLoading: Boolean = true,
    val data: Character? = null,
    val hasError: Boolean = false
)

class CharacterDetailsViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val characterDb = CharacterDb()

    private val characterId =
        savedStateHandle.toRoute<CharacterDetailsRoute>().characterId

    private val _uiState = MutableStateFlow(CharacterDetailsUiState())
    val uiState: StateFlow<CharacterDetailsUiState> = _uiState.asStateFlow()

    init {
        loadCharacter()
    }

    fun loadCharacter() {
        viewModelScope.launch {
            _uiState.value = CharacterDetailsUiState(isLoading = true)

            delay(2_000)

            _uiState.value = CharacterDetailsUiState(
                isLoading = false,
                data = characterDb.getCharacterById(characterId),
                hasError = false
            )
        }
    }

    fun showError() {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            hasError = true
        )
    }
}
