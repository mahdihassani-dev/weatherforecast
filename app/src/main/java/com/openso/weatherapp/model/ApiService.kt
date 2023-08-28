package com.openso.weatherapp.model

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("{location}/")
    suspend fun getGeneralWeatherData(
        @Path("location") location:String ,
        @Query("unitGroup") unitGroup: String = "metric",
        @Query("key") key:String = API_KEY

    ) : WeatherData


}