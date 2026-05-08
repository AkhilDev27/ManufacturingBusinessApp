package com.example.rmapp.domain.model

data class Stock(
    val id: String = "",
    val productId: Long,
    val quantity: Double,
    val minimumThreshold: Double
)