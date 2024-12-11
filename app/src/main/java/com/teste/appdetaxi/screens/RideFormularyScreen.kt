package com.teste.appdetaxi.screens

import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import com.google.android.material.textfield.TextInputEditText
import com.teste.appdetaxi.MainActivity
import com.teste.appdetaxi.api.ApiService
import com.teste.appdetaxi.databinding.FragmentRideFormularyScreenBinding
import com.teste.appdetaxi.model.EstimateRequest
import com.teste.appdetaxi.model.EstimateResponse
import com.teste.appdetaxi.model.Location
import com.teste.appdetaxi.network.RetrofitClient
import com.teste.appdetaxi.viewModel.SharedViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class RideFormularyScreen : Fragment() {

    private var _binding: FragmentRideFormularyScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var buttonSearchRide: Button
    private lateinit var apiService: ApiService
    private lateinit var request: EstimateRequest

    private lateinit var customerIdInput: TextInputEditText
    private lateinit var originPoint: TextInputEditText
    private lateinit var destinationPoint: TextInputEditText

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRideFormularyScreenBinding.inflate(inflater, container, false)

        prepareBinding()

        inflateItems()

        return binding.root
    }

    private fun prepareBinding() {
        binding.apply {
            buttonSearchRide = buttonSearchRideFragment
            customerIdInput = customerIdInputFragment
            originPoint = startPointInputFragment
            destinationPoint = destinationInputFragment
        }
    }

    private fun inflateItems() {
        buttonSearchRide.setOnClickListener {
            (context as MainActivity).callShowLoadScreen()
            checkRideInformation()
        }
    }

    private fun checkRideInformation() {

        var connectWithEndPoint = false

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Olá usuário ${customerIdInput.text}")

        when {
            customerIdInput.text.isNullOrBlank() -> {
                builder.setMessage("Erro, por favor preencha o campo 'Id do usuário'")
            }

            originPoint.text.isNullOrBlank() -> {
                builder.setMessage("Erro, por favor preencha o campo 'Digite o local de partida'")
            }

            destinationPoint.text.isNullOrBlank() -> {
                builder.setMessage("Erro, por favor preencha o campo 'Digite o destino'")
            }

            originPoint.text.toString() == destinationPoint.text.toString() -> {
                builder.setMessage("Erro, o endereço de destino deve ser diferente do endereço de origem")
            }

            originPoint.text.toString() == "Av. Pres. Kenedy, 2385 - Remédios, Osasco - SP, 02675-031" &&
                    destinationPoint.text.toString() == "Av. Paulista, 1538 - Bela Vista, São Paulo - SP, 01310-200" ||
                    originPoint.text.toString() == "Av. Thomas Edison, 365 - Barra Funda, São Paulo - SP, 01140-000" &&
                    destinationPoint.text.toString() == "Av. Paulista, 1538 - Bela Vista, São Paulo - SP, 01310-200" ||
                    originPoint.text.toString() == "Av. Brasil, 2033 - Jardim America, São Paulo - SP, 01431-001" &&
                    destinationPoint.text.toString() == "Av. Paulista, 1538 - Bela Vista, São Paulo - SP, 01310-200" -> {
                builder.setMessage("Estamos traçando a rota e encontrando motoristas, por favor aguarde.")
                connectWithEndPoint = true
            }

            else -> {
                builder.setMessage("No momento não há motoristas disponíveis pra essa rota.")
            }
        }

        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            if (connectWithEndPoint) {
                connectWithEndPoint()
            }
        }
        builder.setCancelable(false)
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun connectWithEndPoint() {

        apiService = RetrofitClient.instance

        request = EstimateRequest(
            customer_id = binding.customerIdInputFragment.text.toString(),
            origin = binding.startPointInputFragment.text.toString(),
            destination = binding.destinationInputFragment.text.toString()
        )

        apiService.estimateRide(request).enqueue(object : Callback<EstimateResponse> {
            override fun onResponse(
                call: Call<EstimateResponse>,
                response: Response<EstimateResponse>
            ) {
                if (response.isSuccessful) {
                    val estimateResponse = response.body()

                    Toast.makeText(context, "Viagem estimada com sucesso!", Toast.LENGTH_SHORT)
                        .show()

                    saveInformation(estimateResponse)

                } else {
                    Toast.makeText(context, "Erro: ${response.code()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<EstimateResponse>, t: Throwable) {
                // Caso haja falha na chamada (ex: sem conexão)
                Toast.makeText(context, "Falha na requisição: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })

    }

//    private fun checkOnGeoCoder() {
//        val geocoder = Geocoder(requireContext(), Locale.getDefault())
//        val originAddress = originPoint
//        val destinationAddress = destinationPoint
//
//        try {
//            val addresses = geocoder.getFromLocationName(originAddress.toString(),1)
//            if (addresses.isNullOrEmpty()) {
//                val location = addresses?.get(0)?.let { Location(latitude = it.latitude, longitude = addresses[0].longitude) }
//            }
//        }catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }

    private fun saveInformation(estimateResponse: EstimateResponse?) {
        val textId = String.format("${binding.customerIdInputFragment.text}")
        sharedViewModel.setUserId(textId)
        sharedViewModel.setDestination(estimateResponse?.destination)
        sharedViewModel.setOrigin(estimateResponse?.origin)
        sharedViewModel.setDistance(estimateResponse?.distance)
        sharedViewModel.setDuration(estimateResponse?.duration)
        sharedViewModel.setOptions(estimateResponse?.options)
        sharedViewModel.setRouteResponse(estimateResponse?.routeResponse)
        (context as MainActivity).callHideLoadScreen()
        (context as MainActivity).callTransactionToChooseDriver()
    }

    interface RideFormularyToMainActivityInteraction {
        fun callTransactionToChooseDriver()
        fun callHideLoadScreen()
        fun callShowLoadScreen()
    }

}