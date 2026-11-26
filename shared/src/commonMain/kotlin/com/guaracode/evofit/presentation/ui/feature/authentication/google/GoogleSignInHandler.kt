package com.guaracode.evofit.presentation.ui.feature.authentication.google

expect class GoogleSignInHandler {
    suspend fun signIn(): Result<String>
    suspend fun signOut()
}
