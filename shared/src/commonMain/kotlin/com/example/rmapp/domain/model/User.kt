package com.example.rmapp.domain.model

data class User(
    val id: String,
    val username: String,
    val password: String,
    val roleId: String,
    val createdAt: Long,
    val updatedAt: Long
)