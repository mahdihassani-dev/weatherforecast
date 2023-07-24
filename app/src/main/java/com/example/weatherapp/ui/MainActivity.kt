package com.example.weatherapp.ui
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.model.WeatherData
import com.example.weatherapp.networking.ApiManager


private const val TAG = "ApiTest"
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val apiManager = ApiManager()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleFragmentMovements()

        binding.moduleToolbarInfo.progressToolbar.visibility = View.VISIBLE

        apiManager.getGeneralData(object : ApiManager.MyApiCallBack<WeatherData>{
            override fun onSuccess(data: WeatherData) {

                binding.moduleToolbarInfo.tempTitle.text = data.days[0].temp.toInt().toString() + "°"
                binding.moduleToolbarInfo.feelLikeTemp.text = "Feel Like " + data.days[0].feelslike + "°"

                when(data.days[0].icon){

                    "snow" ->{binding.moduleToolbarInfo.imgStateTitle.setImageResource(R.drawable.snow)}
                    "rain" ->{binding.moduleToolbarInfo.imgStateTitle.setImageResource(R.drawable.rain)}
                    "fog" ->{binding.moduleToolbarInfo.imgStateTitle.setImageResource(R.drawable.fog)}
                    "wind" ->{binding.moduleToolbarInfo.imgStateTitle.setImageResource(R.drawable.wind)}
                    "cloudy" ->{binding.moduleToolbarInfo.imgStateTitle.setImageResource(R.drawable.cloudy)}
                    "partly-cloudy-day" ->{binding.moduleToolbarInfo.imgStateTitle.setImageResource(R.drawable.partly_cloudy_day)}
                    "partly-cloudy-night" ->{binding.moduleToolbarInfo.imgStateTitle.setImageResource(R.drawable.partly_cloudy_night)}
                    "clear-day" ->{binding.moduleToolbarInfo.imgStateTitle.setImageResource(R.drawable.clear_day)}
                    "clear-night" ->{binding.moduleToolbarInfo.imgStateTitle.setImageResource(R.drawable.clear_night)}


                }

                binding.moduleToolbarInfo.progressToolbar.visibility = View.INVISIBLE

            }

            override fun onFailure(error: String) {

                Log.i(TAG, error)

            }
        })




    }

    private fun handleFragmentMovements() {

        binding.mChipGroup.setOnCheckedChangeListener { group, checkedId ->

            when (checkedId) {

                (R.id.chip_today) -> {
                    if (Navigation.findNavController(binding.fragmentContainerView).currentDestination!!.id == R.id.dailyFragment) {
                        Navigation.findNavController(binding.fragmentContainerView)
                            .navigate(R.id.action_dailyFragment_self)
                    } else {
                        Navigation.findNavController(binding.fragmentContainerView)
                            .navigate(R.id.action_weeklyFragment_to_dailyFragment)
                    }
                }

                (R.id.chip_tomorrow) -> {
                    if (Navigation.findNavController(binding.fragmentContainerView).currentDestination!!.id == R.id.dailyFragment) {
                        Navigation.findNavController(binding.fragmentContainerView)
                            .navigate(R.id.action_dailyFragment_self)
                    } else {
                        Navigation.findNavController(binding.fragmentContainerView)
                            .navigate(R.id.action_weeklyFragment_to_dailyFragment)
                    }
                }

                (R.id.chip_weekly) -> {

                    Navigation.findNavController(binding.fragmentContainerView)
                        .navigate(R.id.action_dailyFragment_to_weeklyFragment)
                }


            }


        }

    }


}