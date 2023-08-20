package com.openso.weatherapp.viewmodel

import com.openso.weatherapp.model.MainRepository
import com.openso.weatherapp.model.WeatherData
import io.reactivex.Single

class DataViewModel(
    private val mainRepository: MainRepository
) {

    fun getDailyWeatherData(location : String) : Single<WeatherData> {

        return mainRepository
            .getWeatherData(location)

    }

}