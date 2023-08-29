package com.openso.weatherapp.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val myModule = module {
            factory<NetworkProvider> { RetrofitBuilder() }

        }

        startKoin {

            androidContext(this@MyApp)
            modules(myModule)

        }



    }

}