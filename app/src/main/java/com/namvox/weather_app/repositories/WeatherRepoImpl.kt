package com.namvox.weather_app.repositories

import com.namvox.weather_app.models.BaseModel
import com.namvox.weather_app.models.CurrentWeather
import com.namvox.weather_app.models.DailyForecasts
import com.namvox.weather_app.models.HourlyForecast
import com.namvox.weather_app.models.Location
import com.namvox.weather_app.network.Api
import retrofit2.Response

class WeatherRepoImpl(private val api: Api) : WeatherRepo {
    override suspend fun searchLocation(query: String): BaseModel<List<Location>> {
        return request {
            api.searchLocation(query = query)
        }
    }

    override suspend fun getDailyForecasts(locationKey: String): BaseModel<DailyForecasts> {
        return request {
            api.getDailyForecasts(locationKey = locationKey)

        }
    }

    override suspend fun getHourlyForecasts(locationKey: String): BaseModel<List<HourlyForecast>> {
        return request {
            api.getHourlyForecasts(locationKey = locationKey)
        }
    }

    override suspend fun getCurrentConditions(locationKey: String): BaseModel<List<CurrentWeather>> {
        return request {
            api.getCurrentConditions(locationKey = locationKey)
        }
    }

    override suspend fun getLocationByCoordinates(
        latitude: Double,
        longitude: Double
    ): BaseModel<Location> {
        return request {
            api.getLocationByCoordinates(coordinates = "$latitude,$longitude")
        }
    }
}

suspend fun <T> request(request: suspend () -> Response<T>): BaseModel<T> {
    try {
        request().also {
            return if (it.isSuccessful) {
                BaseModel.Success(it.body()!!)
            } else {
                BaseModel.Error(it.errorBody()?.string().toString())
            }
        }
    } catch (e: Exception) {
        return BaseModel.Error(e.message.toString())
    }
}

