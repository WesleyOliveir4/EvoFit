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
import com.example.evofit.presentation.ui.feature.splash.SplashViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {
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
}

val domainModule = module {
    factory { GetExerciseDataUseCase(get()) }
    factory { GetOnboardingDataUseCase(get()) }
    factory { SaveOnboardingDataUseCase(get()) }
    factory { CompleteOnboardingUseCase(get()) }
}

val splashModule = module {
    viewModel { SplashViewModel(get()) }
}

val onboardingModule = module {
    viewModel {
        OnboardingViewModel(
            get(),
            get(),
            get(),
            get()
        )
    }
}

val homeModule = module {
    viewModel {
        HomeViewModel(
            get()
        )
    }
}

val appModule = listOf(
    dataModule,
    domainModule,
    splashModule,
    onboardingModule,
    homeModule
)
