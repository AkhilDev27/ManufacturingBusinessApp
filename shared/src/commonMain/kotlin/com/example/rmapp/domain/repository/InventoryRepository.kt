package com.example.rmapp.domain.repository

import com.example.rmapp.domain.model.RawMaterial
import kotlinx.coroutines.flow.Flow

interface InventoryRepository {

    fun getAll(): Flow<List<RawMaterial>>

    fun search(query: String): Flow<List<RawMaterial>>

    suspend fun insert(item: RawMaterial)

    suspend fun update(item: RawMaterial)

    suspend fun delete(id: String)
}