package com.example.evofit.data.datasource

import android.util.Log
import com.example.evofit.data.local.entities.ExerciseSetEntity
import com.example.evofit.data.local.entities.FullWorkoutRemoteData
import com.example.evofit.data.local.entities.WorkoutDoneHistoryEntity
import com.example.evofit.data.local.entities.WorkoutEntity
import com.example.evofit.data.local.entities.WorkoutExerciseEntity
import com.example.evofit.data.mapper.fixInconsistencies
import com.example.evofit.domain.model.WorkoutDone
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await

interface WorkoutRemoteDataSource {
    suspend fun saveFullWorkout(
        workout: WorkoutEntity,
        exercises: List<WorkoutExerciseEntity>,
        sets: List<List<ExerciseSetEntity>>
    )
    suspend fun deleteWorkout(userId: String, workoutId: String)
    suspend fun updateWorkoutsOrder(userId: String, workouts: List<WorkoutEntity>)
    suspend fun saveWorkoutDoneHistory(history: WorkoutDoneHistoryEntity)
    suspend fun getAllWorkouts(userId: String, sinceTimestamp: Long = 0L): List<FullWorkoutRemoteData>
    suspend fun getWorkoutDoneHistory(userId: String): WorkoutDoneHistoryEntity?

    // New History structure
    suspend fun saveWorkoutDone(workoutDone: WorkoutDone)
    suspend fun deleteWorkoutDone(userId: String, workoutDoneId: String)
    suspend fun getLatestWorkoutDoneHistory(userId: String, limit: Int): List<WorkoutDone>
    suspend fun getAllWorkoutDoneHistory(userId: String, sinceTimestamp: Long = 0L): List<WorkoutDone>
    suspend fun getWorkoutsSince(userId: String, sinceTimestamp: Long): List<WorkoutDone>
    suspend fun deleteOldHistorySummary(userId: String)
}

