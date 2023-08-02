package com.openso.weatherapp.utils

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.openso.weatherapp.databinding.ItemWeekRecyclerViewBinding
import com.openso.weatherapp.model.WeeklyItemData
import com.openso.weatherapp.ui.WeeklyFragment

class WeeklyRecyclerAdapter(private val dataset : ArrayList<WeeklyItemData>, private val shouldHide : Boolean) : Adapter<WeeklyRecyclerAdapter.ViewHolder>() {

    private lateinit var binding: ItemWeekRecyclerViewBinding

    inner class ViewHolder(private val binding: ItemWeekRecyclerViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(position: Int){

            if(shouldHide){
                disableShimmer()
            }

            binding.txtWeekDay.text = dataset[position].weekDay
            binding.txtWeekState.text = dataset[position].weekState
            binding.txtWeekMax.text = dataset[position].weekMax
            binding.txtWeekMin.text = dataset[position].weekMin
            val imageId = dataset[position].imgState
            binding.imgWeekState.setImageResource(imageId)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        binding = ItemWeekRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)


        return ViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return 7
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindData(position)
    }

    private fun disableShimmer(){

        binding.shimItemRecycler.hideShimmer()

    }




}