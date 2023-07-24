package com.example.weatherapp.utils

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter


class AxisBarFormatter(private val values: ArrayList<String>) : ValueFormatter() {


    override fun getFormattedValue(value: Float, axis: AxisBase?): String? {

        return values[value.toInt()]
    }
}