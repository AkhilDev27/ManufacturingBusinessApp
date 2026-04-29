package com.example.rmapp.domain.model

data class RawMaterial(
    val id: String,
    val name: String,
    val unit: String,
    val price: Double,
    val stockAvailable: Double
)