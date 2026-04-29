package com.example.rmapp.domain.repository

import com.example.rmapp.domain.model.StockItem
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    fun getAllStock(): Flow<List<StockItem>>
}