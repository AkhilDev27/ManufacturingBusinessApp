package com.example.rmapp.domain.model

data class Product(
    val id: String = "",
    val name: String = "",
    val unit: String = "",
    val sellingPrice: Double = 0.0,
    val costPrice: Double = 0.0
)