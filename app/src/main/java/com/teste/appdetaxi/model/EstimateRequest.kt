package com.teste.appdetaxi.model

data class EstimateRequest (
    val customer_id: String,
    val origin: String,
    val destination: String
)