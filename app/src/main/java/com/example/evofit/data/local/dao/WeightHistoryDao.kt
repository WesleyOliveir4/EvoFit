package com.example.evofit.data.local.dao

import androidx.room.*
import com.example.evofit.data.local.entities.WeightUpdateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeightHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeightUpdate(update: WeightUpdateEntity): Long

    @Query("SELECT * FROM weight_history WHERE userId = :userId ORDER BY timestamp DESC")
    fun getWeightHistory(userId: String): Flow<List<WeightUpdateEntity>>

    @Query("DELETE FROM weight_history WHERE userId = :userId")
    suspend fun deleteWeightHistoryForUser(userId: String): Int

    @Query("DELETE FROM weight_history")
    suspend fun deleteAllWeightHistory()
}
