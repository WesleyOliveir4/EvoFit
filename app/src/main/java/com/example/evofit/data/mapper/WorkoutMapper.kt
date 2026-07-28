package com.example.evofit.data.mapper

import com.example.evofit.data.local.entities.ActiveSessionEntity
import com.example.evofit.data.local.entities.ActiveSessionSetEntity
import com.example.evofit.data.local.entities.ExerciseSetEntity
import com.example.evofit.data.local.entities.WorkoutDoneHistoryEntity
import com.example.evofit.data.local.entities.WorkoutEntity
import com.example.evofit.data.local.entities.WorkoutExerciseEntity
import com.example.evofit.data.local.relations.FullWorkout
import com.example.evofit.data.local.relations.WorkoutExerciseWithSets
import com.example.evofit.domain.model.CompletedSet
import com.example.evofit.domain.model.ExerciseSet
import com.example.evofit.domain.model.MuscleGroup
import com.example.evofit.domain.model.Workout
import com.example.evofit.domain.model.WorkoutExercise
import com.example.evofit.domain.model.WorkoutSession

fun FullWorkout.toDomain(
    muscleGroup: MuscleGroup? = null,
    exerciseNameResolver: (String) -> String = { "" }
): Workout {
    return Workout(
        id = workout.workoutId,
        userId = workout.userId,
        name = workout.name,
        muscleGroupId = workout.muscleGroupId,
        muscleGroup = muscleGroup,
        date = workout.date,
        exercises = exercises
            .sortedBy { it.workoutExercise.orderIndex }
            .map { it.toDomain(exerciseNameResolver) },
        orderIndex = workout.orderIndex
    )
}

fun WorkoutExerciseWithSets.toDomain(exerciseNameResolver: (String) -> String = { "" }): WorkoutExercise {
    val exerciseName = exerciseNameResolver(workoutExercise.exerciseId)
    return WorkoutExercise(
        id = workoutExercise.id,
        exerciseId = workoutExercise.exerciseId,
        sets = sets.sortedBy { it.setNumber }.map { it.toDomain(exerciseName) },
        orderIndex = workoutExercise.orderIndex
    )
}

fun ExerciseSetEntity.toDomain(exerciseName: String = ""): ExerciseSet {
    return ExerciseSet(
        id = id,
        exerciseName = exerciseName,
        workoutExerciseId = workoutExerciseId,
        setNumber = setNumber,
        reps = reps,
        load = load,
        unit = unit,
        time = time,
        distance = distance
    )
}

fun Workout.toEntity(): WorkoutEntity {
    return WorkoutEntity(
        workoutId = id,
        userId = userId,
        name = name,
        muscleGroupId = muscleGroupId,
        date = date,
        orderIndex = orderIndex,
        updatedAt = System.currentTimeMillis() // Adicionado updatedAt
    )
}

fun WorkoutExercise.toEntity(workoutId: String): WorkoutExerciseEntity {
    return WorkoutExerciseEntity(
        id = id,
        workoutId = workoutId,
        exerciseId = exerciseId,
        orderIndex = orderIndex
    )
}

fun ExerciseSet.toEntity(workoutExerciseId: String): ExerciseSetEntity {
    return ExerciseSetEntity(
        id = id,
        workoutExerciseId = workoutExerciseId,
        setNumber = setNumber,
        reps = reps,
        load = load,
        unit = unit,
        time = time,
        distance = distance
    )
}

fun ActiveSessionEntity.toDomain(sets: List<ActiveSessionSetEntity>): WorkoutSession {
    return WorkoutSession(
        workoutId = workoutId,
        startTime = startTime,
        completedSets = sets.map { it.toDomain() }
    )
}

fun ActiveSessionSetEntity.toDomain(): CompletedSet {
    return CompletedSet(
        workoutExerciseId = workoutExerciseId,
        setNumber = setNumber
    )
}

fun CompletedSet.toEntity(workoutId: String): ActiveSessionSetEntity {
    return ActiveSessionSetEntity(
        workoutId = workoutId,
        workoutExerciseId = workoutExerciseId,
        setNumber = setNumber
    )
}
