package com.namvox.weather_app.models

import com.google.gson.annotations.SerializedName

data class CurrentWeather(
    val LocalObservationDateTime: String,
    val EpochTime: Long,
    val WeatherText: String,
    val WeatherIcon: Int,
    val HasPrecipitation: Boolean,
    val PrecipitationType: String?,
    val IsDayTime: Boolean,
    val Temperature: Temperature,
    val RealFeelTemperature: RealFeelTemperature,
    val RelativeHumidity: Int,
    val Wind: Wind,
    val UVIndex: Int,
    val UVIndexText: String,
    val Visibility: Visibility,
    val CloudCover: Int
)

data class Temperature(
    val Metric: UnitValue,
    val Imperial: UnitValue
)

data class RealFeelTemperature(
    val Metric: UnitValue,
    val Imperial: UnitValue
)

data class UnitValue(
    val Value: Double,
    val Unit: String,
    val UnitType: Int
)

data class Wind(
    val Direction: Direction,
    val Speed: Speed
)

data class Direction(
    val Degrees: Int,
    val Localized: String,
    val English: String
)

data class Speed(
    val Metric: UnitValue,
    val Imperial: UnitValue
)

data class Visibility(
    val Metric: UnitValue,
    val Imperial: UnitValue
)
