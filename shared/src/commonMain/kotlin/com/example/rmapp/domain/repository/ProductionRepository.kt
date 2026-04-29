package com.example.rmapp.domain.repository

import com.example.rmapp.domain.model.ProductionBatch
import kotlinx.coroutines.flow.Flow

interface ProductionRepository {
    fun getTodayProduction(): Flow<List<ProductionBatch>>
    fun getRecentBatches(): Flow<List<ProductionBatch>>
}