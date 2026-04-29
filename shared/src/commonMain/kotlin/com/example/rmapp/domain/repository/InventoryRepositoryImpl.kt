package com.example.rmapp.domain.repository

import com.example.rmapp.domain.model.RawMaterial

class InventoryRepositoryImpl : InventoryRepository {

    private val items = mutableListOf<RawMaterial>()

    override suspend fun getAll(): List<RawMaterial> = items

    override suspend fun upsert(item: RawMaterial) {
        val index = items.indexOfFirst { it.id == item.id }
        if (index >= 0) items[index] = item else items.add(item)
    }

    override suspend fun delete(id: String) {
        items.removeAll { it.id == id }
    }
}