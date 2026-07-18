package com.example.evofit.data.datasource

import com.example.evofit.data.local.dao.UserDao
import com.example.evofit.data.local.entities.UserEntity
import com.example.evofit.data.local.entities.UserGoalEntity
import kotlinx.coroutines.flow.Flow

interface UserLocalDataSource {
    fun getUser(): Flow<UserEntity?>
    suspend fun insertUser(user: UserEntity): Long
    suspend fun updateUser(user: UserEntity): Int
    suspend fun saveUserWithGoals(user: UserEntity, goals: List<UserGoalEntity>): Long
    fun getGoalsForUser(userId: String): Flow<List<UserGoalEntity>>
    suspend fun deleteGoalsForUser(userId: String): Int
    suspend fun deleteGoalById(goalId: String): Int
}

class UserLocalDataSourceImpl(
    private val userDao: UserDao
) : UserLocalDataSource {
    override fun getUser() = userDao.getUser()
    
    override suspend fun insertUser(user: UserEntity) = userDao.insertUser(user)
    
    override suspend fun updateUser(user: UserEntity) = userDao.updateUser(user)
    
    override suspend fun saveUserWithGoals(user: UserEntity, goals: List<UserGoalEntity>) = 
        userDao.saveUserWithGoals(user, goals)
    
    override fun getGoalsForUser(userId: String) = userDao.getGoalsForUser(userId)
    
    override suspend fun deleteGoalsForUser(userId: String) = userDao.deleteGoalsForUser(userId)
    
    override suspend fun deleteGoalById(goalId: String) = userDao.deleteGoalById(goalId)
}
