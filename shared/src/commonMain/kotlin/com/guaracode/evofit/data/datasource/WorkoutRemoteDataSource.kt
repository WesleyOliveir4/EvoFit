package com.guaracode.evofit.data.datasource

import com.guaracode.evofit.core.monitoring.CrashReporter
import com.guaracode.evofit.data.local.entities.ExerciseSetEntity
import com.guaracode.evofit.data.local.entities.FullWorkoutRemoteData
import com.guaracode.evofit.data.local.entities.WorkoutDoneHistoryEntity
import com.guaracode.evofit.data.local.entities.WorkoutEntity
import com.guaracode.evofit.data.local.entities.WorkoutExerciseEntity
import com.guaracode.evofit.data.mapper.fixInconsistencies
import com.guaracode.evofit.domain.model.WorkoutDone
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.FieldValue
import dev.gitlive.firebase.firestore.FirebaseFirestore

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

private fun DocumentSnapshot.getSafeInt(field: String, default: Int = 0): Int {
    return try {
        get<Int?>(field) ?: get<Long?>(field)?.toInt() ?: default
    } catch (e: Exception) {
        try {
            get<Double?>(field)?.toInt() ?: default
        } catch (e2: Exception) {
            default
        }
    }
}

private fun DocumentSnapshot.getSafeIntNullable(field: String): Int? {
    return try {
        get<Int?>(field) ?: get<Long?>(field)?.toInt()
    } catch (e: Exception) {
        try {
            get<Double?>(field)?.toInt()
        } catch (e2: Exception) {
            null
        }
    }
}

private fun DocumentSnapshot.getSafeDouble(field: String, default: Double = 0.0): Double {
    return try {
        get<Double?>(field) ?: get<Long?>(field)?.toDouble() ?: default
    } catch (e: Exception) {
        try {
            get<Int?>(field)?.toDouble() ?: default
        } catch (e2: Exception) {
            default
        }
    }
}

private fun DocumentSnapshot.getSafeDoubleNullable(field: String): Double? {
    return try {
        get<Double?>(field) ?: get<Long?>(field)?.toDouble()
    } catch (e: Exception) {
        try {
            get<Int?>(field)?.toDouble()
        } catch (e2: Exception) {
            null
        }
    }
}

private fun DocumentSnapshot.getSafeString(field: String, default: String = ""): String {
    return try {
        get<String?>(field) ?: default
    } catch (e: Exception) {
        default
    }
}

