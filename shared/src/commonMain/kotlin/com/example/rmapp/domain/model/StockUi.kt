package com.example.rmapp.domain.model

data class StockUI(
    val productId: Long,
    val productName: String,
    val quantity: Double,
    val minimumThreshold: Double
)