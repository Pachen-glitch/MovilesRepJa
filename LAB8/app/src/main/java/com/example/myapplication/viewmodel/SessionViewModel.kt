package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SessionUiState(
    val isChecking: Boolean = true,
    val userName: String? = null
)

class SessionViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val userPreferences = UserPreferences(application)

    private val _uiState = MutableStateFlow(
        SessionUiState()
    )

    val uiState: StateFlow<SessionUiState> =
        _uiState.asStateFlow()

    init {
        observeName()
    }

    private fun observeName() {
        viewModelScope.launch {
            userPreferences.name.collect { savedName ->
                _uiState.value = SessionUiState(
                    isChecking = false,
                    userName = savedName
                )
            }
        }
    }

    fun login(
        name: String,
        onSuccess: () -> Unit
    ) {
        val cleanName = name.trim()

        if (cleanName.isBlank()) {
            return
        }

        viewModelScope.launch {
            userPreferences.saveName(cleanName)
            onSuccess()
        }
    }

    fun logout(
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            userPreferences.clearName()
            onSuccess()
        }
    }
}
