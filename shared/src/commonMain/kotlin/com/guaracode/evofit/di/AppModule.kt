package com.guaracode.evofit.di

import com.guaracode.evofit.core.monitoring.CrashReporter
import com.guaracode.evofit.core.monitoring.NoOpCrashReporter
import com.guaracode.evofit.data.datasource.*
import com.guaracode.evofit.data.local.AppDatabase
import com.guaracode.evofit.data.local.session.SessionManager
import com.guaracode.evofit.data.repository.*
import com.guaracode.evofit.domain.repository.*
import com.guaracode.evofit.domain.usecase.*
import com.guaracode.evofit.domain.usecase.profile.CalculateGoalProgressUseCase
import com.guaracode.evofit.domain.usecase.profile.CalculateGoalProgressUseCaseImpl
import com.guaracode.evofit.domain.usecase.profile.GetActiveUserGoalsUseCase
import com.guaracode.evofit.domain.usecase.profile.GetActiveUserGoalsUseCaseImpl
import com.guaracode.evofit.presentation.mapper.AuthErrorMapper
import com.guaracode.evofit.presentation.ui.feature.authentication.tracking.AuthTracker
import com.guaracode.evofit.presentation.ui.feature.authentication.tracking.AuthTrackerImpl
import com.guaracode.evofit.presentation.ui.feature.authentication.viewmodel.LoginViewModel
import com.guaracode.evofit.presentation.ui.feature.authentication.viewmodel.RecoverPasswordViewModel
import com.guaracode.evofit.presentation.ui.feature.authentication.viewmodel.RegisterViewModel
import com.guaracode.evofit.presentation.ui.feature.commons.goals.tracking.GoalWizardTracker
import com.guaracode.evofit.presentation.ui.feature.commons.goals.tracking.GoalWizardTrackerImpl
import com.guaracode.evofit.presentation.ui.feature.commons.goals.viewmodel.GoalWizardViewModel
import com.guaracode.evofit.presentation.ui.feature.evo.analytics.tracking.EvoAnalyticsTracker
import com.guaracode.evofit.presentation.ui.feature.evo.analytics.tracking.EvoAnalyticsTrackerImpl
import com.guaracode.evofit.presentation.ui.feature.evo.analytics.viewmodel.EvoAnalyticsViewModel
import com.guaracode.evofit.presentation.ui.feature.evo.home.tracking.EvoHomeTracker
import com.guaracode.evofit.presentation.ui.feature.evo.home.tracking.EvoHomeTrackerImpl
import com.guaracode.evofit.presentation.ui.feature.evo.home.viewmodel.EvoHomeViewModel
import com.guaracode.evofit.presentation.ui.feature.onboard.tracking.OnboardingTracker
import com.guaracode.evofit.presentation.ui.feature.onboard.tracking.OnboardingTrackerImpl
import com.guaracode.evofit.presentation.ui.feature.onboard.viewmodel.OnboardingViewModel
import com.guaracode.evofit.presentation.ui.feature.profile.about.tracking.AboutAppTracker
import com.guaracode.evofit.presentation.ui.feature.profile.about.tracking.AboutAppTrackerImpl
import com.guaracode.evofit.presentation.ui.feature.profile.about.viewmodel.AboutAppViewModel
import com.guaracode.evofit.presentation.ui.feature.profile.developer.tracking.DeveloperTracker
import com.guaracode.evofit.presentation.ui.feature.profile.developer.tracking.DeveloperTrackerImpl
import com.guaracode.evofit.presentation.ui.feature.profile.developer.viewmodel.DeveloperViewModel
import com.guaracode.evofit.presentation.ui.feature.profile.goals.tracking.PersonalGoalsTracker
import com.guaracode.evofit.presentation.ui.feature.profile.goals.tracking.PersonalGoalsTrackerImpl
import com.guaracode.evofit.presentation.ui.feature.profile.goals.viewmodel.PersonalGoalsViewModel
import com.guaracode.evofit.presentation.ui.feature.profile.home.tracking.ProfileHomeTracker
import com.guaracode.evofit.presentation.ui.feature.profile.home.tracking.ProfileHomeTrackerImpl
import com.guaracode.evofit.presentation.ui.feature.profile.home.viewmodel.ProfileViewModel
import com.guaracode.evofit.presentation.ui.feature.profile.support.tracking.SupportTracker
import com.guaracode.evofit.presentation.ui.feature.profile.support.tracking.SupportTrackerImpl
import com.guaracode.evofit.presentation.ui.feature.profile.support.viewmodel.SupportEmailViewModel
import com.guaracode.evofit.presentation.ui.feature.profile.userdata.tracking.UserDataTracker
import com.guaracode.evofit.presentation.ui.feature.profile.userdata.tracking.UserDataTrackerImpl
import com.guaracode.evofit.presentation.ui.feature.profile.userdata.viewmodel.UserDataViewModel
import com.guaracode.evofit.presentation.ui.feature.splash.tracking.SplashTracker
import com.guaracode.evofit.presentation.ui.feature.splash.tracking.SplashTrackerImpl
import com.guaracode.evofit.presentation.ui.feature.splash.viewmodel.SplashViewModel
import com.guaracode.evofit.presentation.ui.feature.workout.createworkout.tracking.CreateWorkoutTracker
import com.guaracode.evofit.presentation.ui.feature.workout.createworkout.tracking.CreateWorkoutTrackerImpl
import com.guaracode.evofit.presentation.ui.feature.workout.createworkout.viewmodel.ConfigureWorkoutViewModel
import com.guaracode.evofit.presentation.ui.feature.workout.createworkout.viewmodel.NewWorkoutViewModel
import com.guaracode.evofit.presentation.ui.feature.workout.createworkout.viewmodel.SelectExercisesViewModel
import com.guaracode.evofit.presentation.ui.feature.workout.home.tracking.WorkoutHomeTracker
import com.guaracode.evofit.presentation.ui.feature.workout.home.tracking.WorkoutHomeTrackerImpl
import com.guaracode.evofit.presentation.ui.feature.workout.home.viewmodel.WorkoutViewModel
import com.guaracode.evofit.presentation.ui.feature.workout.resume.tracking.WorkoutResumeTracker
import com.guaracode.evofit.presentation.ui.feature.workout.resume.tracking.WorkoutResumeTrackerImpl
import com.guaracode.evofit.presentation.ui.feature.workout.resume.viewmodel.WorkoutResumeViewModel
import com.guaracode.evofit.presentation.ui.feature.workout.startworkout.tracking.StartWorkoutTracker
import com.guaracode.evofit.presentation.ui.feature.workout.startworkout.tracking.StartWorkoutTrackerImpl
import com.guaracode.evofit.presentation.ui.feature.workout.startworkout.viewmodel.WorkoutPreviewViewModel
import com.guaracode.evofit.presentation.ui.feature.workout.startworkout.viewmodel.WorkoutStartViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import org.koin.dsl.module

