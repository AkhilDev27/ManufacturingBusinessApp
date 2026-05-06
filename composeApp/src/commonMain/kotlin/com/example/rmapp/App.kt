package com.example.rmapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.rmapp.data.local.AuthLocalDataSource
import com.example.rmapp.db.DatabaseDriverFactory
import com.example.rmapp.db.RMAppCamphorDatabase
import com.example.rmapp.domain.repository.AuthRepository
import com.example.rmapp.domain.repository.AuthRepositoryImpl
import com.example.rmapp.domain.repository.CustomerRepository
import com.example.rmapp.domain.repository.CustomerRepositoryImpl
import com.example.rmapp.domain.repository.InventoryRepository
import com.example.rmapp.domain.repository.InventoryRepositoryImpl
import com.example.rmapp.domain.repository.ProductRepository
import com.example.rmapp.domain.repository.ProductRepositoryImpl
import com.example.rmapp.domain.repository.ProductionRepository
import com.example.rmapp.domain.repository.ProductionRepositoryImpl
import com.example.rmapp.domain.repository.SalesRepository
import com.example.rmapp.domain.repository.SalesRepositoryImpl
import com.example.rmapp.domain.repository.StockRepository
import com.example.rmapp.domain.repository.StockRepositoryImpl
import com.example.rmapp.domain.usecase.CustomerUseCase
import com.example.rmapp.domain.usecase.DashboardUseCase
import com.example.rmapp.domain.usecase.InventoryUseCase
import com.example.rmapp.domain.usecase.LoginUseCase
import com.example.rmapp.domain.usecase.ProductUseCase
import com.example.rmapp.domain.usecase.StockUseCase
import com.example.rmapp.presentation.customers.CustomerViewModel
import com.example.rmapp.presentation.dashboard.DashboardViewModel
import com.example.rmapp.presentation.inventory.InventoryViewModel
import com.example.rmapp.presentation.launch.LaunchViewModel
import com.example.rmapp.presentation.login.LoginViewModel
import com.example.rmapp.presentation.products.ProductViewModel
import com.example.rmapp.presentation.stock.StockViewModel
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
    factory { DashboardUseCase(productionRepo = get(), salesRepo = get(), stockRepo = get(), productRepo = get()) }
    factory { DashboardViewModel(useCase = get()) }
}

val masterModule = module {
    single<ProductionRepository> {
        ProductionRepositoryImpl(get())
    }

    single<SalesRepository> {
        SalesRepositoryImpl(get())
    }

    single<StockRepository> {
        StockRepositoryImpl(
            stockQueries = get(),
            txQueries = get()
        )
    }

    single<ProductRepository> {
        ProductRepositoryImpl(get())
    }

    single<CustomerRepository> {
        CustomerRepositoryImpl(get())
    }

    single<InventoryRepository> {
        InventoryRepositoryImpl(get())
    }

    // UseCases
    factory { StockUseCase(get()) }
    factory { ProductUseCase(get()) }
    factory { CustomerUseCase(get()) }
    factory { InventoryUseCase(get()) }

    // ViewModels
    viewModel { StockViewModel(get()) }
    viewModel { ProductViewModel(get()) }
    viewModel { CustomerViewModel(get()) }
    viewModel { InventoryViewModel(get()) }

    single { get<RMAppCamphorDatabase>().stockQueries }
    single { get<RMAppCamphorDatabase>().stockTransactionQueries }

    single { get<RMAppCamphorDatabase>().productsQueries }
    single { get<RMAppCamphorDatabase>().customerQueries }
    single { get<RMAppCamphorDatabase>().inventoryQueries }
}

val appModule = module {

    includes(
        coreModule,
        authModule,
        dashboardModule,
        masterModule
    )
}

@Composable
@Preview
fun App() {
    MainApp(appModule)
}