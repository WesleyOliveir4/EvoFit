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
    primaryKeys = ["workoutExerciseId", "setNumber"],
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
    val id: String = "",
    val workoutExerciseId: String = "",
    val setNumber: Int = 0,
    val reps: Int = 0,
    val load: Double = 0.0,
    val unit: MeasurementUnit = MeasurementUnit.WEIGHT,
    val time: Int? = null,
    val distance: Double? = null
)
