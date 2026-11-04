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
                if (!workoutDoc.exists) return@mapNotNull null
                val workoutId: String = try { workoutDoc.get("workoutId") } catch (e: Exception) { workoutDoc.id }
                val workoutUserId: String = try { workoutDoc.get("userId") } catch (e: Exception) { userId }
                val name: String = try { workoutDoc.get("name") } catch (e: Exception) { "" }
                val date: String = try { workoutDoc.get("date") } catch (e: Exception) { "" }
                val orderIndex: Int = try { (workoutDoc.get("orderIndex") as Number).toInt() } catch (e: Exception) { 0 }
                val isDeleted: Boolean = try { workoutDoc.get("isDeleted") } catch (e: Exception) { false }

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
                    val exId: String = try { exerciseDoc.get("id") } catch (e: Exception) { exerciseDoc.id }
                    val exWorkoutId: String = try { exerciseDoc.get("workoutId") } catch (e: Exception) { workoutId }
                    val exerciseId: String = try { exerciseDoc.get("exerciseId") } catch (e: Exception) { "" }
                    val muscleGroupId: String = try { exerciseDoc.get("muscleGroupId") } catch (e: Exception) { "" }
                    val exOrderIndex: Int = try { (exerciseDoc.get("orderIndex") as Number).toInt() } catch (e: Exception) { 0 }
                    val groupOrderIndex: Int = try { (exerciseDoc.get("groupOrderIndex") as Number).toInt() } catch (e: Exception) { 0 }
                    val totalSets: Int = try { (exerciseDoc.get("totalSets") as Number).toInt() } catch (e: Exception) { 0 }

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
                        val setId: String = try { setDoc.get("id") } catch (e: Exception) { setDoc.id }
                        val setWorkoutExerciseId: String = try { setDoc.get("workoutExerciseId") } catch (e: Exception) { exId }
                        val setNumber: Int = try { (setDoc.get("setNumber") as Number).toInt() } catch (e: Exception) { 0 }
                        val reps: Int = try { (setDoc.get("reps") as Number).toInt() } catch (e: Exception) { 0 }
                        val load: Double = try { (setDoc.get("load") as Number).toDouble() } catch (e: Exception) { 0.0 }
                        val unitStr: String = try { setDoc.get("unit") } catch (e: Exception) { "WEIGHT" }
                        val time: Int? = try { (setDoc.get("time") as? Number)?.toInt() } catch (e: Exception) { null }
                        val distance: Double? = try { (setDoc.get("distance") as? Number)?.toDouble() } catch (e: Exception) { null }

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
