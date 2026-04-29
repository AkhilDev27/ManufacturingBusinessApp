package com.example.rmapp.domain.usecase

import com.example.rmapp.domain.model.RawMaterial
import com.example.rmapp.domain.repository.InventoryRepository

class InventoryUseCase (
    private val repo: InventoryRepository
) {

    suspend fun getAll(): List<RawMaterial> =
        repo.getAll()

    suspend fun save(item: RawMaterial) =
        repo.upsert(item)

    suspend fun delete(id: String) =
        repo.delete(id)
}