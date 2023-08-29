package com.openso.weatherapp.di

import com.openso.weatherapp.model.ApiService
import com.openso.weatherapp.model.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitBuilder : NetworkProvider {


    override fun provideNetwork(): ApiService {

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)

    }
}