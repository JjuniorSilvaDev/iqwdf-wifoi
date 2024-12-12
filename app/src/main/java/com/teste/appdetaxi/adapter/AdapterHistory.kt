package com.teste.appdetaxi.adapter

import android.content.Context
import android.icu.text.DecimalFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teste.appdetaxi.databinding.ItemHistoryBinding
import com.teste.appdetaxi.model.Ride
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AdapterHistory(
    private val context: Context,
    private val ridesList: List<Ride>
) : RecyclerView.Adapter<AdapterHistory.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val itemList =
            ItemHistoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return HistoryViewHolder(itemList)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {

        val currentRide = ridesList[position]

        val driverNameAndId = String.format("ID: ${currentRide.driver.id}\n" + currentRide.driver.name)
        holder.driverNameAndId.text = driverNameAndId

        val time = LocalDateTime.parse(currentRide.date, DateTimeFormatter.ISO_DATE_TIME)
        val formattedTime = time.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
        holder.date.text = String.format("Data e hora da corrida\n${formattedTime}")

        holder.originName.text = String.format("Corrida iniciando em\n"+currentRide.origin)

        holder.destinationName.text = String.format("Destino final\n"+currentRide.destination)

        val distanceRide = DecimalFormat("#.##").format(currentRide.distance)
        val distanceRideFormatted = String.format("Distaância percorrida\n$distanceRide KM")
        holder.distance.text = distanceRideFormatted

        holder.time.text = String.format("Duração da corrida\n${currentRide.duration} M")

        val valueRide = DecimalFormat("#.##").format(currentRide.value)
        val valueRideFormatted = String.format("Valor da corrida\n R$ $valueRide")
        holder.value.text = valueRideFormatted
    }

    override fun getItemCount(): Int = ridesList.size

    inner class HistoryViewHolder(binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val date = binding.dateHistoryFragment
        val originName = binding.originHistoryFragment
        val destinationName = binding.destinationHistoryFragment
        val distance = binding.distanceTraveledHistoryFragment
        val time = binding.timeCurrentHistoryFragment
        val driverNameAndId = binding.driverNameHistoryFragment
        val value = binding.priceHistoryFragment
    }
}