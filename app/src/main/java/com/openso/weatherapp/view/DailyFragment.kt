package com.openso.weatherapp.view

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.openso.weatherapp.R
import com.openso.weatherapp.databinding.FragmentDailyBinding
import com.openso.weatherapp.model.WeatherData
import com.openso.weatherapp.model.MainRepository
import com.openso.weatherapp.utils.AxisLineFormatter
import com.openso.weatherapp.utils.showToast
import com.openso.weatherapp.utils.synchronization
import com.openso.weatherapp.viewmodel.DataViewModel
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.Calendar
import java.util.TimeZone

private const val TAG = "TestDaily"

class DailyFragment : Fragment() {

    private var sharedPreferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    private lateinit var binding: FragmentDailyBinding
    private lateinit var mData: WeatherData

    private var location: String? = null
    private var sendDataCallBack: SendWeatherData? = null

    private var infoCode = 0

    private lateinit var dailyViewModel: DataViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDailyBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dailyViewModel = DataViewModel(MainRepository())

        setSharedPref()

        infoCode = arguments?.getInt(CHIP_CHANGE) ?: 0

        sendDataCallBack = activity as SendWeatherData
        setGeneralInfo(location!!)

    }


    private fun setSharedPref() {

        sharedPreferences = requireActivity().getSharedPreferences("ShPref", Context.MODE_PRIVATE)
        editor = sharedPreferences!!.edit()
        location = sharedPreferences!!.getString(CACHE_LOCATION, "Yazd")

    }

    private fun setGeneralInfo(location: String) {

        GlobalScope.launch(Dispatchers.IO) {


            try {
                val data = dailyViewModel.getDailyWeatherData(location)
                mData = data

                withContext(Dispatchers.Main) {

                    sendDataCallBack!!.sendWeatherData(data)
                    setWindData(data)
                    setRainChanceData(data)
                    setPressureData(data)
                    setUvIndexData(data)
                    setHourlyForecast(data)
                    setUpLineChart(data)
                    setUpBarChart(data)
                    setSunState(data)
                    setFiveHourLater()

                    handleShimmers()
                    Log.i("check", Thread.currentThread().name)

                }

            } catch (ex : Exception){

                withContext(Dispatchers.Main){

                    requireContext().showToast(ex.message ?: "null")

                    AlertDialog.Builder(context)
                        .setTitle("There is a problem")
                        .setMessage("please refresh to get data")
                        .setPositiveButton(
                            "Refresh"
                        ) { p0, p1 ->
                            setGeneralInfo(location)
                            p0.dismiss()
                        }
                        .setCancelable(false)
                        .show()
                }


            }



        }

    }

    interface SendWeatherData {
        fun sendWeatherData(data: WeatherData)

    }


    private fun handleShimmers() {

        binding.itemWindSpeed.shimWindspeed.hideShimmer()
        binding.itemRainChance.shimRainChance.hideShimmer()
        binding.itemPressure.shimPressure.hideShimmer()
        binding.itemUvIndex.shimUvIndex.hideShimmer()
        binding.itemHourlyForecast.shimHourlyForecast.hideShimmer()
        binding.itemLineChart.shimDayForeCast.hideShimmer()
        binding.itemBarChart.shimRainChanceChart.hideShimmer()
        binding.itemSunrise.shimSunrise.hideShimmer()
        binding.itemSunset.shimSunset.hideShimmer()

    }

    private fun setWindData(data: WeatherData) {

        binding.itemWindSpeed.txtWindSpeed.text =
            data.days[infoCode].hours[getHourNow()].windspeed.toString() + " km/h"

        val compareWindSpeed =
            data.days[infoCode].hours[getHourNow()].windspeed.toBigDecimal() - data.days[infoCode].hours[getPreviousHour()].windspeed.toBigDecimal()

        if (compareWindSpeed.toDouble() > 0) {
            binding.itemWindSpeed.txtUpOrDownWindSpeed.text = "▲"
        } else if (compareWindSpeed.toDouble() == 0.0) {
            binding.itemWindSpeed.txtUpOrDownWindSpeed.text = ""
        } else {
            binding.itemWindSpeed.txtUpOrDownWindSpeed.text = "▼"
        }

        val absoluteCompareWindSpeed = compareWindSpeed.abs()
        binding.itemWindSpeed.windSpeedChange.text = "$absoluteCompareWindSpeed"


    }

    private fun setRainChanceData(data: WeatherData) {

        binding.itemRainChance.txtRainChance.text =
            data.days[infoCode].hours[getHourNow()].precipprob.toString() + "%"

        val compareRainChance =
            data.days[infoCode].hours[getHourNow()].precipprob.toBigDecimal() - data.days[infoCode].hours[getPreviousHour()].precipprob.toBigDecimal()

        if (compareRainChance.toDouble() > 0) {
            binding.itemRainChance.txtUpOrDownRainChance.text = "▲"
        } else if (compareRainChance.toDouble() == 0.0) {
            binding.itemRainChance.txtUpOrDownRainChance.text = ""
        } else {
            binding.itemRainChance.txtUpOrDownRainChance.text = "▼"
        }

        val absoluteCompareRainChance = compareRainChance.abs()
        binding.itemRainChance.rainChanceChange.text = "$absoluteCompareRainChance"

    }

    private fun setPressureData(data: WeatherData) {

        binding.itemPressure.txtPressure.text =
            data.days[infoCode].hours[getHourNow()].pressure.toString() + " hpa"

        val comparePressure =
            data.days[infoCode].hours[getHourNow()].pressure.toBigDecimal() - data.days[infoCode].hours[getPreviousHour()].pressure.toBigDecimal()

        if (comparePressure.toDouble() > 0) {
            binding.itemPressure.txtUpOrDownPressure.text = "▲"
        } else if (comparePressure.toDouble() == 0.0) {
            binding.itemPressure.txtUpOrDownPressure.text = ""
        } else {
            binding.itemPressure.txtUpOrDownPressure.text = "▼"
        }

        val absoluteComparePressure = comparePressure.abs()
        binding.itemPressure.pressureChange.text = "$absoluteComparePressure"


    }

    private fun setUvIndexData(data: WeatherData) {

        binding.itemUvIndex.txtUvIndex.text =
            data.days[infoCode].hours[getHourNow()].uvindex.toString()

        val compareUvIndex =
            data.days[infoCode].hours[getHourNow()].uvindex.toBigDecimal() - data.days[infoCode].hours[getPreviousHour()].uvindex.toBigDecimal()

        if (compareUvIndex.toDouble() > 0) {
            binding.itemUvIndex.txtUpOrDownUvIndex.text = "▲"
        } else if (compareUvIndex.toDouble() == 0.0) {
            binding.itemUvIndex.txtUpOrDownUvIndex.text = ""
        } else {
            binding.itemUvIndex.txtUpOrDownUvIndex.text = "▼"
        }

        val absoluteCompareUvIndex = compareUvIndex.abs()
        binding.itemUvIndex.uvIndexChange.text = "$absoluteCompareUvIndex"


    }

    private fun setHourlyForecast(data: WeatherData) {

        setIconsAndTemps(
            data,
            getHourNow(),
            binding.itemHourlyForecast.imgNowForecast,
            binding.itemHourlyForecast.tempNowForecast
        )
        setIconsAndTemps(
            data,
            get5HourAfter()[0],
            binding.itemHourlyForecast.img2Forecast,
            binding.itemHourlyForecast.temp2Forecast
        )
        setIconsAndTemps(
            data,
            get5HourAfter()[1],
            binding.itemHourlyForecast.img3Forecast,
            binding.itemHourlyForecast.temp3Forecast
        )
        setIconsAndTemps(
            data,
            get5HourAfter()[2],
            binding.itemHourlyForecast.img4Forecast,
            binding.itemHourlyForecast.temp4Forecast
        )
        setIconsAndTemps(
            data,
            get5HourAfter()[3],
            binding.itemHourlyForecast.img5Forecast,
            binding.itemHourlyForecast.temp5Forecast
        )
        setIconsAndTemps(
            data,
            get5HourAfter()[4],
            binding.itemHourlyForecast.img6Forecast,
            binding.itemHourlyForecast.temp6Forecast
        )


    }

    private fun setIconsAndTemps(data: WeatherData, i: Int, imgIcon: ImageView, txtTemp: TextView) {

        when (data.days[infoCode].hours[i].icon) {

            "snow" -> {
                imgIcon.setImageResource(R.drawable.snow)
            }

            "rain" -> {
                imgIcon.setImageResource(R.drawable.rain)
            }

            "fog" -> {
                imgIcon.setImageResource(R.drawable.fog)
            }

            "wind" -> {
                imgIcon.setImageResource(R.drawable.wind)
            }

            "cloudy" -> {
                imgIcon.setImageResource(R.drawable.cloudy)
            }

            "partly-cloudy-day" -> {
                imgIcon.setImageResource(R.drawable.partly_cloudy_day)
            }

            "partly-cloudy-night" -> {
                imgIcon.setImageResource(R.drawable.partly_cloudy_night)
            }

            "clear-day" -> {
                imgIcon.setImageResource(R.drawable.clear_day)
            }

            "clear-night" -> {
                imgIcon.setImageResource(R.drawable.clear_night)
            }


        }

        txtTemp.text = data.days[infoCode].hours[i].temp.toString()

    }

    private fun getHourNow(): Int {

        val rightNow = Calendar.getInstance(TimeZone.getTimeZone(mData.timezone))

        return rightNow.get(Calendar.HOUR_OF_DAY)

    }

    private fun getPreviousHour(): Int {

        val previousHour: Int = if (getHourNow() == 0) {
            23
        } else {
            getHourNow() - 1
        }

        return previousHour
    }

    private fun setFiveHourLater() {

        binding.itemHourlyForecast.hour2Forecast.text = get5HourAfter()[0].toString()
        binding.itemHourlyForecast.hour3Forecast.text = get5HourAfter()[1].toString()
        binding.itemHourlyForecast.hour4Forecast.text = get5HourAfter()[2].toString()
        binding.itemHourlyForecast.hour5Forecast.text = get5HourAfter()[3].toString()
        binding.itemHourlyForecast.hour6Forecast.text = get5HourAfter()[4].toString()

        getAmOrPm(
            binding.itemHourlyForecast.time2Forecast,
            binding.itemHourlyForecast.hour2Forecast
        )
        getAmOrPm(
            binding.itemHourlyForecast.time3Forecast,
            binding.itemHourlyForecast.hour3Forecast
        )
        getAmOrPm(
            binding.itemHourlyForecast.time4Forecast,
            binding.itemHourlyForecast.hour4Forecast
        )
        getAmOrPm(
            binding.itemHourlyForecast.time5Forecast,
            binding.itemHourlyForecast.hour5Forecast
        )
        getAmOrPm(
            binding.itemHourlyForecast.time6Forecast,
            binding.itemHourlyForecast.hour6Forecast
        )

    }

    private fun get5HourAfter(): ArrayList<Int> {

        val fiveHourAfter = arrayListOf<Int>()
        var hour = getHourNow()

        for (i in 1..5) {

            hour += 1

            if (hour == 24) {
                hour = 0
            }

            fiveHourAfter.add(hour)

        }

        return fiveHourAfter

    }

    private fun getAmOrPm(tvTime: TextView, tvHour: TextView) {

        if (tvHour.text.toString().toInt() <= 12) {
            tvTime.text = "Am"
        } else {
            tvTime.text = "Pm"
        }

    }

    private fun setUpLineChart(data: WeatherData) {

        with(binding.itemLineChart.dayForecastLineChart) {
            animateX(1200, Easing.EaseInSine)
            description.isEnabled = false

            xAxis.setDrawGridLines(false)
            xAxis.setDrawAxisLine(false)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1F
            xAxis.yOffset = 4F
            xAxis.valueFormatter = AxisLineFormatter()
            xAxis.textColor = ContextCompat.getColor(context, R.color.black)

            axisRight.isEnabled = false
            extraRightOffset = 30f

            axisLeft.granularity = 10F
            axisLeft.axisMaximum = 50F
            axisLeft.axisMinimum = -10F
            axisLeft.xOffset = 20f
            axisLeft.textSize = 11f

            legend.orientation = Legend.LegendOrientation.VERTICAL
            legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            legend.textSize = 15F
            legend.form = Legend.LegendForm.NONE

            setDrawBorders(false)
            setDrawGridBackground(false)
            setPinchZoom(false)
            isDoubleTapToZoomEnabled = false
            isDragDecelerationEnabled = false

            marker = object : MarkerView(context, R.layout.line_chart_marker) {
                override fun refreshContent(e: Entry, highlight: Highlight) {
                    (findViewById<View>(R.id.tvContent) as TextView).text = "${e.y}°"
                }
            }

        }

        setDataToLineChart(data)
    }

    private fun lineChartData(data: WeatherData): ArrayList<Entry> {

        val temps = ArrayList<Entry>()
        temps.add(Entry(0f, data.days[0].temp.toFloat()))
        temps.add(Entry(1f, data.days[1].temp.toFloat()))
        temps.add(Entry(2f, data.days[2].temp.toFloat()))
        temps.add(Entry(3f, data.days[3].temp.toFloat()))
        temps.add(Entry(4f, data.days[4].temp.toFloat()))
        temps.add(Entry(5f, data.days[5].temp.toFloat()))
        temps.add(Entry(6f, data.days[6].temp.toFloat()))
        return temps
    }

    private fun setDataToLineChart(data: WeatherData) {

        val weekTemps = LineDataSet(lineChartData(data), "")
        weekTemps.lineWidth = 2f
        weekTemps.valueTextSize = 16f
        weekTemps.mode = LineDataSet.Mode.CUBIC_BEZIER
        weekTemps.color = ContextCompat.getColor(requireContext(), R.color.purple)
        weekTemps.valueTextColor = ContextCompat.getColor(requireContext(), R.color.black)
        weekTemps.setDrawValues(false)
        weekTemps.setDrawCircles(false)
        weekTemps.highLightColor = ContextCompat.getColor(requireContext(), R.color.black_low)
        weekTemps.setDrawHorizontalHighlightIndicator(false)
        weekTemps.setDrawHighlightIndicators(true)
        weekTemps.enableDashedHighlightLine(20f, 10f, 0f)
        weekTemps.setDrawValues(false)
        weekTemps.disableDashedLine()

        val dataSet = ArrayList<ILineDataSet>()
        dataSet.add(weekTemps)

        val lineData = LineData(dataSet)
        binding.itemLineChart.dayForecastLineChart.data = lineData

        binding.itemLineChart.dayForecastLineChart.invalidate()

    }

    private fun setUpBarChart(data: WeatherData) {

        with(binding.itemBarChart.hourlyBarChart) {
            setDrawBarShadow(false)
            legend.isEnabled = false
            setPinchZoom(false)
            description.isEnabled = false

            xAxis.setDrawGridLines(false)
            xAxis.setDrawAxisLine(false)

            axisLeft.axisMaximum = 100f
            axisLeft.axisMinimum = 0f
            axisLeft.isEnabled = true
            axisLeft.yOffset = 10f

            axisRight.setDrawGridLines(false)
            axisRight.isEnabled = false

            setPinchZoom(false)
            isDoubleTapToZoomEnabled = false
            setTouchEnabled(false)

            val values = arrayOf(
                getHourNow().toString(),
                get5HourAfter()[0].toString(),
                get5HourAfter()[1].toString(),
                get5HourAfter()[2].toString()
            )

            xAxis.valueFormatter = (object : ValueFormatter() {

                override fun getFormattedValue(value: Float): String {
                    return values[value.toInt()] + ":00"
                }

            })

            xAxis.labelCount = 4

            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.textSize = 12f

            setDataToBarChart(data)

            animateY(2000)
        }

    }

    private fun barChartData(data: WeatherData): ArrayList<BarEntry> {

        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(0f, data.days[infoCode].hours[getHourNow()].precipprob.toFloat()))
        entries.add(
            BarEntry(
                1f,
                data.days[infoCode].hours[get5HourAfter()[0]].precipprob.toFloat()
            )
        )
        entries.add(
            BarEntry(
                2f,
                data.days[infoCode].hours[get5HourAfter()[1]].precipprob.toFloat()
            )
        )
        entries.add(
            BarEntry(
                3f,
                data.days[infoCode].hours[get5HourAfter()[2]].precipprob.toFloat()
            )
        )

        return entries
    }

    private fun setDataToBarChart(data: WeatherData) {

        val barDataSet = BarDataSet(barChartData(data), "Bar Data Set")
        binding.itemBarChart.hourlyBarChart.setDrawBarShadow(true)
        barDataSet.barShadowColor = Color.argb(40, 150, 150, 150)
        barDataSet.color = ContextCompat.getColor(requireContext(), R.color.purple)
        barDataSet.valueTextSize = 12f

        barDataSet.valueFormatter = (object : ValueFormatter() {

            override fun getFormattedValue(value: Float): String {

                return if (super.getFormattedValue(value).toDouble().toInt() == 100) {
                    ""
                } else {
                    super.getFormattedValue(value).toString() + "%"
                }

            }
        })

        val data = BarData(barDataSet)

        data.barWidth = 0.9f

        binding.itemBarChart.hourlyBarChart.data = data
        binding.itemBarChart.hourlyBarChart.invalidate()

    }

    private fun setSunState(data: WeatherData) {

        binding.itemSunrise.txtSunrise.text = data.days[infoCode].sunrise + " AM"
        binding.itemSunset.txtSunset.text = data.days[infoCode].sunset + " PM"


    }

}
