package com.example.evofit.domain.repository

interface AuthRepository {
    suspend fun register(email: String, password: String): Result<Unit>
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun loginWithGoogle(idToken: String): Result<Unit>
    suspend fun loginWithApple(): Result<Unit>
    suspend fun sendPasswordResetCode(email: String): Result<Unit>
    fun isLoggedIn(): Boolean
    fun getCurrentUserId(): String?
    suspend fun logout(): Result<Unit>
}
