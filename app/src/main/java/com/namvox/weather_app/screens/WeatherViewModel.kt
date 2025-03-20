package com.namvox.weather_app.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.namvox.weather_app.models.BaseModel
import com.namvox.weather_app.models.DailyForecasts
import com.namvox.weather_app.models.HourlyForecast
import com.namvox.weather_app.repositories.WeatherRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class WeatherViewModel : ViewModel(), KoinComponent {
    private val repo: WeatherRepo by inject()

    private val _hourlyForecast =
        MutableStateFlow<BaseModel<List<HourlyForecast>>>(BaseModel.Loading)


    val hourlyForecast = _hourlyForecast.asStateFlow()

    private val _dailyForecast: MutableStateFlow<BaseModel<DailyForecasts>> = MutableStateFlow(
        BaseModel.Loading
    )
    val dailyForecast = _dailyForecast.asStateFlow()

    fun getHourlyForecast(locationKey: String) {
        viewModelScope.launch {
            repo.getHourlyForecasts(locationKey).also { data ->
                _hourlyForecast.update { data }
            }

        }
    } fun getDailyForecast(locationKey: String) {
        viewModelScope.launch {
            repo.getDailyForecasts(locationKey).also { data ->
                _dailyForecast.update { data }
            }

        }
    }
}