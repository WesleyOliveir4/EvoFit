package com.guaracode.evofit.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.guaracode.evofit.data.local.entities.ExerciseSetEntity
import com.guaracode.evofit.data.local.entities.WorkoutExerciseEntity

data class WorkoutExerciseWithSets(
    @Embedded val workoutExercise: WorkoutExerciseEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "workoutExerciseId"
    )
    val sets: List<ExerciseSetEntity>
)
