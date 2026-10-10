package com.guaracode.evofit.di

import com.guaracode.evofit.core.network.ConnectivityObserver
import com.guaracode.evofit.core.network.NetworkConnectivityObserver
import com.guaracode.evofit.data.local.session.SessionManager
import com.guaracode.evofit.data.repository.AuthRepositoryImpl
import com.guaracode.evofit.domain.repository.AuthRepository
import com.guaracode.evofit.domain.usecase.*
import com.guaracode.evofit.presentation.mapper.AuthErrorMapper
import com.guaracode.evofit.presentation.ui.feature.authentication.apple.AppleSignInHandler
import com.guaracode.evofit.presentation.ui.feature.authentication.google.GoogleSignInHandler
import com.guaracode.evofit.presentation.ui.feature.authentication.tracking.AuthTracker
import com.guaracode.evofit.presentation.ui.feature.authentication.tracking.AuthTrackerImpl
import com.guaracode.evofit.presentation.ui.feature.commons.goals.tracking.GoalWizardTracker
import com.guaracode.evofit.presentation.ui.feature.commons.goals.tracking.GoalWizardTrackerImpl
import com.guaracode.evofit.presentation.ui.feature.evo.analytics.tracking.EvoAnalyticsTracker
import com.guaracode.evofit.presentation.ui.feature.evo.analytics.tracking.EvoAnalyticsTrackerImpl
import com.guaracode.evofit.presentation.ui.feature.evo.home.tracking.EvoHomeTracker
import com.guaracode.evofit.presentation.ui.feature.evo.home.tracking.EvoHomeTrackerImpl
import com.guaracode.evofit.presentation.ui.feature.onboard.tracking.OnboardingTracker
import com.guaracode.evofit.presentation.ui.feature.onboard.tracking.OnboardingTrackerImpl
import com.guaracode.evofit.presentation.ui.feature.profile.about.tracking.AboutAppTracker
import com.guaracode.evofit.presentation.ui.feature.profile.about.tracking.AboutAppTrackerImpl
import com.guaracode.evofit.presentation.ui.feature.profile.developer.tracking.DeveloperTracker
import com.guaracode.evofit.presentation.ui.feature.profile.developer.tracking.DeveloperTrackerImpl
import com.guaracode.evofit.presentation.ui.feature.profile.goals.tracking.PersonalGoalsTracker
import com.guaracode.evofit.presentation.ui.feature.profile.goals.tracking.PersonalGoalsTrackerImpl
import com.guaracode.evofit.presentation.ui.feature.profile.home.tracking.ProfileHomeTracker
import com.guaracode.evofit.presentation.ui.feature.profile.home.tracking.ProfileHomeTrackerImpl
import com.guaracode.evofit.presentation.ui.feature.profile.support.tracking.SupportTracker
import com.guaracode.evofit.presentation.ui.feature.profile.support.tracking.SupportTrackerImpl
import com.guaracode.evofit.presentation.ui.feature.profile.userdata.tracking.UserDataTracker
import com.guaracode.evofit.presentation.ui.feature.profile.userdata.tracking.UserDataTrackerImpl
import com.guaracode.evofit.presentation.ui.feature.splash.tracking.SplashTracker
import com.guaracode.evofit.presentation.ui.feature.splash.tracking.SplashTrackerImpl
import com.guaracode.evofit.presentation.ui.feature.workout.createworkout.tracking.CreateWorkoutTracker
import com.guaracode.evofit.presentation.ui.feature.workout.createworkout.tracking.CreateWorkoutTrackerImpl
import com.guaracode.evofit.presentation.ui.feature.workout.home.tracking.WorkoutHomeTracker
import com.guaracode.evofit.presentation.ui.feature.workout.home.tracking.WorkoutHomeTrackerImpl
import com.guaracode.evofit.presentation.ui.feature.workout.resume.tracking.WorkoutResumeTracker
import com.guaracode.evofit.presentation.ui.feature.workout.resume.tracking.WorkoutResumeTrackerImpl
import com.guaracode.evofit.presentation.ui.feature.workout.startworkout.tracking.StartWorkoutTracker
import com.guaracode.evofit.presentation.ui.feature.workout.startworkout.tracking.StartWorkoutTrackerImpl
import com.guaracode.evofit.presentation.ui.feature.authentication.viewmodel.*
import com.guaracode.evofit.data.datasource.UserRemoteDataSource
import com.guaracode.evofit.data.datasource.UserRemoteDataSourceImpl
import com.guaracode.evofit.data.datasource.WorkoutRemoteDataSource
import com.guaracode.evofit.data.datasource.WorkoutRemoteDataSourceImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.room.Room
import com.guaracode.evofit.data.datasource.WorkoutLocalDataSource
import com.guaracode.evofit.data.datasource.WorkoutLocalDataSourceImpl
import com.guaracode.evofit.data.datasource.UserLocalDataSource
import com.guaracode.evofit.data.datasource.UserLocalDataSourceImpl
import com.guaracode.evofit.data.datasource.LocalExerciseDataSource
import com.guaracode.evofit.data.local.AppDatabase
import com.guaracode.evofit.data.repository.ExerciseRepositoryImpl
import com.guaracode.evofit.data.repository.OnboardingRepositoryImpl
import com.guaracode.evofit.data.repository.WorkoutRepositoryImpl
import com.guaracode.evofit.data.repository.WorkoutSessionRepositoryImpl
import com.guaracode.evofit.data.repository.SupportRepositoryImpl
import com.guaracode.evofit.domain.repository.ExerciseRepository
import com.guaracode.evofit.domain.repository.OnboardingRepository
import com.guaracode.evofit.domain.repository.WorkoutRepository
import com.guaracode.evofit.domain.repository.WorkoutSessionRepository
import com.guaracode.evofit.domain.repository.SupportRepository
import com.guaracode.evofit.domain.usecase.ClearWorkoutSessionUseCase
import com.guaracode.evofit.domain.usecase.ClearWorkoutSessionUseCaseImpl
import com.guaracode.evofit.domain.usecase.CompleteOnboardingUseCase
import com.guaracode.evofit.domain.usecase.CompleteOnboardingUseCaseImpl
import com.guaracode.evofit.domain.usecase.DeleteWorkoutUseCase
import com.guaracode.evofit.domain.usecase.DeleteWorkoutUseCaseImpl
import com.guaracode.evofit.domain.usecase.GetActiveWorkoutSessionUseCase
import com.guaracode.evofit.domain.usecase.GetActiveWorkoutSessionUseCaseImpl
import com.guaracode.evofit.domain.usecase.GetAverageWorkoutTimeUseCase
import com.guaracode.evofit.domain.usecase.GetAverageWorkoutTimeUseCaseImpl
import com.guaracode.evofit.domain.usecase.GetCurrentWeekRangeUseCase
import com.guaracode.evofit.domain.usecase.GetCurrentWeekRangeUseCaseImpl
import com.guaracode.evofit.domain.usecase.GetEvoHomeSummaryUseCase
import com.guaracode.evofit.domain.usecase.GetEvoHomeSummaryUseCaseImpl
import com.guaracode.evofit.domain.usecase.GetExercisesByGroupUseCase
import com.guaracode.evofit.domain.usecase.GetExercisesByGroupUseCaseImpl
import com.guaracode.evofit.domain.usecase.GetExercisesByIdsUseCase
import com.guaracode.evofit.domain.usecase.GetExercisesByIdsUseCaseImpl
import com.guaracode.evofit.domain.usecase.FilterWorkoutHistoryByPeriodUseCase
import com.guaracode.evofit.domain.usecase.FilterWorkoutHistoryByPeriodUseCaseImpl
import com.guaracode.evofit.domain.usecase.GetGoalSuggestionsUseCase
import com.guaracode.evofit.domain.usecase.GetGoalSuggestionsUseCaseImpl
import com.guaracode.evofit.domain.usecase.GetMuscleGroupsUseCase
import com.guaracode.evofit.domain.usecase.GetMuscleGroupsUseCaseImpl
import com.guaracode.evofit.domain.usecase.GetKmPerWeekUseCase
import com.guaracode.evofit.domain.usecase.GetKmPerWeekUseCaseImpl
import com.guaracode.evofit.domain.usecase.GetLeastTrainedGroupUseCase
import com.guaracode.evofit.domain.usecase.GetLeastTrainedGroupUseCaseImpl
import com.guaracode.evofit.domain.usecase.GetMostEvolvedMuscleUseCase
import com.guaracode.evofit.domain.usecase.GetMostEvolvedMuscleUseCaseImpl
import com.guaracode.evofit.domain.usecase.GetOnboardingDataUseCase
import com.guaracode.evofit.domain.usecase.GetOnboardingDataUseCaseImpl
import com.guaracode.evofit.domain.usecase.GetStrengthGainsUseCase
import com.guaracode.evofit.domain.usecase.GetStrengthGainsUseCaseImpl
import com.guaracode.evofit.domain.usecase.GetUserIdUseCase
import com.guaracode.evofit.domain.usecase.GetUserIdUseCaseImpl
import com.guaracode.evofit.domain.usecase.GetWorkoutByIdUseCase
import com.guaracode.evofit.domain.usecase.GetWorkoutByIdUseCaseImpl
import com.guaracode.evofit.domain.usecase.GetWorkoutDoneByIdUseCase
import com.guaracode.evofit.domain.usecase.GetWorkoutDoneByIdUseCaseImpl
import com.guaracode.evofit.domain.usecase.GetWorkoutDoneHistoryUseCase
import com.guaracode.evofit.domain.usecase.GetWorkoutDoneHistoryUseCaseImpl
import com.guaracode.evofit.domain.usecase.GetWorkoutsCountUseCase
import com.guaracode.evofit.domain.usecase.GetWorkoutsCountUseCaseImpl
import com.guaracode.evofit.domain.usecase.GetWorkoutsUseCase
import com.guaracode.evofit.domain.usecase.GetWorkoutsUseCaseImpl
import com.guaracode.evofit.domain.usecase.IsOnboardingCompletedUseCase
import com.guaracode.evofit.domain.usecase.IsOnboardingCompletedUseCaseImpl
import com.guaracode.evofit.domain.usecase.IsUserLoggedInUseCase
import com.guaracode.evofit.domain.usecase.IsUserLoggedInUseCaseImpl
import com.guaracode.evofit.domain.usecase.SaveOnboardingDataUseCase
import com.guaracode.evofit.domain.usecase.SaveOnboardingDataUseCaseImpl
import com.guaracode.evofit.domain.usecase.SaveWorkoutDoneUseCase
import com.guaracode.evofit.domain.usecase.SaveWorkoutDoneUseCaseImpl
import com.guaracode.evofit.domain.usecase.SaveWorkoutUseCase
import com.guaracode.evofit.domain.usecase.SaveWorkoutUseCaseImpl
import com.guaracode.evofit.domain.usecase.StartWorkoutSessionUseCase
import com.guaracode.evofit.domain.usecase.StartWorkoutSessionUseCaseImpl
import com.guaracode.evofit.domain.usecase.UpdateCompletedSetsUseCase
import com.guaracode.evofit.domain.usecase.UpdateCompletedSetsUseCaseImpl
import com.guaracode.evofit.domain.usecase.UpdateWorkoutUseCase
import com.guaracode.evofit.domain.usecase.UpdateWorkoutUseCaseImpl
import com.guaracode.evofit.domain.usecase.UpdateWorkoutsOrderUseCase
import com.guaracode.evofit.domain.usecase.UpdateWorkoutsOrderUseCaseImpl
import com.guaracode.evofit.presentation.ui.feature.evo.home.viewmodel.EvoHomeViewModel
import com.guaracode.evofit.presentation.ui.feature.onboard.viewmodel.OnboardingViewModel
import com.guaracode.evofit.presentation.ui.feature.splash.SplashViewModel
import com.guaracode.evofit.presentation.ui.feature.workout.createworkout.viewmodel.ConfigureWorkoutViewModel
import com.guaracode.evofit.presentation.ui.feature.workout.createworkout.viewmodel.NewWorkoutViewModel
import com.guaracode.evofit.presentation.ui.feature.workout.createworkout.viewmodel.SelectExercisesViewModel
import com.guaracode.evofit.presentation.ui.feature.workout.home.viewmodel.WorkoutViewModel
import com.guaracode.evofit.presentation.ui.feature.workout.resume.viewmodel.WorkoutResumeViewModel
import com.guaracode.evofit.presentation.ui.feature.workout.startworkout.viewmodel.WorkoutPreviewViewModel
import com.guaracode.evofit.presentation.ui.feature.workout.startworkout.viewmodel.WorkoutStartViewModel
import com.guaracode.evofit.domain.usecase.FilterTrainedMuscleGroupsUseCase
import com.guaracode.evofit.domain.usecase.FilterTrainedMuscleGroupsUseCaseImpl
import com.guaracode.evofit.domain.usecase.GetTrainedMuscleGroupsUseCase
import com.guaracode.evofit.domain.usecase.GetTrainedMuscleGroupsUseCaseImpl
import com.guaracode.evofit.domain.usecase.GetExercisesWithRecordCountUseCase
import com.guaracode.evofit.domain.usecase.GetExercisesWithRecordCountUseCaseImpl
import com.guaracode.evofit.domain.usecase.ProcessDistanceAnalyticsUseCase
import com.guaracode.evofit.domain.usecase.ProcessDistanceAnalyticsUseCaseImpl
import com.guaracode.evofit.domain.usecase.ProcessExerciseAnalyticsUseCase
import com.guaracode.evofit.domain.usecase.ProcessExerciseAnalyticsUseCaseImpl
import com.guaracode.evofit.domain.usecase.ProcessRepsAnalyticsUseCase
import com.guaracode.evofit.domain.usecase.ProcessRepsAnalyticsUseCaseImpl
import com.guaracode.evofit.domain.usecase.ProcessTimeAnalyticsUseCase
import com.guaracode.evofit.domain.usecase.ProcessTimeAnalyticsUseCaseImpl
import com.guaracode.evofit.domain.usecase.ProcessWeightAnalyticsUseCase
import com.guaracode.evofit.domain.usecase.ProcessWeightAnalyticsUseCaseImpl
import com.guaracode.evofit.domain.usecase.profile.CalculateGoalProgressUseCase
import com.guaracode.evofit.domain.usecase.profile.CalculateGoalProgressUseCaseImpl
import com.guaracode.evofit.domain.usecase.profile.GetActiveUserGoalsUseCase
import com.guaracode.evofit.domain.usecase.profile.GetActiveUserGoalsUseCaseImpl
import com.guaracode.evofit.presentation.ui.feature.profile.developer.viewmodel.DeveloperViewModel
import com.guaracode.evofit.presentation.ui.feature.profile.goals.viewmodel.PersonalGoalsViewModel
import com.guaracode.evofit.presentation.ui.feature.profile.home.viewmodel.ProfileViewModel
import com.guaracode.evofit.presentation.ui.feature.profile.userdata.viewmodel.UserDataViewModel
import com.guaracode.evofit.presentation.ui.feature.profile.support.viewmodel.SupportEmailViewModel
import com.guaracode.evofit.presentation.ui.feature.profile.about.viewmodel.AboutAppViewModel
import com.guaracode.evofit.domain.usecase.GetAppVersionUseCase
import com.guaracode.evofit.domain.usecase.GetAppVersionUseCaseImpl
import com.guaracode.evofit.presentation.ui.feature.evo.analytics.viewmodel.EvoAnalyticsViewModel
import com.guaracode.evofit.presentation.ui.feature.commons.goals.viewmodel.GoalWizardViewModel
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
            AppDatabase.MIGRATION_9_10,
            AppDatabase.MIGRATION_10_11,
            AppDatabase.MIGRATION_11_12,
            AppDatabase.MIGRATION_12_13
        ).fallbackToDestructiveMigration()
            .build()
    }

    single { get<AppDatabase>().userDao() }
    single { get<AppDatabase>().weightHistoryDao() }
    single { LocalExerciseDataSource() }
    single { SessionManager(androidContext()) }
    single<ConnectivityObserver> { NetworkConnectivityObserver(androidContext()) }
    single<WorkoutLocalDataSource> { WorkoutLocalDataSourceImpl(get()) }
    single<UserLocalDataSource> { UserLocalDataSourceImpl(get(), get()) }
    single { FirebaseFirestore.getInstance() }
    single<WorkoutRemoteDataSource> { WorkoutRemoteDataSourceImpl(get(), get()) }
    single<UserRemoteDataSource> { UserRemoteDataSourceImpl(get(), get()) }
    single<WorkoutSessionRepository> { WorkoutSessionRepositoryImpl(get()) }
    single<SupportRepository> { SupportRepositoryImpl(get(), get()) }
    single<OnboardingRepository> { OnboardingRepositoryImpl(get(), get(), get(), get(), get(), get(), androidContext(), get()) }
    single<WorkoutRepository> { WorkoutRepositoryImpl(get(), get(), get(), get()) }
    single<ExerciseRepository> { ExerciseRepositoryImpl(get()) }
    single { FirebaseAuth.getInstance() }
    single { GoogleSignInHandler(androidContext()) }
    single { AppleSignInHandler(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }
}

