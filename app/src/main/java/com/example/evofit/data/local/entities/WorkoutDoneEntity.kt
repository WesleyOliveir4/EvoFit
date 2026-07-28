package com.example.evofit.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.evofit.domain.model.WorkoutExercise

@Entity(tableName = "workout_done")
data class WorkoutDoneEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val name: String,
    val muscleGroupId: String,
    val date: String,
    val exercises: List<WorkoutExercise>,
    val time: String,
    val createdAt: Long
)
