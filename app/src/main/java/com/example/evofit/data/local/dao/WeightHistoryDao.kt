package com.example.evofit.data.local.dao

import androidx.room.*
import com.example.evofit.data.local.entities.WeightUpdateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeightHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeightUpdate(update: WeightUpdateEntity): Long

    @Query("SELECT * FROM weight_history WHERE userId = :userId AND isDeleted = 0 ORDER BY timestamp DESC")
    fun getWeightHistory(userId: String): Flow<List<WeightUpdateEntity>>

    @Query("SELECT * FROM weight_history WHERE syncStatus = 'PENDING'")
    suspend fun getPendingWeightUpdates(): List<WeightUpdateEntity>

    @Query("UPDATE weight_history SET isDeleted = 1, syncStatus = 'PENDING', timestamp = :timestamp WHERE id = :updateId")
    suspend fun softDeleteWeightUpdate(updateId: String, timestamp: Long)

    @Query("UPDATE weight_history SET syncStatus = 'SYNCED', timestamp = :timestamp WHERE id = :id")
    suspend fun markWeightUpdateSynced(id: String, timestamp: Long)

    @Query("DELETE FROM weight_history WHERE id = :id")
    suspend fun deleteWeightUpdateById(id: String): Int

    @Query("DELETE FROM weight_history WHERE userId = :userId")
    suspend fun deleteWeightHistoryForUser(userId: String): Int

    @Query("DELETE FROM weight_history")
    suspend fun deleteAllWeightHistory()
}
