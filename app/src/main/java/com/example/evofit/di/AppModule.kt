package com.example.evofit.di

import com.example.evofit.core.network.ConnectivityObserver
import com.example.evofit.core.network.NetworkConnectivityObserver
import com.example.evofit.data.local.session.SessionManager
import com.example.evofit.data.repository.AuthRepositoryImpl
import com.example.evofit.domain.repository.AuthRepository
import com.example.evofit.domain.usecase.*
import com.example.evofit.presentation.ui.feature.authentication.apple.AppleSignInHandler
import com.example.evofit.presentation.ui.feature.authentication.google.GoogleSignInHandler
import com.example.evofit.presentation.ui.feature.authentication.viewmodel.*
import com.example.evofit.data.datasource.UserRemoteDataSource
import com.example.evofit.data.datasource.UserRemoteDataSourceImpl
import com.example.evofit.data.datasource.WorkoutRemoteDataSource
import com.example.evofit.data.datasource.WorkoutRemoteDataSourceImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.room.Room
import com.example.evofit.data.datasource.WorkoutLocalDataSource
import com.example.evofit.data.datasource.WorkoutLocalDataSourceImpl
import com.example.evofit.data.datasource.UserLocalDataSource
import com.example.evofit.data.datasource.UserLocalDataSourceImpl
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
import com.example.evofit.domain.usecase.GetCurrentWeekRangeUseCaseImpl
import com.example.evofit.domain.usecase.GetEvoHomeSummaryUseCase
import com.example.evofit.domain.usecase.GetEvoHomeSummaryUseCaseImpl
import com.example.evofit.domain.usecase.GetExercisesByGroupUseCase
import com.example.evofit.domain.usecase.GetExercisesByGroupUseCaseImpl
import com.example.evofit.domain.usecase.GetExercisesByIdsUseCase
import com.example.evofit.domain.usecase.GetExercisesByIdsUseCaseImpl
import com.example.evofit.domain.usecase.FilterWorkoutHistoryByPeriodUseCase
import com.example.evofit.domain.usecase.FilterWorkoutHistoryByPeriodUseCaseImpl
import com.example.evofit.domain.usecase.GetGoalSuggestionsUseCase
import com.example.evofit.domain.usecase.GetGoalSuggestionsUseCaseImpl
import com.example.evofit.domain.usecase.GetMuscleGroupsUseCase
import com.example.evofit.domain.usecase.GetMuscleGroupsUseCaseImpl
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
import com.example.evofit.domain.usecase.GetWorkoutDoneByIdUseCaseImpl
import com.example.evofit.domain.usecase.GetWorkoutDoneHistoryUseCase
import com.example.evofit.domain.usecase.GetWorkoutDoneHistoryUseCaseImpl
import com.example.evofit.domain.usecase.GetWorkoutsCountUseCase
import com.example.evofit.domain.usecase.GetWorkoutsCountUseCaseImpl
import com.example.evofit.domain.usecase.GetWorkoutsUseCase
import com.example.evofit.domain.usecase.GetWorkoutsUseCaseImpl
import com.example.evofit.domain.usecase.IsOnboardingCompletedUseCase
import com.example.evofit.domain.usecase.IsOnboardingCompletedUseCaseImpl
import com.example.evofit.domain.usecase.IsUserLoggedInUseCase
import com.example.evofit.domain.usecase.IsUserLoggedInUseCaseImpl
import com.example.evofit.domain.usecase.SaveOnboardingDataUseCase
import com.example.evofit.domain.usecase.SaveOnboardingDataUseCaseImpl
import com.example.evofit.domain.usecase.SaveWorkoutDoneUseCase
import com.example.evofit.domain.usecase.SaveWorkoutDoneUseCaseImpl
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
import com.example.evofit.domain.usecase.profile.CalculateGoalProgressUseCaseImpl
import com.example.evofit.domain.usecase.profile.GetActiveUserGoalsUseCase
import com.example.evofit.domain.usecase.profile.GetActiveUserGoalsUseCaseImpl
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
        ).addMigrations(
            AppDatabase.MIGRATION_4_5,
            AppDatabase.MIGRATION_5_6,
            AppDatabase.MIGRATION_6_7,
            AppDatabase.MIGRATION_7_8,
            AppDatabase.MIGRATION_8_9,
            AppDatabase.MIGRATION_9_10
        ).fallbackToDestructiveMigration()
            .build()
    }

    single { get<AppDatabase>().userDao() }
    single { LocalExerciseDataSource() }
    single { SessionManager(androidContext()) }
    single<ConnectivityObserver> { NetworkConnectivityObserver(androidContext()) }
    single<WorkoutLocalDataSource> { WorkoutLocalDataSourceImpl(get()) }
    single<UserLocalDataSource> { UserLocalDataSourceImpl(get()) }
    single { FirebaseFirestore.getInstance() }
    single<WorkoutRemoteDataSource> { WorkoutRemoteDataSourceImpl(get()) }
    single<UserRemoteDataSource> { UserRemoteDataSourceImpl(get()) }
    single<WorkoutSessionRepository> { WorkoutSessionRepositoryImpl(get()) }
    single<OnboardingRepository> { OnboardingRepositoryImpl(get(), get(), get(), get(), androidContext()) }
    single<WorkoutRepository> { WorkoutRepositoryImpl(get(), get(), get()) }
    single<ExerciseRepository> { ExerciseRepositoryImpl(get()) }
    single { FirebaseAuth.getInstance() }
    single { GoogleSignInHandler(androidContext()) }
    single { AppleSignInHandler(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
}

val domainModule = module {
    factory<GetMuscleGroupsUseCase> { GetMuscleGroupsUseCaseImpl(get()) }
    factory<GetExercisesByGroupUseCase> { GetExercisesByGroupUseCaseImpl(get()) }
    factory<GetExercisesByIdsUseCase> { GetExercisesByIdsUseCaseImpl(get()) }
    factory<GetGoalSuggestionsUseCase> { GetGoalSuggestionsUseCaseImpl(get()) }
    factory<GetOnboardingDataUseCase> { GetOnboardingDataUseCaseImpl(get()) }
    factory<SaveOnboardingDataUseCase> { SaveOnboardingDataUseCaseImpl(get(), get()) }
    factory<CompleteOnboardingUseCase> { CompleteOnboardingUseCaseImpl(get(), get(), get()) }
    factory<IsOnboardingCompletedUseCase> { IsOnboardingCompletedUseCaseImpl(get()) }
    factory<IsUserLoggedInUseCase> { IsUserLoggedInUseCaseImpl(get()) }
    factory<GetUserIdUseCase> { GetUserIdUseCaseImpl(get()) }
    factory<GetWorkoutsUseCase> { GetWorkoutsUseCaseImpl(get()) }
    factory<GetWorkoutsSinceUseCase> { GetWorkoutsSinceUseCaseImpl(get()) }
    factory<GetWorkoutByIdUseCase> { GetWorkoutByIdUseCaseImpl(get()) }
    factory<SaveWorkoutUseCase> { SaveWorkoutUseCaseImpl(get()) }
    factory<SaveWorkoutDoneUseCase> { SaveWorkoutDoneUseCaseImpl(get()) }
    factory<GetWorkoutDoneHistoryUseCase> { GetWorkoutDoneHistoryUseCaseImpl(get()) }
    factory<GetWorkoutDoneByIdUseCase> { GetWorkoutDoneByIdUseCaseImpl(get(), get()) }
    factory<GetCurrentWeekRangeUseCase> { GetCurrentWeekRangeUseCaseImpl() }
    factory<GetStrengthGainsUseCase> { GetStrengthGainsUseCaseImpl() }
    factory<GetMostEvolvedMuscleUseCase> { GetMostEvolvedMuscleUseCaseImpl() }
    factory<GetWorkoutsCountUseCase> { GetWorkoutsCountUseCaseImpl() }
    factory<GetLeastTrainedGroupUseCase> { GetLeastTrainedGroupUseCaseImpl() }
    factory<GetKmPerWeekUseCase> { GetKmPerWeekUseCaseImpl() }
    factory<GetAverageWorkoutTimeUseCase> { GetAverageWorkoutTimeUseCaseImpl() }
    factory<FilterWorkoutHistoryByPeriodUseCase> { FilterWorkoutHistoryByPeriodUseCaseImpl() }
    factory<GetEvoHomeSummaryUseCase> { GetEvoHomeSummaryUseCaseImpl(get(), get(), get(), get(), get(), get(), get(), get()) }
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
    factory<GetActiveUserGoalsUseCase> { GetActiveUserGoalsUseCaseImpl(get()) }
    factory<CalculateGoalProgressUseCase> { CalculateGoalProgressUseCaseImpl(get(), get()) }
    factory<RegisterUseCase> { RegisterUseCaseImpl(get()) }
    factory<LoginUseCase> { LoginUseCaseImpl(get()) }
    factory<LoginWithGoogleUseCase> { LoginWithGoogleUseCaseImpl(get()) }
    factory<LoginWithAppleUseCase> { LoginWithAppleUseCaseImpl(get()) }
    factory<SendPasswordResetCodeUseCase> { SendPasswordResetCodeUseCaseImpl(get()) }
    factory<VerifyPasswordResetCodeUseCase> { VerifyPasswordResetCodeUseCaseImpl(get()) }
    factory<ConfirmPasswordResetUseCase> { ConfirmPasswordResetUseCaseImpl(get()) }
    factory<LogoutUseCase> { LogoutUseCase(get()) }
    factory<SyncUserDataUseCase> { SyncUserDataUseCaseImpl(get()) }
    factory<NukeUserDataUseCase> { NukeUserDataUseCaseImpl(get()) }
}

val splashModule = module {
    viewModel { SplashViewModel(get(), get()) }
}

val onboardingModule = module {
    viewModel {
        OnboardingViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            androidContext(),
            get()
        )
    }
}

