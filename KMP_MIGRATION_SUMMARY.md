# EvoFit KMP Migration - State Snapshot & Context Guidelines

## 1. Project Overview & Tech Stack
- **Project**: EvoFit Android -> Kotlin Multiplatform (KMP) Migration
- **Targets**: Android (`androidMain`) and iOS (`iosMain`: `iosArm64`, `iosSimulatorArm64`, `iosX64`)
- **Key Dependencies**:
  - Kotlin `2.0.21`
  - AGP `9.1.1`
  - Compose Multiplatform `1.7.0`
  - Koin KMP `4.0.0` (`koin-core`, `koin-compose`, `koin-compose-viewmodel`, `koin-android`)
  - GitLive Firebase `2.7.0` (`firebase-auth`, `firebase-firestore`)
  - Room KMP `2.7.0-alpha11` with bundled SQLite (`androidx.sqlite:sqlite-bundled`)
  - DataStore Preferences `1.1.3`
  - `kotlinx-datetime` `0.6.1`
  - Jetpack Lifecycle ViewModel Compose (`org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0`)

---

## 2. Completed Migration Phases

### Phase 1: Setup & Target Configuration
- Configured `:shared` module targeting Android and iOS in `settings.gradle.kts` and `shared/build.gradle.kts`.
- Increased Gradle daemon memory in `gradle.properties` (`-Xmx6144m`) to handle iOS release framework linking.

### Phase 2: UI Theme & Resources
- Migrated `Color.kt`, `Dimens.kt`, `Type.kt`, `Theme.kt` into `shared/src/commonMain/kotlin/com/guaracode/evofit/presentation/ui/theme/`.
- Configured `composeResources` for shared drawables and strings.

### Phase 3: Core Package (`core/`)
- `AppConstants.kt`
- `DateMapper.kt` (using `kotlinx.datetime`)
- `ConnectivityObserver.kt` (expect/actual for Android & iOS)
- `CrashReporter.kt` (monitoring interface)

### Phase 4: Data & Domain Layers (`data/` & `domain/`)
- Room KMP Database (`EvoFitDatabase`) and bundled SQLite driver.
- DataStore `SessionManager.kt`.
- GitLive Firebase Remote Data Sources (`UserRemoteDataSource`, `WorkoutRemoteDataSource`).
- All 15 Domain Models (`Workout`, `WorkoutDone`, `Exercise`, `MuscleGroup`, `UserGoal`, `EvoHomeSummary`, etc.).
- All Repositories (`AuthRepository`, `WorkoutRepository`, `OnboardingRepository`, `ExerciseRepository`, `SupportRepository`, `WorkoutSessionRepository`).

### Phase 5: Domain Use Cases & Dependency Injection
- Migrated 100% of domain use cases (59 use cases: authentication, workout management, analytics, profile goals, fake history generator).
- Configured shared Koin DI (`AppModule.kt` containing `dataModule`, `domainModule`, `evoModule`, `onboardingModule`, `profileModule`, and platform-specific `platformDataModule`).

### Phase 6: Presentation Models & Mappers
- All 8 UI Models (`WorkoutUIModel`, `GoalUIModel`, `MuscleGroupItem`, `ExercisePreviewItem`, `ExerciseSelectionUIModel`, etc.) in `shared/src/commonMain/.../presentation/model/`.
- All Mappers (`ExerciseMapper`, `MuscleGroupMapper`, `AuthErrorMapper`, `GoalDisplayMapper`) in `shared/src/commonMain/.../presentation/mapper/`.

### Phase 7: UI Feature Packages
1. **Authentication Feature (`authentication/`)**:
   - `state/`: `LoginUiState`, `RecoverPasswordUiState`, `RegisterUiState`.
   - `tracking/`: `AuthTracker`, `AuthTrackerImpl`.
   - `viewmodel/`: `LoginViewModel`, `RecoverPasswordViewModel`, `RegisterViewModel`.
   - `apple/` & `google/`: Platform-specific `AppleSignInHandler` (Android + native iOS `AuthenticationServices`) and `GoogleSignInHandler`.
   - `components/` & `screens/`: `ForgotPassword`, `Login`, `PreLogin`, `RecoverPassword`, `Register`, `LegalContent` + `@Preview` functions.
2. **Goals Feature (`commons/goals/`)**:
   - `tracking/`, `viewmodel/` (`GoalWizardViewModel`), `components/` (`GoalWizardComponents`), `screens/` (`GoalWizardScreen`, `GoalWizardBottomSheet`) + `@Preview` functions.
3. **Global Components (`components/`)**:
   - `EvoDatePicker.kt`, `GlobalComponents.kt` (`AppBottomNavigation`, `EvoFitButton`, `TopBarReturn`, `EvoFitAlertDialog`, `EvoFitCautionDialog`, `EvoFitActionDialog`, `EvoFitDropdownFilter`, `EvoFitInputField`) + `@Preview` functions.
4. **Workout Feature (`workout/` - 42 files)**:
   - `components/configure/` & `components/training/` (all cards, wheels, counters, draggable list utils, segmented control).
   - `home/`: `WorkoutState`, `WorkoutHomeTracker`, `WorkoutViewModel`, `WorkoutScreen`.
   - `createworkout/`: States, Trackers, ViewModels, `CreateWorkoutComponents`, `NewWorkoutScreen`, `SelectExercisesScreen`, `ConfigureWorkoutScreen`.
   - `startworkout/`: States, Trackers, ViewModels, `StartWorkoutComponents`, `WorkoutPreviewScreen`, `WorkoutStartScreen`.
   - `resume/`: States, Trackers, `WorkoutResumeViewModel`, `WorkoutResumeComponents`, `WorkoutResumeScreen`.
