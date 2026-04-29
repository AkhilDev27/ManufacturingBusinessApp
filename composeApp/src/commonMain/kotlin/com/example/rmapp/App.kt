package com.example.rmapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.rmapp.data.local.AuthLocalDataSource
import com.example.rmapp.db.DatabaseDriverFactory
import com.example.rmapp.db.RMAppCamphorDatabase
import com.example.rmapp.domain.repository.AuthRepository
import com.example.rmapp.domain.repository.AuthRepositoryImpl
import com.example.rmapp.domain.repository.ProductionRepository
import com.example.rmapp.domain.repository.ProductionRepositoryImpl
import com.example.rmapp.domain.repository.SalesRepository
import com.example.rmapp.domain.repository.SalesRepositoryImpl
import com.example.rmapp.domain.repository.StockRepository
import com.example.rmapp.domain.repository.StockRepositoryImpl
import com.example.rmapp.domain.usecase.DashboardUseCase
import com.example.rmapp.domain.usecase.LoginUseCase
import com.example.rmapp.presentation.dashboard.DashboardViewModel
import com.example.rmapp.presentation.launch.LaunchViewModel
import com.example.rmapp.presentation.login.LoginViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val coreModule = module {
    single { DatabaseDriverFactory() } // Android gets Context injected
    single {
        val driverFactory = get<DatabaseDriverFactory>()
        val driver = driverFactory.createDriver()
        RMAppCamphorDatabase(driver)
    }
}

val authModule = module {
    single { AuthLocalDataSource(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single { LoginUseCase(get()) }
    factory { LoginViewModel(get()) }
    factory { LaunchViewModel(get()) }
}

val dashboardModule = module {
    factory { DashboardUseCase(productionRepo = get(), salesRepo = get(), stockRepo = get()) }
    factory { DashboardViewModel(getDashboardDataUseCase = get()) }
}

val appModule = module {

    single<ProductionRepository> {
        ProductionRepositoryImpl(get())
    }

    single<SalesRepository> {
        SalesRepositoryImpl(get())
    }

    single<StockRepository> {
        StockRepositoryImpl(get())
    }

    includes(
        coreModule,
        authModule,
        dashboardModule
    )
}

@Composable
@Preview
fun App() {
    MainApp(appModule)
}