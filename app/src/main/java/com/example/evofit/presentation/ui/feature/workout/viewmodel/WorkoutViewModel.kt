package com.example.evofit.presentation.ui.feature.workout.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.repository.OnboardingRepository
import com.example.evofit.domain.usecase.GetWorkoutsUseCase
import com.example.evofit.presentation.ui.feature.workout.components.WorkoutUIModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.util.*

data class WorkoutState(
    val userName: String = "",
    val workouts: List<WorkoutUIModel> = emptyList(),
    val totalWorkouts: Int = 0,
    val workoutsThisWeek: Int = 0
)

class WorkoutViewModel(
    private val onboardingRepository: OnboardingRepository,
    private val getWorkoutsUseCase: GetWorkoutsUseCase
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<WorkoutState> = onboardingRepository.getUserData()
        .flatMapLatest { userData ->
            if (userData == null) {
                flowOf(WorkoutState())
            } else {
                val userId = onboardingRepository.getUserId() ?: ""
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
                                    title = workout.muscleGroupId,
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
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = WorkoutState()
        )
}
