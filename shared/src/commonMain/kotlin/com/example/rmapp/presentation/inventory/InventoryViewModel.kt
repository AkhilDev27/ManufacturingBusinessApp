package com.example.rmapp.presentation.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rmapp.domain.model.RawMaterial
import com.example.rmapp.domain.usecase.InventoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InventoryViewModel(
    private val useCase: InventoryUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _editing = MutableStateFlow<RawMaterial?>(null)
    val editing = _editing.asStateFlow()

    val items =
        _searchQuery
            .debounce(300)
            .flatMapLatest {
                if (it.isBlank()) useCase.getAll()
                else useCase.search(it)
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun search(q: String) {
        _searchQuery.value = q
    }

    fun save(item: RawMaterial) {
        viewModelScope.launch {
            val e = _editing.value
            if (e != null) useCase.update(item.copy(id = e.id))
            else useCase.add(item)
            _editing.value = null
        }
    }

    fun delete(id: String) {
        viewModelScope.launch { useCase.delete(id) }
    }

    fun edit(item: RawMaterial) {
        _editing.value = item
    }

    fun clearEdit() {
        _editing.value = null
    }
}