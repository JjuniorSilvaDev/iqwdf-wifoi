package com.teste.appdetaxi.model

import android.location.Location

data class EstimateResponse (
    val origin: Location,
    val destination: Location,
    val distance: Double,
    val duration: String,
    val options: List<Option>,
    val routeResponse: Any // Pode ser outro objeto se necessário
)