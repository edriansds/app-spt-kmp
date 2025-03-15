package com.superterminais.rivermobile

import android.app.Application
import com.superterminais.rivermobile.di.initKoin
import org.koin.android.ext.koin.androidContext


class RiverPortHubApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@RiverPortHubApp)
        }
    }
}
