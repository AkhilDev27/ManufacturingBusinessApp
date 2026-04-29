package com.example.rmapp.db

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.koin.java.KoinJavaComponent

actual class DatabaseDriverFactory actual constructor() {
    val context: Context = KoinJavaComponent.get(Context::class.java)

    actual fun createDriver(): SqlDriver = AndroidSqliteDriver(RMAppCamphorDatabase.Schema, context, "RMAppCamphorDatabase.db")
}