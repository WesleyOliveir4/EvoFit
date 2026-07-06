package com.example.evofit.presentation.ui.feature.workout.resume.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            
            val idToLoad = workoutId?.takeIf { it != -1L } ?: editWorkoutId?.takeIf { it != -1L }

            if (workoutDoneId != null && workoutDoneId != -1L) {
                val workoutDone = getWorkoutDoneByIdUseCase(workoutDoneId)
                workoutDone?.let { workoutDone ->
                    val doneSetsCount = workoutDone.exercises.sumOf { it.sets.size }
                    val totalSetsPlanned = workoutDone.exercises.sumOf { it.totalSets }
                    
                    _uiState.update {
                        it.copy(
                            workoutName = workoutDone.name,
                            totalExercises = workoutDone.exercises.size,
                            totalSets = totalSetsPlanned,
                            completedSets = doneSetsCount,
                            duration = workoutDone.time,
                            formattedDate = workoutDone.date,
                            isLoading = false,
                            isWorkoutDone = true
                        )
                    }
                }
            } else if (idToLoad != null) {
                getWorkoutByIdUseCase(idToLoad).collect { workout ->
                    workout?.let { workoutSelected ->
                        _uiState.update {
                            it.copy(
                                workoutName = workoutSelected.name,
                                totalExercises = workoutSelected.exercises.size,
                                totalSets = workoutSelected.exercises.sumOf { ex -> ex.sets.size },
                                formattedDate = workoutSelected.date,
                                isLoading = false,
                                isWorkoutDone = false
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Retorna a proporção de séries realizadas vs. planejadas.
     * Útil para componentes que precisam desta informação como referência.
     */
    fun getSetsRatio(): Pair<Int, Int> {
        val state = _uiState.value
        val done = state.completedSets ?: 0
        val total = state.totalSets
        return Pair(done, total)
    }
}