val domainModule = module {
    factory<GetMuscleGroupsUseCase> { GetMuscleGroupsUseCaseImpl(get()) }
    factory<GetExercisesByGroupUseCase> { GetExercisesByGroupUseCaseImpl(get()) }
    factory<GetExercisesByIdsUseCase> { GetExercisesByIdsUseCaseImpl(get()) }
    factory<GetGoalSuggestionsUseCase> { GetGoalSuggestionsUseCaseImpl(get()) }
    factory<GetOnboardingDataUseCase> { GetOnboardingDataUseCaseImpl(get()) }
    factory<SaveOnboardingDataUseCase> { SaveOnboardingDataUseCaseImpl(get(), get()) }
    factory<CompleteOnboardingUseCase> { CompleteOnboardingUseCaseImpl(get(), get()) }
    factory<AddWeightUpdateUseCase> { AddWeightUpdateUseCaseImpl(get(), get()) }
    factory<IsOnboardingCompletedUseCase> { IsOnboardingCompletedUseCaseImpl(get()) }
    factory<IsUserLoggedInUseCase> { IsUserLoggedInUseCaseImpl(get()) }
    factory<GetWeightHistoryUseCase> { GetWeightHistoryUseCaseImpl(get()) }
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
    factory<GenerateFakeWorkoutHistoryUseCase> { GenerateFakeWorkoutHistoryUseCaseImpl(get(), get(), get(), get(), get()) }
    factory<ProcessBodyWeightAnalyticsUseCase> { ProcessBodyWeightAnalyticsUseCaseImpl() }
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
    factory<LogoutUseCase> { LogoutUseCase(get()) }
    factory<SyncUserDataUseCase> { SyncUserDataUseCaseImpl(get()) }
    factory<NukeUserDataUseCase> { NukeUserDataUseCaseImpl(get()) }
    factory<SendSupportEmailUseCase> { SendSupportEmailUseCaseImpl(get()) }
    factory<GetAppVersionUseCase> { GetAppVersionUseCaseImpl() }
}

