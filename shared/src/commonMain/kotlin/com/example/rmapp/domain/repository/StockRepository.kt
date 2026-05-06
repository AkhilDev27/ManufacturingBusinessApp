package com.example.rmapp.domain.repository

import com.example.rmapp.domain.model.Stock
import com.example.rmapp.domain.model.StockTransaction
import kotlinx.coroutines.flow.Flow

interface StockRepository {

    fun getAllStock(): Flow<List<Stock>>

    fun getLowStock(): Flow<List<Stock>>

    fun getTransactions(productId: String): Flow<List<StockTransaction>>

    suspend fun applyTransaction(
        productId: String,
        type: String,
        quantity: Double,
        note: String? = null
    )
}