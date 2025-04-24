package com.namvox.weather_app.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.namvox.weather_app.LocationProvider
import com.namvox.weather_app.models.BaseModel
import com.namvox.weather_app.models.CurrentWeather
import com.namvox.weather_app.models.Location as WeatherLocation
import com.namvox.weather_app.repositories.WeatherRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HomeViewModel : ViewModel(), KoinComponent {
    // Dependencies injected by Koin
    private val weatherRepo: WeatherRepo by inject()
    private val locationProvider: LocationProvider by inject()

    // Location search state
    private val _locations = MutableStateFlow<BaseModel<List<WeatherLocation>>?>(null)
    val locations: StateFlow<BaseModel<List<WeatherLocation>>?> = _locations.asStateFlow()

    // Current location state
    private val _currentLocation = MutableStateFlow<BaseModel<WeatherLocation>?>(null)
    val currentLocation: StateFlow<BaseModel<WeatherLocation>?> = _currentLocation.asStateFlow()

    // Current weather state
    private val _currentWeather = MutableStateFlow<BaseModel<List<CurrentWeather>>?>(null)
    val currentWeather: StateFlow<BaseModel<List<CurrentWeather>>?> = _currentWeather.asStateFlow()

    // Location permission state
    private val _locationPermissionGranted = MutableStateFlow(locationProvider.hasLocationPermission())
    val locationPermissionGranted: StateFlow<Boolean> = _locationPermissionGranted.asStateFlow()

    /**
     * Search for locations by query string
     */
    fun searchLocation(query: String) {
        Log.d("HomeViewModel", "Fetching locations for: $query")
        _locations.value = BaseModel.Loading
        viewModelScope.launch {
            try {
                val response = weatherRepo.searchLocation(query)
                _locations.value = response
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching locations: ${e.message}")
                _locations.value = BaseModel.Error(e.message ?: "Unknown error")
            }
        }
    }

    /**
     * Update location permission state
     */
    fun updateLocationPermissionState(granted: Boolean) {
        _locationPermissionGranted.value = granted
        if (granted) {
            fetchCurrentLocationWeather()
        }
    }

    /**
     * Fetch weather for current device location
     */
    fun fetchCurrentLocationWeather() {
        if (!locationProvider.hasLocationPermission()) {
            return
        }

        _currentLocation.value = BaseModel.Loading
        _currentWeather.value = BaseModel.Loading

        viewModelScope.launch {
            try {
                val deviceLocation = locationProvider.getCurrentLocation()
                if (deviceLocation != null) {
                    fetchWeatherForLocation(deviceLocation)
                } else {
                    _currentLocation.value = BaseModel.Error("Không thể lấy vị trí")
                    _currentWeather.value = BaseModel.Error("Không thể lấy vị trí")
                }
            } catch (e: Exception) {
                _currentLocation.value = BaseModel.Error(e.message ?: "Lỗi không xác định")
                _currentWeather.value = BaseModel.Error(e.message ?: "Lỗi không xác định")
            }
        }
    }

    /**
     * Fetch weather for a specific location
     */
    private suspend fun fetchWeatherForLocation(deviceLocation: android.location.Location) {
        val locationResult = weatherRepo.getLocationByCoordinates(
            deviceLocation.latitude,
            deviceLocation.longitude
        )

        when (locationResult) {
            is BaseModel.Success -> {
                _currentLocation.value = BaseModel.Success(locationResult.data)

                val weatherResult = weatherRepo.getCurrentConditions(locationResult.data.key)
                _currentWeather.value = weatherResult
            }
            is BaseModel.Error -> {
                _currentLocation.value = locationResult
                _currentWeather.value = BaseModel.Error(locationResult.error)
            }
            else -> {}
        }
    }
}