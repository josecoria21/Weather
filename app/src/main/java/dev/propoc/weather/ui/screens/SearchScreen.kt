package dev.propoc.weather.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import dev.propoc.weather.model.SearchByCityResponse
import dev.propoc.weather.sharedpreferences.PreferencesHelper
import dev.propoc.weather.viewmodel.SearchViewModel
import dev.propoc.weather.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavHostController) {
    val viewModel: SearchViewModel = viewModel()
    // Get context from LocalContext
    val context = LocalContext.current

    // Fetch the last searched city from preferences
    val lastSearchedCity = PreferencesHelper.getLastSearchedCity(context)

    // Use remembered state for the city
    val (value, onValueChange) = remember { mutableStateOf(lastSearchedCity ?: "") }

    // Auto-load weather data for the last searched city when screen loads
    LaunchedEffect(lastSearchedCity) {
        if (!lastSearchedCity.isNullOrEmpty()) {
            // Ensure the weather is fetched for the last searched city
            viewModel.getWeatherByCity(lastSearchedCity)
        }
    }

    // Get the current UI state
    val uiState = viewModel.uiState.value

    // UI Layout
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Search TextField
            TextField(
                value = value,
                onValueChange = {
                    onValueChange(it)
                    viewModel.getWeatherByCity(it) // Trigger API call when the user changes the city
                },
                textStyle = TextStyle(fontSize = 17.sp),
                leadingIcon = { Icon(Icons.Filled.Search, null, tint = Color.White) },
                modifier = Modifier
                    .padding(10.dp)
                    .background(Color(0xFFE7F1F1), RoundedCornerShape(16.dp)),
                placeholder = { Text(text = "Search for a city...") },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.DarkGray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // UI Based on State
            when (uiState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(top = 16.dp),
                        color = Color.Gray
                    )
                }
                is UiState.Success<*> -> {
                    // Ensure type casting from Success to Success<List<SearchByCityResponse>>
                    val data = uiState.data as? List<SearchByCityResponse> ?: emptyList()

                    // Display results after fetching weather data
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        items(data.size) { index ->
                            Row {
                                Column {
                                    Text(text = "Weather")
                                    data[index].weather.firstOrNull()?.icon?.let { urlImage(it) }
                                    Text(text = "${data[index].weather.firstOrNull()?.main}")
                                    Text(text = "${data[index].weather.firstOrNull()?.description}")
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(text = "Coordinates")
                                Text(text = "${data[index].coord.lat}")
                                Text(text = "${data[index].coord.lon}")
                            }
                        }
                    }
                }
                is UiState.Error -> {
                    // Display error message
                    Text(
                        text = uiState.message,
                        color = Color.Red,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun urlImage(imageCode: String) {
    // Create a painter for the image
    val painter = rememberAsyncImagePainter("https://openweathermap.org/img/wn/$imageCode@2x.png")

    // Use Image composable to display the image
    Image(
        painter = painter,
        contentDescription = "Image from URL",
        modifier = Modifier
            .size(150.dp)
            .clip(RoundedCornerShape(16.dp)),
        contentScale = ContentScale.Crop
    )
}
