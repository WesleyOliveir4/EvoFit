package com.example.evofit.data.local.dao

import androidx.room.*
import com.example.evofit.data.local.entities.*
import com.example.evofit.data.local.relations.FullWorkout
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    // User
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Update
    suspend fun updateUser(user: UserEntity): Int

    @Query("SELECT * FROM users LIMIT 1")
    fun getUser(): Flow<UserEntity?>

    // Goals
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoals(goals: List<UserGoalEntity>): List<Long>

    @Query("DELETE FROM user_goals WHERE userId = :userId")
    suspend fun deleteGoalsForUser(userId: String): Int

    @Query("SELECT * FROM user_goals WHERE userId = :userId")
    fun getGoalsForUser(userId: String): Flow<List<UserGoalEntity>>

    // Workouts
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: WorkoutEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutExercise(workoutExercise: WorkoutExerciseEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExerciseSets(sets: List<ExerciseSetEntity>): List<Long>

    @Transaction
    suspend fun saveUserWithGoals(user: UserEntity, goals: List<UserGoalEntity>): Long {
        val id = insertUser(user)
        deleteGoalsForUser(user.id)
        insertGoals(goals)
        return id
    }

    @Transaction
    @Query("SELECT * FROM workouts WHERE userId = :userId")
    fun getFullWorkouts(userId: String): Flow<List<FullWorkout>>

    @Transaction
    @Query("SELECT * FROM workouts WHERE workoutId = :workoutId")
    fun getFullWorkoutById(workoutId: Long): Flow<FullWorkout?>

    // Workout History
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutDoneHistory(history: WorkoutDoneHistoryEntity)

    @Query("SELECT * FROM workout_done_history WHERE userId = :userId")
    suspend fun getWorkoutDoneHistory(userId: String): WorkoutDoneHistoryEntity?
}
