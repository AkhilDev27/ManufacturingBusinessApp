package com.example.rmapp.domain.repository

import app.cash.sqldelight.coroutines.asFlow
import com.example.rmapp.data.mapper.toDomain
import com.example.rmapp.db.RMAppCamphorDatabase
import com.example.rmapp.domain.model.ProductionBatch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductionRepositoryImpl(
    private val db: RMAppCamphorDatabase
) : ProductionRepository {

    override fun getTodayProduction(): Flow<List<ProductionBatch>> {
        return db.dashboardQueries.getTodayProduction()
            .asFlow()
            .map { query ->
                query.executeAsList().map { it.toDomain() }
            }
    }

    override fun getRecentBatches(): Flow<List<ProductionBatch>> {
        return db.dashboardQueries.getRecentBatches()
            .asFlow()
            .map { query ->
                query.executeAsList().map { it.toDomain() }
            }
    }
}