val dataModule = module {
    single<CrashReporter> { NoOpCrashReporter() }
    single { get<AppDatabase>().userDao() }
    single { get<AppDatabase>().weightHistoryDao() }
    single { SessionManager(get()) }
    single { LocalExerciseDataSource() }
    single<WorkoutLocalDataSource> { WorkoutLocalDataSourceImpl(get()) }
    single<UserLocalDataSource> { UserLocalDataSourceImpl(get(), get()) }
    single { Firebase.firestore }
    single<WorkoutRemoteDataSource> { WorkoutRemoteDataSourceImpl(get(), get()) }
    single<UserRemoteDataSource> { UserRemoteDataSourceImpl(get(), get()) }
    single<WorkoutSessionRepository> { WorkoutSessionRepositoryImpl(get()) }
    single<OnboardingRepository> { OnboardingRepositoryImpl(get(), get(), get(), get(), get(), get(), get()) }
    single<WorkoutRepository> { WorkoutRepositoryImpl(get(), get(), get(), get()) }
    single<ExerciseRepository> { ExerciseRepositoryImpl(get()) }
    single { Firebase.auth }
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
    factory<UpdateWorkoutsOrderUseCase> { UpdateWorkoutsOrderUseCaseImpl(get()) }
    factory<GetActiveWorkoutSessionUseCase> { GetActiveWorkoutSessionUseCaseImpl(get(), get()) }
    factory<StartWorkoutSessionUseCase> { StartWorkoutSessionUseCaseImpl(get()) }
    factory<UpdateCompletedSetsUseCase> { UpdateCompletedSetsUseCaseImpl(get()) }
    factory<ClearWorkoutSessionUseCase> { ClearWorkoutSessionUseCaseImpl(get()) }
    factory<DeleteWorkoutUseCase> { DeleteWorkoutUseCaseImpl(get()) }
    factory<UpdateWorkoutUseCase> { UpdateWorkoutUseCaseImpl(get()) }
    factory<GetWorkoutsCountUseCase> { GetWorkoutsCountUseCaseImpl() }
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
    
    // Evo Use Cases
    factory<FilterWorkoutHistoryByPeriodUseCase> { FilterWorkoutHistoryByPeriodUseCaseImpl() }
    factory<GetStrengthGainsUseCase> { GetStrengthGainsUseCaseImpl() }
    factory<GetMostEvolvedMuscleUseCase> { GetMostEvolvedMuscleUseCaseImpl() }
    factory<GetLeastTrainedGroupUseCase> { GetLeastTrainedGroupUseCaseImpl() }
    factory<GetKmPerWeekUseCase> { GetKmPerWeekUseCaseImpl() }
    factory<GetAverageWorkoutTimeUseCase> { GetAverageWorkoutTimeUseCaseImpl() }
    factory<GetCurrentWeekRangeUseCase> { GetCurrentWeekRangeUseCaseImpl() }
    factory<GetEvoHomeSummaryUseCase> { GetEvoHomeSummaryUseCaseImpl(get(), get(), get(), get(), get(), get(), get(), get()) }
    factory<FilterTrainedMuscleGroupsUseCase> { FilterTrainedMuscleGroupsUseCaseImpl() }
    factory<GetTrainedMuscleGroupsUseCase> { GetTrainedMuscleGroupsUseCaseImpl(get()) }
    factory<GetExercisesWithRecordCountUseCase> { GetExercisesWithRecordCountUseCaseImpl(get()) }
    factory<ProcessWeightAnalyticsUseCase> { ProcessWeightAnalyticsUseCaseImpl() }
    factory<ProcessDistanceAnalyticsUseCase> { ProcessDistanceAnalyticsUseCaseImpl() }
    factory<ProcessTimeAnalyticsUseCase> { ProcessTimeAnalyticsUseCaseImpl() }
    factory<ProcessRepsAnalyticsUseCase> { ProcessRepsAnalyticsUseCaseImpl() }
    factory<ProcessExerciseAnalyticsUseCase> { ProcessExerciseAnalyticsUseCaseImpl(get(), get(), get(), get()) }
    factory<ProcessBodyWeightAnalyticsUseCase> { ProcessBodyWeightAnalyticsUseCaseImpl() }

    // Profile Use Cases
    factory<GetActiveUserGoalsUseCase> { GetActiveUserGoalsUseCaseImpl(get()) }
    factory<CalculateGoalProgressUseCase> { CalculateGoalProgressUseCaseImpl(get(), get()) }
    factory<GenerateFakeWorkoutHistoryUseCase> { GenerateFakeWorkoutHistoryUseCaseImpl(get(), get(), get(), get(), get()) }
}

