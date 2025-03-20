package com.namvox.weather_app.models

import com.google.gson.annotations.SerializedName

data class DailyForecasts(
    @SerializedName("DailyForecasts")
    val dailyForecasts: List<DailyForecast>
)