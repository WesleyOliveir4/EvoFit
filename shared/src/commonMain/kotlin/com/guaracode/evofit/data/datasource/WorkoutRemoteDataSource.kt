package com.guaracode.evofit.data.datasource

import com.guaracode.evofit.core.monitoring.CrashReporter
import com.guaracode.evofit.data.local.entities.ExerciseSetEntity
import com.guaracode.evofit.data.local.entities.FullWorkoutRemoteData
import com.guaracode.evofit.data.local.entities.WorkoutDoneHistoryEntity
import com.guaracode.evofit.data.local.entities.WorkoutEntity
import com.guaracode.evofit.data.local.entities.WorkoutExerciseEntity
import com.guaracode.evofit.domain.model.WorkoutDone
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.FieldValue
import dev.gitlive.firebase.firestore.Direction

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

    suspend fun saveWorkoutDone(workoutDone: WorkoutDone)
    suspend fun deleteWorkoutDone(userId: String, workoutDoneId: String)
    suspend fun getLatestWorkoutDoneHistory(userId: String, limit: Int): List<WorkoutDone>
    suspend fun getAllWorkoutDoneHistory(userId: String, sinceTimestamp: Long = 0L): List<WorkoutDone>
    suspend fun getWorkoutsSince(userId: String, sinceTimestamp: Long): List<WorkoutDone>
    suspend fun deleteOldHistorySummary(userId: String)
}

