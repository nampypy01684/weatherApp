package com.namvox.weather_app.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.namvox.weather_app.models.BaseModel
import com.namvox.weather_app.models.CurrentWeather
import com.namvox.weather_app.models.DailyForecasts
import com.namvox.weather_app.models.HourlyForecast
import com.namvox.weather_app.repositories.WeatherRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CurrentWeatherViewModel : ViewModel(), KoinComponent {
    private val weatherRepo: WeatherRepo by inject()

    private val _dailyForecasts = MutableStateFlow<BaseModel<DailyForecasts>>(BaseModel.Loading)
    val dailyForecasts: StateFlow<BaseModel<DailyForecasts>> = _dailyForecasts

    private val _hourlyForecasts = MutableStateFlow<BaseModel<List<HourlyForecast>>>(BaseModel.Loading)
    val hourlyForecasts: StateFlow<BaseModel<List<HourlyForecast>>> = _hourlyForecasts

    private val _currentWeather = MutableStateFlow<BaseModel<List<CurrentWeather>>>(BaseModel.Loading)
    val currentWeather: StateFlow<BaseModel<List<CurrentWeather>>> = _currentWeather

    fun fetchWeatherData(locationKey: String) {
        fetchDailyForecasts(locationKey)
        fetchHourlyForecasts(locationKey)
        fetchCurrentWeather(locationKey)
    }

    private fun fetchDailyForecasts(locationKey: String) {
        _dailyForecasts.value = BaseModel.Loading
        viewModelScope.launch {
            _dailyForecasts.value = weatherRepo.getDailyForecasts(locationKey)
        }
    }

    private fun fetchHourlyForecasts(locationKey: String) {
        _hourlyForecasts.value = BaseModel.Loading
        viewModelScope.launch {
            _hourlyForecasts.value = weatherRepo.getHourlyForecasts(locationKey)
        }
    }

    private fun fetchCurrentWeather(locationKey: String) {
        _currentWeather.value = BaseModel.Loading
        viewModelScope.launch {
            _currentWeather.value = weatherRepo.getCurrentConditions(locationKey)
        }
    }

    fun refresh(locationKey: String) {
        fetchWeatherData(locationKey)
    }
}