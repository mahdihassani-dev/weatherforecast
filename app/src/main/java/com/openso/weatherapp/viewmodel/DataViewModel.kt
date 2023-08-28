package com.openso.weatherapp.viewmodel

import com.openso.weatherapp.model.MainRepository
import com.openso.weatherapp.model.WeatherData
import io.reactivex.Single

class DataViewModel(
    private val mainRepository: MainRepository
) {

    suspend fun getDailyWeatherData(location: String): WeatherData {

        return mainRepository
            .getWeatherData(location)

    }

}