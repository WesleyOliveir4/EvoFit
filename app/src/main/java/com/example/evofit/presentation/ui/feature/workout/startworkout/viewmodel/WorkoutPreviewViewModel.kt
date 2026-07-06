package com.example.evofit.presentation.ui.feature.workout.startworkout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.domain.repository.WorkoutSessionRepository
import com.example.evofit.domain.usecase.DeleteWorkoutUseCase
import com.example.evofit.domain.usecase.GetExerciseDataUseCase
import com.example.evofit.domain.usecase.GetWorkoutByIdUseCase
import com.example.evofit.presentation.model.ExercisePreviewItem
import com.example.evofit.presentation.model.WorkoutDetailPreview
import com.example.evofit.presentation.ui.feature.workout.startworkout.state.WorkoutPreviewUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class WorkoutPreviewViewModel(
    private val workoutId: Int,
    private val getWorkoutByIdUseCase: GetWorkoutByIdUseCase,
    private val getExerciseDataUseCase: GetExerciseDataUseCase,
    private val sessionRepository: WorkoutSessionRepository,
    private val deleteWorkoutUseCase: DeleteWorkoutUseCase
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
                    muscleGroupId = workoutSelected.muscleGroupId,
                    totalExercises = workoutSelected.exercises.size,
                    totalSets = workoutSelected.exercises.sumOf { ex -> ex.sets.size },
                    exercises = exercises
                )
            }
        }

    private val _hasActiveSessionConflict = MutableStateFlow(false)
    private val _showDeleteDialog = MutableStateFlow(false)
    private val _isDeleted = MutableStateFlow(false)
    private val _showEditBlockedDialog = MutableStateFlow(false)

    val uiState: StateFlow<WorkoutPreviewUiState> = combine(
        previewFlow,
        _hasActiveSessionConflict,
        _showDeleteDialog,
        _isDeleted,
        _showEditBlockedDialog
    ) { preview, conflict, showDelete, isDeleted, editBlocked ->
        WorkoutPreviewUiState(
            preview = preview,
            hasActiveSessionConflict = conflict,
            showDeleteDialog = showDelete,
            isDeleted = isDeleted,
            showEditBlockedDialog = editBlocked
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = WorkoutPreviewUiState()
    )

    fun onStartWorkoutClicked(onProceed: () -> Unit) {
        viewModelScope.launch {
            val hasActiveSession = sessionRepository.getActiveSession().first() != null
            if (hasActiveSession) {
                _hasActiveSessionConflict.value = true
            } else {
                onProceed()
            }
        }
    }

    fun onConfirmDiscardActiveSession(onProceed: () -> Unit) {
        viewModelScope.launch {
            sessionRepository.clearSession()
            _hasActiveSessionConflict.value = false
            onProceed()
        }
    }

    fun onDismissActiveSessionDialog() {
        _hasActiveSessionConflict.value = false
    }

    fun onDeleteClicked() {
        _showDeleteDialog.value = true
    }

    fun onDismissDeleteDialog() {
        _showDeleteDialog.value = false
    }

    fun onConfirmDelete() {
        viewModelScope.launch {
            deleteWorkoutUseCase(workoutId.toLong())
            _showDeleteDialog.value = false
            _isDeleted.value = true
        }
    }

    fun onEditClicked(onProceed: () -> Unit) {
        viewModelScope.launch {
            val activeSession = sessionRepository.getActiveSession().first()
            if (activeSession != null && activeSession.workoutId == workoutId.toLong()) {
                _showEditBlockedDialog.value = true
            } else {
                onProceed()
            }
        }
    }

    fun onDismissEditBlockedDialog() {
        _showEditBlockedDialog.value = false
    }
}
