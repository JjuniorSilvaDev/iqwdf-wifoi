package com.teste.appdetaxi.model

data class Option(
    val id: Int,
    val name: String,
    val description: String,
    val vehicle: String,
    val review: Review,
    val value: Double
)
