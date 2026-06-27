package com.example.evofit.data.repository

import com.example.evofit.data.local.dao.UserDao
import com.example.evofit.data.mapper.mapToDomain
import com.example.evofit.data.mapper.toEntity
import com.example.evofit.domain.model.UserOnboardingData
import com.example.evofit.domain.repository.OnboardingRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
class OnboardingRepositoryImpl(private val userDao: UserDao) : OnboardingRepository {

    override fun getUserData(): Flow<UserOnboardingData?> {
        return userDao.getUser().flatMapLatest { userEntity ->
            if (userEntity == null) {
                flowOf(null)
            } else {
                userDao.getGoalsForUser(userEntity.id).map { goals ->
                    mapToDomain(userEntity, goals)
                }
            }
        }
    }

    override suspend fun getUserId(): String? {
        return userDao.getUser().firstOrNull()?.id
    }

    override suspend fun saveUserData(data: UserOnboardingData, userId: String, isCompleted: Boolean) {
        val userEntity = data.toEntity(userId).copy(
            isOnboardingCompleted = isCompleted
        )
        val goalEntities = data.goals.map { it.toEntity(userId) }
        userDao.saveUserWithGoals(userEntity, goalEntities)
    }

    override suspend fun completeOnboarding() {
        userDao.getUser().firstOrNull()?.let { user ->
            userDao.updateUser(user.copy(isOnboardingCompleted = true))
        }
    }

    override fun isOnboardingCompleted(): Flow<Boolean> {
        return userDao.getUser().map { it?.isOnboardingCompleted ?: false }
    }
}