val splashModule = module {
    single<SplashTracker> { SplashTrackerImpl(get()) }
    viewModel { SplashViewModel(get(), get(), get(), get()) }
}

val onboardingModule = module {
    single<OnboardingTracker> { OnboardingTrackerImpl(get()) }
    viewModel {
        OnboardingViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            androidContext(),
            get(),
            get()
        )
    }
}

val homeModule = module {
    single<EvoHomeTracker> { EvoHomeTrackerImpl(get()) }
    viewModel {
        EvoHomeViewModel(
            get(),
            get(),
            get()
        )
    }
}

val workoutModule = module {
    single<WorkoutHomeTracker> { WorkoutHomeTrackerImpl(get()) }
    single<CreateWorkoutTracker> { CreateWorkoutTrackerImpl(get()) }
    single<WorkoutResumeTracker> { WorkoutResumeTrackerImpl(get()) }
    single<StartWorkoutTracker> { StartWorkoutTrackerImpl(get()) }

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
            get(),
            get()
        )
    }
    viewModel {
        NewWorkoutViewModel(
            get(),
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
            getWorkoutDoneByIdUseCase = get(),
            tracker = get()
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
            getMuscleGroupsUseCase = get(),
            updateWorkoutUseCase = get(),
            tracker = get()
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
            getMuscleGroupsUseCase = get(),
            tracker = get()
        )
    }
}

