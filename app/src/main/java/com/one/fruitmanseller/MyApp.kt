package com.one.fruitmanseller

import android.app.Application
import com.one.fruitmanseller.models.Seller
import com.one.fruitmanseller.repositories.*
import com.one.fruitmanseller.ui.complete.CompleteViewModel
import com.one.fruitmanseller.ui.forgot_password.ForgotPasswordViewModel
import com.one.fruitmanseller.ui.in_progress.InProgressViewModel
import com.one.fruitmanseller.ui.login.LoginViewModel
import com.one.fruitmanseller.ui.main.profile.ProfileViewModel
import com.one.fruitmanseller.ui.main.timeline.TimelineViewModel
import com.one.fruitmanseller.ui.order_in.OrderInViewModel
import com.one.fruitmanseller.ui.product.ProductViewModel
import com.one.fruitmanseller.ui.register.RegisterViewModel
import com.one.fruitmanseller.ui.update_profile.UpdateProfilViewModel
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
    single { FirebaseRepository() }
}

val repositoryModules = module {
    factory { SellerRepository(get()) }
    factory { ProductRepository(get()) }
    factory { OrderRepository(get()) }
    factory { SubDistrictRepository(get()) }
    factory { FruitRepository(get()) }
}

val viewModelModules = module {
    viewModel { RegisterViewModel(get(),get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { UpdateProfilViewModel(get()) }

    viewModel { TimelineViewModel(get(), get()) }
    viewModel { ProductViewModel(get(), get(), get()) }
    viewModel { ForgotPasswordViewModel(get()) }

    viewModel { CompleteViewModel(get()) }
    viewModel { InProgressViewModel(get()) }
    viewModel { OrderInViewModel(get()) }
}