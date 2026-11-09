package com.example.myapplication.data.repository

import android.content.Context
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.local.CharacterEntity
import com.example.myapplication.data.local.toEntity
import com.example.myapplication.data.local.toModel
import com.example.myapplication.data.remote.RickMortyApi
import com.example.myapplication.db.Character
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CharacterRepository(context: Context) {

    private val characterDao = AppDatabase.getDatabase(context).characterDao()
    private val api = RickMortyApi.create()

    fun observeCharacters(): Flow<List<Character>> {
        return characterDao.getAllCharacters().map { entities ->
            entities.map { it.toModel() }
        }
    }

    fun observeCharacter(id: Int): Flow<Character?> {
        return characterDao.getCharacterById(id).map { entity ->
            entity?.toModel()
        }
    }

    suspend fun syncFromApiIfEmpty() {
        if (characterDao.count() > 0) {
            return
        }
        syncAllFromApi()
    }

    suspend fun syncAllFromApi() = withContext(Dispatchers.IO) {
        val allCharacters = mutableListOf<CharacterEntity>()
        var page = 1
        var totalPages: Int

        do {
            val response = api.getCharacters(page)
            allCharacters.addAll(response.results.map { it.toEntity() })
            totalPages = response.info.pages
            page++
        } while (page <= totalPages)

        characterDao.insertAll(allCharacters)
    }

    suspend fun createCharacter(
        name: String,
        status: String,
        species: String,
        gender: String,
        image: String
    ) {
        val nextId = (characterDao.maxId() ?: 0) + 1
        val entity = CharacterEntity(
            id = nextId,
            name = name.trim(),
            status = status.trim(),
            species = species.trim(),
            gender = gender.trim(),
            image = image.trim(),
            isFavorite = false
        )
        characterDao.insert(entity)
    }

    suspend fun updateCharacter(character: Character) {
        characterDao.update(character.toEntity())
    }

    suspend fun deleteCharacter(characterId: Int) {
        characterDao.deleteById(characterId)
    }

    suspend fun toggleFavorite(character: Character) {
        characterDao.updateFavorite(
            characterId = character.id,
            isFavorite = !character.isFavorite
        )
    }

    suspend fun clearAll() {
        characterDao.deleteAll()
    }
}
