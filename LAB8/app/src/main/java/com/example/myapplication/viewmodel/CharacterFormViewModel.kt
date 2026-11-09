package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.myapplication.data.repository.CharacterRepository
import com.example.myapplication.db.Character
import com.example.myapplication.ui.CharacterFormRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CharacterFormUiState(
    val isEditing: Boolean = false,
    val isLoading: Boolean = true,
    val name: String = "",
    val status: String = "",
    val species: String = "",
    val gender: String = "",
    val image: String = "",
    val isSaving: Boolean = false,
    val saveError: String? = null
)

class CharacterFormViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val repository = CharacterRepository(application)

    private val characterId =
        savedStateHandle.toRoute<CharacterFormRoute>().characterId

    private val isEditing = characterId > 0

    private val _uiState = MutableStateFlow(CharacterFormUiState())
    val uiState: StateFlow<CharacterFormUiState> = _uiState.asStateFlow()

    init {
        if (isEditing) {
            loadCharacter()
        } else {
            _uiState.update {
                it.copy(
                    isEditing = false,
                    isLoading = false
                )
            }
        }
    }

    private fun loadCharacter() {
        viewModelScope.launch {
            val character = repository.observeCharacter(characterId).first()
            if (character == null) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        saveError = "Personaje no encontrado"
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isEditing = true,
                        isLoading = false,
                        name = character.name,
                        status = character.status,
                        species = character.species,
                        gender = character.gender,
                        image = character.image
                    )
                }
            }
        }
    }

    fun onNameChange(value: String) {
        _uiState.update { it.copy(name = value) }
    }

    fun onStatusChange(value: String) {
        _uiState.update { it.copy(status = value) }
    }

    fun onSpeciesChange(value: String) {
        _uiState.update { it.copy(species = value) }
    }

    fun onGenderChange(value: String) {
        _uiState.update { it.copy(gender = value) }
    }

    fun onImageChange(value: String) {
        _uiState.update { it.copy(image = value) }
    }

    fun save(onSuccess: () -> Unit) {
        val current = _uiState.value
        if (
            current.name.isBlank() ||
            current.status.isBlank() ||
            current.species.isBlank() ||
            current.gender.isBlank() ||
            current.image.isBlank()
        ) {
            _uiState.update {
                it.copy(saveError = "Completa todos los campos")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, saveError = null) }
            try {
                if (isEditing) {
                    val updated = Character(
                        id = characterId,
                        name = current.name.trim(),
                        status = current.status.trim(),
                        species = current.species.trim(),
                        gender = current.gender.trim(),
                        image = current.image.trim(),
                        isFavorite = repository.observeCharacter(characterId).first()?.isFavorite
                            ?: false
                    )
                    repository.updateCharacter(updated)
                } else {
                    repository.createCharacter(
                        name = current.name,
                        status = current.status,
                        species = current.species,
                        gender = current.gender,
                        image = current.image
                    )
                }
                _uiState.update { it.copy(isSaving = false) }
                onSuccess()
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        saveError = "No se pudo guardar el personaje"
                    )
                }
            }
        }
    }
}