val authModule = module {
    single { AuthErrorMapper() }
    single<AuthTracker> { AuthTrackerImpl(get()) }
    factory { RegisterViewModel(get(), get(), get(), get(), get(), get()) }
    factory { LoginViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    factory { RecoverPasswordViewModel(get(), get(), get()) }
}

val workoutModule = module {
    single<WorkoutHomeTracker> { WorkoutHomeTrackerImpl(get()) }
    single<CreateWorkoutTracker> { CreateWorkoutTrackerImpl(get()) }
    single<WorkoutResumeTracker> { WorkoutResumeTrackerImpl(get()) }
    single<StartWorkoutTracker> { StartWorkoutTrackerImpl(get()) }

    factory { WorkoutViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    factory { NewWorkoutViewModel(get(), get(), get(), get()) }
    factory { SelectExercisesViewModel(get(), get(), get(), get(), get()) }
    factory { ConfigureWorkoutViewModel(get(), get(), get(), get(), get(), get(), get()) }
    factory { (workoutId: String?, workoutDoneId: String?, editWorkoutId: String?, workoutNotFinishedId: String?) ->
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
    factory { (workoutId: String) ->
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
    factory { (workoutId: String) ->
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

val goalModule = module {
    single<GoalWizardTracker> { GoalWizardTrackerImpl(get()) }
    factory { GoalWizardViewModel(get(), get(), get()) }
}

val evoModule = module {
    single<EvoHomeTracker> { EvoHomeTrackerImpl(get()) }
    single<EvoAnalyticsTracker> { EvoAnalyticsTrackerImpl(get()) }
    factory { EvoHomeViewModel(get(), get(), get()) }
    factory { EvoAnalyticsViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
}

val onboardingModule = module {
    single<OnboardingTracker> { OnboardingTrackerImpl(get()) }
    factory { OnboardingViewModel(get(), get(), get(), get(), get(), get(), get()) }
}

val profileModule = module {
    single<AboutAppTracker> { AboutAppTrackerImpl(get()) }
    single<DeveloperTracker> { DeveloperTrackerImpl(get()) }
    single<PersonalGoalsTracker> { PersonalGoalsTrackerImpl(get()) }
    single<ProfileHomeTracker> { ProfileHomeTrackerImpl(get()) }
    single<SupportTracker> { SupportTrackerImpl(get()) }
    single<UserDataTracker> { UserDataTrackerImpl(get()) }

    factory { AboutAppViewModel(get(), get()) }
    factory { DeveloperViewModel(get(), get()) }
    factory { PersonalGoalsViewModel(get(), get(), get(), get(), get(), get(), get()) }
    factory { ProfileViewModel(get(), get(), get(), get(), get()) }
    factory { SupportEmailViewModel(get(), get()) }
    factory { UserDataViewModel(get(), get(), get(), get()) }
}

val splashModule = module {
    single<SplashTracker> { SplashTrackerImpl(get()) }
    factory { SplashViewModel(get(), get(), get(), get()) }
}

val sharedAppModules = listOf(
    dataModule,
    domainModule,
    authModule,
    workoutModule,
    goalModule,
    evoModule,
    onboardingModule,
    profileModule,
    splashModule,
    platformDataModule
)
