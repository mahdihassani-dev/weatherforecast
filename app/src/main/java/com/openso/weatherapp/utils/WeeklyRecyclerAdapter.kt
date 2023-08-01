package com.openso.weatherapp.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.openso.weatherapp.databinding.ItemWeekRecyclerViewBinding
import com.openso.weatherapp.model.WeeklyItemData

class WeeklyRecyclerAdapter(private val dataset : ArrayList<WeeklyItemData>) : Adapter<WeeklyRecyclerAdapter.ViewHolder>() {


    inner class ViewHolder(private val binding: ItemWeekRecyclerViewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(position: Int){

            binding.txtWeekDay.text = dataset[position].weekDay
            binding.txtWeekState.text = dataset[position].weekState
            binding.txtWeekMax.text = dataset[position].weekMax
            binding.txtWeekMin.text = dataset[position].weekMin
            val imageId = dataset[position].imgState
            binding.imgWeekState.setImageResource(imageId)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemWeekRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return 7
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindData(position)
    }

}