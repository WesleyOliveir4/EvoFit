package com.guaracode.evofit.data.datasource

import com.guaracode.evofit.core.monitoring.CrashReporter
import com.guaracode.evofit.data.local.entities.UserEntity
import com.guaracode.evofit.data.local.entities.UserGoalEntity
import com.guaracode.evofit.data.local.entities.WeightUpdateEntity
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.FieldValue
import dev.gitlive.firebase.firestore.FirebaseFirestore

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
            "isOnboardingCompleted" to user.onboardingCompleted,
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
                val rawOnboard1: Boolean? = try { document.get("onboardingCompleted") } catch (e: Exception) { null }
                val rawOnboard2: Boolean? = try { document.get("isOnboardingCompleted") } catch (e: Exception) { null }
                val isOnboardComplete = rawOnboard1 == true || rawOnboard2 == true

                val name: String = try { document.get("name") } catch (e: Exception) { "" }
                val birthDate: String = try { document.get("birthDate") } catch (e: Exception) { try { document.get("age") } catch (e: Exception) { "" } }
                val weight: String = try { document.get("weight") } catch (e: Exception) { "" }
                val height: String = try { document.get("height") } catch (e: Exception) { "" }

                val user = UserEntity(
                    id = userId,
                    name = name,
                    birthDate = birthDate,
                    weight = weight,
                    height = height,
                    onboardingCompleted = isOnboardComplete
                )

                val updatedAtRaw: Long? = try { document.get("updatedAt") } catch (e: Exception) { null }
                user.updatedAt = updatedAtRaw ?: 0L
                
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
                if (!doc.exists) return@mapNotNull null
                UserGoalEntity(
                    id = doc.id,
                    userId = userId,
                    type = try { doc.get("type") } catch (e: Exception) { "" },
                    exerciseName = try { doc.get("exerciseName") } catch (e: Exception) { null },
                    value = try { doc.get("value") } catch (e: Exception) { null },
                    unit = try { doc.get("unit") } catch (e: Exception) { "WEIGHT" },
                    cardioType = try { doc.get("cardioType") } catch (e: Exception) { null },
                    distance = try { doc.get("distance") } catch (e: Exception) { null },
                    time = try { doc.get("time") } catch (e: Exception) { null },
                    isDeleted = try { doc.get("isDeleted") } catch (e: Exception) { false }
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
                if (!doc.exists) return@mapNotNull null
                WeightUpdateEntity(
                    id = doc.id,
                    userId = userId,
                    weight = try { doc.get("weight") } catch (e: Exception) { "" },
                    date = try { doc.get("date") } catch (e: Exception) { "" },
                    isDeleted = try { doc.get("isDeleted") } catch (e: Exception) { false }
                )
            }
        } catch (e: Exception) {
            crashReporter.recordException(e)
            emptyList()
        }
    }
}
