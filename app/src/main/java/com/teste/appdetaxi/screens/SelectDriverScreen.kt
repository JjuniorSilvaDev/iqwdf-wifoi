package com.teste.appdetaxi.screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.teste.appdetaxi.MainActivity
import com.teste.appdetaxi.adapter.AdapterDriver
import com.teste.appdetaxi.api.ApiService
import com.teste.appdetaxi.databinding.FragmentSelectDriverScreenBinding
import com.teste.appdetaxi.model.ConfirmRequest
import com.teste.appdetaxi.model.ConfirmResponse
import com.teste.appdetaxi.model.Driver
import com.teste.appdetaxi.model.Location
import com.teste.appdetaxi.network.RetrofitClient
import com.teste.appdetaxi.viewModel.SharedViewModel
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.io.IOException

class SelectDriverScreen : Fragment(),
    AdapterDriver.AdapterDriverToChooseDriverInteraction {

    private var _binding: FragmentSelectDriverScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var apiService: ApiService
    private lateinit var request: ConfirmRequest
    private lateinit var customer_id: String
    private lateinit var origin: String
    private lateinit var destination: String
    private var distance: Double = 0.0
    private lateinit var duration: String
    private lateinit var driver: Driver
    private var value: Double = 0.0

    private lateinit var originCoordinate: Location
    private lateinit var destinationCoordinate: Location
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectDriverScreenBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()

        prepareMap()

        fetchRoute()

    }

    private fun prepareRecyclerView() {
        sharedViewModel.option.value
        sharedViewModel.option.observe(viewLifecycleOwner) { listOfDrivers ->
            if (!listOfDrivers.isNullOrEmpty()) {
                val recyclerView = binding.recyclerViewChooseDriverFragment
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.setHasFixedSize(true)
                val adapterDriver = AdapterDriver(requireContext(), listOfDrivers, sharedViewModel, this)
                recyclerView.adapter = adapterDriver
            }
        }


    }

    private fun prepareMap() {
        val ctx = requireContext().applicationContext
        Configuration.getInstance().userAgentValue = ctx.packageName

        originCoordinate = sharedViewModel.originCoordinate.value ?: Location(0.0, 0.0)
        destinationCoordinate = sharedViewModel.destinationCoordinate.value ?: Location(0.0, 0.0)

        val minLatitude = minOf(originCoordinate.latitude, destinationCoordinate.latitude)
        val maxLatitude = maxOf(originCoordinate.latitude, destinationCoordinate.latitude)
        val minLongitude = minOf(originCoordinate.longitude, destinationCoordinate.longitude)
        val maxLongitude = maxOf(originCoordinate.longitude, destinationCoordinate.longitude)

        val margin = 0.05
        val boundingBox = org.osmdroid.util.BoundingBox(
            maxLatitude + margin,
            maxLongitude + margin,
            minLatitude - margin,
            minLongitude - margin
        )

        Configuration.getInstance().userAgentValue = requireContext().packageName
        mapView = binding.mapTravelOptionsFragment
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.post {
            mapView.zoomToBoundingBox(boundingBox, true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fetchRoute() {
        val url = String.format(
            "https://router.project-osrm.org/route/v1/driving/" +
                    "${originCoordinate.longitude},${originCoordinate.latitude};${destinationCoordinate.longitude},${destinationCoordinate.latitude}"
        )

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                requireActivity().runOnUiThread {
                    Toast.makeText(
                        requireContext(),
                        "Erro ao buscar rota: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    requireActivity().runOnUiThread {
                        Toast.makeText(
                            requireContext(),
                            "Erro na resposta do servidor",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    return
                }
                drawRouteOnMap()
            }
        })
    }

    private fun drawRouteOnMap() {
        val startPoint = GeoPoint(originCoordinate.latitude, originCoordinate.longitude)
        val endPoint = GeoPoint(destinationCoordinate.latitude, destinationCoordinate.longitude)

        // Mark A
        val markerStart = Marker(mapView).apply {
            position = startPoint
            title = "Ponto A"
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        }
        mapView.overlays.add(markerStart)

        // Mark B
        val markerEnd = Marker(mapView).apply {
            position = endPoint
            title = "Ponto B"
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        }
        mapView.overlays.add(markerEnd)

        mapView.invalidate() // Update map

        (context as MainActivity).callHideLoadScreen()
    }

    private fun sendDriverChoiceToServer() {

        getRaceData()

        apiService = RetrofitClient.instance

        request = ConfirmRequest(
            customer_id, origin, destination, distance, duration, driver, value
        )

        apiService.confirmRide(request).enqueue(object : retrofit2.Callback<ConfirmResponse> {
            override fun onResponse(
                call: retrofit2.Call<ConfirmResponse>,
                response: retrofit2.Response<ConfirmResponse>
            ) {
                if (response.isSuccessful) {
                    val confirmResponse = response.body()
                    Log.i("API", "Corrida confirmada: $confirmResponse")
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setMessage("$customer_id, a sua corrida foi confirmada com sucesso!")
                    builder.setPositiveButton("Ok") { dialog, _ ->
                        dialog.dismiss()
                        (context as MainActivity).callTransactionToTravelHistory()
                    }
                    builder.setCancelable(false)
                    val alertDialog = builder.create()
                    alertDialog.show()
                } else {
                    Log.e("API", "Erro: ${response.errorBody()?.string()}")
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("Erro")
                    builder.setMessage("${response.errorBody()?.toString()}")
                    builder.setPositiveButton("Ok") { dialog, _ ->
                        dialog.dismiss()
                    }
                    builder.setCancelable(false)
                    val alertDialog = builder.create()
                    alertDialog.show()
                }
            }

            override fun onFailure(call: retrofit2.Call<ConfirmResponse>, t: Throwable) {
                Log.e("API", "Falha ao enviar a requisição: ${t.message}")
            }
        })

    }

    private fun getRaceData() {
        customer_id = sharedViewModel.userId.value!!
        origin = sharedViewModel.origin.value!!
        destination = sharedViewModel.destination.value!!
        distance = sharedViewModel.distance.value!!
        duration = sharedViewModel.duration.value!!
        driver = sharedViewModel.driver.value!!
        value = sharedViewModel.value.value!!
    }

    interface ChooseDriverToMainActivityInteraction {
        fun callHideLoadScreen()
        fun callTransactionToTravelHistory()
    }

    override fun callSendDriverChoiceToServer() {
        sendDriverChoiceToServer()
    }
}

