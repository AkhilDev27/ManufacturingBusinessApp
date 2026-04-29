package com.example.rmapp.domain.repository

import com.example.rmapp.domain.model.User

interface AuthRepository {

    suspend fun getUser(username: String): User?

    suspend fun login(username: String, password: String): Boolean

}