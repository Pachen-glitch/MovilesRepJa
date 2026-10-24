package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.UserPreferences
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.local.toEntity
import com.example.myapplication.db.CharacterDb
import com.example.myapplication.db.LocationDb
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SessionUiState(
    val isChecking: Boolean = true,
    val userName: String? = null,
    val isSyncing: Boolean = false
)

class SessionViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val userPreferences = UserPreferences(application)
    private val database = AppDatabase.getDatabase(application)
    private val characterDao = database.characterDao()
    private val locationDao = database.locationDao()

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
            _uiState.update { it.copy(isSyncing = true) }

            delay(4_000)

            if (characterDao.count() == 0) {
                val characters = CharacterDb()
                    .getAllCharacters()
                    .map { it.toEntity() }
                characterDao.insertAll(characters)
            }

            if (locationDao.count() == 0) {
                val locations = LocationDb()
                    .getAllLocations()
                    .map { it.toEntity() }
                locationDao.insertAll(locations)
            }

            userPreferences.saveName(cleanName)

            _uiState.update { it.copy(isSyncing = false) }
            onSuccess()
        }
    }

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            userPreferences.clearName()
            characterDao.deleteAll()
            locationDao.deleteAll()
            onSuccess()
        }
    }
}
