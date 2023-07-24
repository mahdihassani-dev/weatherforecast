package com.example.weatherapp.utils

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class AxisLineFormatter : IndexAxisValueFormatter() {

    private var items = arrayListOf("Mon", "Tue", "Wen", "Thu", "Fri", "Sat", "Sun")

    override fun getAxisLabel(value: Float, axis: AxisBase?): String? {
        val index = value.toInt()
        return if (index < items.size) {
            items[index]
        } else {
            null
        }
    }
}