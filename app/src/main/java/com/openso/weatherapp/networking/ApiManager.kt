package com.openso.weatherapp.networking

import android.util.Log
import com.openso.weatherapp.model.WeatherData
import io.reactivex.Scheduler
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiManager {

    private val apiService: ApiService
    private lateinit var disposable: Disposable

    init {

        val retrofit = Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

    }

    fun getGeneralData(location : String ,apiCallBack : MyApiCallBack<WeatherData>){

        apiService
            .getGeneralWeatherData(location)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( object : SingleObserver<WeatherData>{
                override fun onSubscribe(d: Disposable) {
                    disposable = d
                }

                override fun onError(e: Throwable) {
                    Log.i("testLog", e.message!!)
                    apiCallBack.onFailure(e.message!!)
                }

                override fun onSuccess(t: WeatherData) {

                    apiCallBack.onSuccess(t)

                }


            })

    }

    interface MyApiCallBack<T>{

        fun onSuccess(data : T)

        fun onFailure(error : String)

    }

}