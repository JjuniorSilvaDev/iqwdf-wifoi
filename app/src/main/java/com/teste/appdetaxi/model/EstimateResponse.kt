package com.teste.appdetaxi.model

import com.teste.appdetaxi.model.Location

data class EstimateResponse (
    val origin: Location,
    val destination: Location,
    val distance: Double?,
    val duration: String?,
    val options: List<Option>?,
    val routeResponse: Any?
)