package com.example.rmapp.domain.repository

import app.cash.sqldelight.coroutines.asFlow
import com.example.rmapp.data.mapper.toDomain
import com.example.rmapp.db.RMAppCamphorDatabase
import com.example.rmapp.domain.model.StockItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StockRepositoryImpl(
    private val db: RMAppCamphorDatabase
) : StockRepository {

    override fun getAllStock(): Flow<List<StockItem>> {
        return db.stockQueries.getAllStock()
            .asFlow()
            .map { query ->
                query.executeAsList().map { it.toDomain() }
            }
    }
}