private fun DocumentSnapshot.getSafeBoolean(field: String, default: Boolean = false): Boolean {
    return try {
        get<Boolean?>(field) ?: default
    } catch (e: Exception) {
        default
    }
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
            val exerciseMap = mapOf<String, Any?>(
                "id" to exercise.id,
                "workoutId" to exercise.workoutId,
                "exerciseId" to exercise.exerciseId,
                "muscleGroupId" to exercise.muscleGroupId,
                "orderIndex" to exercise.orderIndex,
                "groupOrderIndex" to exercise.groupOrderIndex,
                "totalSets" to exercise.totalSets
            )
            batch.set(exerciseRef, exerciseMap)
            
            sets[index].forEach { set ->
                val setRef = exerciseRef.collection("sets").document(set.setNumber.toString())
                val setMap = mapOf<String, Any?>(
                    "id" to set.id,
                    "workoutExerciseId" to set.workoutExerciseId,
                    "setNumber" to set.setNumber,
                    "reps" to set.reps,
                    "load" to set.load,
                    "unit" to set.unit.name,
                    "time" to set.time,
                    "distance" to set.distance
                )
                batch.set(setRef, setMap)
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
        val workoutDoneMap = workoutDoneToMap(workoutDone)

        firestore.collection("users")
            .document(workoutDone.userId)
            .collection("history")
            .document(workoutDone.id)
            .set(workoutDoneMap)
    }

    private fun workoutDoneToMap(workoutDone: WorkoutDone): Map<String, Any?> {
        return mapOf(
            "id" to workoutDone.id,
            "userId" to workoutDone.userId,
            "name" to workoutDone.name,
            "date" to workoutDone.date,
            "time" to workoutDone.time,
            "createdAt" to workoutDone.createdAt,
            "exercisesByGroup" to workoutDone.exercisesByGroup.map { group ->
                mapOf(
                    "muscleGroupId" to group.muscleGroupId,
                    "muscleGroup" to group.muscleGroup?.let { mg ->
                        mapOf(
                            "id" to mg.id,
                            "name" to mg.name,
                            "type" to mg.type.name,
                            "category" to mg.category.name
                        )
                    },
                    "orderIndex" to group.orderIndex,
                    "exercises" to group.exercises.map { exercise ->
                        mapOf(
                            "id" to exercise.id,
                            "exerciseId" to exercise.exerciseId,
                            "muscleGroupId" to exercise.muscleGroupId,
                            "totalSets" to exercise.totalSets,
                            "orderIndex" to exercise.orderIndex,
                            "sets" to exercise.sets.map { set ->
                                mapOf(
                                    "id" to set.id,
                                    "exerciseName" to set.exerciseName,
                                    "workoutExerciseId" to set.workoutExerciseId,
                                    "setNumber" to set.setNumber,
                                    "reps" to set.reps,
                                    "load" to set.load,
                                    "unit" to set.unit.name,
                                    "time" to set.time,
                                    "distance" to set.distance
                                )
                            }
                        )
                    }
                )
            }
        )
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
                    doc.data(WorkoutDone.serializer()).fixInconsistencies()
                } catch (e: Exception) {
                    crashReporter.recordException(e)
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
                        doc.data(WorkoutDone.serializer()).fixInconsistencies()
                    } catch (e: Exception) {
                        crashReporter.recordException(e)
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
                if (!workoutDoc.exists) return@mapNotNull null
                val workoutId: String = workoutDoc.getSafeString("workoutId", workoutDoc.id)
                val workoutUserId: String = workoutDoc.getSafeString("userId", userId)
                val name: String = workoutDoc.getSafeString("name")
                val date: String = workoutDoc.getSafeString("date")
                val orderIndex: Int = workoutDoc.getSafeInt("orderIndex")
                val isDeleted: Boolean = workoutDoc.getSafeBoolean("isDeleted")

                val workout = WorkoutEntity(
                    workoutId = workoutId,
                    userId = workoutUserId,
                    name = name,
                    date = date,
                    orderIndex = orderIndex,
                    isDeleted = isDeleted
                )

                val exercisesSnapshot = workoutDoc.reference.collection("exercises").get()
                val exercisesWithSets = exercisesSnapshot.documents.mapNotNull { exerciseDoc ->
                    if (!exerciseDoc.exists) return@mapNotNull null
                    val exId: String = exerciseDoc.getSafeString("id", exerciseDoc.id)
                    val exWorkoutId: String = exerciseDoc.getSafeString("workoutId", workoutId)
                    val exerciseId: String = exerciseDoc.getSafeString("exerciseId")
                    val muscleGroupId: String = exerciseDoc.getSafeString("muscleGroupId")
                    val exOrderIndex: Int = exerciseDoc.getSafeInt("orderIndex")
                    val groupOrderIndex: Int = exerciseDoc.getSafeInt("groupOrderIndex")
                    val totalSets: Int = exerciseDoc.getSafeInt("totalSets")

                    val exercise = WorkoutExerciseEntity(
                        id = exId,
                        workoutId = exWorkoutId,
                        exerciseId = exerciseId,
                        muscleGroupId = muscleGroupId,
                        orderIndex = exOrderIndex,
                        groupOrderIndex = groupOrderIndex,
                        totalSets = totalSets
                    )

                    val setsSnapshot = exerciseDoc.reference.collection("sets").get()
                    val sets = setsSnapshot.documents.mapNotNull { setDoc ->
                        if (!setDoc.exists) return@mapNotNull null
                        val setId: String = setDoc.getSafeString("id", setDoc.id)
                        val setWorkoutExerciseId: String = setDoc.getSafeString("workoutExerciseId", exId)
                        val setNumber: Int = setDoc.getSafeInt("setNumber")
                        val reps: Int = setDoc.getSafeInt("reps")
                        val load: Double = setDoc.getSafeDouble("load")
                        val unitStr: String = setDoc.getSafeString("unit", "WEIGHT")
                        val time: Int? = setDoc.getSafeIntNullable("time")
                        val distance: Double? = setDoc.getSafeDoubleNullable("distance")

                        val unit = try {
                            com.guaracode.evofit.domain.model.MeasurementUnit.valueOf(unitStr)
                        } catch (e: Exception) {
                            com.guaracode.evofit.domain.model.MeasurementUnit.WEIGHT
                        }

                        ExerciseSetEntity(
                            id = setId,
                            workoutExerciseId = setWorkoutExerciseId,
                            setNumber = setNumber,
                            reps = reps,
                            load = load,
                            unit = unit,
                            time = time,
                            distance = distance
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