class WorkoutRemoteDataSourceImpl(
    private val firestore: FirebaseFirestore
) : WorkoutRemoteDataSource {

    companion object {
        private const val TAG = "EvoFit_Debug"
    }

    override suspend fun saveFullWorkout(
        workout: WorkoutEntity,
        exercises: List<WorkoutExerciseEntity>,
        sets: List<List<ExerciseSetEntity>>
    ) {
        val workoutRef = firestore.collection("users")
            .document(workout.userId)
            .collection("workouts")
            .document(workout.workoutId)

        // 1. Limpeza: Deleta exercícios e séries antigos antes de salvar o novo estado
        val exercisesSnapshot = workoutRef.collection("exercises").get().await()
        val deleteBatch = firestore.batch()
        for (exerciseDoc in exercisesSnapshot.documents) {
            val setsSnapshot = exerciseDoc.reference.collection("sets").get().await()
            for (setDoc in setsSnapshot.documents) {
                deleteBatch.delete(setDoc.reference)
            }
            deleteBatch.delete(exerciseDoc.reference)
        }
        if (exercisesSnapshot.size() > 0) {
            deleteBatch.commit().await()
        }

        // 2. Escrita: Salva o treino e a nova estrutura de exercícios/séries
        val batch = firestore.batch()
        
        val workoutMap = mutableMapOf<String, Any>(
            "workoutId" to workout.workoutId,
            "userId" to workout.userId,
            "name" to workout.name,
            "date" to workout.date,
            "orderIndex" to workout.orderIndex,
            "updatedAt" to FieldValue.serverTimestamp()
        )
        batch.set(workoutRef, workoutMap)

        exercises.forEachIndexed { index, exercise ->
            val exerciseRef = workoutRef.collection("exercises").document(exercise.id)
            batch.set(exerciseRef, exercise)
            
            sets[index].forEach { set ->
                val setRef = exerciseRef.collection("sets").document(set.setNumber.toString())
                batch.set(setRef, set)
            }
        }

        batch.commit().await()
    }

    override suspend fun deleteWorkout(userId: String, workoutId: String) {
        try {
            // Deleção física no Firestore (Estratégia Clean Cloud)
            firestore.collection("users")
                .document(userId)
                .collection("workouts")
                .document(workoutId)
                .delete()
                .await()
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao deletar treino completo: $workoutId", e)
            throw e
        }
    }

    override suspend fun updateWorkoutsOrder(userId: String, workouts: List<WorkoutEntity>) {
        val batch = firestore.batch()
        workouts.forEach { workout ->
            val docRef = firestore.collection("users")
                .document(userId)
                .collection("workouts")
                .document(workout.workoutId)
            batch.update(docRef, "orderIndex", workout.orderIndex, "updatedAt", FieldValue.serverTimestamp())
        }
        batch.commit().await()
    }

    override suspend fun saveWorkoutDoneHistory(history: WorkoutDoneHistoryEntity) {
        firestore.collection("users")
            .document(history.userId)
            .collection("history")
            .document("summary")
            .set(history)
            .await()
    }

    override suspend fun saveWorkoutDone(workoutDone: WorkoutDone) {
        val workoutDoneMap = mutableMapOf<String, Any>(
            "id" to workoutDone.id,
            "userId" to workoutDone.userId,
            "name" to workoutDone.name,
            "date" to workoutDone.date,
            "exercisesByGroup" to workoutDone.exercisesByGroup,
            "time" to workoutDone.time,
            "createdAt" to FieldValue.serverTimestamp()
        )

        firestore.collection("users")
            .document(workoutDone.userId)
            .collection("history")
            .document(workoutDone.id)
            .set(workoutDoneMap)
            .await()
    }

    override suspend fun deleteWorkoutDone(userId: String, workoutDoneId: String) {
        // Deleção física no Firestore (Estratégia Clean Cloud)
        firestore.collection("users")
            .document(userId)
            .collection("history")
            .document(workoutDoneId)
            .delete()
            .await()
    }

    override suspend fun getLatestWorkoutDoneHistory(userId: String, limit: Int): List<WorkoutDone> {
        return try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("history")
                .whereEqualTo("isDeleted", false)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()
            
            snapshot.documents.mapNotNull { doc ->
                mapDocToWorkoutDone(doc)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getAllWorkoutDoneHistory(userId: String, sinceTimestamp: Long): List<WorkoutDone> {
        return try {
            val collectionRef = firestore.collection("users")
                .document(userId)
                .collection("history")

            val query = if (sinceTimestamp > 0) {
                collectionRef.whereGreaterThanOrEqualTo("createdAt", com.google.firebase.Timestamp(sinceTimestamp / 1000, ((sinceTimestamp % 1000) * 1000000).toInt()))
                    .orderBy("createdAt", Query.Direction.DESCENDING)
            } else {
                collectionRef.orderBy("createdAt", Query.Direction.DESCENDING)
            }
            
            val snapshot = query.get().await()
            
            snapshot.documents
                .filter { it.id != "summary" }
                .mapNotNull { doc ->
                    mapDocToWorkoutDone(doc)
                }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun mapDocToWorkoutDone(doc: com.google.firebase.firestore.DocumentSnapshot): WorkoutDone? {
        val data = doc.data ?: return null
        return try {
            // Mapeamento Manual Total para evitar crash de Timestamp -> Long no toObject
            val exercisesRaw = data["exercisesByGroup"] as? List<Map<String, Any>> ?: emptyList()
            
            WorkoutDone(
                id = data["id"] as? String ?: doc.id,
                userId = data["userId"] as? String ?: "",
                name = data["name"] as? String ?: "",
                date = data["date"] as? String ?: "",
                time = data["time"] as? String ?: "",
                createdAt = when (val cr = data["createdAt"]) {
                    is com.google.firebase.Timestamp -> cr.toDate().time
                    is Long -> cr
                    is Number -> cr.toLong()
                    else -> 0L
                },
                exercisesByGroup = exercisesRaw.map { groupMap ->
                    val muscleGroupMap = groupMap["muscleGroup"] as? Map<String, Any>
                    com.example.evofit.domain.model.WorkoutGroup(
                        muscleGroupId = groupMap["muscleGroupId"] as? String ?: "",
                        muscleGroup = muscleGroupMap?.let { mgMap ->
                            com.example.evofit.domain.model.MuscleGroup(
                                id = mgMap["id"] as? String ?: "",
                                name = mgMap["name"] as? String ?: "",
                                type = try {
                                    com.example.evofit.domain.model.MuscleGroupType.valueOf(mgMap["type"] as? String ?: "OTHER")
                                } catch (e: Exception) {
                                    com.example.evofit.domain.model.MuscleGroupType.OTHER
                                },
                                category = try {
                                    com.example.evofit.domain.model.ExerciseCategory.valueOf(mgMap["category"] as? String ?: "STRENGTH")
                                } catch (e: Exception) {
                                    com.example.evofit.domain.model.ExerciseCategory.STRENGTH
                                }
                            )
                        },
                        orderIndex = (groupMap["orderIndex"] as? Long)?.toInt() ?: 0,
                        exercises = (groupMap["exercises"] as? List<Map<String, Any>>)?.map { exMap ->
                            com.example.evofit.domain.model.WorkoutExercise(
                                id = exMap["id"] as? String ?: "",
                                exerciseId = exMap["exerciseId"] as? String ?: "",
                                muscleGroupId = exMap["muscleGroupId"] as? String ?: groupMap["muscleGroupId"] as? String ?: "",
                                totalSets = (exMap["totalSets"] as? Long)?.toInt() ?: 0,
                                orderIndex = (exMap["orderIndex"] as? Long)?.toInt() ?: 0,
                                sets = (exMap["sets"] as? List<Map<String, Any>>)?.map { setMap ->
                                    com.example.evofit.domain.model.ExerciseSet(
                                        id = setMap["id"] as? String ?: "",
                                        exerciseName = setMap["exerciseName"] as? String ?: "",
                                        workoutExerciseId = setMap["workoutExerciseId"] as? String ?: "",
                                        setNumber = (setMap["setNumber"] as? Long)?.toInt() ?: 0,
                                        reps = (setMap["reps"] as? Long)?.toInt() ?: 0,
                                        load = (setMap["load"] as? Double) ?: (setMap["load"] as? Long)?.toDouble() ?: 0.0,
                                        unit = com.example.evofit.domain.model.MeasurementUnit.valueOf(setMap["unit"] as? String ?: "WEIGHT"),
                                        time = (setMap["time"] as? Long)?.toInt(),
                                        distance = (setMap["distance"] as? Double) ?: (setMap["distance"] as? Long)?.toDouble()
                                    )
                                } ?: emptyList()
                            )
                        } ?: emptyList()
                    )
                }
            ).fixInconsistencies()
        } catch (e: Exception) {
            Log.e("EvoFit_Debug", "Falha no mapeamento manual de WorkoutDone: ${doc.id}", e)
            null
        }
    }

    override suspend fun getWorkoutsSince(userId: String, sinceTimestamp: Long): List<WorkoutDone> {
        return getAllWorkoutDoneHistory(userId, sinceTimestamp)
    }

    override suspend fun deleteOldHistorySummary(userId: String) {
        try {
            firestore.collection("users")
                .document(userId)
                .collection("history")
                .document("summary")
                .delete()
                .await()
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao deletar sumario antigo: $userId", e)
        }
    }

    override suspend fun getAllWorkouts(userId: String, sinceTimestamp: Long): List<FullWorkoutRemoteData> = coroutineScope {
        try {
            val collectionRef = firestore.collection("users")
                .document(userId)
                .collection("workouts")

            val query = if (sinceTimestamp > 0) {
                collectionRef.whereGreaterThanOrEqualTo("updatedAt", com.google.firebase.Timestamp(sinceTimestamp / 1000, ((sinceTimestamp % 1000) * 1000000).toInt()))
            } else {
                collectionRef
            }

            val workoutsSnapshot = query.get().await()

            val deferredFullWorkouts = workoutsSnapshot.documents.map { workoutDoc ->
                async {
                    val data = workoutDoc.data ?: return@async null
                    
                    val workout = WorkoutEntity(
                        workoutId = data["workoutId"] as? String ?: workoutDoc.id,
                        userId = data["userId"] as? String ?: userId,
                        name = data["name"] as? String ?: "",
                        date = data["date"] as? String ?: "",
                        orderIndex = (data["orderIndex"] as? Long)?.toInt() ?: 0,
                        isDeleted = data["isDeleted"] as? Boolean ?: false
                    )
                    
                    val updatedAtRaw = data["updatedAt"]
                    val updatedAt = when (updatedAtRaw) {
                        is com.google.firebase.Timestamp -> updatedAtRaw.toDate().time
                        is Long -> updatedAtRaw
                        is Number -> updatedAtRaw.toLong()
                        else -> 0L
                    }
                    workout.updatedAt = updatedAt

                    // Fetch Exercises
                    val exercisesSnapshot = try {
                        workoutDoc.reference.collection("exercises").get().await()
                    } catch (e: Exception) {
                        null
                    }
                    
                    val exercisesWithSets = exercisesSnapshot?.documents?.map { exerciseDoc ->
                        async {
                            try {
                                val exData = exerciseDoc.data ?: return@async null
                                val exercise = WorkoutExerciseEntity(
                                    id = exData["id"] as? String ?: exerciseDoc.id,
                                    workoutId = exData["workoutId"] as? String ?: "",
                                    exerciseId = exData["exerciseId"] as? String ?: "",
                                    muscleGroupId = exData["muscleGroupId"] as? String ?: "",
                                    orderIndex = (exData["orderIndex"] as? Long)?.toInt() ?: 0,
                                    groupOrderIndex = (exData["groupOrderIndex"] as? Long)?.toInt() ?: 0,
                                    totalSets = (exData["totalSets"] as? Long)?.toInt() ?: 0
                                )

                                // Fetch Sets for this exercise
                                val setsSnapshot = exerciseDoc.reference.collection("sets").get().await()
                                val sets = setsSnapshot.documents.mapNotNull { setDoc ->
                                    val setData = setDoc.data ?: return@mapNotNull null
                                    ExerciseSetEntity(
                                        id = setData["id"] as? String ?: setDoc.id,
                                        workoutExerciseId = setData["workoutExerciseId"] as? String ?: "",
                                        setNumber = (setData["setNumber"] as? Long)?.toInt() ?: 0,
                                        reps = (setData["reps"] as? Long)?.toInt() ?: 0,
                                        load = (setData["load"] as? Double) ?: (setData["load"] as? Long)?.toDouble() ?: 0.0,
                                        unit = com.example.evofit.domain.model.MeasurementUnit.valueOf(setData["unit"] as? String ?: "WEIGHT"),
                                        time = (setData["time"] as? Long)?.toInt(),
                                        distance = (setData["distance"] as? Double) ?: (setData["distance"] as? Long)?.toDouble()
                                    )
                                }
                                Pair(exercise, sets)
                            } catch (e: Exception) {
                                null
                            }
                        }
                    }?.awaitAll()?.filterNotNull() ?: emptyList()

                    val exercises = exercisesWithSets.map { it.first }
                    val setsList = exercisesWithSets.map { it.second }

                    FullWorkoutRemoteData(workout, exercises, setsList)
                }
            }
            deferredFullWorkouts.awaitAll().filterNotNull()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getWorkoutDoneHistory(userId: String): WorkoutDoneHistoryEntity? {
        return try {
            val document = firestore.collection("users")
                .document(userId)
                .collection("history")
                .document("summary")
                .get()
                .await()
            
            if (document.exists()) {
                val data = document.data ?: return null
                val historyRaw = data["history"] as? List<Map<String, Any>> ?: emptyList()
                
                WorkoutDoneHistoryEntity(
                    userId = data["userId"] as? String ?: userId,
                    history = historyRaw.mapNotNull { itemMap ->
                        try {
                            val createdAtRaw = itemMap["createdAt"]
                            val createdAt = when (createdAtRaw) {
                                is com.google.firebase.Timestamp -> createdAtRaw.toDate().time
                                is Long -> createdAtRaw
                                is Number -> createdAtRaw.toLong()
                                else -> 0L
                            }
                            
                            WorkoutDone(
                                id = itemMap["id"] as? String ?: "",
                                userId = itemMap["userId"] as? String ?: "",
                                name = itemMap["name"] as? String ?: "",
                                date = itemMap["date"] as? String ?: "",
                                time = itemMap["time"] as? String ?: "",
                                createdAt = createdAt
                            ).fixInconsistencies()
                        } catch (e: Exception) {
                            null
                        }
                    }
                )
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
