package com.example.evofit.data.mapper

import com.example.evofit.data.local.entities.ExerciseSetEntity
import com.example.evofit.data.local.entities.WorkoutEntity
import com.example.evofit.data.local.entities.WorkoutExerciseEntity
import com.example.evofit.data.local.relations.FullWorkout
import com.example.evofit.data.local.relations.WorkoutExerciseWithSets
import com.example.evofit.domain.model.ExerciseSet
import com.example.evofit.domain.model.Workout
import com.example.evofit.domain.model.WorkoutExercise

import com.example.evofit.data.model.MuscleGroupModel

fun FullWorkout.toDomain(muscleGroup: MuscleGroupModel? = null): Workout {
    return Workout(
        id = workout.workoutId,
        userId = workout.userId,
        name = workout.name,
        muscleGroupId = workout.muscleGroupId,
        muscleGroup = muscleGroup,
        date = workout.date,
        exercises = exercises.map { it.toDomain() }
    )
}

fun WorkoutExerciseWithSets.toDomain(): WorkoutExercise {
    return WorkoutExercise(
        id = workoutExercise.id,
        exerciseId = workoutExercise.exerciseId,
        sets = sets.map { it.toDomain() }
    )
}

fun ExerciseSetEntity.toDomain(): ExerciseSet {
    return ExerciseSet(
        id = id,
        setNumber = setNumber,
        reps = reps,
        load = load
    )
}

fun Workout.toEntity(): WorkoutEntity {
    return WorkoutEntity(
        workoutId = id,
        userId = userId,
        name = name,
        muscleGroupId = muscleGroupId,
        date = date
    )
}

fun WorkoutExercise.toEntity(workoutId: Long): WorkoutExerciseEntity {
    return WorkoutExerciseEntity(
        id = id,
        workoutId = workoutId,
        exerciseId = exerciseId
    )
}

fun ExerciseSet.toEntity(workoutExerciseId: Long): ExerciseSetEntity {
    return ExerciseSetEntity(
        id = id,
        workoutExerciseId = workoutExerciseId,
        setNumber = setNumber,
        reps = reps,
        load = load
    )
}
