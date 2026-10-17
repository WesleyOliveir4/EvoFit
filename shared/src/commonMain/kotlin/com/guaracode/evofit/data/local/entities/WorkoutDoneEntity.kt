package com.guaracode.evofit.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.guaracode.evofit.domain.model.WorkoutGroup

@Entity(tableName = "workout_done")
data class WorkoutDoneEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val name: String,
    val date: String,
    val exercisesByGroup: List<WorkoutGroup>,
    val time: String,
    val createdAt: Long,
    var isDeleted: Boolean = false,
    var syncStatus: SyncStatus = SyncStatus.SYNCED
)
