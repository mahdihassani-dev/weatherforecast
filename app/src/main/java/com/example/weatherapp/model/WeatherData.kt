package com.example.weatherapp.model


import com.google.gson.annotations.SerializedName
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherData(
    @SerializedName("address")
    val address: String,
//    @SerializedName("alerts")
//    val alerts: List<Any>,
    @SerializedName("currentConditions")
    val currentConditions: CurrentConditions,
    @SerializedName("days")
    val days: List<Day>,
    @SerializedName("description")
    val description: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("queryCost")
    val queryCost: Int,
    @SerializedName("resolvedAddress")
    val resolvedAddress: String,
    @SerializedName("stations")
    val stations: Stations,
    @SerializedName("timezone")
    val timezone: String,
    @SerializedName("tzoffset")
    val tzoffset: Double
) : Parcelable {
    @Parcelize
    data class CurrentConditions(
        @SerializedName("cloudcover")
        val cloudcover: Double,
        @SerializedName("conditions")
        val conditions: String,
        @SerializedName("datetime")
        val datetime: String,
        @SerializedName("datetimeEpoch")
        val datetimeEpoch: Int,
        @SerializedName("dew")
        val dew: Double,
        @SerializedName("feelslike")
        val feelslike: Double,
        @SerializedName("humidity")
        val humidity: Double,
        @SerializedName("icon")
        val icon: String,
        @SerializedName("moonphase")
        val moonphase: Double,
//        @SerializedName("precip")
//        val precip: Any,
        @SerializedName("precipprob")
        val precipprob: Double,
//        @SerializedName("preciptype")
//        val preciptype: Any,
        @SerializedName("pressure")
        val pressure: Double,
        @SerializedName("snow")
        val snow: Double,
        @SerializedName("snowdepth")
        val snowdepth: Double,
        @SerializedName("solarenergy")
        val solarenergy: Double,
        @SerializedName("solarradiation")
        val solarradiation: Double,
        @SerializedName("source")
        val source: String,
        @SerializedName("stations")
        val stations: List<String>,
        @SerializedName("sunrise")
        val sunrise: String,
        @SerializedName("sunriseEpoch")
        val sunriseEpoch: Int,
        @SerializedName("sunset")
        val sunset: String,
        @SerializedName("sunsetEpoch")
        val sunsetEpoch: Int,
        @SerializedName("temp")
        val temp: Double,
        @SerializedName("uvindex")
        val uvindex: Double,
        @SerializedName("visibility")
        val visibility: Double,
        @SerializedName("winddir")
        val winddir: Double,
//        @SerializedName("windgust")
//        val windgust: Any,
        @SerializedName("windspeed")
        val windspeed: Double
    ) : Parcelable

    @Parcelize
    data class Day(
        @SerializedName("cloudcover")
        val cloudcover: Double,
        @SerializedName("conditions")
        val conditions: String,
        @SerializedName("datetime")
        val datetime: String,
        @SerializedName("datetimeEpoch")
        val datetimeEpoch: Int,
        @SerializedName("description")
        val description: String,
        @SerializedName("dew")
        val dew: Double,
        @SerializedName("feelslike")
        val feelslike: Double,
        @SerializedName("feelslikemax")
        val feelslikemax: Double,
        @SerializedName("feelslikemin")
        val feelslikemin: Double,
        @SerializedName("hours")
        val hours: List<Hour>,
        @SerializedName("humidity")
        val humidity: Double,
        @SerializedName("icon")
        val icon: String,
        @SerializedName("moonphase")
        val moonphase: Double,
        @SerializedName("precip")
        val precip: Double,
        @SerializedName("precipcover")
        val precipcover: Double,
        @SerializedName("precipprob")
        val precipprob: Double,
//        @SerializedName("preciptype")
//        val preciptype: Any,
        @SerializedName("pressure")
        val pressure: Double,
        @SerializedName("severerisk")
        val severerisk: Double,
        @SerializedName("snow")
        val snow: Double,
        @SerializedName("snowdepth")
        val snowdepth: Double,
        @SerializedName("solarenergy")
        val solarenergy: Double,
        @SerializedName("solarradiation")
        val solarradiation: Double,
        @SerializedName("source")
        val source: String,
        @SerializedName("stations")
        val stations: List<String>,
        @SerializedName("sunrise")
        val sunrise: String,
        @SerializedName("sunriseEpoch")
        val sunriseEpoch: Int,
        @SerializedName("sunset")
        val sunset: String,
        @SerializedName("sunsetEpoch")
        val sunsetEpoch: Int,
        @SerializedName("temp")
        val temp: Double,
        @SerializedName("tempmax")
        val tempmax: Double,
        @SerializedName("tempmin")
        val tempmin: Double,
        @SerializedName("uvindex")
        val uvindex: Double,
        @SerializedName("visibility")
        val visibility: Double,
        @SerializedName("winddir")
        val winddir: Double,
        @SerializedName("windgust")
        val windgust: Double,
        @SerializedName("windspeed")
        val windspeed: Double
    ) : Parcelable {
        @Parcelize
        data class Hour(
            @SerializedName("cloudcover")
            val cloudcover: Double,
            @SerializedName("conditions")
            val conditions: String,
            @SerializedName("datetime")
            val datetime: String,
            @SerializedName("datetimeEpoch")
            val datetimeEpoch: Int,
            @SerializedName("dew")
            val dew: Double,
            @SerializedName("feelslike")
            val feelslike: Double,
            @SerializedName("humidity")
            val humidity: Double,
            @SerializedName("icon")
            val icon: String,
            @SerializedName("precip")
            val precip: Double,
            @SerializedName("precipprob")
            val precipprob: Double,
//            @SerializedName("preciptype")
//            val preciptype: Any,
            @SerializedName("pressure")
            val pressure: Double,
            @SerializedName("severerisk")
            val severerisk: Double,
            @SerializedName("snow")
            val snow: Double,
            @SerializedName("snowdepth")
            val snowdepth: Double,
            @SerializedName("solarenergy")
            val solarenergy: Double,
            @SerializedName("solarradiation")
            val solarradiation: Double,
            @SerializedName("source")
            val source: String,
            @SerializedName("stations")
            val stations: List<String>,
            @SerializedName("temp")
            val temp: Double,
            @SerializedName("uvindex")
            val uvindex: Double,
            @SerializedName("visibility")
            val visibility: Double,
            @SerializedName("winddir")
            val winddir: Double,
            @SerializedName("windgust")
            val windgust: Double,
            @SerializedName("windspeed")
            val windspeed: Double
        ) : Parcelable
    }

    @Parcelize
    data class Stations(
        @SerializedName("OIYY")
        val oIYY: OIYY
    ) : Parcelable {
        @Parcelize
        data class OIYY(
            @SerializedName("contribution")
            val contribution: Double,
            @SerializedName("distance")
            val distance: Double,
            @SerializedName("id")
            val id: String,
            @SerializedName("latitude")
            val latitude: Double,
            @SerializedName("longitude")
            val longitude: Double,
            @SerializedName("name")
            val name: String,
            @SerializedName("quality")
            val quality: Int,
            @SerializedName("useCount")
            val useCount: Int
        ) : Parcelable
    }
}