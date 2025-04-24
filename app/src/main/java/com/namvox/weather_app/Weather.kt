package com.namvox.weather_app


import com.google.gson.annotations.SerializedName

data class Weather(
    @SerializedName("LocalObservationDateTime")
    val localObservationDateTime: String,

    @SerializedName("EpochTime")
    val epochTime: Long,

    @SerializedName("WeatherText")
    val weatherText: String,

    @SerializedName("WeatherIcon")
    val weatherIcon: Int,

    @SerializedName("HasPrecipitation")
    val hasPrecipitation: Boolean,

    @SerializedName("PrecipitationType")
    val precipitationType: String?,

    @SerializedName("IsDayTime")
    val isDayTime: Boolean,

//    @SerializedName("Temperature")
//    val temperature: Temperature,

    @SerializedName("MobileLink")
    val mobileLink: String,

    @SerializedName("Link")
    val link: String
)

//data class Temperature(
//    @SerializedName("Metric")
//    val metric: TemperatureUnit,
//
//    @SerializedName("Imperial")
//    val imperial: TemperatureUnit
//)

data class TemperatureUnit(
    @SerializedName("Value")
    val value: Double,

    @SerializedName("Unit")
    val unit: String,

    @SerializedName("UnitType")
    val unitType: Int
)