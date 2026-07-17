package com.example.evofit.data.datasource

import com.example.evofit.data.local.dao.UserDao
import com.example.evofit.data.local.entities.ExerciseSetEntity
import com.example.evofit.data.local.entities.WorkoutDoneHistoryEntity
import com.example.evofit.data.local.entities.WorkoutEntity
import com.example.evofit.data.local.entities.WorkoutExerciseEntity
import com.example.evofit.data.local.relations.FullWorkout
import kotlinx.coroutines.flow.Flow

interface WorkoutLocalDataSource {
    fun getFullWorkouts(userId: String): Flow<List<FullWorkout>>
    fun getFullWorkoutById(workoutId: String): Flow<FullWorkout?>
    suspend fun getMaxOrderIndex(userId: String): Int?
    suspend fun insertFullWorkout(
        workout: WorkoutEntity,
        exercises: List<WorkoutExerciseEntity>,
        sets: List<List<ExerciseSetEntity>>
    ): String
    suspend fun updateFullWorkout(
        workout: WorkoutEntity,
        exercises: List<WorkoutExerciseEntity>,
        sets: List<List<ExerciseSetEntity>>
    )
    suspend fun deleteWorkoutById(workoutId: String)
    suspend fun updateWorkoutsOrder(workouts: List<WorkoutEntity>)
    suspend fun getWorkoutDoneHistory(userId: String): WorkoutDoneHistoryEntity?
    suspend fun insertWorkoutDoneHistory(history: WorkoutDoneHistoryEntity)
}

class WorkoutLocalDataSourceImpl(
    private val userDao: UserDao
) : WorkoutLocalDataSource {
    override fun getFullWorkouts(userId: String) = userDao.getFullWorkouts(userId)
    
    override fun getFullWorkoutById(workoutId: String) = userDao.getFullWorkoutById(workoutId)
    
    override suspend fun getMaxOrderIndex(userId: String) = userDao.getMaxOrderIndex(userId)
    
    override suspend fun insertFullWorkout(
        workout: WorkoutEntity,
        exercises: List<WorkoutExerciseEntity>,
        sets: List<List<ExerciseSetEntity>>
    ) = userDao.insertFullWorkoutReturnId(workout, exercises, sets)
    
    override suspend fun updateFullWorkout(
        workout: WorkoutEntity,
        exercises: List<WorkoutExerciseEntity>,
        sets: List<List<ExerciseSetEntity>>
    ) = userDao.updateFullWorkout(workout, exercises, sets)
    
    override suspend fun deleteWorkoutById(workoutId: String) = userDao.deleteWorkoutById(workoutId)
    
    override suspend fun updateWorkoutsOrder(workouts: List<WorkoutEntity>) = userDao.updateWorkoutsOrder(workouts)
    
    override suspend fun getWorkoutDoneHistory(userId: String) = userDao.getWorkoutDoneHistory(userId)
    
    override suspend fun insertWorkoutDoneHistory(history: WorkoutDoneHistoryEntity) = userDao.insertWorkoutDoneHistory(history)
}
