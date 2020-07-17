package com.one.fruitmanseller

import android.app.Application
import com.one.fruitmanseller.models.Seller
import com.one.fruitmanseller.repositories.OrderRepository
import com.one.fruitmanseller.repositories.ProductRepository
import com.one.fruitmanseller.repositories.SellerRepository
import com.one.fruitmanseller.ui.login.LoginViewModel
import com.one.fruitmanseller.ui.main.timeline.TimelineViewModel
import com.one.fruitmanseller.ui.product.ProductViewModel
import com.one.fruitmanseller.ui.register.RegisterViewModel
import com.one.fruitmanseller.webservices.ApiClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.core.logger.Level

class MyApp : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MyApp)
            modules(listOf(repositoryModules, viewModelModules, retrofitModule))
        }
    }
}

val retrofitModule = module {
    single { ApiClient.instance() }
}

val repositoryModules = module {
    factory { SellerRepository(get()) }
    factory { ProductRepository(get()) }
    factory { OrderRepository(get()) }
}

val viewModelModules = module {
    viewModel { RegisterViewModel(get()) }
    viewModel { LoginViewModel(get()) }

    viewModel { TimelineViewModel(get()) }
    viewModel { ProductViewModel(get()) }
}