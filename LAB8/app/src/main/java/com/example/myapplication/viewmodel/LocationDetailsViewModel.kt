package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.myapplication.data.repository.LocationRepository
import com.example.myapplication.db.Location
import com.example.myapplication.ui.LocationDetailsRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LocationDetailsUiState(
    val isLoading: Boolean = true,
    val data: Location? = null,
    val hasError: Boolean = false
)

class LocationDetailsViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val repository = LocationRepository(application)

    private val locationId =
        savedStateHandle.toRoute<LocationDetailsRoute>().locationId

    private val _uiState = MutableStateFlow(LocationDetailsUiState())
    val uiState: StateFlow<LocationDetailsUiState> = _uiState.asStateFlow()

    init {
        observeLocation()
    }

    private fun observeLocation() {
        viewModelScope.launch {
            repository.observeLocation(locationId)
                .catch {
                    _uiState.update { current ->
                        current.copy(
                            isLoading = false,
                            hasError = true
                        )
                    }
                }
                .collect { location ->
                    if (location == null) {
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
                                data = location,
                                hasError = false
                            )
                        }
                    }
                }
        }
    }

    fun toggleFavorite(location: Location) {
        viewModelScope.launch {
            repository.toggleFavorite(location)
        }
    }

    fun loadLocation() {
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
