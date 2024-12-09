package com.teste.appdetaxi.model

data class EstimateRequest (
    val customerId: String,
    val origin: String,
    val destination: String
)