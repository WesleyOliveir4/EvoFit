package com.guaracode.evofit.data.repository

import com.guaracode.evofit.core.monitoring.CrashReporter
import com.guaracode.evofit.data.local.session.SessionManager
import com.guaracode.evofit.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val sessionManager: SessionManager,
    private val crashReporter: CrashReporter
) : AuthRepository {
    override suspend fun register(email: String, password: String): Result<Unit> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result.user?.uid?.let {
                sessionManager.saveSession(it)
                crashReporter.setUserId(it)
                crashReporter.logEvent("User registered with email")
            }
            Result.success(Unit)
        } catch (e: Exception) {
            crashReporter.recordException(e)
            Result.failure(e)
        }
    }

    override suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            result.user?.uid?.let {
                sessionManager.saveSession(it)
                crashReporter.setUserId(it)
                crashReporter.logEvent("User logged in with email")
            }
            Result.success(Unit)
        } catch (e: Exception) {
            crashReporter.recordException(e)
            Result.failure(e)
        }
    }

    override suspend fun loginWithGoogle(idToken: String): Result<Unit> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = firebaseAuth.signInWithCredential(credential).await()
            result.user?.uid?.let {
                sessionManager.saveSession(it)
                crashReporter.setUserId(it)
                crashReporter.logEvent("User logged in with Google")
            }
            Result.success(Unit)
        } catch (e: Exception) {
            crashReporter.recordException(e)
            Result.failure(e)
        }
    }

    override suspend fun loginWithApple(): Result<Unit> {
        // Implementation with Firebase OAuthProvider("apple.com")
        // Note: Apple sign-in often needs an Activity context for the web-based flow if not using native SDK.
        // For simplicity in a repository, we assume the flow is handled or we use the generic provider.
        return try {
            val provider = OAuthProvider.newBuilder("apple.com")
            // This usually requires startWithSignInLink or startActivityForSignInWithProvider
            // which needs an Activity. In a clean architecture, we might need a way to pass the activity
            // or use a different approach. For now, I'll keep it as a placeholder or use the provider
            // if we were passed a result.
            // If we are using the native Apple Sign In, we'd get a token and use signInWithCredential.
            Result.failure(Exception("Apple Sign-In requires Activity context for web flow or native token."))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendPasswordResetCode(email: String): Result<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            crashReporter.recordException(e)
            Result.failure(e)
        }
    }

    override suspend fun updateDisplayName(name: String): Result<Unit> {
        return try {
            val user = firebaseAuth.currentUser ?: return Result.failure(IllegalStateException("No user logged in"))
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
            user.updateProfile(profileUpdates).await()
            Result.success(Unit)
        } catch (e: Exception) {
            crashReporter.recordException(e)
            Result.failure(e)
        }
    }

    override fun isLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun getCurrentUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            sessionManager.clearSession()
            crashReporter.setUserId("")
            crashReporter.logEvent("User logged out")
            Result.success(Unit)
        } catch (e: Exception) {
            crashReporter.recordException(e)
            Result.failure(e)
        }
    }
}
