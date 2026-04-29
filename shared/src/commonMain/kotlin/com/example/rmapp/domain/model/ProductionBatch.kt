package com.example.rmapp.domain.model

data class ProductionBatch(
    val id: Long,
    val date: String,
    val inputQty: Double,
    val outputQty: Double,
    val wastageQty: Double
)