class WorkoutRemoteDataSourceImpl(
    private val firestore: FirebaseFirestore,
    private val crashReporter: CrashReporter
) : WorkoutRemoteDataSource {

    override suspend fun saveFullWorkout(
        workout: WorkoutEntity,
        exercises: List<WorkoutExerciseEntity>,
        sets: List<List<ExerciseSetEntity>>
    ) {
        val workoutRef = firestore.collection("users")
            .document(workout.userId)
            .collection("workouts")
            .document(workout.workoutId)

        val exercisesSnapshot = workoutRef.collection("exercises").get()
        val deleteBatch = firestore.batch()
        for (exerciseDoc in exercisesSnapshot.documents) {
            val setsSnapshot = exerciseDoc.reference.collection("sets").get()
            for (setDoc in setsSnapshot.documents) {
                deleteBatch.delete(setDoc.reference)
            }
            deleteBatch.delete(exerciseDoc.reference)
        }
        if (exercisesSnapshot.documents.isNotEmpty()) {
            deleteBatch.commit()
        }

        val batch = firestore.batch()
        val workoutMap = mapOf<String, Any?>(
            "workoutId" to workout.workoutId,
            "userId" to workout.userId,
            "name" to workout.name,
            "date" to workout.date,
            "orderIndex" to workout.orderIndex,
            "updatedAt" to FieldValue.serverTimestamp
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

        batch.commit()
    }

    override suspend fun deleteWorkout(userId: String, workoutId: String) {
        try {
            firestore.collection("users")
                .document(userId)
                .collection("workouts")
                .document(workoutId)
                .delete()
        } catch (e: Exception) {
            crashReporter.recordException(e)
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
            batch.update(docRef, mapOf("orderIndex" to workout.orderIndex, "updatedAt" to FieldValue.serverTimestamp))
        }
        batch.commit()
    }

    override suspend fun saveWorkoutDoneHistory(history: WorkoutDoneHistoryEntity) {
        firestore.collection("users")
            .document(history.userId)
            .collection("history")
            .document("summary")
            .set(history)
    }

    override suspend fun saveWorkoutDone(workoutDone: WorkoutDone) {
        val workoutDoneMap = mapOf<String, Any?>(
            "id" to workoutDone.id,
            "userId" to workoutDone.userId,
            "name" to workoutDone.name,
            "date" to workoutDone.date,
            "exercisesByGroup" to workoutDone.exercisesByGroup,
            "time" to workoutDone.time,
            "createdAt" to FieldValue.serverTimestamp
        )

        firestore.collection("users")
            .document(workoutDone.userId)
            .collection("history")
            .document(workoutDone.id)
            .set(workoutDoneMap)
    }

    override suspend fun deleteWorkoutDone(userId: String, workoutDoneId: String) {
        firestore.collection("users")
            .document(userId)
            .collection("history")
            .document(workoutDoneId)
            .delete()
    }

    override suspend fun getLatestWorkoutDoneHistory(userId: String, limit: Int): List<WorkoutDone> {
        return try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("history")
                .orderBy("createdAt", Direction.DESCENDING)
                .limit(limit)
                .get()
            
            snapshot.documents.mapNotNull { doc ->
                try {
                    doc.data(WorkoutDone.serializer())
                } catch (e: Exception) {
                    null
                }
            }
        } catch (e: Exception) {
            crashReporter.recordException(e)
            emptyList()
        }
    }

    override suspend fun getAllWorkoutDoneHistory(userId: String, sinceTimestamp: Long): List<WorkoutDone> {
        return try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("history")
                .orderBy("createdAt", Direction.DESCENDING)
                .get()
            
            snapshot.documents
                .filter { it.id != "summary" }
                .mapNotNull { doc ->
                    try {
                        doc.data(WorkoutDone.serializer())
                    } catch (e: Exception) {
                        null
                    }
                }
        } catch (e: Exception) {
            crashReporter.recordException(e)
            emptyList()
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
        } catch (e: Exception) {
            crashReporter.recordException(e)
        }
    }

    override suspend fun getAllWorkouts(userId: String, sinceTimestamp: Long): List<FullWorkoutRemoteData> {
        return try {
            val workoutsSnapshot = firestore.collection("users")
                .document(userId)
                .collection("workouts")
                .get()

            workoutsSnapshot.documents.mapNotNull { workoutDoc ->
                val data = workoutDoc.data<Map<String, Any?>>()
                val workout = WorkoutEntity(
                    workoutId = data["workoutId"] as? String ?: workoutDoc.id,
                    userId = data["userId"] as? String ?: userId,
                    name = data["name"] as? String ?: "",
                    date = data["date"] as? String ?: "",
                    orderIndex = (data["orderIndex"] as? Long)?.toInt() ?: 0
                )
                val exercisesSnapshot = workoutDoc.reference.collection("exercises").get()
                val exercisesWithSets = exercisesSnapshot.documents.mapNotNull { exerciseDoc ->
                    val exData = exerciseDoc.data<Map<String, Any?>>()
                    val exercise = WorkoutExerciseEntity(
                        id = exData["id"] as? String ?: exerciseDoc.id,
                        workoutId = exData["workoutId"] as? String ?: "",
                        exerciseId = exData["exerciseId"] as? String ?: "",
                        muscleGroupId = exData["muscleGroupId"] as? String ?: "",
                        orderIndex = (exData["orderIndex"] as? Long)?.toInt() ?: 0,
                        groupOrderIndex = (exData["groupOrderIndex"] as? Long)?.toInt() ?: 0,
                        totalSets = (exData["totalSets"] as? Long)?.toInt() ?: 0
                    )
                    val setsSnapshot = exerciseDoc.reference.collection("sets").get()
                    val sets = setsSnapshot.documents.mapNotNull { setDoc ->
                        val setData = setDoc.data<Map<String, Any?>>()
                        ExerciseSetEntity(
                            id = setData["id"] as? String ?: setDoc.id,
                            workoutExerciseId = setData["workoutExerciseId"] as? String ?: "",
                            setNumber = (setData["setNumber"] as? Long)?.toInt() ?: 0,
                            reps = (setData["reps"] as? Long)?.toInt() ?: 0,
                            load = (setData["load"] as? Double) ?: (setData["load"] as? Long)?.toDouble() ?: 0.0,
                            unit = com.guaracode.evofit.domain.model.MeasurementUnit.valueOf(setData["unit"] as? String ?: "WEIGHT"),
                            time = (setData["time"] as? Long)?.toInt(),
                            distance = (setData["distance"] as? Double) ?: (setData["distance"] as? Long)?.toDouble()
                        )
                    }
                    exercise to sets
                }
                FullWorkoutRemoteData(workout, exercisesWithSets.map { it.first }, exercisesWithSets.map { it.second })
            }
        } catch (e: Exception) {
            crashReporter.recordException(e)
            emptyList()
        }
    }

    override suspend fun getWorkoutDoneHistory(userId: String): WorkoutDoneHistoryEntity? {
        return try {
            val doc = firestore.collection("users")
                .document(userId)
                .collection("history")
                .document("summary")
                .get()
            if (doc.exists) {
                doc.data(WorkoutDoneHistoryEntity.serializer())
            } else null
        } catch (e: Exception) {
            crashReporter.recordException(e)
            null
        }
    }
}
