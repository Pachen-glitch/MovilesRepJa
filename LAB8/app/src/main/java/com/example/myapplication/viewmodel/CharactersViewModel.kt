package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.CharacterRepository
import com.example.myapplication.db.Character
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class CharacterListFilter {
    ALL,
    FAVORITES
}

data class CharactersUiState(
    val isLoading: Boolean = true,
    val data: List<Character> = emptyList(),
    val hasError: Boolean = false,
    val listFilter: CharacterListFilter = CharacterListFilter.ALL
)

class CharactersViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = CharacterRepository(application)

    private val _uiState = MutableStateFlow(CharactersUiState())
    val uiState: StateFlow<CharactersUiState> = _uiState.asStateFlow()

    private var allCharacters: List<Character> = emptyList()

    init {
        observeCharacters()
    }

    private fun observeCharacters() {
        viewModelScope.launch {
            repository.observeCharacters()
                .catch {
                    _uiState.update { current ->
                        current.copy(
                            isLoading = false,
                            hasError = true
                        )
                    }
                }
                .collect { characters ->
                    allCharacters = characters
                    applyFilter()
                }
        }
    }

    private fun applyFilter() {
        val filter = _uiState.value.listFilter
        val filtered = when (filter) {
            CharacterListFilter.ALL -> allCharacters
            CharacterListFilter.FAVORITES -> allCharacters.filter { it.isFavorite }
        }

        _uiState.update { current ->
            current.copy(
                isLoading = false,
                data = filtered,
                hasError = false
            )
        }
    }

    fun toggleFavoritesFilter() {
        _uiState.update { current ->
            val nextFilter = when (current.listFilter) {
                CharacterListFilter.ALL -> CharacterListFilter.FAVORITES
                CharacterListFilter.FAVORITES -> CharacterListFilter.ALL
            }
            current.copy(listFilter = nextFilter)
        }
        applyFilter()
    }

    fun toggleFavorite(character: Character) {
        viewModelScope.launch {
            repository.toggleFavorite(character)
        }
    }

    fun deleteCharacter(characterId: Int) {
        viewModelScope.launch {
            repository.deleteCharacter(characterId)
        }
    }

    fun loadCharacters() {
        _uiState.update {
            it.copy(
                isLoading = false,
                hasError = false
            )
        }
        applyFilter()
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
