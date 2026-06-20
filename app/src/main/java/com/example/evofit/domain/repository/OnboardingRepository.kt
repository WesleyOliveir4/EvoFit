package com.example.evofit.domain.repository

import com.example.evofit.domain.model.UserOnboardingData
import kotlinx.coroutines.flow.Flow

interface OnboardingRepository {
    fun getUserData(): Flow<UserOnboardingData>
    suspend fun saveUserData(data: UserOnboardingData)
    suspend fun completeOnboarding()
    fun isOnboardingCompleted(): Flow<Boolean>
}
