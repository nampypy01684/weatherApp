package com.namvox.weather_app.models

import com.google.gson.annotations.SerializedName

data class DailyForecast(
    @SerializedName("Date")
    val date: String,
    @SerializedName("EpochDate")
    val epochDate: Long,
    @SerializedName("Temperature")
    val temperature: Temperaturee,
    @SerializedName("Day")
    val day: WeatherStat,
    @SerializedName("Night")
    val night: WeatherStat
)

data class Temperaturee(
    @SerializedName("Minimum")
    val min: Value,
    @SerializedName("Maximum")
    val max: Value
)

data class WeatherStat(
    @SerializedName("Icon")
    val icon: Int,
    @SerializedName("IconPhrase")
    val iconPhrase: String,
    @SerializedName("HasPrecipitation")
    val hasPrecipitation: Boolean

)