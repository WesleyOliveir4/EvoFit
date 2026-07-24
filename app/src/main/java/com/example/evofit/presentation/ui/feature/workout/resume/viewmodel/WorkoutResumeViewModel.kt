package com.example.evofit.presentation.ui.feature.workout.resume.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.core.common.AppConstants
import com.example.evofit.domain.usecase.GetWorkoutDoneByIdUseCase
import com.example.evofit.domain.usecase.GetWorkoutByIdUseCase
import com.example.evofit.presentation.ui.feature.workout.resume.state.ResumeMode
import com.example.evofit.presentation.ui.feature.workout.resume.state.WorkoutResumeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WorkoutResumeViewModel(
    private val workoutId: String? = null,
    private val workoutDoneId: String? = null,
    private val editWorkoutId: String? = null,
    private val workoutNotFinishedId: String? = null,
    private val getWorkoutByIdUseCase: GetWorkoutByIdUseCase,
    private val getWorkoutDoneByIdUseCase: GetWorkoutDoneByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkoutResumeUiState())
    val uiState: StateFlow<WorkoutResumeUiState> = _uiState.asStateFlow()

    init {
        val initialMode = when {
            workoutDoneId != null && workoutDoneId != AppConstants.INVALID_ID -> ResumeMode.COMPLETED
            workoutNotFinishedId != null && workoutNotFinishedId != AppConstants.INVALID_ID -> ResumeMode.CANCELLED
            editWorkoutId != null && editWorkoutId != AppConstants.INVALID_ID -> ResumeMode.UPDATED
            else -> ResumeMode.CREATED
        }
        _uiState.update { it.copy(mode = initialMode) }
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (_uiState.value.mode) {
                ResumeMode.COMPLETED -> loadWorkoutDone(workoutDoneId!!)
                ResumeMode.CANCELLED -> loadWorkoutBase(workoutNotFinishedId!!)
                ResumeMode.UPDATED -> loadWorkoutBase(editWorkoutId!!)
                ResumeMode.CREATED -> loadWorkoutBase(workoutId ?: AppConstants.INVALID_ID)
            }
        }
    }

    private fun loadWorkoutDone(id: String) {
        viewModelScope.launch {
            getWorkoutDoneByIdUseCase(id).collect { workoutDone ->
                _uiState.update { state ->
                    workoutDone?.let { done ->
                        state.copy(
                            workoutName = done.name,
                            totalExercises = done.exercises.size,
                            totalSets = done.exercises.sumOf { it.totalSets },
                            completedSets = done.exercises.sumOf { it.sets.size },
                            duration = done.time,
                            formattedDate = done.date,
                            isLoading = false
                        )
                    } ?: state.copy(isLoading = false)
                }
            }
        }
    }

    private suspend fun loadWorkoutBase(id: String) {
        if (id == AppConstants.INVALID_ID) {
            _uiState.update { it.copy(isLoading = false) }
            return
        }

        getWorkoutByIdUseCase(id).collect { workout ->
            _uiState.update { state ->
                workout?.let { w ->
                    state.copy(
                        workoutName = w.name,
                        totalExercises = w.exercises.size,
                        totalSets = w.exercises.sumOf { it.sets.size },
                        formattedDate = w.date,
                        isLoading = false
                    )
                } ?: state.copy(isLoading = false)
            }
        }
    }
}
