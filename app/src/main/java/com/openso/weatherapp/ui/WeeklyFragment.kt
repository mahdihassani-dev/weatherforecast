package com.openso.weatherapp.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.openso.weatherapp.R
import com.openso.weatherapp.databinding.FragmentWeeklyBinding
import com.openso.weatherapp.model.WeatherData
import com.openso.weatherapp.model.WeeklyItemData
import com.openso.weatherapp.networking.ApiManager
import com.openso.weatherapp.utils.WeeklyRecyclerAdapter
import java.text.SimpleDateFormat
import java.util.Date


class WeeklyFragment : Fragment() {

    private var sharedPreferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    private var location: String? = null

    lateinit var binding: FragmentWeeklyBinding

    private val apiManager = ApiManager()

    private val dataSet = arrayListOf<WeeklyItemData>()

    private val items =
        arrayListOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    private var dataLoadedCallback: DataLoaded? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentWeeklyBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSharedPref()

        dataLoadedCallback = activity as DataLoaded


        showItemsBeforeResponse()
        setWeeklyData(location!!)

    }

    private fun showItemsBeforeResponse() {

        dataSet.add(WeeklyItemData("", "", 0, "", ""))
        dataSet.add(WeeklyItemData("", "", 0, "", ""))
        dataSet.add(WeeklyItemData("", "", 0, "", ""))
        dataSet.add(WeeklyItemData("", "", 0, "", ""))
        dataSet.add(WeeklyItemData("", "", 0, "", ""))
        dataSet.add(WeeklyItemData("", "", 0, "", ""))
        dataSet.add(WeeklyItemData("", "", 0, "", ""))

        val adapter = WeeklyRecyclerAdapter(dataSet, false)

        binding.weeklyRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.weeklyRecyclerView.adapter = adapter


    }


    private fun getToday(): String {

        val sdf = SimpleDateFormat("EEEE")
        val d = Date()

        return sdf.format(d)

    }

    private fun getWeekDays(): ArrayList<String> {

        var first = items.indexOf(getToday())

        val weekDay = arrayListOf<String>()
        weekDay.add("Today")

        for (i in 0..5) {

            if (first == 6) {
                first = 0
            } else {
                first += 1
            }

            weekDay.add(items[first])

        }

        return weekDay


    }


    private fun setWeeklyData(location: String) {

        apiManager.getGeneralData(location, object : ApiManager.MyApiCallBack<WeatherData> {
            override fun onSuccess(data: WeatherData) {

                dataSet.clear()

                dataSet.add(setWeeklyItems(data, 0))
                dataSet.add(setWeeklyItems(data, 1))
                dataSet.add(setWeeklyItems(data, 2))
                dataSet.add(setWeeklyItems(data, 3))
                dataSet.add(setWeeklyItems(data, 4))
                dataSet.add(setWeeklyItems(data, 5))
                dataSet.add(setWeeklyItems(data, 6))

                val adapter = WeeklyRecyclerAdapter(dataSet, true)

                binding.weeklyRecyclerView.layoutManager = LinearLayoutManager(context)
                binding.weeklyRecyclerView.adapter = adapter

                dataLoadedCallback!!.loaded()


            }

            override fun onFailure(error: String) {

                Toast.makeText(context, error, Toast.LENGTH_LONG).show()

            }

        })

    }

    private fun setWeeklyItems(data: WeatherData, dayPos: Int): WeeklyItemData {

        return WeeklyItemData(
            getWeekDays()[dayPos],
            data.days[dayPos].conditions,
            setIcon(data, dayPos),
            data.days[dayPos].tempmax.toString(),
            data.days[dayPos].tempmin.toString()
        )
    }

    private fun setIcon(data: WeatherData, dayPos: Int): Int {

        var imgIconId = 0

        when (data.days[dayPos].icon) {

            "snow" -> {
                imgIconId = R.drawable.snow
            }

            "rain" -> {
                imgIconId = R.drawable.rain
            }

            "fog" -> {
                imgIconId = R.drawable.fog
            }

            "wind" -> {
                imgIconId = R.drawable.wind
            }

            "cloudy" -> {
                imgIconId = R.drawable.cloudy
            }

            "partly-cloudy-day" -> {
                imgIconId = R.drawable.partly_cloudy_day
            }

            "partly-cloudy-night" -> {
                imgIconId = R.drawable.partly_cloudy_night
            }

            "clear-day" -> {
                imgIconId = R.drawable.clear_day
            }

            "clear-night" -> {
                imgIconId = R.drawable.clear_night
            }

        }

        return imgIconId
    }

    private fun setSharedPref() {

        sharedPreferences = requireActivity().getSharedPreferences("ShPref", Context.MODE_PRIVATE)
        editor = sharedPreferences!!.edit()
        location = sharedPreferences!!.getString(CACHE_LOCATION, "Yazd")

    }

    interface DataLoaded {
        fun loaded()

    }


}