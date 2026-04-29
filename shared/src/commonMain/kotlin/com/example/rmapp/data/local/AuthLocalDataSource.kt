package com.example.rmapp.data.local

import com.example.rmapp.db.RMAppCamphorDatabase
import com.example.rmapp.db.auth.User

class AuthLocalDataSource( db: RMAppCamphorDatabase ) {

    private val queries = db.authQueries

    fun getUserByUsername(username: String): User? {
        return queries.getUserByUsername(username)
            .executeAsOneOrNull()
    }
}