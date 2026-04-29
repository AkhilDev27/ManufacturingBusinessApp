package com.example.rmapp.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            RMAppCamphorDatabase.Schema,
            "RMAppCamphorDatabase.db"
        )
    }
}