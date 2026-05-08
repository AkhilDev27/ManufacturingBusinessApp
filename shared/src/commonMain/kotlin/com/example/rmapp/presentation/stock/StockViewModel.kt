package com.example.rmapp.presentation.stock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rmapp.domain.usecase.StockUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StockViewModel(
    private val useCase: StockUseCase
) : ViewModel() {

    val items = useCase.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val lowStock = useCase.getLowStock()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun produce(productId: Long) {
        viewModelScope.launch {
            useCase.produce(productId, 1.0)
        }
    }

    fun sell(productId: Long) {
        viewModelScope.launch {
            useCase.sell(productId, 1.0)
        }
    }
}