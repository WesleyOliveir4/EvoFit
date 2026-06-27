package com.example.evofit

import android.app.Application
import com.example.evofit.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class EvoFitApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidLogger()
            androidContext(this@EvoFitApplication)
            modules(appModule)
        }
    }
}
