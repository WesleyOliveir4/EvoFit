package com.example.evofit.presentation.ui.feature.authentication.apple

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import kotlinx.coroutines.tasks.await

class AppleSignInHandler(
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun signIn(activity: Activity): Result<Unit> {
        return try {
            val provider = OAuthProvider.newBuilder("apple.com")
            
            // Configurar escopos se necessário
            provider.scopes = listOf("email", "name")
            
            firebaseAuth.startActivityForSignInWithProvider(activity, provider.build()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}