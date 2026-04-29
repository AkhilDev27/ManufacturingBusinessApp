package com.example.rmapp.domain.model

data class Sale(
    val id: Long,
    val customerId: Long?,
    val totalAmount: Double,
    val date: String
)