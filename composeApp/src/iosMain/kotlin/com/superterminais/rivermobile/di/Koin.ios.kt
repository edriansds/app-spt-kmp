package com.superterminais.rivermobile.di

import com.superterminais.rivermobile.createDataStore
import org.koin.core.module.Module
import org.koin.dsl.module

actual val preferencesModule: Module
    get() = module {
        single { createDataStore() }
    }