package com.namvox.weather_app.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.namvox.weather_app.models.BaseModel
import com.namvox.weather_app.models.Location
import com.namvox.weather_app.repositories.WeatherRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HomeViewModel : ViewModel(), KoinComponent {
    val repo: WeatherRepo by inject()

    private val _location: MutableStateFlow<BaseModel<List<Location>>?> = MutableStateFlow(null)
    val locations = _location.asStateFlow()

//    fun searchLocation(query: String) {
//        viewModelScope.launch {
//            _location.update { BaseModel.Loading }
//            repo.searchLocation(query).also { result ->
//                _location.update { result }
//            }
//        }
//    }
fun searchLocation(query: String) {
    Log.d("HomeViewModel", "Fetching locations for: $query")
    viewModelScope.launch {
        _location.value = BaseModel.Loading
        try {
            val response = repo.searchLocation(query) // Gọi API
            _location.value = response
        } catch (e: Exception) {
            Log.e("HomeViewModel", "Error fetching locations: ${e.message}")
            _location.value = BaseModel.Error(e.message ?: "Unknown error")
        }
    }
}

}