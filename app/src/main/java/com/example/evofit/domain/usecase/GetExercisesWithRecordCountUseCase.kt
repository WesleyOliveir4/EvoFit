package com.example.evofit.domain.usecase

import com.example.evofit.domain.model.Exercise
import com.example.evofit.domain.model.WorkoutDone

data class ExerciseWithRecords(
    val exercise: Exercise,
    val recordsCount: Int
)

interface GetExercisesWithRecordCountUseCase {
    operator fun invoke(muscleGroupId: String, history: List<WorkoutDone>): List<ExerciseWithRecords>
}

class GetExercisesWithRecordCountUseCaseImpl(
    private val getExercisesByGroupUseCase: GetExercisesByGroupUseCase
) : GetExercisesWithRecordCountUseCase {
    override fun invoke(muscleGroupId: String, history: List<WorkoutDone>): List<ExerciseWithRecords> {
        val exercises = getExercisesByGroupUseCase(muscleGroupId)

        return exercises.map { exercise ->
            val count = history.flatMap { it.exercises }
                .count { it.exerciseId == exercise.id }
            ExerciseWithRecords(exercise, count)
        }.filter { it.recordsCount > 0 }
    }
}
