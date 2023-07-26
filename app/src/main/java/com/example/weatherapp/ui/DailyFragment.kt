package com.example.weatherapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherapp.databinding.FragmentDailyBinding
import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.weatherapp.utils.AxisLineFormatter
import com.example.weatherapp.R
import com.example.weatherapp.model.WeatherData
import com.example.weatherapp.networking.ApiManager
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
import java.util.Calendar
import java.util.TimeZone


class DailyFragment : Fragment() {


    lateinit var binding: FragmentDailyBinding

    private val apiManager = ApiManager()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDailyBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFiveHourLater()
        setUpBarChart()
        setGeneralInfo()


    }

    private fun setGeneralInfo() {
        apiManager.getGeneralData(object : ApiManager.MyApiCallBack<WeatherData> {
            override fun onSuccess(data: WeatherData) {
                setWindData(data)
                setRainChanceData(data)
                setPressureData(data)
                setUvIndexData(data)
                setHourlyForecast(data)
                setUpLineChart(data)

            }

            override fun onFailure(error: String) {

                Toast.makeText(this@DailyFragment.requireContext(), error, Toast.LENGTH_SHORT)
                    .show()

            }
        })
    }
    private fun setWindData(data: WeatherData) {

        binding.itemWindSpeed.txtWindSpeed.text =
            data.days[0].hours[getHourNow()].windspeed.toString() + " km/h"

        val compareWindSpeed =
            data.days[0].hours[getHourNow()].windspeed.toBigDecimal() - data.days[0].hours[getPreviousHour()].windspeed.toBigDecimal()

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
            data.days[0].hours[getHourNow()].precipprob.toString() + "%"

        val compareRainChance =
            data.days[0].hours[getHourNow()].precipprob.toBigDecimal() - data.days[0].hours[getPreviousHour()].precipprob.toBigDecimal()

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
            data.days[0].hours[getHourNow()].pressure.toString() + " hpa"

        val comparePressure =
            data.days[0].hours[getHourNow()].pressure.toBigDecimal() - data.days[0].hours[getPreviousHour()].pressure.toBigDecimal()

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

        binding.itemUvIndex.txtUvIndex.text = data.days[0].hours[getHourNow()].uvindex.toString()

        val compareUvIndex =
            data.days[0].hours[getHourNow()].uvindex.toBigDecimal() - data.days[0].hours[getPreviousHour()].uvindex.toBigDecimal()

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
    private fun setHourlyForecast(data: WeatherData){

        setIconsAndTemps(data, getHourNow(), binding.itemHourlyForecast.imgNowForecast,binding.itemHourlyForecast.tempNowForecast)
        setIconsAndTemps(data, get5HourAfter()[0], binding.itemHourlyForecast.img2Forecast, binding.itemHourlyForecast.temp2Forecast)
        setIconsAndTemps(data, get5HourAfter()[1], binding.itemHourlyForecast.img3Forecast, binding.itemHourlyForecast.temp3Forecast)
        setIconsAndTemps(data, get5HourAfter()[2], binding.itemHourlyForecast.img4Forecast, binding.itemHourlyForecast.temp4Forecast)
        setIconsAndTemps(data, get5HourAfter()[3], binding.itemHourlyForecast.img5Forecast, binding.itemHourlyForecast.temp5Forecast)
        setIconsAndTemps(data, get5HourAfter()[4], binding.itemHourlyForecast.img6Forecast, binding.itemHourlyForecast.temp6Forecast)



    }
    private fun setIconsAndTemps(data: WeatherData, i:Int, imgIcon : ImageView, txtTemp : TextView){

        when (data.days[0].hours[i].icon) {

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

        txtTemp.text = data.days[0].hours[i].temp.toString()

    }
    private fun getHourNow(): Int {

        val rightNow = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tehran"))

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

        getAmOrPm(binding.itemHourlyForecast.time2Forecast ,binding.itemHourlyForecast.hour2Forecast)
        getAmOrPm(binding.itemHourlyForecast.time3Forecast ,binding.itemHourlyForecast.hour3Forecast)
        getAmOrPm(binding.itemHourlyForecast.time4Forecast ,binding.itemHourlyForecast.hour4Forecast)
        getAmOrPm(binding.itemHourlyForecast.time5Forecast ,binding.itemHourlyForecast.hour5Forecast)
        getAmOrPm(binding.itemHourlyForecast.time6Forecast ,binding.itemHourlyForecast.hour6Forecast)

    }
    private fun get5HourAfter(): ArrayList<Int> {

        val fiveHourAfter = arrayListOf<Int>()

        for (i in 1..5) {

            var hour = getHourNow() + i

            if (hour == 24) {
                hour = 0
            }

            fiveHourAfter.add(hour)

        }

        return fiveHourAfter

    }
    private fun getAmOrPm(tvTime: TextView, tvHour: TextView) {

        if (tvHour.text.toString().toInt() <= 12){
            tvTime.text = "Am"
        }else{
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
    private fun setUpBarChart() {

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

            val values = arrayOf("7:00", "8:00", "9:00", "10:00")
            xAxis.valueFormatter = (object : ValueFormatter() {

                override fun getFormattedValue(value: Float): String {
                    return values[value.toInt()]
                }

            })

            xAxis.setLabelCount(4)

            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.textSize = 12f

            setDataToBarChart()

            animateY(2000)
        }

    }
    private fun barChartData(): ArrayList<BarEntry> {
        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(0f, 27f))
        entries.add(BarEntry(1f, 45f))
        entries.add(BarEntry(2f, 65f))
        entries.add(BarEntry(3f, 77f))

        return entries
    }
    private fun setDataToBarChart() {

        val barDataSet = BarDataSet(barChartData(), "Bar Data Set")
        binding.itemBarChart.hourlyBarChart.setDrawBarShadow(true)
        barDataSet.barShadowColor = Color.argb(40, 150, 150, 150)
        barDataSet.color = ContextCompat.getColor(requireContext(), R.color.purple)
        barDataSet.valueTextSize = 12f
        barDataSet.valueFormatter = (object : ValueFormatter() {

            override fun getFormattedValue(value: Float): String {
                return super.getFormattedValue(value).toDouble().toInt().toString() + "%"
            }

        })

        val data = BarData(barDataSet)

        data.barWidth = 0.9f

        binding.itemBarChart.hourlyBarChart.data = data
        binding.itemBarChart.hourlyBarChart.invalidate()

    }


}