package com.openso.weatherapp.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.openso.weatherapp.R
import com.openso.weatherapp.databinding.ActivityMainBinding
import com.openso.weatherapp.model.WeatherData

private const val TAG = "MainTest"
const val CACHE_LOCATION = "cache_loc"
const val CHIP_CHANGE = "chip_changes"

class MainActivity : AppCompatActivity() , MapFragmentDialog.GetLatLng, DailyFragment.SendWeatherData, WeeklyFragment.DataLoaded{

    lateinit var binding: ActivityMainBinding

    private var sharedPreferences: SharedPreferences ?= null
    private var editor: SharedPreferences.Editor ?= null
    private var infoCode = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSharedPref()
        handleFragmentMovements()

        binding.toolbar.setOnMenuItemClickListener {

            val mapFragment = MapFragmentDialog()
            mapFragment.show(supportFragmentManager, "my_fragment")

            true
        }

    }
    private fun setSharedPref(){

        sharedPreferences = this.getSharedPreferences("ShPref",Context.MODE_PRIVATE)
        editor = sharedPreferences!!.edit()

        if (sharedPreferences!!.getString(CACHE_LOCATION, "") == null){

            editor!!.putString(CACHE_LOCATION, "yazd")
            editor!!.apply()
            editor!!.commit()

        }

    }
    private fun setToolbarData(data: WeatherData, infoCode: Int) {


        binding.moduleToolbarInfo.tempTitle.text =
            data.days[infoCode].temp.toInt().toString() + "°"
        binding.moduleToolbarInfo.feelLikeTemp.text =
            "Feel Like " + data.days[infoCode].feelslike + "°"
        binding.toolbar.title = data.timezone

        when (data.days[infoCode].icon) {

            "snow" -> {
                binding.moduleToolbarInfo.imgStateTitle.setImageResource(R.drawable.snow)
            }

            "rain" -> {
                binding.moduleToolbarInfo.imgStateTitle.setImageResource(R.drawable.rain)
            }

            "fog" -> {
                binding.moduleToolbarInfo.imgStateTitle.setImageResource(R.drawable.fog)
            }

            "wind" -> {
                binding.moduleToolbarInfo.imgStateTitle.setImageResource(R.drawable.wind)
            }

            "cloudy" -> {
                binding.moduleToolbarInfo.imgStateTitle.setImageResource(R.drawable.cloudy)
            }

            "partly-cloudy-day" -> {
                binding.moduleToolbarInfo.imgStateTitle.setImageResource(R.drawable.partly_cloudy_day)
            }

            "partly-cloudy-night" -> {
                binding.moduleToolbarInfo.imgStateTitle.setImageResource(R.drawable.partly_cloudy_night)
            }

            "clear-day" -> {
                binding.moduleToolbarInfo.imgStateTitle.setImageResource(R.drawable.clear_day)
            }

            "clear-night" -> {
                binding.moduleToolbarInfo.imgStateTitle.setImageResource(R.drawable.clear_night)
            }


        }

        handleChipAbility(true)

    }
    private fun handleChipAbility(enabled: Boolean) {

        if (enabled) {
            binding.chipToday.isEnabled = true
            binding.chipTomorrow.isEnabled = true
            binding.chipWeekly.isEnabled = true
        } else {
            binding.chipToday.isEnabled = false
            binding.chipTomorrow.isEnabled = false
            binding.chipWeekly.isEnabled = false
        }


    }

    private fun handleToolbarInfoVisibility(shouldVisible : Boolean){

        if(shouldVisible){

            binding.moduleToolbarInfo.imgStateTitle.visibility = View.VISIBLE
            binding.moduleToolbarInfo.tempTitle.visibility = View.VISIBLE
            binding.moduleToolbarInfo.feelLikeTemp.visibility = View.VISIBLE

        }else{

            binding.moduleToolbarInfo.imgStateTitle.visibility = View.INVISIBLE
            binding.moduleToolbarInfo.tempTitle.visibility = View.INVISIBLE
            binding.moduleToolbarInfo.feelLikeTemp.visibility = View.INVISIBLE

        }

    }
    private fun handleFragmentMovements() {

        val bundle = Bundle()

        binding.mChipGroup.setOnCheckedChangeListener { group, checkedId ->

            handleChipAbility(false)

            when (checkedId) {

                (R.id.chip_today) -> {

                    infoCode = 0

                    if (Navigation.findNavController(binding.fragmentContainerView).currentDestination!!.id == R.id.dailyFragment) {
                        Navigation.findNavController(binding.fragmentContainerView)
                            .navigate(R.id.action_dailyFragment_self, bundle)
                    } else {
                        Navigation.findNavController(binding.fragmentContainerView)
                            .navigate(R.id.action_weeklyFragment_to_dailyFragment, bundle)
                    }

                    bundle.putInt(CHIP_CHANGE, 0)
                    handleToolbarInfoVisibility(true)

                }

                (R.id.chip_tomorrow) -> {

                    infoCode = 1


                    if (Navigation.findNavController(binding.fragmentContainerView).currentDestination!!.id == R.id.dailyFragment) {
                        Navigation.findNavController(binding.fragmentContainerView)
                            .navigate(R.id.action_dailyFragment_self, bundle)
                    } else {
                        Navigation.findNavController(binding.fragmentContainerView)
                            .navigate(R.id.action_weeklyFragment_to_dailyFragment, bundle)
                    }

                    bundle.putInt(CHIP_CHANGE, 1)
                    handleToolbarInfoVisibility(true)

                }

                (R.id.chip_weekly) -> {

                    Navigation.findNavController(binding.fragmentContainerView)
                        .navigate(R.id.action_dailyFragment_to_weeklyFragment)

                    handleToolbarInfoVisibility(false)

                }


            }


        }

    }
    override fun onSelected(latLng: String) {


        if(binding.chipWeekly.isChecked){
            findNavController(R.id.fragmentContainerView).navigate(R.id.action_weeklyFragment_self)
        }else{
            findNavController(R.id.fragmentContainerView).setGraph(R.navigation.nav_graph)
        }



        editor!!.clear()
        editor!!.putString(CACHE_LOCATION, latLng)
        editor!!.apply()
        editor!!.commit()
    }
    override fun sendWeatherData(data: WeatherData) {
        setToolbarData(data, infoCode)
    }

    override fun loaded() {
        
        handleChipAbility(true)

    }


}

