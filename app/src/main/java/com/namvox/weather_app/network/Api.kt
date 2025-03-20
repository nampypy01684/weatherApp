package com.namvox.weather_app.network

import com.namvox.weather_app.models.DailyForecasts
import com.namvox.weather_app.models.HourlyForecast
import com.namvox.weather_app.models.Location
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val API_KEY = "IY36204QgjhIcZCm3AfJrIeLAGJtKVhs"

interface Api {
    @GET("locations/v1/cities/search")
    suspend fun searchLocation(
        @Query("apikey") apiKey: String = API_KEY,
        @Query("q") query: String
    ): Response<List<Location>>

    @GET("forecasts/v1/daily/5day/{location_key}")
    suspend fun getDailyForecasts(
        @Path("location_key") locationKey: String,
        @Query("apikey") apiKey: String = API_KEY,
        @Query("metric") metric: Boolean = true
    ): Response<DailyForecasts>

    @GET("forecasts/v1/hourly/12hour/{location_key}")
    suspend fun getHourlyForecasts(
        @Path("location_key") locationKey: String,
        @Query("apikey") apiKey: String = API_KEY,
        @Query("metric") metric: Boolean = true
    ): Response<List<HourlyForecast>>
}