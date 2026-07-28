package com.example.evofit.data.datasource

import android.util.Log
import com.example.evofit.data.local.entities.ExerciseSetEntity
import com.example.evofit.data.local.entities.FullWorkoutRemoteData
import com.example.evofit.data.local.entities.WorkoutDoneHistoryEntity
import com.example.evofit.data.local.entities.WorkoutEntity
import com.example.evofit.data.local.entities.WorkoutExerciseEntity
import com.example.evofit.domain.model.WorkoutDone
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
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
    suspend fun getAllWorkouts(userId: String): List<FullWorkoutRemoteData>
    suspend fun getWorkoutDoneHistory(userId: String): WorkoutDoneHistoryEntity?

    // New History structure
    suspend fun saveWorkoutDone(workoutDone: WorkoutDone)
    suspend fun getLatestWorkoutDoneHistory(userId: String, limit: Int): List<WorkoutDone>
    suspend fun getAllWorkoutDoneHistory(userId: String): List<WorkoutDone>
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
        val batch = firestore.batch()
        
        // Save Workout
        val workoutRef = firestore.collection("users")
            .document(workout.userId)
            .collection("workouts")
            .document(workout.workoutId)
        batch.set(workoutRef, workout)

        // Save Exercises and Sets
        exercises.forEachIndexed { index, exercise ->
            val exerciseRef = workoutRef.collection("exercises").document(exercise.id)
            batch.set(exerciseRef, exercise)
            
            sets[index].forEach { set ->
                val setRef = exerciseRef.collection("sets").document(set.id)
                batch.set(setRef, set)
            }
        }

        batch.commit().await()
    }

    override suspend fun deleteWorkout(userId: String, workoutId: String) {
        try {
            val workoutRef = firestore.collection("users")
                .document(userId)
                .collection("workouts")
                .document(workoutId)

            val batch = firestore.batch()

            // 1. Fetch Exercises
            val exercisesSnapshot = workoutRef.collection("exercises").get().await()

            for (exerciseDoc in exercisesSnapshot.documents) {
                // 2. Fetch Sets for each Exercise
                val setsSnapshot = exerciseDoc.reference.collection("sets").get().await()
                
                // 3. Add all Sets to batch deletion
                for (setDoc in setsSnapshot.documents) {
                    batch.delete(setDoc.reference)
                }

                // 4. Add Exercise to batch deletion
                batch.delete(exerciseDoc.reference)
            }

            // 5. Add Workout to batch deletion
            batch.delete(workoutRef)

            // 6. Commit everything atomically
            batch.commit().await()
            
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
            batch.update(docRef, "orderIndex", workout.orderIndex, "updatedAt", workout.updatedAt)
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
        firestore.collection("users")
            .document(workoutDone.userId)
            .collection("history")
            .document(workoutDone.id)
            .set(workoutDone)
            .await()
    }

    override suspend fun getLatestWorkoutDoneHistory(userId: String, limit: Int): List<WorkoutDone> {
        return try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("history")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()
            
            snapshot.toObjects(WorkoutDone::class.java)
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao buscar historico recente: $userId", e)
            emptyList()
        }
    }

    override suspend fun getAllWorkoutDoneHistory(userId: String): List<WorkoutDone> {
        return try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("history")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
            
            // Filtramos o "summary" se ele ainda existir na lista (toObjects pode tentar mapear se houver campos iguais)
            snapshot.documents
                .filter { it.id != "summary" }
                .mapNotNull { it.toObject<WorkoutDone>() }
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao buscar todo o historico: $userId", e)
            emptyList()
        }
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

    override suspend fun getAllWorkouts(userId: String): List<FullWorkoutRemoteData> {
        return try {
            val workoutsSnapshot = firestore.collection("users")
                .document(userId)
                .collection("workouts")
                .get()
                .await()

            val fullWorkouts = mutableListOf<FullWorkoutRemoteData>()

            for (workoutDoc in workoutsSnapshot.documents) {
                val workout = workoutDoc.toObject<WorkoutEntity>() ?: continue
                
                // Fetch Exercises
                val exercisesSnapshot = workoutDoc.reference.collection("exercises").get().await()
                val exercises = mutableListOf<WorkoutExerciseEntity>()
                val setsList = mutableListOf<List<ExerciseSetEntity>>()

                for (exerciseDoc in exercisesSnapshot.documents) {
                    val exercise = exerciseDoc.toObject<WorkoutExerciseEntity>() ?: continue
                    exercises.add(exercise)

                    // Fetch Sets for this exercise
                    val setsSnapshot = exerciseDoc.reference.collection("sets").get().await()
                    val sets = setsSnapshot.toObjects(ExerciseSetEntity::class.java)
                    setsList.add(sets)
                }

                fullWorkouts.add(FullWorkoutRemoteData(workout, exercises, setsList))
            }
            fullWorkouts
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao buscar todos os treinos: $userId", e)
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
                document.toObject<WorkoutDoneHistoryEntity>()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao buscar historico: $userId", e)
            null
        }
    }
}
