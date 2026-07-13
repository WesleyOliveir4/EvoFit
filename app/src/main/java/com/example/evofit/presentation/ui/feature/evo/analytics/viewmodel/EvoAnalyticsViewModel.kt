package com.example.evofit.presentation.ui.feature.evo.analytics.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.usecase.GetExercisesWithRecordCountUseCase
import com.example.evofit.domain.usecase.GetTrainedMuscleGroupsUseCase
import com.example.evofit.domain.usecase.GetUserIdUseCase
import com.example.evofit.domain.usecase.GetWorkoutDoneHistoryUseCase
import com.example.evofit.presentation.ui.feature.evo.analytics.state.AnalyticsChartPoint
import com.example.evofit.presentation.ui.feature.evo.analytics.state.EvoAnalyticsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EvoAnalyticsViewModel(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getWorkoutDoneHistoryUseCase: GetWorkoutDoneHistoryUseCase,
    private val getTrainedMuscleGroupsUseCase: GetTrainedMuscleGroupsUseCase,
    private val getExercisesWithRecordCountUseCase: GetExercisesWithRecordCountUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EvoAnalyticsState())
    val uiState: StateFlow<EvoAnalyticsState> = _uiState.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val userId = getUserIdUseCase()
                if (userId != null) {
                    val history = getWorkoutDoneHistoryUseCase(userId)
                    val groups = getTrainedMuscleGroupsUseCase(history)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            historyRawData = history,
                            trainedGroups = groups
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "User not found") }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error"
                    )
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
                exercisesForSelection = exercises
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
        processExerciseAnalytics(exerciseId)
    }

    private fun processExerciseAnalytics(exerciseId: String) {
        val history = _uiState.value.historyRawData
        
        val filteredWorkouts = history.filter { workout ->
            workout.exercises.any { it.exerciseId == exerciseId }
        }.sortedBy { it.date }

        if (filteredWorkouts.isEmpty()) return

        var maxLoad = 0.0
        var totalSetsCount = 0
        val chartDataLoad = mutableListOf<Pair<String, Double>>()
        val chartDataVolume = mutableListOf<Pair<String, Double>>()

        filteredWorkouts.forEach { workout ->
            val workoutExercise = workout.exercises.first { it.exerciseId == exerciseId }
            val sets = workoutExercise.sets
            
            val workoutMaxLoad = sets.maxOfOrNull { it.load } ?: 0.0
            if (workoutMaxLoad > maxLoad) maxLoad = workoutMaxLoad
            
            totalSetsCount += sets.size
            
            val workoutVolume = sets.sumOf { it.load * it.reps }
            
            chartDataLoad.add(workout.date to workoutMaxLoad)
            chartDataVolume.add(workout.date to workoutVolume)
        }

        val last6Load = chartDataLoad.takeLast(6).map {
            AnalyticsChartPoint(label = formatDateToMonth(it.first), value = it.second.toFloat()) 
        }
        val last6Volume = chartDataVolume.takeLast(6).map { 
            AnalyticsChartPoint(label = formatDateToMonth(it.first), value = it.second.toFloat()) 
        }

        _uiState.update {
            it.copy(
                maxRecord = "${maxLoad.toInt()}kg",
                totalSets = totalSetsCount.toString(),
                firstRecordDate = formatDate(filteredWorkouts.first().date),
                lastRecordDate = formatDate(filteredWorkouts.last().date),
                loadChartPoints = last6Load,
                volumeChartPoints = last6Volume
            )
        }
    }

    private fun formatDateToMonth(dateStr: String): String {
        return try {
            val month = dateStr.split("-")[1]
            when (month) {
                "01" -> "Jan"
                "02" -> "Fev"
                "03" -> "Mar"
                "04" -> "Abr"
                "05" -> "Mai"
                "06" -> "Jun"
                "07" -> "Jul"
                "08" -> "Ago"
                "09" -> "Set"
                "10" -> "Out"
                "11" -> "Nov"
                "12" -> "Dez"
                else -> ""
            }
        } catch (e: Exception) {
            ""
        }
    }

    private fun formatDate(dateStr: String): String {
        return try {
            val parts = dateStr.split("-")
            "${parts[2]}/${parts[1]}/${parts[0]}"
        } catch (e: Exception) {
            dateStr
        }
    }
}
