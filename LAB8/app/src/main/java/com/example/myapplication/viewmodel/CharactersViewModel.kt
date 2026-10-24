package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.local.toModel
import com.example.myapplication.db.Character
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CharactersUiState(
    val isLoading: Boolean = true,
    val data: List<Character> = emptyList(),
    val hasError: Boolean = false,
    val showFavoritesOnly: Boolean = false
)

class CharactersViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val characterDao =
        AppDatabase.getDatabase(application).characterDao()

    private val _uiState = MutableStateFlow(CharactersUiState())
    val uiState: StateFlow<CharactersUiState> = _uiState.asStateFlow()

    private var allCharacters: List<Character> = emptyList()

    init {
        observeCharacters()
    }

    private fun observeCharacters() {
        viewModelScope.launch {
            characterDao.getAllCharacters()
                .catch {
                    _uiState.update { current ->
                        current.copy(
                            isLoading = false,
                            hasError = true
                        )
                    }
                }
                .collect { entities ->
                    allCharacters = entities.map { it.toModel() }
                    applyFilter()
                }
        }
    }

    private fun applyFilter() {
        val showFavoritesOnly = _uiState.value.showFavoritesOnly
        val filtered = if (showFavoritesOnly) {
            allCharacters.filter { it.isFavorite }
        } else {
            allCharacters
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
            current.copy(showFavoritesOnly = !current.showFavoritesOnly)
        }
        applyFilter()
    }

    fun toggleFavorite(character: Character) {
        viewModelScope.launch {
            characterDao.updateFavorite(
                characterId = character.id,
                isFavorite = !character.isFavorite
            )
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
