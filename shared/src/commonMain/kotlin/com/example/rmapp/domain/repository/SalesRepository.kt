package com.example.rmapp.domain.repository

import com.example.rmapp.domain.model.Sale
import kotlinx.coroutines.flow.Flow

interface SalesRepository {
    fun getTodaySales(): Flow<List<Sale>>
}