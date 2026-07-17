package com.example.evofit.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "workouts",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["userId"])]
)
data class WorkoutEntity(
    @PrimaryKey val workoutId: String,
    val userId: String,
    val name: String,
    val muscleGroupId: String,
    val date: String,
    val orderIndex: Int = 0,
    val updatedAt: Long
)
