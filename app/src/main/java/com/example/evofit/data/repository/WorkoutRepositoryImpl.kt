package com.example.evofit.data.repository

import com.example.evofit.data.local.dao.UserDao
import com.example.evofit.data.mapper.toDomain
import com.example.evofit.domain.model.Workout
import com.example.evofit.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WorkoutRepositoryImpl(private val userDao: UserDao) : WorkoutRepository {
    override fun getWorkouts(userId: String): Flow<List<Workout>> {
        return userDao.getFullWorkouts(userId).map { fullWorkouts ->
            fullWorkouts.map { it.toDomain() }
        }
    }
}
