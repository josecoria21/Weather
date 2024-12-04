package dev.propoc.weather.network

import dev.propoc.weather.model.SearchByCityResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET(".")
    suspend fun getWeatherByCity(
        @Query("q") city: String,
        @Query("appid") token: String = ""
    ): SearchByCityResponse
}
