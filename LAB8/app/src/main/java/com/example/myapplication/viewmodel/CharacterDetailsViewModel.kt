package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.local.toModel
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

    private val characterDao =
        AppDatabase.getDatabase(application).characterDao()

    private val characterId =
        savedStateHandle.toRoute<CharacterDetailsRoute>().characterId

    private val _uiState = MutableStateFlow(CharacterDetailsUiState())
    val uiState: StateFlow<CharacterDetailsUiState> = _uiState.asStateFlow()

    init {
        observeCharacter()
    }

    private fun observeCharacter() {
        viewModelScope.launch {
            characterDao.getCharacterById(characterId)
                .catch {
                    _uiState.update { current ->
                        current.copy(
                            isLoading = false,
                            hasError = true
                        )
                    }
                }
                .collect { entity ->
                    if (entity == null) {
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
                                data = entity.toModel(),
                                hasError = false
                            )
                        }
                    }
                }
        }
    }

    fun toggleFavorite(character: Character) {
        viewModelScope.launch {
            characterDao.updateFavorite(
                characterId = character.id,
                isFavorite = !character.isFavorite
            )
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
