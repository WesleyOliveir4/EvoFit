package com.example.evofit.di

import androidx.room.Room
import com.example.evofit.data.datasource.LocalExerciseDataSource
import com.example.evofit.data.local.AppDatabase
import com.example.evofit.data.repository.ExerciseRepositoryImpl
import com.example.evofit.data.repository.OnboardingRepositoryImpl
import com.example.evofit.data.repository.WorkoutRepositoryImpl
import com.example.evofit.data.repository.WorkoutSessionRepositoryImpl
import com.example.evofit.domain.repository.ExerciseRepository
import com.example.evofit.domain.repository.OnboardingRepository
import com.example.evofit.domain.repository.WorkoutRepository
import com.example.evofit.domain.repository.WorkoutSessionRepository
import com.example.evofit.domain.usecase.ClearWorkoutSessionUseCase
import com.example.evofit.domain.usecase.ClearWorkoutSessionUseCaseImpl
import com.example.evofit.domain.usecase.CompleteOnboardingUseCase
import com.example.evofit.domain.usecase.CompleteOnboardingUseCaseImpl
import com.example.evofit.domain.usecase.DeleteWorkoutUseCase
import com.example.evofit.domain.usecase.DeleteWorkoutUseCaseImpl
import com.example.evofit.domain.usecase.UpdateWorkoutUseCase
import com.example.evofit.domain.usecase.UpdateWorkoutUseCaseImpl
import com.example.evofit.domain.usecase.GetActiveWorkoutSessionUseCase
import com.example.evofit.domain.usecase.GetActiveWorkoutSessionUseCaseImpl
import com.example.evofit.domain.usecase.GetCurrentWeekRangeUseCase
import com.example.evofit.domain.usecase.GetExerciseDataUseCase
import com.example.evofit.domain.usecase.GetOnboardingDataUseCase
import com.example.evofit.domain.usecase.GetOnboardingDataUseCaseImpl
import com.example.evofit.domain.usecase.GetUserIdUseCase
import com.example.evofit.domain.usecase.GetUserIdUseCaseImpl
import com.example.evofit.domain.usecase.GetWorkoutByIdUseCase
import com.example.evofit.domain.usecase.GetWorkoutByIdUseCaseImpl
import com.example.evofit.domain.usecase.GetWorkoutDoneHistoryUseCase
import com.example.evofit.domain.usecase.GetWorkoutsUseCase
import com.example.evofit.domain.usecase.GetWorkoutsUseCaseImpl
import com.example.evofit.domain.usecase.IsOnboardingCompletedUseCase
import com.example.evofit.domain.usecase.IsOnboardingCompletedUseCaseImpl
import com.example.evofit.domain.usecase.SaveOnboardingDataUseCase
import com.example.evofit.domain.usecase.SaveOnboardingDataUseCaseImpl
import com.example.evofit.domain.usecase.SaveWorkoutDoneUseCase
import com.example.evofit.domain.usecase.SaveWorkoutUseCase
import com.example.evofit.domain.usecase.SaveWorkoutUseCaseImpl
import com.example.evofit.domain.usecase.StartWorkoutSessionUseCase
import com.example.evofit.domain.usecase.StartWorkoutSessionUseCaseImpl
import com.example.evofit.domain.usecase.UpdateCompletedSetsUseCase
import com.example.evofit.domain.usecase.UpdateCompletedSetsUseCaseImpl
import com.example.evofit.domain.usecase.UpdateWorkoutsOrderUseCase
import com.example.evofit.domain.usecase.UpdateWorkoutsOrderUseCaseImpl
import com.example.evofit.presentation.ui.feature.home.viewmodel.HomeViewModel
import com.example.evofit.presentation.ui.feature.onboard.viewmodel.OnboardingViewModel
import com.example.evofit.presentation.ui.feature.splash.SplashViewModel
import com.example.evofit.presentation.ui.feature.workout.createworkout.viewmodel.ConfigureWorkoutViewModel
import com.example.evofit.presentation.ui.feature.workout.createworkout.viewmodel.NewWorkoutViewModel
import com.example.evofit.presentation.ui.feature.workout.createworkout.viewmodel.SelectExercisesViewModel
import com.example.evofit.presentation.ui.feature.workout.home.viewmodel.WorkoutViewModel
import com.example.evofit.presentation.ui.feature.workout.startworkout.viewmodel.WorkoutPreviewViewModel
import com.example.evofit.presentation.ui.feature.workout.startworkout.viewmodel.WorkoutStartViewModel
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
    single<WorkoutSessionRepository> { WorkoutSessionRepositoryImpl(androidContext()) }
    single<OnboardingRepository> { OnboardingRepositoryImpl(get()) }
    single<WorkoutRepository> { WorkoutRepositoryImpl(get(), get()) }
    single<ExerciseRepository> { ExerciseRepositoryImpl(get()) }
}

