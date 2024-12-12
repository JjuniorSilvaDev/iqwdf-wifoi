package com.teste.appdetaxi.adapter

import android.content.Context
import android.icu.text.DecimalFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.teste.appdetaxi.databinding.ItemDriverBinding
import com.teste.appdetaxi.model.Driver
import com.teste.appdetaxi.model.Option
import com.teste.appdetaxi.screens.SelectDriverScreen
import com.teste.appdetaxi.viewModel.SharedViewModel

class AdapterDriver(
    private val context: Context,
    private val driverList: List<Option>,
    private val sharedViewModel: SharedViewModel,
    private val listener: SelectDriverScreen
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

        val driverRate = DecimalFormat("#.##").format(driver.review.rating)
        holder.rateFragment.text = String.format("${holder.rateFragment.text}\n$driverRate")

        holder.driverCar.text = String.format("${holder.driverCar.text}\n${driver.vehicle}")

        val rideFare = DecimalFormat("#.##").format(driver.value)
        holder.rideFare.text = String.format("${holder.rideFare.text}\nR$ $rideFare")

        holder.description.text = driver.description

        holder.comment.text = driver.review.comment

        holder.chooseDriverButton.setOnClickListener {
            val distance = (sharedViewModel.distance.value!!) / 100

            if (
                driver.id == 1 && distance >= 1 ||
                driver.id == 2 && distance >= 5 ||
                driver.id == 3 && distance >= 10
            ) {
                sharedViewModel.setDriver(Driver(driver.id, driver.name))
                sharedViewModel.setValue(driver.value)
                listener.callSendDriverChoiceToServer()
            } else {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Distância Inválida")
                builder.setMessage("Desculpe ${sharedViewModel.userId.value}, mas o motorista ${driver.name} só aceita corridas com distâncias mais longas")
            }

        }

    }

    override fun getItemCount(): Int = driverList.size

    inner class DriverViewHolder(binding: ItemDriverBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val driverName = binding.driverNameFragment
        val rateFragment = binding.rateFragment
        val driverCar = binding.carFragment
        val description = binding.descriptionFragment
        val rideFare = binding.rideValue
        val chooseDriverButton = binding.chooseDriverButtonFragment
        val comment = binding.commentFragment
    }

    interface AdapterDriverToChooseDriverInteraction {
        fun callSendDriverChoiceToServer()
    }
}