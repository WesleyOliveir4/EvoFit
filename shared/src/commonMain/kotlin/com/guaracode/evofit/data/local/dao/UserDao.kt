package com.guaracode.evofit.data.local.dao

import androidx.room.*
import com.guaracode.evofit.data.local.entities.*
import com.guaracode.evofit.data.local.relations.*
import kotlinx.coroutines.flow.Flow

@Dao
abstract class UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun internalInsertUser(user: UserEntity): Long

    @Transaction
    open suspend fun insertUser(user: UserEntity): Long {
        val id = internalInsertUser(user)
        if (id == -1L) {
            updateUserFields(
                id = user.id,
                name = user.name,
                birthDate = user.birthDate,
                weight = user.weight,
                height = user.height,
                onboardingCompleted = user.onboardingCompleted,
                updatedAt = user.updatedAt
            )
        }
        return id
    }

    @Query("UPDATE users SET name = :name, birthDate = :birthDate, weight = :weight, height = :height, isOnboardingCompleted = :onboardingCompleted, updatedAt = :updatedAt WHERE id = :id")
    protected abstract suspend fun updateUserFields(
        id: String,
        name: String,
        birthDate: String,
        weight: String,
        height: String,
        onboardingCompleted: Boolean,
        updatedAt: Long
    )

    @Update
    abstract suspend fun updateUser(user: UserEntity): Int

    @Query("SELECT * FROM users LIMIT 1")
    abstract fun getUser(): Flow<UserEntity?>

    @Query("SELECT * FROM users LIMIT 1")
    abstract suspend fun getUserDirect(): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertGoals(goals: List<UserGoalEntity>): List<Long>

    @Query("DELETE FROM user_goals WHERE userId = :userId")
    abstract suspend fun deleteGoalsForUser(userId: String): Int

    @Query("DELETE FROM users")
    protected abstract suspend fun internalDeleteAllUsers()

    suspend fun deleteAllUsers() {
        internalDeleteAllUsers()
    }

    @Query("DELETE FROM user_goals")
    abstract suspend fun deleteAllGoals()

    @Query("DELETE FROM workouts")
    suspend fun deleteAllWorkouts() {
        internalDeleteAllWorkouts()
    }

    @Query("DELETE FROM workouts")
    protected abstract suspend fun internalDeleteAllWorkouts()

    @Query("DELETE FROM workout_done_history")
    abstract suspend fun deleteAllWorkoutHistory()

    @Query("DELETE FROM user_goals WHERE id = :goalId")
    abstract suspend fun deleteGoalById(goalId: String): Int

    @Query("DELETE FROM workout_done WHERE id = :workoutDoneId")
    abstract suspend fun deleteWorkoutDoneById(workoutDoneId: String): Int

    @Query("SELECT * FROM user_goals WHERE userId = :userId AND isDeleted = 0")
    abstract fun getGoalsForUser(userId: String): Flow<List<UserGoalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertWorkout(workout: WorkoutEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertWorkoutExercise(workoutExercise: WorkoutExerciseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertExerciseSets(sets: List<ExerciseSetEntity>)

    @Transaction
    open suspend fun saveUserWithGoals(user: UserEntity, goals: List<UserGoalEntity>): Long {
        val id = insertUser(user)
        deleteGoalsForUser(user.id)
        insertGoals(goals)
        return id
    }

    @Transaction
    @Query("SELECT * FROM workouts WHERE userId = :userId AND isDeleted = 0 ORDER BY orderIndex ASC")
    abstract fun getFullWorkouts(userId: String): Flow<List<FullWorkout>>

    @Query("SELECT MAX(orderIndex) FROM workouts WHERE userId = :userId")
    abstract suspend fun getMaxOrderIndex(userId: String): Int?

    @Update
    abstract suspend fun updateWorkoutsOrder(workouts: List<WorkoutEntity>)

    @Transaction
    @Query("SELECT * FROM workouts WHERE workoutId = :workoutId AND isDeleted = 0")
    abstract fun getFullWorkoutById(workoutId: String): Flow<FullWorkout?>

    @Transaction
    open suspend fun insertFullWorkoutReturnId(
        workout: WorkoutEntity,
        exercises: List<WorkoutExerciseEntity>,
        sets: List<List<ExerciseSetEntity>>
    ): String {
        insertWorkout(workout)
        exercises.forEachIndexed { index, exercise ->
            insertWorkoutExercise(exercise)
            insertExerciseSets(sets[index])
        }
        return workout.workoutId
    }

    @Query("DELETE FROM workout_exercises WHERE workoutId = :workoutId")
    abstract suspend fun deleteWorkoutExercisesForWorkout(workoutId: String)

    @Query("DELETE FROM workouts WHERE workoutId = :workoutId")
    abstract suspend fun deleteWorkoutById(workoutId: String)

    @Transaction
    open suspend fun updateFullWorkout(
        workout: WorkoutEntity,
        exercises: List<WorkoutExerciseEntity>,
        sets: List<List<ExerciseSetEntity>>
    ) {
        insertWorkout(workout)
        deleteWorkoutExercisesForWorkout(workout.workoutId)
        exercises.forEachIndexed { index, exercise ->
            insertWorkoutExercise(exercise.copy(workoutId = workout.workoutId))
            insertExerciseSets(sets[index].map { it.copy(workoutExerciseId = exercise.id) })
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertWorkoutDoneHistory(history: WorkoutDoneHistoryEntity)

    @Query("SELECT * FROM workout_done_history WHERE userId = :userId")
    abstract fun getWorkoutDoneHistory(userId: String): Flow<WorkoutDoneHistoryEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertWorkoutDone(workoutDone: WorkoutDoneEntity)

    @Query("SELECT * FROM workout_done WHERE userId = :userId AND isDeleted = 0 ORDER BY createdAt DESC")
    abstract fun getAllWorkoutDoneHistory(userId: String): Flow<List<WorkoutDoneEntity>>

    @Query("DELETE FROM workout_done WHERE userId = :userId")
    abstract suspend fun deleteAllWorkoutDone(userId: String)

    // Active Session
    @Transaction
    @Query("SELECT * FROM active_session LIMIT 1")
    abstract fun getActiveSessionWithSets(): Flow<ActiveSessionWithSets?>

    @Transaction
    open suspend fun updateActiveSession(session: ActiveSessionEntity, sets: List<ActiveSessionSetEntity>) {
        deleteActiveSession()
        insertActiveSession(session)
        insertActiveSessionSets(sets)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertActiveSession(session: ActiveSessionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertActiveSessionSets(sets: List<ActiveSessionSetEntity>)

    @Query("DELETE FROM active_session")
    abstract suspend fun deleteActiveSession()

    @Transaction
    open suspend fun nukeUserData() {
        deleteAllUsers()
        deleteAllGoals()
        deleteAllWorkouts()
        deleteAllWorkoutHistory()
        deleteActiveSession()
    }

    @Transaction
    open suspend fun clearSyncableUserData() {
        deleteAllUsers()
        deleteAllGoals()
        deleteAllWorkouts()
        deleteAllWorkoutHistory()
    }
}
