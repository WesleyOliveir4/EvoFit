package com.example.evofit.di

import com.example.evofit.data.datasource.LocalExerciseDataSource
import com.example.evofit.data.repository.OnboardingRepositoryImpl
import com.example.evofit.domain.repository.OnboardingRepository
import com.example.evofit.domain.usecase.GetExerciseDataUseCase
import com.example.evofit.ui.feature.home.viewmodel.HomeViewModel
import com.example.evofit.ui.feature.onboard.viewmodel.OnboardingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { LocalExerciseDataSource() }
    single<OnboardingRepository> { OnboardingRepositoryImpl(androidContext()) }
    
    factory { GetExerciseDataUseCase(get()) }
    
    viewModel { OnboardingViewModel(get(), get()) }
    viewModel { HomeViewModel(get()) }
}
