package com.example.rmapp.domain.model

data class StockItem(
    val id: Long,
    val name: String,
    val quantity: Double,
    val unitPrice: Double,
    val minimumThreshold: Double
)