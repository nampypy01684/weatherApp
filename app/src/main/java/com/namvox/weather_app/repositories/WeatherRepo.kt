package com.namvox.weather_app.repositories

import com.namvox.weather_app.models.BaseModel
import com.namvox.weather_app.models.CurrentWeather
import com.namvox.weather_app.models.DailyForecasts
import com.namvox.weather_app.models.HourlyForecast
import com.namvox.weather_app.models.Location

interface WeatherRepo {
    suspend fun searchLocation(query: String): BaseModel<List<Location>>
    suspend fun getDailyForecasts(locationKey: String): BaseModel<DailyForecasts>
    suspend fun getHourlyForecasts(locationKey: String): BaseModel<List<HourlyForecast>>

    suspend fun getCurrentConditions(locationKey: String): BaseModel<List<CurrentWeather>>
    suspend fun getLocationByCoordinates(latitude: Double, longitude: Double): BaseModel<Location>
}