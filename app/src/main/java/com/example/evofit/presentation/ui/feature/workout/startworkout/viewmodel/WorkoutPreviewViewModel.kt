package com.example.evofit.presentation.ui.feature.workout.startworkout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.model.MeasurementUnit
import com.example.evofit.domain.usecase.ClearWorkoutSessionUseCase
import com.example.evofit.domain.usecase.DeleteWorkoutUseCase
import com.example.evofit.domain.usecase.GetActiveWorkoutSessionUseCase
import com.example.evofit.domain.usecase.GetExercisesByIdsUseCase
import com.example.evofit.domain.usecase.GetMuscleGroupsUseCase
import com.example.evofit.domain.usecase.GetWorkoutByIdUseCase
import com.example.evofit.domain.usecase.UpdateWorkoutUseCase
import com.example.evofit.presentation.model.ExercisePreviewItem
import com.example.evofit.presentation.model.WorkoutDetailPreview
import com.example.evofit.presentation.ui.feature.workout.startworkout.state.WorkoutPreviewUiState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class WorkoutPreviewViewModel(
    private val workoutId: String,
    private val getWorkoutByIdUseCase: GetWorkoutByIdUseCase,
    private val getExercisesByIdsUseCase: GetExercisesByIdsUseCase,
    private val deleteWorkoutUseCase: DeleteWorkoutUseCase,
    private val getActiveWorkoutSessionUseCase: GetActiveWorkoutSessionUseCase,
    private val clearWorkoutSessionUseCase: ClearWorkoutSessionUseCase,
    private val getMuscleGroupsUseCase: GetMuscleGroupsUseCase,
    private val updateWorkoutUseCase: UpdateWorkoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkoutPreviewUiState())
    val uiState: StateFlow<WorkoutPreviewUiState> = _uiState.asStateFlow()

    private val _updateOrderFlow = MutableSharedFlow<List<String>>()

    init {
        loadWorkoutPreview()
        viewModelScope.launch {
            _updateOrderFlow
                .debounce(1000L)
                .collect { newOrder ->
                    performUpdateMuscleGroupOrder(newOrder)
                }
        }
    }

    private fun loadWorkoutPreview() {
        viewModelScope.launch {
            getWorkoutByIdUseCase(workoutId)
                .map { workout ->
                    workout?.let { workoutSelected ->
                        val allExercises = workoutSelected.exercisesByGroup.flatMap { it.exercises }
                        val exerciseIds = allExercises.map { it.exerciseId }
                        val exerciseDataMap = getExercisesByIdsUseCase(exerciseIds)
                            .associateBy { it.id }

                        val muscleGroups = getMuscleGroupsUseCase()
                        val muscleGroupsMap = muscleGroups.associateBy { it.id }

                        val exercises = allExercises.map { workoutExercise ->
                            val exercise = exerciseDataMap[workoutExercise.exerciseId]
                            val unit = exercise?.unit ?: MeasurementUnit.WEIGHT
                            val mGroupId = exercise?.muscleGroupId ?: ""
                            val mGroupName = muscleGroupsMap[mGroupId]?.name ?: ""

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
                                muscleGroupId = mGroupId,
                                muscleGroupName = mGroupName,
                                setsCount = setsCount,
                                weight = bestSet?.load ?: 0.0,
                                reps = bestSet?.reps ?: 0,
                                unit = unit,
                                time = bestSet?.time,
                                distance = bestSet?.distance
                            )
                        }

                        val firstMuscleGroup = workoutSelected.exercisesByGroup.firstOrNull()?.muscleGroup
                        
                        WorkoutDetailPreview(
                            title = workoutSelected.name,
                            muscleGroupId = firstMuscleGroup?.id ?: "",
                            totalExercises = allExercises.size,
                            totalSets = allExercises.sumOf { ex -> ex.sets.size },
                            exercises = exercises,
                            groupedExercises = exercises.groupBy { it.muscleGroupName }
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
            if (activeSession != null && activeSession.workout.id == workoutId) {
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
            deleteWorkoutUseCase(workoutId)
            _uiState.update { it.copy(showDeleteDialog = false, isDeleted = true) }
        }
    }

    fun onEditClicked(onProceed: () -> Unit) {
        viewModelScope.launch {
            val activeSession = getActiveWorkoutSessionUseCase().first()
            if (activeSession != null && activeSession.workout.id == workoutId) {
                _uiState.update { it.copy(showEditBlockedDialog = true) }
            } else {
                onProceed()
            }
        }
    }

    fun onDismissEditBlockedDialog() {
        _uiState.update { it.copy(showEditBlockedDialog = false) }
    }

    fun updateMuscleGroupOrder(newOrder: List<String>) {
        viewModelScope.launch {
            _updateOrderFlow.emit(newOrder)
        }
    }

    private suspend fun performUpdateMuscleGroupOrder(newOrder: List<String>) {
        val currentWorkout = getWorkoutByIdUseCase(workoutId).first() ?: return
        
        val updatedGroups = currentWorkout.exercisesByGroup.map { group ->
            val newIndex = newOrder.indexOf(group.muscleGroup?.name)
            if (newIndex != -1) {
                group.copy(orderIndex = newIndex)
            } else {
                group
            }
        }.sortedBy { it.orderIndex }

        if (updatedGroups != currentWorkout.exercisesByGroup) {
            updateWorkoutUseCase(currentWorkout.copy(exercisesByGroup = updatedGroups))
        }
    }
}
