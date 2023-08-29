package com.openso.weatherapp.di

import com.openso.weatherapp.model.ApiService

interface NetworkProvider {

    fun provideNetwork() : ApiService

}