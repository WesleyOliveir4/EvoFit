package com.example.evofit.data.datasource

import com.example.evofit.data.local.entities.UserEntity
import com.example.evofit.data.local.entities.UserGoalEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

interface UserRemoteDataSource {
    suspend fun saveUser(user: UserEntity)
    suspend fun saveGoals(userId: String, goals: List<UserGoalEntity>)
    suspend fun deleteGoal(userId: String, goalId: String)
}

class UserRemoteDataSourceImpl(
    private val firestore: FirebaseFirestore
) : UserRemoteDataSource {

    override suspend fun saveUser(user: UserEntity) {
        firestore.collection("users")
            .document(user.id)
            .set(user)
            .await()
    }

    override suspend fun saveGoals(userId: String, goals: List<UserGoalEntity>) {
        val batch = firestore.batch()
        goals.forEach { goal ->
            val docRef = firestore.collection("users")
                .document(userId)
                .collection("goals")
                .document(goal.id)
            batch.set(docRef, goal)
        }
        batch.commit().await()
    }

    override suspend fun deleteGoal(userId: String, goalId: String) {
        firestore.collection("users")
            .document(userId)
            .collection("goals")
            .document(goalId)
            .delete()
            .await()
    }
}
