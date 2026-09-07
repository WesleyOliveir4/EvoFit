# Refactor Password Recovery Flow to Link-based Email

The goal is to simplify the password recovery flow by removing the OTP (One-Time Password) and manual password reset screens within the app. The new flow will send a reset link to the user's email, and the app will show a success screen directing the user back to the login.

## Proposed Changes

### Domain Layer

#### [DELETE] [VerifyPasswordResetCodeUseCase.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/domain/usecase/VerifyPasswordResetCodeUseCase.kt)
#### [DELETE] [ConfirmPasswordResetUseCase.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/domain/usecase/ConfirmPasswordResetUseCase.kt)

#### [MODIFY] [AuthRepository.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/domain/repository/AuthRepository.kt)
- Remove `verifyPasswordResetCode` and `confirmPasswordReset` methods.

---

### Data Layer

#### [MODIFY] [AuthRepositoryImpl.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/data/repository/AuthRepositoryImpl.kt)
- Remove `verifyPasswordResetCode` and `confirmPasswordReset` implementations.

---

### Presentation Layer (Authentication)

#### [DELETE] [VerifyCodeScreen.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/authentication/screens/VerifyCodeScreen.kt)
#### [DELETE] [NewPasswordScreen.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/authentication/screens/NewPasswordScreen.kt)

#### [DELETE] [VerifyCodeViewModel.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/authentication/viewmodel/VerifyCodeViewModel.kt)
#### [DELETE] [NewPasswordViewModel.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/authentication/viewmodel/NewPasswordViewModel.kt)

#### [DELETE] [VerifyCodeUiState.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/authentication/state/VerifyCodeUiState.kt)
#### [DELETE] [NewPasswordUiState.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/authentication/state/NewPasswordUiState.kt)

#### [DELETE] [VerifyCodeComponents.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/authentication/components/VerifyCodeComponents.kt)
#### [DELETE] [NewPasswordComponents.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/authentication/components/NewPasswordComponents.kt)

#### [MODIFY] [ForgotPasswordScreen.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/authentication/screens/ForgotPasswordScreen.kt)
- Repurpose as a success screen.
- Remove intro-specific text and logic.
- Update to inform that the email was sent and the user should login after changing the password.

#### [MODIFY] [ForgotPasswordComponents.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/authentication/components/ForgotPasswordComponents.kt)
- Update `ForgotPasswordHeader` and `ForgotPasswordFooter` to use new strings (Success title/subtitle and "Seguir para o Login" button).

---

### Navigation

#### [MODIFY] [NavRoutes.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/navigation/NavRoutes.kt)
- Remove `VerifyCode` and `NewPassword` routes.
- Add `ForgotPassword` route.

#### [MODIFY] [NavNavigation.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/navigation/NavNavigation.kt)
- Remove composables for `VerifyCode` and `NewPassword`.
- Add composable for `ForgotPassword`.
- Link `RecoverPasswordScreen` to `ForgotPassword`.
- Link `ForgotPasswordScreen` back to `Login`.

---

### Resources

#### [MODIFY] [strings.xml](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/res/values/strings.xml)
- Add success strings for forgot password flow.
- Remove old/unused strings related to verification code and new password if they are no longer needed.

---

### DI (Koin)

#### [MODIFY] [AppModule.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/di/AppModule.kt)
- Remove definitions for `VerifyCodeViewModel`, `NewPasswordViewModel`, `VerifyPasswordResetCodeUseCase`, and `ConfirmPasswordResetUseCase`.

## Verification Plan

### Manual Verification
1.  Navigate to Login.
2.  Tap "Esqueci minha senha".
3.  Enter email in `RecoverPasswordScreen` and tap "Enviar código" (or "Enviar link" if I update the text).
4.  Verify navigation to `ForgotPasswordScreen` (Success screen).
5.  Verify the message and button text "Seguir para o Login".
6.  Tap the button and verify navigation back to `LoginScreen`.
7.  Check that files were deleted and there are no compilation errors.
