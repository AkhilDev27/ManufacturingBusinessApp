package com.example.rmapp.presentation.launch

import com.example.rmapp.domain.repository.AuthRepository

class LaunchViewModel(
    private val repo: AuthRepository
) {

    suspend fun isUserLoggedIn(): Boolean {
//        return repo.isUserAvailable()
        return true
    }
}