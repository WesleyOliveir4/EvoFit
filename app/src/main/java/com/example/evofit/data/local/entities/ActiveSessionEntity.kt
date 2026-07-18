package com.example.evofit.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "active_session")
data class ActiveSessionEntity(
    @PrimaryKey val workoutId: String,
    val startTime: Long
)

@Entity(
    tableName = "active_session_sets",
    foreignKeys = [
        ForeignKey(
            entity = ActiveSessionEntity::class,
            parentColumns = ["workoutId"],
            childColumns = ["workoutId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ActiveSessionSetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val workoutId: String,
    val workoutExerciseId: String,
    val setNumber: Int
)
