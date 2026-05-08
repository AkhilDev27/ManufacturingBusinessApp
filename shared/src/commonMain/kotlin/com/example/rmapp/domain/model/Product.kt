package com.example.rmapp.domain.model

data class Product(
    val id: Long = 0,
    val name: String = "",
    val unitId: Long = 0,
    val unitName: String = "",
    val costPrice: Double = 0.0,
    val sellingPrice: Double = 0.0
)