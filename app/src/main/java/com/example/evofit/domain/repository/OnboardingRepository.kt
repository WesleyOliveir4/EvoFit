package com.example.evofit.domain.repository

import com.example.evofit.domain.model.UserOnboardingData
import kotlinx.coroutines.flow.Flow

interface OnboardingRepository {
    fun getUserData(): Flow<UserOnboardingData?>
    suspend fun getUserId(): String?
    suspend fun saveUserData(data: UserOnboardingData, userId: String, isCompleted: Boolean)
    suspend fun completeOnboarding()
    suspend fun deleteGoal(goalId: String)
    fun isOnboardingCompleted(): Flow<Boolean>
    suspend fun syncUserData(userId: String): Result<Unit>
    suspend fun nukeUserData()
}
