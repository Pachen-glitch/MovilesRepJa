package com.example.myapplication.data.remote

import com.example.myapplication.data.remote.dto.CharacterPageResponse
import com.example.myapplication.data.remote.dto.LocationPageResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.delay

class RickMortyApi {

    private val httpClient: HttpClient by lazy { RickMortyHttpClient.client }

    suspend fun getCharacters(page: Int): CharacterPageResponse {
        return withRateLimitRetry {
            httpClient.get("${RickMortyHttpClient.BASE_URL}character") {
                parameter("page", page)
            }.body()
        }
    }

    suspend fun getLocations(page: Int): LocationPageResponse {
        return withRateLimitRetry {
            httpClient.get("${RickMortyHttpClient.BASE_URL}location") {
                parameter("page", page)
            }.body()
        }
    }

    private suspend fun <T> withRateLimitRetry(request: suspend () -> T): T {
        var attempt = 0
        while (true) {
            try {
                return request()
            } catch (exception: ClientRequestException) {
                val rateLimited = exception.response.status == HttpStatusCode.TooManyRequests
                if (!rateLimited || attempt >= 4) {
                    throw exception
                }
                attempt++
                delay(35_000L)
            }
        }
    }

    companion object {
        fun create(): RickMortyApi = RickMortyApi()
    }
}
