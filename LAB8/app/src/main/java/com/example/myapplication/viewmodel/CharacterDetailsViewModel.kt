package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.myapplication.data.repository.CharacterRepository
import com.example.myapplication.db.Character
import com.example.myapplication.ui.CharacterDetailsRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CharacterDetailsUiState(
    val isLoading: Boolean = true,
    val data: Character? = null,
    val hasError: Boolean = false
)

class CharacterDetailsViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val repository = CharacterRepository(application)

    private val characterId =
        savedStateHandle.toRoute<CharacterDetailsRoute>().characterId

    private val _uiState = MutableStateFlow(CharacterDetailsUiState())
    val uiState: StateFlow<CharacterDetailsUiState> = _uiState.asStateFlow()

    init {
        observeCharacter()
    }

    private fun observeCharacter() {
        viewModelScope.launch {
            repository.observeCharacter(characterId)
                .catch {
                    _uiState.update { current ->
                        current.copy(
                            isLoading = false,
                            hasError = true
                        )
                    }
                }
                .collect { character ->
                    if (character == null) {
                        _uiState.update { current ->
                            current.copy(
                                isLoading = false,
                                data = null,
                                hasError = true
                            )
                        }
                    } else {
                        _uiState.update { current ->
                            current.copy(
                                isLoading = false,
                                data = character,
                                hasError = false
                            )
                        }
                    }
                }
        }
    }

    fun toggleFavorite(character: Character) {
        viewModelScope.launch {
            repository.toggleFavorite(character)
        }
    }

    fun deleteCharacter(onDeleted: () -> Unit) {
        viewModelScope.launch {
            repository.deleteCharacter(characterId)
            onDeleted()
        }
    }

    fun loadCharacter() {
        _uiState.update {
            it.copy(
                isLoading = false,
                hasError = false
            )
        }
    }

    fun showError() {
        _uiState.update {
            it.copy(
                isLoading = false,
                hasError = true
            )
        }
    }
}