val homeModule = module {
    viewModel {
        HomeViewModel(
            get(),
            androidContext()
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
            get(),
            get()
        )
    }
    viewModel {
        SelectExercisesViewModel(
            get(),
            get(),
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
            get(),
            get()
        )
    }
    viewModel { (workoutId: String?, workoutDoneId: String?, editWorkoutId: String?, workoutNotFinishedId: String?) ->
        WorkoutResumeViewModel(
            workoutId = workoutId,
            workoutDoneId = workoutDoneId,
            editWorkoutId = editWorkoutId,
            workoutNotFinishedId = workoutNotFinishedId,
            getWorkoutByIdUseCase = get(),
            getWorkoutDoneByIdUseCase = get()
        )
    }
    viewModel { (workoutId: String) ->
        WorkoutPreviewViewModel(
            workoutId = workoutId,
            getWorkoutByIdUseCase = get(),
            getExercisesByIdsUseCase = get(),
            deleteWorkoutUseCase = get(),
            getActiveWorkoutSessionUseCase = get(),
            clearWorkoutSessionUseCase = get(),
            getMuscleGroupsUseCase = get()
        )
    }
    viewModel { (workoutId: String) ->
        WorkoutStartViewModel(
            workoutId = workoutId,
            getWorkoutByIdUseCase = get(),
            getExercisesByIdsUseCase = get(),
            saveWorkoutDoneUseCase = get(),
            getUserIdUseCase = get(),
            getActiveWorkoutSessionUseCase = get(),
            startWorkoutSessionUseCase = get(),
            updateCompletedSetsUseCase = get(),
            clearWorkoutSessionUseCase = get(),
            getMuscleGroupsUseCase = get()
        )
    }
}

val evoModule = module {
    viewModel { EvoAnalyticsViewModel(get(), get(), get(), get(), get(), get()) }
}

val profileModule = module {
    viewModel { ProfileViewModel(get(), get(), get(), get()) }
    viewModel { UserDataViewModel(get(), get()) }
    viewModel { PersonalGoalsViewModel(get(), get(), get(), get(), get(), get()) }
}

val authModule = module {
    viewModel { RegisterViewModel(get(), get()) }
    viewModel { LoginViewModel(get(), get(), get(), get(), get(), get(), get()) }
    viewModel { RecoverPasswordViewModel(get()) }
    viewModel { VerifyCodeViewModel(get(), get()) }
    viewModel { NewPasswordViewModel(get()) }
}

val appModule = listOf(
    dataModule,
    domainModule,
    splashModule,
    onboardingModule,
    homeModule,
    workoutModule,
    evoModule,
    profileModule,
    authModule
)
