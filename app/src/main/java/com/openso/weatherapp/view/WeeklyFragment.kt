package com.openso.weatherapp.view

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.openso.weatherapp.di.NetworkProvider
import com.openso.weatherapp.R
import com.openso.weatherapp.databinding.FragmentWeeklyBinding
import com.openso.weatherapp.model.WeatherData
import com.openso.weatherapp.model.WeeklyItemData
import com.openso.weatherapp.model.MainRepository
import com.openso.weatherapp.utils.WeeklyRecyclerAdapter
import com.openso.weatherapp.utils.showToast
import com.openso.weatherapp.viewmodel.DataViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date


class WeeklyFragment : Fragment() {

    private var sharedPreferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    private var location: String? = null

    private lateinit var binding: FragmentWeeklyBinding

    private val dataSet = arrayListOf<WeeklyItemData>()

    private val items =
        arrayListOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    private var dataLoadedCallback: DataLoaded? = null

    private lateinit var dataViewModel: DataViewModel

    private val apiServiceProvider : NetworkProvider by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentWeeklyBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewModel = DataViewModel(MainRepository(apiServiceProvider.provideNetwork()))

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

        GlobalScope.launch() {

            try {
                val data = dataViewModel.getDailyWeatherData(location)

                withContext(Dispatchers.Main) {

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
            }catch (ex : Exception){

                withContext(Dispatchers.IO){
                    requireContext().showToast(ex.message ?: "null")

                    AlertDialog.Builder(context)
                        .setTitle("There is a problem")
                        .setMessage("please refresh to get data")
                        .setPositiveButton(
                            "Refresh"
                        ) { p0, p1 ->
                            setWeeklyData(location)
                            p0.dismiss()
                        }
                        .setCancelable(false)
                        .show()
                }



            }

        }


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