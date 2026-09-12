package com.example.evofit.data.repository

import com.example.evofit.data.datasource.LocalExerciseDataSource
import com.example.evofit.data.datasource.WorkoutLocalDataSource
import com.example.evofit.data.datasource.WorkoutRemoteDataSource
import com.example.evofit.data.local.entities.SyncStatus
import com.example.evofit.data.local.entities.WorkoutDoneEntity
import com.example.evofit.data.mapper.toDomain
import com.example.evofit.data.mapper.toEntity
import com.example.evofit.core.common.DateMapper
import com.example.evofit.domain.model.Workout
import com.example.evofit.domain.model.WorkoutDone
import com.example.evofit.domain.repository.WorkoutRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
    private val workoutRemoteDataSource: WorkoutRemoteDataSource
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
        
        val workoutId = java.util.UUID.randomUUID().toString()
        val workoutEntity = workout.toEntity().copy(
            workoutId = workoutId,
            orderIndex = nextOrderIndex,
            updatedAt = System.currentTimeMillis(),
            syncStatus = SyncStatus.PENDING
        )
        
        val exercises = workout.exercisesByGroup.flatMap { group ->
            group.exercises.map { exercise ->
                val workoutExerciseUuid = if (exercise.id.isEmpty()) java.util.UUID.randomUUID().toString() else exercise.id
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

        // Sync em background opcional ou aguardar o SyncWorker
        triggerBackgroundSync(workout.userId)

        return workoutId
    }

    override suspend fun updateWorkout(workout: Workout): String {
        val workoutEntity = workout.toEntity().copy(
            updatedAt = System.currentTimeMillis(),
            syncStatus = SyncStatus.PENDING
        )

        val exercises = workout.exercisesByGroup.flatMap { group ->
            group.exercises.map { exercise ->
                val workoutExerciseUuid = if (exercise.id.isEmpty()) java.util.UUID.randomUUID().toString() else exercise.id
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

        triggerBackgroundSync(workout.userId)

        return workout.id
    }

    override suspend fun deleteWorkout(workoutId: String) {
        workoutDataSource.getFullWorkoutById(workoutId).firstOrNull()?.workout?.let { workout ->
            workoutDataSource.softDeleteWorkout(workoutId, System.currentTimeMillis())
            triggerBackgroundSync(workout.userId)
        }
    }

    private fun triggerBackgroundSync(userId: String) {
        // Idealmente aqui chamaríamos um WorkManager, mas para manter compatibilidade:
        scope.launch {
            // Note: SyncUserDataUseCase should be injected if we want to trigger it here
            // For now, the existing Sync mechanism in ViewModels will handle it or we can add it to DI
        }
    }

    override suspend fun updateWorkoutsOrder(workouts: List<Workout>) {
        val entities = workouts.map { 
            it.toEntity().copy(
                syncStatus = SyncStatus.PENDING,
                updatedAt = System.currentTimeMillis()
            )
        }
        workoutDataSource.updateWorkoutsOrder(entities)

        if (entities.isNotEmpty()) {
            triggerBackgroundSync(entities.first().userId)
        }
    }

    override suspend fun getMaxOrderIndex(userId: String): Int {
        return workoutDataSource.getMaxOrderIndex(userId) ?: -1
    }

    override suspend fun saveWorkoutDone(userId: String, workoutDone: WorkoutDone) {
        val nextId = if (workoutDone.id.isEmpty()) java.util.UUID.randomUUID().toString() else workoutDone.id
        val workoutWithId = workoutDone.copy(id = nextId)
        
        // Save Local (New structure)
        val entity = workoutWithId.toEntity().copy(
            syncStatus = SyncStatus.PENDING,
            isDeleted = false
        )
        workoutDataSource.insertWorkoutDone(entity)

        triggerBackgroundSync(userId)
    }

    override suspend fun deleteWorkoutDone(userId: String, workoutDoneId: String) {
        workoutDataSource.softDeleteWorkoutDone(workoutDoneId, System.currentTimeMillis())
        triggerBackgroundSync(userId)
    }

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    override fun getWorkoutDoneHistory(userId: String): Flow<List<WorkoutDone>> {
        return flow {
            // Reaproveita a lógica de migração se necessário
            val hasData = workoutDataSource.getLatestWorkoutDoneHistory(userId, 1).first().isNotEmpty()
            if (!hasData) {
                val legacy = workoutDataSource.getWorkoutDoneHistory(userId).firstOrNull()
                if (legacy != null && legacy.history.isNotEmpty()) {
                    legacy.history.forEach { workout ->
                        val migratedWorkout = if (workout.createdAt == 0L || workout.createdAt > System.currentTimeMillis() - 1000) {
                            // Tenta recuperar a data original para não perder a ordem
                            val timestamp = DateMapper.parseDate(workout.date)?.time ?: workout.createdAt
                            workout.copy(createdAt = timestamp)
                        } else workout
                        
                        workoutDataSource.insertWorkoutDone(migratedWorkout.toEntity())
                    }
                    scope.launch {
                        workoutDataSource.deleteWorkoutHistorySummary(userId)
                        workoutRemoteDataSource.deleteOldHistorySummary(userId)
                    }
                }
            }
            emit(Unit)
        }.flatMapLatest {
            val muscleGroups = exerciseDataSource.getAllMuscleGroups().map { it.toDomain() }
            val exerciseNames = exerciseDataSource.getAllExercises().associate { it.id to it.name }
            val resolver = { id: String -> exerciseNames[id] ?: "" }
            
            workoutDataSource.getAllWorkoutDoneHistory(userId).map { list ->
                list.map { it.toDomain(muscleGroups, resolver) }
            }
        }
    }

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    override fun getWorkoutDoneHistory(userId: String, limit: Int): Flow<List<WorkoutDone>> {
        return flow {
            val muscleGroups = exerciseDataSource.getAllMuscleGroups().map { it.toDomain() }
            val exerciseNames = exerciseDataSource.getAllExercises().associate { it.id to it.name }
            val resolver = { id: String -> exerciseNames[id] ?: "" }

            // 1. Verificar se existe histórico na nova estrutura local
            val newHistory = workoutDataSource.getLatestWorkoutDoneHistory(userId, limit).first()
            
            if (newHistory.isNotEmpty()) {
                emit(newHistory.map { it.toDomain(muscleGroups, resolver) })
            } else {
                // 2. Se a nova estrutura estiver vazia, tenta migrar do formato antigo
                val legacyHistory = workoutDataSource.getWorkoutDoneHistory(userId).firstOrNull()
                
                if (legacyHistory != null && legacyHistory.history.isNotEmpty()) {
                    // Migra localmente
                    val migratedHistory = legacyHistory.history.map { workout ->
                        if (workout.createdAt == 0L || workout.createdAt > System.currentTimeMillis() - 1000) {
                            val timestamp = DateMapper.parseDate(workout.date)?.time ?: workout.createdAt
                            workout.copy(createdAt = timestamp)
                        } else workout
                    }

                    migratedHistory.forEach { workout ->
                        workoutDataSource.insertWorkoutDone(workout.toEntity())
                    }
                    // Emite os dados migrados (limitados)
                    emit(migratedHistory.sortedByDescending { it.createdAt }.take(limit))
                    
                    // Limpa o legado local e remoto em background
                    scope.launch {
                        try {
                            workoutDataSource.deleteWorkoutHistorySummary(userId)
                            workoutRemoteDataSource.deleteOldHistorySummary(userId)
                            // Opcional: Salva os itens individuais no Firestore também
                            migratedHistory.forEach { workout ->
                                workoutRemoteDataSource.saveWorkoutDone(workout)
                            }
                        } catch (e: Exception) {
                            // Log error
                        }
                    }
                } else {
                    // 3. Se nem no local antigo tem nada, tenta buscar do remoto (opcional se o sync já cuida disso)
                    emit(emptyList<WorkoutDone>())
                }
            }
        }.flatMapLatest { initialData ->
            val muscleGroups = exerciseDataSource.getAllMuscleGroups().map { it.toDomain() }
            val exerciseNames = exerciseDataSource.getAllExercises().associate { it.id to it.name }
            val resolver = { id: String -> exerciseNames[id] ?: "" }

            // Retorna o flow contínuo da nova tabela
            workoutDataSource.getLatestWorkoutDoneHistory(userId, limit).map { list ->
                list.map { it.toDomain(muscleGroups, resolver) }
            }
        }
    }

    override fun getWorkoutDoneSince(userId: String, sinceTimestamp: Long): Flow<List<WorkoutDone>> {
        val muscleGroups = exerciseDataSource.getAllMuscleGroups().map { it.toDomain() }
        val exerciseNames = exerciseDataSource.getAllExercises().associate { it.id to it.name }
        val resolver = { id: String -> exerciseNames[id] ?: "" }

        return workoutDataSource.getWorkoutsSince(userId, sinceTimestamp).map { list ->
            list.map { it.toDomain(muscleGroups, resolver) }
        }
    }
}
