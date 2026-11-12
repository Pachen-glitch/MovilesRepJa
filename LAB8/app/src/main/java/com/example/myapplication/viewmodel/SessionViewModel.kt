package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.UserPreferences
import com.example.myapplication.data.repository.CharacterRepository
import com.example.myapplication.data.repository.LocationRepository
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

data class SessionUiState(
    val isChecking: Boolean = true,
    val userName: String? = null,
    val isSyncing: Boolean = false,
    val syncError: String? = null
)

class SessionViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val userPreferences = UserPreferences(application)
    private val characterRepository = CharacterRepository(application)
    private val locationRepository = LocationRepository(application)

    private val _uiState = MutableStateFlow(SessionUiState())
    val uiState: StateFlow<SessionUiState> = _uiState.asStateFlow()

    init {
        observeName()
    }

    private fun observeName() {
        viewModelScope.launch {
            userPreferences.name.collect { savedName ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isChecking = false,
                        userName = savedName
                    )
                }
            }
        }
    }

    fun login(
        name: String,
        onSuccess: () -> Unit
    ) {
        val cleanName = name.trim()

        if (cleanName.isBlank() || _uiState.value.isSyncing) {
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isSyncing = true,
                    syncError = null
                )
            }

            try {
                characterRepository.syncFromApiIfEmpty()
                locationRepository.syncFromApiIfEmpty()
                userPreferences.saveName(cleanName)
                _uiState.update { it.copy(isSyncing = false) }
                onSuccess()
            } catch (cancellation: CancellationException) {
                throw cancellation
            } catch (exception: Exception) {
                Log.e("SessionSync", "Fallo al sincronizar", exception)
                _uiState.update {
                    it.copy(
                        isSyncing = false,
                        syncError = "No se pudo sincronizar. Espera un momento e inténtalo de nuevo."
                    )
                }
            }
        }
    }

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            userPreferences.clearName()
            characterRepository.clearAll()
            locationRepository.clearAll()
            onSuccess()
        }
    }
}
