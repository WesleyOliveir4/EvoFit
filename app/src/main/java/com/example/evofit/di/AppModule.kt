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
import com.example.evofit.domain.usecase.GetActiveWorkoutSessionUseCase
import com.example.evofit.domain.usecase.GetActiveWorkoutSessionUseCaseImpl
import com.example.evofit.domain.usecase.GetAverageWorkoutTimeUseCase
import com.example.evofit.domain.usecase.GetAverageWorkoutTimeUseCaseImpl
import com.example.evofit.domain.usecase.GetCurrentWeekRangeUseCase
import com.example.evofit.domain.usecase.GetEvoHomeSummaryUseCase
import com.example.evofit.domain.usecase.GetEvoHomeSummaryUseCaseImpl
import com.example.evofit.domain.usecase.GetExerciseDataUseCase
import com.example.evofit.domain.usecase.GetKmPerWeekUseCase
import com.example.evofit.domain.usecase.GetKmPerWeekUseCaseImpl
import com.example.evofit.domain.usecase.GetLeastTrainedGroupUseCase
import com.example.evofit.domain.usecase.GetLeastTrainedGroupUseCaseImpl
import com.example.evofit.domain.usecase.GetMostEvolvedMuscleUseCase
import com.example.evofit.domain.usecase.GetMostEvolvedMuscleUseCaseImpl
import com.example.evofit.domain.usecase.GetOnboardingDataUseCase
import com.example.evofit.domain.usecase.GetOnboardingDataUseCaseImpl
import com.example.evofit.domain.usecase.GetStrengthGainsUseCase
import com.example.evofit.domain.usecase.GetStrengthGainsUseCaseImpl
import com.example.evofit.domain.usecase.GetUserIdUseCase
import com.example.evofit.domain.usecase.GetUserIdUseCaseImpl
import com.example.evofit.domain.usecase.GetWorkoutByIdUseCase
import com.example.evofit.domain.usecase.GetWorkoutByIdUseCaseImpl
import com.example.evofit.domain.usecase.GetWorkoutDoneByIdUseCase
import com.example.evofit.domain.usecase.GetWorkoutDoneHistoryUseCase
import com.example.evofit.domain.usecase.GetWorkoutsCountUseCase
import com.example.evofit.domain.usecase.GetWorkoutsCountUseCaseImpl
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
import com.example.evofit.domain.usecase.UpdateWorkoutUseCase
import com.example.evofit.domain.usecase.UpdateWorkoutUseCaseImpl
import com.example.evofit.domain.usecase.UpdateWorkoutsOrderUseCase
import com.example.evofit.domain.usecase.UpdateWorkoutsOrderUseCaseImpl
import com.example.evofit.presentation.ui.feature.evo.home.viewmodel.EvoHomeViewModel
import com.example.evofit.presentation.ui.feature.home.viewmodel.HomeViewModel
import com.example.evofit.presentation.ui.feature.onboard.viewmodel.OnboardingViewModel
import com.example.evofit.presentation.ui.feature.splash.SplashViewModel
import com.example.evofit.presentation.ui.feature.workout.createworkout.viewmodel.ConfigureWorkoutViewModel
import com.example.evofit.presentation.ui.feature.workout.createworkout.viewmodel.NewWorkoutViewModel
import com.example.evofit.presentation.ui.feature.workout.createworkout.viewmodel.SelectExercisesViewModel
import com.example.evofit.presentation.ui.feature.workout.home.viewmodel.WorkoutViewModel
import com.example.evofit.presentation.ui.feature.workout.resume.viewmodel.WorkoutResumeViewModel
import com.example.evofit.presentation.ui.feature.workout.startworkout.viewmodel.WorkoutPreviewViewModel
import com.example.evofit.presentation.ui.feature.workout.startworkout.viewmodel.WorkoutStartViewModel
import com.example.evofit.domain.usecase.FilterTrainedMuscleGroupsUseCase
import com.example.evofit.domain.usecase.FilterTrainedMuscleGroupsUseCaseImpl
import com.example.evofit.domain.usecase.GetTrainedMuscleGroupsUseCase
import com.example.evofit.domain.usecase.GetTrainedMuscleGroupsUseCaseImpl
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     import com.example.evofit.domain.usecase.GetExercisesWithRecordCountUseCase
import com.example.evofit.domain.usecase.GetExercisesWithRecordCountUseCaseImpl
import com.example.evofit.domain.usecase.ProcessDistanceAnalyticsUseCase
import com.example.evofit.domain.usecase.ProcessDistanceAnalyticsUseCaseImpl
import com.example.evofit.domain.usecase.ProcessExerciseAnalyticsUseCase
import com.example.evofit.domain.usecase.ProcessExerciseAnalyticsUseCaseImpl
import com.example.evofit.domain.usecase.ProcessRepsAnalyticsUseCase
import com.example.evofit.domain.usecase.ProcessRepsAnalyticsUseCaseImpl
import com.example.evofit.domain.usecase.ProcessTimeAnalyticsUseCase
import com.example.evofit.domain.usecase.ProcessTimeAnalyticsUseCaseImpl
import com.example.evofit.domain.usecase.ProcessWeightAnalyticsUseCase
import com.example.evofit.domain.usecase.ProcessWeightAnalyticsUseCaseImpl
import com.example.evofit.domain.usecase.profile.CalculateGoalProgressUseCase
import com.example.evofit.domain.usecase.profile.GetActiveUserGoalsUseCase
import com.example.evofit.presentation.ui.feature.profile.goals.viewmodel.PersonalGoalsViewModel
import com.example.evofit.presentation.ui.feature.profile.home.viewmodel.ProfileViewModel
import com.example.evofit.presentation.ui.feature.profile.userdata.viewmodel.UserDataViewModel
import com.example.evofit.presentation.ui.feature.evo.analytics.viewmodel.EvoAnalyticsViewModel
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
    factory { GetWorkoutDoneByIdUseCase(get(), get()) }
    factory { GetCurrentWeekRangeUseCase() }
    factory<GetStrengthGainsUseCase> { GetStrengthGainsUseCaseImpl() }
    factory<GetMostEvolvedMuscleUseCase> { GetMostEvolvedMuscleUseCaseImpl() }
    factory<GetWorkoutsCountUseCase> { GetWorkoutsCountUseCaseImpl() }
    factory<GetLeastTrainedGroupUseCase> { GetLeastTrainedGroupUseCaseImpl() }
    factory<GetKmPerWeekUseCase> { GetKmPerWeekUseCaseImpl() }
    factory<GetAverageWorkoutTimeUseCase> { GetAverageWorkoutTimeUseCaseImpl() }
    factory<GetEvoHomeSummaryUseCase> { GetEvoHomeSummaryUseCaseImpl(get(), get(), get(), get(), get(), get(), get()) }
    factory<FilterTrainedMuscleGroupsUseCase> { FilterTrainedMuscleGroupsUseCaseImpl() }
    factory<GetTrainedMuscleGroupsUseCase> { GetTrainedMuscleGroupsUseCaseImpl(get()) }
    factory<GetExercisesWithRecordCountUseCase> { GetExercisesWithRecordCountUseCaseImpl(get()) }
    factory<UpdateWorkoutsOrderUseCase> { (UpdateWorkoutsOrderUseCaseImpl(get())) }
    factory<GetActiveWorkoutSessionUseCase> { GetActiveWorkoutSessionUseCaseImpl(get(), get()) }
    factory<StartWorkoutSessionUseCase> { StartWorkoutSessionUseCaseImpl(get()) }
    factory<UpdateCompletedSetsUseCase> { UpdateCompletedSetsUseCaseImpl(get()) }
    factory<ClearWorkoutSessionUseCase> { ClearWorkoutSessionUseCaseImpl(get()) }
    factory<DeleteWorkoutUseCase> { DeleteWorkoutUseCaseImpl(get()) }
    factory<UpdateWorkoutUseCase> { UpdateWorkoutUseCaseImpl(get()) }
    factory<ProcessWeightAnalyticsUseCase> { ProcessWeightAnalyticsUseCaseImpl() }
    factory<ProcessDistanceAnalyticsUseCase> { ProcessDistanceAnalyticsUseCaseImpl() }
    factory<ProcessTimeAnalyticsUseCase> { ProcessTimeAnalyticsUseCaseImpl() }
    factory<ProcessRepsAnalyticsUseCase> { ProcessRepsAnalyticsUseCaseImpl() }
    factory<ProcessExerciseAnalyticsUseCase> { ProcessExerciseAnalyticsUseCaseImpl(get(), get(), get(), get()) }
    factory { GetActiveUserGoalsUseCase(get()) }
    factory { CalculateGoalProgressUseCase(get(), get()) }
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
    viewModel {
        EvoHomeViewModel(
            get(),
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
    viewModel { (workoutId: Long?, workoutDoneId: Long?, editWorkoutId: Long?) ->
        WorkoutResumeViewModel(
            workoutId = workoutId,
            workoutDoneId = workoutDoneId,
            editWorkoutId = editWorkoutId,
            getWorkoutByIdUseCase = get(),
            getWorkoutDoneByIdUseCase = get()
        )
    }
    viewModel { (workoutId: Int) ->
        WorkoutPreviewViewModel(
            workoutId = workoutId,
            getWorkoutByIdUseCase = get(),
            getExerciseDataUseCase = get(),
            deleteWorkoutUseCase = get(),
            getActiveWorkoutSessionUseCase = get(),
            clearWorkoutSessionUseCase = get()
        )
    }
    viewModel { (workoutId: Int) ->
        WorkoutStartViewModel(
            workoutId = workoutId,
            getWorkoutByIdUseCase = get(),
            getExerciseDataUseCase = get(),
            saveWorkoutDoneUseCase = get(),
            getUserIdUseCase = get(),
            getWorkoutDoneHistoryUseCase = get(),
            getActiveWorkoutSessionUseCase = get(),
            startWorkoutSessionUseCase = get(),
            updateCompletedSetsUseCase = get(),
            clearWorkoutSessionUseCase = get()
        )
    }
}

val evoModule = module {
    viewModel { EvoAnalyticsViewModel(get(), get(), get(), get(), get()) }
}

val profileModule = module {
    viewModel { ProfileViewModel(get(), get(), get(), get()) }
    viewModel { UserDataViewModel(get(), get()) }
    viewModel { PersonalGoalsViewModel(get(), get(), get(), get(), get()) }
}

val appModule = listOf(
    dataModule,
    domainModule,
    splashModule,
    onboardingModule,
    homeModule,
    workoutModule,
    evoModule,
    profileModule
)
