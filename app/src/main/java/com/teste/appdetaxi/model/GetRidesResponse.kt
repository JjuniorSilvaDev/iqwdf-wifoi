package com.teste.appdetaxi.model

data class GetRidesResponse(
    val customer_id: String,
    val rides: List<Ride>
)
