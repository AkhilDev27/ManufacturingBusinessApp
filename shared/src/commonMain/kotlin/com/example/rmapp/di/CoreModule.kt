package com.example.rmapp.di

import com.example.rmapp.data.local.AuthLocalDataSource
import com.example.rmapp.db.DatabaseDriverFactory
import com.example.rmapp.db.RMAppCamphorDatabase
import com.example.rmapp.domain.repository.AuthRepository
import com.example.rmapp.domain.repository.AuthRepositoryImpl
import com.example.rmapp.domain.usecase.LoginUseCase
import com.example.rmapp.presentation.login.LoginViewModel
import org.koin.dsl.module
import kotlin.coroutines.EmptyCoroutineContext

val coreModule = module {

    single { DatabaseDriverFactory() } // Android gets Context injected
    single {
        val driverFactory = get<DatabaseDriverFactory>()
        val driver = driverFactory.createDriver()
        RMAppCamphorDatabase(driver)
    }
}

val appModule = module {
    single { AuthLocalDataSource(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single { LoginUseCase(get()) }
    factory { LoginViewModel(get()) }
}