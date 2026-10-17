package com.guaracode.evofit.data.repository

import com.guaracode.evofit.data.datasource.LocalExerciseDataSource
import com.guaracode.evofit.data.datasource.WorkoutLocalDataSource
import com.guaracode.evofit.data.datasource.WorkoutRemoteDataSource
import com.guaracode.evofit.data.local.entities.SyncStatus
import com.guaracode.evofit.data.mapper.toDomain
import com.guaracode.evofit.data.mapper.toEntity
import com.guaracode.evofit.core.common.DateMapper
import com.guaracode.evofit.core.monitoring.CrashReporter
import com.guaracode.evofit.domain.model.Workout
import com.guaracode.evofit.domain.model.WorkoutDone
import com.guaracode.evofit.domain.repository.WorkoutRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class WorkoutRepositoryImpl(
    private val workoutDataSource: WorkoutLocalDataSource,
    private val exerciseDataSource: LocalExerciseDataSource,
    private val workoutRemoteDataSource: WorkoutRemoteDataSource,
    private val crashReporter: CrashReporter
) : WorkoutRepository {

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun getWorkouts(userId: String): Flow<List<Workout>> {
        return workoutDataSource.getFullWorkouts(userId).map { fullWorkouts ->
            val muscleGroups = exerciseDataSource.getAllMuscleGroups().map { it.toDomain() }
            fullWorkouts.map { fullWorkout ->
                val nameResolver = buildExerciseNameResolver(fullWorkout.exercises.map { it.workoutExercise.exerciseId })
                fullWorkout.toDomain(muscleGroups, nameResolver)
            }
        }
    }

    override fun getWorkoutById(workoutId: String): Flow<Workout?> {
        return workoutDataSource.getFullWorkoutById(workoutId).map { fullWorkout ->
            fullWorkout?.let {
                val muscleGroups = exerciseDataSource.getAllMuscleGroups().map { it.toDomain() }
                val nameResolver = buildExerciseNameResolver(fullWorkout.exercises.map { ex -> ex.workoutExercise.exerciseId })
                fullWorkout.toDomain(muscleGroups, nameResolver)
            }
        }
    }

    private fun buildExerciseNameResolver(exerciseIds: List<String>): (String) -> String {
        val namesById = exerciseDataSource.getExercisesByIds(exerciseIds).associate { it.id to it.name }
        return { exerciseId -> namesById[exerciseId] ?: "" }
    }

    override suspend fun saveWorkout(workout: Workout): String {
        val nextOrderIndex = (workoutDataSource.getMaxOrderIndex(workout.userId) ?: -1) + 1
        
        val workoutId = kotlinx.datetime.Clock.System.now().toEpochMilliseconds().toString() + "_" + (1000..9999).random()
        val workoutEntity = workout.toEntity().copy(
            workoutId = workoutId,
            orderIndex = nextOrderIndex,
            updatedAt = kotlinx.datetime.Clock.System.now().toEpochMilliseconds(),
            syncStatus = SyncStatus.PENDING
        )
        
        val exercises = workout.exercisesByGroup.flatMap { group ->
            group.exercises.map { exercise ->
                val workoutExerciseUuid = if (exercise.id.isEmpty()) kotlinx.datetime.Clock.System.now().toEpochMilliseconds().toString() + "_" + (1000..9999).random() else exercise.id
                val exerciseEntity = exercise.toEntity(workoutId, group.muscleGroupId, group.orderIndex).copy(id = workoutExerciseUuid)
                val sets = exercise.sets.map { set ->
                    set.toEntity(workoutExerciseUuid).copy(id = exercise.exerciseId)
                }
                exerciseEntity to sets
            }
        }

        val exerciseEntities = exercises.map { it.first }
        val setsEntities = exercises.map { it.second }

        workoutDataSource.insertFullWorkout(workoutEntity, exerciseEntities, setsEntities)

        return workoutId
    }

    override suspend fun updateWorkout(workout: Workout): String {
        val workoutEntity = workout.toEntity().copy(
            updatedAt = kotlinx.datetime.Clock.System.now().toEpochMilliseconds(),
            syncStatus = SyncStatus.PENDING
        )

        val exercises = workout.exercisesByGroup.flatMap { group ->
            group.exercises.map { exercise ->
                val workoutExerciseUuid = if (exercise.id.isEmpty()) kotlinx.datetime.Clock.System.now().toEpochMilliseconds().toString() + "_" + (1000..9999).random() else exercise.id
                val exerciseEntity = exercise.toEntity(workout.id, group.muscleGroupId, group.orderIndex).copy(id = workoutExerciseUuid)
                val sets = exercise.sets.map { set ->
                    set.toEntity(workoutExerciseUuid).copy(id = exercise.exerciseId)
                }
                exerciseEntity to sets
            }
        }

        val exerciseEntities = exercises.map { it.first }
        val setsEntities = exercises.map { it.second }

        workoutDataSource.updateFullWorkout(workoutEntity, exerciseEntities, setsEntities)

        return workout.id
    }

    override suspend fun deleteWorkout(workoutId: String) {
        workoutDataSource.deleteWorkoutById(workoutId)
    }

    override suspend fun updateWorkoutsOrder(workouts: List<Workout>) {
        val entities = workouts.map { 
            it.toEntity().copy(
                syncStatus = SyncStatus.PENDING,
                updatedAt = kotlinx.datetime.Clock.System.now().toEpochMilliseconds()
            )
        }
        workoutDataSource.updateWorkoutsOrder(entities)
    }

    override suspend fun getMaxOrderIndex(userId: String): Int {
        return workoutDataSource.getMaxOrderIndex(userId) ?: -1
    }

    override suspend fun saveWorkoutDone(userId: String, workoutDone: WorkoutDone) {
        val nextId = if (workoutDone.id.isEmpty()) kotlinx.datetime.Clock.System.now().toEpochMilliseconds().toString() + "_" + (1000..9999).random() else workoutDone.id
        val workoutWithId = workoutDone.copy(id = nextId)
        
        val entity = workoutWithId.toEntity().copy(
            syncStatus = SyncStatus.PENDING,
            isDeleted = false
        )
        workoutDataSource.insertWorkoutDone(entity)
    }

    override suspend fun deleteWorkoutDone(userId: String, workoutDoneId: String) {
        workoutDataSource.deleteWorkoutDoneById(workoutDoneId)
    }

    override fun getWorkoutDoneHistory(userId: String): Flow<List<WorkoutDone>> {
        val muscleGroups = exerciseDataSource.getAllMuscleGroups().map { it.toDomain() }
        val exerciseNames = exerciseDataSource.getAllExercises().associate { it.id to it.name }
        val resolver = { id: String -> exerciseNames[id] ?: "" }
        
        return workoutDataSource.getAllWorkoutDoneHistory(userId).map { list ->
            list.map { it.toDomain(muscleGroups, resolver) }
        }
    }

    override fun getWorkoutDoneHistory(userId: String, limit: Int): Flow<List<WorkoutDone>> {
        val muscleGroups = exerciseDataSource.getAllMuscleGroups().map { it.toDomain() }
        val exerciseNames = exerciseDataSource.getAllExercises().associate { it.id to it.name }
        val resolver = { id: String -> exerciseNames[id] ?: "" }
        
        return workoutDataSource.getAllWorkoutDoneHistory(userId).map { list ->
            list.take(limit).map { it.toDomain(muscleGroups, resolver) }
        }
    }

    override fun getWorkoutDoneSince(userId: String, sinceTimestamp: Long): Flow<List<WorkoutDone>> {
        val muscleGroups = exerciseDataSource.getAllMuscleGroups().map { it.toDomain() }
        val exerciseNames = exerciseDataSource.getAllExercises().associate { it.id to it.name }
        val resolver = { id: String -> exerciseNames[id] ?: "" }

        return workoutDataSource.getAllWorkoutDoneHistory(userId).map { list ->
            list.filter { it.createdAt >= sinceTimestamp }.map { it.toDomain(muscleGroups, resolver) }
        }
    }
}
