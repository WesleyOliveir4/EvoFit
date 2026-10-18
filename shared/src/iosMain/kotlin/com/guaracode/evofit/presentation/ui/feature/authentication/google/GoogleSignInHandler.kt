package com.guaracode.evofit.presentation.ui.feature.authentication.google

class GoogleSignInHandler {
    suspend fun signIn(): Result<String> {
        return Result.failure(NotImplementedError("Google Sign-In on iOS not yet implemented"))
    }

    suspend fun signOut() {}
}
