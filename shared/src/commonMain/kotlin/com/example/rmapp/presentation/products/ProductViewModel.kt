package com.example.rmapp.presentation.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rmapp.domain.model.Product
import com.example.rmapp.domain.usecase.ProductUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductViewModel(
    private val useCase: ProductUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _editingProduct = MutableStateFlow<Product?>(null)
    val editingProduct: StateFlow<Product?> = _editingProduct.asStateFlow()

    private var recentlyDeleted: Product? = null

    val products: StateFlow<List<Product>> =
        _searchQuery
            .debounce(300)
            .distinctUntilChanged()
            .flatMapLatest { query ->
                if (query.isBlank()) {
                    useCase.getAll()
                } else {
                    useCase.search(query)
                }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    fun search(query: String) {
        _searchQuery.value = query
    }

    fun save(product: Product) {
        viewModelScope.launch {
            val editing = _editingProduct.value

            if (editing != null) {
                useCase.update(product.copy(id = editing.id))
            } else {
                useCase.add(product)
            }

            _editingProduct.value = null
        }
    }

    fun delete(product: Product) {
        viewModelScope.launch {
            recentlyDeleted = product
            useCase.delete(product.id)
        }
    }

    fun undoDelete() {
        viewModelScope.launch {
            recentlyDeleted?.let {
                useCase.add(it)
                recentlyDeleted = null
            }
        }
    }

    fun confirmDelete() {
        recentlyDeleted = null
    }

    fun edit(product: Product) {
        _editingProduct.value = product
    }

    fun clearEdit() {
        _editingProduct.value = null
    }
}