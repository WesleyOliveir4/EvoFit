package com.example.evofit.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.evofit.domain.model.WorkoutDone

@Entity(tableName = "workout_done_history")
data class WorkoutDoneHistoryEntity(
    @PrimaryKey val userId: String,
    val history: List<WorkoutDone>
)
