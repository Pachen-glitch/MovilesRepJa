package com.example.myapplication.data.remote.dto

data class PagedResponse<T>(
    val info: PageInfo,
    val results: List<T>
)

data class PageInfo(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)

data class CharacterDto(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val image: String
)

data class LocationDto(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String
)
