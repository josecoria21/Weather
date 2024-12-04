package dev.propoc.weather.sharedpreferences

import android.content.Context

object PreferencesHelper {
    private const val PREFS_NAME = "weather_prefs"
    private const val LAST_SEARCHED_CITY = "last_searched_city"

    fun saveLastSearchedCity(context: Context, city: String) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(LAST_SEARCHED_CITY, city).apply()
    }

    fun getLastSearchedCity(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(LAST_SEARCHED_CITY, null)
    }
}