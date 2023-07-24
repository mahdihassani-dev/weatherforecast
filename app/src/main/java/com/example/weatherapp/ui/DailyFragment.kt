package com.example.weatherapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherapp.databinding.FragmentDailyBinding
import android.graphics.Color
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.weatherapp.utils.AxisLineFormatter
import com.example.weatherapp.R
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


class DailyFragment : Fragment() {


    lateinit var binding: FragmentDailyBinding

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

        setUpLineChart()
        setUpBarChart()

    }

    private fun setUpLineChart() {

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
            axisLeft.axisMaximum = 10F
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

        setDataToLineChart()
    }
    private fun lineChartData(): ArrayList<Entry> {
        val temps = ArrayList<Entry>()
        temps.add(Entry(0f, -7f))
        temps.add(Entry(1f, -2f))
        temps.add(Entry(2f, -2f))
        temps.add(Entry(3f, 1f))
        temps.add(Entry(4f, 1f))
        temps.add(Entry(5f, -1f))
        temps.add(Entry(6f, 0f))
        return temps
    }
    private fun setDataToLineChart() {

        val weekTemps = LineDataSet(lineChartData(), "")
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

            val values = arrayOf("10 PM", "9 PM", "8 PM", "7 PM")
            xAxis.valueFormatter = (object : ValueFormatter() {

                override fun getFormattedValue(value: Float): String {
                    return values[value.toInt()]
                }

            })


            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.textSize = 12f

            setDataToBarChart()

            animateY(2000)
        }

    }
    private fun barChartData() : ArrayList<BarEntry>{
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