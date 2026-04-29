package com.example.rmapp.domain.usecase

import com.example.rmapp.domain.model.User
import com.example.rmapp.domain.repository.AuthRepository

class LoginUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        username: String,
        password: String
    ): Result<Unit> {

        if (username.isBlank() || password.isBlank()) {
            return Result.failure(Exception("Fields cannot be empty"))
        }

        val success = repository.login(username, password)

        return if (success) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Invalid credentials"))
        }
    }
}