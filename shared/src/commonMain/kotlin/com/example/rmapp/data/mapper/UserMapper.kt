package com.example.rmapp.data.mapper

import com.example.rmapp.db.auth.User
import com.example.rmapp.domain.model.User as DomainUser

fun User.toDomain(): DomainUser {
    return DomainUser(
        id = id,
        username = username,
        password = password,
        roleId = roleId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}