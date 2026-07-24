package com.example.evofit.data.repository

import com.example.evofit.data.datasource.LocalExerciseDataSource
import com.example.evofit.data.datasource.WorkoutLocalDataSource
import com.example.evofit.data.datasource.WorkoutRemoteDataSource
import com.example.evofit.data.mapper.toDomain
import com.example.evofit.data.mapper.toEntity
import com.example.evofit.data.local.entities.WorkoutDoneHistoryEntity
import com.example.evofit.domain.model.Workout
import com.example.evofit.domain.model.WorkoutDone
import com.example.evofit.domain.repository.WorkoutRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class WorkoutRepositoryImpl(
    private val workoutDataSource: WorkoutLocalDataSource,
    private val exerciseDataSource: LocalExerciseDataSource,
    private val workoutRemoteDataSource: WorkoutRemoteDataSource
) : WorkoutRepository {

    private val scope = CoroutineScope(Dispatchers.IO)
    override fun getWorkouts(userId: String): Flow<List<Workout>> {
        return workoutDataSource.getFullWorkouts(userId).map { fullWorkouts ->
            val muscleGroups = exerciseDataSource.getAllMuscleGroups().map { it.toDomain() }
            fullWorkouts.map { fullWorkout ->
                val group = muscleGroups.find { it.id == fullWorkout.workout.muscleGroupId }
                val nameResolver = buildExerciseNameResolver(fullWorkout.exercises.map { it.workoutExercise.exerciseId })
                fullWorkout.toDomain(group, nameResolver)
            }
        }
    }

    override fun getWorkoutById(workoutId: String): Flow<Workout?> {
        return workoutDataSource.getFullWorkoutById(workoutId).map { fullWorkout ->
            fullWorkout?.let {
                val group = exerciseDataSource.getAllMuscleGroups()
                    .find { it.id == fullWorkout.workout.muscleGroupId }
                    ?.toDomain()
                val nameResolver = buildExerciseNameResolver(fullWorkout.exercises.map { ex -> ex.workoutExercise.exerciseId })
                fullWorkout.toDomain(group, nameResolver)
            }
        }
    }

    private fun buildExerciseNameResolver(exerciseIds: List<String>): (String) -> String {
        val namesById = exerciseDataSource.getExercisesByIds(exerciseIds).associate { it.id to it.name }
        return { exerciseId -> namesById[exerciseId] ?: "" }
    }

    override suspend fun saveWorkout(workout: Workout): String {
        val nextOrderIndex = (workoutDataSource.getMaxOrderIndex(workout.userId) ?: -1) + 1
        
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

        val insertedId = workoutDataSource.insertFullWorkout(workoutEntity, exerciseEntities, setsEntities)

        scope.launch {
            try {
                workoutRemoteDataSource.saveFullWorkout(workoutEntity, exerciseEntities, setsEntities)
            } catch (e: Exception) {
                // Log error
            }
        }

        return insertedId
    }

    override suspend fun updateWorkout(workout: Workout): String {
        val workoutEntity = workout.toEntity()

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

        workoutDataSource.updateFullWorkout(workoutEntity, exerciseEntities, setsEntities)

        scope.launch {
            try {
                workoutRemoteDataSource.saveFullWorkout(workoutEntity, exerciseEntities, setsEntities)
            } catch (e: Exception) {
                // Log error
            }
        }

        return workout.id
    }

    override suspend fun deleteWorkout(workoutId: String) {
        val workout = workoutDataSource.getFullWorkoutById(workoutId).firstOrNull()?.workout
        workoutDataSource.deleteWorkoutById(workoutId)

        workout?.let {
            scope.launch {
                try {
                    workoutRemoteDataSource.deleteWorkout(it.userId, workoutId)
                } catch (e: Exception) {
                    // Log error
                }
            }
        }
    }

    override suspend fun updateWorkoutsOrder(workouts: List<Workout>) {
        val entities = workouts.map { it.toEntity() }
        workoutDataSource.updateWorkoutsOrder(entities)

        if (entities.isNotEmpty()) {
            val userId = entities.first().userId
            scope.launch {
                try {
                    workoutRemoteDataSource.updateWorkoutsOrder(userId, entities)
                } catch (e: Exception) {
                    // Log error
                }
            }
        }
    }

    override suspend fun getMaxOrderIndex(userId: String): Int {
        return workoutDataSource.getMaxOrderIndex(userId) ?: -1
    }

    override suspend fun saveWorkoutDone(userId: String, workoutDone: WorkoutDone) {
        val existingHistory = workoutDataSource.getWorkoutDoneHistory(userId).firstOrNull()
        
        val nextId = java.util.UUID.randomUUID().toString()
        val workoutWithId = workoutDone.copy(id = nextId)
        
        val updatedList = if (existingHistory != null) {
            existingHistory.history + workoutWithId
        } else {
            listOf(workoutWithId)
        }
        
        val historyEntity = WorkoutDoneHistoryEntity(
            userId = userId,
            history = updatedList
        )

        workoutDataSource.insertWorkoutDoneHistory(historyEntity)

        scope.launch {
            try {
                workoutRemoteDataSource.saveWorkoutDoneHistory(historyEntity)
            } catch (e: Exception) {
                // Log error
            }
        }
    }

    override fun getWorkoutDoneHistory(userId: String): Flow<List<WorkoutDone>> {
        return workoutDataSource.getWorkoutDoneHistory(userId).map { 
            it?.history ?: emptyList() 
        }
    }
}
