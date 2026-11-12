package com.example.myapplication.data.repository

import android.content.Context
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.local.LocationEntity
import com.example.myapplication.data.local.toEntity
import com.example.myapplication.data.local.toModel
import com.example.myapplication.data.remote.RickMortyApi
import com.example.myapplication.db.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class LocationRepository(context: Context) {

    private val locationDao = AppDatabase.getDatabase(context).locationDao()
    private val api by lazy { RickMortyApi.create() }

    fun observeLocations(): Flow<List<Location>> {
        return locationDao.getAllLocations().map { entities ->
            entities.map { it.toModel() }
        }
    }

    fun observeLocation(id: Int): Flow<Location?> {
        return locationDao.getLocationById(id).map { entity ->
            entity?.toModel()
        }
    }

    suspend fun syncFromApiIfEmpty() {
        if (locationDao.count() > 0) {
            return
        }
        syncAllFromApi()
    }

    suspend fun syncAllFromApi() = withContext(Dispatchers.IO) {
        val allLocations = mutableListOf<LocationEntity>()
        var page = 1
        var totalPages: Int

        do {
            val response = api.getLocations(page)
            allLocations.addAll(response.results.map { it.toEntity() })
            totalPages = response.info.pages
            page++
            if (page <= totalPages) {
                delay(PAGE_DELAY_MS)
            }
        } while (page <= totalPages)

        locationDao.insertAll(allLocations)
    }

    suspend fun toggleFavorite(location: Location) {
        locationDao.updateFavorite(
            locationId = location.id,
            isFavorite = !location.isFavorite
        )
    }

    suspend fun clearAll() {
        locationDao.deleteAll()
    }

    private companion object {
        const val PAGE_DELAY_MS = 600L
    }
}
