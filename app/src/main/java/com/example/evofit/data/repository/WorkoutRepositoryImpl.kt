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

    override fun getWorkoutById(workoutId: String): Flow<Workout?> {
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

    override suspend fun saveWorkout(workout: Workout): String {
        val nextOrderIndex = (userDao.getMaxOrderIndex(workout.userId) ?: -1) + 1
        
        // Gerar UUIDs para toda a estrutura
        val workoutId = java.util.UUID.randomUUID().toString()
        val workoutEntity = workout.toEntity().copy(
            workoutId = workoutId,
            orderIndex = nextOrderIndex
        )
        
        val exercises = workout.exercises.map { exercise ->
            val exerciseId = java.util.UUID.randomUUID().toString()
            val exerciseEntity = exercise.toEntity(workoutId).copy(id = exerciseId)
            val sets = exercise.sets.map { set ->
                set.toEntity(exerciseId).copy(id = java.util.UUID.randomUUID().toString())
            }
            exerciseEntity to sets
        }

        val exerciseEntities = exercises.map { it.first }
        val setsEntities = exercises.map { it.second }

        return userDao.insertFullWorkoutReturnId(workoutEntity, exerciseEntities, setsEntities)
    }

    override suspend fun updateWorkout(workout: Workout): String {
        val workoutEntity = workout.toEntity()

        // Para update, mantemos os IDs se existirem, ou geramos se forem novos
        val exercises = workout.exercises.map { exercise ->
            val exerciseId = if (exercise.id.isEmpty()) java.util.UUID.randomUUID().toString() else exercise.id
            val exerciseEntity = exercise.toEntity(workout.id).copy(id = exerciseId)
            val sets = exercise.sets.map { set ->
                val setId = if (set.id.isEmpty()) java.util.UUID.randomUUID().toString() else set.id
                set.toEntity(exerciseId).copy(id = setId)
            }
            exerciseEntity to sets
        }

        val exerciseEntities = exercises.map { it.first }
        val setsEntities = exercises.map { it.second }

        userDao.updateFullWorkout(workoutEntity, exerciseEntities, setsEntities)
        return workout.id
    }

    override suspend fun deleteWorkout(workoutId: String) {
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
        
        val nextId = java.util.UUID.randomUUID().toString()
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
}
