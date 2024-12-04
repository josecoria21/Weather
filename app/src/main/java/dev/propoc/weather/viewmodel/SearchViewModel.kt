package dev.propoc.weather.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.propoc.weather.model.SearchByCityResponse
import dev.propoc.weather.repository.WeatherRepository
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val repository = WeatherRepository()
    private val _uiState = mutableStateOf<UiState<List<SearchByCityResponse>>>(UiState.Loading)
    val uiState = _uiState

    fun getWeatherByCity(city: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val result = repository.getWeatherByCity(city)
                _uiState.value = UiState.Success(listOf(result))
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error: ${e.message}")
            }
        }
    }
}