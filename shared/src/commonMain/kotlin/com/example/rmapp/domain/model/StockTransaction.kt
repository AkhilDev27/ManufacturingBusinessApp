package com.example.rmapp.domain.model

data class StockTransaction(
    val id: String = "",
    val productId: String,
    val type: String,
    val quantity: Double,
    val note: String?,
    val createdAt: String
)