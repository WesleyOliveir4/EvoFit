package com.guaracode.evofit.di

import com.guaracode.evofit.data.datasource.*
import com.guaracode.evofit.data.repository.*
import com.guaracode.evofit.domain.repository.*
import com.guaracode.evofit.domain.usecase.*
import com.guaracode.evofit.presentation.ui.feature.evo.analytics.tracking.EvoAnalyticsTracker
import com.guaracode.evofit.presentation.ui.feature.evo.analytics.tracking.EvoAnalyticsTrackerImpl
import com.guaracode.evofit.presentation.ui.feature.evo.analytics.viewmodel.EvoAnalyticsViewModel
import com.guaracode.evofit.presentation.ui.feature.evo.home.tracking.EvoHomeTracker
import com.guaracode.evofit.presentation.ui.feature.evo.home.tracking.EvoHomeTrackerImpl
import com.guaracode.evofit.presentation.ui.feature.evo.home.viewmodel.EvoHomeViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import org.koin.dsl.module

val dataModule = module {
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
}

val evoModule = module {
    single<EvoHomeTracker> { EvoHomeTrackerImpl(get()) }
    single<EvoAnalyticsTracker> { EvoAnalyticsTrackerImpl(get()) }
    factory { EvoHomeViewModel(get(), get(), get()) }
    factory { EvoAnalyticsViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
}

val sharedAppModules = listOf(
    dataModule,
    domainModule,
    evoModule,
    platformDataModule
)
