package com.example.evofit.di

import androidx.room.Room
import com.example.evofit.data.datasource.LocalExerciseDataSource
import com.example.evofit.data.local.AppDatabase
import com.example.evofit.data.repository.OnboardingRepositoryImpl
import com.example.evofit.domain.repository.OnboardingRepository
import com.example.evofit.domain.usecase.CompleteOnboardingUseCase
import com.example.evofit.domain.usecase.GetExerciseDataUseCase
import com.example.evofit.domain.usecase.GetOnboardingDataUseCase
import com.example.evofit.domain.usecase.SaveOnboardingDataUseCase
import com.example.evofit.presentation.ui.feature.home.viewmodel.HomeViewModel
import com.example.evofit.presentation.ui.feature.onboard.viewmodel.OnboardingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "evofit_database"
        ).fallbackToDestructiveMigration()
         .build()
    }
    
    single { get<AppDatabase>().userDao() }

    single { LocalExerciseDataSource() }
    single<OnboardingRepository> { OnboardingRepositoryImpl(get()) }
    
    factory { GetExerciseDataUseCase(get()) }
    factory { GetOnboardingDataUseCase(get()) }
    factory { SaveOnboardingDataUseCase(get()) }
    factory { CompleteOnboardingUseCase(get()) }
    
    viewModel {
        OnboardingViewModel(
            get(),
            get(),
            get(),
            get()
        )
    }
    viewModel {
        HomeViewModel(
            get()
        )
    }
    viewModel {
        com.example.evofit.presentation.ui.feature.splash.SplashViewModel(
            get()
        )
    }
}
