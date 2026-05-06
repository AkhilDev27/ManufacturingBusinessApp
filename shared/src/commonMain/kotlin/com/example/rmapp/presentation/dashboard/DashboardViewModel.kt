package com.example.rmapp.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rmapp.domain.usecase.DashboardUseCase
import com.example.rmapp.presentation.dashboard.model.DashboardData
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class DashboardViewModel(
    useCase: DashboardUseCase
) : ViewModel() {

    val uiState = useCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            DashboardData()
        )
}