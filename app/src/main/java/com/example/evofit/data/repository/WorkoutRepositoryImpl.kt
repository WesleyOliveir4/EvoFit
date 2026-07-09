package com.example.evofit.data.repository

import com.example.evofit.data.datasource.LocalExerciseDataSource
import com.example.evofit.data.local.dao.UserDao
import com.example.evofit.data.mapper.toData
import com.example.evofit.data.mapper.toDomain
import com.example.evofit.data.mapper.toEntity
import com.example.evofit.data.local.entities.WorkoutDoneHistoryEntity
import com.example.evofit.domain.model.Workout
import com.example.evofit.domain.model.WorkoutDone
import com.example.evofit.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WorkoutRepositoryImpl(
    private val userDao: UserDao,
    private val exerciseDataSource: LocalExerciseDataSource
) : WorkoutRepository {
    override fun getWorkouts(userId: String): Flow<List<Workout>> {
        return userDao.getFullWorkouts(userId).map { fullWorkouts ->
            val muscleGroups = exerciseDataSource.getAllMuscleGroups().map { it.toDomain() }
            fullWorkouts.map { fullWorkout ->
                val group = muscleGroups.find { it.id == fullWorkout.workout.muscleGroupId }
                val nameResolver = buildExerciseNameResolver(fullWorkout.exercises.map { it.workoutExercise.exerciseId })
                fullWorkout.toDomain(group, nameResolver)
            }
        }
    }

    override fun getWorkoutById(workoutId: Long): Flow<Workout?> {
        return userDao.getFullWorkoutById(workoutId).map { fullWorkout ->
            fullWorkout?.let {
                val group = exerciseDataSource.getAllMuscleGroups()
                    .find { it.id == fullWorkout.workout.muscleGroupId }
                    ?.toDomain()
                val nameResolver = buildExerciseNameResolver(fullWorkout.exercises.map { ex -> ex.workoutExercise.exerciseId })
                fullWorkout.toDomain(group, nameResolver)
            }
        }
    }

    /**
     * Constrói um resolver de id -> nome de exercício a partir do catálogo local,
     * usado para preencher ExerciseSet.exerciseName ao montar o domínio a partir das entidades.
     */
    private fun buildExerciseNameResolver(exerciseIds: List<String>): (String) -> String {
        val namesById = exerciseDataSource.getExercisesByIds(exerciseIds).associate { it.id to it.name }
        return { exerciseId -> namesById[exerciseId] ?: "" }
    }

    override suspend fun saveWorkout(workout: Workout): Long {
        val nextOrderIndex = (userDao.getMaxOrderIndex(workout.userId) ?: -1) + 1
        val workoutEntity = workout.toEntity().copy(orderIndex = nextOrderIndex)
        
        val exercises = workout.exercises.map { it.toEntity(0) }
        val sets = workout.exercises.map { exercise ->
            exercise.sets.map { it.toEntity(0) }
        }

        return userDao.insertFullWorkoutReturnId(workoutEntity, exercises, sets)
    }

    override suspend fun updateWorkout(workout: Workout): Long {
        val workoutEntity = workout.toEntity()

        val exercises = workout.exercises.map { it.toEntity(0) }
        val sets = workout.exercises.map { exercise ->
            exercise.sets.map { it.toEntity(0) }
        }

        userDao.updateFullWorkout(workoutEntity, exercises, sets)
        return workout.id
    }

    override suspend fun deleteWorkout(workoutId: Long) {
        userDao.deleteWorkoutById(workoutId)
    }

    override suspend fun updateWorkoutsOrder(workouts: List<Workout>) {
        userDao.updateWorkoutsOrder(workouts.map { it.toEntity() })
    }

    override suspend fun getMaxOrderIndex(userId: String): Int {
        return userDao.getMaxOrderIndex(userId) ?: -1
    }

    override suspend fun saveWorkoutDone(userId: String, workoutDone: WorkoutDone) {
        val existingHistory = userDao.getWorkoutDoneHistory(userId)
        
        val nextId = (existingHistory?.history?.maxOfOrNull { it.id } ?: 0L) + 1
        val workoutWithId = workoutDone.copy(id = nextId)
        
        val updatedList = if (existingHistory != null) {
            existingHistory.history + workoutWithId
        } else {
            listOf(workoutWithId)
        }
        
        userDao.insertWorkoutDoneHistory(
            WorkoutDoneHistoryEntity(
                userId = userId,
                history = updatedList
            )
        )
    }

    override suspend fun getWorkoutDoneHistory(userId: String): List<WorkoutDone> {
        return userDao.getWorkoutDoneHistory(userId)?.history ?: emptyList()
    }

    override suspend fun getWorkoutDoneHistory(userId: String, period: com.example.evofit.domain.model.EvoPeriod): List<WorkoutDone> {
        val allHistory = getWorkoutDoneHistory(userId)
        val startDate = getStartDateForPeriod(period) ?: return allHistory

        return allHistory.filter { workout ->
            val workoutDate = com.example.evofit.presentation.mapper.DateMapper.parseDate(workout.date)
            workoutDate != null && workoutDate.after(startDate)
        }
    }

    private fun getStartDateForPeriod(period: com.example.evofit.domain.model.EvoPeriod): java.util.Date? {
        val calendar = java.util.Calendar.getInstance()
        return when (period) {
            com.example.evofit.domain.model.EvoPeriod.LAST_7_DAYS -> {
                calendar.add(java.util.Calendar.DAY_OF_YEAR, -7)
                calendar.time
            }
            com.example.evofit.domain.model.EvoPeriod.LAST_30_DAYS -> {
                calendar.add(java.util.Calendar.MONTH, -1)
                calendar.time
            }
            com.example.evofit.domain.model.EvoPeriod.LAST_90_DAYS -> {
                calendar.add(java.util.Calendar.MONTH, -3)
                calendar.time
            }
            com.example.evofit.domain.model.EvoPeriod.LAST_180_DAYS -> {
                calendar.add(java.util.Calendar.MONTH, -6)
                calendar.time
            }
            com.example.evofit.domain.model.EvoPeriod.ALL_TIME -> null
        }
    }
}
