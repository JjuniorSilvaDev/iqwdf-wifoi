package com.teste.appdetaxi.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.teste.appdetaxi.R
import com.teste.appdetaxi.adapter.AdapterHistory
import com.teste.appdetaxi.databinding.FragmentTravelHistoryScreenBinding
import com.teste.appdetaxi.model.GetRidesResponse
import com.teste.appdetaxi.model.Ride
import com.teste.appdetaxi.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TravelHistoryScreen : Fragment() {

    private var _binding: FragmentTravelHistoryScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var userIdInput: TextInputEditText
    private lateinit var driverSpinner: Spinner
    private lateinit var searchButton: Button
    private lateinit var recyclerView: RecyclerView

    private lateinit var customerId: String
    private var driverId: Int? = null
    private lateinit var ridesList: List<Ride>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTravelHistoryScreenBinding.inflate(inflater, container, false)

        prepareBinding()

        inflateItems()

        return binding.root
    }

    private fun prepareBinding() {
        binding.apply {
            userIdInput = userIdHistoryScreenFragment
            driverSpinner = spinnerDriverNameFragment
            searchButton = searchHistoryButtonFragment
            recyclerView = travelHistoryRecyclerViewFragment
        }
    }

    private fun inflateItems() {
        searchButton.setOnClickListener {

            customerId = userIdInput.text.toString()

            checkDriverId()

            checkRidesHistory()
        }

    }

    private fun checkDriverId() {

        when (driverSpinner.selectedItem.toString()) {
            resources.getStringArray(R.array.drivers_list)[0] -> {
                driverId = null
            }

            resources.getStringArray(R.array.drivers_list)[1] -> {
                driverId = 1
            }

            resources.getStringArray(R.array.drivers_list)[2] -> {
                driverId = 2
            }

            resources.getStringArray(R.array.drivers_list)[3] -> {
                driverId = 3
            }

            resources.getStringArray(R.array.drivers_list)[4] -> {
                driverId = 4
            }
        }

    }

    private fun checkRidesHistory() {

        when {

            customerId == "CT01" &&
                    driverId != null &&
                    driverId != 1 &&
                    driverId != 2 &&
                    driverId != 3 &&
                    driverId != 4 -> {

                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Erro")
                builder.setMessage("Motorista não encontrado")
                builder.setCancelable(false)
                builder.setPositiveButton("Ok") { dialog, _ ->
                    dialog.dismiss()
                }
                val alertDialog = builder.create()
                alertDialog.show()

            }

            customerId == "CT01" -> {

                connectToServer()

            }

            customerId == "" -> {

                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Erro")
                builder.setMessage("Por favor, não deixe o campo Id em branco")
                builder.setCancelable(false)
                builder.setPositiveButton("Ok") { dialog, _ ->
                    dialog.dismiss()
                }
                val alertDialog = builder.create()
                alertDialog.show()

            }

            else -> {

                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Erro")
                builder.setMessage("Não há histórico de viagens disponível pra esse cliente")
                builder.setCancelable(false)
                builder.setPositiveButton("Ok") { dialog, _ ->
                    dialog.dismiss()
                }
                val alertDialog = builder.create()
                alertDialog.show()

            }

        }

    }

    private fun prepareRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        val adapterHistory = AdapterHistory(requireContext(), ridesList)
        recyclerView.adapter = adapterHistory
    }

    private fun checkDriverValidate() {

        when (driverId) {

            null -> {
                ridesList = emptyList()

                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Erro")
                builder.setMessage("Por favor, selecione um motorista!")
                builder.setPositiveButton("Ok") { dialog, _ ->
                    dialog.dismiss()
                }
                builder.setCancelable(false)
                val alertDialog = builder.create()
                alertDialog.show()
            }

            4 -> {

                val newRidesList: MutableList<Ride> = ridesList.toMutableList()
                var cont = 0

                for (ride in ridesList) {

                    if (
                        ride.driver.name == "Homer Simpson" && ride.driver.id != 1 ||
                        ride.driver.name == "Dominic Toretto" && ride.driver.id != 2 ||
                        ride.driver.name == "James Bond" && ride.driver.id != 3
                    ) {
                        newRidesList.remove(ride)
                        cont++
                    }
                }

                ridesList = newRidesList

                when {
                    cont != 0 && ridesList.isNotEmpty() -> {

                        val builder = AlertDialog.Builder(requireContext())
                        builder.setMessage("Alguns resultados em que haviam motoristas com Id inválido foram excluidos do resultado.")
                        builder.setPositiveButton("Ok") { dialog, _ ->
                            dialog.dismiss()
                        }
                        builder.setCancelable(false)
                        val alertDialog = builder.create()
                        alertDialog.show()

                    }

                    cont != 0 -> {

                        val builder = AlertDialog.Builder(requireContext())
                        builder.setMessage("Não foram encontradas corridas, alguns resultados em que haviam motoristas com Id inválido foram excluidos do resultado.")
                        builder.setPositiveButton("Ok") { dialog, _ ->
                            dialog.dismiss()
                        }
                        builder.setCancelable(false)
                        val alertDialog = builder.create()
                        alertDialog.show()

                    }
                }

                prepareRecyclerView()

            }

            else -> {
                val newRidesList: MutableList<Ride> = ridesList.toMutableList()
                var cont = 0

                for (ride in ridesList) {

                    if (
                        ride.driver.name == "Homer Simpson" && ride.driver.id != 1 ||
                        ride.driver.name == "Dominic Toretto" && ride.driver.id != 2 ||
                        ride.driver.name == "James Bond" && ride.driver.id != 3 ||
                        ride.driver.id != driverId
                    ) {
                        newRidesList.remove(ride)
                        cont++
                    }

                }
                ridesList = newRidesList

                when {
                    cont != 0 && ridesList.isNotEmpty() -> {

                        val builder = AlertDialog.Builder(requireContext())
                        builder.setMessage("Alguns resultados em que haviam motoristas com Id inválido foram excluidos do resultado.")
                        builder.setPositiveButton("Ok") { dialog, _ ->
                            dialog.dismiss()
                        }
                        builder.setCancelable(false)
                        val alertDialog = builder.create()
                        alertDialog.show()

                    }

                    cont != 0 -> {

                        val builder = AlertDialog.Builder(requireContext())
                        builder.setMessage("Não foram encontradas corridas com esse motorista, alguns resultados em que haviam motoristas com Id inválido foram excluidos do resultado.")
                        builder.setPositiveButton("Ok") { dialog, _ ->
                            dialog.dismiss()
                        }
                        builder.setCancelable(false)
                        val alertDialog = builder.create()
                        alertDialog.show()

                    }
                }

                prepareRecyclerView()
            }

        }
    }


    private fun connectToServer() {

        val apiService = RetrofitClient.instance

        apiService.getRides(customerId).enqueue(object : Callback<GetRidesResponse> {
            override fun onResponse(
                call: Call<GetRidesResponse>,
                response: Response<GetRidesResponse>
            ) {
                if (response.isSuccessful) {
                    ridesList = response.body()!!.rides
                    checkDriverValidate()
                } else {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("Erro")
                    builder.setMessage("${response.code()}")
                    builder.setCancelable(false)
                    builder.setPositiveButton("Ok") { dialog, _ ->
                        dialog.dismiss()
                    }
                    val alertDialog = builder.create()
                    alertDialog.show()
                }
            }

            override fun onFailure(call: Call<GetRidesResponse>, t: Throwable) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Falha na Requisição")
                builder.setMessage("${t.message}")
                builder.setCancelable(false)
                builder.setPositiveButton("Ok") { dialog, _ ->
                    dialog.dismiss()
                }
                val alertDialog = builder.create()
                alertDialog.show()
            }

        })
    }

}