5. **Evo Feature (`evo/` - 15 files)**:
   - `analytics/`: `EvoAnalyticsState`, `EvoAnalyticsTracker`, `EvoAnalyticsTrackerImpl`, `EvoAnalyticsViewModel`, `ExerciseAnalyticsComponents`, `MuscleGroupSelectionComponents`, `CategoryAnalyticsSelectionScreen`, `CategoryDetailAnalyticsScreen`, `ExerciseSelectionScreen`.
   - `home/`: `EvoHomeUiState`, `EvoHomeTracker`, `EvoHomeTrackerImpl`, `EvoHomeViewModel`, `EvoHomeComponents`, `EvoHomeScreen`.
6. **Onboard Feature (`onboard/` - 11 files)**:
   - `state/`: `OnboardingUiState`
   - `tracking/`: `OnboardingTracker`, `OnboardingTrackerImpl`
   - `viewmodel/`: `OnboardingViewModel` (KMP pure, Context dependency removed)
   - `components/`: `OnboardingComponents` (`EvoWheelPicker`, `PageIndicators`, `UserInputField`, `GoalTag`, `ActiveGoalItem`, `AddNewGoalButton`)
   - `screens/`: `OnboardingScreen`, `OnboardUserDataScreen`, `OnboardWeightScreen`, `OnboardHeightScreen`, `OnboardingGoalsScreen`, `OnboardSummaryScreen`
7. **Profile Feature (`profile/` - 42 files)**:
   - `about/`: `AboutAppUiState`, `AboutAppTracker`, `AboutAppTrackerImpl`, `AboutAppViewModel`, `AboutAppScreen`.
   - `developer/`: `DeveloperUiState`, `DeveloperTracker`, `DeveloperTrackerImpl`, `DeveloperViewModel`, `DeveloperScreen`.
   - `goals/`: `GoalComponents`, `PersonalGoalsScreen`, `PersonalGoalsTracker`, `PersonalGoalsTrackerImpl`, `PersonalGoalsViewModel`.
   - `home/`: `ProfileComponents`, `ProfileHomeScreen`, `ProfileUiState`, `ProfileHomeTracker`, `ProfileHomeTrackerImpl`, `ProfileViewModel`.
   - `support/`: `FAQItem`, `SupportItem`, `FAQScreen`, `SuggestionNewWorkoutsScreen`, `SupportEmailScreen`, `SupportScreen`, `SupportEmailUiState`, `SupportTracker`, `SupportTrackerImpl`, `EmailTopic`, `FAQContext`, `SuggestionCategory`, `SupportEmailViewModel`.
   - `userdata/`: `EvoWeightPickerDialog`, `UserDataComponents`, `UserDataItem`, `UserDataScreen`, `UserDataUiState`, `UserDataTracker`, `UserDataTrackerImpl`, `UserDataViewModel`.

---

## 3. Strict Rules & Architectural Constraints

1. **Keep Native Code Intact**: Do NOT delete or modify code in `:app`. Keep `:app` intact for comparison and reference.
2. **No Mock Implementations**: Implement 100% native logic and functionality.
3. **Date/Time Handling**: Always use `kotlinx.datetime` (`Clock.System.now()`, `LocalDate`, `Instant`, `DateMapper`) instead of `java.util.Calendar` or `java.time.*` in `commonMain`.
4. **Platform-Specific APIs**:
   - Android-specific classes (`Context`, `Activity`, `CredentialManager`, `BackHandler`) must be kept in `androidMain` or replaced with KMP callbacks/expect-actual.
5. **Compose Multiplatform UI & Resources**:
   - Use `org.jetbrains.compose.resources.painterResource(Res.drawable.*)` for drawables.
   - Use `org.jetbrains.compose.ui.tooling.preview.Preview` for `@Preview`.
   - Wrap previews in `EvoFitTheme`.
6. **Dependency Injection**:
   - Register ViewModels and components in Koin modules as needed.
7. **Verification**:
   - Always run `./gradlew :shared:assemble` and `./gradlew :app:assembleDebug` after migrating to verify build success.

---

## 4. Pending Packages to Migrate (Next Steps)

Remaining packages under `app/src/main/java/com/guaracode/evofit/presentation/ui/feature/`:

1. **`splash/`**:
   - `SplashScreen.kt`, `SplashViewModel.kt`
2. **Navigation & Root App Composable**:
   - `navigation/NavRoutes.kt` & shared navigation host.

---

## 5. Prompt for New Session

To resume the migration in a new chat, use the following prompt:

> "Continuando a migração do EvoFit para KMP com base no arquivo `KMP_MIGRATION_SUMMARY.md`. Todas as fases anteriores (core, data, domain, use cases, di, presentation models/mappers, authentication, commons/goals, global components, workout feature, evo feature, onboard feature, profile feature) já foram concluídas. Vamos mapear e criar o plano para migrar o próximo pacote (`splash` ou `navigation`). Por favor, leia o `KMP_MIGRATION_SUMMARY.md` para carregar as regras e o contexto."
