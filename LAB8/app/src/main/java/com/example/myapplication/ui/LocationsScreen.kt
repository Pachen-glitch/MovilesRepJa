package com.example.myapplication.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.viewmodel.LocationListFilter
import com.example.myapplication.viewmodel.LocationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsScreen(
    onLocationClick: (Int) -> Unit,
    viewModel: LocationViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val label = when (uiState.listFilter) {
                        LocationListFilter.ALL -> "Todos"
                        LocationListFilter.FAVORITES -> "Favoritos"
                        LocationListFilter.NON_FAVORITES -> "No favoritos"
                    }
                    Text("Locations ($label)")
                },
                actions = {
                    IconButton(onClick = { viewModel.cycleFilter() }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Cambiar filtro"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        when {
            uiState.isLoading -> {
                LoadingLayout(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .clickable { viewModel.showError() }
                )
            }

            uiState.hasError -> {
                ErrorLayout(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    message = "Error al obtener listado de ubicaciones",
                    onRetry = { viewModel.loadLocations() }
                )
            }

            uiState.data.isEmpty() -> {
                val emptyMessage = when (uiState.listFilter) {
                    LocationListFilter.FAVORITES -> "No hay ubicaciones favoritas."
                    LocationListFilter.NON_FAVORITES -> "No hay ubicaciones no favoritas."
                    LocationListFilter.ALL -> "No hay ubicaciones para mostrar."
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = emptyMessage,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    items(items = uiState.data, key = { it.id }) { location ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onLocationClick(location.id) }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = location.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Text(
                                    text = location.type,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            IconButton(
                                onClick = { viewModel.toggleFavorite(location) }
                            ) {
                                Icon(
                                    imageVector = if (location.isFavorite) {
                                        Icons.Default.Favorite
                                    } else {
                                        Icons.Default.FavoriteBorder
                                    },
                                    contentDescription = "Favorito",
                                    tint = if (location.isFavorite) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    }
                                )
                            }
                        }

                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
            }
        }
    }
}
