package com.example.evofit.data.mapper

import com.example.evofit.data.local.entities.ActiveSessionEntity
import com.example.evofit.data.local.entities.ActiveSessionSetEntity
import com.example.evofit.data.local.entities.ExerciseSetEntity
import com.example.evofit.data.local.entities.WorkoutDoneEntity
import com.example.evofit.data.local.entities.WorkoutDoneHistoryEntity
import com.example.evofit.data.local.entities.WorkoutEntity
import com.example.evofit.data.local.entities.WorkoutExerciseEntity
import com.example.evofit.data.local.relations.FullWorkout
import com.example.evofit.data.local.relations.WorkoutExerciseWithSets
import com.example.evofit.domain.model.CompletedSet
import com.example.evofit.domain.model.ExerciseSet
import com.example.evofit.domain.model.MuscleGroup
import com.example.evofit.domain.model.Workout
import com.example.evofit.domain.model.WorkoutDone
import com.example.evofit.domain.model.WorkoutExercise
import com.example.evofit.domain.model.WorkoutSession

import com.example.evofit.domain.model.WorkoutGroup

fun FullWorkout.toDomain(
    muscleGroups: List<MuscleGroup> = emptyList(),
    exerciseNameResolver: (String) -> String = { "" }
): Workout {
    val muscleGroupsMap = muscleGroups.associateBy { it.id }
    
    val groupedExercises = exercises
        .groupBy { it.workoutExercise.muscleGroupId }
        .map { (muscleGroupId, exercisesWithSets) ->
            WorkoutGroup(
                muscleGroupId = muscleGroupId,
                muscleGroup = muscleGroupsMap[muscleGroupId],
                orderIndex = exercisesWithSets.minOfOrNull { it.workoutExercise.orderIndex } ?: 0,
                exercises = exercisesWithSets
                    .sortedBy { it.workoutExercise.orderIndex }
                    .map { it.toDomain(exerciseNameResolver) }
            )
        }
        .sortedBy { it.orderIndex }

    return Workout(
        id = workout.workoutId,
        userId = workout.userId,
        name = workout.name,
        date = workout.date,
        exercisesByGroup = groupedExercises,
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
        date = date,
        orderIndex = orderIndex,
        updatedAt = System.currentTimeMillis()
    )
}

fun WorkoutExercise.toEntity(workoutId: String, muscleGroupId: String): WorkoutExerciseEntity {
    return WorkoutExerciseEntity(
        id = id,
        workoutId = workoutId,
        exerciseId = exerciseId,
        muscleGroupId = muscleGroupId,
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

fun WorkoutDone.toEntity(): WorkoutDoneEntity {
    return WorkoutDoneEntity(
        id = id,
        userId = userId,
        name = name,
        date = date,
        exercisesByGroup = exercisesByGroup,
        time = time,
        createdAt = createdAt
    )
}

fun WorkoutDoneEntity.toDomain(): WorkoutDone {
    return WorkoutDone(
        id = id,
        userId = userId,
        name = name,
        date = date,
        exercisesByGroup = exercisesByGroup,
        time = time,
        createdAt = createdAt
    )
}
