package com.teste.appdetaxi.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teste.appdetaxi.databinding.ItemDriverBinding
import com.teste.appdetaxi.model.Option
import com.teste.appdetaxi.viewModel.SharedViewModel

class AdapterDriver(
    private val context: Context,
    private val driverList: List<Option>,
    private val sharedViewModel: SharedViewModel
) : RecyclerView.Adapter<AdapterDriver.DriverViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterDriver.DriverViewHolder {
        val itemList =
            ItemDriverBinding.inflate(LayoutInflater.from(context), parent, false)
        return DriverViewHolder(itemList)
    }

    override fun onBindViewHolder(holder: AdapterDriver.DriverViewHolder, position: Int) {

        val driver = driverList[position]

        holder.driverName.text = driver.name
        holder.rateFragment.text = String.format("${holder.rateFragment.text}\n${driver.review.rating}")
        holder.driverCar.text = String.format("${holder.driverCar.text}\n${driver.vehicle}")
        holder.rideFare.text = String.format("${holder.rideFare.text}\nR$ ${driver.value}")
        holder.description.text = driver.description

        holder.chooseDriverButton.setOnClickListener {

        }

    }

    override fun getItemCount(): Int = driverList.size

    inner class DriverViewHolder(binding: ItemDriverBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val driverName = binding.driverNameFragment
        val rateFragment = binding.rateFragment
        val driverCar = binding.carFragment
        val description = binding.descriptionFragment
        val rideFare = binding.rideFare
        val chooseDriverButton = binding.chooseDriverButtonFragment
    }
}