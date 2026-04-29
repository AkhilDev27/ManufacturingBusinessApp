package com.example.rmapp.domain.repository

import com.example.rmapp.domain.model.RawMaterial

interface InventoryRepository {
    suspend fun getAll(): List<RawMaterial>
    suspend fun upsert(item: RawMaterial) // handles add + edit
    suspend fun delete(id: String)
}