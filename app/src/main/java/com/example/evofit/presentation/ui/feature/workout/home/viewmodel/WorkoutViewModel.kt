package com.example.evofit.presentation.ui.feature.workout.home.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.model.ActiveWorkoutSession
import com.example.evofit.domain.usecase.GetActiveWorkoutSessionUseCase
import com.example.evofit.domain.usecase.GetOnboardingDataUseCase
import com.example.evofit.domain.usecase.GetUserIdUseCase
import com.example.evofit.domain.usecase.GetWorkoutDoneHistoryUseCase
import com.example.evofit.domain.usecase.GetWorkoutsUseCase
import com.example.evofit.domain.usecase.UpdateWorkoutsOrderUseCase
import com.example.evofit.domain.usecase.GetCurrentWeekRangeUseCase
import com.example.evofit.presentation.mapper.DateMapper
import com.example.evofit.presentation.model.WorkoutUIModel
import com.example.evofit.presentation.ui.feature.workout.home.state.WorkoutState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class WorkoutViewModel(
    private val getOnboardingDataUseCase: GetOnboardingDataUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getWorkoutsUseCase: GetWorkoutsUseCase,
    private val updateWorkoutsOrderUseCase: UpdateWorkoutsOrderUseCase,
    private val getWorkoutDoneHistoryUseCase: GetWorkoutDoneHistoryUseCase,
    private val getCurrentWeekRangeUseCase: GetCurrentWeekRangeUseCase,
    private val getActiveWorkoutSessionUseCase: GetActiveWorkoutSessionUseCase
) : ViewModel() {

    private val _updateOrderFlow = MutableSharedFlow<List<WorkoutUIModel>>()

    init {
        viewModelScope.launch {
            _updateOrderFlow
                .debounce(1000L)
                .collect { orderedList ->
                    performUpdateOrder(orderedList)
                }
        }
    }

    private suspend fun performUpdateOrder(orderedList: List<WorkoutUIModel>) {
        val userId = getUserIdUseCase() ?: return
        val currentWorkouts = getWorkoutsUseCase(userId).first()

        val reorderedWorkouts = orderedList.mapNotNull { uiModel ->
            currentWorkouts.find { it.id.toInt() == uiModel.id }
        }

        updateWorkoutsOrderUseCase(reorderedWorkouts)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val baseState: Flow<WorkoutState> = getOnboardingDataUseCase()
        .flatMapLatest { userData ->
            val userId = getUserIdUseCase() ?: ""
            if (userId.isEmpty()) {
                flowOf(WorkoutState(userName = userData.name))
            } else {
                val history = getWorkoutDoneHistoryUseCase(userId)
                getWorkoutsUseCase(userId).map { workouts ->
                    val startOfWeek = getCurrentWeekRangeUseCase()

                    WorkoutState(
                        userName = userData.name,
                        workouts = workouts.map { workout ->
                            WorkoutUIModel(
                                id = workout.id.toInt(),
                                title = workout.name.ifEmpty { workout.muscleGroupId },
                                exercises = workout.exercises.size,
                                series = workout.exercises.sumOf { it.sets.size },
                                icon = Icons.Default.FitnessCenter
                            )
                        },
                        totalWorkouts = workouts.size,
                        workoutsThisWeek = history.count {
                            val date = DateMapper.parseDate(it.date)
                            date != null && date.time >= startOfWeek
                        },
                        history = history
                    )
                }
            }
        }

    val uiState: StateFlow<WorkoutState> = combine(
        baseState,
        getActiveWorkoutSessionUseCase()
    ) { state, activeSession ->
        state.copy(activeSession = activeSession)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000),
        initialValue = WorkoutState()
    )

    fun updateWorkoutOrder(orderedList: List<WorkoutUIModel>) {
        viewModelScope.launch {
            _updateOrderFlow.emit(orderedList)
        }
    }
}
