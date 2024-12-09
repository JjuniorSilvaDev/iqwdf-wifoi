package com.teste.appdetaxi.model

data class ConfirmRequest(
    val customer_id: String,
    val origin: String,
    val destination: String,
    val distance: Double,
    val duration: String,
    val driver: Driver,
    val value: Double
)
