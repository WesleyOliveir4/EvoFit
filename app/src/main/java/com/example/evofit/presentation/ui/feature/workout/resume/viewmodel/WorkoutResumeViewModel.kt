package com.example.evofit.presentation.ui.feature.workout.resume.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.core.common.AppConstants
import com.example.evofit.domain.usecase.GetWorkoutDoneByIdUseCase
import com.example.evofit.domain.usecase.GetWorkoutByIdUseCase
import com.example.evofit.presentation.ui.feature.workout.resume.state.WorkoutResumeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WorkoutResumeViewModel(
    private val workoutId: Long? = null,
    private val workoutDoneId: Long? = null,
    private val editWorkoutId: Long? = null,
    private val getWorkoutByIdUseCase: GetWorkoutByIdUseCase,
    private val getWorkoutDoneByIdUseCase: GetWorkoutDoneByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkoutResumeUiState())
    val uiState: StateFlow<WorkoutResumeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            val idToLoad = workoutId?.takeIf { it != AppConstants.INVALID_ID } ?: editWorkoutId?.takeIf { it != AppConstants.INVALID_ID }

            if (workoutDoneId != null && workoutDoneId != AppConstants.INVALID_ID) {
                val workoutDone = getWorkoutDoneByIdUseCase(workoutDoneId)
                _uiState.update { state ->
                    workoutDone?.let { workoutDone ->
                        val doneSetsCount = workoutDone.exercises.sumOf { it.sets.size }
                        val totalSetsPlanned = workoutDone.exercises.sumOf { it.totalSets }
                        state.copy(
                            workoutName = workoutDone.name,
                            totalExercises = workoutDone.exercises.size,
                            totalSets = totalSetsPlanned,
                            completedSets = doneSetsCount,
                            duration = workoutDone.time,
                            formattedDate = workoutDone.date,
                            isLoading = false,
                            isWorkoutDone = true
                        )
                    } ?: state.copy(isLoading = false)
                }
            } else if (idToLoad != null) {
                getWorkoutByIdUseCase(idToLoad).collect { workout ->
                    _uiState.update { state ->
                        workout?.let { workout ->
                            state.copy(
                                workoutName = workout.name,
                                totalExercises = workout.exercises.size,
                                totalSets = workout.exercises.sumOf { ex -> ex.sets.size },
                                formattedDate = workout.date,
                                isLoading = false,
                                isWorkoutDone = false
                            )
                        } ?: state.copy(isLoading = false)
                    }
                }
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
    
}
