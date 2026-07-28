package com.example.evofit.data.datasource

import android.util.Log
import com.example.evofit.data.local.entities.UserEntity
import com.example.evofit.data.local.entities.UserGoalEntity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

interface UserRemoteDataSource {
    suspend fun saveUser(user: UserEntity)
    suspend fun saveGoals(userId: String, goals: List<UserGoalEntity>)
    suspend fun deleteGoal(userId: String, goalId: String)
    suspend fun getUser(userId: String): UserEntity?
    suspend fun getGoals(userId: String): List<UserGoalEntity>
}

class UserRemoteDataSourceImpl(
    private val firestore: FirebaseFirestore
) : UserRemoteDataSource {

    companion object {
        private const val TAG = "EvoFit_Debug"
    }

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

    override suspend fun getUser(userId: String): UserEntity? {
        return try {
            val document = firestore.collection("users")
                .document(userId)
                .get()
                .await()
            
            if (document.exists()) {
                val data = document.data
                Log.d(TAG, "Dados do Firestore: $data")
                val user = document.toObject<UserEntity>()
                Log.d(TAG, "UserEntity mapeado: birthDate=${user?.birthDate}")
                user
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao buscar usuario: $userId", e)
            null
        }
    }

    override suspend fun getGoals(userId: String): List<UserGoalEntity> {
        return try {
            firestore.collection("users")
                .document(userId)
                .collection("goals")
                .get()
                .await()
                .toObjects(UserGoalEntity::class.java)
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao buscar metas: $userId", e)
            emptyList()
        }
    }
}