val domainModule = module {
    factory { GetExerciseDataUseCase(get()) }
    factory<GetOnboardingDataUseCase> { GetOnboardingDataUseCaseImpl(get()) }
    factory<SaveOnboardingDataUseCase> { SaveOnboardingDataUseCaseImpl(get()) }
    factory<CompleteOnboardingUseCase> { CompleteOnboardingUseCaseImpl(get()) }
    factory<IsOnboardingCompletedUseCase> { IsOnboardingCompletedUseCaseImpl(get()) }
    factory<GetUserIdUseCase> { GetUserIdUseCaseImpl(get()) }
    factory<GetWorkoutsUseCase> { GetWorkoutsUseCaseImpl(get()) }
    factory<GetWorkoutByIdUseCase> { GetWorkoutByIdUseCaseImpl(get()) }
    factory<SaveWorkoutUseCase> { SaveWorkoutUseCaseImpl(get()) }
    factory { SaveWorkoutDoneUseCase(get()) }
    factory { GetWorkoutDoneHistoryUseCase(get()) }
    factory { GetCurrentWeekRangeUseCase() }
    factory<UpdateWorkoutsOrderUseCase> { (UpdateWorkoutsOrderUseCaseImpl(get())) }
    factory<GetActiveWorkoutSessionUseCase> { GetActiveWorkoutSessionUseCaseImpl(get(), get()) }
    factory<StartWorkoutSessionUseCase> { StartWorkoutSessionUseCaseImpl(get()) }
    factory<UpdateCompletedSetsUseCase> { UpdateCompletedSetsUseCaseImpl(get()) }
    factory<ClearWorkoutSessionUseCase> { ClearWorkoutSessionUseCaseImpl(get()) }
    factory<DeleteWorkoutUseCase> { DeleteWorkoutUseCaseImpl(get()) }
    factory<UpdateWorkoutUseCase> { UpdateWorkoutUseCaseImpl(get()) }
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

val workoutModule = module {
    viewModel {
        WorkoutViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    viewModel {
        NewWorkoutViewModel(
            get(),
        )
    }
    viewModel {
        SelectExercisesViewModel(
            get(),
            get()
        )
    }
    viewModel {
        ConfigureWorkoutViewModel(
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    viewModel { (workoutId: Int) ->
        WorkoutPreviewViewModel(
            workoutId = workoutId,
            get(),
            get(),
            get(),
            get()
        )
    }
    viewModel { (workoutId: Int) ->
        WorkoutStartViewModel(
            workoutId = workoutId,
            getWorkoutByIdUseCase = get(),
            getExerciseDataUseCase = get(),
            saveWorkoutDoneUseCase = get(),
            getUserIdUseCase = get(),
            getActiveWorkoutSessionUseCase = get(),
            startWorkoutSessionUseCase = get(),
            updateCompletedSetsUseCase = get(),
            clearWorkoutSessionUseCase = get()
        )
    }
}

val appModule = listOf(
    dataModule,
    domainModule,
    splashModule,
    onboardingModule,
    homeModule,
    workoutModule
)
