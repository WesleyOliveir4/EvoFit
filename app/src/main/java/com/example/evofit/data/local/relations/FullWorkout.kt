package com.example.evofit.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.evofit.data.local.entities.WorkoutEntity
import com.example.evofit.data.local.entities.WorkoutExerciseEntity

data class FullWorkout(
    @Embedded val workout: WorkoutEntity,
    @Relation(
        entity = WorkoutExerciseEntity::class,
        parentColumn = "workoutId",
        entityColumn = "workoutId"
    )
    val exercises: List<WorkoutExerciseWithSets>
)
