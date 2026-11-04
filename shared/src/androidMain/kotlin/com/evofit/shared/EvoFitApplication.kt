package com.evofit.shared

import android.app.Application
import com.guaracode.evofit.di.sharedAppModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class EvoFitApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidLogger()
            androidContext(this@EvoFitApplication)
            modules(sharedAppModules)
        }
    }
}