val evoModule = module {
    single<EvoAnalyticsTracker> { EvoAnalyticsTrackerImpl(get()) }
    viewModel { EvoAnalyticsViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
}

val profileModule = module {
    single<ProfileHomeTracker> { ProfileHomeTrackerImpl(get()) }
    single<AboutAppTracker> { AboutAppTrackerImpl(get()) }
    single<DeveloperTracker> { DeveloperTrackerImpl(get()) }
    single<PersonalGoalsTracker> { PersonalGoalsTrackerImpl(get()) }
    single<SupportTracker> { SupportTrackerImpl(get()) }
    single<UserDataTracker> { UserDataTrackerImpl(get()) }

    viewModel { ProfileViewModel(get(), get(), get(), get(), get()) }
    viewModel { SupportEmailViewModel(get(), get()) }
    viewModel { AboutAppViewModel(get(), get()) }
    viewModel { DeveloperViewModel(get(), get()) }
    viewModel { UserDataViewModel(get(), get(), get(), get()) }
    viewModel { PersonalGoalsViewModel(get(), get(), get(), get(), get(), get(), get()) }
}

val authModule = module {
    single<AuthErrorMapper> { AuthErrorMapper(androidContext()) }
    single<AuthTracker> { AuthTrackerImpl(get()) }
    viewModel { RegisterViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { LoginViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { RecoverPasswordViewModel(get(), get(), get()) }
}

val goalModule = module {
    single<GoalWizardTracker> { GoalWizardTrackerImpl(get()) }
    viewModel { GoalWizardViewModel(get(), get(), get()) }
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
    authModule,
    goalModule
)
