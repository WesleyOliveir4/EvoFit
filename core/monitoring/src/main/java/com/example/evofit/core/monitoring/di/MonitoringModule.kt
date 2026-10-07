package com.example.evofit.core.monitoring.di

import com.example.evofit.core.monitoring.CrashReporter
import com.example.evofit.core.monitoring.FirebaseCrashReporter
import org.koin.dsl.module

val monitoringModule = module {
    single<CrashReporter> { FirebaseCrashReporter() }
}
