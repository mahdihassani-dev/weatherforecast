package com.example.weatherapp.networking

import com.example.weatherapp.model.WeatherData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("{location}/")
    fun getGeneralWeatherData(
        @Path("location") location:String = "Rasht",
        @Query("unitGroup") unitGroup: String = "metric",
        @Query("key") key:String = API_KEY

    ) : Call<WeatherData>


}