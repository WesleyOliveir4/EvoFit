package com.example.evofit.presentation.ui.feature.workout.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.usecase.GetOnboardingDataUseCase
import com.example.evofit.domain.usecase.GetUserIdUseCase
import com.example.evofit.domain.usecase.GetWorkoutsUseCase
import com.example.evofit.presentation.ui.feature.workout.components.WorkoutUIModel
import com.example.evofit.presentation.ui.feature.workout.state.WorkoutState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.util.*


class WorkoutViewModel(
    private val getOnboardingDataUseCase: GetOnboardingDataUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getWorkoutsUseCase: GetWorkoutsUseCase
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<WorkoutState> = getOnboardingDataUseCase()
        .flatMapLatest { userData ->
            val userId = getUserIdUseCase() ?: ""
            if (userId.isEmpty()) {
                flowOf(WorkoutState(userName = userData.name))
            } else {
                getWorkoutsUseCase(userId).map { workouts ->
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
                    calendar.set(Calendar.HOUR_OF_DAY, 0)
                    calendar.set(Calendar.MINUTE, 0)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                    val startOfWeek = calendar.timeInMillis

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
                        workoutsThisWeek = workouts.count { it.date >= startOfWeek }
                    )
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = WorkoutState()
        )
}
