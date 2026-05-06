package com.example.rmapp.domain.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.rmapp.db.inventory.InventoryQueries
import com.example.rmapp.domain.model.RawMaterial
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class InventoryRepositoryImpl(
    private val queries: InventoryQueries
) : InventoryRepository {

    override fun getAll(): Flow<List<RawMaterial>> =
        queries.getAllMaterials()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { list ->
                list.map { m ->
                    RawMaterial(
                        id = m.id.toString(),
                        name = m.name,
                        unit = m.unit,
                        price = m.price,
                        stockAvailable = m.quantity
                    )
                }
            }

    override fun search(query: String): Flow<List<RawMaterial>> =
        queries.searchMaterials(query)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map {
                it.map { m ->
                    RawMaterial(
                        id = m.id.toString(),
                        name = m.name,
                        unit = m.unit,
                        price = m.price,
                        stockAvailable = m.quantity
                    )
                }
            }

    override suspend fun insert(item: RawMaterial) {
        queries.insertMaterial(item.name, item.unit, item.price, item.stockAvailable)
    }

    override suspend fun update(item: RawMaterial) {
        queries.updateMaterial(item.name, item.unit, item.price, item.stockAvailable, item.id.toLong())
    }

    override suspend fun delete(id: String) {
        queries.deleteMaterial(id.toLong())
    }
}