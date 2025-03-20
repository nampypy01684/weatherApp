package com.namvox.weather_app

import android.app.Application
import com.namvox.weather_app.network.Api
import com.namvox.weather_app.network.HeaderInterceptor
import com.namvox.weather_app.repositories.WeatherRepo
import com.namvox.weather_app.repositories.WeatherRepoImpl
import okhttp3.OkHttpClient
import org.koin.core.context.startKoin
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        val appModule = module {
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
            single {
                val retrofit: Retrofit = get()
                retrofit.create(Api::class.java)
            }
            single {
                val api: Api = get()
                WeatherRepoImpl(api)
            }bind WeatherRepo::class
        }

        startKoin {
            modules(appModule)
        }
    }
}
