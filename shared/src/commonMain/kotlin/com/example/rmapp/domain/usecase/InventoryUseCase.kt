package com.example.rmapp.domain.usecase

import com.example.rmapp.domain.model.RawMaterial
import com.example.rmapp.domain.repository.InventoryRepository

class InventoryUseCase(
    private val repo: InventoryRepository
) {

    fun getAll() = repo.getAll()

    fun search(query: String) =
        if (query.isBlank()) repo.getAll()
        else repo.search(query)

    suspend fun add(item: RawMaterial) = repo.insert(item)

    suspend fun update(item: RawMaterial) = repo.update(item)

    suspend fun delete(id: String) = repo.delete(id)
}