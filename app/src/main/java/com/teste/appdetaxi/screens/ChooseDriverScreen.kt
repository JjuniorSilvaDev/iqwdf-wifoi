package com.teste.appdetaxi.screens

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.teste.appdetaxi.databinding.FragmentChooseDriverScreenBinding
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

        val ctx = requireContext().applicationContext
        Configuration.getInstance().userAgentValue = ctx.packageName

        // Configuração do mapa
        Configuration.getInstance().userAgentValue = requireContext().packageName
        mapView = binding.mapTravelOptionsFragment
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.controller.setZoom(15.0)
        mapView.controller.setCenter(GeoPoint(-23.55052, -46.633308))

        // Buscar e exibir rota
        fetchRoute()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fetchRoute() {
        val url = "https://router.project-osrm.org/route/v1/driving/-46.633308,-23.55052;-46.625290,-23.559616"

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
        val startPoint = GeoPoint(-23.55052, -46.633308) // Ponto A
        val endPoint = GeoPoint(-23.559616, -46.625290) // Ponto B

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
    }
}

