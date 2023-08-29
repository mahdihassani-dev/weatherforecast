package com.openso.weatherapp.model

import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainRepository(private val apiService: ApiService) {

//    private val apiService: ApiService
//
//    init {
//
//        val retrofit = Retrofit
//            .Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .build()
//
//        apiService = retrofit.create(ApiService::class.java)
//
//    }

    suspend fun getWeatherData(location: String) : WeatherData {
        return apiService.getGeneralWeatherData(location)
    }




}