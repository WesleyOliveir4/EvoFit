package com.example.evofit.presentation.ui.feature.evo.analytics.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.usecase.GetExercisesWithRecordCountUseCase
import com.example.evofit.domain.usecase.GetMuscleGroupsUseCase
import com.example.evofit.domain.usecase.GetTrainedMuscleGroupsUseCase
import com.example.evofit.domain.usecase.GetUserIdUseCase
import com.example.evofit.domain.usecase.GetWorkoutDoneHistoryUseCase
import com.example.evofit.domain.usecase.ProcessExerciseAnalyticsUseCase
import com.example.evofit.presentation.mapper.toItem
import com.example.evofit.presentation.model.ExerciseWithRecordsUIModel
import com.example.evofit.presentation.ui.feature.evo.analytics.state.AnalyticsChartPoint
import com.example.evofit.presentation.ui.feature.evo.analytics.state.EvoAnalyticsState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EvoAnalyticsViewModel(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getWorkoutDoneHistoryUseCase: GetWorkoutDoneHistoryUseCase,
    private val getTrainedMuscleGroupsUseCase: GetTrainedMuscleGroupsUseCase,
    private val getMuscleGroupsUseCase: GetMuscleGroupsUseCase,
    private val getExercisesWithRecordCountUseCase: GetExercisesWithRecordCountUseCase,
    private val processExerciseAnalyticsUseCase: ProcessExerciseAnalyticsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(EvoAnalyticsState())
    val uiState: StateFlow<EvoAnalyticsState> = _uiState.asStateFlow()

    init {
        loadHistory()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadHistory() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getUserIdUseCase().flatMapLatest { userId ->
                if (userId != null) {
                    val allGroups = getMuscleGroupsUseCase()
                    getWorkoutDoneHistoryUseCase(userId, 500).map { history ->
                        val groups = getTrainedMuscleGroupsUseCase(history, allGroups)
                        history to groups.map { group -> group.toItem() }
                    }
                } else {
                    flowOf(null)
                }
            }.collect { result ->
                if (result != null) {
                    val (history, trainedGroups) = result
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            historyRawData = history,
                            trainedGroups = trainedGroups
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "User not found") }
                }
            }
        }
    }

    fun onMuscleGroupSelected(groupId: String, groupName: String) {
        val exercises = getExercisesWithRecordCountUseCase(groupId, _uiState.value.historyRawData)
        _uiState.update {
            it.copy(
                selectedMuscleGroupId = groupId,
                muscleGroupName = groupName,
                exercisesForSelection = exercises.map { item ->
                    ExerciseWithRecordsUIModel(
                        id = item.exercise.id,
                        name = item.exercise.name,
                        recordsCount = item.recordsCount
                    )
                }
            )
        }
    }

    fun onExerciseSelected(exerciseId: String, exerciseName: String) {
        _uiState.update {
            it.copy(
                selectedExerciseId = exerciseId,
                selectedExerciseName = exerciseName
            )
        }
        
        processExerciseAnalyticsUseCase(exerciseId, _uiState.value.historyRawData)?.let { result ->
            _uiState.update {
                it.copy(
                    unit = result.unit,
                    maxRecord = result.maxRecord,
                    secondaryRecord = result.secondaryRecord,
                    totalSets = result.totalSets,
                    firstRecordDate = result.firstRecordDate,
                    lastRecordDate = result.lastRecordDate,
                    // Mapeando do Domain Model (AnalyticsDataPoint) para o UI Model (AnalyticsChartPoint)
                    loadChartPoints = result.loadChartPoints.map { point ->
                        AnalyticsChartPoint(label = point.label, value = point.value)
                    },
                    volumeChartPoints = result.volumeChartPoints.map { point ->
                        AnalyticsChartPoint(label = point.label, value = point.value)
                    }
                )
            }
        }
    }
}
