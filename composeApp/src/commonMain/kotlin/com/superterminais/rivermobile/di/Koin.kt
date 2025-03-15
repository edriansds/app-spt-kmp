package com.superterminais.rivermobile.di

import com.example.rivermobile.auth.LoginViewModel
import com.superterminais.rivermobile.MainViewModel
import com.superterminais.rivermobile.core.createHttpClient
import com.superterminais.rivermobile.core.createHttpClientEngine
import com.superterminais.rivermobile.data.DataStoreRepository
import com.superterminais.rivermobile.data.KtorRiverPortHubApi
import com.superterminais.rivermobile.data.RiverPortHubRepository
import com.superterminais.rivermobile.screens.discount.details.DiscountDetailsViewModel
import com.superterminais.rivermobile.screens.discount.list.DiscountListViewModel
import com.superterminais.rivermobile.screens.extension.details.StorageExtensionDetailsViewModel
import com.superterminais.rivermobile.screens.extension.list.StorageExtensionListViewModel
import com.superterminais.rivermobile.screens.profile.ProfileViewModel
import com.superterminais.rivermobile.screens.synchronization.SyncAnalyzedDiscountsViewModel
import com.superterminais.rivermobile.screens.synchronization.SyncAnalyzedExtensionsViewModel
import com.superterminais.rivermobile.screens.synchronization.SyncViewModel
import io.ktor.client.HttpClient
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

expect val preferencesModule: Module

val dataModule = module {

    single<HttpClient> { createHttpClient(createHttpClientEngine()) }

    single { KtorRiverPortHubApi(get()) }
    single {
        RiverPortHubRepository(get())
    }

    single { DataStoreRepository(get()) }
}

val viewModelModule = module {
    factoryOf(::LoginViewModel)
    factoryOf(::MainViewModel)

    factoryOf(::ProfileViewModel)

    factoryOf(::DiscountListViewModel)
    factoryOf(::DiscountDetailsViewModel)

    factoryOf(::StorageExtensionListViewModel)
    factoryOf(::StorageExtensionDetailsViewModel)

    factoryOf(::SyncViewModel)
    factoryOf(::SyncAnalyzedDiscountsViewModel)
    factoryOf(::SyncAnalyzedExtensionsViewModel)
}
