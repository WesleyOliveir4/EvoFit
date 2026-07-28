package com.example.evofit.data.local.dao

import androidx.room.*
import com.example.evofit.data.local.entities.*
import com.example.evofit.data.local.relations.ActiveSessionWithSets
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

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()

    @Query("DELETE FROM user_goals")
    suspend fun deleteAllGoals()

    @Query("DELETE FROM workouts")
    suspend fun deleteAllWorkouts()

    @Query("DELETE FROM workout_done_history")
    suspend fun deleteAllWorkoutHistory()

    @Query("DELETE FROM workout_done_history WHERE userId = :userId")
    suspend fun deleteWorkoutHistorySummary(userId: String): Int

    @Transaction
    suspend fun nukeUserData() {
        deleteAllUsers()
        deleteAllGoals()
        deleteAllWorkouts()
        deleteAllWorkoutHistory()
        deleteActiveSession()
    }

    @Transaction
    suspend fun clearSyncableUserData() {
        deleteAllUsers()
        deleteAllGoals()
        deleteAllWorkouts()
        deleteAllWorkoutHistory()
    }

    @Transaction
    suspend fun syncAllData(
        user: UserEntity?,
        goals: List<UserGoalEntity>,
        workouts: List<FullWorkoutRemoteData>,
        legacyHistory: WorkoutDoneHistoryEntity?,
        newHistory: List<WorkoutDoneEntity>,
        shouldClearActiveSession: Boolean
    ) {
        if (shouldClearActiveSession) {
            nukeUserData()
        } else {
            clearSyncableUserData()
            deleteAllWorkoutDone() // Adicionado para garantir limpeza total do historico novo também
        }

        user?.let {
            insertUser(it)
        }
        if (goals.isNotEmpty()) {
            insertGoals(goals)
        }
        for (fullWorkout in workouts) {
            insertFullWorkoutReturnId(
                fullWorkout.workout,
                fullWorkout.exercises,
                fullWorkout.sets
            )
        }
        legacyHistory?.let {
            insertWorkoutDoneHistory(it)
        }
        for (historyItem in newHistory) {
            insertWorkoutDone(historyItem)
        }
    }

    @Query("DELETE FROM workout_done")
    suspend fun deleteAllWorkoutDone()

    @Query("DELETE FROM user_goals WHERE id = :goalId")
    suspend fun deleteGoalById(goalId: String): Int

    @Query("SELECT * FROM user_goals WHERE userId = :userId")
    fun getGoalsForUser(userId: String): Flow<List<UserGoalEntity>>

    // Workouts
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: WorkoutEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutExercise(workoutExercise: WorkoutExerciseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExerciseSets(sets: List<ExerciseSetEntity>)

    @Transaction
    suspend fun saveUserWithGoals(user: UserEntity, goals: List<UserGoalEntity>): Long {
        val id = insertUser(user)
        deleteGoalsForUser(user.id)
        insertGoals(goals)
        return id
    }

    @Transaction
    @Query("SELECT * FROM workouts WHERE userId = :userId ORDER BY orderIndex ASC")
    fun getFullWorkouts(userId: String): Flow<List<FullWorkout>>

    @Query("SELECT MAX(orderIndex) FROM workouts WHERE userId = :userId")
    suspend fun getMaxOrderIndex(userId: String): Int?

    @Update
    suspend fun updateWorkouts(workouts: List<WorkoutEntity>)

    @Transaction
    suspend fun updateWorkoutsOrder(workouts: List<WorkoutEntity>) {
        updateWorkouts(workouts)
    }

    @Transaction
    @Query("SELECT * FROM workouts WHERE workoutId = :workoutId")
    fun getFullWorkoutById(workoutId: String): Flow<FullWorkout?>

    @Transaction
    suspend fun insertFullWorkoutReturnId(
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
    suspend fun deleteWorkoutExercisesForWorkout(workoutId: String)

    @Query("DELETE FROM workouts WHERE workoutId = :workoutId")
    suspend fun deleteWorkoutById(workoutId: String)

    /**
     * Atualiza um treino existente substituindo por completo seus exercícios/séries.
     * A remoção de workout_exercises aciona o CASCADE de exercise_sets automaticamente.
     */
    @Transaction
    suspend fun updateFullWorkout(
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

    // Workout History (Legacy)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutDoneHistory(history: WorkoutDoneHistoryEntity)

    @Query("SELECT * FROM workout_done_history WHERE userId = :userId")
    fun getWorkoutDoneHistory(userId: String): Flow<WorkoutDoneHistoryEntity?>

    // Workout History (New - Individual Items)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutDone(workoutDone: WorkoutDoneEntity)

    @Query("SELECT * FROM workout_done WHERE userId = :userId ORDER BY createdAt DESC LIMIT :limit")
    fun getLatestWorkoutDoneHistory(userId: String, limit: Int): Flow<List<WorkoutDoneEntity>>

    @Query("SELECT * FROM workout_done WHERE userId = :userId ORDER BY createdAt DESC")
    fun getAllWorkoutDoneHistory(userId: String): Flow<List<WorkoutDoneEntity>>

    @Query("DELETE FROM workout_done WHERE userId = :userId")
    suspend fun deleteAllWorkoutDone(userId: String)

    // Active Session
    @Transaction
    @Query("SELECT * FROM active_session LIMIT 1")
    fun getActiveSessionWithSets(): Flow<ActiveSessionWithSets?>

    @Transaction
    suspend fun updateActiveSession(session: ActiveSessionEntity, sets: List<ActiveSessionSetEntity>) {
        deleteActiveSession()
        insertActiveSession(session)
        insertActiveSessionSets(sets)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActiveSession(session: ActiveSessionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActiveSessionSets(sets: List<ActiveSessionSetEntity>)

    @Query("DELETE FROM active_session")
    suspend fun deleteActiveSession()
}
