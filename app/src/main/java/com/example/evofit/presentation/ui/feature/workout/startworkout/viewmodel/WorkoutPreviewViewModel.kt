package com.example.evofit.presentation.ui.feature.workout.startworkout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.usecase.GetExerciseDataUseCase
import com.example.evofit.domain.usecase.GetWorkoutByIdUseCase
import com.example.evofit.presentation.ui.feature.workout.components.ExercisePreviewItem
import com.example.evofit.presentation.ui.feature.workout.components.WorkoutDetailPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class WorkoutPreviewViewModel(
    private val workoutId: Int,
    private val getWorkoutByIdUseCase: GetWorkoutByIdUseCase,
    private val getExerciseDataUseCase: GetExerciseDataUseCase
) : ViewModel() {

    val uiState: StateFlow<WorkoutDetailPreview?> = getWorkoutByIdUseCase(workoutId.toLong())
        .map { workout ->
            workout?.let {
                val exercises = it.exercises.map { workoutExercise ->
                    val muscleGroup = getExerciseDataUseCase.getMuscleGroups()
                        .find { group -> group.id == it.muscleGroupId }

                    val exercise = getExerciseDataUseCase.getExercisesByGroup(it.muscleGroupId)
                        .find { ex -> ex.id == workoutExercise.exerciseId }

                    val setsCount = workoutExercise.sets.size
                    val weight = workoutExercise.sets.firstOrNull()?.load ?: 0.0
                    val reps = workoutExercise.sets.firstOrNull()?.reps ?: 0

                    ExercisePreviewItem(
                        name = exercise?.name ?: "",
                        setsCount = setsCount,
                        weight = weight,
                        reps = reps
                    )
                }

                WorkoutDetailPreview(
                    title = it.name.ifEmpty { it.muscleGroupId },
                    totalExercises = it.exercises.size,
                    totalSets = it.exercises.sumOf { ex -> ex.sets.size },
                    exercises = exercises
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = null
        )
}