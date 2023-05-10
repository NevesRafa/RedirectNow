package com.kansha.phone2whats.internal

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Phone2WhatsApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@Phone2WhatsApplication)
            modules(appModule)
        }
    }
}