package com.example.evofit.data.datasource

import com.example.evofit.data.local.entities.ExerciseSetEntity
import com.example.evofit.data.local.entities.WorkoutDoneHistoryEntity
import com.example.evofit.data.local.entities.WorkoutEntity
import com.example.evofit.data.local.entities.WorkoutExerciseEntity
import com.google.firebase.firestore.FirebaseFirestore
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
}

data class FullWorkoutRemoteData(
    val workout: WorkoutEntity,
    val exercises: List<WorkoutExerciseEntity>,
    val sets: List<List<ExerciseSetEntity>>
)

class WorkoutRemoteDataSourceImpl(
    private val firestore: FirebaseFirestore
) : WorkoutRemoteDataSource {

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
        // Note: Firestore doesn't automatically delete subcollections. 
        // For a full implementation, we'd need to delete subcollections too, 
        // but for a "dual-write" MVP, deleting the main doc is a start.
        // A better approach is often a 'deleted' flag or a Cloud Function.
        firestore.collection("users")
            .document(userId)
            .collection("workouts")
            .document(workoutId)
            .delete()
            .await()
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
            .document("summary") // Keeping it simple for now, as the local entity stores a list
            .set(history)
            .await()
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
                val workout = workoutDoc.toObject(WorkoutEntity::class.java) ?: continue
                
                // Fetch Exercises
                val exercisesSnapshot = workoutDoc.reference.collection("exercises").get().await()
                val exercises = mutableListOf<WorkoutExerciseEntity>()
                val setsList = mutableListOf<List<ExerciseSetEntity>>()

                for (exerciseDoc in exercisesSnapshot.documents) {
                    val exercise = exerciseDoc.toObject(WorkoutExerciseEntity::class.java) ?: continue
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
            emptyList()
        }
    }

    override suspend fun getWorkoutDoneHistory(userId: String): WorkoutDoneHistoryEntity? {
        return try {
            firestore.collection("users")
                .document(userId)
                .collection("history")
                .document("summary")
                .get()
                .await()
                .toObject(WorkoutDoneHistoryEntity::class.java)
        } catch (e: Exception) {
            null
        }
    }
}
