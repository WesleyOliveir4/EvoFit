package com.guaracode.evofit.presentation.ui.feature.authentication.apple

class AppleSignInHandler {
    suspend fun signIn(): Result<Unit> {
        return Result.failure(NotImplementedError("Apple Sign-In on iOS not yet implemented"))
    }
}
