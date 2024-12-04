package dev.propoc.weather.repository

import dev.propoc.weather.model.SearchByCityResponse
import dev.propoc.weather.network.ApiClient

class WeatherRepository {
    private val apiService = ApiClient.apiService

    suspend fun getWeatherByCity(city: String): SearchByCityResponse {
        return apiService.getWeatherByCity(city)
    }
}