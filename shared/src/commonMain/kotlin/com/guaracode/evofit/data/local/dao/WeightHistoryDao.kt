package com.guaracode.evofit.data.local.dao

import androidx.room.*
import com.guaracode.evofit.data.local.entities.WeightUpdateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeightHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeightUpdate(update: WeightUpdateEntity): Long

    @Query("SELECT * FROM weight_history WHERE userId = :userId AND isDeleted = 0 ORDER BY timestamp DESC")
    fun getWeightHistory(userId: String): Flow<List<WeightUpdateEntity>>

    @Query("DELETE FROM weight_history WHERE id = :id")
    suspend fun deleteWeightUpdateById(id: String): Int

    @Query("DELETE FROM weight_history")
    suspend fun deleteAllWeightHistory()
}
