package com.kansha.redirectNow.internal

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RedirectNowApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@RedirectNowApplication)
            modules(appModule)
        }
    }
}