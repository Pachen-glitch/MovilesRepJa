package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.db.Location
import com.example.myapplication.db.LocationDb
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LocationUiState(
    val isLoading: Boolean = true,
    val data: List<Location> = emptyList(),
    val hasError: Boolean = false
)

class LocationViewModel : ViewModel() {

    private val locationDb = LocationDb()

    private val _uiState = MutableStateFlow(LocationUiState())
    val uiState: StateFlow<LocationUiState> = _uiState.asStateFlow()

    init {
        loadLocations()
    }

    fun loadLocations() {
        viewModelScope.launch {
            _uiState.value = LocationUiState(isLoading = true)

            delay(4_000)
            if (_uiState.value.hasError) {
                return@launch
            }

            _uiState.value = LocationUiState(
                isLoading = false,
                data = locationDb.getAllLocations(),
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
