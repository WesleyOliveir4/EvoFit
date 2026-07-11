package com.example.evofit.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.evofit.domain.model.MeasurementUnit
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "exercise_sets",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["workoutExerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["workoutExerciseId"])]
)
data class ExerciseSetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val workoutExerciseId: Long,
    val setNumber: Int,
    val reps: Int,
    val load: Double,
    val unit: MeasurementUnit = MeasurementUnit.WEIGHT,
    val time: Int? = null,
    val distance: Double? = null
)
