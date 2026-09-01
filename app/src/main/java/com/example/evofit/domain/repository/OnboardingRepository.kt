package com.example.evofit.domain.repository

import com.example.evofit.domain.model.UserOnboardingData
import kotlinx.coroutines.flow.Flow

interface OnboardingRepository {
    fun getUserData(): Flow<UserOnboardingData?>
    fun getUserId(): Flow<String?>
    suspend fun saveUserData(data: UserOnboardingData, userId: String, isCompleted: Boolean)
    suspend fun completeOnboarding()
    suspend fun deleteGoal(goalId: String)
    suspend fun deleteProfilePicture(uri: String)
    fun isOnboardingCompleted(): Flow<Boolean>
    suspend fun syncUserData(userId: String, shouldClearActiveSession: Boolean, isOnline: Boolean): Result<Unit>
    suspend fun nukeUserData()
}
