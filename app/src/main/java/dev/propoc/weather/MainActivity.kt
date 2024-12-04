package dev.propoc.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.propoc.weather.ui.screens.SearchScreen
import dev.propoc.weather.ui.theme.WeatherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherTheme {
                Weather()
            }
        }
    }
}

@Composable
fun Weather() {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { SearchScreen(navController) }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherTheme {
        Weather()
    }
}
