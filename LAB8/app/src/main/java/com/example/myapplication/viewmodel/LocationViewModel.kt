package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.local.toModel
import com.example.myapplication.db.Location
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LocationUiState(
    val isLoading: Boolean = true,
    val data: List<Location> = emptyList(),
    val hasError: Boolean = false
)

class LocationViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val locationDao =
        AppDatabase.getDatabase(application).locationDao()

    private val _uiState = MutableStateFlow(LocationUiState())
    val uiState: StateFlow<LocationUiState> = _uiState.asStateFlow()

    init {
        observeLocations()
    }

    private fun observeLocations() {
        viewModelScope.launch {
            locationDao.getAllLocations()
                .catch {
                    _uiState.update { current ->
                        current.copy(
                            isLoading = false,
                            hasError = true
                        )
                    }
                }
                .collect { entities ->
                    _uiState.update { current ->
                        current.copy(
                            isLoading = false,
                            data = entities.map { it.toModel() },
                            hasError = false
                        )
                    }
                }
        }
    }

    fun loadLocations() {
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
