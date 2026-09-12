package com.example.evofit.data.datasource

import com.example.evofit.data.local.dao.UserDao
import com.example.evofit.data.local.dao.WeightHistoryDao
import com.example.evofit.data.local.entities.*
import kotlinx.coroutines.flow.Flow

interface UserLocalDataSource {
    fun getUser(): Flow<UserEntity?>
    suspend fun getUserDirect(): UserEntity?
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
    suspend fun syncIncremental(
        user: UserEntity?,
        goals: List<UserGoalEntity>,
        workouts: List<FullWorkoutRemoteData>,
        newHistory: List<WorkoutDoneEntity>
    )
    fun getGoalsForUser(userId: String): Flow<List<UserGoalEntity>>
    suspend fun deleteGoalsForUser(userId: String): Int
    suspend fun deleteGoalById(goalId: String): Int
    suspend fun softDeleteGoal(goalId: String, timestamp: Long): Unit
    suspend fun getPendingGoals(): List<UserGoalEntity>
    suspend fun getPendingUser(): UserEntity?
    suspend fun markGoalSynced(goalId: String, timestamp: Long)
    suspend fun markUserSynced(userId: String, timestamp: Long)
    
    // Weight History
    fun getWeightHistory(userId: String): Flow<List<WeightUpdateEntity>>
    suspend fun insertWeightUpdate(update: WeightUpdateEntity): Long
    suspend fun softDeleteWeightUpdate(updateId: String, timestamp: Long): Unit
    suspend fun getPendingWeightUpdates(): List<WeightUpdateEntity>
    suspend fun markWeightUpdateSynced(id: String, timestamp: Long)
    suspend fun deleteWeightUpdateById(id: String)
    
    suspend fun nukeUserData()
    suspend fun clearSyncableUserData()
}

class UserLocalDataSourceImpl(
    private val userDao: UserDao,
    private val weightHistoryDao: WeightHistoryDao
) : UserLocalDataSource {
    override fun getUser() = userDao.getUser()
    
    override suspend fun getUserDirect(): UserEntity? = userDao.getUserDirect()
    
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

    override suspend fun syncIncremental(
        user: UserEntity?,
        goals: List<UserGoalEntity>,
        workouts: List<FullWorkoutRemoteData>,
        newHistory: List<WorkoutDoneEntity>
    ) = userDao.syncIncremental(user, goals, workouts, newHistory)
    
    override fun getGoalsForUser(userId: String) = userDao.getGoalsForUser(userId)
    
    override suspend fun deleteGoalsForUser(userId: String) = userDao.deleteGoalsForUser(userId)
    
    override suspend fun deleteGoalById(goalId: String) = userDao.deleteGoalById(goalId)

    override suspend fun softDeleteGoal(goalId: String, timestamp: Long) = 
        userDao.softDeleteGoal(goalId, timestamp)

    override suspend fun getPendingGoals(): List<UserGoalEntity> = userDao.getPendingGoals()
    
    override suspend fun getPendingUser(): UserEntity? = userDao.getPendingUser()
    
    override suspend fun markGoalSynced(goalId: String, timestamp: Long) = userDao.markGoalSynced(goalId, timestamp)
    
    override suspend fun markUserSynced(userId: String, timestamp: Long) = userDao.markUserSynced(userId, timestamp)

    override fun getWeightHistory(userId: String) = weightHistoryDao.getWeightHistory(userId)

    override suspend fun insertWeightUpdate(update: WeightUpdateEntity) = weightHistoryDao.insertWeightUpdate(update)

    override suspend fun softDeleteWeightUpdate(updateId: String, timestamp: Long) = 
        weightHistoryDao.softDeleteWeightUpdate(updateId, timestamp)

    override suspend fun getPendingWeightUpdates(): List<WeightUpdateEntity> = weightHistoryDao.getPendingWeightUpdates()
    
    override suspend fun markWeightUpdateSynced(id: String, timestamp: Long) = weightHistoryDao.markWeightUpdateSynced(id, timestamp)

    override suspend fun deleteWeightUpdateById(id: String) = weightHistoryDao.deleteWeightUpdateById(id).let { }

    override suspend fun nukeUserData() {
        userDao.nukeUserData()
        weightHistoryDao.deleteAllWeightHistory()
    }

    override suspend fun clearSyncableUserData() {
        userDao.clearSyncableUserData()
        weightHistoryDao.deleteAllWeightHistory()
    }
}
