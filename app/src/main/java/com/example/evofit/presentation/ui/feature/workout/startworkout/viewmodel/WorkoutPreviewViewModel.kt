package com.example.evofit.presentation.ui.feature.workout.startworkout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.domain.repository.WorkoutSessionRepository
import com.example.evofit.domain.usecase.GetExerciseDataUseCase
import com.example.evofit.domain.usecase.GetWorkoutByIdUseCase
import com.example.evofit.presentation.model.ExercisePreviewItem
import com.example.evofit.presentation.model.WorkoutDetailPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class WorkoutPreviewUiState(
    val preview: WorkoutDetailPreview? = null,
    val hasActiveSessionConflict: Boolean = false
)
// TODO
class WorkoutPreviewViewModel(
    private val workoutId: Int,
    private val getWorkoutByIdUseCase: GetWorkoutByIdUseCase,
    private val getExerciseDataUseCase: GetExerciseDataUseCase,
    private val sessionRepository: WorkoutSessionRepository
) : ViewModel() {

    private val previewFlow = getWorkoutByIdUseCase(workoutId.toLong())
        .map { workout ->
            workout?.let { workoutSelected ->
                val exerciseIds = workoutSelected.exercises.map { it.exerciseId }
                val exerciseDataMap = getExerciseDataUseCase.getExercisesByIds(exerciseIds)
                    .associateBy { it.id }

                val exercises = workoutSelected.exercises.map { workoutExercise ->
                    val exercise = exerciseDataMap[workoutExercise.exerciseId]
                    val unit = exercise?.unit ?: MeasurementUnit.WEIGHT

                    val setsCount = workoutExercise.sets.size

                    val bestSet = when (unit) {
                        MeasurementUnit.WEIGHT -> workoutExercise.sets.maxByOrNull { it.load }
                        MeasurementUnit.DISTANCE -> workoutExercise.sets.maxByOrNull { it.distance ?: 0.0 }
                        MeasurementUnit.TIME -> workoutExercise.sets.maxByOrNull { it.time ?: 0 }
                        MeasurementUnit.REPS -> workoutExercise.sets.maxByOrNull { it.reps }
                    }

                    ExercisePreviewItem(
                        workoutExerciseId = workoutExercise.id,
                        name = exercise?.name ?: "",
                        setsCount = setsCount,
                        weight = bestSet?.load ?: 0.0,
                        reps = bestSet?.reps ?: 0,
                        unit = unit,
                        time = bestSet?.time,
                        distance = bestSet?.distance
                    )
                }

                WorkoutDetailPreview(
                    title = workoutSelected.name.ifEmpty { workoutSelected.muscleGroupId },
                    totalExercises = workoutSelected.exercises.size,
                    totalSets = workoutSelected.exercises.sumOf { ex -> ex.sets.size },
                    exercises = exercises
                )
            }
        }

    private val _hasActiveSessionConflict = MutableStateFlow(false)

    val uiState: StateFlow<WorkoutPreviewUiState> = combine(
        previewFlow,
        _hasActiveSessionConflict
    ) { preview, conflict ->
        WorkoutPreviewUiState(preview = preview, hasActiveSessionConflict = conflict)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = WorkoutPreviewUiState()
    )

    fun onStartWorkoutClicked(onProceed: () -> Unit) {
        val hasActiveSession = sessionRepository.getActiveSession() != null
        if (hasActiveSession) {
            _hasActiveSessionConflict.value = true
        } else {
            onProceed()
        }
    }

    fun onConfirmDiscardActiveSession(onProceed: () -> Unit) {
        sessionRepository.clearSession()
        _hasActiveSessionConflict.value = false
        onProceed()
    }

    fun onDismissActiveSessionDialog() {
        _hasActiveSessionConflict.value = false
    }
}
