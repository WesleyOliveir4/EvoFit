package com.example.evofit.data.local.dao

import androidx.room.*
import com.example.evofit.data.local.entities.*
import com.example.evofit.data.local.relations.ActiveSessionWithSets
import com.example.evofit.data.local.relations.FullWorkout
import kotlinx.coroutines.flow.Flow

@Dao
abstract class UserDao {
    // User
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun internalInsertUser(user: UserEntity): Long

    @Transaction
    open suspend fun insertUser(user: UserEntity): Long {
        val id = internalInsertUser(user)
        if (id == -1L) {
            // Se já existe, apenas atualizamos os campos para NÃO disparar o CASCADE DELETE do REPLACE
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

    // Goals
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

    @Query("DELETE FROM workout_done_history WHERE userId = :userId")
    abstract suspend fun deleteWorkoutHistorySummary(userId: String): Int

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

    @Transaction
    open suspend fun syncAllData(
        user: UserEntity?,
        goals: List<UserGoalEntity>,
        workouts: List<FullWorkoutRemoteData>,
        legacyHistory: WorkoutDoneHistoryEntity?,
        newHistory: List<WorkoutDoneEntity>,
        shouldClearActiveSession: Boolean
    ) {
        if (shouldClearActiveSession) {
            nukeUserData()
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

    @Transaction
    open suspend fun syncIncremental(
        user: UserEntity?,
        goals: List<UserGoalEntity>,
        workouts: List<FullWorkoutRemoteData>,
        newHistory: List<WorkoutDoneEntity>
    ) {
        user?.let {
            insertUser(it)
        }
        
        for (goal in goals) {
            if (goal.isDeleted) {
                deleteGoalById(goal.id)
            } else {
                insertGoals(listOf(goal))
            }
        }

        for (fullWorkout in workouts) {
            if (fullWorkout.workout.isDeleted) {
                deleteWorkoutById(fullWorkout.workout.workoutId)
            } else {
                insertFullWorkoutReturnId(
                    fullWorkout.workout,
                    fullWorkout.exercises,
                    fullWorkout.sets
                )
            }
        }

        for (historyItem in newHistory) {
            if (historyItem.isDeleted) {
                deleteWorkoutDoneById(historyItem.id)
            } else {
                insertWorkoutDone(historyItem)
            }
        }
    }

    @Query("DELETE FROM workout_done")
    abstract suspend fun deleteAllWorkoutDone()

    @Query("DELETE FROM user_goals WHERE id = :goalId")
    abstract suspend fun deleteGoalById(goalId: String): Int

    @Query("DELETE FROM workout_done WHERE id = :workoutDoneId")
    abstract suspend fun deleteWorkoutDoneById(workoutDoneId: String): Int

    @Query("SELECT * FROM user_goals WHERE userId = :userId AND isDeleted = 0")
    abstract fun getGoalsForUser(userId: String): Flow<List<UserGoalEntity>>

    // Pending Sync Queries
    @Query("SELECT * FROM user_goals WHERE syncStatus = 'PENDING'")
    abstract suspend fun getPendingGoals(): List<UserGoalEntity>

    @Query("SELECT * FROM workouts WHERE syncStatus = 'PENDING'")
    abstract suspend fun getPendingWorkouts(): List<WorkoutEntity>

    @Query("SELECT * FROM workout_done WHERE syncStatus = 'PENDING'")
    abstract suspend fun getPendingWorkoutDone(): List<WorkoutDoneEntity>

    // Soft Delete methods
    @Query("UPDATE user_goals SET isDeleted = 1, syncStatus = 'PENDING', updatedAt = :timestamp WHERE id = :goalId")
    abstract suspend fun softDeleteGoal(goalId: String, timestamp: Long)

    @Query("UPDATE workouts SET isDeleted = 1, syncStatus = 'PENDING', updatedAt = :timestamp WHERE workoutId = :workoutId")
    abstract suspend fun softDeleteWorkout(workoutId: String, timestamp: Long)

    @Query("UPDATE workout_done SET isDeleted = 1, syncStatus = 'PENDING', createdAt = :timestamp WHERE id = :workoutDoneId")
    abstract suspend fun softDeleteWorkoutDone(workoutDoneId: String, timestamp: Long)

    @Query("UPDATE workouts SET syncStatus = 'SYNCED', updatedAt = :timestamp WHERE workoutId = :workoutId")
    abstract suspend fun markWorkoutSynced(workoutId: String, timestamp: Long)

    @Query("UPDATE workout_done SET syncStatus = 'SYNCED' WHERE id = :id")
    abstract suspend fun markWorkoutDoneSynced(id: String)

    @Query("UPDATE user_goals SET syncStatus = 'SYNCED', updatedAt = :timestamp WHERE id = :id")
    abstract suspend fun markGoalSynced(id: String, timestamp: Long)

    @Query("UPDATE users SET syncStatus = 'SYNCED', updatedAt = :timestamp WHERE id = :id")
    abstract suspend fun markUserSynced(id: String, timestamp: Long)

    @Query("SELECT * FROM users WHERE syncStatus = 'PENDING' LIMIT 1")
    abstract suspend fun getPendingUser(): UserEntity?

    @Transaction
    @Query("SELECT * FROM workouts WHERE syncStatus = 'PENDING'")
    abstract suspend fun getPendingFullWorkouts(): List<FullWorkout>

    // Workouts
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
    abstract suspend fun updateWorkouts(workouts: List<WorkoutEntity>)

    @Transaction
    open suspend fun updateWorkoutsOrder(workouts: List<WorkoutEntity>) {
        updateWorkouts(workouts)
    }

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

    /**
     * Atualiza um treino existente substituindo por completo seus exercícios/séries.
     * A remoção de workout_exercises aciona o CASCADE de exercise_sets automaticamente.
     */
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

    // Workout History (Legacy)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertWorkoutDoneHistory(history: WorkoutDoneHistoryEntity)

    @Query("SELECT * FROM workout_done_history WHERE userId = :userId")
    abstract fun getWorkoutDoneHistory(userId: String): Flow<WorkoutDoneHistoryEntity?>

    // Workout History (New - Individual Items)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertWorkoutDone(workoutDone: WorkoutDoneEntity)

    @Query("SELECT * FROM workout_done WHERE userId = :userId AND isDeleted = 0 ORDER BY createdAt DESC LIMIT :limit")
    abstract fun getLatestWorkoutDoneHistory(userId: String, limit: Int): Flow<List<WorkoutDoneEntity>>

    @Query("SELECT * FROM workout_done WHERE userId = :userId AND isDeleted = 0 ORDER BY createdAt DESC")
    abstract fun getAllWorkoutDoneHistory(userId: String): Flow<List<WorkoutDoneEntity>>

    @Query("SELECT * FROM workout_done WHERE userId = :userId AND isDeleted = 0 AND createdAt >= :sinceTimestamp ORDER BY createdAt DESC")
    abstract fun getWorkoutsSince(userId: String, sinceTimestamp: Long): Flow<List<WorkoutDoneEntity>>

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
}
