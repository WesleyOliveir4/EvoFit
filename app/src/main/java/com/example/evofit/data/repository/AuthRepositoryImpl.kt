package com.example.evofit.data.repository

import com.example.evofit.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {
    override suspend fun register(name: String, email: String, password: String): Result<Unit> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
                user.updateProfile(profileUpdates).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loginWithGoogle(idToken: String): Result<Unit> {
        // Implementation with Credential Manager / Firebase GoogleAuthProvider
        // will be finalized in Step 5. Returning success for flow testing.
        return Result.success(Unit)
    }

    override suspend fun loginWithApple(): Result<Unit> {
        // Implementation with Firebase OAuthProvider("apple.com")
        // will be finalized in Step 5. Returning success for flow testing.
        return Result.success(Unit)
    }

    override suspend fun sendPasswordResetCode(email: String): Result<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun verifyPasswordResetCode(email: String, code: String): Result<String> {
        // Since Firebase sends a link, we'll simulate the code verification 
        // or return a mock oobCode for now. Actual logic in Step 5.
        return Result.success("mock_oob_code")
    }

    override suspend fun confirmPasswordReset(oobCode: String, newPassword: String): Result<Unit> {
        return try {
            if (oobCode == "mock_oob_code") {
                // In a real scenario, we'd use firebaseAuth.confirmPasswordReset(oobCode, newPassword)
                Result.success(Unit)
            } else {
                firebaseAuth.confirmPasswordReset(oobCode, newPassword).await()
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun isLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }
}
