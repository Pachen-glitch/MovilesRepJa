package com.example.myapplication.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.myapplication.db.Location
import com.example.myapplication.db.LocationDb
import com.example.myapplication.ui.LocationDetailsRoute
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LocationDetailsUiState(
    val isLoading: Boolean = true,
    val data: Location? = null,
    val hasError: Boolean = false
)

class LocationDetailsViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val locationDb = LocationDb()

    private val locationId =
        savedStateHandle.toRoute<LocationDetailsRoute>().locationId

    private val _uiState = MutableStateFlow(LocationDetailsUiState())
    val uiState: StateFlow<LocationDetailsUiState> = _uiState.asStateFlow()

    init {
        loadLocation()
    }

    fun loadLocation() {
        viewModelScope.launch {
            _uiState.value = LocationDetailsUiState(isLoading = true)

            delay(2_000)
            if (_uiState.value.hasError) {
                return@launch
            }

            _uiState.value = LocationDetailsUiState(
                isLoading = false,
                data = locationDb.getLocationById(locationId),
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
