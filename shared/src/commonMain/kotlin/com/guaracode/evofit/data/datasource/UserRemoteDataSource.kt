package com.guaracode.evofit.data.datasource

import com.guaracode.evofit.core.monitoring.CrashReporter
import com.guaracode.evofit.data.local.entities.UserEntity
import com.guaracode.evofit.data.local.entities.UserGoalEntity
import com.guaracode.evofit.data.local.entities.WeightUpdateEntity
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.FieldValue
import dev.gitlive.firebase.firestore.Direction

interface UserRemoteDataSource {
    suspend fun saveUser(user: UserEntity)
    suspend fun saveGoals(userId: String, goals: List<UserGoalEntity>)
    suspend fun deleteGoal(userId: String, goalId: String)
    suspend fun getUser(userId: String): UserEntity?
    suspend fun getGoals(userId: String, sinceTimestamp: Long = 0L): List<UserGoalEntity>
    
    // Weight History
    suspend fun saveWeightUpdate(userId: String, update: WeightUpdateEntity)
    suspend fun deleteWeightUpdate(userId: String, updateId: String)
    suspend fun getWeightHistory(userId: String, sinceTimestamp: Long = 0L): List<WeightUpdateEntity>
}

class UserRemoteDataSourceImpl(
    private val firestore: FirebaseFirestore,
    private val crashReporter: CrashReporter
) : UserRemoteDataSource {

    override suspend fun saveUser(user: UserEntity) {
        val userMap = mutableMapOf<String, Any?>(
            "id" to user.id,
            "name" to user.name,
            "birthDate" to user.birthDate,
            "weight" to user.weight,
            "height" to user.height,
            "onboardingCompleted" to user.onboardingCompleted,
            "updatedAt" to FieldValue.serverTimestamp
        )
        firestore.collection("users")
            .document(user.id)
            .set(userMap)
    }

    override suspend fun saveGoals(userId: String, goals: List<UserGoalEntity>) {
        val batch = firestore.batch()
        goals.forEach { goal ->
            val docRef = firestore.collection("users")
                .document(userId)
                .collection("goals")
                .document(goal.id)
            
            val goalMap = mutableMapOf<String, Any?>(
                "id" to goal.id,
                "userId" to goal.userId,
                "type" to goal.type,
                "updatedAt" to FieldValue.serverTimestamp
            )
            goal.exerciseName?.let { goalMap["exerciseName"] = it }
            goal.value?.let { goalMap["value"] = it }
            goal.unit?.let { goalMap["unit"] = it }
            goal.cardioType?.let { goalMap["cardioType"] = it }
            goal.distance?.let { goalMap["distance"] = it }
            goal.time?.let { goalMap["time"] = it }

            batch.set(docRef, goalMap)
        }
        batch.commit()
    }

    override suspend fun deleteGoal(userId: String, goalId: String) {
        firestore.collection("users")
            .document(userId)
            .collection("goals")
            .document(goalId)
            .delete()
    }

    override suspend fun getUser(userId: String): UserEntity? {
        return try {
            val document = firestore.collection("users")
                .document(userId)
                .get()
            
            if (document.exists) {
                val data = document.data<Map<String, Any?>>() ?: return null

                val rawOnboard = data["onboardingCompleted"]
                val isOnboardComplete = when (rawOnboard) {
                    is Boolean -> rawOnboard
                    is Number -> rawOnboard.toInt() != 0
                    is String -> rawOnboard.trim().lowercase() == "true"
                    else -> false
                }

                val user = UserEntity(
                    id = data["id"] as? String ?: userId,
                    name = data["name"] as? String ?: "",
                    birthDate = (data["birthDate"] as? String) ?: (data["age"] as? String) ?: "",
                    weight = data["weight"] as? String ?: "",
                    height = data["height"] as? String ?: "",
                    onboardingCompleted = isOnboardComplete
                )

                val updatedAtRaw = data["updatedAt"]
                val updatedAt = when (updatedAtRaw) {
                    is Long -> updatedAtRaw
                    is Number -> updatedAtRaw.toLong()
                    else -> 0L
                }
                user.updatedAt = updatedAt
                
                user
            } else {
                null
            }
        } catch (e: Exception) {
            crashReporter.recordException(e)
            null
        }
    }

    override suspend fun getGoals(userId: String, sinceTimestamp: Long): List<UserGoalEntity> {
        return try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("goals")
                .get()

            snapshot.documents.mapNotNull { doc ->
                val data = doc.data<Map<String, Any?>>() ?: return@mapNotNull null
                UserGoalEntity(
                    id = data["id"] as? String ?: doc.id,
                    userId = data["userId"] as? String ?: userId,
                    type = data["type"] as? String ?: "",
                    exerciseName = data["exerciseName"] as? String,
                    value = data["value"] as? String,
                    unit = data["unit"] as? String ?: "WEIGHT",
                    cardioType = data["cardioType"] as? String,
                    distance = data["distance"] as? String,
                    time = data["time"] as? String,
                    isDeleted = data["isDeleted"] as? Boolean ?: false
                )
            }
        } catch (e: Exception) {
            crashReporter.recordException(e)
            emptyList()
        }
    }

    override suspend fun saveWeightUpdate(userId: String, update: WeightUpdateEntity) {
        val updateMap = mutableMapOf<String, Any?>(
            "id" to update.id,
            "userId" to update.userId,
            "weight" to update.weight,
            "date" to update.date,
            "timestamp" to FieldValue.serverTimestamp
        )
        firestore.collection("users")
            .document(userId)
            .collection("weight_history")
            .document(update.id)
            .set(updateMap)
    }

    override suspend fun deleteWeightUpdate(userId: String, updateId: String) {
        firestore.collection("users")
            .document(userId)
            .collection("weight_history")
            .document(updateId)
            .delete()
    }

    override suspend fun getWeightHistory(userId: String, sinceTimestamp: Long): List<WeightUpdateEntity> {
        return try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("weight_history")
                .orderBy("timestamp", Direction.DESCENDING)
                .get()

            snapshot.documents.mapNotNull { doc ->
                val data = doc.data<Map<String, Any?>>() ?: return@mapNotNull null
                WeightUpdateEntity(
                    id = data["id"] as? String ?: doc.id,
                    userId = data["userId"] as? String ?: userId,
                    weight = data["weight"] as? String ?: "",
                    date = data["date"] as? String ?: "",
                    isDeleted = data["isDeleted"] as? Boolean ?: false
                )
            }
        } catch (e: Exception) {
            crashReporter.recordException(e)
            emptyList()
        }
    }
}
