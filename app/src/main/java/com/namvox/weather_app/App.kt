package com.namvox.weather_app

import android.app.Application
import com.google.android.gms.location.LocationServices
import com.namvox.weather_app.network.Api
import com.namvox.weather_app.network.HeaderInterceptor
import com.namvox.weather_app.repositories.WeatherRepo
import com.namvox.weather_app.repositories.WeatherRepoImpl
import com.namvox.weather_app.screens.CurrentWeatherViewModel
import com.namvox.weather_app.screens.HomeViewModel
import com.namvox.weather_app.screens.WeatherViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        val appModule = module {
            // Định nghĩa Retrofit
            single {
                val client = OkHttpClient.Builder()
                    .addInterceptor(HeaderInterceptor())
                    .build()
                Retrofit.Builder()
                    .baseUrl("http://dataservice.accuweather.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
            }

            // Định nghĩa Api
            single {
                val retrofit: Retrofit = get()
                retrofit.create(Api::class.java)
            }

            // Định nghĩa WeatherRepo
            single {
                val api: Api = get()
                WeatherRepoImpl(api)
            } bind WeatherRepo::class

            // Định nghĩa FusedLocationProviderClient
            single {
                LocationServices.getFusedLocationProviderClient(androidContext())
            }

            // Định nghĩa LocationProvider
            single {
                LocationProvider(androidContext(), get())
            }

            // Định nghĩa ViewModels
            viewModel { CurrentWeatherViewModel() }
            viewModel { WeatherViewModel() }
            viewModel { HomeViewModel() }
        }

        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }
}