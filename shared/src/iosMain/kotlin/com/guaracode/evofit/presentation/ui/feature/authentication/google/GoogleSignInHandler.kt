package com.guaracode.evofit.presentation.ui.feature.authentication.google

actual class GoogleSignInHandler {
    actual suspend fun signIn(): Result<String> {
        return Result.failure(NotImplementedError("Google Sign-In on iOS not yet implemented"))
    }

    actual suspend fun signOut() {}
}
