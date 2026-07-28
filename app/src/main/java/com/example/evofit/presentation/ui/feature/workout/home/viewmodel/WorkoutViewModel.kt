package com.example.evofit.presentation.ui.feature.workout.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.usecase.GetActiveWorkoutSessionUseCase
import com.example.evofit.domain.usecase.GetOnboardingDataUseCase
import com.example.evofit.domain.usecase.GetUserIdUseCase
import com.example.evofit.domain.usecase.GetWorkoutDoneHistoryUseCase
import com.example.evofit.domain.usecase.GetWorkoutsUseCase
import com.example.evofit.domain.usecase.UpdateWorkoutsOrderUseCase
import com.example.evofit.domain.usecase.GetCurrentWeekRangeUseCase
import com.example.evofit.core.common.DateMapper
import com.example.evofit.core.network.ConnectivityObserver
import com.example.evofit.data.local.session.SessionManager
import com.example.evofit.domain.usecase.SyncUserDataUseCase
import com.example.evofit.presentation.model.ActiveSessionUIModel
import com.example.evofit.presentation.model.ExercisePreviewItem
import com.example.evofit.presentation.model.WorkoutHistoryUIModel
import com.example.evofit.presentation.model.WorkoutUIModel
import com.example.evofit.presentation.ui.feature.workout.home.state.WorkoutState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.asStateFlow

@OptIn(FlowPreview::class)
class WorkoutViewModel(
    private val getOnboardingDataUseCase: GetOnboardingDataUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getWorkoutsUseCase: GetWorkoutsUseCase,
    private val updateWorkoutsOrderUseCase: UpdateWorkoutsOrderUseCase,
    private val getWorkoutDoneHistoryUseCase: GetWorkoutDoneHistoryUseCase,
    private val getCurrentWeekRangeUseCase: GetCurrentWeekRangeUseCase,
    private val getActiveWorkoutSessionUseCase: GetActiveWorkoutSessionUseCase,
    private val connectivityObserver: ConnectivityObserver,
    private val sessionManager: SessionManager,
    private val syncUserDataUseCase: SyncUserDataUseCase
) : ViewModel() {

    private val _updateOrderFlow = MutableSharedFlow<List<WorkoutUIModel>>()
    
    private val _isOnline = MutableStateFlow(true)
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    init {
        observeConnectivity()
        viewModelScope.launch {
            _updateOrderFlow
                .debounce(1000L)
                .collect { orderedList ->
                    performUpdateOrder(orderedList)
                }
        }
    }

    private fun observeConnectivity() {
        viewModelScope.launch {
            connectivityObserver.observe().collect { status ->
                val online = status == ConnectivityObserver.Status.Available
                _isOnline.value = online
                if (online) {
                    val userId = sessionManager.userId.firstOrNull()
                    if (userId != null) {
                        syncUserDataUseCase(userId, shouldClearActiveSession = false, isOnline = true)
                    }
                }
            }
        }
    }

    private suspend fun performUpdateOrder(orderedList: List<WorkoutUIModel>) {
        val userId = getUserIdUseCase() ?: return
        val currentWorkouts = getWorkoutsUseCase(userId).first()

        val reorderedWorkouts = orderedList.mapNotNull { uiModel ->
            currentWorkouts.find { it.id == uiModel.id }
        }

        updateWorkoutsOrderUseCase(reorderedWorkouts)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val baseState: Flow<WorkoutState> = getOnboardingDataUseCase()
        .flatMapLatest { userData ->
            val userId = getUserIdUseCase() ?: ""
            val firstName = getFirstName(userData.name)
            if (userId.isEmpty()) {
                flowOf(WorkoutState(userName = firstName))
            } else {
                combine(
                    getWorkoutsUseCase(userId),
                    getWorkoutDoneHistoryUseCase(userId)
                ) { workouts, history ->
                    val startOfWeek = getCurrentWeekRangeUseCase()

                    WorkoutState(
                        userName = firstName,
                        workouts = workouts.map { workout ->
                            WorkoutUIModel(
                                id = workout.id,
                                title = workout.name.ifEmpty { workout.muscleGroupId },
                                exercises = workout.exercises.size,
                                series = workout.exercises.sumOf { it.sets.size }
                            )
                        },
                        totalWorkouts = workouts.size,
                        workoutsThisWeek = history.count {
                            val date = DateMapper.parseDate(it.date)
                            date != null && date.time >= startOfWeek
                        },
                        history = history.map { workoutDone ->
                            WorkoutHistoryUIModel(
                                id = workoutDone.id,
                                name = workoutDone.name,
                                date = workoutDone.date,
                                time = workoutDone.time,
                                exercises = workoutDone.exercises.map { workoutExercise ->
                                    val sets = workoutExercise.sets
                                    val firstSet = sets.firstOrNull()
                                    ExercisePreviewItem(
                                        workoutExerciseId = workoutExercise.id,
                                        name = firstSet?.exerciseName ?: "",
                                        setsCount = sets.size,
                                        weight = sets.maxOfOrNull { it.load } ?: 0.0,
                                        reps = sets.maxOfOrNull { it.reps } ?: 0,
                                        unit = firstSet?.unit ?: com.example.evofit.domain.model.MeasurementUnit.WEIGHT,
                                        time = sets.maxOfOrNull { it.time ?: 0 } ?: 0,
                                        distance = sets.maxOfOrNull { it.distance ?: 0.0 } ?: 0.0
                                    )
                                }
                            )
                        }
                    )
                }
            }
        }

    val uiState: StateFlow<WorkoutState> = combine(
        baseState,
        getActiveWorkoutSessionUseCase()
    ) { state, activeSession ->
        state.copy(
            activeSession = activeSession?.let {
                ActiveSessionUIModel(
                    workoutId = it.workout.id,
                    workoutName = it.workout.name.ifEmpty { it.workout.muscleGroupId }
                )
            }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = WorkoutState()
    )

    fun updateWorkoutOrder(orderedList: List<WorkoutUIModel>) {
        viewModelScope.launch {
            _updateOrderFlow.emit(orderedList)
        }
    }

    private fun getFirstName(fullName: String): String {
        return fullName.trim().split("\\s+".toRegex()).firstOrNull() ?: fullName
    }
}
