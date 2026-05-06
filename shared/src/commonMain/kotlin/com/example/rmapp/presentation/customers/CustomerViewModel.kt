package com.example.rmapp.presentation.customers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rmapp.domain.model.Customer
import com.example.rmapp.domain.usecase.CustomerUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CustomerViewModel(
    private val useCase: CustomerUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _editingCustomer = MutableStateFlow<Customer?>(null)
    val editingCustomer: StateFlow<Customer?> = _editingCustomer.asStateFlow()

    val customers: StateFlow<List<Customer>> =
        _searchQuery
            .debounce(300)
            .distinctUntilChanged()
            .flatMapLatest { query ->
                if (query.isBlank()) useCase.getAll()
                else useCase.search(query)
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun search(query: String) {
        _searchQuery.value = query
    }

    fun save(customer: Customer) {
        viewModelScope.launch {
            val editing = _editingCustomer.value
            if (editing != null) {
                useCase.update(customer.copy(id = editing.id))
            } else {
                useCase.add(customer)
            }
            _editingCustomer.value = null
        }
    }

    fun delete(id: String) {
        viewModelScope.launch {
            useCase.delete(id)
        }
    }

    fun edit(customer: Customer) {
        _editingCustomer.value = customer
    }

    fun clearEdit() {
        _editingCustomer.value = null
    }
}