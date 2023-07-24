package com.example.weatherapp.ui
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleFragmentMovements()

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