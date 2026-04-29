package com.example.rmapp.presentation.inventory

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rmapp.domain.model.RawMaterial
import com.example.rmapp.domain.usecase.InventoryUseCase
import kotlinx.coroutines.launch

class InventoryViewModel(
    private val useCases: InventoryUseCase
) : ViewModel() {

    var items by mutableStateOf(listOf<RawMaterial>())
        private set

    var editingItem by mutableStateOf<RawMaterial?>(null)
        private set

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            items = useCases.getAll()
        }
    }

    fun save(item: RawMaterial) {
        viewModelScope.launch {
            useCases.save(item)
            load()
            editingItem = null
        }
    }

    fun delete(id: String) {
        viewModelScope.launch {
            useCases.delete(id)
            load()
        }
    }

    fun edit(item: RawMaterial) {
        editingItem = item
    }

    fun clearEdit() {
        editingItem = null
    }
}