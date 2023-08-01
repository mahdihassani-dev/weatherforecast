package com.openso.weatherapp.utils

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.Date

class AxisLineFormatter : IndexAxisValueFormatter() {

    private var items = arrayListOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    override fun getAxisLabel(value: Float, axis: AxisBase?): String? {
        val index = value.toInt()
        return if (index < items.size) {
            getWeekDay()[index]
        } else {
            null
        }
    }

    private fun getToday(): String {

        val sdf = SimpleDateFormat("EEE")
        val d = Date()

        return sdf.format(d)

    }

    private fun getWeekDay() : ArrayList<String>{

        var first = items.indexOf(getToday())

        val weekDay = arrayListOf<String>()
        weekDay.add(items[first])

        for (i in 0..5){

            if(first == 6){
                first = 0
            }else{
                first += 1
            }

            weekDay.add(items[first])

        }

        return weekDay

    }
}