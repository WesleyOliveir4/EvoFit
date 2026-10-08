package com.guaracode.evofit.core.monitoring.di

import com.guaracode.evofit.core.monitoring.CrashReporter
import com.guaracode.evofit.core.monitoring.FirebaseCrashReporter
import org.koin.dsl.module

val monitoringModule = module {
    single<CrashReporter> { FirebaseCrashReporter() }
}
