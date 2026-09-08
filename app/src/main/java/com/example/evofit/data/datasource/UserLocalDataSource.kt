package com.example.evofit.data.datasource

import com.example.evofit.data.local.dao.UserDao
import com.example.evofit.data.local.dao.WeightHistoryDao
import com.example.evofit.data.local.entities.*
import kotlinx.coroutines.flow.Flow

interface UserLocalDataSource {
    fun getUser(): Flow<UserEntity?>
    suspend fun insertUser(user: UserEntity): Long
    suspend fun updateUser(user: UserEntity): Int
    suspend fun saveUserWithGoals(user: UserEntity, goals: List<UserGoalEntity>): Long
    suspend fun syncAllData(
        user: UserEntity?,
        goals: List<UserGoalEntity>,
        workouts: List<FullWorkoutRemoteData>,
        legacyHistory: WorkoutDoneHistoryEntity?,
        newHistory: List<WorkoutDoneEntity>,
        shouldClearActiveSession: Boolean
    )
    fun getGoalsForUser(userId: String): Flow<List<UserGoalEntity>>
    suspend fun deleteGoalsForUser(userId: String): Int
    suspend fun deleteGoalById(goalId: String): Int
    
    // Weight History
    fun getWeightHistory(userId: String): Flow<List<WeightUpdateEntity>>
    suspend fun insertWeightUpdate(update: WeightUpdateEntity): Long
    
    suspend fun nukeUserData()
    suspend fun clearSyncableUserData()
}

class UserLocalDataSourceImpl(
    private val userDao: UserDao,
    private val weightHistoryDao: WeightHistoryDao
) : UserLocalDataSource {
    override fun getUser() = userDao.getUser()
    
    override suspend fun insertUser(user: UserEntity) = userDao.insertUser(user)
    
    override suspend fun updateUser(user: UserEntity) = userDao.updateUser(user)
    
    override suspend fun saveUserWithGoals(user: UserEntity, goals: List<UserGoalEntity>) = 
        userDao.saveUserWithGoals(user, goals)

    override suspend fun syncAllData(
        user: UserEntity?,
        goals: List<UserGoalEntity>,
        workouts: List<FullWorkoutRemoteData>,
        legacyHistory: WorkoutDoneHistoryEntity?,
        newHistory: List<WorkoutDoneEntity>,
        shouldClearActiveSession: Boolean
    ) = userDao.syncAllData(user, goals, workouts, legacyHistory, newHistory, shouldClearActiveSession)
    
    override fun getGoalsForUser(userId: String) = userDao.getGoalsForUser(userId)
    
    override suspend fun deleteGoalsForUser(userId: String) = userDao.deleteGoalsForUser(userId)
    
    override suspend fun deleteGoalById(goalId: String) = userDao.deleteGoalById(goalId)

    override fun getWeightHistory(userId: String) = weightHistoryDao.getWeightHistory(userId)

    override suspend fun insertWeightUpdate(update: WeightUpdateEntity) = weightHistoryDao.insertWeightUpdate(update)

    override suspend fun nukeUserData() {
        userDao.nukeUserData()
        weightHistoryDao.deleteAllWeightHistory()
    }

    override suspend fun clearSyncableUserData() {
        userDao.clearSyncableUserData()
        weightHistoryDao.deleteAllWeightHistory()
    }
}
