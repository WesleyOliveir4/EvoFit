package com.example.evofit.presentation.ui.feature.workout.startworkout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.domain.usecase.ClearWorkoutSessionUseCase
import com.example.evofit.domain.usecase.DeleteWorkoutUseCase
import com.example.evofit.domain.usecase.GetActiveWorkoutSessionUseCase
import com.example.evofit.domain.usecase.GetExerciseDataUseCase
import com.example.evofit.domain.usecase.GetWorkoutByIdUseCase
import com.example.evofit.presentation.model.ExercisePreviewItem
import com.example.evofit.presentation.model.WorkoutDetailPreview
import com.example.evofit.presentation.ui.feature.workout.startworkout.state.WorkoutPreviewUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WorkoutPreviewViewModel(
    private val workoutId: Int,
    private val getWorkoutByIdUseCase: GetWorkoutByIdUseCase,
    private val getExerciseDataUseCase: GetExerciseDataUseCase,
    private val deleteWorkoutUseCase: DeleteWorkoutUseCase,
    private val getActiveWorkoutSessionUseCase: GetActiveWorkoutSessionUseCase,
    private val clearWorkoutSessionUseCase: ClearWorkoutSessionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkoutPreviewUiState())
    val uiState: StateFlow<WorkoutPreviewUiState> = _uiState.asStateFlow()

    init {
        loadWorkoutPreview()
    }

    private fun loadWorkoutPreview() {
        viewModelScope.launch {
            getWorkoutByIdUseCase(workoutId.toLong())
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
                            muscleGroupId = workoutSelected.muscleGroupId,
                            totalExercises = workoutSelected.exercises.size,
                            totalSets = workoutSelected.exercises.sumOf { ex -> ex.sets.size },
                            exercises = exercises
                        )
                    }
                }
                .collect { preview ->
                    _uiState.update { it.copy(preview = preview) }
                }
        }
    }

    fun onStartWorkoutClicked(onProceed: () -> Unit) {
        viewModelScope.launch {
            val hasActiveSession = getActiveWorkoutSessionUseCase().first() != null
            if (hasActiveSession) {
                _uiState.update { it.copy(hasActiveSessionConflict = true) }
            } else {
                onProceed()
            }
        }
    }

    fun onConfirmDiscardActiveSession(onProceed: () -> Unit) {
        viewModelScope.launch {
            clearWorkoutSessionUseCase()
            _uiState.update { it.copy(hasActiveSessionConflict = false) }
            onProceed()
        }
    }

    fun onDismissActiveSessionDialog() {
        _uiState.update { it.copy(hasActiveSessionConflict = false) }
    }

    fun onDeleteClicked() {
        viewModelScope.launch {
            val activeSession = getActiveWorkoutSessionUseCase().first()
            if (activeSession != null && activeSession.workout.id == workoutId.toLong()) {
                _uiState.update { it.copy(showDeleteBlockedDialog = true) }
            } else {
                _uiState.update { it.copy(showDeleteDialog = true) }
            }
        }
    }

    fun onDismissDeleteDialog() {
        _uiState.update { it.copy(showDeleteDialog = false) }
    }

    fun onDismissDeleteBlockedDialog() {
        _uiState.update { it.copy(showDeleteBlockedDialog = false) }
    }

    fun onConfirmDelete() {
        viewModelScope.launch {
            deleteWorkoutUseCase(workoutId.toLong())
            _uiState.update { it.copy(showDeleteDialog = false, isDeleted = true) }
        }
    }

    fun onEditClicked(onProceed: () -> Unit) {
        viewModelScope.launch {
            val activeSession = getActiveWorkoutSessionUseCase().first()
            if (activeSession != null && activeSession.workout.id == workoutId.toLong()) {
                _uiState.update { it.copy(showEditBlockedDialog = true) }
            } else {
                onProceed()
            }
        }
    }

    fun onDismissEditBlockedDialog() {
        _uiState.update { it.copy(showEditBlockedDialog = false) }
    }
}
