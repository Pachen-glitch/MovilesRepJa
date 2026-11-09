package com.example.myapplication.data.remote

import com.example.myapplication.data.remote.dto.CharacterDto
import com.example.myapplication.data.remote.dto.LocationDto
import com.example.myapplication.data.remote.dto.PagedResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RickMortyApi {

    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int
    ): PagedResponse<CharacterDto>

    @GET("location")
    suspend fun getLocations(
        @Query("page") page: Int
    ): PagedResponse<LocationDto>

    companion object {
        private const val BASE_URL = "https://rickandmortyapi.com/api/"

        fun create(): RickMortyApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RickMortyApi::class.java)
        }
    }
}
