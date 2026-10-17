package com.guaracode.evofit.di

import com.guaracode.evofit.data.repository.SupportRepositoryImpl
import com.guaracode.evofit.domain.repository.SupportRepository
import org.koin.dsl.module

actual val platformDataModule = module {
    single<SupportRepository> { SupportRepositoryImpl(get()) }
}
