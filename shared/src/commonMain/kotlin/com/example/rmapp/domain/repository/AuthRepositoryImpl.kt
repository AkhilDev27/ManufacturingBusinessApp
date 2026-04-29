package com.example.rmapp.domain.repository

import com.example.rmapp.data.local.AuthLocalDataSource
import com.example.rmapp.data.mapper.toDomain
import com.example.rmapp.domain.model.User
import kotlin.time.Clock

class AuthRepositoryImpl(
    private val local: AuthLocalDataSource
) : AuthRepository {

    private val user1 = User(
        id = "1",
        username = "admin",
        password = "admin123", // plain password
        roleId = "ADMIN",
        createdAt = Clock.System.now().toEpochMilliseconds(),
        updatedAt = Clock.System.now().toEpochMilliseconds()
    )

    override suspend fun getUser(username: String): User? {
        return local.getUserByUsername(username)?.toDomain()
    }

    override suspend fun login(
        username: String,
        password: String
    ): Boolean {

        return username == user1.username && password == user1.password

        // Local
        /*val user = local.getUserByUsername(username)
            ?: return false

        return hasher.verify(password, user.passwordHash)*/
    }
}