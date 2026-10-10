# Implementation Plan — Migration of Authentication Flow to Shared KMP Module

Migrate the complete authentication feature (`com.guaracode.evofit.presentation.ui.feature.authentication`) from the `app` module to the `shared` KMP module (`shared/src/commonMain/kotlin/com/guaracode/evofit/presentation/ui/feature/authentication`), keeping business logic calls (UseCases, Repositories) intact and adapting UI/viewmodels for Compose Multiplatform.

## User Review Required

> [!IMPORTANT]
> - **Android-specific APIs in CommonMain**: Classes like `android.widget.Toast`, `android.util.Patterns`, `GoogleSignInHandler`, and `AppleSignInHandler` will be adapted or placed in platform-specific (`androidMain`) / handled via platform-agnostic UI state.
> - **String Resources**: `R.string.*` references in commonMain screens/components will be mapped via a multiplatform string provider or expect/actual approach to support both Android and iOS targets.

## Proposed Changes

### Shared Module (`shared`)

#### [NEW] [LoginUiState.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/shared/src/commonMain/kotlin/com/guaracode/evofit/presentation/ui/feature/authentication/state/LoginUiState.kt)
#### [NEW] [RegisterUiState.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/shared/src/commonMain/kotlin/com/guaracode/evofit/presentation/ui/feature/authentication/state/RegisterUiState.kt)
#### [NEW] [RecoverPasswordUiState.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/shared/src/commonMain/kotlin/com/guaracode/evofit/presentation/ui/feature/authentication/state/RecoverPasswordUiState.kt)

#### [NEW] [AuthTracker.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/shared/src/commonMain/kotlin/com/guaracode/evofit/presentation/ui/feature/authentication/tracking/AuthTracker.kt)
#### [NEW] [AuthTrackerImpl.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/shared/src/androidMain/kotlin/com/guaracode/evofit/presentation/ui/feature/authentication/tracking/AuthTrackerImpl.kt)

#### [NEW] [LoginViewModel.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/shared/src/commonMain/kotlin/com/guaracode/evofit/presentation/ui/feature/authentication/viewmodel/LoginViewModel.kt)
#### [NEW] [RegisterViewModel.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/shared/src/commonMain/kotlin/com/guaracode/evofit/presentation/ui/feature/authentication/viewmodel/RegisterViewModel.kt)
#### [NEW] [RecoverPasswordViewModel.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/shared/src/commonMain/kotlin/com/guaracode/evofit/presentation/ui/feature/authentication/viewmodel/RecoverPasswordViewModel.kt)

#### [NEW] Components & Screens
- `components/ForgotPasswordComponents.kt`
- `components/LoginComponents.kt`
- `components/PreLoginComponents.kt`
- `components/RecoverPasswordComponents.kt`
- `components/RegisterComponents.kt`
- `screens/ForgotPasswordScreen.kt`
- `screens/LegalContentScreen.kt`
- `screens/LoginScreen.kt`
- `screens/PreLoginScreen.kt`
- `screens/RecoverPasswordScreen.kt`
- `screens/RegisterScreen.kt`

#### [NEW] Handlers (Android Main)
- `google/GoogleSignInHandler.kt` (in `shared/src/androidMain`)
- `apple/AppleSignInHandler.kt` (in `shared/src/androidMain`)

#### [NEW] Error Mapper
- `presentation/mapper/AuthErrorMapper.kt` in `shared/src/commonMain`

### App Module (`app`)
- Remove old authentication package from `app/src/main/java/com/guaracode/evofit/presentation/ui/feature/authentication`.
- Update Koin module definitions (`di/AppModule.kt`) to reference the shared ViewModels, Handlers, and Trackers.

## Verification Plan

### Automated Tests
- Run Gradle build to ensure compilation across shared and app modules: `./gradlew assembleDebug`
- Run unit tests: `./gradlew testUnit`

### Manual Verification
- Deploy app to emulator/device and verify authentication flow (PreLogin -> Login -> Register -> Recover Password -> Navigation).
