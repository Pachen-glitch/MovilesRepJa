package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.LocationRepository
import com.example.myapplication.db.Location
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class LocationListFilter {
    ALL,
    FAVORITES,
    NON_FAVORITES
}

data class LocationUiState(
    val isLoading: Boolean = true,
    val data: List<Location> = emptyList(),
    val hasError: Boolean = false,
    val listFilter: LocationListFilter = LocationListFilter.ALL
)

class LocationViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = LocationRepository(application)

    private val _uiState = MutableStateFlow(LocationUiState())
    val uiState: StateFlow<LocationUiState> = _uiState.asStateFlow()

    private var allLocations: List<Location> = emptyList()

    init {
        observeLocations()
    }

    private fun observeLocations() {
        viewModelScope.launch {
            repository.observeLocations()
                .catch {
                    _uiState.update { current ->
                        current.copy(
                            isLoading = false,
                            hasError = true
                        )
                    }
                }
                .collect { locations ->
                    allLocations = locations
                    applyFilter()
                }
        }
    }

    private fun applyFilter() {
        val filter = _uiState.value.listFilter
        val filtered = when (filter) {
            LocationListFilter.ALL -> allLocations
            LocationListFilter.FAVORITES -> allLocations.filter { it.isFavorite }
            LocationListFilter.NON_FAVORITES -> allLocations.filter { !it.isFavorite }
        }

        _uiState.update { current ->
            current.copy(
                isLoading = false,
                data = filtered,
                hasError = false
            )
        }
    }

    fun cycleFilter() {
        _uiState.update { current ->
            val nextFilter = when (current.listFilter) {
                LocationListFilter.ALL -> LocationListFilter.FAVORITES
                LocationListFilter.FAVORITES -> LocationListFilter.NON_FAVORITES
                LocationListFilter.NON_FAVORITES -> LocationListFilter.ALL
            }
            current.copy(listFilter = nextFilter)
        }
        applyFilter()
    }

    fun toggleFavorite(location: Location) {
        viewModelScope.launch {
            repository.toggleFavorite(location)
        }
    }

    fun loadLocations() {
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
