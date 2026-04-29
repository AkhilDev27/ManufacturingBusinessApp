package com.example.rmapp.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rmapp.domain.usecase.DashboardUseCase
import com.example.rmapp.presentation.dashboard.model.DashboardData
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class DashboardViewModel(
    private val getDashboardDataUseCase: DashboardUseCase
) : ViewModel() {

    val uiState: StateFlow<DashboardData> =
        getDashboardDataUseCase()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = DashboardData()
            )
}