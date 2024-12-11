package com.teste.appdetaxi.screens

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.teste.appdetaxi.MainActivity
import com.teste.appdetaxi.adapter.AdapterDriver
import com.teste.appdetaxi.databinding.FragmentChooseDriverScreenBinding
import com.teste.appdetaxi.model.Location
import com.teste.appdetaxi.viewModel.SharedViewModel
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import java.io.IOException

class ChooseDriverScreen : Fragment() {

    private var _binding: FragmentChooseDriverScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var origin: Location
    private lateinit var destination: Location
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseDriverScreenBinding.inflate(inflater, container, false)

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
                val adapterDriver = AdapterDriver(requireContext(), listOfDrivers, sharedViewModel)
                recyclerView.adapter = adapterDriver
            }
        }


    }

    private fun prepareMap() {
        val ctx = requireContext().applicationContext
        Configuration.getInstance().userAgentValue = ctx.packageName

        origin = sharedViewModel.origin.value ?: Location(0.0,0.0)
        destination = sharedViewModel.destination.value ?: Location(0.0,0.0)

        val minLatitude = minOf(origin.latitude, destination.latitude)
        val maxLatitude = maxOf(origin.latitude, destination.latitude)
        val minLongitude = minOf(origin.longitude, destination.longitude)
        val maxLongitude = maxOf(origin.longitude, destination.longitude)

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
            mapView.zoomToBoundingBox(boundingBox,false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fetchRoute() {
        val url = String.format("https://router.project-osrm.org/route/v1/driving/"+
                "${origin.longitude},${origin.latitude};${destination.longitude},${destination.latitude}")

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "Erro ao buscar rota: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(), "Erro na resposta do servidor", Toast.LENGTH_LONG).show()
                    }
                    return
                }
                response.body?.string()?.let { jsonResponse ->
                    processRouteResponse(jsonResponse)
                }
            }
        })
    }

    private fun processRouteResponse(jsonResponse: String) {
        try {
            val jsonObject = JSONObject(jsonResponse)
            val routes = jsonObject.getJSONArray("routes")
            if (routes.length() > 0) {
                val route = routes.getJSONObject(0)
                val geometry = route.getString("geometry")
                val decodedPoints = PolylineEncoder.decode(geometry)

                requireActivity().runOnUiThread {
                    drawRouteOnMap(decodedPoints)
                }
            } else {
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "Nenhuma rota encontrada", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            requireActivity().runOnUiThread {
                Toast.makeText(requireContext(), "Erro ao processar a rota: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    object PolylineEncoder {
        fun decode(polyline: String): List<GeoPoint> {
            val points = mutableListOf<GeoPoint>()
            var index = 0
            var lat = 0
            var lng = 0

            while (index < polyline.length) {
                var shift = 0
                var result = 0
                var byte: Int
                do {
                    byte = polyline[index++].toInt() - 63
                    result = result or ((byte and 0x1f) shl shift)
                    shift += 5
                } while (byte >= 0x20)

                val dLat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
                lat += dLat

                shift = 0
                result = 0
                do {
                    byte = polyline[index++].toInt() - 63
                    result = result or ((byte and 0x1f) shl shift)
                    shift += 5
                } while (byte >= 0x20)

                val dLng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
                lng += dLng

                points.add(GeoPoint(lat / 1E5, lng / 1E5))
            }

            return points
        }
    }

    private fun drawRouteOnMap(points: List<GeoPoint>) {
        val startPoint = GeoPoint(origin.latitude, origin.longitude)
        val endPoint = GeoPoint(destination.latitude, destination.longitude)

        // Marcador inicial
        val markerStart = Marker(mapView).apply {
            position = startPoint
            title = "Ponto A"
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        }
        mapView.overlays.add(markerStart)

        // Marcador final
        val markerEnd = Marker(mapView).apply {
            position = endPoint
            title = "Ponto B"
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        }
        mapView.overlays.add(markerEnd)

        // Desenhar rota
        val roadOverlay = Polyline().apply {
            setPoints(points)
            color = Color.BLUE
            width = 8.0f
        }

        mapView.overlays.add(roadOverlay)
        mapView.invalidate() // Atualizar mapa

        (context as MainActivity).callHideLoadScreen()
    }

    interface ChooseDriverToMainActivityInteraction {
        fun callHideLoadScreen()
    }
}

