package com.example.weatherapp.networking

import com.example.weatherapp.model.WeatherData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiManager {

    private val apiService: ApiService

    init {

        val retrofit = Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

    }

    fun getGeneralData(apiCallBack : MyApiCallBack<WeatherData>){

        apiService.getGeneralWeatherData().enqueue(object  : Callback<WeatherData> {
            override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {

                apiCallBack.onSuccess(response.body()!!)

            }

            override fun onFailure(call: Call<WeatherData>, t: Throwable) {

                apiCallBack.onFailure(t.message.toString())

            }

        })

    }

    interface MyApiCallBack<T>{

        fun onSuccess(data : T)

        fun onFailure(error : String)

    }

}