package com.example.rmapp.domain.repository

import app.cash.sqldelight.coroutines.asFlow
import com.example.rmapp.data.mapper.toDomain
import com.example.rmapp.db.RMAppCamphorDatabase
import com.example.rmapp.domain.model.Sale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SalesRepositoryImpl(
    private val db: RMAppCamphorDatabase
) : SalesRepository {

    override fun getTodaySales(): Flow<List<Sale>> {
        return db.salesQueries.getTodaySales()
            .asFlow()
            .map { query ->
                query.executeAsList().map { it.toDomain() }
            }
    }
}